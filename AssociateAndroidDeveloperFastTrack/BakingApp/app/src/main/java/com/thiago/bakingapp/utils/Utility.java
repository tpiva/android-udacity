/*
 * Create by Thiago Piva Magalhães on  01/07/17 20:47
 * Copyright (c) 2017. All right reserved.
 * File Utility.java belongs to Project BakingApp
 *
 * Last modified 01/07/17 20:05
 *
 */
package com.thiago.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utils class for BakingApp.
 */
public class Utility {

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
}
