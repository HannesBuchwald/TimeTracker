package org.hdm.app.sambia;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;

import org.hdm.app.sambia.adapter.IntroPageAdapter;
import org.hdm.app.sambia.main.MainActivity;
import org.hdm.app.sambia.models.SubjectModel;
import org.hdm.app.sambia.tasks.InitDeviceTask;
import org.hdm.app.sambia.util.Settings;
import org.json.JSONObject;

import me.relex.circleindicator.CircleIndicator;

/**
 * Present a walkthrough-interface where a new subject can be created and all needed files
 * are pushed/pulled from our server.
 */
public class IntroActivity extends FragmentActivity {

    final private String LOG_INDICATOR = "IntroActivity";

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager introPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter introPagerAdapter;

    /**
     * Attributes
     */
    private int slidePos = 0;
    private Button buttonPrev;
    private Button buttonNext;
    private CircleIndicator introIndicator;
    private RadioGroup radioIndicators;
    private MaterialDialog finishDialog;

    /**
     * Data
     */
    private SubjectModel subjectData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set content
        setContentView(R.layout.activity_intro);

        // Instantiate ViewPager and PagerAdapter.
        introPager = (ViewPager) findViewById(R.id.viewpager_unselected_background);
        introIndicator = (CircleIndicator) findViewById(R.id.indicator_unselected_background);
        introPagerAdapter = new IntroPageAdapter(getSupportFragmentManager());
        //introPager.setOffscreenPageLimit(introPagerAdapter.getCount());
        introPager.setAdapter(introPagerAdapter);
        introIndicator.setViewPager(introPager);

        // start listening for page changes
        introIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                // call our handler for position changes.
                onPositionChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // get all elements and add click events if necessary
        buttonNext = (Button) findViewById(R.id.intro_button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // user clicked next button, increment position
                slidePos = introPager.getCurrentItem() + 1;
                onControlButton();
            }
        });
        buttonPrev = (Button) findViewById(R.id.intro_button_prev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // the user clicked the prev button, decrement position
                slidePos = introPager.getCurrentItem() - 1;
                onControlButton();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (introPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            introPager.setCurrentItem(introPager.getCurrentItem() - 1);
        }
    }

    /**
     * Is called whenever the prev-/next-button is clicked.
     * Sets the viewPager page to the current slidePos.
     */
    private void onControlButton() {

        if(slidePos != 4) {
            introPager.setCurrentItem(slidePos, true);
        }
        else {
            // last step, start uploading data
            Log.d(LOG_INDICATOR, "Last Step");

            boolean subjectCreated = checkSubjectData();

            if(!subjectCreated) {
                Log.e(LOG_INDICATOR, "Missing form data");
                // TODO: give visual feedback
                // return;
            }

            finishIntro();
        }
    }

    /**
     * Is called whenever the viewPager's page changes.
     * Is used for changing the position indicators.
     * @param currentPos
     */
    private void onPositionChanged(int currentPos) {
        //Log.d(logIndicator, "onPositionChanged() " + currentPos);

        switch(currentPos) {
            case 0:
                // default start position, hide prev-button
                buttonPrev.animate()
                        .alpha(0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // after fadeOut, make button invisible
                                buttonPrev.setVisibility(View.INVISIBLE);
                            }
                        });
                break;
            case 1:
                // make the prev-button visible & animate it in
                buttonPrev.setVisibility(View.VISIBLE);
                buttonPrev.animate()
                        .alpha(1f)
                        .setListener(null); // clears previously set listeners
                break;
            case 2:
                // insert button next text, if the user clicked the buttonPrev
                buttonNext.setText(getString(R.string.intro_button_next));
                break;
            case 3:
                // insert button ready text, we are on the last step
                buttonNext.setText(getString(R.string.intro_button_ready));
                break;
        }
    }

    /**
     * Fetches the input-data from all intro-screens and builds a SubjectModel with it.
     * If needed input-data is missing, false is returned
     *
     * @return boolean False, if needed form-fields were empty
     */
    private boolean checkSubjectData() {

        try {
            // get the input views
            EditText nameInput = (EditText) introPager.findViewById(R.id.input_name);
            EditText ageInput = (EditText) introPager.findViewById(R.id.input_age);
            Spinner genderSpinner = (Spinner) introPager.findViewById(R.id.input_gender_spinner);
            Spinner educationSpinner = (Spinner) introPager.findViewById(R.id.input_education_spinner);

            EditText tribeInput = (EditText) introPager.findViewById(R.id.input_tribe);
            EditText householdInput = (EditText) introPager.findViewById(R.id.input_household);
            EditText sizeInput = (EditText) introPager.findViewById(R.id.input_size);
            EditText weightInput = (EditText) introPager.findViewById(R.id.input_weight);

            EditText landOwnedInput = (EditText) introPager.findViewById(R.id.input_owned);
            EditText landCultivatedInput = (EditText) introPager.findViewById(R.id.input_cultivated);

            // get the input data
            String nameValue = nameInput.getText().toString().trim();
            int ageValue = Integer.parseInt(ageInput.getText().toString());
            String genderValue = genderSpinner.getSelectedItem().toString();
            String educationValue = educationSpinner.getSelectedItem().toString();

            String tribeValue = tribeInput.getText().toString().trim();
            int householdValue = Integer.parseInt(householdInput.getText().toString());
            int sizeValue = Integer.parseInt(sizeInput.getText().toString());
            int weightValue = Integer.parseInt(weightInput.getText().toString());

            int landOwnedValue = Integer.parseInt(landOwnedInput.getText().toString());
            int landCultivatedValue = Integer.parseInt(landCultivatedInput.getText().toString());

            // build model from input
            this.subjectData = new SubjectModel(
                    null,
                    nameValue,
                    ageValue,
                    genderValue,
                    educationValue,
                    tribeValue,
                    householdValue,
                    sizeValue,
                    weightValue,
                    landOwnedValue,
                    landCultivatedValue
            );

            return true;

        } catch(Exception error) {
            error.printStackTrace();

            return false;
        }

    }

    /**
     * Generates all needed setting-values, inserts them inside the user's encrypted settings
     * and redirects the user to the MainActivity.
     */
    private void finishIntro() {
        // TODO: Remove debugging data
        this.subjectData = new SubjectModel(
                null,
                "Name",
                25,
                "Male",
                "Student",
                "Tribe",
                3,
                180,
                80,
                80,
                7
        );

        // give feedback
        this.finishDialog = new MaterialDialog.Builder(this)
                .title(R.string.intro_finish_dialog_title)
                .content(R.string.intro_finish_dialog_content_one)
                .progress(false, 100, true)
                .show();

        // upload subject data to server, listener-callback after task execution
        new InitDeviceTask(this.listener, this.finishDialog).execute(this.subjectData);
    }

    /**
     * Callback for AsyncTask "initDeviceTask"
     */
    InitDeviceTask.InitListener listener = new InitDeviceTask.InitListener() {
        @Override
        public void onFinished(String subjectId, JSONObject activities) {
            finishDialog.setContent(R.string.intro_finish_dialog_content_three);

            // redirect to the MainActivity
            Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // insert data into persistent settings
            Settings.initSettings(getBaseContext(), "defaultPassword", subjectId, "0.0.1");

            // disable introActivity on next start
            Settings.disableFirstRun(getBaseContext());

            finishDialog.dismiss();
            startActivity(mainIntent);
        }
    };
}