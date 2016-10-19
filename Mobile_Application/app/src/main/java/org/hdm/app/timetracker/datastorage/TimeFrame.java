package org.hdm.app.timetracker.datastorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hannes on 03.06.2016.
 */
public class TimeFrame {
    public Date startTime;
    public Date endTime;
    public String contractWork;
    public String author;
    public String portion = "";
    public List food = new ArrayList();
}
