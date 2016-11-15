package org.hdm.app.timetracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.listener.CalendarItemOnClickListener;
import org.hdm.app.timetracker.listener.ViewHolderListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.hdm.app.timetracker.util.Consts.CALENDARITEMROW;
import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;

/**
 * Created by Hannes on 27.05.2016.
 */
public class CalendarListAdapter extends RecyclerView.Adapter<View_Holder> implements
        ViewHolderListener, CalendarItemOnClickListener {

    private final String TAG = "CalendarListAdapter";
    public Context context;


    private LinkedHashMap calendarMap;
    public ArrayList list;
    private CalendarItemOnClickListener listener;
    private View v;
    int lastPosition = 0;

    public Variables var = Variables.getInstance();


    private CalendarItemListAdapter resAdapter;


    public CalendarListAdapter(Activity activity, LinkedHashMap calendar) {
        this.context = activity;
        this.calendarMap = calendar;
        list = new ArrayList(calendar.keySet());
        // removeStrings();
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_calendar, parent, false);
        View_Holder holder = new View_Holder(v, this);
        return holder;
    }


    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        // TimeFrame
        String title = list.get(position).toString();
        int date = Integer.parseInt(title.substring(8, 10));

        holder.id = title;
        holder.setListener(this);

        // display addButttn
        setAddButtonVisibility(holder);
        setBackground(holder, date);
        setTitle(holder, title);


        String calendarTitleDay = list.get(position).toString().substring(0, 3);
        String calendarTitleDate = list.get(position).toString().substring(8, 10);
        if(DEBUGMODE) Log.d(TAG, "calendarTitle " + list.get(position));
        if (listener != null) listener.setCalendarTitle(calendarTitleDate + ". " + calendarTitleDay);


        // Init RowItemContent
        resAdapter = new CalendarItemListAdapter(context,
                (ArrayList) calendarMap.get(list.get(position).toString()));

        resAdapter.setListener(this);
        resAdapter.time = list.get(position).toString();

        holder.rv_content.setAdapter(resAdapter);
        holder.rv_content.setLayoutManager(new LinearLayoutManager(context));
        holder.rv_content.setLayoutManager(new StaggeredGridLayoutManager(
                CALENDARITEMROW, StaggeredGridLayoutManager.HORIZONTAL));

    }




    private void setTitle(View_Holder holder, String title) {
        // Display only Hours and Minutes
        // Cut away the first 14 characters
        title = title.substring(11);
        // Cut away all characters after item 5
        title = title.substring(0, 5);

        if (!title.contains("00")) {
            String subTitle = title;
            holder.title.setText("   " + subTitle.substring(3));
        } else {
            holder.title.setText(title);
        }
    }




    private void setBackground(View_Holder holder, int date) {
        if (holder.iv_background_top != null) {

            boolean colored = false;
            if (var.coloredDates != null) {
                for (int i = 0; i < var.coloredDates.size(); i++) {
                    if(DEBUGMODE) Log.d(TAG, "colored " + var.coloredDates.get(i).getDate() + " " + date);
                    if (var.coloredDates.get(i).getDate() == date) colored = true;
                }
            }

            if (colored) {
                holder.iv_background_bottom.setVisibility(View.VISIBLE);
                holder.iv_background_top.setVisibility(View.VISIBLE);
            } else {
                holder.iv_background_bottom.setVisibility(View.INVISIBLE);
                holder.iv_background_top.setVisibility(View.INVISIBLE);
            }
        }
    }



    private void setAddButtonVisibility(View_Holder holder) {
        if (var.editable) {
            if (holder.btn_add != null) holder.btn_add.setVisibility(View.VISIBLE);
        } else {
            if (holder.btn_add != null) holder.btn_add.setVisibility(View.GONE);
        }
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



    public void setListener(CalendarItemOnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void didClickOnView(View view, String s, View_Holder holder) {
        if(DEBUGMODE) Log.d(TAG, "holder " + holder.id);
    }


    @Override
    public void didLongClickOnView(View view, String s, View_Holder view_holder) {
        if(DEBUGMODE) Log.d(TAG, "holder ");
        if (listener != null) listener.didOnClickAddBtn(view_holder);

    }


    private void removeStrings() {
        for (int i = 0; i < list.size(); i++) {
            String str = (String) list.get(i);
            String strNew = str.substring(11, 17);
            list.set(i, strNew);
        }
    }


    @Override
    public void didOnClick(String time, String s, View_Holder holder) {
        if(DEBUGMODE) Log.d(TAG, "holder " + holder.id);
        if (listener != null) listener.didOnClick(time, s, holder);
    }


    @Override
    public void didOnClickAddBtn(View_Holder holder) {

    }

    @Override
    public void setCalendarTitle(String s) {

    }
}