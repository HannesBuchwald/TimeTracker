package org.hdm.app.timetracker.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.ActivityListOnClickListener;
import org.hdm.app.timetracker.listener.ViewHolderListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.List;

/**
 * Created by Hannes on 27.05.2016.
 */
public class ObjectListAdapter extends RecyclerView.Adapter<View_Holder> implements
        ViewHolderListener {

    private final String TAG = "ObjectListAdapter";

    public List<String> list = null;
    private ActivityListOnClickListener listener;
    public Variables var = Variables.getInstance();
    public DataManager dataManager = DataManager.getInstance();
    public String activeTime;
    public boolean leave;


    public ObjectListAdapter(List<String> activityObject) {
        this.list = activityObject;
        Log.d(TAG, "listSize " + list.size());
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }






    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        ActivityObject object = dataManager.getActivityObject(list.get(position));
        holder.setListener(this);
        holder.title.setText(object.title);
        holder.activityList = false;
        if(dataManager.imageMap.get(object.imageName) != null ) holder.imageView.setImageBitmap((dataManager.imageMap.get(object.imageName)));
        if(!var.editable) {


            if(object.service != null) {

                if(object.service.contains("Yes") && object.activeState) {
                    holder.setBackground("blue");
                } else {
                    holder.setBackground(object.activeState);
                }
            } else {
                holder.setBackground(object.activeState);
            }
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


    public void setListener (ActivityListOnClickListener listener) {
        this.listener = listener;
    }



    @Override
    public void didClickOnView(View view, String title, View_Holder holder) {

        if(listener != null) listener.didClickOnActivityListItem(title, holder);

    }


    @Override
    public void didLongClickOnView(View view, String title, View_Holder holder) {
        if(listener != null) listener.didLongClickOnActivityListItem(title, holder);
    }
}