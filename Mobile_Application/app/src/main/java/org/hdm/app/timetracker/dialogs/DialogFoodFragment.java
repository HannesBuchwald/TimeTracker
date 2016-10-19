package org.hdm.app.timetracker.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.hdm.app.timetracker.R;
import org.hdm.app.timetracker.adapter.DialogFoodListAdapter;
import org.hdm.app.timetracker.datastorage.ActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.Stamp;
import org.hdm.app.timetracker.listener.DialogPortionListOnClickListener;
import org.hdm.app.timetracker.util.Variables;
import org.hdm.app.timetracker.util.View_Holder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;


/**
 * Created by Hannes on 03.06.2016.
 */
public class DialogFoodFragment extends DialogFragment implements DialogPortionListOnClickListener, View.OnLongClickListener {


    private static final String TAG = "DialogFoodFragment";
    private ActivityObject activityObject;
    private DialogFoodListAdapter portionAdapter;
    private RecyclerView recyclerView;


    public Variables var = Variables.getInstance();
    public DataManager dataManager = DataManager.getInstance();


    View view;
    private Button btnDialogFood;

    public DialogFoodFragment() {
    }


    public DialogFoodFragment(ActivityObject activityObject) {
        this.activityObject = activityObject;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_food, container,
                false);


        initLayout();


        return view;
    }


    private void initLayout() {

        getDialog().setTitle("Choose Types of Food");

        portionAdapter = new DialogFoodListAdapter((List) new ArrayList<>(dataManager.getFoodMap().keySet()));
        portionAdapter.setListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_listt);
        recyclerView.setAdapter(portionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                var.listRows, StaggeredGridLayoutManager.VERTICAL));

        btnDialogFood = (Button) view.findViewById(R.id.btn_dialog);
        btnDialogFood.setVisibility(View.GONE);
        btnDialogFood.setOnLongClickListener(this);
    }


    @Override
    public void didClickOnPortionListItem(String title, View_Holder holder) {

    }

    @Override
    public void didLongClickOnPortionListItem(String title, View_Holder view_holder) {
        if(DEBUGMODE) Log.d(TAG, "click on Dialog " + title);

        handleObjectClick(title, view_holder);
    }

    private void handleObjectClick(String title, View_Holder view_holder) {


        // get clicked PortionObject
        ActivityObject foodObject = dataManager.getFoodObject(title);

        if (foodObject.activeState) {
            foodObject.activeState = false;

            if (activityObject.food.contains(foodObject.title)) {
                activityObject.food.remove(foodObject.title);
            }

        } else {
            foodObject.activeState = true;

            if (!activityObject.food.contains(foodObject.title)) {
                activityObject.food.add(foodObject.title);
            }
        }

        // set Background to green
        view_holder.setBackground(foodObject.activeState);

            if (DEBUGMODE) Log.d(TAG, "foodObject " + foodObject.title);
        if (DEBUGMODE) Log.d(TAG, "foodObject a" + activityObject.food);

        if (activityObject.food.size() >= 1) {
            btnDialogFood.setVisibility(View.VISIBLE);
        } else {
            btnDialogFood.setVisibility(View.GONE);
        }
    }


    private void resetFoodItemState() {
        LinkedHashMap<String, ActivityObject> foodMap = dataManager.getFoodMap();
        for (Map.Entry<String, ActivityObject> entry : foodMap.entrySet()) {
            entry.getValue().activeState = false;
        }
    }


    @Override
    public boolean onLongClick(View v) {
        if(DEBUGMODE) Log.d(TAG, "Here I´m " + dataManager.getActivityObject("Eating + Drinking").food.toString());
        if(DEBUGMODE) Log.d(TAG, "Here I´m " + dataManager.getActivityObject("Eating + Drinking").portion);
        if(DEBUGMODE) Log.d(TAG, "Here I´m " + dataManager.getActivityObject("Eating + Drinking").startTime.toString());
        if(DEBUGMODE) Log.d(TAG, "Here I´m " + dataManager.getActivityObject("Eating + Drinking").endTime.toString());


        saveStateToLogList(activityObject);
        // Save Timestamp and SubCategory in ActivityObject
        activityObject.saveTimeStamp("uuser");
        dataManager.setActivityObject(activityObject);
        resetFoodItemState();
        this.dismiss();
        return true;
    }


    public void saveStateToLogList(ActivityObject activityObject) {

        Stamp stamp = new Stamp();
        stamp.a03_userID = Variables.getInstance().user_ID;
        stamp.a01_activity = activityObject.title;

//        int year = 1900 + activityObject.startTime.getYear();
//        int month = activityObject.startTime.getMonth()+1;
//        int day = activityObject.startTime.getDate();

        String date =  activityObject.startTime.toString();
        stamp.b01_time_date = date.substring(0,10) + " " + date.substring(date.length()-4);;
        stamp.b02_time_start = date.substring(11,19);
        stamp.b03_time_end = activityObject.endTime.toString().substring(11,19);

        long ms = activityObject.endTime.getTime() - activityObject.startTime.getTime();
        stamp.b05_time_sum_sec = String.valueOf((ms/1000));

        long timeDiff = activityObject.endTime.getTime() - activityObject.startTime.getTime();

        int seconds = (int) (timeDiff / 1000) % 60;
        int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
        int hours = (int) (timeDiff/1000) / 3600;

        String secondsStr = String.valueOf(seconds);
        String minutesStr = String.valueOf(minutes);
        String hoursStr = String.valueOf(hours);

        if (seconds < 10) secondsStr = "0" + secondsStr;
        if (minutes < 10) minutesStr = "0" + minutesStr;
        if (hours < 10) hoursStr = "0" + hoursStr;

        String time = hoursStr + ":" + minutesStr + ":" + secondsStr;

        stamp.b04_time_sum = time;



//        stamp.b04_time_sum = String.valueOf((ms/1000)/60);

        stamp.c01_contract_work = activityObject.service;
        stamp.a06_author = activityObject.author;
        stamp.a07_delete = "No";


        stamp.f01_portion = activityObject.portion;
        if (DEBUGMODE) Log.d(TAG, "Portion " + stamp.f01_portion);


        int foodSum = 0;

        LinkedHashMap<String, ActivityObject> foodMap = dataManager.getFoodMap();
        for (Map.Entry<String, ActivityObject> entry : foodMap.entrySet()) {

//            boolean activeState = entry.getValue().activeState;
            int value = 0;
            if(entry.getValue().activeState) {
                value = 1;
                foodSum++;
            }

            String title = entry.getValue().title;
            switch (title) {

                case "Cereals":
                    stamp.f02_cereals = String.valueOf(value);
                    break;

                case "Roots/tubers":
                    stamp.f03_roots_tubers = String.valueOf(value);
                    break;

                case "Vegetables":
                    stamp.f04_vegetables = String.valueOf(value);
                    break;

                case "Fruits":
                    stamp.f05_fruits = String.valueOf(value);
                    break;

                case "Meats":
                    stamp.f06_meats = String.valueOf(value);
                    break;

                case "Eggs":
                    stamp.f07_eggs = String.valueOf(value);
                    break;

                case "Fish/seafood":
                    stamp.f08_fish_seafood = String.valueOf(value);
                    break;

                case "Pulses/legumes/nuts":
                    stamp.f09_pulses_legumes_nuts = String.valueOf(value);
                    break;

                case "Milk products":
                    stamp.f10_milk_products = String.valueOf(value);
                    break;

                case "Oils/fats":
                    stamp.f11_oils_fats = String.valueOf(value);
                    break;

                case "Sugar/honey":
                    stamp.f12_sugar_honey = String.valueOf(value);
                    break;

                case "Tea/coffee":
                    stamp.f13_tea_coffee = String.valueOf(value);
                    break;

                default:
                    break;

            }
            entry.getValue().activeState = false;
        }


        stamp.f14_food_sum = String.valueOf(foodSum);
        dataManager.logList.add(stamp);
    }

}