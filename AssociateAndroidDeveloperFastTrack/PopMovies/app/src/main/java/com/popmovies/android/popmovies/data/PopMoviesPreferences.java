/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 */

package com.popmovies.android.popmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.popmovies.android.popmovies.R;

/**
 * Utils class to get search preference of user (favorite, popular or top rated search);
 */

public class PopMoviesPreferences {

    /**
     * Gets current search type on preferences.
     * @param context
     * @return
     */
    public static String getSearchType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(context.getString(R.string.pref_key_search_movies),
                context.getString(R.string.pref_popular));

        return value;
    }
}
