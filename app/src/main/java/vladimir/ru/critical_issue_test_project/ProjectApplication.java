package vladimir.ru.critical_issue_test_project;

import android.app.Application;

import vladimir.ru.critical_issue_test_project.helpers.LocaleHelper;

/**
 * Created by Vladimir on 14.10.2016.
 * Needs to apply some global things
 */

public class ProjectApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setDefaultLang();
    }

    private void setDefaultLang() {
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));
    }
}
