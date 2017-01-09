package com.thiago.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tmagalhaes on 05-Jan-17.
 */

public class Utility {

    private static String LOG = Utility.class.getSimpleName();

    public static Date getFormatDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = null;
        try {
            releaseDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(LOG, "ParseException",e);
        }

        return releaseDate;
    }

    public static boolean isPopular(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(
                context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_popular_movies_popular))
                .equals(context.getString(R.string.pref_popular_movies_popular));
    }

    public static String getYearOfReleaseDate(Date releaseDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(releaseDate);
    }
}
