package com.example.pinghomeapp;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String string){
        addPreferencesFromResource(R.xml.preferences);
    }
}
