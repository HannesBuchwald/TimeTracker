package org.hdm.app.timetracker.datastorage;

import android.graphics.Bitmap;
import android.util.Log;

import org.hdm.app.timetracker.util.Consts;
import org.hdm.app.timetracker.util.Variables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;

/**
 * Created by Hannes on 06.05.2016.
 */
public class DataManager {

    private final String TAG = "DataManager";
    // Instance from DaataManager
    private static DataManager instance = null;




    // In this Map are all the Activity Objects stored
    // It is used as DataBase from every Screen
    public LinkedHashMap<String, AAAActivityObject> activityMap = new LinkedHashMap<>();



    // In this map is stored the activitys for Calender list
    public LinkedHashMap<String, ArrayList<String>> calenderMap = new LinkedHashMap<>();
    public ArrayList<String> activeList = new ArrayList<>();
    public List<Stamp> logList = new ArrayList<>();
    public String lastLog = "";


    // New Maps
    private LinkedHashMap<String, ActivityObject> objectMap = new LinkedHashMap<>();
    public LinkedHashMap<String, ActivityObject> portionMap = new LinkedHashMap<>();
    public LinkedHashMap<String, ActivityObject> foodMap = new LinkedHashMap<>();

    private LinkedHashMap<String, ActiveObject> activeMap = new LinkedHashMap<>();
    private LinkedHashMap<Date, Stamp> logMap = new LinkedHashMap<>();

    public HashMap<String, Bitmap> imageMap = new HashMap<>();




    public LinkedHashMap<String, ActiveObject> getActiveMap() {
        return activeMap;
    }

    public ActiveObject getActiveObject(String id) {
        return activeMap.get(id);
    }

    public void setActiveMap(LinkedHashMap<String, ActiveObject> activeMap) {
        this.activeMap = activeMap;
        if(DEBUGMODE) Log.d(TAG, "activeList " + activeMap.size());
    }





    public ActivityObject getObject(String key) {
        return objectMap.get(key);
    }


    public LinkedHashMap<String, ActivityObject> getObjectMap() {
        return objectMap;
    }


    public void setObjectMap(LinkedHashMap<String, ActivityObject> objectMap) {
        this.objectMap = objectMap;
    }



    public int addObjectToActivityConfigurationMap(ActivityObject activityObject) {

        String id = activityObject.get_id();
        if (id != null && objectMap != null) {

            if(!objectMap.containsKey(id)) {
                objectMap.put(id, activityObject);
                return 01;
            }
            return 02;
        }
        return 03;
    }



    public int addObjectToPortionMap(ActivityObject activityObject) {

        String id = activityObject.get_id();
        if (id != null && portionMap != null) {

            if(!portionMap.containsKey(id)) {
                portionMap.put(id, activityObject);
                return 01;
            }
            return 02;
        }
        return 03;
    }


    public int addObjectToFoodMap(ActivityObject activityObject) {

        String id = activityObject.get_id();
        if (id != null && foodMap != null) {

            if(!foodMap.containsKey(id)) {
                foodMap.put(id, activityObject);
                return 01;
            }
            return 02;
        }
        return 03;
    }



    public boolean createActivityObject(String name, AAAActivityObject AAAActivityObject) {
        if(name != null) {
            if(!activityMap.containsKey(name)) {

                if(AAAActivityObject != null) {
                    activityMap.put(name, AAAActivityObject);
                } else {
                    activityMap.put(name, new AAAActivityObject(name));
                }
                return true;
            }
        }
        return false;
    }







    public boolean setActivityObject(AAAActivityObject AAAActivityObject) {

        String title = AAAActivityObject.title;
        if (title != null && activityMap != null) {

            if(!activityMap.containsKey(title)) {
                createActivityObject(title, AAAActivityObject);
            }
            activityMap.put(title, AAAActivityObject);
            if(DEBUGMODE && AAAActivityObject.timeFrameList.size()>1) {
                Log.d(TAG, "key:" + AAAActivityObject.timeFrameList.get(AAAActivityObject.timeFrameList.size()-1).startTime);
            }
            return true;
            }
        return false;
    }



    public AAAActivityObject getActivityObject(String id) {
        if(id != null && activityMap.containsKey(id)) {
            return activityMap.get(id);
        }
        return null;
    }




    public LinkedHashMap getObjectMappp() {
        return activityMap;
    }





//    public boolean createPortionObject(String name, AAAActivityObject AAAActivityObject) {
//        if(name != null) {
//            if(!portionMap.containsKey(name)) {
//
//                if(AAAActivityObject != null) {
//                    portionMap.put(name, AAAActivityObject);
//                } else {
//                    portionMap.put(name, new AAAActivityObject(name));
//                }
//                return true;
//            }
//        }
//        return false;
//    }

//    public boolean setPortionObject(AAAActivityObject AAAActivityObject) {
//
//        String title = AAAActivityObject.title;
//        if (title != null && portionMap != null) {
//
//            if(!portionMap.containsKey(title)) {
//                createPortionObject(title, AAAActivityObject);
//            }
//            portionMap.put(title, AAAActivityObject);
////            if(DEBUGMODE && AAAActivityObject.timeFrameList.size()>1) {
////                Log.d(TAG, "key:" + AAAActivityObject.timeFrameList.get(AAAActivityObject.timeFrameList.size()-1).startTime);
////            }
//            return true;
//        }
//        return false;
//    }

//    public AAAActivityObject getPortionObject(String name) {
//        if(name != null && portionMap.containsKey(name)) {
//            return portionMap.get(name);
//        }
//        return null;
//    }

    public LinkedHashMap getPortionMap() {
        return portionMap;
    }






//    public boolean createFoodObject(String name, AAAActivityObject AAAActivityObject) {
//        if(name != null) {
//            if(!foodMap.containsKey(name)) {
//
//                if(AAAActivityObject != null) {
//                    foodMap.put(name, AAAActivityObject);
//                } else {
//                    foodMap.put(name, new AAAActivityObject(name));
//                }
//                return true;
//            }
//        }
//        return false;
//    }

//    public boolean setFoodObject(AAAActivityObject AAAActivityObject) {
//
//        String title = AAAActivityObject.title;
//        if (title != null && foodMap != null) {
//
//            if(!foodMap.containsKey(title)) {
//                createFoodObject(title, AAAActivityObject);
//            }
//            foodMap.put(title, AAAActivityObject);
//            if(DEBUGMODE && AAAActivityObject.timeFrameList.size()>1) {
//                Log.d(TAG, "key:" + AAAActivityObject.timeFrameList.get(AAAActivityObject.timeFrameList.size()-1).startTime);
//            }
//            return true;
//        }
//        return false;
//    }

//    public AAAActivityObject getFoodObject(String name) {
//        if(name != null && foodMap.containsKey(name)) {
//            return foodMap.get(name);
//        }
//        return null;
//    }

    public LinkedHashMap getFoodMap() {
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
//            Log.d(TAG, "key: " + key.toString() + " // value: " + calenderMap.get(key).toString());
                Log.d(TAG, "key: " + calenderMap.keySet());

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

    public LinkedHashMap<String, ArrayList<String>> getCalendarMap() {
        return calenderMap;
    }
















    public int addImageToImageMap(String imageName, Bitmap bitmap) {

        if(imageName!= null){

            if(!imageMap.containsKey(imageName)) {
                imageMap.put(imageName, bitmap);
                return 01;
            }
            return 02;
        }
        return 03;
    }


    /**
     * This method creates a default activityObject with current starttime and author as user
     *
     * @param id = it is the String ID from the activity to identify the activity
     * @param contractWork = contract Work Status
     * @return
     */
    public ActiveObject createActiveObject(String id, boolean contractWork) {
        ActiveObject activeObject = new ActiveObject();

        ActivityObject activityObject = getObject(id);
        activeObject.id = activityObject.get_id();
        activeObject.title = activityObject.getTitle();
        activeObject.startTime = Calendar.getInstance().getTime();
        activeObject.contractWork = contractWork;
        activeObject.author = Consts.USER;

        return activeObject;
    }






    public Stamp convertActiveObjectToStamp(ActiveObject activeObject) {
        Stamp stamp = new Stamp();
        String date =  activeObject.startTime.toString();

        stamp.a03_userID = Variables.getInstance().user_ID;
        stamp.a01_activity = activeObject.title;
        stamp.c01_contract_work = convertBooleanToString(activeObject.contractWork);

        stamp.a06_author = activeObject.author;
        stamp.a07_delete = activeObject.delete;

        stamp.b01_time_date = date.substring(0,10) + " " + date.substring(date.length()-4);;
        stamp.b02_time_start = date.substring(11,19);
        stamp.b03_time_end = activeObject.endTime.toString().substring(11,19);
        stamp.b05_time_sum_sec = String.valueOf((getTimeDiff(activeObject)/1000));
        stamp.b04_time_sum = getTimeSum(activeObject);

        if(stamp.a07_delete.equals("Yes")) stamp.b04_time_sum = "-" + stamp.b04_time_sum;

        return stamp;
    }




    private String convertBooleanToString(boolean contractWork) {
        if(contractWork) {
            return "Yes";
        } else {
            return "No";
        }
    }


    private String getTimeSum(ActiveObject activityObject) {

        long timeDiff = getTimeDiff(activityObject);

        int seconds = (int) (timeDiff / 1000) % 60;
        int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
        int hours = (int) (timeDiff/1000) / 3600;

        String secondsStr = String.valueOf(seconds);
        String minutesStr = String.valueOf(minutes);
        String hoursStr = String.valueOf(hours);

        if (seconds < 10) secondsStr = "0" + secondsStr;
        if (minutes < 10) minutesStr = "0" + minutesStr;
        if (hours < 10) hoursStr = "0" + hoursStr;

        String time = hoursStr + ":" + minutesStr + ":" + secondsStr;

        return time;
    }


    private long getTimeDiff(ActiveObject activityObject) {
        return activityObject.endTime.getTime() - activityObject.startTime.getTime();
    }




    public void addToLogMap(Date id, Stamp stamp) {
        if(id!= null && stamp != null) logMap.put(id, stamp);
    }




    public void initMaps() {

        objectMap = new LinkedHashMap<String, ActivityObject>();
        imageMap = new LinkedHashMap<String, Bitmap>();
        activeMap = new LinkedHashMap<>();
        logMap = new LinkedHashMap<>();

        portionMap = new LinkedHashMap<>();
        foodMap = new LinkedHashMap<>();
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
