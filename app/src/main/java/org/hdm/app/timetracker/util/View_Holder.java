package org.hdm.app.timetracker.util;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.hdm.app.timetracker.adapter.CalendarListAdapter;
import org.hdm.app.timetracker.adapter.CalendarItemListAdapter;
import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.listener.ViewHolderListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Hannes on 27.05.2016.
 */
public class View_Holder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnLongClickListener {


    private final String TAG = "View_Holder";


    private ViewHolderListener listener;

    public RecyclerView rv_content;
    public  CardView cv;
    private ImageView iv_play;
    public  TextView title;
    public  TextView time;
    public  ImageView imageView;
    public Timer timer;
    Handler handler = new Handler();

    public int count = 0;
    private String titleText;
    private long countt;
    public ImageView iv_background_bottom;
    public ImageView iv_background_top;
    public CardView btn_add;

    Date startDate;
    public String id ="";

    public boolean activityList;
    private TimerTask timerTask;
    private ImageView iv_cancel;

    /************** Constructors ******************/



    // Called from the Adapter for the ActivityObjectList in Activity Screen
    public View_Holder(View v) {
        super(v);
        initActivityItemLayout();
    }


    public View_Holder(View v, CalendarListAdapter calendarListAdapter) {
        super(v);
        initCalendarLayout();
    }


    public View_Holder(View v, CalendarItemListAdapter calendarItemListAdapter) {
        super(v);
        initActivityItemLayout();
    }


    /************** Constructors  End ******************/




    private void initActivityItemLayout() {
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        time = (TextView)itemView.findViewById(R.id.tv_time);
        titleText = title.getText().toString();
        iv_cancel = (ImageView) itemView.findViewById(R.id.iv_cancel);

        cv.setOnClickListener(this);
        cv.setOnLongClickListener(this);
    }



    private void initCalendarLayout() {
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);

        rv_content = (RecyclerView) itemView.findViewById(R.id.rv_calender_item_content);
        btn_add = (CardView) itemView.findViewById((R.id.btn_calendar_row_add));
        btn_add.setOnClickListener(this);
        btn_add.setOnLongClickListener(this);

        iv_background_bottom = (ImageView) itemView.findViewById(R.id.iv_background_bottom);
        iv_background_top = (ImageView) itemView.findViewById(R.id.iv_background_top);

    }


    /************** Init  End ******************/


    public void setBackground(String blue) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lollipop
                cv.setCardBackgroundColor(Color.BLUE);
            } else {
                // lollipop and above
                cv.setBackgroundColor(cv.getResources().getColor(R.color.blue));
            }
            time.setVisibility(View.VISIBLE);
            if(activityList) runCount();

        Log.d(TAG, "here immm");
    }




    // called from the Activity Screen Interaction

    // Change CardView Style of Activitys
    // Activity == active - green background
    // Activity != active - white background
    public void setBackground(boolean state) {

        if(state) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lollipop
                cv.setCardBackgroundColor(Color.GREEN);
            } else {
                // lollipop and above
                cv.setBackgroundColor(cv.getResources().getColor(R.color.green));
            }
            time.setVisibility(View.VISIBLE);
            if(activityList) runCount();


        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lillipop
                cv.setCardBackgroundColor(Color.WHITE);
            } else {
                // lollipop and above
                cv.setBackgroundColor(cv.getResources().getColor(R.color.white));
            }
            time.setVisibility(View.GONE);
            stopCount();
        }
    }



//    public void handleTimeCounter(boolean state) {
//
//        if(state) {
//            runCount();
//        } else {
//            stopCount();
//        }
//    }



    public void runCount() {

        ActivityObject object = DataManager.getInstance().getActivityObject(title.getText().toString());
        startDate = object.startTime;

        if(timer != null) {
            stopCount();
            Log.d(TAG, "StartCount " +title);
        }

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Date currentDate = Calendar.getInstance().getTime();
                countt = (currentDate.getTime() - startDate.getTime())/1000;

                Log.d(TAG, timer.toString()+  " "  + countt);

                handler.post(new Runnable() {
                    public void run() {
                        int seconds = (int) countt % 60;
                        int minutes = (int) countt / 60;
                        int houres = minutes / 60;
                        String stringTime = String.format("%02d:%02d:%02d", houres, minutes, seconds);
                        time.setText(stringTime);
                        //count++;
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask,0, 1000);
    }


    public void stopCount() {
        if(timer != null)timer.cancel();
        if(timerTask != null) timerTask.cancel();

        Log.d(TAG, "stop counting");
    }



    // Called from the CalendarView
    public void setCalendarItemBackground(boolean editable) {

        if(editable) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lollipop
                cv.setCardBackgroundColor(Color.RED);
               iv_cancel.setVisibility(View.VISIBLE);

            } else {
                // lollipop and above
                cv.setCardBackgroundColor(cv.getResources().getColor(R.color.red));
                iv_cancel.setVisibility(View.VISIBLE);
            }
        } else {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lillipop
                cv.setCardBackgroundColor(Color.TRANSPARENT);
                iv_cancel.setVisibility(View.GONE);

            } else {
                // lollipop and above
                cv.setCardBackgroundColor(cv.getResources().getColor(R.color.transparent));
                iv_cancel.setVisibility(View.GONE);
            }
        }
    }





    
    

    /******************* Listener **************************/
    // reference parent listener
    public void setListener(ViewHolderListener listener) {
        this.listener = listener;
    }



    // Listener Interface with parent class
    @Override
    public void onClick(View v) {
        if(listener!= null) listener.didClickOnView(v, title.getText().toString(), this);
    }


    @Override
    public boolean onLongClick(View v) {
            if(listener != null) {
                if(listener!= null) listener.didLongClickOnView(v, title.getText().toString(), this);
                return true;
            }
        return false;
    }


}