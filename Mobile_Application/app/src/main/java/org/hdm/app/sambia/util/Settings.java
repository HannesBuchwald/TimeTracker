package org.hdm.app.sambia.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.securepreferences.SecurePreferences;

import java.security.GeneralSecurityException;

/**
 * Helper-methods for managing persistent SharedPreferences.
 * All preferences will be encrypted with AES.
 */
public class Settings {
    static final String LOG_TAG = "Settings";

    /**
     * Constants
     */
    public static final String PREF_PLAIN_ID = "SAMBIA_PREFS_PLAIN"; // unencrypted settings
    public static final String PREF_ID = "SAMBIA_PREFS";
    public static final String PREF_SUBJECT_ID = "SAMBIA_USER_ID";
    public static final String PREF_UPDATE_VERSION = "SAMBIA_UPDATE_VERSION";
    public static final String PREF_LAST_PUSH = "SAMBIA_LAST_PUSH";
    public static final String PREF_PLAIN_STARTED = "SUS_PLAIN_STARTED"; // init reference
    private static final String PREF_UNENCRYPTED_NAME = "encrypted_prefs.xml";

    /**
     * Exceptions
     */
    private static final String NO_PASSWORD_EXCEPTION = "No password entered";
    private static final String INVALID_PASSWORD_EXCEPTION = "Entered password is not valid";

    /**
     * Singleton
     */
    private static Settings singleInstance;

    /**
     * Data
     */
    private SharedPreferences settings;
    private String settingsPassword;

    /**
     * Constructor.
     *
     * @param settings
     * @param password
     */
    public Settings(SharedPreferences settings, String password) {
        this.settings = settings;
        this.settingsPassword = password;

        Log.d(LOG_TAG, "Settings Instance created");
    }

    /**
     * Maintains a static reference to the lone singleton instance and returns the reference from.
     *
     * @return Settings instance
     */
    public static Settings getInstance() throws RuntimeException {
        // is there already an instance of the class?
        if(singleInstance == null) {
            return null;
        }

        return singleInstance;
    }

    /**
     * Checks weather the app was started for the first time (and needs initialization).
     *
     * @param _context Application context.
     * @return True, if first time.
     */
    public static boolean isFirstRun(Context _context) {
        // get unencrypted settings and check if PREF_PLAIN_STARTED was initialized yet
        SharedPreferences plainPrefs = _context.getSharedPreferences(PREF_PLAIN_ID,
                Context.MODE_PRIVATE);

        if(plainPrefs.getBoolean(PREF_PLAIN_STARTED, true)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * No more initialization needed, marks it in the unencrypted settings.
     *
     * @param _context Application context.
     */
    public static void disableFirstRun(Context _context) {
        // get unencrypted settings and mark PREF_PLAIN_STARTED as initialized
        SharedPreferences plainPrefs = _context.getSharedPreferences(PREF_PLAIN_ID,
                Context.MODE_PRIVATE);

        plainPrefs.edit().putBoolean(PREF_PLAIN_STARTED, false).commit();
    }

    /**
     * Initializes the encrypted settings, with all needed default values.
     *
     * @param _context Application context.
     * @param _password Application password.
     * @param _subjectId Username inside chat.
     * @param _updateVersion Update version..
     */
    public static void initSettings(Context _context, String _password, String _subjectId,
                                     String _updateVersion) {

        SharedPreferences setupPreferences = new SecurePreferences(_context, _password,
                PREF_UNENCRYPTED_NAME);

        // add values
        SharedPreferences.Editor setupEditor = setupPreferences.edit();
        setupEditor.putString(Settings.PREF_SUBJECT_ID, _subjectId);
        setupEditor.putString(Settings.PREF_UPDATE_VERSION, _updateVersion);
        setupEditor.putString(Settings.PREF_LAST_PUSH, "");
        setupEditor.commit();

        Log.d(LOG_TAG, "subjectId: " + setupPreferences.getString(Settings.PREF_SUBJECT_ID, null));
    }

    /**
     * Checks if the handed password is valid for unlocking the encrypted settings.
     *
     * @param _context Application context.
     * @param _password Entered password.
     * @return True, if correct.
     */
    public static boolean unlockSettings(Context _context, String _password) {

        // try to open locked settings and ask for a value
        SharedPreferences settings = new SecurePreferences(_context, _password,
                PREF_UNENCRYPTED_NAME);
        if(settings.getString(Settings.PREF_SUBJECT_ID, "default") == null) {
            // value not available, password has to be invalid
            return false;
        }
        else {
            // valid password, create an instance
            singleInstance = new Settings(settings, _password);

            return true;
        }
    }

    /**
     * Updates the current application password with a new password, if the old one is correct.
     *
     * @param _context Application context calling the method.
     * @param _oldPassword Current application password.
     * @param _newPassword New application password.
     * @return True, if the update was successfully.
     */
    public static boolean changePassword(Context _context, String _oldPassword, String _newPassword) {
        SecurePreferences securePrefs = new SecurePreferences(_context, _oldPassword,
                PREF_UNENCRYPTED_NAME);
        try {
            securePrefs.handlePasswordChange(_newPassword, _context);
        } catch(GeneralSecurityException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }
}
