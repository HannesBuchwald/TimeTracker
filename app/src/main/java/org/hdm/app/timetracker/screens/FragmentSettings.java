package org.hdm.app.timetracker.screens;

/**
 * Created by Hannes on 13.05.2016.
 */

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.adapter.ActiveListAdapter;
import org.hdm.app.timetracker.adapter.ObjectListAdapter;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.listener.ActiveActivityListOnClickListener;
import org.hdm.app.timetracker.listener.ActivityListOnClickListener;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;


/**
 * This fragment representing the front of the card.
 */
public class FragmentSettings extends BaseFragemnt {

    private final String TAG = "FragmentSettings";


    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        initMenu(view);
        initLayout();

        Settings settings = new Settings();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.fragment_settings, settings).commit();
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setMenuTitle("Version 0.9");
        setMenuBackground(android.R.color.holo_red_light);
        setMenuBtn(R.drawable.ic_forward);
        menuView.findViewById(R.id.menu_tv).setOnClickListener(null);
        Log.d(TAG, "Settings on Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Settings on Pause");

    }


    /*******************
     * Life Cycle Ende
     ***********************/



    /**
     * Init the SettingsLayout
     */
    private void initLayout() {

    }



    /*******************
     * Init Ende
     ***********************/

}