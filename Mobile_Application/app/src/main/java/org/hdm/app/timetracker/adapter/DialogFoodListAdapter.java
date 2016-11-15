package org.hdm.app.timetracker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.listener.DialogPortionListOnClickListener;
import org.hdm.app.timetracker.listener.ViewHolderListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.List;

import static org.hdm.app.timetracker.util.Consts.GREEN;
import static org.hdm.app.timetracker.util.Consts.WHITE;

/**
 * Created by Hannes on 27.05.2016.
 */
public class DialogFoodListAdapter extends RecyclerView.Adapter<View_Holder> implements
        ViewHolderListener {

    private final String TAG = "DialogFoodListAdapter";
    public List<String> list = null;

    private DialogPortionListOnClickListener listener;
    public Variables var = Variables.getInstance();
    public DataManager dataManager = DataManager.getInstance();
    public List<String> activeFoodList;


    public DialogFoodListAdapter(List<String> activityObject, List<String> activeFood) {
        this.list = activityObject;
        this.activeFoodList = activeFood;
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
        ActivityObject object = dataManager.foodMap.get((list.get(position)));
        holder.setListener(this);
        holder.title.setText(object.getTitle());
        holder.id = object.get_id();
        if(dataManager.imageMap.get(object.getImageName()) != null )
            holder.imageView.setImageBitmap((dataManager.imageMap.get(object.getImageName())));

        if(activeFoodList.contains(object.get_id())) {
            holder.setBackground(GREEN);
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


    public void setListener (DialogPortionListOnClickListener listener) {this.listener = listener;}



    @Override
    public void didClickOnView(View view, String title, View_Holder holder) {

        if(listener != null) listener.didClickOnPortionListItem(title);

    }


    @Override
    public void didLongClickOnView(View view, String title, View_Holder holder) {
        if(listener != null) listener.didLongClickOnPortionListItem(title);
    }
}