package org.hdm.app.timetracker.datastorage;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;

/**
 * Created by Hannes on 27.05.2016.
 */

public class ActivityObject extends Object {

    private static final String TAG = "ActivityObject";

    // General Parameter
    public String title = null;
    public String _id = null;
    public String item = null;
    public String group_activity = "";
    public String externalWork = "";

//    public String sub_activity = "";
//    public boolean sub_category = false;

    public String imageName = "";
    public ArrayList<TimeFrame> timeFrameList = null;


    // Dynamic parameters
    public boolean activeState = false;
    public int count = 0;

    public Date startTime = null;
    public Date endTime = null;
    public String service = "";

    public String portion = "";
    public List<String> food = new ArrayList<>();


    public ActivityObject() {
        timeFrameList = new ArrayList<>();
    }

    public ActivityObject(String name) {
        title = name;
        timeFrameList = new ArrayList<>();
    }

    // All Activities
    public void saveTimeStamp(String editor) {
        TimeFrame timeFrame = new TimeFrame();
        timeFrame.startTime = this.startTime;
        timeFrame.endTime = this.endTime;

        timeFrame.contractWork = this.service;
        timeFrame.author = editor;

        timeFrame.portion = this.portion;
        timeFrame.food = this.food;

        this.timeFrameList.add(timeFrame);

        this.startTime = null;
        this.endTime = null;
        this.service = null;
        this.portion = null;
        this.food = new ArrayList<>();

        if(DEBUGMODE) Log.d(TAG, "timeFrame " + timeFrame.contractWork + " " + timeFrame.startTime + " " + timeFrame.endTime);
    }



    public void saveTimeStamp(String author, Date startTime, Date endTime) {

        TimeFrame timeFrame = new TimeFrame();
        timeFrame.startTime = startTime;
        timeFrame.endTime = endTime;
        timeFrame.author = author;
        timeFrame.contractWork = this.service;
        timeFrame.portion = this.portion;
        timeFrame.food = this.food;

        this.timeFrameList.add(timeFrame);


    }
}