package com.popmovies.android.popmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.popmovies.android.popmovies.R;

/**
 * Created by Thiago on 11/05/2017.
 */

public class PopMoviesPreferences {

    public static String getSearchType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(context.getString(R.string.pref_key_search_movies),
                context.getString(R.string.pref_popular));

        return value;
    }
}
