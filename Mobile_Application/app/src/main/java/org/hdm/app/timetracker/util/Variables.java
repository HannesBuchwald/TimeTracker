package org.hdm.app.timetracker.util;

/**
 * Created by Hannes on 06.05.2016.
 */
public class Variables {

    private final String TAG = "Variables";
    // Instance from DaataManager
    private static Variables instance = null;



    // Name of the User
    public String user_ID = "01";

    // which activity setup should be loaded
    public String setup = "standard";

    // Amount of maximal recorded activities at the same time
    public int maxRecordedActivity = 2;

    // amount of rows for the activity list
    public int listRows = 2;

    // Time of Notification Vibration in ms
    public int vibrationTime = 5000;

    // Period of notificaton in ms
    public int notificationPeriode = 900000;






    /***** Activity Screen *****/
    public int activeListRow = 1;
    public int activeCount = 0;


    // Editable Flag in Calendar Screen
    public boolean editable = false;
    public String selectedTime = "";
    public long minRecordingTime = 1000;


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
