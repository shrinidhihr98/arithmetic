package com.math.math;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

//Filter results where gameId = 'startTime'
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

        tableLayout.setColumnStretchable(0,true);
        tableLayout.setColumnStretchable(1,true);
        tableLayout.setColumnStretchable(2,true);
        tableLayout.setColumnStretchable(3,true);
        View tableHeading = View.inflate(getActivity(), R.layout.scorestablerow, null);
        TextView problemTextHeading = tableHeading.findViewById(R.id.problem_text_view);
        TextView correctSolutionTextHeading = tableHeading.findViewById(R.id.solution_text_view);
        TextView userSolutionTextHeading = tableHeading.findViewById(R.id.user_solution_text_view);
        TextView correctTextHeading = tableHeading.findViewById(R.id.correct_text_view);

        problemTextHeading.setText(getString(R.string.scores_table_column_heading_1));
        correctSolutionTextHeading.setText(getString(R.string.scores_table_column_heading_2));
        userSolutionTextHeading.setText(getString(R.string.scores_table_column_heading_3));
        correctTextHeading.setText(getString(R.string.scores_table_column_heading_4));
        problemTextHeading.setTextColor(Color.parseColor("#1d1a05"));
        correctSolutionTextHeading.setTextColor(Color.parseColor("#1d1a05"));
        userSolutionTextHeading.setTextColor(Color.parseColor("#1d1a05"));
        correctTextHeading.setTextColor(Color.parseColor("#1d1a05"));


        tableLayout.addView(tableHeading);


        while (cursor.moveToNext()) {
            String problem = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(database.GameRuns.COLUMN_NAME_PROBLEM)));
            String correctSolution = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(database.GameRuns.COLUMN_NAME_CORRECT_SOLUTION)));
            String userSolution = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(database.GameRuns.COLUMN_NAME_USER_SOLUTION)));
            String correct = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(database.GameRuns.COLUMN_NAME_CORRECT)));

            View tableRow = View.inflate(getActivity(), R.layout.scorestablerow, null);

            TextView problemText = tableRow.findViewById(R.id.problem_text_view);
            TextView correctSolutionText = tableRow.findViewById(R.id.solution_text_view);
            TextView userSolutionText = tableRow.findViewById(R.id.user_solution_text_view);
            TextView correctText = tableRow.findViewById(R.id.correct_text_view);

            problemText.setTextColor(Color.parseColor("#1d1a05"));
            correctSolutionText.setTextColor(Color.parseColor("#1d1a05"));
            userSolutionText.setTextColor(Color.parseColor("#1d1a05"));
            correctText.setTextColor(Color.parseColor("#1d1a05"));
            problemText.setText(problem);
            correctSolutionText.setText(correctSolution);
            userSolutionText.setText(userSolution);
            System.out.println("Values of correct are:" + correct);
            if (correct.equalsIgnoreCase("1")) {
                correctText.setText(getString(R.string.correct));
            } else {
                correctText.setText(getString(R.string.incorrect));
            }


            tableLayout.addView(tableRow);

            Log.i("scoresDisplay.java", " TableRow added! " + problem + " " + correctSolution + " " + userSolution +" " +correct);
        }
        cursor.close();
        dbh.close();

//Finally add table to parent view
        ConstraintLayout scoresLayout = view.findViewById(R.id.scores_layout);
        scoresLayout.addView(tableLayout);


        Button playAgainButton = view.findViewById(R.id.play_again);
        playAgainButton.setTransformationMethod(null);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}