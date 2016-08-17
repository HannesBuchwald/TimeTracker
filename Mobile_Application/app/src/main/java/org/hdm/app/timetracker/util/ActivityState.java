package org.hdm.app.timetracker.util;

import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Hannes on 30.07.2016.
 */
public class ActivityState {


    public List<ActivityObject> activitys = new ArrayList<>();
    public ActivityState() {
        createUser();
    }
    private void createUser(){

        Map<String, ActivityObject> map = DataManager.getInstance().getObjectMap();
        for (Map.Entry<String, ActivityObject> entry : map.entrySet()) {
            ActivityObject object = entry.getValue();
            activitys.add(object);
        }
    }
}
