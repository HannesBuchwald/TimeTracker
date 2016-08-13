package org.hdm.app.sambia.util;


import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hdm.app.sambia.datastorage.ActivityObject;
import org.hdm.app.sambia.datastorage.ActivityObjectMap;

import java.io.IOException;
import java.util.ArrayList;

import static org.hdm.app.sambia.util.Consts.*;

public class MyJsonParser {
    private final String TAG = "MyJsonParser";


    public MyJsonParser() {}


    public ArrayList<ActivityObject> createObjectFromJson(String objects, String jsonString) {

        if(jsonString != null && objects != null) {


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Json String Input
//            JsonParser jp = jsonFactory.createJsonParser(jsonString);
            Log.d(TAG,"string  " + jsonString.toString());
            ActivityObjectMap activityObjectMap = objectMapper.readValue(jsonString, ActivityObjectMap.class);
            ArrayList arrayList = activityObjectMap.get(objects);
            ActivityObject a =(ActivityObject) arrayList.get(1);
            Log.d(TAG,"object " + a.title + " done");
            return arrayList;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        }
        return null;
    }


    public String logName;


    public String createLogJsonFromActivityObjects() {

        ActivityLogs activityLogs = new ActivityLogs();
        String jsonInString = convertObjectToJson(activityLogs);
        logName  = activityLogs.date + "-"+ activityLogs.user_id + "-activities.txt";

        return jsonInString;
    }



    public String createActivityStateJson() {

        ActivityState activityState = new ActivityState();
        String jsonInString =  convertObjectToJson(activityState);
        logName  = TEMPACTIVITIES;
        return jsonInString;
    }




    private String convertObjectToJson(Object object) {

        try {
            //Convert object to JSON string
            return new ObjectMapper().writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
