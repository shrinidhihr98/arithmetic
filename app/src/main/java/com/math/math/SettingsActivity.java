package com.math.math;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.R.id.content;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Provide up action for navigation
        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(content, new SettingsFragment())
                .commit();
    }

    @Override
    public  void onBackPressed(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
