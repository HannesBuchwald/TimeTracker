package org.hdm.app.sambia.datastorage;

import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

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
    public Bitmap image = null;
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


    public void saveTimeStamp() {
        this.timeFrameList.add(new TimeFrame(this.startTime, this.endTime, this.service));
        this.startTime = null;
        this.endTime = null;
        this.service = null;
    }


}