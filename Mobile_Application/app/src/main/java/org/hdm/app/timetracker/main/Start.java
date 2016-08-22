package org.hdm.app.timetracker.main;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.util.FileLoader;
import org.hdm.app.timetracker.util.Variables;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hannes on 17.08.2016.
 */
public class Start extends Application{


    @Override
    public void onCreate() {
        super.onCreate();

        DataManager.init();
        Variables.init();

        FileLoader fl = new FileLoader(this);
        fl.initFiles();

        initNotif();

    }



    private void initNotif(){

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                initNotification();
            }
        };

        timer.scheduleAtFixedRate(timerTask,Variables.getInstance().notificationPeriode, Variables.getInstance().notificationPeriode);
    }


    private void initNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("TimeTracker")
                        .setContentText("Wath you are doing currently")
//                .setWhen(System.currentTimeMillis())
                ;

        //Vibration
        int vibr = Variables.getInstance().vibrationTime;
        mBuilder.setVibrate(new long[] { vibr, vibr, vibr, vibr, vibr });

        Intent startIntent = new Intent(this.getBaseContext(), MainActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.getBaseContext().startActivity(startIntent);

        Intent resultIntent = new Intent(this, this.getClass());
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, startIntent, 0);

        mBuilder.setContentIntent(pendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
