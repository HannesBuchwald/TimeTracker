package org.hdm.app.timetracker.main;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.hdm.app.timetracker.IntroActivity;
import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.util.FileLoader;
import org.hdm.app.timetracker.util.Settings;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    int vibr = 100;  // in ms
    int notifytime = 10000;  // in ms
    private NotificationManager nmgr;
    private Notification noti;
    private boolean appIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // initFirstStart();
        initConfiguration();
        initCalenderMap();
        setFullScreen(true);
        setContentView(R.layout.activity_main);
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
//        fl.saveActivityStateOnExternal();
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
//        calEndTime.add(Calendar.DAY_OF_WEEK, 1);
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

}
