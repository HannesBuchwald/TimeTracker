package org.hdm.app.timetracker.tasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.main.MainActivity;
import org.hdm.app.timetracker.util.Consts;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Pushes newly created logs to the server.
 *
 * During the execution a Notification is shown by the Android NotificationManager.
 */
public class PushActivitiesTask extends AsyncTask<Void, Integer, Boolean> {
    private final String LOG_INDICATOR = "PushActivitiesTask";


    /**
     * Attributes
     */
    private MainActivity activity;
    private Context context;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    /**
     * Constructor
     * @param _context
     */
    public PushActivitiesTask(Context _context) {
        this.context = _context;
    }

    protected Boolean doInBackground(Void... subjects) {
        // show update notification
        // showNotificationIcon();

        ArrayList<JSONObject> uploadArray = readFromSd();
        uploadActivities(uploadArray);

        return true;
    }

    protected void onProgressUpdate(Integer... progress) {
        //dialog.incrementProgress(progress[0]);
    }

    protected void onPostExecute(Boolean result) {
        //  this.listener.onFinished(this.subjectId, this.activitiesJSON);
    }

    /**
     * Downloads all enabled activities from the server.
     *
     * @return Activities as JSON
     */
    private JSONObject uploadActivities(ArrayList<JSONObject> _uploadArray) {
        // extract upload-data
        RequestBody body = RequestBody.create(JSON, _uploadArray.toString());

        Request request = new Request.Builder()
                // TODO: Use real IP
                //.url("http://192.168.1.158:8080/api/data")
                .url("https://sambia.i-was-perfect.net/api/data")
                .post(body)
                .build();

        // make request and handle response
        JSONObject responseJSON = null;
        try {
            Response response = this.client.newCall(request).execute();

            // parse response to string
            String responseMessage = response.body().string();

            // parse string to json
            Log.d(LOG_INDICATOR, responseMessage);
        } catch(IOException error) {
            error.printStackTrace();
        }

        return responseJSON;
    }

    /**
     * Reads all Log-files from sd-card and returns them in an array-list.
     *
     * @return True, if everything went well.
     */
    private ArrayList<JSONObject> readFromSd() {
        // result list
        ArrayList<JSONObject> resultArray = new ArrayList<JSONObject>();

        // fetch all files inside log-folder
        String logPath = Environment.getExternalStorageDirectory().toString() + "/" +
                Consts.PARENTPATH + "/" + Consts.LOGPATH;
        File logFolder = new File(logPath);
        String[] logFiles = logFolder.list();

        // loop through available log-files
        for (String fileName : logFiles) {
            Log.d(LOG_INDICATOR, fileName);

            // parse log-string into JSON-Object
            JSONObject currentLog = readFromFile(fileName);
            resultArray.add(currentLog);

            try {
                Log.d(LOG_INDICATOR, currentLog.get("date").toString());
            } catch(JSONException _error) {
                _error.printStackTrace();
            }

        }

        return resultArray;
    }

    /**
     * Reads a handed filePath of a log-file and returns it as JSON-object.
     * @param _fileName
     * @return
     */
    private JSONObject readFromFile(String _fileName) {
        JSONObject jsonResult = null;

        // get full log path
        String logPath = Environment.getExternalStorageDirectory().toString() + "/" +
                Consts.PARENTPATH + "/" + Consts.LOGPATH + "/" + _fileName;

        try {
            // read file into BufferedReader
            FileInputStream fileInput = new FileInputStream(new File(logPath));

            if (fileInput != null) {
                BufferedReader fileInputBuffered = new BufferedReader(
                        new InputStreamReader(fileInput));

                // read file data into resultBuilder
                String resultString = "";
                StringBuilder resultBuilder = new StringBuilder();
                while ((resultString = fileInputBuffered.readLine()) != null) {
                    // append each new line to resultBuilder
                    resultBuilder.append(resultString);
                }

                // close stream and parse into JSON
                fileInput.close();
                jsonResult = new JSONObject(resultBuilder.toString());
            }
        } catch(FileNotFoundException _error) {
            _error.printStackTrace();
        } catch(IOException _error) {
            _error.printStackTrace();
        } catch(JSONException _error) {
            _error.printStackTrace();
        }

        return jsonResult;
    }

    /**
     * Opens a status-indicator-notification when the updating-task was started.
     */
    private void showNotificationIcon() {

        // set MainActivity as notification action, which redirects to the last opened Activity
        /*Intent notificationIntent = new Intent(this, ChatActivity.class);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // set a "closing-broadcast" as notification close action, which signals the broadcast-
        // listener to close the whole app
        Intent closingIntent = new Intent(this, MainActivity.class);
        closingIntent.putExtra("EXIT", true);
        closingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent closePendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        closingIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );*/

        // set notification content and open it
        NotificationManager notificationManager = (NotificationManager)
                this.context.getSystemService(this.context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this.context)
                .setContentTitle("Sambia App")
                .setContentText("Checking for Updates")
                .setSmallIcon(R.drawable.settings)
                //.addAction(R.drawable.cancel_24,
                //        this.context.getString(R.string.notify_running_stop),
                //        closePendingIntent)
                //.setContentIntent(resultPendingIntent)
                .setOngoing(true)
                .build();
        notificationManager.notify(MainActivity.NOTIFICATION_ID, notification);

    }

}
