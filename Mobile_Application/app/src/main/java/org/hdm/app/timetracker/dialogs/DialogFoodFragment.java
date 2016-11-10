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
import org.hdm.app.timetracker.datastorage.AAAActivityObject;
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.Stamp;
import org.hdm.app.timetracker.listener.DialogPortionListOnClickListener;
import org.hdm.app.timetracker.screens.FragmentActivity;
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
    private AAAActivityObject AAAActivityObject;
    private DialogFoodListAdapter portionAdapter;
    private RecyclerView recyclerView;


    public Variables var = Variables.getInstance();
    public DataManager dataManager = DataManager.getInstance();


    View view;
    private Button btnDialogFood;
    private FragmentActivity fragmentActivity;

    public DialogFoodFragment() {
    }


    public DialogFoodFragment(FragmentActivity fragmentActivity, AAAActivityObject AAAActivityObject) {
        this.AAAActivityObject = AAAActivityObject;
        this.fragmentActivity = fragmentActivity;
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
        AAAActivityObject foodObject = dataManager.getFoodObject(title);

        if (foodObject.activeState) {
            foodObject.activeState = false;

            if (AAAActivityObject.food.contains(foodObject.title)) {
                AAAActivityObject.food.remove(foodObject.title);
            }

        } else {
            foodObject.activeState = true;

            if (!AAAActivityObject.food.contains(foodObject.title)) {
                AAAActivityObject.food.add(foodObject.title);
            }
        }

        // set Background to green
        view_holder.setBackground(foodObject.activeState);

            if (DEBUGMODE) Log.d(TAG, "foodObject " + foodObject.title);
        if (DEBUGMODE) Log.d(TAG, "foodObject a" + AAAActivityObject.food);

        if (AAAActivityObject.food.size() >= 1) {
            btnDialogFood.setVisibility(View.VISIBLE);
        } else {
            btnDialogFood.setVisibility(View.GONE);
        }
    }


    private void resetFoodItemState() {
        LinkedHashMap<String, AAAActivityObject> foodMap = dataManager.getFoodMap();
        for (Map.Entry<String, AAAActivityObject> entry : foodMap.entrySet()) {
            entry.getValue().activeState = false;
        }
    }


    @Override
    public boolean onLongClick(View v) {
        if(DEBUGMODE) Log.d(TAG, "Here I´m " + dataManager.getActivityObject("Eating + Drinking").food.toString());
        if(DEBUGMODE) Log.d(TAG, "Here I´m " + dataManager.getActivityObject("Eating + Drinking").portion);
        if(DEBUGMODE) Log.d(TAG, "Here I´m " + dataManager.getActivityObject("Eating + Drinking").startTime.toString());
        if(DEBUGMODE) Log.d(TAG, "Here I´m " + dataManager.getActivityObject("Eating + Drinking").endTime.toString());


        saveStateToLogList(AAAActivityObject);
        // Save Timestamp and SubCategory in AAAActivityObject
        AAAActivityObject.saveTimeStamp("uuser");
        dataManager.setActivityObject(AAAActivityObject);
        resetFoodItemState();
        this.dismiss();
        if (var.editable) fragmentActivity.flip();
        return true;
    }


    public void saveStateToLogList(AAAActivityObject AAAActivityObject) {

        Stamp stamp = new Stamp();
        stamp.a03_userID = Variables.getInstance().user_ID;
        stamp.a01_activity = AAAActivityObject.title;

//        int year = 1900 + AAAActivityObject.startTime.getYear();
//        int month = AAAActivityObject.startTime.getMonth()+1;
//        int day = AAAActivityObject.startTime.getDate();

        String date =  AAAActivityObject.startTime.toString();
        stamp.b01_time_date = date.substring(0,10) + " " + date.substring(date.length()-4);;
        stamp.b02_time_start = date.substring(11,19);
        stamp.b03_time_end = AAAActivityObject.endTime.toString().substring(11,19);

        long ms = AAAActivityObject.endTime.getTime() - AAAActivityObject.startTime.getTime();
        stamp.b05_time_sum_sec = String.valueOf((ms/1000));

        long timeDiff = AAAActivityObject.endTime.getTime() - AAAActivityObject.startTime.getTime();

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

        stamp.c01_contract_work = AAAActivityObject.service;
        stamp.a06_author = AAAActivityObject.author;
        stamp.a07_delete = "No";


        stamp.f01_portion = AAAActivityObject.portion;
        if (DEBUGMODE) Log.d(TAG, "Portion " + stamp.f01_portion);


        int foodSum = 0;

        LinkedHashMap<String, AAAActivityObject> foodMap = dataManager.getFoodMap();
        for (Map.Entry<String, AAAActivityObject> entry : foodMap.entrySet()) {

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

                case "EgEgs":
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