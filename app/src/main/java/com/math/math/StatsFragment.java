package com.math.math;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

public class StatsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.statsdisplay, container, false);

        TextView statsTextView = view.findViewById(R.id.stats_text_view);

//Get game data from database
        database.databaseHelper dbh = new database.databaseHelper(getActivity());
        SQLiteDatabase db = dbh.getReadableDatabase();
        /*
        int gameCount = (int)DatabaseUtils.queryNumEntries(db, database.GameRuns.TABLE_NAME_GAMES);
        Log.i("scoresDisplay.java", "The number of complete games played is: "+gameCount);
        */
        int correctCount =(int) DatabaseUtils.queryNumEntries(db, database.GameRuns.TABLE_NAME_SCORES,
                database.GameRuns.COLUMN_NAME_CORRECT+"=?", new String[] {"1"});
        Log.i("scoresDisplay.java", "The number of correct answers is: "+correctCount);

        int incorrectCount =(int) DatabaseUtils.queryNumEntries(db, database.GameRuns.TABLE_NAME_SCORES,
                database.GameRuns.COLUMN_NAME_CORRECT+"=?", new String[] {"0"});
        Log.i("scoresDisplay.java", "The number of incorrect answers is: "+incorrectCount);

        dbh.close();
        double accuracy = 0;
        if((correctCount+incorrectCount) != 0) {
            accuracy = ( (correctCount * 100.0) / (correctCount+incorrectCount));
        }
        Log.i("scoresDisplay.java", "The accuracy is: "+accuracy);
        String summary = "Total correct answers: "+correctCount +"\nTotal incorrect answers: "+incorrectCount+"\nAccuracy: "+(String.format(Locale.ENGLISH,"%.2f",accuracy))+"%";
        statsTextView.setText(summary);


        return view;


    }

}
