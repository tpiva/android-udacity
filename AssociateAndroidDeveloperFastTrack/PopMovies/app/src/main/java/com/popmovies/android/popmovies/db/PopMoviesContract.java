/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 */

package com.popmovies.android.popmovies.db;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Represents columns of table Movie and URI/URLs of content provider of PopMovies.
 */

public class PopMoviesContract implements BaseColumns{

    public static final String CONTENT_AUTHORITY = "com.popmovies.android.popmovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    public static final String TABLE_NAME = "movie";

    public static final String COLUMN_OVERVIEW = "overview";

    public static final String COLUMN_RELEASE_DATE = "release_date";

    public static final String COLUMN_ORIGINAL_TITLE = "original_title";

    public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    public static final String COLUMN_VOTE_COUNT = "vote_count";

    public static final String COLUMN_POSTER = "poster";

    public static final String COLUMN_MOVIE_ID = "movie_ID";

    public static Uri buildUriWithId(long id) {
        return ContentUris.withAppendedId(CONTENT_URI,id);
    }

}
