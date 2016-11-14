package org.hdm.app.timetracker.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hannes on 06.05.2016.
 */
public class Variables {

    private final String TAG = "Variables";
    // Instance from DaataManager
    private static Variables instance = null;




    /***********
     * General Variables
     *************+/

    /**
     * UserName from the person which is using the application
     */
    public String user_ID = "xxx";


    /**
     * Which activity setup should be loaded
     */
    public String setup = "standard";


    /**
     * Amount of maximal recorded activities at the same time
     */
    public int maxRecordedActivity = 3;


    /**
     * Log Interval in minutes
     */
    public long logTimeInterval = 15;







    /**********
     * Activity Screen Variables
     ************/

    /**
     * Amount of rows for the objectList
     */
    public int listRows = 2;

    /**
     * Amount of rows for the activeList
     */
    public int activeListRow = 1;

    /**
     * Amount of active Activitys
     */
    public int activeCount = 0;





    /**********
     * Calendar Screen Variables
     ************/

    // Editable Flag in Calendar Screen
    public boolean editable = false;
    public String selectedTime = "";

    /**
     * This time is the minimum time which an activity must be recorded
     * to be displayed in CalendarList.
     *
     * the value represent minutes
     */
    public long minRecordingTime = 5;





    /***********
     * MainActivity Variables
     ***********/


    /**
     * Backpress flags
     */
    public boolean backPress = false;
    public boolean backPressDialog = false;





    /**********
     * RecordTime Variables
     ***************/

    /**
     * TimeSlot in calendar screen
     */
    public int timeFrame = 30;

    /**
     * Start min of recordTime in calendar screen
     */
    public int startMin = 0;

    /**
     * Start hour of recordTime in calendar screen
     */
    public int startHour = 0;

    /**
     * End hour of recordTime in calendar screen
     */
    public int endHour = 24;


    /**
     * Current Timestamp
     */
    public Date currentTime;

    /**
     * active Activities - CashList for active Activities
     */
    public ArrayList<String> activeActivities;


    /**
     * Settings enabler
     */
    public int settingsCounter = 10;

    /**
     * Flag for editable Mode is active
     */
    public boolean editableMode = true;

    public int shortClickCounter = 3;
    public long shortClickCounterResetTime = 4000;  //ms


    /**
     * Status if Smartphone is connected to server
     */
    public boolean serverConnection = true;
    public String serverPort = "4460";
    public String serverIP = "192.168.2.117";

    /**
     *  Variable for Date in CalendarView
     */
    public String fistDay;
    public List<Date> dateArray;
    public List<Date> coloredDates;


    public int amountOfDays = 4; // set value not under 1


    //********* Device Parameter *********/


    /**
     * Device WifiState flag
     */
    public boolean wifiState = false;

    /**
     * Device Bluetooth flag
     */
    public boolean bluetoothState = false;


    /**
     * Device Screen Off
     */
    public int screenOffTimeout = 1;

    /**
     * Device Display brightness
     */
    public int displayBrightness = 100;

    /**
     * Device Show toast messages to comunicate with user
     */
    public boolean showText = true;


    /***********
     * Singelton pattern
     ***********/

    public static void init() {
        if (instance == null) {
            instance = new Variables();
        }
    }

    public static Variables getInstance() {
        if (instance == null) {
            instance = new Variables();
        }
        return instance;
    }
}
