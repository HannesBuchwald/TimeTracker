package org.hdm.app.timetracker.util;


import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hdm.app.timetracker.datastorage.AAAActivityObject;
import org.hdm.app.timetracker.datastorage.ActivityObjectMap;

import java.io.IOException;
import java.util.ArrayList;

import static org.hdm.app.timetracker.util.Consts.*;

public class MyJsonParser {
    private final String TAG = "MyJsonParser";


    public MyJsonParser() {}


    public ArrayList<AAAActivityObject> createObjectFromJson(String objects, String jsonString) {

        if(jsonString != null && objects != null) {


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Json String Input
//            JsonParser jp = jsonFactory.createJsonParser(jsonString);
            Log.d(TAG,"string  " + jsonString.toString());
            ActivityObjectMap activityObjectMap = objectMapper.readValue(jsonString, ActivityObjectMap.class);
            ArrayList arrayList = activityObjectMap.get(objects);
            AAAActivityObject a =(AAAActivityObject) arrayList.get(1);
            if(DEBUGMODE) Log.d(TAG,"object " + a.title + " done");
            return arrayList;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        }
        return null;
    }




    public String createLogJsonFromActivityObjects() {

        ActivityLogs activityLogs = new ActivityLogs();
        String jsonInString = convertObjectToJson(activityLogs);
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
