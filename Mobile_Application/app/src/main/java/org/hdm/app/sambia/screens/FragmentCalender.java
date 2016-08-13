package org.hdm.app.sambia.screens;

/**
 * Created by Hannes on 13.05.2016.
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.sambia.R;
import org.hdm.app.sambia.datastorage.DataManager;
import org.hdm.app.sambia.datastorage.TimeFrame;
import org.hdm.app.sambia.listener.CalendarItemOnClickListener;
import org.hdm.app.sambia.adapter.CalendarListAdapter;
import org.hdm.app.sambia.util.Variables;
import org.hdm.app.sambia.util.View_Holder;

import static org.hdm.app.sambia.util.Consts.DEBUGMODE;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TreeMap;


/**
 * A fragment representing the front of the card.
 */
public class FragmentCalender extends BaseFragemnt implements
        CalendarItemOnClickListener,
        View.OnClickListener
       {


    private String TAG = "DayView";
    private View view;
    private RecyclerView rv_calender;
    private CalendarListAdapter adapter;
    private int rows = 1;
    private LinkedHashMap data;
    private TreeMap calendar;
    private FloatingActionButton fab_calendar;
    private int overallXScroll;
    private int lastFirstVisiblePosition = 0;

    private DataManager manager = DataManager.getInstance();
    public Variables var = Variables.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);

        initMenu(view);
        initCalenderList();
        initFloatingButton();
        return view;
    }

    private void initFloatingButton() {
        fab_calendar = (FloatingActionButton) view.findViewById(R.id.fab_calendar);
        fab_calendar.setOnClickListener(this);
    }

           @Override
           public void onPause() {
               super.onPause();
           }



           @Override
    public void onResume() {
        super.onResume();
        setMenuTitle(TAG);
        setMenuBackground(android.R.color.holo_blue_light);
        setMenuBtn(R.drawable.ic_back);
        if(!var.editable) scrollListToCurrentTime();
    }





    private void initCalenderList() {
        data = dataManager.getObjectMap();
        calendar = dataManager.getCalendarMap();
        adapter = new CalendarListAdapter(getActivity(),data, calendar);
        adapter.setListener(this);
        rv_calender = (RecyclerView) view.findViewById(R.id.rv_calender);
        rv_calender.setAdapter(adapter);
        rv_calender.setLayoutManager(new LinearLayoutManager(getActivity()));
        scrollListToCurrentTime();
    }





    @Override
    public void didOnClick(String time, String s, View_Holder holder) {

        if(DEBUGMODE) Log.d(TAG, "holder " + time + " // String " + s  + " " + holder.id);

        if(var.editable){
            // Delete Entry in CalendarMap
            dataManager.deleteCalenderMapEntry(time, s);

            // Delete Entry in ActivityObject TimeFrame
            // ToDo Discuss if the TimeFrameList is realy helpful - better way CalendarMap?
            ArrayList<TimeFrame> list = dataManager.getActivityObject(s).timeFrameList;
            if(DEBUGMODE) Log.d(TAG, "activity " + list.size());
        }
    }





           @Override
           public void didOnClickAddBtn(View_Holder holder) {
               if(DEBUGMODE) Log.d(TAG, "holder " + holder.id);
               var.selectedTime = holder.id;
               listener.flip();
           }





           // FloatingActionButton Listener
    @Override
    public void onClick(View v) {
        lastFirstVisiblePosition = ((LinearLayoutManager)rv_calender.getLayoutManager()).findFirstVisibleItemPosition();

        if(var.editable) {
            var.editable = false;
            fab_calendar.setImageResource(android.R.drawable.ic_menu_edit);

        } else  {
            var.editable = true;
            fab_calendar.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        }
        // Invalidate new
       adapter.notifyDataSetChanged();
    }




    // scroll in Calendarlist to current Time
    private void scrollListToCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        int hour = currentTime.getHours();
        if(hour>=7) hour = hour*3-8;
        rv_calender.scrollToPosition(hour);
    }

}