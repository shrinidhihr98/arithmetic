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

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();

        String numBound = sharedPreferences.getString("numbers_bound", "20");
        String prefNumBound = sharedPreferences.getString("numbers_bound_permutation","10");

        String title = Objects.requireNonNull(getContext()).getString(R.string.summary_numbers_bound,numBound);
        numbersBound.setSummary(title);

        String prefTitle = getContext().getString(R.string.summary_numbers_bound_permutation,prefNumBound);
        numbersBoundPermutation.setSummary(prefTitle);

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

                    if(numberCheck(newValue)){
                        String title = Objects.requireNonNull(getContext()).getString(R.string.summary_numbers_bound,(String)newValue);
                        preference.setSummary(title);
                        return true;
                    }else{
                        return false;
                    }
                case "numbers_bound_permutation":
                    if(numberCheckPermutation(newValue)){
                        String title = Objects.requireNonNull(getContext()).getString(R.string.summary_numbers_bound_permutation,(String)newValue);
                        preference.setSummary(title);
                        return true;
                    }else{
                        return false;
                    }
                default:
                    return false;
            }
        }
    };

    private boolean numberCheck(Object newValue) {
        Log.i(TAG, "numberCheck: NewValue is :"+newValue);
        int minLimit;
        if(selectedOperatorsCount() >2 && basicOperatorsCount() >= 2){
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
        if(permOperatorsCount() == 0){
            minLimit = 5;
        } else if(selectedOperatorsCount() >2 && permOperatorsCount() >= 1){
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
            int selectedOperatorsCount = selectedOperatorsCount();
            String preferenceToBeChanged = preference.getKey();

            int minLimit =10;
            if(!(boolean)newValue){
                if(selectedOperatorsCount >= 4){//Three will be selected after this change.
                    return true;
                }else if(selectedOperatorsCount == 3){ //Two will be selected after this change.
                    switch (getState(preferenceToBeChanged)){
                        case 1: if(getNumbersBound() < minLimit){
                            Toast.makeText(getContext(), "To deselect, set Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                            return false;
                            }else{
                                return true;
                            }
                        case 2: if(getPermNumbersBound() < minLimit){
                            Toast.makeText(getContext(), "To deselect, set Permutation and Combination Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                            return false;
                            }else {
                                return true;
                            }
                        case 3: if(getNumbersBound()<minLimit){
                            Toast.makeText(getContext(), "To deselect, set Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                            return false;
                            }else if(getPermNumbersBound()<minLimit){
                                Toast.makeText(getContext(), "To deselect, set Permutation and Combination Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                                return false;
                            }else{
                                return true;
                            }
                    }
                }else if(selectedOperatorsCount == 2){ //One will be selected after this change.
                    switch (getState(preferenceToBeChanged)){
                        case 4:
                            if(getNumbersBound() < minLimit){
                                Toast.makeText(getContext(), "To deselect, set Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                                return false;
                            }else{
                                return true;
                            }
                        case 5: if(getPermNumbersBound()<minLimit){
                            Toast.makeText(getContext(), "To deselect, set Permutation and Combination Numbers Limit to at least ten.", Toast.LENGTH_SHORT).show();
                            return false;
                        }else{
                            return true;
                        }
                    }
                }
                if(selectedOperatorsCount == 1){
                    Toast.makeText(getContext(),"Please choose at least one operation.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
    };

    private int getState(String toBeRemoved){
        /*
            Returns 1 if two basic operators are left after removing toBeRemoved.
            Returns 2 if two perm operators are left after removing toBeRemoved.
            Returns 3 if one basic operator and one perm operator is left after removing toBeRemoved.
            Returns 4 if only one basic operator is enabled after removing toBeRemoved.
            Returns 5 if only one perm operator is enabled after removing toBeRemoved.
        */
        boolean[] checked = new boolean[6];
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[] operatorsList = new String[]{"addition_enable", "subtraction_enable", "multiplication_enable", "division_enable", "permutation_enable", "combination_enable"};
        checked[0] = sharedPref.getBoolean("addition_enable", true);
        checked[1] = sharedPref.getBoolean("subtraction_enable", true);
        checked[2] = sharedPref.getBoolean("multiplication_enable", true);
        checked[3] = sharedPref.getBoolean("division_enable", true);
        checked[4] = sharedPref.getBoolean("permutation_enable", true);
        checked[5] = sharedPref.getBoolean("combination_enable", true);
        for(int i = 0; i< 6; i++){
            if(operatorsList[i].equals(toBeRemoved)){
                checked[i] = false;
            }
        }
        int basicCount = 0;
        int permCount = 0;
        for(int i =0; i<4;i++){
            if(checked[i])basicCount++;
        }
        for(int i =4; i<6;i++){
            if(checked[i])permCount++;
        }
        if (basicCount == 2) return 1;
        if (permCount == 2) return 2;
        if (basicCount == 1 && permCount == 1) return 3;
        if (basicCount == 1 && permCount == 0) return 4;
        if (basicCount == 0 && permCount == 1) return 5;

        return 0;
    }

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

    private int getNumbersBound(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound", "20")));

    }

    private int getPermNumbersBound(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound_permutation", "10")));
    }

}
