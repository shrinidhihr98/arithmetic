package com.math.math;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class scoresDisplay extends Fragment {

    //Required empty public constructor
    public scoresDisplay(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.scoresdisplaylayout, container, false);

//Get game session data from preferences
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String startTime = String.valueOf(sharedPreferences.getInt("startTime", -1));

//Get game data from database
        database.databaseHelper dbh = new database.databaseHelper(getActivity());
        SQLiteDatabase db = dbh.getReadableDatabase();

        String[] projection = {
                database.GameRuns.COLUMN_NAME_GAME_ID,
                database.GameRuns.COLUMN_NAME_PROBLEM_ID,
                database.GameRuns.COLUMN_NAME_PROBLEM,
                database.GameRuns.COLUMN_NAME_CORRECT_SOLUTION,
                database.GameRuns.COLUMN_NAME_USER_SOLUTION,
                database.GameRuns.COLUMN_NAME_CORRECT
        };

//Filter results where gameId = 'starttime'
        String selection = database.GameRuns.COLUMN_NAME_GAME_ID + " = ?";
        String[] selectionArgs = {startTime};

        String sortOrder = database.GameRuns.COLUMN_NAME_PROBLEM_ID + " ASC";

        Cursor cursor = db.query(
                database.GameRuns.TABLE_NAME_SCORES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

//Create table layout, add rows and fill data, add filled rows into the table view.
        TableLayout tableLayout = new TableLayout(getActivity());

        View tableHeading = View.inflate(getActivity(), R.layout.scorestablerow, null);
        TextView problemtextHeading = tableHeading.findViewById(R.id.problemtextview);
        TextView correctSolutiontextHeading = tableHeading.findViewById(R.id.solutiontextview);
        TextView userSolutiontextHeading = tableHeading.findViewById(R.id.usersolutiontextview);
        TextView correcttextHeading = tableHeading.findViewById(R.id.correcttextview);

        problemtextHeading.setText(getString(R.string.scorestablecolumnheading1));
        correctSolutiontextHeading.setText(getString(R.string.scorestablecolumnheading2));
        userSolutiontextHeading.setText(getString(R.string.scorestablecolumnheading3));
        correcttextHeading.setText(getString(R.string.scorestablecolumnheading4));
        tableLayout.addView(tableHeading);


        while (cursor.moveToNext()) {
            String problem = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(database.GameRuns.COLUMN_NAME_PROBLEM)));
            String correctSolution = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(database.GameRuns.COLUMN_NAME_CORRECT_SOLUTION)));
            String userSolution = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(database.GameRuns.COLUMN_NAME_USER_SOLUTION)));
            String correct = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(database.GameRuns.COLUMN_NAME_CORRECT)));

            View tableRow = View.inflate(getActivity(), R.layout.scorestablerow, null);

            TextView problemtext = tableRow.findViewById(R.id.problemtextview);
            TextView correctSolutiontext = tableRow.findViewById(R.id.solutiontextview);
            TextView userSolutiontext = tableRow.findViewById(R.id.usersolutiontextview);
            TextView correcttext = tableRow.findViewById(R.id.correcttextview);

            problemtext.setText(problem);
            correctSolutiontext.setText(correctSolution);
            userSolutiontext.setText(userSolution);
            System.out.println("Values of correct are:" + correct);
            if (correct.equalsIgnoreCase("1")) {
                correcttext.setText(getString(R.string.correct));
            } else {
                correcttext.setText(getString(R.string.incorrect));
            }


            tableLayout.addView(tableRow);

            Log.i("scoresDisplay.java", " Tablerow added! " + problem + " " + correctSolution + " " + userSolution +" " +correct);
        }
        cursor.close();
        dbh.close();

//Finally add table to parent view
        ConstraintLayout scoreslayout = view.findViewById(R.id.scoreslayout);
        scoreslayout.addView(tableLayout);


        Button playagainbutton = view.findViewById(R.id.playagain);
        playagainbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}