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
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.listener.CalendarItemOnClickListener;
import org.hdm.app.timetracker.listener.ViewHolderListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.hdm.app.timetracker.util.Consts.CALENDARITEMROW;

/**
 * Created by Hannes on 27.05.2016.
 */
public class CalendarListAdapter extends RecyclerView.Adapter<View_Holder> implements
        ViewHolderListener, CalendarItemOnClickListener {

    private final String TAG = "CalendarListAdapter";
    public Context context;



    private TreeMap calendarMap;
    public LinkedHashMap data;
    public ArrayList list;
    private CalendarItemOnClickListener listener;
    private View v;
    int lastPosition = 0;

    public Variables var = Variables.getInstance();



    private CalendarItemListAdapter resAdapter;


    public CalendarListAdapter(Activity activity, LinkedHashMap data, TreeMap calendar) {
        this.context = activity;
        this.data = data;
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
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView

        String title = list.get(position).toString();
        holder.id = title;
        holder.setListener(this);

        Log.d(TAG,"title "+ title);

        // Display only Hours and Minutes
        // Cut away the first 14 characters
        title = title.substring(11);
        // Cut away all characters after item 5
        title = title.substring(0, 5);


        if(!title.contains("00")){
            String subTitle = title;
            holder.title.setText("   " + subTitle.substring(3));
        } else {
            holder.title.setText(title);
        }

        // Init RowItemContent
        resAdapter = new CalendarItemListAdapter(context,
                data,
                (ArrayList) calendarMap.get(list.get(position).toString()));
        resAdapter.setListener(this);
        resAdapter.time = list.get(position).toString();
        holder.rv_content.setAdapter(resAdapter);
        holder.rv_content.setLayoutManager(new LinearLayoutManager(context));
        holder.rv_content.setLayoutManager(new StaggeredGridLayoutManager(
                CALENDARITEMROW,StaggeredGridLayoutManager.HORIZONTAL));

        if(var.editable) {
            if (holder.btn_add != null) holder.btn_add.setVisibility(View.VISIBLE);
        } else {
            if (holder.btn_add != null) holder.btn_add.setVisibility(View.GONE);
        }


        // Set
        Date currentTime = Calendar.getInstance().getTime();
        Date givenTime = Calendar.getInstance().getTime();


        int hour =  Integer.parseInt(title.substring(0, 2));
        int min =  Integer.parseInt(title.substring(3));

        Log.d(TAG, "hour " +hour +" "+ min);
        givenTime.setHours(hour);
        givenTime.setMinutes(min);

        if(givenTime.before(currentTime)) {
            holder.iv_background_bottom.setVisibility(View.VISIBLE);
            holder.iv_background_top.setVisibility(View.VISIBLE);
            lastPosition = position+1;
        } else {
            holder.iv_background_bottom.setVisibility(View.INVISIBLE);
            holder.iv_background_top.setVisibility(View.INVISIBLE);

            // make iv_background_top for in currentTime CalendarItem visible
            givenTime.setMinutes(min-15);
            if(givenTime.before(currentTime))holder.iv_background_top.setVisibility(View.VISIBLE);
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


    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, ActivityObject activityObject) {
        list.add(position, activityObject);
        notifyItemInserted(position);
    }



    // Remove a RecyclerView item containing a specified Daata object
    public void remove(ActivityObject activityObject) {
        int position = list.indexOf(activityObject);
        list.remove(position);
        notifyItemRemoved(position);
    }







    public void setListener (CalendarItemOnClickListener listener) {
        this.listener = listener;
    }



    @Override
    public void didClickOnView(View view, String s, View_Holder holder) {
        Log.d(TAG, "holder " + holder.id);
        if(listener!= null) listener.didOnClickAddBtn(holder);
    }


    @Override
    public void didLongClickOnView(View view, String s, View_Holder view_holder) {
    }




    private void removeStrings() {
        for(int i=0; i<list.size(); i++){
            String str = (String) list.get(i);
            String strNew = str.substring(11, 17);
            list.set(i,strNew);
        }
    }



    @Override
    public void didOnClick(String time, String s, View_Holder holder) {
        Log.d(TAG, "holder " + holder.id);
        if(listener!= null) listener.didOnClick(time, s, holder);
    }



    @Override
    public void didOnClickAddBtn(View_Holder holder) {

    }
}