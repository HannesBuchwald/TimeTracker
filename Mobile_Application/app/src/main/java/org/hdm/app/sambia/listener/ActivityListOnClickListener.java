package org.hdm.app.sambia.listener;

import android.view.View;

import org.hdm.app.sambia.util.View_Holder;

/**
 * Created by Hannes on 10.06.2016.
 */
public interface ActivityListOnClickListener {
    public void didClickOnActivityListItem(String title, View_Holder holder);
    public void didLongClickOnView(String title, View_Holder view_holder);

}
