package vladimir.ru.critical_issue_test_project.model.events;

import vladimir.ru.critical_issue_test_project.model.enums.DialogResultsEnum;

/**
 * Created by Vladimir on 18.10.2016.
 * Needs to notify about dialog results
 */

public class AlertDialogFinishedEvent {
    private DialogResultsEnum dialogResults;

    public AlertDialogFinishedEvent(DialogResultsEnum dialogResults) {
        this.dialogResults = dialogResults;
    }

    public DialogResultsEnum getDialogResults() {
        return dialogResults;
    }
}
