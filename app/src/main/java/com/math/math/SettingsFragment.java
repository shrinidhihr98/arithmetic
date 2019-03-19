package com.math.math;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey){
        setPreferencesFromResource(R.xml.preferences,rootkey);
    }
}
