package org.hdm.app.timetracker.screens;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.listener.PreferenceListener;
import org.hdm.app.timetracker.util.Variables;


/**
 * Created by Hannes on 14.09.2016.
 */
public class Settings extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String TAG = "Settings";

    PreferenceListener listener;
    private Preference prefActivitiesReset;
    private Preference preEditableMode;

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



    private void initXML() {
        addPreferencesFromResource(R.xml.settings);

        prefActivitiesReset = getPreferenceManager().findPreference(getString(R.string.pref_key_reset_activities));
        preEditableMode = getPreferenceManager().findPreference(getString(R.string.pref_key_editable_mode));
    }

    private void initListener() {

        if(mainActivity!= null) listener = (PreferenceListener) mainActivity;

        prefActivitiesReset.setOnPreferenceClickListener(this);
        preEditableMode.setOnPreferenceChangeListener(this);
    }



    private void initConditions() {

//        if(preEditableMode != null) preEditableMode.(Variables.getInstance().editableMode);
//
//        boolean a = getPreferenceManager().getSharedPreferences().getBoolean(getString(R.string.pref_key_editable_mode), true);
//        Log.d(TAG, "boolean " + a + " " + Variables.getInstance().editableMode);

    }

    @Override
    public void onResume() {
        super.onResume();
        initConditions();
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

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference.equals(preEditableMode)) {
            if(newValue instanceof Boolean){
                Variables.getInstance().editableMode = (Boolean)newValue;
            }
        }

        return true;
    }
}
