package org.hdm.app.timetracker.listener;

import org.hdm.app.timetracker.util.View_Holder;

/**
 * Created by Hannes on 10.06.2016.
 */
public interface ActiveActivityListOnClickListener {
    public void didOnClickOnActiveListItem(String s, View_Holder holder);
    public void didOnLongClickOnActiveListItem(String s, View_Holder holder);
}
