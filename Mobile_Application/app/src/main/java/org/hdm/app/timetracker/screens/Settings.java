package org.hdm.app.timetracker.screens;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.listener.PreferenceListener;
import org.hdm.app.timetracker.util.Variables;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;

/**
 * Created by Hannes on 14.09.2016.
 */
public class Settings extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private static final String TAG = "Settings";

    private PreferenceListener listener;
    private Activity mainActivity;

    private Preference prefRestore;
    private Preference prefEditableMode;
    private Preference prefUserID;
    private Preference prefConnectionSend;
    private Preference prefConnectionIP;
    private Preference prefConnectionPort;
    private Preference prefMaxActivities;
    private Preference prefThreshold;
    private Preference prefReload;


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




    @Override
    public void onResume() {
        super.onResume();
        initConditions();

    }

    @Override
    public void onPause() {
        super.onPause();
    }



    private void initXML() {

        addPreferencesFromResource(R.xml.settings);

        prefUserID = getPreferenceManager().findPreference(getString(R.string.pref_key_user_user_id));

        prefConnectionSend = getPreferenceManager().findPreference(getString(R.string.pref_key_connection_send));
        prefConnectionIP = getPreferenceManager().findPreference(getString(R.string.pref_key_connection_ip));
        prefConnectionPort = getPreferenceManager().findPreference(getString(R.string.pref_key_connection_port));

        prefEditableMode = getPreferenceManager().findPreference(getString(R.string.pref_key_preferences_editable_mode));
        prefMaxActivities = getPreferenceManager().findPreference(getString(R.string.pref_key_preferences_max_active_activities));
        prefThreshold = getPreferenceManager().findPreference(getString(R.string.pref_key_preferences_threshold));
        prefRestore = getPreferenceManager().findPreference(getString(R.string.pref_key_preferences_restore));
        prefReload = getPreferenceManager().findPreference(getString(R.string.pref_key_preferences_reload));
    }

    private void initListener() {

        if (mainActivity != null) listener = (PreferenceListener) mainActivity;

        prefUserID.setOnPreferenceChangeListener(this);

        prefConnectionSend.setOnPreferenceClickListener(this);
        prefConnectionIP.setOnPreferenceChangeListener(this);
        prefConnectionPort.setOnPreferenceChangeListener(this);

        prefEditableMode.setOnPreferenceChangeListener(this);
        prefMaxActivities.setOnPreferenceChangeListener(this);
        prefThreshold.setOnPreferenceChangeListener(this);
        prefRestore.setOnPreferenceClickListener(this);
        prefReload.setOnPreferenceClickListener(this);

    }





    private void initConditions() {

        if (prefUserID != null) {
            prefUserID.setTitle("User ID: " + Variables.getInstance().user_ID);
        }

        if (prefConnectionSend != null) {
            prefConnectionSend.setEnabled(Variables.getInstance().serverConnection);
        }

        if (prefConnectionIP != null) {
            prefConnectionIP.setTitle("Port: " + Variables.getInstance().serverIP);
        }

        if (prefConnectionPort != null) {
            prefConnectionPort.setTitle("Port: " + Variables.getInstance().serverPort);
        }

        if (prefMaxActivities != null) {
            prefMaxActivities.setTitle("Max. active Activities: " + Variables.getInstance().maxRecordedActivity);
        }

        if (prefThreshold != null) {
            prefThreshold.setTitle("Threshold Minutes: " + Variables.getInstance().minRecordingTime);
        }
    }



    @Override
    public boolean onPreferenceClick(Preference preference) {



        if (preference.equals(prefConnectionSend)) {
            if (listener != null) listener.sendLogFile();
        }
        if (preference.equals(prefRestore)){
            if (listener != null) listener.restore();
        }
        if (preference.equals(prefReload)){
            if (listener != null) listener.reload();
        }


        return true;
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference.equals(prefEditableMode)) {
            if (newValue instanceof Boolean) {
                Variables.getInstance().editableMode = (Boolean) newValue;
                Variables.getInstance().backPress = (Boolean) newValue;
            }
        }

        else if (preference.equals(prefUserID)) {
            if (newValue instanceof String) {
                Variables.getInstance().user_ID = (String) newValue;
                prefUserID.setTitle("User ID: " + Variables.getInstance().user_ID);
                if(DEBUGMODE) Log.d(TAG, "User ID: " + Variables.getInstance().user_ID);
            }
        }

        else if (preference.equals(prefConnectionIP)) {
            if (newValue instanceof String) {
                Variables.getInstance().serverIP = (String) newValue;
                prefConnectionIP.setTitle("IP: " + Variables.getInstance().serverIP);
            }
        }

        else if (preference.equals(prefConnectionPort)) {

            if (newValue instanceof String) {
                Variables.getInstance().serverPort = (String) newValue;
                prefConnectionPort.setTitle("Port: " + Variables.getInstance().serverPort);
            }

        } else if(preference.equals(prefMaxActivities)) {

            if (newValue instanceof String){
                int value = Integer.valueOf((String) newValue);
                if(value >= 1 && value<=100) {
                    Variables.getInstance().maxRecordedActivity = value;
                    prefMaxActivities.setTitle("Max. active Activities: " + value);
                } else {
                    Toast.makeText(getActivity(),
                            "Only a number between 1 - 100 is allowed",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } else if(preference.equals(prefThreshold)) {

            if (newValue instanceof String) {
                int value = Integer.valueOf((String) newValue);
                if (value >= 0 && value <= 30) {
                    Variables.getInstance().minRecordingTime = value;
                    prefThreshold.setTitle("Threshold Minutes: " + value);
                } else {
                    Toast.makeText(getActivity(),
                            "Only a number between 0 - 30 is allowed",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        return true;
    }
}
