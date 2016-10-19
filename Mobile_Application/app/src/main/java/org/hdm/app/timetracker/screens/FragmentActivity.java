package org.hdm.app.timetracker.screens;

/**
 * Created by Hannes on 13.05.2016.
 */

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.Stamp;
import org.hdm.app.timetracker.dialogs.DialogPortionFragment;
import org.hdm.app.timetracker.listener.ActiveActivityListOnClickListener;
import org.hdm.app.timetracker.listener.ActivityListOnClickListener;
import org.hdm.app.timetracker.adapter.ObjectListAdapter;
import org.hdm.app.timetracker.adapter.ActiveListAdapter;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static org.hdm.app.timetracker.util.Consts.*;


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



    private boolean externalWork;
    private int shortClickCounter = Variables.getInstance().shortClickCounter;
    private String currentShortClickTitle = "";
    private Runnable updateRemainingTimeRunnable;
    private Timer tmr;
    private Handler handler = new Handler();

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
        addActiveActivitiesToCalenderList();

        if(DEBUGMODE) Log.d(TAG, "on Pause");
        activeAdapter.stopCounting();
        stopCounting();
    }


    /*******************
     * Life Cycle Ende
     ***********************/


    private void initActiveList() {

        activeAdapter = new ActiveListAdapter(dataManager.activeList);
        activeAdapter.setListener(this);
        recyclerView_activeData = (RecyclerView) view.findViewById(R.id.rv_active);
        recyclerView_activeData.setAdapter(activeAdapter);
        recyclerView_activeData.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_activeData.setLayoutManager(new StaggeredGridLayoutManager(
                var.activeListRow, StaggeredGridLayoutManager.HORIZONTAL));
        if(DEBUGMODE) Log.d(TAG, "size init Activity List");
    }


    private void initObjectList() {

        objectAdapter = new ObjectListAdapter((List) new ArrayList<>(dataManager.getObjectMap().keySet()));
        objectAdapter.setListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        recyclerView.setAdapter(objectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                var.listRows, StaggeredGridLayoutManager.VERTICAL));
    }


    /*******************
     * Init Ende
     ***********************/


    // Listener from the ActiveActivityObjectList
    @Override
    public void didOnClickOnActiveListItem(String title, View_Holder holder) {
        if(DEBUGMODE) Log.d(TAG, "titlelll " + title);
        handleShortClick(title, null);
    }

    @Override
    public void didOnLongClickOnActiveListItem(String title, View_Holder holder) {
        handleLongClick(title, null);
    }


    /**
     * Listener from the ActivityObjectList
     * Handled the click on an Activity
     */
    @Override
    public void didClickOnActivityListItem(String title, View_Holder holder) {
        if(DEBUGMODE) Log.d(TAG, "did click on View");
        handleShortClick(title, holder);
    }

    @Override
    public void didLongClickOnActivityListItem(String title, View_Holder view_holder) {
        handleLongClick(title, view_holder);
    }


    private void handleShortClick(String title, View_Holder holder) {

        if(DEBUGMODE) Log.d(TAG, "title1 " + title + " " + currentShortClickTitle + " " + shortClickCounter);


        if (title.equals(currentShortClickTitle)) shortClickCounter--;
        currentShortClickTitle = title;
        if(DEBUGMODE) Log.d(TAG, "title3 " + title + " " + currentShortClickTitle + " " + shortClickCounter);


        if (shortClickCounter <= 1) {
            shortClickCounter = var.shortClickCounter;
            currentShortClickTitle = "";


            // is clicked from ActiveListItem
            if (holder == null) {
                handleLongClick(title, holder);
                return;
            }

            ActivityObject object = dataManager.getActivityObject((String) holder.title.getText());

            if(DEBUGMODE) Log.d(TAG, "title4 " + object.externalWork);


            if (object.externalWork != null) {

                if (object.externalWork.equals("Yes") && !object.activeState) {

                    externalWork = true;
                    object.service = "Yes";
                    dataManager.setActivityObject(object);
                    if (!object.activeState && holder != null) holder.setBackground("blue");
                    handleLongClick(title, holder);

                } else if (object.externalWork.equals("Yes") && object.activeState) {
                    handleLongClick(title, holder);
                }

            }

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shortClickCounter = var.shortClickCounter;
                currentShortClickTitle = "";
            }
        }, var.shortClickCounterResetTime);


    }


    private void handleLongClick(String title, View_Holder holder) {


        if(DEBUGMODE) Log.d(TAG, " titllte " + title + " " + holder + var.editable);

        // If edditable Mode true - than add activity to selectedTime in CalendearList
        if (var.editable) {
            handelEditableActivity(title);
            return;
        }

        // Get the DataObject which was clicked
        // there are all information stored about the activity object
        // state, names image ect.
        ActivityObject activityObject = dataManager.getActivityObject(title);

        // when Activity is not active
        if (!activityObject.activeState && var.activeCount < var.maxRecordedActivity) {


            // Set State to active
            activityObject.activeState = true;

            // set temporary start time
            activityObject.startTime = Calendar.getInstance().getTime();
            if (DEBUGMODE) Log.d(TAG, "activityObject " + activityObject.startTime);

            // Only for testing porpuse
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.DAY_OF_MONTH, -2);
//            cal.add(Calendar.HOUR, -2);
//            cal.add(Calendar.MONTH, -9);
//            cal.add(Calendar.MINUTE, -59);
//            cal.add(Calendar.SECOND, -55);
//            activityObject.startTime = cal.getTime();

            if (DEBUGMODE) Log.d(TAG, "activityObject " + activityObject.startTime);


            if (!externalWork) activityObject.service = "No";


            // Count how many activity are active
            var.activeCount++;

            // Add Activity to activeList
            dataManager.activeList.add(activityObject.title);

        } else if (activityObject.activeState) {


            // Deactivate Activity
            activityObject.activeState = false;
            activityObject.count = 0;

            // set temporary end time
            activityObject.endTime = Calendar.getInstance().getTime();

            activityObject.author = "User";

            //Count how many activities are active
            var.activeCount--;

            Date start = activityObject.startTime;
            Date end = activityObject.endTime;

            if(DEBUGMODE) Log.d(TAG, "startTimee " + activityObject.startTime);


//                if ((end.getTime() - start.getTime())/10000f > var.minRecordingTime) {
//                    // add ActivityObject to CalendarContentList
//                }

            addActivityObjectToCalendarList(activityObject, activityObject.startTime);

            if (activityObject.title.equals("Eating + Drinking")) {

                DialogPortionFragment dFragment = new DialogPortionFragment(activityObject);
                FragmentManager fm = getFragmentManager();
                dFragment.show(fm, "Dialog Fragment");

            } else {
                // Save Timestamp and SubCategory in ActivityObject
                saveStateToLogList(activityObject);
                activityObject.saveTimeStamp("uuser");
            }

            dataManager.activeList.remove(activityObject.title);
        }


        // Store edited ActivityObject back in DataManager
//        if(!activityObject.title.equals("01"))
        dataManager.setActivityObject(activityObject);


        // Set Background if pressed from AdapterList
        if (holder != null) {
            holder.count = activityObject.count;
            if (!externalWork) holder.setBackground(activityObject.activeState);
            if (externalWork && !activityObject.activeState)
                holder.setBackground(activityObject.activeState);
//            holder.handleTimeCounter(activityObject.activeState);
            externalWork = false;
        }

        updateActiveList();

        /**
         * If Activity selected from ActiveList (disable Activity)
         * than notify the corresponding ActivityObject in the ActivityList
         */
        if (holder == null) {
            objectAdapter.notifyItemChanged(objectAdapter.list.indexOf(activityObject.title));
        }
    }


    private void saveStateToLogList(ActivityObject activityObject) {

        Stamp stamp = new Stamp();
        stamp.a03_userID = Variables.getInstance().user_ID;
        stamp.a01_activity = activityObject.title;

        stamp.c01_contract_work = activityObject.service;
        stamp.a06_author = activityObject.author;
        stamp.a07_delete = "No";

//        int year = 1900 + activityObject.startTime.getYear();
//        int month = activityObject.startTime.getMonth()+1;
//        int day = activityObject.startTime.getDate();
        String date =  activityObject.startTime.toString();
        stamp.b01_time_date = date.substring(0,10) + " " + date.substring(date.length()-4);;
        stamp.b02_time_start = date.substring(11,19);
        stamp.b03_time_end = activityObject.endTime.toString().substring(11,19);

        long ms = activityObject.endTime.getTime() - activityObject.startTime.getTime();
        stamp.b04_time_sum = String.valueOf((ms/1000)/60);

        if(stamp.a07_delete.equals("Yes")) stamp.b04_time_sum = "-" + stamp.b04_time_sum;


        dataManager.logList.add(stamp);
    }


    /**
     * This function handle the onClick of an Activity when the Activity
     * will add manually to specific time in CalendarList.
     * If the activity is already added to the CalendarLsit
     * - than the activity will not be added and the timestamp will not be saved
     *
     * @param title Name from the selected Activity
     */
    private void handelEditableActivity(String title) {

        // Get the DataObject which was clicked
        // there are all information stored about the activity object
        // state, names image ect.
        ActivityObject activityObject = new ActivityObject();
        activityObject = dataManager.getActivityObject(title);


        String startTime = var.selectedTime;
        int startDay = Integer.parseInt(startTime.substring(8, 10));
        int startHour = Integer.parseInt(startTime.substring(11, 13));
        int startMin = Integer.parseInt(startTime.substring(14, 16));

        // StartTime
        Date startDate = Calendar.getInstance().getTime();
        startDate.setDate(startDay);
        startDate.setHours(startHour);
        startDate.setMinutes(startMin);
        startDate.setSeconds(00);


        // EndTime
        Calendar endT = Calendar.getInstance();
        endT.setTime(startDate);
        endT.add(Calendar.MINUTE, var.timeFrame);
        Date endDate = endT.getTime();



        String titlee = activityObject.title;

        if (externalWork) {
            titlee = "Y___"+titlee;
        } else {
            titlee = "N___"+titlee;
        }

        // add ActivityObject to CalenderList
        boolean add = dataManager.setActivityToCalendarList(var.selectedTime, titlee);

        if (add) {
            // Add the TimeStamp to the ArrayList in the Activity Object
            if (externalWork) {
                activityObject.service = "Yes";
            } else {
                activityObject.service = "No";
            }

            activityObject.startTime = startDate;
            activityObject.endTime = endDate;
            activityObject.author = "Admin";
            saveStateToLogList(activityObject);
//            activityObject.saveTimeStamp("admin", startDate, endDate);
//            dataManager.setActivityObject(activityObject);
        }

        externalWork = false;
        // Flip back the view to CalendarView
        listener.flip();
    }

    private void addActivityObjectToCalendarList(ActivityObject object, Date startTime) {

        String title;


        if(object.service.contains("Yes")) {
            title = "Y___"+object.title;
        }else {
            title = "N___"+object.title;
        }

        // find current TimeSlot
        int startMin = startTime.getMinutes();
        int firstMin = 0;
        if (startMin > var.timeFrame) firstMin = var.timeFrame;


        Calendar calFirstTimeSlot = Calendar.getInstance();
        calFirstTimeSlot.setTime(startTime);
        Date firstDate = calFirstTimeSlot.getTime();
        // Set Current TimeSlot
        firstDate.setSeconds(0);
        firstDate.setMinutes(firstMin);


        Calendar cal = Calendar.getInstance();
        // cal.add(Calendar.HOUR, 2); // for Testing purpouse
        Date currentDate = cal.getTime();


        if(DEBUGMODE) Log.d(TAG, "time1 " + startTime);


        long diff = currentDate.getTime() - startTime.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;

        if (minutes >= var.minRecordingTime) {


            // Store Activity in TimeSlot from CalendarList
            dataManager.setActivityToCalendarList(firstDate.toString(), title);

            if (DEBUGMODE) Log.d(TAG, "time2; " + startTime + " || startTime; " + firstDate);


            calFirstTimeSlot.setTime(firstDate);
            calFirstTimeSlot.add(Calendar.MINUTE, var.timeFrame);
            firstDate = calFirstTimeSlot.getTime();

            if (DEBUGMODE) Log.d(TAG, "time3; " + startTime + " || startTime; " + firstDate);

            while (firstDate.before(currentDate)) {

                dataManager.setActivityToCalendarList(firstDate.toString(), title);

                // Count FirstDate + 30 min
                calFirstTimeSlot.setTime(firstDate);
                calFirstTimeSlot.add(Calendar.MINUTE, var.timeFrame);
                firstDate = calFirstTimeSlot.getTime();

                if (DEBUGMODE)
                    Log.d(TAG, "time4; " + startTime + " || startTime; " + firstDate + " || currentTime; " + currentDate);

            }

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
            setMenuBackground(android.R.color.holo_orange_light);
            setMenuBtn(R.drawable.ic_forward);
            startCounting();
        }
    }


    // load edited List and uupdate ActivityObjectListAdapter
    public void updateObjectList() {
        objectAdapter.list = new ArrayList<>(dataManager.getObjectMap().keySet());
        objectAdapter.notifyDataSetChanged();
    }

    // load edited List and uupdate activeActivityObjectListAdapter
    public void updateActiveList() {
        activeAdapter.stopCounting();
        activeAdapter.list = dataManager.activeList;
        activeAdapter.notifyDataSetChanged();
    }


    /**
     * add all active Activity to CalendarList
     */
    private void addActiveActivitiesToCalenderList() {

        ArrayList<String> activeList = dataManager.activeList;

        if (activeList != null && activeList.size() > 0) {

            for (int i = 0; i < activeList.size(); i++) {
                ActivityObject aObject = dataManager.getActivityObject(activeList.get(i));
                addActivityObjectToCalendarList(aObject, aObject.startTime);
            }
        }
    }





    public void startCounting() {

        updateRemainingTimeRunnable = new Runnable() {
            @Override
            public void run() {
                String currentTime = Calendar.getInstance().getTime().toString().substring(11,16);
                setMenuTitle(currentTime);
                if(DEBUGMODE) Log.d(TAG, currentTime);
            }
        };
        startUpdateTimer();
    }


    public void stopCounting() {
        if (tmr != null) tmr.cancel();
    }

    private void startUpdateTimer() {
        tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(updateRemainingTimeRunnable);
            }
        }, 0, 1000);
    }
}