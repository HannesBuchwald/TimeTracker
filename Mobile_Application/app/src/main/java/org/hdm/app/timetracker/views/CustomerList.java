package org.hdm.app.timetracker.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.hdm.app.timetracker.R;

public class CustomerList extends ArrayAdapter<String> {

    private final Context context;
    private final Integer[] imageId;
    private ImageView imageView;




    public CustomerList(Context context, Integer[] imageId) {
        super(context, R.layout.row_layout_list);
        this.context = context;
        this.imageId = imageId;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout_list, null, true);

        imageView = (ImageView) rowView.findViewById(R.id.iv_calendar_content);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}