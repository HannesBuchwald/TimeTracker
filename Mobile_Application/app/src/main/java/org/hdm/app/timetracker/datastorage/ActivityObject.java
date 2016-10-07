package org.hdm.app.timetracker.datastorage;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public String ownWork = null;

//    public String sub_activity = "";
//    public boolean sub_category = false;

    public String imageName = "";
    public ArrayList<TimeFrame> timeFrameList = null;


    // Dynamic parameters
    public boolean activeState = false;
    public int count = 0;

    public Date startTime = null;
    public Date endTime = null;
    private String service = null;

    public String portion = null;
    public List<String> food = new ArrayList<>();


    public ActivityObject() {
        timeFrameList = new ArrayList<>();
    }

    public ActivityObject(String name) {
        title = name;
        timeFrameList = new ArrayList<>();
    }

    // All Activities
    public void saveTimeStamp(String whereFrom) {
        TimeFrame timeFrame = new TimeFrame(this.startTime, this.endTime, this.service, whereFrom);
        timeFrame.ownWork = this.ownWork;
        this.timeFrameList.add(timeFrame);

        this.ownWork = null;
        this.startTime = null;
        this.endTime = null;
        this.service = null;
    }

    // Food Activity
    public void saveTimeStamp(String whereFrom, boolean b) {

        TimeFrame timeFrame = new TimeFrame(this.startTime, this.endTime, this.service, whereFrom);
        timeFrame.portion = this.portion;
        timeFrame.food = this.food;
        timeFrame.ownWork = this.ownWork;
        this.timeFrameList.add(timeFrame);

        this.food = new ArrayList<>();
        this.portion = null;

        this.ownWork = null;
        this.startTime = null;
        this.endTime = null;
        this.service = null;
    }



    public void saveTimeStamp(String whereFrom, Date startTime, Date endTime, String ownWork) {
        TimeFrame timeFrame = new TimeFrame(startTime, endTime, this.service, whereFrom);
        timeFrame.ownWork = ownWork;
        this.timeFrameList.add(timeFrame);
    }
}