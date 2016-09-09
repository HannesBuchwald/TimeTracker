package org.hdm.app.timetracker.util;

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
    public String user_ID = "01";


    /**
     * Which activity setup should be loaded
     */
    public String setup = "standard";


    /**
     * Amount of maximal recorded activities at the same time
     */
    public int maxRecordedActivity = 2;


    /**
     * Log Interval in minutes
     */
    public long logTimeInterval = 1;







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
    public long minRecordingTime = 0;





    /***********
     * MainActivity Variables
     ***********/


    /**
     * Backpress flags
     */
    public boolean backPress = true;
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
