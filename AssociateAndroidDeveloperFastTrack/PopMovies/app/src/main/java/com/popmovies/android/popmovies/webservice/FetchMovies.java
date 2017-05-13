/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 * Request data to server and delegate to handler of Json.
 */

package com.popmovies.android.popmovies.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.popmovies.android.popmovies.BuildConfig;
import com.popmovies.android.popmovies.MainActivity;
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

public class FetchMovies extends AsyncTask<HttpUrl.Builder, Void, List<Movie>> {
    // COMPLETED change to another lib for get data from webservice

    private static final String LOG = FetchMovies.class.getSimpleName();

    private final Context mContext;
    private final MovieTaskCallback mUI;

    public FetchMovies(Context context, MovieTaskCallback ui) {
        this.mContext = context;
        this.mUI = ui;
    }

    @Override
    protected List<Movie> doInBackground(HttpUrl.Builder... args) {
        Log.d(LOG, "Initializing task of Popular Movie.");

        if(args == null) {
            return new ArrayList<>();
        }

        if(!Utility.isOnline(mContext)) {
            return new ArrayList<>();
        }

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(args[0].toString()).build();
            Response response = client.newCall(request).execute();

            return Parser.getMoviesFromJson(response.body().string());
        } catch (JSONException e) {
            Log.e(LOG,"JSONException", e);
        } catch (IOException e) {
            Log.e(LOG,"IOException", e);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        mUI.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        mUI.onPostExecute(movies);
    }

    /**
     * Interface implemented by UI component to get information after AsyncTask finish and handler
     * UI components.
     */
    public interface MovieTaskCallback {
        void onPreExecute();

        void onPostExecute(List<Movie> movies);
    }

}
