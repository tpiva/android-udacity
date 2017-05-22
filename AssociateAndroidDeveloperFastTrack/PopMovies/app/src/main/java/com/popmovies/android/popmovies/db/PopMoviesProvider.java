package com.popmovies.android.popmovies.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Thiago on 18/05/2017.
 */

public class PopMoviesProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;

    private static UriMatcher mUriMatcher = buildMatcher();

    private PopMoviesDbHelper mPopMoviesDbHelper;

    private static UriMatcher buildMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopMoviesContract.CONTENT_AUTHORITY, PopMoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(PopMoviesContract.CONTENT_AUTHORITY, PopMoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mPopMoviesDbHelper = new PopMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = mPopMoviesDbHelper.getReadableDatabase();
        Cursor cursor;
        int matchId = mUriMatcher.match(uri);
        switch (matchId) {
            case MOVIES:
                cursor = sqLiteDatabase.query(PopMoviesContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Operation not supported.");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = mPopMoviesDbHelper.getWritableDatabase();
        Uri returnUri = null;
        int matchId = mUriMatcher.match(uri);
        switch (matchId) {
            case MOVIES:
                long id = sqLiteDatabase.insert(PopMoviesContract.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(uri,id);
                }
                break;
            default:
                throw new UnsupportedOperationException("Operation not supported.");
        }
        getContext().getContentResolver().notifyChange(returnUri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase sqLiteDatabase = mPopMoviesDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        int matchId = mUriMatcher.match(uri);
        switch (matchId) {
            case MOVIES_WITH_ID:
                rowsDeleted = sqLiteDatabase.delete(PopMoviesContract.TABLE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Operation not supported.");
        }
        if (rowsDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase sqLiteDatabase = mPopMoviesDbHelper.getWritableDatabase();
        int rowsUpdate = 0;
        int matchId = mUriMatcher.match(uri);
        switch (matchId) {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                rowsUpdate = sqLiteDatabase.update(PopMoviesContract.TABLE_NAME, contentValues,
                        "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Operation not supported.");
        }
        if (rowsUpdate > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdate;
    }
}
