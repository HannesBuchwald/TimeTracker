package org.hdm.app.timetracker.dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.adapter.DialogPortionListAdapter;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.DialogPortionListOnClickListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hannes on 03.06.2016.
 */
public class DialogPortionFragment extends DialogFragment implements DialogPortionListOnClickListener, View.OnLongClickListener {


    private static final String TAG = "DialogPortionFragment";
    private ActivityObject activityObject;
    private DialogPortionListAdapter portionAdapter;
    private RecyclerView recyclerView;


    public Variables var = Variables.getInstance();
    public DataManager dataManager = DataManager.getInstance();


    View view;
    private Button btnDialogFood;
    private String lastSelectedItem = "";


    public DialogPortionFragment() {
    }


    public DialogPortionFragment(ActivityObject activityObject) {
        this.activityObject = activityObject;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_food, container,
                false);
        initLayout();

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        resetPortionItemState();
        if (this != null) this.dismiss();
    }


    private void initLayout() {

        getDialog().setTitle("Choose a Portion");

        portionAdapter = new DialogPortionListAdapter((List) new ArrayList<>(dataManager.getPortionMap().keySet()));
        portionAdapter.setListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_listt);
        recyclerView.setAdapter(portionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                var.listRows, StaggeredGridLayoutManager.VERTICAL));

        btnDialogFood = (Button) view.findViewById(R.id.btn_dialog);
        btnDialogFood.setVisibility(View.GONE);
        btnDialogFood.setOnLongClickListener(this);
    }


    @Override
    public void didClickOnPortionListItem(String title, View_Holder holder) {

    }

    @Override
    public void didLongClickOnPortionListItem(String title, View_Holder view_holder) {
        Log.d(TAG, "click on Dialog " + title);

        handleObjectClick(title, view_holder);
    }

    private void handleObjectClick(String title, View_Holder view_holder) {

        // get clicked PortionObject
        ActivityObject portionObject = dataManager.getPortionObject(title);
        Log.d(TAG, "activState " + portionObject.activeState + " portion " + activityObject.portion);


        if (portionObject.activeState) {
            portionObject.activeState = false;
        } else {
            portionObject.activeState = true;

            // save portion to activityObject
            activityObject.portion = portionObject.title;

            if (!portionObject.title.equals(lastSelectedItem)) {

                ActivityObject lastObject = dataManager.getPortionObject(lastSelectedItem);

                if (lastObject != null) {

                    lastObject.activeState = false;
                    dataManager.setPortionObject(lastObject);
                    portionAdapter.notifyItemChanged(portionAdapter.list.indexOf(lastSelectedItem));
                }
            }

            lastSelectedItem = portionObject.title;
        }


        // set Background to green
        view_holder.setBackground(portionObject.activeState);

        if (portionObject.activeState) {
            btnDialogFood.setVisibility(View.VISIBLE);
        } else {
            btnDialogFood.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onLongClick(View v) {

        // pass onto next Dialog
        DialogFoodFragment dFoodFragment = new DialogFoodFragment(activityObject);
        FragmentManager fm = getFragmentManager();
        dFoodFragment.show(fm, "Food Fragment");
        resetPortionItemState();
        this.dismiss();
        return true;
    }

    private void resetPortionItemState() {
        LinkedHashMap<String, ActivityObject> portionMap = dataManager.getPortionMap();
        for (Map.Entry<String, ActivityObject> entry : portionMap.entrySet()) {
            entry.getValue().activeState = false;
        }
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