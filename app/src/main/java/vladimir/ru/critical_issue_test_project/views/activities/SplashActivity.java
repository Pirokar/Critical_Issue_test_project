package vladimir.ru.critical_issue_test_project.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import vladimir.ru.critical_issue_test_project.views.activities.MainActivity;

/**
 * Created by Vladimir on 13.10.2016.
 * Starts with app
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
