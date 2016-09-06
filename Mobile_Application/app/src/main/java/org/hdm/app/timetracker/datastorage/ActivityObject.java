package org.hdm.app.timetracker.datastorage;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hannes on 27.05.2016.
 */

public class ActivityObject extends Object  {

    private static final String TAG = "ActivityObject";

    // General Parameter
    public String title = null;
    public String _id = null;
    public String item = null;

    public String group_activity = "";
    public String sub_activity = "";
    public boolean sub_category = false;

    public String imageName = "";
    public ArrayList<TimeFrame> timeFrameList = null;



    // Dynamic parameters
    public boolean activeState = false;
    public int count = 0;

    public Date startTime = null;
    public Date endTime = null;
    private String service = null;




    public ActivityObject() {
        timeFrameList = new ArrayList<>();
    }

    public ActivityObject(String name) {
        title = name;
        timeFrameList = new ArrayList<>();
    }



    public void saveTimeStamp(String whereFrom) {
        this.timeFrameList.add(new TimeFrame(this.startTime, this.endTime, this.service, whereFrom));
        Log.d(TAG, "timeStamp " + startTime + " // " + endTime + " // " + service + " // "+ whereFrom);
        this.startTime = null;
        this.endTime = null;
        this.service = null;
    }

    public void saveTimeStamp(String whereFrom, Date startTime, Date endTime) {
        this.timeFrameList.add(new TimeFrame(startTime, endTime, this.service, whereFrom));
        Log.d(TAG, "timeStamp " + startTime + " // " + endTime + " // " + service + " // "+ whereFrom);
    }
}