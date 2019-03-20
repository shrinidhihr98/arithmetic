package com.math.math;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity.java";

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//Set default values to your preferences.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        boolean operators_enabled[] = new boolean[6];

        operators_enabled[0] = sharedPref.getBoolean(getResources().getString(R.string.key_addition_enable), true);
        Log.i(TAG, "onCreate: The shared preference addition enable is:" + operators_enabled[0]);

        operators_enabled[1] = sharedPref.getBoolean(getResources().getString(R.string.key_subtraction_enable), true);
        Log.i(TAG, "onCreate: The shared preference subtraction enable is:" + operators_enabled[1]);

        operators_enabled[2] = sharedPref.getBoolean(getResources().getString(R.string.key_multiplication_enable), true);
        Log.i(TAG, "onCreate: The shared preference multiplication enable is:" + operators_enabled[2]);

        operators_enabled[3] = sharedPref.getBoolean(getResources().getString(R.string.key_division_enable), true);
        Log.i(TAG, "onCreate: The shared preference division enable is:" + operators_enabled[3]);

        operators_enabled[4] = sharedPref.getBoolean("permutation_enable", false);
        Log.i(TAG, "onCreate: The shared preference permutation enable is:" + operators_enabled[4]);

        operators_enabled[5] = sharedPref.getBoolean("combination_enable", false);
        Log.i(TAG, "onCreate: The shared preference combination enable is:" + operators_enabled[5]);


        int numbers_bound = Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound", "10")));
        Log.i(TAG, "onCreateView: The numbers bound is:" + numbers_bound);

        int numbers_bound_permutation = Integer.parseInt(Objects.requireNonNull(sharedPref.getString("numbers_bound_permutation", "10")));
        Log.i(TAG, "onCreateView: The numbers bound is:" + numbers_bound_permutation);

        questionGenerator questiongenerator = new questionGenerator(operators_enabled,numbers_bound,numbers_bound_permutation);

        final String[] problems, answers;
        problems = questiongenerator.problems;
        answers = questiongenerator.answers;

        button = findViewById(R.id.startbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);

//Store game session details in shared preferences
                int timeStamp = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("gameInProgress", true);
                editor.putInt("problemId",0);
                editor.putInt("startTime", timeStamp);
                editor.apply();

//Store game session details in database.
                database.databaseHelper dbh = new database.databaseHelper(getApplicationContext());
                SQLiteDatabase db = dbh.getWritableDatabase();

                ContentValues gameValues = new ContentValues();
                gameValues.put(database.GameRuns.COLUMN_NAME_GAME_ID,timeStamp);

                long newRowId = db.insert(database.GameRuns.TABLE_NAME_GAMES,null,gameValues);
                Log.i(TAG, "onClick: in mainactivity, new game session has been added to database:" + newRowId);

                dbh.close();

//Replace current fragment to game fragment

                Fragment gamefragment = new game();
                Bundle bundle = new Bundle();
                bundle.putStringArray("problemsArray",problems);
                bundle.putStringArray("answersArray",answers);

                gamefragment.setArguments(bundle);
                replaceFragment(gamefragment);

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame1,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public  void onBackPressed(){

        Fragment f = MainActivity.this.getFragmentManager().findFragmentById(R.id.frame1);

        if(f instanceof game || f instanceof scoresDisplay) {
            SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
            int startTime = sharedPreferences.getInt("startTime", -1);

            database.databaseHelper dbh = new database.databaseHelper(this);
            SQLiteDatabase db = dbh.getWritableDatabase();

            String gametable = database.GameRuns.TABLE_NAME_GAMES;
            String whereClause = "gameId =?";
            String[] whereArgs = new String[]{String.valueOf(startTime)};
            db.delete(gametable, whereClause, whereArgs);

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            this.finish();
        }else{
            super.onBackPressed();
        }
    }

    //Settings options code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Todo: Add an icon as the start button in the mainActivity.
    //Todo: Add clear user data option in settings.
    //Todo: Add user statistics display.
    //Todo: Add time to answer the game.
    //Todo: Determine memory usage, and move problem generator to new service/async something if necessary.

}
