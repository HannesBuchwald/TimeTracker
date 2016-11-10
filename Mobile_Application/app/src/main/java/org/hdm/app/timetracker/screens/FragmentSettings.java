package org.hdm.app.timetracker.screens;

/**
 * Created by Hannes on 13.05.2016.
 */

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;


/**
 * This fragment representing the front of the card.
 */
public class FragmentSettings extends BaseFragemnt {

    private final String TAG = "FragmentSettings";


    private View view;
    private WifiManager wifiManager;


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


        initWifi();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setMenuTitle("Settings");
        setMenuBackground(android.R.color.holo_red_light);
        setMenuBtn(R.drawable.ic_forward);
        menuView.findViewById(R.id.menu_tv).setOnClickListener(null);
        if(DEBUGMODE) Log.d(TAG, "Settings on Resume");

        if(wifiManager!= null) wifiManager.setWifiEnabled(true);
        if(DEBUGMODE) Log.d(TAG, "Wifi is on" + wifiManager.isWifiEnabled());

    }

    @Override
    public void onPause() {
        super.onPause();
        if(DEBUGMODE) Log.d(TAG, "Settings on Pause");

        if(wifiManager!= null) wifiManager.setWifiEnabled(false);
        if(DEBUGMODE) Log.d(TAG, "Wifi is on" + wifiManager.isWifiEnabled());
    }


    /*******************
     * Life Cycle Ende
     ***********************/



    /**
     * Init the SettingsLayout
     */
    private void initLayout() {

    }

    private void initWifi() {
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
    }


    /*******************
     * Init Ende
     ***********************/

}