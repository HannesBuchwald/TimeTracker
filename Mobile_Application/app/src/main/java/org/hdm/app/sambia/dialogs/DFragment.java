package org.hdm.app.sambia.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.sambia.R;
import org.hdm.app.sambia.datastorage.ActivityObject;

/**
 * Created by Hannes on 03.06.2016.
 */
public class DFragment extends DialogFragment {


    private ActivityObject activityObject;

    public DFragment(){}


    public DFragment(ActivityObject activityObject) {
        this.activityObject = activityObject;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_activity, container,
                false);
//        getDialog().setTitle("DialogFragment Tutorial");
        // Do something else
        return rootView;
    }


//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new AlertDialog.Builder(getActivity())
//                // Set Dialog Icon
////                .setIcon(R.drawable.shovel)
//                // Set Dialog Title
////                .setTitle("Alert DialogFragment")
//                // Set Dialog Message
////                .setMessage("Alert DialogFragment Tutorial")
//
//                // Positive button
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do something else
//                    }
//                })
//
//                // Negative Button
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,	int which) {
//                        // Do something else
//                    }
//                }).create();
//    }


}