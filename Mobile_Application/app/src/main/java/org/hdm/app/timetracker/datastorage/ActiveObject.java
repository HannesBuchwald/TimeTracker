package org.hdm.app.timetracker.datastorage;

import java.util.Date;
import java.util.List;

/**
 * Created by Hannes on 03.06.2016.
 */
public class ActiveObject {

    public String id = "";
    public String title = "";

    public Date startTime;
    public Date endTime;

    public boolean contractWork;
    public String author = "";
    public String delete = "";
}
