package com.gzr7702.movieguide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Used to display dialog with individual review
 */

public class ReviewFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Intent intent = getActivity().getIntent();
        String review = intent.getExtras().getParcelable("review");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(review)
                // TODO: change "cancel" to string
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO: cancel the dialog
                    }
                }).show();
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
