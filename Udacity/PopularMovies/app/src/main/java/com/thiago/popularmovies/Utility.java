package com.thiago.popularmovies;

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
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            Log.e(LOG, "ParseException",e);
        }

        return null;
    }
}
