package org.hdm.app.timetracker.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.hdm.app.timetracker.IntroActivity;
import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.util.FileLoader;
import org.hdm.app.timetracker.util.Settings;
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

    /**
     * Constants
     */
    public static final int NOTIFICATION_ID = 100;

    /**
     * Attributes
     */
    int startMin = 0;
    int startHour = 6;
    int endHour = 24;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // initFirstStart();
        initConfiguration();
        initCalenderMap();
        initDataLogger();
//        initResetRecordedData();
        loadSavedObjectState();
        setFullScreen(true);
        setContentView(R.layout.activity_main);
        


        Log.d(TAG, "ImageSize " + DataManager.getInstance().imageMap.size());

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


    Handler handler = new Handler();

    private void initDataLogger() {

       final FileLoader fl = new FileLoader(this);

       Timer timer = new Timer();
       TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                final Date currentDate = Calendar.getInstance().getTime();

                Log.d(TAG, "currentDate " + currentDate.toString());

                Calendar calEndTime = Calendar.getInstance();
                Date endTime = calEndTime.getTime();
                endTime.setSeconds(00);
                endTime.setMinutes(14);
                endTime.setHours(23);
                calEndTime.setTime(endTime);
                final Date time = calEndTime.getTime();


                endTime.setHours(4);
                calEndTime.setTime(endTime);
                final Date startTime = calEndTime.getTime();

//                Log.d(TAG, "time " + time.toString() );
//                Log.d(TAG, "timee " + currentDate.toString());
//                Log.d(TAG, "timeee " + startTime.toString());

                handler.post(new Runnable() {
                    public void run() {

                        if(currentDate.before(time) && currentDate.after(startTime)){
                            fl.saveLogsOnExternal();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0, 900000);
    }


    private void loadSavedObjectState() {
        Gson gson = new Gson();
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);


        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove("MyObject");
        editor.remove("ActiveList");
        editor.remove("CalendarMap");
        editor.apply();


        if(mPrefs.contains("MyObject")){

            String json = mPrefs.getString("MyObject", "");
            Log.d(TAG, "Jsonnnnnn " + json);



            Type typeOfHashMap = new TypeToken<LinkedHashMap<String, ActivityObject>>() { }.getType();
            LinkedHashMap<String, ActivityObject> newMap = gson.fromJson(json, typeOfHashMap); // This type must match TypeToken
            DataManager.getInstance().activityMap = newMap;


            if(mPrefs.contains("ActiveList")){
                json = mPrefs.getString("ActiveList", "");
                Type type = new TypeToken<List<String>>(){}.getType();
                ArrayList<String> activeList = gson.fromJson(json, type);
                DataManager.getInstance().activeList = activeList;

            }

            if(mPrefs.contains("CalendarMap")){
                json = mPrefs.getString("CalendarMap", "");
                Log.d(TAG, "Jsonnnnnn " + json);
                Type type = new TypeToken<TreeMap<String, ArrayList<String>>>(){}.getType();
                TreeMap<String, ArrayList<String>> calendarMap = gson.fromJson(json, type);
                DataManager.getInstance().calenderMap = calendarMap;
                Log.d(TAG, "Jsonnnnnn " + DataManager.getInstance().calenderMap);
                Log.d(TAG, "Jsonnnnnn " + DataManager.getInstance().calenderMap.size());

            }

        }
        Variables.getInstance().activeCount = DataManager.getInstance().activeList.size();
    }

    // Do nothing on backpress - for ui reason - simplify navigation
    @Override
    public void onBackPressed() {
        // Your Code Here. Leave empty if you want nothing to happen on back press.
    }



    @Override
    public void onStop() {
        super.onStop();
        // ToDo Save File on External
        // ToDo Upload to Server
        // ToDo Save Objects when App is closed
        if(DEBUGMODE) Log.d(TAG, "onStop");
        FileLoader fl = new FileLoader(this);
        fl.saveLogsOnExternal();
        saveCurrentState();
//        startActivity(new Intent(this, MainActivity.class));
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

            Toast.makeText(this, "You pressed the home button!", Toast.LENGTH_LONG).show();
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
//            return true;
        }
//        return super.onKeyDown(keyCode, event);
        return true;
    }


    private void setFullScreen(boolean fullscreen) {

        if (fullscreen) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }










    /*****************************
     * Init
     *******************************************/

    /**
     * Checks if the user started the app for the first time. If true he is redirected to
     * the IntroActivity.
     */
    private void initFirstStart() {
        boolean firstStart = Settings.isFirstRun(getBaseContext());

        // did the user start the app for the first time?
        if(firstStart) {
            // true, show intro
            Intent intentIntro = new Intent(this, IntroActivity.class);
            startActivity(intentIntro);
        }
    }




    private void initConfiguration() {
        // Init the Data Structure - there all the created where hosted
        DataManager.init();
        Variables.init();

        FileLoader fl = new FileLoader(this);
        fl.initFiles();
    }




    private void initCalenderMap() {

        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();

        // Reset Time to 00:00:00
        time.setHours(startHour);
        time.setMinutes(startMin);
        time.setSeconds(startMin);


        // Set Calendar with reseted time
        cal.setTime(time);


        Calendar calEndTime = Calendar.getInstance();
        Date endTime = calEndTime.getTime();
        endTime.setHours(endHour);
        endTime.setMinutes(startMin);
        endTime.setSeconds(startMin);
        calEndTime.setTime(endTime);
        calEndTime.add(Calendar.MINUTE, -15);
        endTime = calEndTime.getTime();


        while(time.before(endTime)) {
            time = cal.getTime();
//            Log.d(TAG, "startTime "+  time);
            DataManager.getInstance().setCalenderMapEntry(time.toString(), null);
            // add 15 minutes to setTime
            cal.add(Calendar.MINUTE, 15);
            startMin++;
        }
    }

    /**
     * Checks for updates, e.g. overwrites the activites.json if it was changed on the server.
     * Also pushes new logged data to the server.
     */
    private void syncWithServer() {
//        new PullActivitiesTask(this).execute();
//        new PushActivitiesTask(this).execute();
    }



    private void saveCurrentState() {

        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();

        // Save ObjectActivity Map
        Map<String, ActivityObject> map = DataManager.getInstance().getObjectMap();
        String json = gson.toJson(map);
        prefsEditor.putString("MyObject", json);


        // Save ActiveList
        ArrayList<String> activeList = DataManager.getInstance().activeList;
        json = gson.toJson(activeList);
        prefsEditor.putString("ActiveList", json);


        // Save CalendarMap
        TreeMap<String, ArrayList<String>> calendarMap = DataManager.getInstance().calenderMap;
        json = gson.toJson(calendarMap);
        prefsEditor.putString("CalendarMap", json);

        prefsEditor.commit();
    }

}
