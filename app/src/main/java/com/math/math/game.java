package com.math.math;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

public class game extends Fragment {

    private String TAG = "Game.java";

    int a,b,solution;
    int userInputSolution = 0;
    TextView sumDisplay,usersolutiontext;
    View view;
    Random rand;

// Required empty public constructor
    public game() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.gamelayout, container, false);

        rand = new Random();
        a = rand.nextInt(10);
        b = rand.nextInt(10);
        solution = 0;
        int operatorchoice = rand.nextInt(4);
        String operator = null;
        switch (operatorchoice){
            case 0: operator = "+";
                solution = a+b;
                break;
            case 1: operator = "-";
                if(a<b) {
                    Log.i(TAG, "A:" + a+" B: " +b + " Swap: Swapping in progress!");
                    int j = a;
                    a = b;
                    b = j;
                    Log.i(TAG, "A:" + a+" B: " +b + " Swap: Swapping Complete!");
                }
                solution = a-b;
                break;
            case 2: operator = "*";
                solution = a*b;
                break;
            case 3: operator = "/";

                if(a<b) {
                    Log.i(TAG, "A:" + a+" B: " +b + " Swap: Swapping in progress!");

                    int j = a;
                    a = b;
                    b = j;
                    Log.i(TAG, "A:" + a+" B: " +b + " Swap: Swapping Complete!");
                }
                if(b == 0){
                    Log.i(TAG, "B is zero. Setting to 2.");
                    b =2;
                }

                solution = a/b;
                break;
            default: Log.i(TAG,"Operator not generated! Error!"+operatorchoice);
        }

        sumDisplay = view.findViewById(R.id.sumtextview);
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

    public static void showKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}