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
 * Show about program dialog
 */

public class AboutDialog extends DialogFragment {
    public static void showDialog(FragmentManager fragmentManager) {
        AboutDialog dialog = new AboutDialog();
        dialog.show(fragmentManager, "aboutDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getString(R.string.developer)
                + "\n\n" + getString(R.string.phone_number) + ": +7787777878"
                + "\nSkype: skype"
                + "\nEmail: email";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.about));
        builder.setMessage(message);
        builder.setCancelable(true);

        return builder.create();
    }
}
