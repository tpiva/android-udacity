package com.thiago.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tmagalhaes on 12-Jan-17.
 */

public class MovieContract implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "com.thiago.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final String TABLE_NAME = "movie";

    public static final String COLUMN_OVERVIEW = "overview";

    public static final String COLUMN_RELEASE_DATE = "release_date";

    public static final String COLUMN_ORIGINAL_TITLE = "original_title";

    public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    public static final String COLUMN_VOTE_COUNT = "vote_count";

    public static final String COLUMN_POSTER = "poster";

    public static final String COLUMN_MOVIE_ID = "movie_ID";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

    public static Uri buildMovieIdUri(long id) {
        return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static long getMovieidFromUri(Uri uri) {
        return Long.parseLong(uri.getPathSegments().get(1));
    }
}
