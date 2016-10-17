package org.hdm.app.timetracker.screens;

/**
 * Created by Hannes on 13.05.2016.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.TimeFrame;
import org.hdm.app.timetracker.listener.CalendarItemOnClickListener;
import org.hdm.app.timetracker.adapter.CalendarListAdapter;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TreeMap;


/**
 * A fragment representing the front of the card.
 */
@SuppressLint("NewApi")
public class FragmentCalender extends BaseFragemnt implements
        CalendarItemOnClickListener,
        View.OnClickListener,
        View.OnLongClickListener {


    private String TAG = "DayView";
    private View view;
    private RecyclerView rv_calender;
    private CalendarListAdapter adapter;
    private int rows = 1;
    private LinkedHashMap data;
    private LinkedHashMap calendar;
    private FloatingActionButton fab_calendar;
    private int overallXScroll;
    private int lastFirstVisiblePosition = 0;

    private DataManager manager = DataManager.getInstance();
    public Variables var = Variables.getInstance();
    private Button btn_settings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);
        initMenu(view, (RecyclerView) view.findViewById(R.id.rv_calender));
        initCalenderList();
        initFloatingButton();
        return view;
    }




    @Override
    public void onPause() {


        super.onPause();
        setCalendarIconVisibility(false);
}


    @Override
    public void onResume() {
        super.onResume();
        setMenuTitle(TAG);
        setMenuBackground(android.R.color.holo_blue_light);
        setMenuBtn(R.drawable.ic_back);
        setCalendarIconVisibility(true);
//        if (!var.editable) scrollListToCurrentTime();

        if(var.editableMode) {
            fab_calendar.setVisibility(View.VISIBLE);
        }else {
            fab_calendar.setVisibility(View.GONE);
        }
    }





    private void initFloatingButton() {
        fab_calendar = (FloatingActionButton) view.findViewById(R.id.fab_calendar);
      //  fab_calendar.setOnClickListener(this);
        fab_calendar.setOnLongClickListener(this);
        fab_calendar.setVisibility(View.INVISIBLE);
    }

    private void initCalenderList() {
        data = DataManager.getInstance().activityMap;
        calendar = DataManager.getInstance().calenderMap;
        adapter = new CalendarListAdapter(getActivity(), data, calendar);
        if(DEBUGMODE) Log.d(TAG, "Jsonnnnnn " + "Calendar " + data);
        if(DEBUGMODE) Log.d(TAG, "Jsonnnnnn " + "Calendar " + calendar);

        adapter.setListener(this);
        rv_calender = (RecyclerView) view.findViewById(R.id.rv_calender);
        rv_calender.setAdapter(adapter);
        rv_calender.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_calender.setOnClickListener(this);
//        scrollListToCurrentTime();
    }







    @Override
    public void didOnClick(String time, String s, View_Holder holder) {

        if (DEBUGMODE) Log.d(TAG, "holder " + time + " // String " + s + " " + holder.id);

        if (var.editable) {
            // Delete Entry in CalendarMap
            dataManager.deleteCalenderMapEntry(time, s);

            // Delete Entry in ActivityObject TimeFrame
            // ToDo Discuss if the TimeFrameList is realy helpful - better way CalendarMap?
            ArrayList<TimeFrame> list = dataManager.getActivityObject(s).timeFrameList;
            if (DEBUGMODE) Log.d(TAG, "activity " + list.size());
        }
    }


    @Override
    public void didOnClickAddBtn(View_Holder holder) {
        if (DEBUGMODE) Log.d(TAG, "holder " + holder.id);
        var.selectedTime = holder.id;
        listener.flip();
    }

    @Override
    public void setCalendarTitle(String s) {
        setMenuTitle(s);
    }


    // FloatingActionButton Listener
    @Override
    public void onClick(View v) {


        if(DEBUGMODE) Log.d(TAG, "click " + v.getId());

//        lastFirstVisiblePosition = ((LinearLayoutManager) rv_calender.getLayoutManager()).findFirstVisibleItemPosition();
//
//        if (var.editable) {
//            var.editable = false;
//            fab_calendar.setImageResource(android.R.drawable.ic_menu_edit);
//
//        } else {
//            var.editable = true;
//            fab_calendar.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
//        }
//        // Invalidate new
//        adapter.notifyDataSetChanged();
    }



    @Override
    public boolean onLongClick(View v) {
        lastFirstVisiblePosition = ((LinearLayoutManager) rv_calender.getLayoutManager()).findFirstVisibleItemPosition();

        if (var.editable) {
            var.editable = false;
            fab_calendar.setImageResource(android.R.drawable.ic_menu_edit);

        } else {
            var.editable = true;
            fab_calendar.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        }
        // Invalidate new
        adapter.notifyDataSetChanged();
        return true;
    }


}