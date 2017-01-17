package com.thiago.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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

    public static String getStringOfDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static byte[] getByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] convertImage = bos.toByteArray();

        return convertImage;
    }

    public static boolean isPopular(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(
                context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_popular_movies_popular))
                .equals(context.getString(R.string.pref_popular_movies_popular));
    }

    public static String getSortOrder(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(
                context.getString(R.string.pref_sort_order_key),
                context.getString(R.string.pref_popular_movies_popular));
    }

    public static void setSearchOrder(Context context, String newSearchOrder) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_sort_order_key),newSearchOrder);
        editor.commit();
    }

    public static String getYearOfReleaseDate(Date releaseDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(releaseDate);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
