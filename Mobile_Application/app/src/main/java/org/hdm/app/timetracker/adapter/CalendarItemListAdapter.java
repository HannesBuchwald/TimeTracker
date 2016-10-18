package org.hdm.app.timetracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.CalendarItemOnClickListener;
import org.hdm.app.timetracker.listener.ViewHolderListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Hannes on 27.05.2016.
 */
public class CalendarItemListAdapter extends RecyclerView.Adapter<View_Holder> implements
        ViewHolderListener {

    private final String TAG = "CalendarItemListAdapter";
    private final LinkedHashMap data;
    private Context context;



    public ArrayList list;
    private CalendarItemOnClickListener listener;
    private View v;
    public String time = "";
    public Variables var = Variables.getInstance();



    public CalendarItemListAdapter(Context context, LinkedHashMap data, ArrayList recActivityTitles) {
        this.context = context;
        this.data = data;
        list = recActivityTitles;
    }




    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_calender_content, parent, false);
        View_Holder holder = new View_Holder(v, this);
        return holder;
    }




    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.setListener(this);
        if(holder.imageView != null) {
            
            ActivityObject dataa =(ActivityObject) data.get(list.get(position));
            if(DataManager.getInstance().imageMap.get(dataa.imageName) != null )
                holder.imageView.setImageBitmap(DataManager.getInstance().imageMap.get(dataa.imageName));
            holder.title.setText(dataa.title);
            holder.setCalendarItemBackground(var.editable);
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
    public void remove(String activityObject) {
        int position = list.indexOf(activityObject);
        list.remove(position);
        notifyItemRemoved(position);
    }







    public void setListener (CalendarItemOnClickListener listener) {
        this.listener = listener;
    }



    @Override
    public void didClickOnView(View view, String s, View_Holder view_holder) {
    }



    @Override
    public void didLongClickOnView(View view, String s, View_Holder view_holder) {
            if(var.editable) remove(s);
            if(listener!=null) listener.didOnClick(time, s, view_holder);
    }

}