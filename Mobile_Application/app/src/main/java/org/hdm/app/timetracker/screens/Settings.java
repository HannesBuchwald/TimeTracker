package org.hdm.app.timetracker.screens;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.listener.PreferenceListener;


/**
 * Created by Hannes on 14.09.2016.
 */
public class Settings extends PreferenceFragment {
    public static final String KEY_PREF_SYNC_CONN = "pref_syncConnectionType";

    PreferenceListener listener;

    Activity activity;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        initPre();

    }

    private void initPre() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
