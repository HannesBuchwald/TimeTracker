package org.hdm.app.timetracker.util;

/**
 * Created by Hannes on 25.06.2016.
 */
public final class Consts {

    public final static boolean DEBUGMODE = true;
    public final static int CALENDARITEMROW = 1;


    public final static String IMAGEFOLDER = "imageFolder";
    public final static String PROPERTIESFILE = "config.properties";
    public final static String MAIN_FOLDER = "SambiaApp/";
    public final static String IMAGE_FOLDER = MAIN_FOLDER + "Images/";
    public final static String CONFIG_FOLDER = MAIN_FOLDER + "Config/";
    public final static String LOGS_FOLDER = MAIN_FOLDER + "Logs/";


    // Name from the Json Object
    public final static String ACTIVITIES = "activitys";
    public final static String PORTIONS = "portions";
    public final static String FOOD = "food";


    public final static String ACTIVITY_STATE = "ActivityState";
    public final static String ACTIVE_LIST = "ActiveList";
    public final static String CALENDAR_MAP = "CalendarMap";
    public final static String LOG_LIST = "LogList";


    public final static String WHITE = "white";
    public final static String BLUE = "blue";
    public final static String GREEN = "green";
    public final static String RED = "red";
    public final static String GRAY = "gray";
    public final static String TRANSPARENT = "transparent";

    public final static String EXTERNALWORK = "Yes";




    /**
     * The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     * and so on. Thus, the caller should be prevented from constructing objects of
     * this class, by declaring this private constructor.
     */
    private Consts() {
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
