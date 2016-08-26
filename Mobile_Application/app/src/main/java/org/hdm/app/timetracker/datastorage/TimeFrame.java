package org.hdm.app.timetracker.datastorage;

import java.util.Date;

/**
 * Created by Hannes on 03.06.2016.
 */
public class TimeFrame {

    public Date startTime;
    public Date endTime;
    public String service;
    public String whereFrom;

    public TimeFrame(Date startTime, Date endTime, String service, String whereFrom) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.service = service;
        this.whereFrom = whereFrom;
    }
}
