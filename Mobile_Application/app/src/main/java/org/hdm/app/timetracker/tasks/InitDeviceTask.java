package org.hdm.app.timetracker.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.hdm.app.timetracker.models.SubjectModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * AsyncTask that is called when the IntroActivity was completed.
 *
 * Uploads the newly created subject to the server.
 * Downloads all available activities from the server to the current device.
 */
public class InitDeviceTask extends AsyncTask<SubjectModel, Integer, Boolean> {
    private final String LOG_INDICATOR = "InitDeviceTask";


    /**
     * AsyncTask callback interface
     */
    public interface InitListener {
        void onFinished(String subjectId, JSONObject activities);
    }
    private final InitListener listener;

    /**
     * Attributes
     */
    private String subjectId;
    private JSONObject activitiesJSON;
    private MaterialDialog dialog;
    private OkHttpClient client = new OkHttpClient();
    private final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * Constructor
     */
    public InitDeviceTask(InitListener _listener, MaterialDialog _dialog) {
        this.listener = _listener;
        this.dialog = _dialog;
    }

    protected Boolean doInBackground(SubjectModel... subjects) {

        this.subjectId = uploadSubject(subjects[0]);
        this.activitiesJSON = downloadActivities();

        return true;
    }

    protected void onProgressUpdate(Integer... progress) {
        dialog.incrementProgress(progress[0]);
    }

    protected void onPostExecute(Boolean result) {
        this.listener.onFinished(this.subjectId, this.activitiesJSON);
    }

    /**
     * Uploads a handed subjectModel to the server. In response it will receive the newly created
     * subject's MongoDB-id.
     *
     * @param subjectModel
     * @return MongoDB-id.
     */
    private String uploadSubject(SubjectModel subjectModel) {
        // parse SubjectModel into JSON
        Gson parser = new Gson();
        String subjectJson = parser.toJson(subjectModel);

        // create HTTP-client and build request
        RequestBody body = RequestBody.create(JSON, subjectJson);
        Request request = new Request.Builder()
                // TODO: Use real IP
                //.url("http://192.168.1.158:8080/api/subjects")
                .url("https://sambia.i-was-perfect.net/api/subjects")
                .post(body)
                .build();

        // make request and handle response
        String responseId = "";
        try {
            Response response = this.client.newCall(request).execute();

            // parse response to string
            String responseMessage = response.body().string();

            // parse string to json
            JSONObject responseJSON = new JSONObject(responseMessage);

            // get response id
            responseId = responseJSON.get("data").toString();

            publishProgress(35);
        } catch(IOException error) {
            error.printStackTrace();
        } catch(JSONException error) {
            error.printStackTrace();
        }

        return responseId;
    }

    /**
     * Downloads all enabled activities from the server.
     *
     * @return Activities as JSON
     */
    private JSONObject downloadActivities() {
        Request request = new Request.Builder()
                // TODO: Use real IP
                .url("http://192.168.1.158:8080/api/activities")
                .build();

        // make request and handle response
        JSONObject responseJSON = null;
        try {
            Response response = this.client.newCall(request).execute();

            // parse response to string
            String responseMessage = response.body().string();

            // parse string to json
            responseJSON = new JSONObject(responseMessage);
            Log.d(LOG_INDICATOR, responseJSON.toString());

            publishProgress(70);
        } catch(IOException error) {
            error.printStackTrace();
        } catch(JSONException error) {
            error.printStackTrace();
        }

        return responseJSON;
    }
}
