package org.hdm.app.timetracker.main;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.Stamp;
import org.hdm.app.timetracker.listener.PreferenceListener;
import org.hdm.app.timetracker.util.FileLoader;
import org.hdm.app.timetracker.util.Variables;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static org.hdm.app.timetracker.util.Consts.*;


// Version 1.2 - 26.10.2016

public class MainActivity extends Activity implements PreferenceListener {
    private final String TAG = "MainActivity";

    // ToDo Add Logik for Dayshift

    /**
     * Constants
     */
    Handler handler = new Handler();
    private Variables var;
    private Timer timer;
    private int count = 0;
    private DataManager dataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initConfiguration();
        loadConfigurationFromExternal();
        initCalendar();
        loadSavedObjectState();
        initVariables();
        initDeviceModies();
        initLayout();
    }


    @Override
    public void onStop() {
        super.onStop();
        var.editableMode = false;
        var.backPress = false;
        saveLogFile();
        saveCurrentState();
        if (timer != null) timer.cancel();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initSaveCurrentState();

        if(DEBUGMODE) {
            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
            Log.d(TAG, "MMM UserID " + var.user_ID + " || "+ sh.getString(getString(R.string.pref_key_user_user_id),""));
            Log.d(TAG, "MMM IP " + var.serverIP + " || "+ sh.getString(getString(R.string.pref_key_connection_ip),""));
            Log.d(TAG, "MMM Port " + var.serverPort + " || "+ sh.getString(getString(R.string.pref_key_connection_port),""));
            Log.d(TAG, "MMM Editable " + var.editableMode + " || "+ sh.getBoolean(getString(R.string.pref_key_preferences_editable_mode),false));
            Log.d(TAG, "MMM max " + var.maxRecordedActivity + " || "+ sh.getString(getString(R.string.pref_key_preferences_max_active_activities),""));
            Log.d(TAG, "MMM max " + var.minRecordingTime + " || "+ sh.getString(getString(R.string.pref_key_preferences_threshold),""));
            Log.d(TAG, "MMM lastLog " + dataManager.lastLog + " || "+ sh.getString(getString(R.string.lastLog),""));
        }

    }

    /**
     * There are to options for the Backpress Button
     * 1. if backPress flag is true that allow to use this button as backpress
     * 2. if backPressDialog is true then show a AlertDialog before quit the app
     */
    @Override
    public void onBackPressed() {

        // ToDo write test cases
        if (var.backPress) {

            // if backPressDialog is true then show a AlertDialog before quit the app
            if (Variables.getInstance().backPressDialog) {
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


    /**
     * Use the App as Homescreen App
     * with this feature is the home button override
     * That means when the user is pressing the homebutton the app will start again
     * and the user can´t get to the regular homescreen of the smartphone
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_MAIN.equals(intent.getAction())) {
        }
    }


    /*****************************
     * Init
     *******************************************/

    /**
     * Init DataManagement Singleton
     * Init Variables Singleton
     */
    private void initConfiguration() {
        // Init the Data Structure - there all the created where hosted
        DataManager.init();
        Variables.init();
        dataManager = DataManager.getInstance();
        var = Variables.getInstance();
    }


    /**
     * Load all used files like activity.json, images
     */
    private void loadConfigurationFromExternal() {
        FileLoader fl = new FileLoader(this);
        fl.initFiles();
    }


    private void initVariables() {

        if(DEBUGMODE) Log.d(TAG, "initVariables") ;

        Calendar cal = Calendar.getInstance();
        var.currentTime = cal.getTime();
        var.backPress = var.editableMode;

        // Datenabgleich zwischen SharedMemory und Variables
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(this).edit();
        prefs.putString(getString(R.string.pref_key_user_user_id), var.user_ID);

        prefs.putString(getString(R.string.pref_key_connection_ip), var.serverIP);
        prefs.putString(getString(R.string.pref_key_connection_port), var.serverPort);

        prefs.putBoolean(getString(R.string.pref_key_preferences_editable_mode), var.editableMode);
        prefs.putString(getString(R.string.pref_key_preferences_max_active_activities), String.valueOf(var.maxRecordedActivity));
        prefs.putString(getString(R.string.pref_key_preferences_threshold), String.valueOf(var.minRecordingTime));
        prefs.putString(getString(R.string.pref_key_preferences_log_interval), String.valueOf(var.logTimeInterval));
        prefs.commit();

    }


    private void initDeviceModies() {

        // Set Wifi state
        WifiManager wifiManager = (WifiManager) this.getSystemService(this.WIFI_SERVICE);
        wifiManager.setWifiEnabled(var.wifiState);

        // Set Bluetooth state
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (var.bluetoothState) {
            bluetoothAdapter.enable();
        } else {
            bluetoothAdapter.disable();
        }

        // Set Display Brightness
        float brightness = var.displayBrightness / (float) 255;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = brightness;
        getWindow().setAttributes(lp);


        // set Display TimeOut
        setTimeout(var.screenOffTimeout);
    }


    // Set Device Sleep Mode
    private void setTimeout(int screenOffTimeout) {
        int time;
        switch (screenOffTimeout) {
            case 0:
                time = 15000;
                break;
            case 1:
                time = 30000;
                break;
            case 2:
                time = 60000;
                break;
            case 3:
                time = 120000;
                break;
            case 4:
                time = 600000;
                break;
            case 5:
                time = 1800000;
                break;
            default:
                time = -1;
        }


        android.provider.Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, time);
    }


    /*
     * Init CalendarList with new Entries
     *
     * @param calendar current Time in Date
     */
    public void initCalenderMap(Date currentTime) {

        if (DEBUGMODE) Log.d(TAG, "calendar eingang " + currentTime);

        Calendar cal = Calendar.getInstance();
        if (DEBUGMODE) Log.d(TAG, "calendar new Instance " + cal.getTime());
        cal.setTime(currentTime);
        if (DEBUGMODE) Log.d(TAG, "calendar set with eingang " + cal.getTime());


        Calendar calEndTime = Calendar.getInstance();
        calEndTime.setTime(currentTime);
        if (DEBUGMODE) Log.d(TAG, "calendar end time " + calEndTime.getTime());


        Date time = cal.getTime();
        // Reset Time to 00:00:00
        time.setHours(var.startHour);
        time.setMinutes(var.startMin);
        time.setSeconds(var.startMin);

        // Set Calendar with reseted time
        cal.setTime(time);


        Date endTime = var.dateArray.get(var.dateArray.size() - 1);
        endTime.setHours(var.endHour);
        endTime.setMinutes(var.startMin);
        endTime.setSeconds(var.startMin);
        calEndTime.setTime(endTime);
        calEndTime.add(Calendar.MINUTE, -var.timeFrame);
        endTime = calEndTime.getTime();

        if (DEBUGMODE) Log.d(TAG, "calendar end time " + endTime + "  " + time);


        while (time.before(endTime)) {
            time = cal.getTime();
            dataManager.setCalenderMapEntry(time.toString(), null);
            // add 30 minutes to setTime
            cal.add(Calendar.MINUTE, var.timeFrame);
            if (DEBUGMODE)
                Log.d(TAG, "calendar time new " + time.toString() + " " + dataManager.calenderMap.size());
        }


    }


    /**
     * Save current State every 15 min
     * Run in TimerTask Thread not on UI Thread
     */
    private void initSaveCurrentState() {

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    public void run() {
                        saveLogFile();
                        if (DEBUGMODE) Log.d(TAG, "saveLogFile " + count++);
                    }
                });

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, var.logTimeInterval * 60000);
    }


    /**
     * Init the Layout
     * activate FullScreen Mode
     */
    private void initLayout() {
        setFullScreen(true);
        setContentView(R.layout.activity_main);
    }


    /**
     * Set App to fullscreen mode if boolean == true
     *
     * @param fullscreen a flag for setting the app in fullscreen mode or not
     */
    private void setFullScreen(boolean fullscreen) {
        if (fullscreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    /**
     * Save the current activities State on local storage as json format
     */
    private void saveLogFile() {
        String currentDate = Calendar.getInstance().getTime().toString();
        int size = currentDate.length();
        String year = currentDate.substring(size - 4, size);
        String date = currentDate.substring(4, 19);
        if (DEBUGMODE) Log.d(TAG, "User ID:" + var.user_ID);
        String fileName = var.user_ID + "_" + year + "_" + date + "_activities.txt";
        fileName = fileName.replaceAll(" ", "_");
        if (DEBUGMODE) Log.d(TAG, "currentDate " + fileName);
        new FileLoader(this).saveLogsOnExternal(fileName);
    }


    /**
     * Save current activity state, and CalenderMap in internal storage
     */
    private void saveCurrentState() {

        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();


        // Save ObjectActivity Map
        Map<String, ActivityObject> map = dataManager.getObjectMap();
        String json = gson.toJson(map);
        prefsEditor.putString(ACTIVITY_STATE, json);


        // Save ActiveList
        ArrayList<String> activeList = dataManager.activeList;
        json = gson.toJson(activeList);
        prefsEditor.putString(ACTIVE_LIST, json);


        // Save CalendarMap
        LinkedHashMap<String, ArrayList<String>> calendarMap = dataManager.calenderMap;
        json = gson.toJson(calendarMap);
        prefsEditor.putString(CALENDAR_MAP, json);


        List<Stamp> logList = dataManager.logList;
        json = gson.toJson(logList);
        prefsEditor.putString(LOG_LIST,json);

        prefsEditor.putString(getString(R.string.pref_key_user_user_id), var.user_ID);
        prefsEditor.putString(getString(R.string.pref_key_connection_ip), var.serverIP);
        prefsEditor.putString(getString(R.string.pref_key_connection_port), var.serverPort);
        prefsEditor.putBoolean(getString(R.string.pref_key_preferences_editable_mode), var.editableMode);
        prefsEditor.putString(getString(R.string.pref_key_preferences_max_active_activities),
                String.valueOf(var.maxRecordedActivity));
        prefsEditor.putString(getString(R.string.pref_key_preferences_threshold),
                String.valueOf(var.minRecordingTime));
        prefsEditor.putString(getString(R.string.pref_key_preferences_log_interval),
                String.valueOf(var.logTimeInterval));

        prefsEditor.putString(getString(R.string.lastLog), dataManager.lastLog);
        prefsEditor.putString(getString(R.string.firstDay), var.fistDay);
        prefsEditor.commit();
    }


    /**
     * Load the stored current states after the app is opened
     */
    private void loadSavedObjectState() {
        Gson gson = new Gson();

        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);

        if (mPrefs.contains(getString(R.string.pref_key_user_user_id))) {
            String userID = mPrefs.getString(getString(R.string.pref_key_user_user_id), "");
            var.user_ID = userID;
        }
        if (mPrefs.contains(getString(R.string.pref_key_connection_ip))) {
            String ip = mPrefs.getString(getString(R.string.pref_key_connection_ip), "");
            var.serverIP = ip;
        }
        if (mPrefs.contains(getString(R.string.pref_key_connection_port))) {
            String port = mPrefs.getString(getString(R.string.pref_key_connection_port), "");
            var.serverPort = port;
        }
        if (mPrefs.contains(getString(R.string.pref_key_preferences_editable_mode))) {
            boolean editableMode = mPrefs.getBoolean(getString(R.string.pref_key_preferences_editable_mode), false);
            var.editableMode = editableMode;
        }
        if (mPrefs.contains(getString(R.string.pref_key_preferences_max_active_activities))) {
            String max = mPrefs.getString(getString(R.string.pref_key_preferences_max_active_activities), "");
            var.maxRecordedActivity = Integer.valueOf(max);
        }

        if (mPrefs.contains(getString(R.string.pref_key_preferences_threshold))) {
            String max = mPrefs.getString(getString(R.string.pref_key_preferences_threshold), "");
            var.minRecordingTime = Integer.valueOf(max);
        }

        if (mPrefs.contains(getString(R.string.pref_key_preferences_log_interval))) {
            String max = mPrefs.getString(getString(R.string.pref_key_preferences_log_interval), "");
            var.logTimeInterval = Integer.valueOf(max);
        }

        if (mPrefs.contains(getString(R.string.lastLog))) {
            String lastLog = mPrefs.getString(getString(R.string.lastLog), "");
            dataManager.lastLog = lastLog;
        }

        if (mPrefs.contains(getString(R.string.firstDay))) {
            String lastLog = mPrefs.getString(getString(R.string.firstDay), "");
            var.fistDay = lastLog;
        }
        if (mPrefs.contains(ACTIVITY_STATE)) {

            String json = mPrefs.getString(ACTIVITY_STATE, "");
            if (DEBUGMODE) Log.d(TAG, "Jsonnnnnn " + json);

            Type typeOfHashMap = new TypeToken<LinkedHashMap<String, ActivityObject>>() {
            }.getType();
            LinkedHashMap<String, ActivityObject> newMap = gson.fromJson(json, typeOfHashMap); // This type must match TypeToken
            dataManager.activityMap = newMap;
        }


        if (mPrefs.contains(ACTIVE_LIST)) {
            String json = mPrefs.getString(ACTIVE_LIST, "");
            Type type = new TypeToken<List<String>>() {
            }.getType();
            ArrayList<String> activeList = gson.fromJson(json, type);
            dataManager.activeList = activeList;
        }

        if (mPrefs.contains(CALENDAR_MAP)) {
            String json = mPrefs.getString(CALENDAR_MAP, "");
            if (DEBUGMODE) Log.d(TAG, "Jsonnnnnn " + json);
            Type type = new TypeToken<LinkedHashMap<String, ArrayList<String>>>() {
            }.getType();
            LinkedHashMap<String, ArrayList<String>> calendarMap = gson.fromJson(json, type);
            dataManager.calenderMap = calendarMap;
            if (DEBUGMODE) Log.d(TAG, "Jsonnnnnnnnnn " + dataManager.calenderMap);
            if (DEBUGMODE)
                Log.d(TAG, "Jsonnnnnn " + dataManager.calenderMap.size());
        }

        if (mPrefs.contains(LOG_LIST)) {
            String json = mPrefs.getString(LOG_LIST, "");
            Type type = new TypeToken<List<Stamp>>() {
            }.getType();
            List<Stamp> logList = gson.fromJson(json, type);
            dataManager.logList = logList;
        }


        if (dataManager.activeList != null) {
            Variables.getInstance().activeCount = dataManager.activeList.size();
        }
    }


    /**
     * Listener from Settings Screen
     */

    @Override
    public void restore() {
        deleteAllExternalFiles();
        reload();
    }

    @Override
    public void reload() {
        saveLogFile();
        deleteCurrentActivityState();
        loadConfigurationFromExternal();
        initCalendar();
    }


    @Override
    public void sendLogFile() {

        saveLogFile();

        if (DEBUGMODE) Log.d(TAG, "Send Files Click");

        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Socket socket = new Socket(var.serverIP, Integer.parseInt(var.serverPort));

                    if (DEBUGMODE) Log.d(TAG, "Send Files create socket " + var.serverIP +" "+ var.serverPort);

                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    if (DEBUGMODE) Log.d(TAG, "Send Files new Print Writer");

                    writer.write(dataManager.lastLog);
                    if (DEBUGMODE) Log.d(TAG, "Send Files write");

                    writer.flush();
                    writer.close();
                    socket.close();
                    if (DEBUGMODE) Log.d(TAG, "Send File has been send");


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sendThread.start();
    }





    private void deleteCurrentActivityState() {

        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(ACTIVITY_STATE);
        editor.remove(ACTIVE_LIST);
        editor.remove(CALENDAR_MAP);
        editor.remove(LOG_LIST);
        editor.remove(getString(R.string.pref_key_user_user_id));
        editor.remove(getString(R.string.pref_key_connection_ip));
        editor.remove(getString(R.string.pref_key_connection_port));
        editor.remove(getString(R.string.pref_key_preferences_editable_mode));
        editor.remove(getString(R.string.pref_key_preferences_max_active_activities));
        editor.remove(getString(R.string.pref_key_preferences_threshold));
        editor.remove(getString(R.string.pref_key_preferences_log_interval));
        editor.remove(getString(R.string.lastLog));
        editor.remove(getString(R.string.firstDay));
        editor.commit();

    }

    private void deleteAllExternalFiles() {
        File imageFolder = new File(Environment.getExternalStorageDirectory(), IMAGE_FOLDER);
        File configFolder = new File(Environment.getExternalStorageDirectory(), CONFIG_FOLDER);

        FileLoader fl = new FileLoader(this);
        fl.delete(imageFolder);
        fl.delete(configFolder);
    }


    private void initCalendar() {

        Calendar calEndTime = Calendar.getInstance();
//        calEndTime.add(Calendar.DAY_OF_MONTH, -1);
        var.fistDay = String.valueOf(calEndTime.getTime().getDate());
        Log.d(TAG, "FirstDay " + var.fistDay);
        var.dateArray = new ArrayList<>();
        var.coloredDates = new ArrayList<>();
        dataManager.calenderMap = new LinkedHashMap<>();

        int size = var.amountOfDays;
        if (var.amountOfDays < 1) size = 1;

        for (int i = 0; i < size; i++) {
            var.dateArray.add(calEndTime.getTime());
            if (i % 2 == 1) var.coloredDates.add((calEndTime.getTime()));
            calEndTime.add(Calendar.DAY_OF_MONTH, 1);
            if (DEBUGMODE) Log.d(TAG, "mod " + i % 2);

        }

        if (DEBUGMODE)
            Log.d(TAG, "size " + var.coloredDates.size() + " // " + var.dateArray.size());
        initCalenderMap(var.dateArray.get(0));
    }
}
