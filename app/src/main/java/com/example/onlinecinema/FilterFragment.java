package com.example.onlinecinema;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.onlinecinema.entities.SettingsOfFilter;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private String tagFragment;
    private SettingsOfFilter settings;
    private Spinner settingsOfSort;
    private Spinner settingsOfGenre;
    private Spinner settingsOfCountry;
    private RangeSlider ratingSettings;
    private EditText settingsOfYear;
    private Button saveSettings;
    private Button removeSettings;

    public FilterFragment(String tagFragment) {
        this.tagFragment = tagFragment;
        this.settings = new SettingsOfFilter();
    }

    public static FilterFragment newInstance(String tagFragment) {
        FilterFragment fragment = new FilterFragment(tagFragment);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new SearchFragment())
                        .commit();
            }
        });
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView arrowBackBtn = view.findViewById(R.id.arrowBackButtonFlt);
        arrowBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        settingsOfSort = view.findViewById(R.id.sortingSettings);
        settingsOfGenre = view.findViewById(R.id.genreSettings);
        settingsOfCountry = view.findViewById(R.id.countrySettings);
        ratingSettings = view.findViewById(R.id.ratingSettings);
        settingsOfYear = view.findViewById(R.id.yearsSettings);
        saveSettings = view.findViewById(R.id.saveSettingsButton);
        removeSettings = view.findViewById(R.id.removeSettingsButton);

        settingsOfGenre.setOnItemSelectedListener(this);
        settingsOfCountry.setOnItemSelectedListener(this);
        settingsOfSort.setOnItemSelectedListener(this);

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View range) {
                saveSettings();
            }
        });
        removeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSettings();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        adapterView.getItemAtPosition(0);
    }

    public void clearSettings() {
        settingsOfGenre.setSelection(0);
        settingsOfCountry.setSelection(0);
        settingsOfSort.setSelection(0);
        settingsOfYear.setText("");
        ArrayList<Float> newValues = new ArrayList<Float>();
        newValues.add(0.0f);
        newValues.add(10.0f);
        ratingSettings.setValues(newValues);
        saveSettings();
    }

    public void saveSettings() {
        settings.setGenre(settingsOfGenre.getSelectedItem().toString());
        settings.setCountry(settingsOfCountry.getSelectedItem().toString());
        String textYear = settingsOfYear.getText().toString();
        if (textYear.length() == 0) {
            settings.setYear(null);
        } else {
            settings.setYear(Integer.parseInt(settingsOfYear.getText().toString()));
        }
        settings.setMinRating(ratingSettings.getValues().get(0));
        settings.setMaxRating(ratingSettings.getValues().get(1));
        settings.setTypeOfSort(settingsOfSort.getSelectedItemPosition());
    }
}