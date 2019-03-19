package com.math.math;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class game extends Fragment {

    private final String TAG = "Game.java";

    private int a;
    private int b;
    private int solution;
    private int userInputSolution = 0;
    private TextView usersolutiontext;
    private View view;

    // Required empty public constructor
    public game() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.gamelayout, container, false);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean useAddition = sharedPref.getBoolean(getResources().getString(R.string.key_addition_enable), true);
        Log.i(TAG, "onCreate: The shared preference addition enable is:" + useAddition);

        boolean useSubtraction = sharedPref.getBoolean(getResources().getString(R.string.key_subtraction_enable), true);
        Log.i(TAG, "onCreate: The shared preference subtraction enable is:" + useSubtraction);

        boolean useMultiplication = sharedPref.getBoolean(getResources().getString(R.string.key_multiplication_enable), true);
        Log.i(TAG, "onCreate: The shared preference multiplication enable is:" + useMultiplication);

        boolean useDivision = sharedPref.getBoolean(getResources().getString(R.string.key_division_enable), true);
        Log.i(TAG, "onCreate: The shared preference division enable is:" + useDivision);

        boolean usePermutation = sharedPref.getBoolean("permutation_enable", false);
        Log.i(TAG, "onCreate: The shared preference permutation enable is:" + usePermutation);

        boolean useCombination = sharedPref.getBoolean("combination_enable", false);
        Log.i(TAG, "onCreate: The shared preference combination enable is:" + useCombination);


        int numbersBound = Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound", "10")));
        Log.i(TAG, "onCreateView: The numbers bound is:" + numbersBound);

        int numbers_bound_permutation = Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound_permutation", "10")));
        Log.i(TAG, "onCreateView: The numbers bound is:" + numbers_bound_permutation);


        ArrayList<String> operatorArray = new ArrayList<>();
        if (useAddition) {
            operatorArray.add("+");
        }
        if (useSubtraction) {
            operatorArray.add("-");
        }
        if (useMultiplication) {
            operatorArray.add("*");
        }
        if (useDivision) {
            operatorArray.add("/");
        }
        if(usePermutation){
            operatorArray.add("P");
        }
        if(useCombination){
            operatorArray.add("C");
        }

        Log.i(TAG, "onCreateView: The operators used are:" + operatorArray);

        Random rand = new Random();
        solution = 0;
        String operator = operatorArray.get(rand.nextInt(operatorArray.size()));
        if(operator.equals("P") || operator.equals("C")){
            a = rand.nextInt(numbers_bound_permutation);
            b = rand.nextInt(numbers_bound_permutation);
        }else {
            a = rand.nextInt(numbersBound);
            b = rand.nextInt(numbersBound);
        }

        Log.i(TAG, "onCreateView: a generated:"+ a +"\nb generated: "+b);
        switch (operator) {
            case "+":
                solution = a + b;
                break;
            case "-":
                if (a < b) {
                    Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping in progress!");
                    int j = a;
                    a = b;
                    b = j;
                    Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping Complete!");
                }
                solution = a - b;
                break;
            case "*":
                solution = a * b;
                break;
            case "/":

                if (a < b) {
                    Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping in progress!");

                    int j = a;
                    a = b;
                    b = j;
                    Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping Complete!");
                }
                if (b == 0) {
                    Log.i(TAG, "Division: B is zero. Setting B to 2.");
                    b = 2;
                }

                solution = a / b;
                break;
            case "P":
                if (a < b) {
                    Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping in progress!");

                    int j = a;
                    a = b;
                    b = j;
                    Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping Complete!");
                }

                solution = permutation(a,b);
                break;
            case "C":
                if (a < b) {
                    Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping in progress!");

                    int j = a;
                    a = b;
                    b = j;
                    Log.i(TAG, "A:" + a + " B: " + b + " Swap: Swapping Complete!");
                }

                solution=combination(a,b);
                break;

            default:
                Log.i(TAG, "Operator not generated! Error!" + operator);
        }

        Log.i(TAG, "onCreateView: Solution generated: "+solution);

        TextView sumDisplay = view.findViewById(R.id.sumtextview);
        sumDisplay.setText(getString(R.string.problemstring, a,operator,b));

        usersolutiontext = view.findViewById(R.id.usersolutiontext);
        usersolutiontext.requestFocus();
        showKeyboard(getActivity(),usersolutiontext);

        final String finalOperator = operator;
        usersolutiontext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){

                    String userInput = usersolutiontext.getText().toString().trim();

                    if(!TextUtils.isEmpty(userInput)) {
                        userInputSolution = Integer.parseInt(userInput);
                        boolean correct = (solution == userInputSolution);

                        String buffer = a + finalOperator + b + " " + solution + " " + userInputSolution + " " + correct + "|";
                        Log.i(TAG, "onEditorAction: is reached" + buffer);

                        database.databaseHelper dbh = new database.databaseHelper(getActivity());
                        SQLiteDatabase db = dbh.getWritableDatabase();

                        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
                        int startTime = sharedPreferences.getInt("startTime",-1);
                        int problemId = sharedPreferences.getInt("problemId",-1);

                        ContentValues scoreValues = new ContentValues();
                        scoreValues.put(database.GameRuns.COLUMN_NAME_GAME_ID,startTime);
                        scoreValues.put(database.GameRuns.COLUMN_NAME_PROBLEM_ID, problemId);
                        scoreValues.put(database.GameRuns.COLUMN_NAME_PROBLEM, a+finalOperator+b );
                        scoreValues.put(database.GameRuns.COLUMN_NAME_CORRECT_SOLUTION, solution);
                        scoreValues.put(database.GameRuns.COLUMN_NAME_USER_SOLUTION, userInputSolution);
                        scoreValues.put(database.GameRuns.COLUMN_NAME_CORRECT, correct);

                        long newRowId = db.insert(database.GameRuns.TABLE_NAME_SCORES,null,scoreValues);
                        Log.i(TAG, "onEditorAction: newRowId inserted into database table scores:"+newRowId);

                        dbh.close();

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        Fragment gamefragment = new game();
                        Fragment endfragment = new scoresDisplay();
                        if(problemId < 9){
                            Log.i(TAG, "game.java: problemcount is :"+problemId);
                            Log.i(TAG, "game.java: Incrementing problemcount and reloading gamefragment");
                            problemId++;
                            editor.putInt("problemId", problemId);
                            editor.apply();
                            replaceFragment(gamefragment);
                        }else {
                            hideKeyboardFrom(getActivity(),view);
                            editor.putBoolean("gameInProgress", false);
                            replaceFragment(endfragment);
                        }


                        handled = true;
                    }
                }
                return handled;
            }
        });

        return view;
    }


    private void replaceFragment(android.app.Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame1,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private static void showKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

    }
    private static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private static int permutation(int a, int b){
        int p = 1;
        for(int i = a; i>(a-b); i--){
            p =p*i;
        }
        return p;
    }

    private static int combination(int a, int b){
        int p = 1;
        for(int i = a; i>(a-b); i--){
            p =p*i;
        }
        int r =1;
        for(int i =1;i<=b;i++){
            r=r*i;
        }

        return p/r;
    }

}
