package org.hdm.app.sambia.screens;

/**
 * Created by Hannes on 13.05.2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.sambia.R;
import org.hdm.app.sambia.datastorage.ActivityObject;
import org.hdm.app.sambia.datastorage.DataManager;
import org.hdm.app.sambia.listener.ActiveActivityListOnClickListener;
import org.hdm.app.sambia.listener.ActivityListOnClickListener;
import org.hdm.app.sambia.adapter.ObjectListAdapter;
import org.hdm.app.sambia.adapter.ActiveListAdapter;
import org.hdm.app.sambia.util.View_Holder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static org.hdm.app.sambia.util.Consts.*;


/**
 * This fragment representing the front of the card.
 */
public class FragmentActivity extends BaseFragemnt implements
        ActivityListOnClickListener,
        ActiveActivityListOnClickListener {


    private final String TAG = "FragmentActivity";


    private View view;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView_activeData;

    private ObjectListAdapter objectAdapter;
    private ActiveListAdapter activeAdapter;

    private Date startDate;
    private long countt;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_activitys, container, false);
        initMenu(view);
        initActiveList();
        initObjectList();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateActiveList();
        updateObjectList();
        editableMode();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /*******************
     * Life Cycle Ende
     ***********************/


    private void initActiveList() {

        activeAdapter = new ActiveListAdapter(new ArrayList<>(dataManager.activeList));
        activeAdapter.setListener(this);
        recyclerView_activeData = (RecyclerView) view.findViewById(R.id.rv_active);
        recyclerView_activeData.setAdapter(activeAdapter);
        recyclerView_activeData.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_activeData.setLayoutManager(new StaggeredGridLayoutManager(
                var.activeListRow, StaggeredGridLayoutManager.HORIZONTAL));
        Log.d(TAG, "size init Activity List");
    }



    private void initObjectList() {

        objectAdapter = new ObjectListAdapter((List) new ArrayList<>(dataManager.getObjectMap().keySet()));
        objectAdapter.setListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        recyclerView.setAdapter(objectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                var.activityListRows, StaggeredGridLayoutManager.VERTICAL));
    }


    /*******************
     * Init Ende
     ***********************/


    // Listener from the ActiveActivityObjectList
    @Override
    public void didOnClickActivityList(String title, View_Holder holder) {
        didClickOnActivityListItem(title, null);
    }

    List<Timer> timerList = new ArrayList<>();



    // Listener from the ActivityObjectList
    @Override
    public void didClickOnActivityListItem(String title, View_Holder holder) {


        // Get the DataObject which was clicked
        // there are all information stored about the activity object
        // state, names image ect.
        ActivityObject activityObject = dataManager.getActivityObject(title);
        // If editable Mode - than add activity to selectedTime in CalendearList
        if (var.editable) {
            dataManager.setCalenderMapEntry(var.selectedTime, activityObject.title);
            listener.flip();
        } else {

            // when Activity is not active
            if (!activityObject.activeState) {

                // ToDo Show DialogFragment  -  not at the Moment had to bee discussed in Africa
//            if(list.get(position).sub_category && list.get(position).activeState) {
//                DFragment dFragment = new DFragment(dsata);
//                FragmentManager fm = fr.getFragmentManager();
//                dFragment.show(fm, "Dialog Fragment");
//            }

                // Set State to active
                activityObject.activeState = true;

                // set temporary start time
                activityObject.startTime = Calendar.getInstance().getTime();

                // Count how many activity are active
                var.activeCount++;

                dataManager.activeList.add(activityObject.title);


            } else {

                // Deactivate Activity
                activityObject.activeState = false;
                activityObject.count = 0;

                // set temporary end time
                activityObject.endTime = Calendar.getInstance().getTime();


                Calendar aa = Calendar.getInstance();
                        aa.setTimeZone(TimeZone.getTimeZone("MESZ"));
                Log.d(TAG, "new Time " + aa.getTime());
                Log.d(TAG, "new Time " + aa.getTimeZone().toString());

                Calendar ca = Calendar.getInstance();
                ca.setTimeZone(TimeZone.getTimeZone("UTC"));

                Log.d(TAG, "new Time " + ca.getTime());
                //Count how many activitys are active
                var.activeCount--;

                // ToDo Implement Function when Activity is not longer than 1 Minute active than do not count
                // add ActivityObject to CalendarContentList
                addActivtyObjectForCalenderContent(activityObject);

                // Save Time and subCategory in Dsata
                activityObject.saveTimeStamp();

                dataManager.activeList.remove(activityObject.title);
            }
        }

        // Store edited ActivityObject back in DataManager
        dataManager.setActivityObject(activityObject);



        // Set Background if pressed from AdapterList
        if (holder != null) {
            holder.count = activityObject.count;
            holder.setBackground(activityObject.activeState);
        }



        updateActiveList();
        if(holder == null) objectAdapter.notifyItemChanged(objectAdapter.list.indexOf(activityObject.title));


        // get activeMap look into and for every entry add to
        if (DEBUGMODE) {
            Log.d(TAG, "aaa "
                    + activityObject.title + " "
                    + activityObject.activeState + " "
                    + activityObject.timeFrameList.size());
        }
    }

    @Override
    public void didLongClickOnView(String title, View_Holder view_holder) {
        didClickOnActivityListItem(title,view_holder);
    }


    private void addActivtyObjectForCalenderContent(ActivityObject activityObject) {

        Date startTime = activityObject.startTime;
        int startMin = startTime.getMinutes();

        Date firstDate = activityObject.startTime;
        int firstMin;



        Calendar calTimeSlot = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();


        // find current TimeSlot
        if (startMin < 15) {
            firstMin = 0;
        } else if (startMin < 30) {
            firstMin = 15;
        } else if (startMin < 45) {
            firstMin = 30;
        } else {
            firstMin = 45;
        }


        // Set Current TimeSlot
        firstDate.setSeconds(0);
        firstDate.setMinutes(firstMin);


        if (DEBUGMODE) Log.d(TAG, "c "
                + firstDate + " || s "
                + activityObject.startTime);



        // Debugging
        if (DEBUGMODE) {
//            ArrayList<String> list = dataManager.getCalendarMap().get(firstDate.toString());
//            cal.add(Calendar.HOUR, -1);
//            startTime = cal.getTime();
//
//            calTimeSlot.setTime(firstDate);
//            calTimeSlot.add(Calendar.HOUR, -1);
//            firstDate = calTimeSlot.getTime();
        }


        while(startTime.before(currentDate)){
            dataManager.setCalenderMapEntry(firstDate.toString(), activityObject.title);

            // Count Start + 15 min
            cal.setTime(startTime);
            cal.add(Calendar.MINUTE, 15);
            startTime = cal.getTime();

            // Count FirstDate + 15 min
            calTimeSlot.setTime(firstDate);
            calTimeSlot.add(Calendar.MINUTE, 15);
            firstDate = calTimeSlot.getTime();
        }
    }






    // In this mode the user only sees a list of activitys
    // when he selects one than screens flip back to calendar screen
    private void editableMode() {

        if (var.editable) {
            menuView.setVisibility(View.GONE);
            recyclerView_activeData.setVisibility(View.GONE);
        } else {
            menuView.setVisibility(View.VISIBLE);
            recyclerView_activeData.setVisibility(View.VISIBLE);

            setMenuTitle("Activity");
            setMenuBackground(android.R.color.holo_green_dark);
            setMenuBtn(R.drawable.ic_forward);
        }
    }



    // load edited List and update ActivityObjectListAdapter
    public void updateObjectList() {
        objectAdapter.list = new ArrayList<>(dataManager.getObjectMap().keySet());
        objectAdapter.notifyDataSetChanged();
    }

    // load edited List and update activeActivityObjectListAdapter
    public void updateActiveList() {
        activeAdapter.list = dataManager.activeList;
        Log.d(TAG, "size " + timerList.size());
        activeAdapter.notifyDataSetChanged();
    }
}