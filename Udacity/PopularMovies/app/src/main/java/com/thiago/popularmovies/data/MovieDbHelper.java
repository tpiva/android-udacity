package com.thiago.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tmagalhaes on 12-Jan-17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "popMovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_MOVIE = "CREATE TABLE " + MovieContract.TABLE_NAME + " ("
                + MovieContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
                + MovieContract.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "
                + MovieContract.COLUMN_VOTE_COUNT + " REAL NOT NULL, "
                + MovieContract.COLUMN_POSTER + " BLOB NOT NULL, UNIQUE ("
                + MovieContract.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE );" ;

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
