
package org.hdm.app.timetracker.util;

import android.util.Log;

import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.TimeFrame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Hannes on 30.07.2016.
 */
public class ActivityLogs {
    private static final String TAG = "ActivityLogs";
    public String date;
    public String user_id;
    public List<Logs> logs = new ArrayList<>();


    public ActivityLogs() {
        createUser();
    }



    private void createUser(){

        Calendar calendar = Calendar.getInstance();

        this.date =  Integer.toString(calendar.get(Calendar.YEAR)) +
                ":" +  Integer.toString(calendar.get(Calendar.MONTH)+1) +
                ":" +  Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        this.user_id = Variables.getInstance().user_ID;

        Map<String, ActivityObject> map = DataManager.getInstance().getObjectMap();



        for (Map.Entry<String, ActivityObject> entry : map.entrySet()) {
            ActivityObject object = entry.getValue();

            // Create new Log Object
            Logs logs = new Logs();

            // Add Activity ID to Log
            logs._id = object._id;

            // Add Activity title to Log
            logs.title = object.title;

            // Get TimeFrameList - with all recorded timeFrames from the activity
            ArrayList<TimeFrame> list = object.timeFrameList;

            // Inner List add all tracked timeFrame in String format to TimeStamp List
            for(int i = 0; i< list.size(); i++){
                TimeFrame frame = list.get(i);
                TimeStamp timeStamp = new TimeStamp();
                timeStamp.start = frame.startTime.getHours() + ":" + frame.startTime.getMinutes();

                Log.d(TAG, "StartTime" + timeStamp.start);
                timeStamp.end = frame.endTime.getHours() + ":" + frame.endTime.getMinutes();
                timeStamp.whereFrom = frame.whereFrom;
                logs.timeStamps.add(timeStamp);
            }

            this.logs.add(logs);
        }
    }
}
