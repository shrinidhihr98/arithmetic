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

public class game extends Fragment {

    private final String TAG = "Game.java";

    private TextView usersolutiontext;

    private View view;
    private String[] problemsArray;
    private String[] answersArray;

    // Required empty public constructor
    public game() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.gamelayout, container, false);


        Bundle bundle =this.getArguments();
        if(bundle != null) {
            problemsArray = bundle.getStringArray("problemsArray");
            answersArray = bundle.getStringArray("answersArray");
        }

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);

        final int problemId = sharedPreferences.getInt("problemId", -1);


        TextView sumDisplay = view.findViewById(R.id.sum_text_view);
        sumDisplay.setText(problemsArray[problemId]);



        usersolutiontext = view.findViewById(R.id.user_solution_text);
        usersolutiontext.requestFocus();

        showKeyboard(getActivity(),usersolutiontext);


        usersolutiontext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){

                    String userInput = usersolutiontext.getText().toString().trim();

                    if(!TextUtils.isEmpty(userInput)) {

                        boolean correct = (answersArray[problemId].equals(userInput));
                        Log.i(TAG, "onEditorAction: Question: "+problemsArray[problemId]+" Answer: "+ answersArray[problemId]+ " User Answer: "+ userInput + " Correct? "+correct);

                        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
                        int startTime = sharedPreferences.getInt("startTime",-1);
                        int problemId = sharedPreferences.getInt("problemId", -1);

                        database.databaseHelper dbh = new database.databaseHelper(getActivity());
                        SQLiteDatabase db = dbh.getWritableDatabase();

                        ContentValues scoreValues = new ContentValues();
                        scoreValues.put(database.GameRuns.COLUMN_NAME_GAME_ID,startTime);
                        scoreValues.put(database.GameRuns.COLUMN_NAME_PROBLEM_ID, problemId);
                        scoreValues.put(database.GameRuns.COLUMN_NAME_PROBLEM, problemsArray[problemId] );
                        scoreValues.put(database.GameRuns.COLUMN_NAME_CORRECT_SOLUTION, answersArray[problemId]);
                        scoreValues.put(database.GameRuns.COLUMN_NAME_USER_SOLUTION, userInput);
                        scoreValues.put(database.GameRuns.COLUMN_NAME_CORRECT, correct);

                        long newRowId = db.insert(database.GameRuns.TABLE_NAME_SCORES,null,scoreValues);
                        Log.i(TAG, "onEditorAction: newRowId inserted into database table scores:"+newRowId);

                        dbh.close();

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        Fragment gamefragment = new game();
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("problemsArray",problemsArray);
                        bundle.putStringArray("answersArray",answersArray);

                        gamefragment.setArguments(bundle);

                        Fragment endfragment = new scoresDisplay();
                        if(problemId < 9){
                            Log.i(TAG, "game.java: problemcount is :"+ problemId);
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

    @Override
    public void onResume() {
        super.onResume();
        usersolutiontext = view.findViewById(R.id.user_solution_text);
        usersolutiontext.requestFocus();

        showKeyboard(getActivity(),usersolutiontext);

    }
}
