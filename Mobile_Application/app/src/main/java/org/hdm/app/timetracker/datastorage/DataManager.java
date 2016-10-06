package org.hdm.app.timetracker.datastorage;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;

/**
 * Created by Hannes on 06.05.2016.
 */
public class DataManager {

    private final String TAG = "DataManager";
    // Instance from DaataManager
    private static DataManager instance = null;


    public HashMap<String, Bitmap> imageMap = new HashMap<>();


    // In this Map are all the Activity Objects stored
    // It is used as DataBase from every Screen
    public LinkedHashMap<String, ActivityObject> activityMap = new LinkedHashMap<>();



    // In this map is stored the activitys for Calender list
    public TreeMap<String, ArrayList<String>> calenderMap = new TreeMap<>();


    public LinkedHashMap<String, ActivityObject> plateMap = new LinkedHashMap<>();
    public LinkedHashMap<String, ActivityObject> foodMap = new LinkedHashMap<>();




    public ArrayList<String> activeList = new ArrayList<>();








    public boolean createActivityObject(String name, ActivityObject activityObject) {
        if(name != null) {
            if(!activityMap.containsKey(name)) {

                if(activityObject != null) {
                    activityMap.put(name, activityObject);
                } else {
                    activityMap.put(name, new ActivityObject(name));
                }
                return true;
            }
        }
        return false;
    }







    public boolean setActivityObject(ActivityObject activityObject) {

        String title = activityObject.title;
        if (title != null && activityMap != null) {

            if(!activityMap.containsKey(title)) {
                createActivityObject(title, activityObject);
            }
            activityMap.put(title, activityObject);
            if(DEBUGMODE && activityObject.timeFrameList.size()>1) {
                Log.d(TAG, "key:" + activityObject.timeFrameList.get(activityObject.timeFrameList.size()-1).startTime);
            }
            return true;
            }
        return false;
    }



    public ActivityObject getActivityObject(String name) {
        if(name != null && activityMap.containsKey(name)) {
            return activityMap.get(name);
        }
        return null;
    }


    public LinkedHashMap getObjectMap() {
        return activityMap;
    }





    public boolean createPlateObject(String name, ActivityObject activityObject) {
        if(name != null) {
            if(!plateMap.containsKey(name)) {

                if(activityObject != null) {
                    plateMap.put(name, activityObject);
                } else {
                    plateMap.put(name, new ActivityObject(name));
                }
                return true;
            }
        }
        return false;
    }

    public boolean setPlateObject(ActivityObject activityObject) {

        String title = activityObject.title;
        if (title != null && plateMap != null) {

            if(!plateMap.containsKey(title)) {
                createActivityObject(title, activityObject);
            }
            plateMap.put(title, activityObject);
            if(DEBUGMODE && activityObject.timeFrameList.size()>1) {
                Log.d(TAG, "key:" + activityObject.timeFrameList.get(activityObject.timeFrameList.size()-1).startTime);
            }
            return true;
        }
        return false;
    }

    public ActivityObject getPlateObject(String name) {
        if(name != null && plateMap.containsKey(name)) {
            return plateMap.get(name);
        }
        return null;
    }

    public LinkedHashMap getPlateMap() {
        return plateMap;
    }






    public boolean createFoodObject(String name, ActivityObject activityObject) {
        if(name != null) {
            if(!foodMap.containsKey(name)) {

                if(activityObject != null) {
                    foodMap.put(name, activityObject);
                } else {
                    foodMap.put(name, new ActivityObject(name));
                }
                return true;
            }
        }
        return false;
    }

    public boolean setFoodObject(ActivityObject activityObject) {

        String title = activityObject.title;
        if (title != null && foodMap != null) {

            if(!foodMap.containsKey(title)) {
                createActivityObject(title, activityObject);
            }
            foodMap.put(title, activityObject);
            if(DEBUGMODE && activityObject.timeFrameList.size()>1) {
                Log.d(TAG, "key:" + activityObject.timeFrameList.get(activityObject.timeFrameList.size()-1).startTime);
            }
            return true;
        }
        return false;
    }

    public ActivityObject getFoodObject(String name) {
        if(name != null && foodMap.containsKey(name)) {
            return foodMap.get(name);
        }
        return null;
    }

    public LinkedHashMap getfoodMap() {
        return foodMap;
    }







    public boolean setCalenderMapEntry(String key, String activity) {

        // check if key is not null
        if (key != null) {

            ArrayList<String> list = null;

            if(activity != null && calenderMap.containsKey(key)) {
                list = calenderMap.get(key);
                // do not add enty if list contains already activitys
                if(list.contains(activity)) return true;
                list.add(activity);
            } else {
                list = new ArrayList<String>();
            }
            calenderMap.put(key, list);
            if (DEBUGMODE) {
            Log.d(TAG, "key: " + key.toString() + " // value: " + calenderMap.get(key).toString());
            }
            return true;
        } else {
            return false;
        }
    }


    public boolean setActivityToCalendarList(String key, String activity) {

        // check if key is not null
        if (key != null && activity != null) {

            if(calenderMap.containsKey(key)) {

                ArrayList<String> list = calenderMap.get(key);
                // do not add enty if list contains already activitys
                if (list.contains(activity)) return false;
                list.add(activity);
                calenderMap.put(key, list);

                if (DEBUGMODE) {
                    Log.d(TAG, "keyy: " + key.toString() + " // value: " + calenderMap.get(key).toString());
                }
                return true;
            }
        }
        return false;
    }



    public boolean deleteCalenderMapEntry(String key, String activity){

        if(key != null && activity != null) {

            if(calenderMap.containsKey(key)) {
                ArrayList<String> list = calenderMap.get(key);
                if(DEBUGMODE) Log.d(TAG, "listSize before: " + list.size() + " " + list.toString());


                if(list.remove(activity)){
                    if(DEBUGMODE) Log.d(TAG, "listSize after: " + list.size() + " " + list.toString());
                    return true;
                }
            }
        }

        return false;
    }

    public TreeMap<String, ArrayList<String>> getCalendarMap() {
        return calenderMap;
    }


    /***********
     * Singelton pattern
     ***********/

    public static void init() {
        if (instance == null) {
            instance = new DataManager();

        }
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }


}
