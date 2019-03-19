package com.math.math;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey){
        setPreferencesFromResource(R.xml.preferences,rootkey);

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


    // Checks that a preference is a valid numerical value

    private final Preference.OnPreferenceChangeListener numberCheckListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            //Check that the string is an integer.
            System.out.println(preference.getKey());
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
        System.out.println("Newvalue is :"+newValue);
        if( !newValue.toString().equals("")  &&!newValue.toString().equals("0") &&  newValue.toString().matches("\\d*") ) {
            System.out.println("Valid number bound!");
            return true;
        }
        else {
            System.out.println("Invalid number bound!");
            Toast.makeText(this.getContext(), getResources().getString(R.string.invalid_string), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean numberCheckPermutation(Object newValue) {
        System.out.println("Newvalue is :"+newValue);
        int newvalueint = 0;
        try{
            newValue = Integer.parseInt(newValue.toString());
        }
        catch (Exception e){

            e.printStackTrace();
            Toast.makeText(this.getContext(), getResources().getString(R.string.invalid_string_permutation), Toast.LENGTH_SHORT).show();
            return false;
        }
        //noinspection ConstantConditions
        if( !newValue.toString().equals("")  &&!newValue.toString().equals("0") && newvalueint <= 12 &&  newValue.toString().matches("\\d*") ) {
            System.out.println("Valid permutation number bound!");
            return true;
        }
        else {
            System.out.println("Invalid number bound!");
            Toast.makeText(this.getContext(), getResources().getString(R.string.invalid_string_permutation), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    //Checks if atleast one checkbox is selected true, and returns.
    private final Preference.OnPreferenceChangeListener selectedCheck = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            //noinspection PointlessBooleanExpression
            if(!((boolean) newValue)) {
                return checkIfAtleastOne();
            }else
                return true;

        }

    };

    private boolean checkIfAtleastOne() {
        boolean[] checked = new boolean[6];
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        checked[0] = sharedPref.getBoolean("addition_enable", true);
        System.out.println("The shared preference addition enable is:" + checked[0]);

        checked[1] = sharedPref.getBoolean("subtraction_enable", true);
        System.out.println("The shared preference subtraction enable is:" + checked[1]);

        checked[2] = sharedPref.getBoolean("multiplication_enable", true);
        System.out.println("The shared preference multiplication enable is:" + checked[2]);

        checked[3] = sharedPref.getBoolean("division_enable", true);
        System.out.println("The shared preference division enable is:" + checked[3]);

        checked[4] = sharedPref.getBoolean("permutation_enable", false);
        System.out.println("The shared preference permutation enable is:" + checked[4]);

        checked[5] = sharedPref.getBoolean("combination_enable", false);
        System.out.println("The shared preference combination enable is:" + checked[5]);


        int count = 0;
        for(int i = 0; i<6; i++){
            if(checked[i]){count++;}
        }
        if(count < 2){
            Toast.makeText(getContext(), getResources().getString(R.string.check_atleast_one), Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;
    }

}
