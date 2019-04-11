package com.math.math;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.widget.Toast;

public class DialogPrefFragCompat extends PreferenceDialogFragmentCompat {
    public static DialogPrefFragCompat newInstance(String key) {
        final DialogPrefFragCompat fragment = new DialogPrefFragCompat();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            database.databaseHelper dbh = new database.databaseHelper(getActivity());
            SQLiteDatabase db = dbh.getWritableDatabase();

            db.execSQL("delete from "+database.GameRuns.TABLE_NAME_SCORES);
            db.execSQL("delete from "+database.GameRuns.TABLE_NAME_GAMES);

            db.close();

            Toast.makeText(getContext(),"Cleared!",Toast.LENGTH_SHORT).show();
        }
    }
}