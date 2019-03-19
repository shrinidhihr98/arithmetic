package com.math.math;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.math.math.settingsfile";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey){
        setPreferencesFromResource(R.xml.preferences,rootkey);

        mPreferences = this.getActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        Preference addition_enable = this.findPreference("addition_enable");

        Preference numbersBound = this.findPreference("numbers_bound");

        //Validate numbers only
        numbersBound.setOnPreferenceChangeListener(numberCheckListener);


    }

    /**
     * Checks that a preference is a valid numerical value
     */
    Preference.OnPreferenceChangeListener numberCheckListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            //Check that the string is an integer.
            return numberCheck(newValue);
        }
    };

    private boolean numberCheck(Object newValue) {
        if( !newValue.toString().equals("")  &&  newValue.toString().matches("\\d*") ) {
            return true;
        }
        else {
            Toast.makeText(this.getContext(), newValue+" "+getResources().getString(R.string.invalid_string), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}
