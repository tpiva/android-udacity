/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 * Support class with helper methods about format date and check is connection is available.
 */

package com.popmovies.android.popmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    private static final String LOG = Utility.class.getSimpleName();

    /**
     * Return date from String
     * @param date
     * @return
     */
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

    /**
     * Return date formatted as string from Date object.
     * @param releaseDate
     * @return
     */
    public static String getFormatDateAsString(Date releaseDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(releaseDate);
    }

    /**
     * Verify if connection is available.
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Convert an image as bitmap to byte array.
     * @param bitmap
     * @return
     */
    public static byte[] getByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] convertImage = bos.toByteArray();

        return convertImage;
    }
}
