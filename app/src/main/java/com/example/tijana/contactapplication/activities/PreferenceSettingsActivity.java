package com.example.tijana.contactapplication.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.tijana.contactapplication.R;

public class PreferenceSettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
