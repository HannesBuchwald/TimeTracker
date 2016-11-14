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
import org.hdm.app.timetracker.datastorage.DataManager;
import org.hdm.app.timetracker.datastorage.Stamp;
import org.hdm.app.timetracker.listener.DialogPortionListOnClickListener;
import org.hdm.app.timetracker.screens.FragmentActivity;
import org.hdm.app.timetracker.util.Variables;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hdm.app.timetracker.util.Consts.DEBUGMODE;


/**
 * Created by Hannes on 03.06.2016.
 */
public class DialogFoodFragment extends DialogFragment implements DialogPortionListOnClickListener, View.OnLongClickListener {


    private static final String TAG = "DialogFoodFragment";
    private static final String TITLE = "Choose a Portion";
    private final Date startTime;

    private DialogFoodListAdapter foodAdapter;
    private Stamp stamp;
    private RecyclerView recyclerView;
    private List<String> activeFoodList = new ArrayList();


    public Variables var = Variables.getInstance();
    public DataManager dataManager = DataManager.getInstance();


    View view;
    private Button btnDialogFood;
    private FragmentActivity fragmentActivity;


    public DialogFoodFragment(FragmentActivity fragmentActivity, Stamp stamp, Date startTime) {

        this.stamp = stamp;
        this.fragmentActivity = fragmentActivity;
        this.startTime = startTime;
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

        getDialog().setTitle(TITLE);

        foodAdapter = new DialogFoodListAdapter((List) new ArrayList<>(dataManager.getFoodMap().keySet()), activeFoodList);
        foodAdapter.setListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_listt);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                var.listRows, StaggeredGridLayoutManager.VERTICAL));

        btnDialogFood = (Button) view.findViewById(R.id.btn_dialog);
        btnDialogFood.setVisibility(View.GONE);
        btnDialogFood.setOnLongClickListener(this);
    }



    /**
     * Listen on the Activity Click
     * @param id
     */
    @Override
    public void didClickOnPortionListItem(String id) {
        //...
    }

    /**
     * Listen on the Activity Click
     * @param id
     */
    @Override
    public void didLongClickOnPortionListItem(String id) {
        handleObjectClick(id);
    }


    /**
     * Listen on the OK Button Click
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {

        fragmentActivity.stamp = stamp;

        addActiveFoodListToStamp(activeFoodList);
        dataManager.addToLogMap(startTime, stamp);
        resetActiveFoodList();
        this.dismiss();
        if (var.editable) fragmentActivity.flip();
        return true;
    }




    private void handleObjectClick(String id) {

        if (activeFoodList.contains(id)) {
            activeFoodList.remove(id);
        } else {
            activeFoodList.add(id);
        }

        // Set visibility of OK Button
        if (activeFoodList.size() >= 1) {
            btnDialogFood.setVisibility(View.VISIBLE);
        } else {
            btnDialogFood.setVisibility(View.GONE);
        }
        updateAdapter();
    }




    // Update the DialogFoodAdapter
    private void updateAdapter() {
        foodAdapter.activeFoodList = activeFoodList;
        foodAdapter.notifyDataSetChanged();
    }


    private void resetActiveFoodList() {
        activeFoodList = new ArrayList<>();
        foodAdapter.activeFoodList = new ArrayList<>();
    }



    private void addActiveFoodListToStamp(List<String> activeFoodList) {

        // Add FoodSum
        stamp.f14_food_sum = String.valueOf(activeFoodList.size());
        if (DEBUGMODE) Log.d(TAG, "String Value " + stamp.f14_food_sum);

        // Add Food Category
        for (String value : activeFoodList) {

            String title = dataManager.foodMap.get(value).getTitle();

            if (title != null) {
                switch (title) {
                    case "Cereals":
                        stamp.f02_cereals = "1";
                        break;
                    case "Roots/tubers":
                        stamp.f03_roots_tubers = "1";
                        break;
                    case "Vegetables":
                        stamp.f04_vegetables = "1";
                        break;
                    case "Fruits":
                        stamp.f05_fruits = "1";
                        break;
                    case "Meats":
                        stamp.f06_meats = "1";
                        break;
                    case "EgEgs":
                        stamp.f07_eggs = "1";
                        break;
                    case "Fish/seafood":
                        stamp.f08_fish_seafood = "1";
                        break;
                    case "Pulses/legumes/nuts":
                        stamp.f09_pulses_legumes_nuts = "1";
                        break;
                    case "Milk products":
                        stamp.f10_milk_products = "1";
                        break;
                    case "Oils/fats":
                        stamp.f11_oils_fats = "1";
                        break;
                    case "Sugar/honey":
                        stamp.f12_sugar_honey = "1";
                        break;
                    case "Tea/coffee":
                        stamp.f13_tea_coffee = "1";
                        break;
                    default:
                        break;
                }
            }
        }
    }
}