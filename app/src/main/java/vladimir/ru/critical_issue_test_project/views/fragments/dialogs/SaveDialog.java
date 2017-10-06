package vladimir.ru.critical_issue_test_project.views.fragments.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import vladimir.ru.critical_issue_test_project.R;
import vladimir.ru.critical_issue_test_project.model.enums.DialogResultsEnum;
import vladimir.ru.critical_issue_test_project.model.events.AlertDialogFinishedEvent;
import vladimir.ru.critical_issue_test_project.utils.BusProvider;

/**
 * Created by Vladimir on 18.10.2016.
 * Shows notification when user try to back with unsaved data
 */

public class SaveDialog extends DialogFragment {
    public static void showDialog(FragmentManager fragmentManager) {
        SaveDialog dialog = new SaveDialog();
        dialog.show(fragmentManager, "saveDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.save_changes_title));
        builder.setMessage(getString(R.string.save_changes_text));
        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BusProvider.getUIBusInstance().post(new AlertDialogFinishedEvent(DialogResultsEnum.OK));
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BusProvider.getUIBusInstance().post(new AlertDialogFinishedEvent(DialogResultsEnum.CANCEL));
            }
        });
        builder.setCancelable(true);
        return builder.create();
    }
}
