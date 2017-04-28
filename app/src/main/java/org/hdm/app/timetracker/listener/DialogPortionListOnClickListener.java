package org.hdm.app.timetracker.listener;

import org.hdm.app.timetracker.util.View_Holder;

/**
 * Created by Hannes on 10.06.2016.
 */
public interface DialogPortionListOnClickListener {
    public void didClickOnPortionListItem(String title, View_Holder holder);
    public void didLongClickOnPortionListItem(String title, View_Holder view_holder);
}

