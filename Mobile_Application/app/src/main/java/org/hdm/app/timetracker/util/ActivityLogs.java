package org.hdm.app.timetracker.util;

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

        this.user_id = "fdsjhf3738";

        Map<String, ActivityObject> map = DataManager.getInstance().getObjectMap();



        for (Map.Entry<String, ActivityObject> entry : map.entrySet()) {
            ActivityObject object = entry.getValue();

            // Create new Log Object
            Logs logs = new Logs();
            logs._id = object._id;

            // Get TimeFrameList - with all tracked timeframes from the activity
            ArrayList<TimeFrame> list = object.timeFrameList;
//            if(DEBUGMODUS) Log.d(TAG, object.title + " " + object.timeFrameList.size());

            // Inner List add all tracked timeFrame in String format to TimeStamp List
            for(int i = 0; i< list.size(); i++){
                TimeFrame frame = list.get(i);
                TimeStamp timeStamp = new TimeStamp();
                timeStamp.start = frame.startTime.getHours() + ":" + frame.startTime.getMinutes();
                timeStamp.end = frame.startTime.getHours() + ":" + frame.startTime.getMinutes();
                timeStamp.whereFrom = frame.whereFrom;

                logs.timeStamps.add(timeStamp);
            }

            this.logs.add(logs);
        }
    }
}
