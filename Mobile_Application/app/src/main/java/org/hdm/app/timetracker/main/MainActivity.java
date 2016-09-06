package org.hdm.app.timetracker.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.util.FileLoader;
import org.hdm.app.timetracker.util.Variables;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static org.hdm.app.timetracker.util.Consts.*;

public class MainActivity extends Activity  {
    private final String TAG = "MainActivity";

    // ToDo Implement new Feature Food
    // ToDo Implement LogFile Handling
    // ToDo Implement Transmit Data to Server
    // ToDo Implement Settings Menu
    // ToDo Fix Display Activitys in CalendarScreen
    // ToDo How to install the App on the Smartphone
    // ToDo Change Size of Items in CalendarList
    // ToDo Change Numbers of 00:30 and 00:48 in CalendarList
    // ToDo Change Tracking from Title to ID
    // ToDo Fix TimeCounter in ActiveList (Delete Thread every minute and restart it)
    // ToDo Fix Blue Background in CalendarList when time is 30 min  or 0 min

    /**
     * Constants
     */
    Handler handler = new Handler();
    private Variables var;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initConfiguration();
        initDataLogger();
        initResetRecordedData();
        loadSavedObjectState();
        initCalenderMap();
        initLayout();
//        initConfiguration();
    }



    @Override
    public void onBackPressed() {
        // ToDo write test cases
        // if backPress is true then backpress button in active
        if(Variables.getInstance().backPress) {

            // if backPressDialog is true then show a AlertDialog before quit the app
            if(Variables.getInstance().backPressDialog) {
                new AlertDialog.Builder(this)
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                MainActivity.super.onBackPressed();
                            }
                        }).create().show();
            } else {
                MainActivity.super.onBackPressed();
            }
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        if(DEBUGMODE) Log.d(TAG, "onStop");
        saveLogFile();
        saveCurrentState();
//        startActivity(new Intent(this, MainActivity.class));
    }










    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_MAIN.equals(intent.getAction())) {
            Log.i(TAG, "onNewIntent: HOME Key");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            Log.d(TAG, "MENU pressed");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }







    /*****************************
     * Init
     *******************************************/

    private void initConfiguration() {
        // Init the Data Structure - there all the created where hosted
        DataManager.init();
        Variables.init();
        var = Variables.getInstance();

        FileLoader fl = new FileLoader(this);
        fl.initFiles();
    }




    private void initCalenderMap() {

        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();

        // Reset Time to 00:00:00
        time.setHours(var.startHour);
        time.setMinutes(var.startMin);
        time.setSeconds(var.startMin);

        // Set Calendar with reseted time
        cal.setTime(time);


        Calendar calEndTime = Calendar.getInstance();
        Date endTime = calEndTime.getTime();
        endTime.setHours(var.endHour);
        endTime.setMinutes(var.startMin);
        endTime.setSeconds(var.startMin);
        calEndTime.setTime(endTime);
        calEndTime.add(Calendar.MINUTE, -var.timeFrame);
        endTime = calEndTime.getTime();

        DataManager.getInstance().calenderMap = new TreeMap<>();

        while(time.before(endTime)) {
            time = cal.getTime();
            DataManager.getInstance().setCalenderMapEntry(time.toString(), null);
            // add 30 minutes to setTime
            cal.add(Calendar.MINUTE, var.timeFrame);
        }
    }


    /**
     * Save current State every 15 min
     * Run in TimerTask Thread not on UI Thread
     */
    private void initDataLogger() {

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        saveLogFile();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0, var.logTimeInterval);
    }





    private void initResetRecordedData() {


        Calendar calEndTime = Calendar.getInstance();
        Date endTime = calEndTime.getTime();
        endTime.setSeconds(00);
        endTime.setMinutes(30);
        endTime.setHours(23);


        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                DataManager.getInstance().activeList = new ArrayList<>();
                DataManager.getInstance().calenderMap = new TreeMap<>();
                initConfiguration();
                Log.d(TAG, "restart");
            }
        };
        timer.schedule(timerTask, endTime.getTime());
    }


    private void initLayout() {
        setFullScreen(true);
        setContentView(R.layout.activity_main);
    }













    /**
     * Set App to fullscreen mode if boolean == true
     * @param fullscreen    a flag for setting the app in fullscreen mode or not
     */
    private void setFullScreen(boolean fullscreen) {
        if (fullscreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    private void saveLogFile() {
        String currentDate = Calendar.getInstance().getTime().toString();
        int size = currentDate.length();
        String year = currentDate.substring(size-4, size);
        String date = currentDate.substring(4, 19);
        String fileName = var.user_ID + "_" + year+"_"+ date + "_activities.txt";
        fileName = fileName.replaceAll(" ","_");
        Log.d(TAG, "currentDate " + fileName);
        new FileLoader(this).saveLogsOnExternal(fileName);
    }




    private void saveCurrentState() {

        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();


        // Save ObjectActivity Map
        Map<String, ActivityObject> map = DataManager.getInstance().getObjectMap();
        String json = gson.toJson(map);
        prefsEditor.putString(ACTIVITY_STATE, json);


        // Save ActiveList
        ArrayList<String> activeList = DataManager.getInstance().activeList;
//        activeList = new ArrayList<>();
        Log.d(TAG, "map " + activeList);
        json = gson.toJson(activeList);
        prefsEditor.putString(ACTIVE_LIST, json);


        // Save CalendarMap
        TreeMap<String, ArrayList<String>> calendarMap = DataManager.getInstance().calenderMap;
        json = gson.toJson(calendarMap);
        prefsEditor.putString(CALENDAR_MAP, json);
        prefsEditor.commit();

    }




    private void loadSavedObjectState() {
        Gson gson = new Gson();
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);


//        SharedPreferences.Editor editor = mPrefs.edit();
//        editor.remove(ACTIVITY_STATE);
//        editor.remove(ACTIVE_LIST);
//        editor.remove(CALENDAR_MAP);
//        editor.apply();


        if(mPrefs.contains(ACTIVITY_STATE)){

            String json = mPrefs.getString(ACTIVITY_STATE, "");
            Log.d(TAG, "Jsonnnnnn " + json);



            Type typeOfHashMap = new TypeToken<LinkedHashMap<String, ActivityObject>>() { }.getType();
            LinkedHashMap<String, ActivityObject> newMap = gson.fromJson(json, typeOfHashMap); // This type must match TypeToken
            DataManager.getInstance().activityMap = newMap;


            if(mPrefs.contains(ACTIVE_LIST)){
                json = mPrefs.getString(ACTIVE_LIST, "");
                Type type = new TypeToken<List<String>>(){}.getType();
                ArrayList<String> activeList = gson.fromJson(json, type);
                DataManager.getInstance().activeList = activeList;

            }

            if(mPrefs.contains(CALENDAR_MAP)){
                json = mPrefs.getString(CALENDAR_MAP, "");
                Log.d(TAG, "Jsonnnnnn " + json);
                Type type = new TypeToken<TreeMap<String, ArrayList<String>>>(){}.getType();
                TreeMap<String, ArrayList<String>> calendarMap = gson.fromJson(json, type);
                DataManager.getInstance().calenderMap = calendarMap;
                Log.d(TAG, "Jsonnnnnn " + DataManager.getInstance().calenderMap);
                Log.d(TAG, "Jsonnnnnn " + DataManager.getInstance().calenderMap.size());
            }

        }
        if(DataManager.getInstance().activeList != null) {
            Variables.getInstance().activeCount = DataManager.getInstance().activeList.size();
        }
    }

}
