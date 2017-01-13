package com.thiago.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.thiago.popularmovies.dto.Movie;

/**
 * Created by tmagalhaes on 12-Jan-17.
 */

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_MOVIE_ID = 101;

    private MovieDbHelper mMovieDbHelper;

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_MOVIE_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null
                        ,sortOrder);
                break;
            case MOVIE_WITH_MOVIE_ID:
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieContract.TABLE_NAME,
                        projection,
                        MovieContract.COLUMN_MOVIE_ID + " ?",
                        new String[]{String.valueOf(MovieContract.getMovieidFromUri(uri))},
                        null,
                        null,
                        sortOrder);
                break;
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.CONTENT_TYPE;
            case MOVIE_WITH_MOVIE_ID:
                return MovieContract.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        Uri returnUri = null;
        if(match == MOVIE) {
            long _id = db.insert(MovieContract.TABLE_NAME, null, contentValues);
            if(_id != -1) {
                returnUri = MovieContract.buildMovieUri(_id);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsModified = 0;
        if ( null == selection ) selection = "1";

        if(match == MOVIE_WITH_MOVIE_ID) {
            rowsModified = db.delete(MovieContract.TABLE_NAME, selection, selectionArgs);
        }
        if(rowsModified != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsModified;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsModified = 0;

        if(match == MOVIE_WITH_MOVIE_ID) {
            rowsModified = db.update(MovieContract.TABLE_NAME, contentValues,selection, selectionArgs);

        }
        if(rowsModified != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsModified;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mMovieDbHelper.close();
        super.shutdown();
    }
}
