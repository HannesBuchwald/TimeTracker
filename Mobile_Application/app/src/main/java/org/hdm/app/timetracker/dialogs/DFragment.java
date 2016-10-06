package org.hdm.app.timetracker.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.adapter.ObjectListAdapter;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.ActivityListOnClickListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hannes on 03.06.2016.
 */
public class DFragment extends DialogFragment implements ActivityListOnClickListener{


    private ActivityObject activityObject;
    private ObjectListAdapter objectAdapter;
    private RecyclerView recyclerView;


    public Variables var = Variables.getInstance();
    public DataManager dataManager = DataManager.getInstance();


    View rootView;

    public DFragment(){}


    public DFragment(ActivityObject activityObject) {
        this.activityObject = activityObject;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_activity, container,
                false);


        initLayout();


        return rootView;
    }





    private void initLayout() {

        getDialog().setTitle("Choose Portion of Food");

        objectAdapter = new ObjectListAdapter((List) new ArrayList<>(dataManager.getPlateMap().keySet()));
        objectAdapter.setListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_listt);
        recyclerView.setAdapter(objectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                var.listRows, StaggeredGridLayoutManager.VERTICAL));



    }

    @Override
    public void didClickOnActivityListItem(String title, View_Holder holder) {

    }

    @Override
    public void didLongClickOnActivityListItem(String title, View_Holder view_holder) {

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