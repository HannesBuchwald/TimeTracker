package org.hdm.app.sambia.listener;

import org.hdm.app.sambia.util.View_Holder;

/**
 * Created by Hannes on 10.06.2016.
 */
public interface CalendarItemOnClickListener {

    public void didOnClick(String time, String s, View_Holder holder);

    public void didOnClickAddBtn(View_Holder holder);
}
