package org.hdm.app.timetracker.util;

import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;

import java.util.Map;

/**
 * Created by Hannes on 18.08.2016.
 */
public class ActivityObjectLog {

    public Map<String, ActivityObject> activitys = DataManager.getInstance().getObjectMap();
}
