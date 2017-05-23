/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 */

package com.popmovies.android.popmovies.webservice;

import android.content.Context;
import android.util.Log;

import com.popmovies.android.popmovies.Utility;
import com.popmovies.android.popmovies.bo.Movie;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Request data to server and delegate to handler of Json.
 */

public class FetchMovies {
    // COMPLETED change to another lib for get data from webservice

    private static final String LOG = FetchMovies.class.getSimpleName();

    /**
     * Gets Movies objects from WebService
     * @param context
     * @param args
     * @return
     */
    public static List<Movie> getMovies(Context context, HttpUrl.Builder... args) {
        Log.d(LOG, "Initializing task of getting Movies.");

        if (args == null) {
            return new ArrayList<>();
        }

        if (!Utility.isOnline(context)) {
            return new ArrayList<>();
        }

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(args[0].toString()).build();
            Response response = client.newCall(request).execute();

            return Parser.getMoviesFromJson(response.body().string());
        } catch (JSONException e) {
            Log.e(LOG, "JSONException", e);
        } catch (IOException e) {
            Log.e(LOG, "IOException", e);
        }

        return null;
    }
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                