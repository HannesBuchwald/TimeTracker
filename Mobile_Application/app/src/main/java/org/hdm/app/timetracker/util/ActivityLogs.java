
package org.hdm.app.timetracker.util;

import android.util.Log;

import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.TimeFrame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hdm.app.timetracker.util.Consts.*;
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


    private void createUser() {

        Calendar calendar = Calendar.getInstance();

        this.date = Integer.toString(calendar.get(Calendar.YEAR)) +
                ":" + Integer.toString(calendar.get(Calendar.MONTH) + 1) +
                ":" + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

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

            if(DEBUGMODE) Log.d(TAG, "titleee " + logs.title);

            // Get TimeFrameList - with all recorded timeFrames from the activity
            ArrayList<TimeFrame> list = object.timeFrameList;

            // Inner List add all tracked timeFrame in String format to TimeStamp List
            for (int i = 0; i < list.size(); i++) {
                TimeFrame frame = list.get(i);

                TimeStamp timeStamp = new TimeStamp();
                String start = frame.startTime.getHours() + ":"
                        + frame.startTime.getMinutes() + ":"
                        + frame.startTime.getSeconds();

                String end = frame.endTime.getHours() + ":"
                        + frame.endTime.getMinutes() + ":"
                        + frame.endTime.getSeconds();


                Calendar cal = Calendar.getInstance();
                cal.setTime(frame.startTime);
                String currentDate = cal.getTime().toString();
                int size = currentDate.length();
                String year = currentDate.substring(size-4, size);
                String date = currentDate.substring(4, 19);
                String datee = year+"_"+ date;


                timeStamp.start = start;
                timeStamp.end = end;
                timeStamp.date = datee;

                timeStamp.author = frame.author;
                timeStamp.portion = frame.portion;
                timeStamp.food = frame.food;
                timeStamp.contractWork = frame.contractWork;
                logs.timeStamps.add(timeStamp);
            }


            if (DataManager.getInstance().activeList.contains(object.title)) {
                TimeStamp timeStamp = new TimeStamp();

                Calendar cal = Calendar.getInstance();
                Date currentTime = cal.getTime();

                timeStamp.start = object.startTime.getHours() + ":"
                        + object.startTime.getMinutes() + ":"
                        + object.startTime.getSeconds();
                timeStamp.end = currentTime.getHours() + ":"
                        + currentTime.getMinutes() + ":"
                        + currentTime.getSeconds();

                Calendar call = Calendar.getInstance();
                call.setTime(object.startTime);
                String currentDate = call.getTime().toString();
                int size = currentDate.length();
                String year = currentDate.substring(size-4, size);
                String date = currentDate.substring(4, 19);
                String datee = year+"_"+ date;
                timeStamp.date = datee;


                timeStamp.author = "recording";
                timeStamp.contractWork = "";
                timeStamp.portion = object.portion;
                timeStamp.food = new ArrayList();
                timeStamp.contractWork = object.service;
                logs.timeStamps.add(timeStamp);
            }

            this.logs.add(logs);
        }
    }
}
