package org.hdm.app.timetracker.util;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.hdm.app.timetracker.adapter.CalendarListAdapter;
import org.hdm.app.timetracker.adapter.CalendarItemListAdapter;
import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.listener.ViewHolderListener;

import static org.hdm.app.timetracker.util.Consts.BLUE;
import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;
import static org.hdm.app.timetracker.util.Consts.GRAY;
import static org.hdm.app.timetracker.util.Consts.GREEN;
import static org.hdm.app.timetracker.util.Consts.RED;
import static org.hdm.app.timetracker.util.Consts.TRANSPARENT;
import static org.hdm.app.timetracker.util.Consts.WHITE;


/**
 * Created by Hannes on 27.05.2016.
 */
public class View_Holder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnLongClickListener {


    private final String TAG = "View_Holder";


    private ViewHolderListener listener;

    public RecyclerView rv_content;
    public CardView cv;
    private ImageView iv_play;
    public TextView title;
    public TextView time;
    public ImageView imageView;

    private String titleText;
    public ImageView iv_background_bottom;
    public ImageView iv_background_top;
    public CardView btn_add;

    public String id = "";

    public boolean activityList;
    private ImageView iv_cancel;
    public int count = 0;
    public String setID;

    /**************
     * Constructors
     ******************/


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


    /**************
     * Constructors  End
     ******************/


    private void initActivityItemLayout() {
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        time = (TextView) itemView.findViewById(R.id.tv_time);
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


    /**************
     * Init  End
     ******************/


    public void setBackground(String color) {

        switch (color) {

            case WHITE:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                    cv.setCardBackgroundColor(Color.WHITE);
                } else {
                    cv.setBackgroundColor(cv.getResources().getColor(R.color.white));
                }
                break;


            case BLUE:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                    cv.setCardBackgroundColor(Color.BLUE);
                } else {
                    cv.setBackgroundColor(cv.getResources().getColor(R.color.blue));
                }
                break;


            case GREEN:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                    cv.setCardBackgroundColor(Color.GREEN);
                } else {
                    cv.setBackgroundColor(cv.getResources().getColor(R.color.green));
                }
                break;

            case RED:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                    cv.setCardBackgroundColor(Color.RED);
                    iv_cancel.setVisibility(View.VISIBLE);

                } else {
                    cv.setCardBackgroundColor(cv.getResources().getColor(R.color.red));
                    iv_cancel.setVisibility(View.VISIBLE);
                }
                break;

            case GRAY:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                    cv.setCardBackgroundColor(Color.GRAY);
                    iv_cancel.setVisibility(View.VISIBLE);

                } else {
                    cv.setCardBackgroundColor(cv.getResources().getColor(R.color.gray));
                    iv_cancel.setVisibility(View.VISIBLE);
                }
                break;

            case TRANSPARENT:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                    cv.setCardBackgroundColor(Color.TRANSPARENT);
                    iv_cancel.setVisibility(View.GONE);
                } else {
                    cv.setCardBackgroundColor(cv.getResources().getColor(R.color.transparent));
                    iv_cancel.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }

    }


    // called from the Activity Screen Interaction

    // Change CardView Style of Activitys
    // Activity == active - green background
    // Activity != active - white background
    public void setBackground(boolean state) {

        if (state) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lollipop
                cv.setCardBackgroundColor(Color.GREEN);
            } else {
                // lollipop and above
                cv.setBackgroundColor(cv.getResources().getColor(R.color.green));
            }

        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lillipop
                cv.setCardBackgroundColor(Color.WHITE);
            } else {
                // lollipop and above
                cv.setBackgroundColor(cv.getResources().getColor(R.color.white));
            }

        }
    }


    // Called from the CalendarView
    public void setCalendarItemBackground(boolean editable) {

        if (editable) {

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


    // Called from the CalendarView
    public void setCalendarItemBackground(boolean editable, String a) {

        if (editable) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lollipop
                cv.setCardBackgroundColor(Color.GRAY);
                iv_cancel.setVisibility(View.VISIBLE);

            } else {
                // lollipop and above
                cv.setCardBackgroundColor(cv.getResources().getColor(R.color.gray));
                iv_cancel.setVisibility(View.VISIBLE);
            }
        } else {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                // below lillipop
                cv.setCardBackgroundColor(Color.GRAY);
                iv_cancel.setVisibility(View.GONE);

            } else {
                // lollipop and above
                cv.setCardBackgroundColor(cv.getResources().getColor(R.color.gray));
                iv_cancel.setVisibility(View.GONE);
            }
        }
    }


    /*******************
     * Listener
     **************************/
    // reference parent listener
    public void setListener(ViewHolderListener listener) {
        this.listener = listener;
    }


    // Listener Interface with parent class
    @Override
    public void onClick(View v) {
        if (listener != null) listener.didClickOnView(v, id, this);
    }


    @Override
    public boolean onLongClick(View v) {
        if (listener != null) {
            if (listener != null) listener.didLongClickOnView(v, id, this);
            return true;
        }
        return false;
    }


    public void updateTimeRemaining(String startTime) {
        if (time != null) time.setText(startTime);
    }


    public void setID(String ID) {
        this.id = ID;
    }
}