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
import org.hdm.app.timetracker.datastorage.ActiveObject;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.Stamp;
import org.hdm.app.timetracker.listener.DialogPortionListOnClickListener;
import org.hdm.app.timetracker.screens.FragmentActivity;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Hannes on 03.06.2016.
 */
public class DialogPortionFragment extends DialogFragment implements DialogPortionListOnClickListener, View.OnLongClickListener {


    private static final String TAG = "DialogPortionFragment";
    private static final String TITLE = "Choose a Portion";
    private final ActiveObject activeObject;

    private DialogPortionListAdapter portionAdapter;
    private RecyclerView recyclerView;
    private boolean clickFlag;
    private Stamp stamp;

    public Variables var = Variables.getInstance();
    public DataManager dataManager = DataManager.getInstance();
    private View view;
    private FragmentActivity fragmentActivity;

    private Button btnDialogFood;

    private String activePortion = "";




    public DialogPortionFragment(FragmentActivity fragmentActivity, Stamp stamp, ActiveObject startTime) {
        this.stamp = stamp;
        this.fragmentActivity = fragmentActivity;
        this.activeObject = startTime;
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
        if(!clickFlag) {
            Log.d(TAG, "onPause DialogFragment" );
            dataManager.addToLogMap(activeObject.startTime, stamp);
            dataManager.addToCalendarMap(activeObject);
            if (var.editable) fragmentActivity.flip();
        }
        if (this != null) this.dismiss();
        clickFlag = false;
    }



    private void initLayout() {

        getDialog().setTitle(TITLE);

        portionAdapter = new DialogPortionListAdapter(
                (List) new ArrayList<>(dataManager.getPortionMap().keySet()),
                activePortion);

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



    /**
     * Listen on the Activity Click
     * @param id
     */
    @Override
    public void didClickOnPortionListItem(String id) {
    }


    /**
     * Listen on the Activity Click
     * @param id
     */
    @Override
    public void didLongClickOnPortionListItem(String id) {
        handleObjectClick(id);
    }


    /**
     * Listen on the OK Button Click
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {

        // pass onto next Dialog
        clickFlag = true;
        DialogFoodFragment dFoodFragment = new DialogFoodFragment(fragmentActivity, stamp, activeObject);
        FragmentManager fm = getFragmentManager();
        dFoodFragment.show(fm, "Food Fragment");
        resetPortionItemState();
        this.dismiss();
        return true;
    }



    private void handleObjectClick(String id) {
        Log.d(TAG, "PortionClick " + id);
        // get clicked PortionObject
        ActivityObject portionObject = dataManager.portionMap.get(id);

        // Add or delete clicked Activity
        if(activePortion.equals(id)) {
            activePortion = "";
            stamp.f01_portion = "";
            btnDialogFood.setVisibility(View.GONE);
        } else {
            activePortion = id;
            stamp.f01_portion = portionObject.getTitle();
            btnDialogFood.setVisibility(View.VISIBLE);
        }

        updateAdapter();
    }



    /**
     *     Update Adapter
     */
    private void updateAdapter() {
        portionAdapter.activePortion = activePortion;
        portionAdapter.notifyDataSetChanged();
    }


    private void resetPortionItemState() {
        activePortion = "";
        portionAdapter.activePortion = "";
    }
}