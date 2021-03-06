package org.hdm.app.timetracker.screens;

/**
 * Created by Hannes on 13.05.2016.
 */

import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.view.View;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.MainListener;
import org.hdm.app.timetracker.listener.MenuListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.views.MenuView;

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




    public void initMenu(View view) {
        menuView = (MenuView) view.findViewById(R.id.frag_menu);
        menuView.setListener(this);
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



    @Override
    public void mClickInteraction(View v) {
        if(var.editable) var.editable = false;
        this.listener.flip();
    }

    @Override
    public void onClickSettingsButton() {
        if(listener != null) this.listener.displaySettingsFragment();
    }

    public void setContext(FragmentContainer fragmentContainer) {
        this.listener = fragmentContainer;
    }



}