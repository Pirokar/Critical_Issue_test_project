package vladimir.ru.critical_issue_test_project.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.model.enums.ImageToOpenEnum;
import vladimir.ru.critical_issue_test_project.views.activities.ScalingActivity;

/**
 * Created by Vladimir on 14.10.2016.
 * Presents selection between two options
 */

public class ScalingFragment extends Fragment implements View.OnClickListener {
    ImageButton galleryButton, cameraButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scaling, container, false);

        initViews(view);
        setButtonsOnClickListener();

        return view;
    }

    private void initViews(View view) {
        galleryButton = (ImageButton)view.findViewById(R.id.gallery_button);
        cameraButton = (ImageButton)view.findViewById(R.id.camera_button);
    }

    private void setButtonsOnClickListener() {
        galleryButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.gallery_button) {
            ScalingActivity.openActivity(getContext(), ImageToOpenEnum.GALLERY);
        } else {
            ScalingActivity.openActivity(getContext(), ImageToOpenEnum.CAMERA);
        }
    }
}
