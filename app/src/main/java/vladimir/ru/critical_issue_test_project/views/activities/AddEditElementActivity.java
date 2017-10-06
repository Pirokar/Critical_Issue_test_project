package vladimir.ru.critical_issue_test_project.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.model.enums.DialogResultsEnum;
import vladimir.ru.critical_issue_test_project.model.events.AlertDialogFinishedEvent;
import vladimir.ru.critical_issue_test_project.utils.BusProvider;
import vladimir.ru.critical_issue_test_project.views.fragments.dialogs.SaveDialog;

/**
 * Created by Vladimir on 17.10.2016.
 * Presents activity
 */

public class AddEditElementActivity extends AppCompatActivity {
    public static final String TEXT_RESULT_KEY = "text_result_key";
    public static final String ELEMENT_POSITION_KEY = "element_position_key";

    EditText editText;
    Button okBtn, cancelBtn;

    public static void openActivityForResult(int elementPosition, int launchCode, Activity activity) {
        Intent intent = new Intent(activity, AddEditElementActivity.class);
        intent.putExtra(ELEMENT_POSITION_KEY, elementPosition);
        activity.startActivityForResult(intent, launchCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        initViews();
        setOkBtnBehavior();
        setCancelBtnBehavior();
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getUIBusInstance().register(this);
    }

    @Override
    protected void onStop() {
        BusProvider.getUIBusInstance().unregister(this);
        super.onStop();
    }

    private void initViews() {
        editText = (EditText)findViewById(R.id.edit_text);
        okBtn = (Button)findViewById(R.id.ok_button);
        cancelBtn = (Button)findViewById(R.id.cancel_button);
    }

    private void setOkBtnBehavior() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnOkResult();
            }
        });
    }

    private void setCancelBtnBehavior() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnCancelResult();
            }
        });
    }

    private void returnOkResult() {
        Intent answerIntent = new Intent();
        answerIntent.putExtra(TEXT_RESULT_KEY, editText.getText().toString());
        answerIntent.putExtra(ELEMENT_POSITION_KEY, getIntent().getIntExtra(ELEMENT_POSITION_KEY, 0));
        setResult(RESULT_OK, answerIntent);
        finish();
    }

    private void returnCancelResult() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(!editText.getText().toString().isEmpty()) {
            SaveDialog.showDialog(getFragmentManager());
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe
    @SuppressWarnings("all")
    public void onDialogFinishedEvent(AlertDialogFinishedEvent event) {
        if(event.getDialogResults() == DialogResultsEnum.OK) {
            returnOkResult();
        } else {
            returnCancelResult();
        }
    }
}
