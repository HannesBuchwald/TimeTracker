package org.hdm.app.timetracker.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.ActiveObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.ActivityListOnClickListener;
import org.hdm.app.timetracker.listener.ViewHolderListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.List;

import static org.hdm.app.timetracker.util.Consts.BLUE;
import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;
import static org.hdm.app.timetracker.util.Consts.GREEN;
import static org.hdm.app.timetracker.util.Consts.WHITE;

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
        if(DEBUGMODE)Log.d(TAG, "listSize " + list.size());
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

        // Init Listener in View_Holder
        holder.setListener(this);


        // set CardView Image
        ActivityObject object = dataManager.getObject(list.get(position));
        if(dataManager.imageMap.get(object.getImageName()) != null ) {
            holder.imageView.setImageBitmap((dataManager.imageMap.get(object.getImageName())));
        }

        // set ID
        holder.setID(object.get_id());


        // set Background
        // get Item from Active List
        // ToDo Implement External Work Flag
        ActiveObject activeObject = dataManager.getActiveObject(object.get_id());

        if(activeObject!= null) {
            if(activeObject.contractWork) {
                holder.setBackground(BLUE);
            } else {
                holder.setBackground(GREEN);
            }
        } else {
            holder.setBackground(WHITE);
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



    public void setListener (ActivityListOnClickListener listener) {
        this.listener = listener;
    }



    @Override
    public void didClickOnView(View view, String title, View_Holder holder) {
        if(listener != null) listener.shortClickOnObjectItem(title);
    }


    @Override
    public void didLongClickOnView(View view, String title, View_Holder holder) {
        if(listener != null) listener.longClickOnObjectItem(title);
    }
}