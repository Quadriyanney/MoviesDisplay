package com.quadriyanney.moviesdisplay.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by quadriy on 5/22/17.
 */

public class DatabaseManager extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + "(" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_THUMBNAIL + " TEXT," +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT," +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT," +
                MovieContract.MovieEntry.COLUMN_VOTE + " TEXT," +
                MovieContract.MovieEntry.COLUMN_DATE + " TEXT," +
                MovieContract.MovieEntry.COLUMN_ID + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
