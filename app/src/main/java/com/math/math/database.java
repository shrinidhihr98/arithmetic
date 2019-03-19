package com.math.math;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class database {
    //To prevent someone from accidentally instantiating this class, make it private.
    private database(){}

    //Class that defines table contents: headings

    static class GameRuns implements BaseColumns {
        static final String TABLE_NAME_SCORES = "scores";
        static final String TABLE_NAME_GAMES = "games";
        static final String COLUMN_NAME_GAME_ID = "gameId";
        static final String COLUMN_NAME_PROBLEM = "problem";
        static final String COLUMN_NAME_CORRECT_SOLUTION = "correctSolution";
        static final String COLUMN_NAME_USER_SOLUTION = "userSolution";
        static final String COLUMN_NAME_CORRECT = "correct";
        static final String COLUMN_NAME_PROBLEM_ID = "problemId";

    }

    //Sql query strings
    private static final String SQL_CREATE_GAME_TABLE = "CREATE TABLE "+ GameRuns.TABLE_NAME_GAMES +" (" +
            GameRuns.COLUMN_NAME_GAME_ID + " INTEGER PRIMARY KEY)";

    private static final String SQL_CREATE_SCORES_TABLE = "CREATE TABLE "+ GameRuns.TABLE_NAME_SCORES +" (" +
            GameRuns.COLUMN_NAME_GAME_ID + " INTEGER,"+
            GameRuns.COLUMN_NAME_PROBLEM_ID + " INTEGER,"+
            GameRuns.COLUMN_NAME_PROBLEM + " TEXT,"+
            GameRuns.COLUMN_NAME_CORRECT_SOLUTION + " TEXT,"+
            GameRuns.COLUMN_NAME_USER_SOLUTION + " TEXT,"+
            GameRuns.COLUMN_NAME_CORRECT + " VARCHAR(5),"+
            " FOREIGN KEY ("+GameRuns.COLUMN_NAME_GAME_ID +") REFERENCES GAMES(GAME_ID))";


    //Database helper handles database creation and upgrades.
    public static class databaseHelper extends SQLiteOpenHelper {
        //If you change the database schema, you must increment the database version.
        static final int DATABASE_VERSION =1;
        static final String DATABASE_NAME = "GameRuns.db";

        databaseHelper(Context context){
            super(context,DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db){
            db.execSQL(SQL_CREATE_GAME_TABLE);
            db.execSQL(SQL_CREATE_SCORES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            //Todo: implement proper upgrade policy here.
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
            onUpgrade(db, newVersion, oldVersion);
        }
    }

}

