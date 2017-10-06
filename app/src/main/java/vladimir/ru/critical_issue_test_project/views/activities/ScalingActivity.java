package vladimir.ru.critical_issue_test_project.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.provider.MediaStore;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.model.enums.ImageToOpenEnum;

/**
 * Created by Vladimir on 16.10.2016.
 * Presents activity with picture scaling
 */

public class ScalingActivity extends Activity implements OnTouchListener {
    private static final String IMAGE_TO_OPEN_KEY = "image_to_open_key";
    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 4321;
    private static final int REQUEST_IMAGE_CAPTURE = 3241;

    ImageView imageView;
    ImageButton plusButton, minusButton;

    ImageToOpenEnum imageToOpen;

    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float[] lastEvent = null;
    float newRot = 0f;


    public static void openActivity(Context context, ImageToOpenEnum imageToOpen) {
        Intent intent = new Intent(context, ScalingActivity.class);
        intent.putExtra(IMAGE_TO_OPEN_KEY, ImageToOpenEnum.resolve(imageToOpen));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaling);

        initViews();
        imageToOpen = ImageToOpenEnum.resolve(getIntent()
                .getIntExtra(IMAGE_TO_OPEN_KEY, ImageToOpenEnum.resolve(ImageToOpenEnum.GALLERY)));
        getImage();

        imageView.setOnTouchListener(this);
        setZoomInButtonBehavior();
        setZoomOutButtonBehavior();
    }

    private void initViews() {
        imageView = (ImageView)findViewById(R.id.scaling_image);
        plusButton = (ImageButton)findViewById(R.id.plus_button);
        minusButton = (ImageButton)findViewById(R.id.minus_button);
    }

    private void getImage() {
        switch (imageToOpen) {
            case GALLERY: {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, RESULT_LOAD_IMAGE_FROM_GALLERY);
                break;
            }
            case CAMERA: {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (requestCode == RESULT_LOAD_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bitmap = decodeSampledBitmapFromFile(picturePath, width, height);
                imageView.setImageBitmap(bitmap);
                centerBitmapAtHolder();
                cursor.close();
            }
        } else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), width, height);
            imageView.setImageBitmap(bitmap);
            centerBitmapAtHolder();
        } else {
            finish();
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    private void setZoomInButtonBehavior() {
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float x = imageView.getScaleX();
                float y = imageView.getScaleY();
                imageView.setScaleX(x*1.1f);
                imageView.setScaleY(y*1.1f);
            }
        });
    }

    private void setZoomOutButtonBehavior() {
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float x = imageView.getScaleX();
                float y = imageView.getScaleY();
                imageView.setScaleX(x*0.9f);
                imageView.setScaleY(y*0.9f);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // handle touch events here
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            }
            case MotionEvent.ACTION_UP: {}
            case MotionEvent.ACTION_POINTER_UP: {
                mode = NONE;
                lastEvent = null;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 3) {
                        newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];
                        float xc = (view.getWidth() / 2) * sx;
                        float yc = (view.getHeight() / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                break;
            }
        }

        view.setImageMatrix(matrix);

        return true;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     */
    private float rotation(MotionEvent motionEvent) {
        double delta_x = (motionEvent.getX(0) - motionEvent.getX(1));
        double delta_y = (motionEvent.getY(0) - motionEvent.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private void centerBitmapAtHolder() {
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Drawable drawable = imageView.getDrawable();
                Rect rectDrawable = drawable.getBounds();
                float leftOffset = (imageView.getMeasuredWidth() - rectDrawable.width()) / 2f;
                float topOffset = (imageView.getMeasuredHeight() - rectDrawable.height()) / 2f;

                matrix = imageView.getImageMatrix();
                matrix.postTranslate(leftOffset, topOffset);
                imageView.setImageMatrix(matrix);
                imageView.invalidate();
            }
        });
    }
}
