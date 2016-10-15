package org.hdm.app.timetracker.listener;

import android.view.View;

/**
 * Created by Hannes on 13.05.2016.
 */
public interface MenuListener {

    void mClickInteraction(View v);
    void onClickSettingsButton();
    void scrollToCurrentTime(View view);
}
