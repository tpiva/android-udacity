/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 */

package com.popmovies.android.popmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates database of popMovies.
 */

public class PopMoviesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "popMovies.db";

    public PopMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_MOVIE = "CREATE TABLE " + PopMoviesContract.TABLE_NAME + " ("
                + PopMoviesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PopMoviesContract.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + PopMoviesContract.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + PopMoviesContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + PopMoviesContract.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
                + PopMoviesContract.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "
                + PopMoviesContract.COLUMN_VOTE_COUNT + " REAL NOT NULL, "
                + PopMoviesContract.COLUMN_POSTER + " BLOB NOT NULL, UNIQUE ("
                + PopMoviesContract.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE );" ;

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopMoviesContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
