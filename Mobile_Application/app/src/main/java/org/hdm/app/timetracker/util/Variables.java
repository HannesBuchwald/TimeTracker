package org.hdm.app.timetracker.util;

/**
 * Created by Hannes on 06.05.2016.
 */
public class Variables {

    private final String TAG = "Variables";
    // Instance from DaataManager
    private static Variables instance = null;




    private int timeTreshold = 1;



    /***** Activity Screen *****/
    public int activityListRows = 3;
    public int activeListRow = 1;
    public int activeCount = 0;


    // Editable Flag in Calendar Screen
    public boolean editable = false;
    public String selectedTime = "";


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
