package com.math.math;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    private final String TAG = "SettingsFragment.java";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
        setPreferencesFromResource(R.xml.preferences,rootKey);

        Preference additionEnable = this.findPreference("addition_enable");
        Preference subtractionEnable = this.findPreference("subtraction_enable");
        Preference multiplicationEnable = this.findPreference("multiplication_enable");
        Preference divisionEnable = this.findPreference("division_enable");
        Preference permutationEnable = this.findPreference("permutation_enable");
        Preference combinationEnable = this.findPreference("combination_enable");

        Preference numbersBound = this.findPreference("numbers_bound");
        Preference numbersBoundPermutation = this.findPreference("numbers_bound_permutation");

        additionEnable.setOnPreferenceChangeListener(selectedCheck);
        subtractionEnable.setOnPreferenceChangeListener(selectedCheck);
        multiplicationEnable.setOnPreferenceChangeListener(selectedCheck);
        divisionEnable.setOnPreferenceChangeListener(selectedCheck);
        permutationEnable.setOnPreferenceChangeListener(selectedCheck);
        combinationEnable.setOnPreferenceChangeListener(selectedCheck);


        //Validate numbers only
        numbersBound.setOnPreferenceChangeListener(numberCheckListener);
        numbersBoundPermutation.setOnPreferenceChangeListener(numberCheckListener);

    }

    private final Preference.OnPreferenceChangeListener numberCheckListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case "numbers_bound":
                    return numberCheck(newValue);
                case "numbers_bound_permutation":
                    return numberCheckPermutation(newValue);
                default:
                    return false;
            }
        }
    };

    private boolean numberCheck(Object newValue) {
        System.out.println("NewValue is :"+newValue);
        int minLimit;
        if(selectedOperatorsCount() >1){
            minLimit = 5;
        }else{
            minLimit = 10;
        }
        if( !newValue.toString().equals("") &&  newValue.toString().matches("\\d*") ) {
            if(Integer.valueOf((String) newValue)>=minLimit ){
                Log.i(TAG, "numberCheck: Valid number bound! Returning true.");
                return true;
            }
            else{
                Log.i(TAG, "numberCheck: Number bound not enough!");
                Toast.makeText(this.getContext(), "Unable to set new value! Use a number more than or equal to "+minLimit+".", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        else {
            Log.i(TAG, "numberCheck: Input is not a number!");
            Toast.makeText(this.getContext(), "Unable to set new value! Use a number more than or equal to "+minLimit+".", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean numberCheckPermutation(Object newValue) {
        System.out.println("NewValue is :"+newValue);
        int minLimit;
        if(selectedOperatorsCount() >1){
            minLimit = 5;
        }else{
            minLimit = 10;
        }

        if( !newValue.toString().equals("")  && newValue.toString().matches("\\d*") ) {
            if((Integer.valueOf((String) newValue) >=minLimit) && (Integer.valueOf((String) newValue) <= 12)){
                System.out.println("Valid permutation number bound!");
                return true;
            }
            else{
                System.out.println("Number bound not enough!");
                Toast.makeText(this.getContext(), "Unable to set new value! Use numbers in range "+minLimit+" to 12.", Toast.LENGTH_SHORT).show();

                return false;
            }

        }
        else {
            System.out.println("Invalid number bound!");
            Toast.makeText(this.getContext(), getResources().getString(R.string.invalid_string_permutation), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private final Preference.OnPreferenceChangeListener selectedCheck = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            int permOperatorsCount = permOperatorsCount();
            int basicOperatorsCount = basicOperatorsCount();
            int selectedOperatorsCount = selectedOperatorsCount();
            int minLimit ;
            if(!(boolean)newValue){
                if(selectedOperatorsCount == 1){
                    Toast.makeText(getContext(),"Please choose at least one operation.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(basicOperatorsCount > 0 && permOperatorsCount == 0){
                    Log.i(TAG, "onPreferenceChange: basicOperators are being validated.");

                    if(basicOperatorsCount > 3){
                        minLimit = 5;
                    }else{
                        minLimit = 10;
                    }
                    Log.i(TAG, "onPreferenceChange: minLimit is"+minLimit);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    int numbersBound = Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound", "20")));
                    if(numbersBound < minLimit){
                        Toast.makeText(getContext(), "To unselect, set Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;

                }
                if(basicOperatorsCount == 0 && permOperatorsCount > 0){
                    Log.i(TAG, "onPreferenceChange: permOperators are being validated.");
                    minLimit = 10;

                    Log.i(TAG, "onPreferenceChange: minLimit is"+minLimit);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    int numbersBoundPermutation = Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound_permutation", "10")));
                    if(numbersBoundPermutation < minLimit){
                        Toast.makeText(getContext(), "To select only one operator, set Permutation and Combination Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                }

                if(basicOperatorsCount == 1){
                    minLimit = 10;
                    String preferenceToBeChanged = preference.getKey();
                    Log.i(TAG, "onPreferenceChange: Preference to be be changed is:"+preferenceToBeChanged);
                    if(preferenceToBeChanged.equals("addition_enable") ||
                            preferenceToBeChanged.equals("subtraction_enable") ||
                            preferenceToBeChanged.equals("multiplication_enable")||
                            preferenceToBeChanged.equals("division_enable")){
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        int numbersBoundPermutation = Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound_permutation", "10")));
                        if(numbersBoundPermutation < minLimit){
                            Toast.makeText(getContext(), "To unselect, set Permutation and Combination Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        return true;
                    }else{
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        int numbersBound = Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound", "20")));
                        if(numbersBound < minLimit){
                            Toast.makeText(getContext(), "To unselect, set Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        return true;
                    }
                }
            }
            return true;
        }
    };


    private int selectedOperatorsCount() {
        boolean[] checked = new boolean[6];
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        checked[0] = sharedPref.getBoolean("addition_enable", true);
        checked[1] = sharedPref.getBoolean("subtraction_enable", true);
        checked[2] = sharedPref.getBoolean("multiplication_enable", true);
        checked[3] = sharedPref.getBoolean("division_enable", true);
        checked[4] = sharedPref.getBoolean("permutation_enable", true);
        checked[5] = sharedPref.getBoolean("combination_enable", true);
        int count = 0;
        for(int i = 0; i<6; i++){
            if(checked[i]){count++;}
        }
        Log.i(TAG, "selectedOperatorsCount: selectedOperatorsCount is: "+count);
        return count;
    }

    private int basicOperatorsCount(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean[] checked = new boolean[4];

        checked[0] = sharedPref.getBoolean("addition_enable", true);
        checked[1] = sharedPref.getBoolean("subtraction_enable", true);
        checked[2] = sharedPref.getBoolean("multiplication_enable", true);
        checked[3] = sharedPref.getBoolean("division_enable", true);

        int count = 0;
        for(int i = 0; i<4; i++){
            if(checked[i]){count++;}
        }
        Log.i(TAG, "basicOperatorsCount: basicOperatorsCount is: "+count);
        return count;
    }

    private int permOperatorsCount(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean[] checked = new boolean[2];

        checked[0] = sharedPref.getBoolean("permutation_enable", true);
        checked[1] = sharedPref.getBoolean("combination_enable", true);

        int count = 0;
        for(int i = 0; i<2; i++){
            if(checked[i]){count++;}
        }
        Log.i(TAG, "permOperatorsCount: permOperatorsCount is: "+count);
        return count;
    }
}
