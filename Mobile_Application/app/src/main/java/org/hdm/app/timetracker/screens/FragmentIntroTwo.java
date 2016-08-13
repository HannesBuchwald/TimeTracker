package org.hdm.app.timetracker.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.hdm.app.timetracker.R;

/**
 * Walktrough Fragment 2 of 4.
 *
 * Holds the Subject-Form.
 */
public class FragmentIntroTwo extends Fragment {

    private final String logIndictaor = "FragmentIntroTwo";

    /**
     * Views
     */
    private Spinner genderSpinner;
    private Spinner educationSpinner;

    /**
     * Data
     */
    private ArrayAdapter<CharSequence> genderAdapter;
    private ArrayAdapter<CharSequence> educationAdapter;
    private String genderSelected = "Female";
    private String educationSelected = "Farmer";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView(view);
    }

    /**
     * Setup view
     * @param _view IntroActivity view
     */
    private void setupView(View _view) {
        // get views
        this.genderSpinner = (Spinner) _view.findViewById(R.id.input_gender_spinner);
        this.educationSpinner = (Spinner) _view.findViewById(R.id.input_education_spinner);

        // setup spinner adapter
        this.genderAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.gender_array, R.layout.spinner_item);
        this.genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.educationAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.education_array, R.layout.spinner_item);
        this.educationAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        // link adapter to spinner
        this.genderSpinner.setAdapter(this.genderAdapter);
        this.genderSpinner.setOnItemSelectedListener(this.onGenderSelected);

        this.educationSpinner.setAdapter(this.educationAdapter);
        this.educationSpinner.setOnItemSelectedListener(this.onEducationSelected);
    }

    /**
     * OnSelect: Gender-spinner.
     */
    private AdapterView.OnItemSelectedListener onGenderSelected =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // an item was selected
            Object selected = parent.getItemAtPosition(pos);
            genderSelected = selected.toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // another interface callback
        }
    };

    /**
     * OnSelect: Education-spinner.
     */
    private AdapterView.OnItemSelectedListener onEducationSelected =
            new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // an item was selected
            Object selected = parent.getItemAtPosition(pos);
            educationSelected = selected.toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // another interface callback
        }
    };
}