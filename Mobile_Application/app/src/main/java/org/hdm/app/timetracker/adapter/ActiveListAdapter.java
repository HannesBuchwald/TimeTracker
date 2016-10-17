package org.hdm.app.timetracker.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.ActiveActivityListOnClickListener;
import org.hdm.app.timetracker.listener.ViewHolderListener;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import static org.hdm.app.timetracker.util.Consts.*;


/**
 * Created by Hannes on 27.05.2016.
 */
public class ActiveListAdapter extends RecyclerView.Adapter<View_Holder> implements
        ViewHolderListener {

    private final String TAG = "ActiveListAdapter";

    public DataManager dataManager = DataManager.getInstance();
    private ActiveActivityListOnClickListener listener;

    public List<String> list = null;

    public List<View_Holder> activeList;

    android.os.Handler handler = new android.os.Handler();
    private Runnable updateRemainingTimeRunnable;
    public Timer tmr;

    public List<Timer> timerList;


    public ActiveListAdapter(ArrayList<String> activityObject) {

        this.list = activityObject;
        this.activeList = new ArrayList<>();
        this.timerList = new ArrayList<>();


//        update();
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_active, parent, false);
        View_Holder holder = new View_Holder(v);
        if(DEBUGMODE) Log.d(TAG, "Count View Holder " + holder);
        return holder;
    }

    int count = 0;


    public void update() {


        updateRemainingTimeRunnable = new Runnable() {
            @Override
            public void run() {

              if(DEBUGMODE)  Log.d(TAG, "run "+ timerList.size() +" " + count++);

                for (View_Holder holder : activeList) {


                    ActivityObject object = DataManager.getInstance().getActivityObject(holder.title.getText().toString());
                    Date startDate = object.startTime;

                    Date currentDate =  Calendar.getInstance().getTime();
                    long currentTime = currentDate.getTime();

                    if (object != null) {

                        int dayDiff =  currentDate.getDate() - startDate.getDate();
                        long timeDiff = currentTime - startDate.getTime();

                        int seconds = (int) (timeDiff / 1000) % 60;
                        int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                        int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);

                        for(int i=0;i<dayDiff;i++){
                            hours+=24;
                        }

                        String secondsStr = String.valueOf(seconds);
                        String minutesStr = String.valueOf(minutes);
                        String hoursStr = String.valueOf(hours);

                        if (seconds < 10) secondsStr = "0" + secondsStr;
                        if (minutes < 10) minutesStr = "0" + minutesStr;
                        if (hours < 10) hoursStr = "0" + hoursStr;


                        String time = hoursStr + ":" + minutesStr + ":" + secondsStr;
                        holder.updateTimeRemaining(time);
                    }
                }
            }
        };
        startUpdateTimer();
    }


    private void startUpdateTimer() {
        tmr = new Timer();
        timerList.add(tmr);
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(updateRemainingTimeRunnable);
            }
        }, 0, 1000);
    }


    @Override
    public void onBindViewHolder(View_Holder holder, int position) {


        if(!activeList.equals(holder)) activeList.add(holder);
        update();



        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        ActivityObject object = dataManager.getActivityObject(list.get(position));
        holder.setListener(this);
        holder.title.setText(object.title);
        holder.activityList = true;
        if (dataManager.imageMap.get(object.imageName) != null) {
            holder.imageView.setImageBitmap((dataManager.imageMap.get(object.imageName)));
        }

        if (object.service.equals("Yes")) {
            holder.setBackground("blue");
        } else {
            holder.setBackground(object.activeState);
        }

        if(DEBUGMODE) Log.d(TAG, "Count View Holder " + position + " " + holder.title.getText() + " " + timerList.size());

    }


    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }


    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, String activityObject) {
        list.add(position, activityObject);
        notifyItemInserted(position);
    }


    // Remove a RecyclerView item containing a specified Daata object
    public void remove(ActivityObject activityObject) {
        int position = list.indexOf(activityObject);
        list.remove(position);
        notifyItemRemoved(position);
    }


    public void setListener(ActiveActivityListOnClickListener fragmentActivity) {
        this.listener = fragmentActivity;
    }


    @Override
    public void didClickOnView(View view, String s, View_Holder holder) {
        if (listener != null) listener.didOnClickOnActiveListItem(s, holder);
    }


    @Override
    public void didLongClickOnView(View view, String s, View_Holder holder) {
        if (listener != null) listener.didOnLongClickOnActiveListItem(s, holder);

    }


    public void stopCounting() {

        if (timerList != null) {
            for (Timer timer : timerList) {
                timer.cancel();
            }
           timerList = new ArrayList<>();
            activeList = new ArrayList<>();
        }
    }
}