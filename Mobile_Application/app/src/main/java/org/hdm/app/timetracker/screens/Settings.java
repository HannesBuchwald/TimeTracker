package org.hdm.app.timetracker.screens;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.listener.PreferenceListener;


/**
 * Created by Hannes on 14.09.2016.
 */
public class Settings extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private static final String TAG = "Settings";

    PreferenceListener listener;
    Preference prefActivitiesReset;

    Activity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initXML();
        initListener();
    }

    private void initListener() {

        if(mainActivity!= null) {
            listener = (PreferenceListener) mainActivity;
        }
    }

    private void initXML() {
        addPreferencesFromResource(R.xml.settings);

        prefActivitiesReset = getPreferenceManager().findPreference(getString(R.string.pref_key_reset_activities));
        prefActivitiesReset.setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (preference.equals(prefActivitiesReset)) {
            if(listener != null) listener.resetActivities();
            Log.d(TAG, "Click on Reset");
        }
        return true;
    }

}
