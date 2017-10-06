package vladimir.ru.critical_issue_test_project.model.events;

import android.app.Activity;

import vladimir.ru.critical_issue_test_project.views.activities.AddEditElementActivity;

/**
 * Created by Vladimir on 17.10.2016.
 * This is event for pressing "Add event" in toolbar menu
 */

public class AddElementEvent {
    private Activity activity;

    public AddElementEvent(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
