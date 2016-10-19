package org.hdm.app.timetracker.screens;

/**
 * Created by Hannes on 13.05.2016.
 */

import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.MainListener;
import org.hdm.app.timetracker.listener.MenuListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.views.MenuView;

import java.util.Calendar;
import java.util.Date;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;

/**
 * A fragment representing the front of the card.
 */
public class BaseFragemnt extends Fragment implements
        MenuListener
{


    private String TAG = "BaseFragment";


    public MainListener listener;
    public MenuView menuView;
    public DataManager dataManager = DataManager.getInstance();
    public Variables var = Variables.getInstance();
    private RecyclerView rv_calender;


    public void initMenu(View view ) {
        menuView = (MenuView) view.findViewById(R.id.frag_menu);
        menuView.setListener(this);
    }

    public void initMenu(View view, RecyclerView viewById) {
        initMenu(view);
        rv_calender = viewById;
    }


    public void setMenuBackground(int i) {
        if (menuView != null) {
            menuView.setBackground(i);}
    }


    public void setMenuTitle(String title) {
        if (menuView != null) {
            menuView.setTitle(title);}
    }


    public void setMenuBtn(int d) {
        if (menuView != null) {
            menuView.setImage(d);}
    }



    public void setCalendarIconVisibility(boolean isVisible) {

        if(isVisible) {
            menuView.menu_btn_currentDate.setVisibility(View.VISIBLE);
        } else {
            menuView.menu_btn_currentDate.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void mClickInteraction(View v) {
        if(var.editable) var.editable = false;
        this.listener.flip();
    }

    @Override
    public void onClickSettingsButton() {
        if(listener != null) this.listener.displaySettingsFragment();
    }


    @Override
    public void scrollToCurrentTime(View view) {
        scrollListToCurrentTime();
    }

    public void setContext(FragmentContainer fragmentContainer) {
        this.listener = fragmentContainer;
    }


    // scroll in Calendarlist to current Time
    public void scrollListToCurrentTime() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 0);


        Date currentTime = cal.getTime();
        int difference = currentTime.getDate()- Integer.parseInt(var.fistDay);
        int offSet = difference*48;
        int hour = currentTime.getHours();
        if(DEBUGMODE) Log.d(TAG, "offSet " + offSet + " // hour " + hour);
        int position = offSet+(hour*2+3);

        if(offSet == 0 && hour < 2) position = 0;

        if(rv_calender != null) rv_calender.smoothScrollToPosition(position);
    }

}