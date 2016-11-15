package org.hdm.app.timetracker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActiveObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.CalendarItemOnClickListener;
import org.hdm.app.timetracker.listener.ViewHolderListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;
import static org.hdm.app.timetracker.util.Consts.GRAY;
import static org.hdm.app.timetracker.util.Consts.RED;
import static org.hdm.app.timetracker.util.Consts.TRANSPARENT;

/**
 * Created by Hannes on 27.05.2016.
 */
public class CalendarItemListAdapter extends RecyclerView.Adapter<View_Holder> implements
        ViewHolderListener {

    private final String TAG = "CalendarItemListAdapter";
    private Context context;


    public ArrayList<ActiveObject> list;
    private CalendarItemOnClickListener listener;
    private View v;
    public String time = "";
    public Variables var = Variables.getInstance();
    private DataManager dataManager = DataManager.getInstance();


    public CalendarItemListAdapter(Context context, ArrayList recActivityTitles) {
        this.context = context;
        list = recActivityTitles;
        int size = list.size();
        if (DEBUGMODE) Log.d(TAG, "List " + size);
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
        if (holder.imageView != null) {

            ActiveObject aObject = list.get(position);
            holder.setListener(this);
            holder.title.setText(aObject.title);
            holder.id = aObject.id;
            setImg(holder, aObject);
            setBackground(holder, aObject);

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


    // Remove a RecyclerView item containing a specified Daata object
    public void remove(String activityObject) {
        Log.d(TAG, "01" + activityObject);
        int position = list.indexOf(activityObject);
        Log.d(TAG, "02" + position);
        list.remove(position);
        notifyItemRemoved(position);
    }


    public void setListener(CalendarItemOnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void didClickOnView(View view, String s, View_Holder view_holder) {
    }


    @Override
    public void didLongClickOnView(View view, String s, View_Holder view_holder) {
//        if (var.editable) remove(s);
        if (listener != null) listener.didOnClick(time, s, view_holder);
    }


    private void setImg(View_Holder holder, ActiveObject aObject) {
        Bitmap image = dataManager.imageMap.get(dataManager.getObject(aObject.id).getImageName());
        if (image != null) holder.imageView.setImageBitmap(image);
    }


    private void setBackground(View_Holder holder, ActiveObject aObject) {

        if (var.editable) {
            holder.setBackground(RED);
        } else if (aObject.contractWork) {
            holder.setBackground(GRAY);
        } else {
            holder.setBackground(TRANSPARENT);
        }
    }

}