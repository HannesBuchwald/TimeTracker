package org.hdm.app.timetracker.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Hannes on 13.08.2016.
 */
public class Navigation extends Activity{

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        text = new TextView(this);
        text.setText("Welcome to new Activity");
        setContentView(text);
    }

}
