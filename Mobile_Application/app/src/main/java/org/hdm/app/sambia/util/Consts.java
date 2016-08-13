package org.hdm.app.sambia.util;

/**
 * Created by Hannes on 25.06.2016.
 */
public final class Consts {

    public final static boolean DEBUGMODE = true;


    public final static int CALENDARITEMROW = 1;

    public final static String JSONFILE = "activitys.json";
    public final static String TEMPACTIVITIES = "temp-activities.json";
    public final static String PARENTPATH = "SambiaApp";
    public final static String IMAGEPATH = "Images";
    public final static String CONFIGPATH = "Config";
    public final static String LOGPATH = "Logs";

    public final static String ACTIVITIES = "activitys";

    public final static String MAINFOLDER = "mainFolder";
    public final static String IMAGEFOLDER = "imageFolder";
    public final static String CONFIGFOLDER = "configFolder";
    public final static String LOGFOLDER = "logFolder";

    public final static String PROPERTIESFILE = "folder.properties";





    /**
     The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
     and so on. Thus, the caller should be prevented from constructing objects of
     this class, by declaring this private constructor.
     */
    private Consts(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
