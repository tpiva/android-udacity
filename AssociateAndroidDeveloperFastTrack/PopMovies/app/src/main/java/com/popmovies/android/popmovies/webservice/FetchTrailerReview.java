/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 */

package com.popmovies.android.popmovies.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.popmovies.android.popmovies.Utility;
import com.popmovies.android.popmovies.bo.Movie;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Request extra information of movies to webservice and delegate to Json.
 */

public class FetchTrailerReview extends AsyncTask<HttpUrl.Builder, Void, Void> {

    private static final String LOG = FetchTrailerReview.class.getSimpleName();

    private final Context mContext;
    private final TrailerReviewTaskCallback mUI;
    private Movie mMovie;

    public FetchTrailerReview(Context context, Movie movie, TrailerReviewTaskCallback ui) {
        this.mContext = context;
        this.mUI = ui;
        this.mMovie = movie;
    }

    @Override
    protected Void doInBackground(HttpUrl.Builder... args) {
        Log.d(LOG, "Initializing task of getting Review and Trialer of Movies.");

        if(args == null) {
            return null;
        }

        if(!Utility.isOnline(mContext)) {
            return null;
        }

        try {
            OkHttpClient client = new OkHttpClient();
            Request requestTrailer = new Request.Builder().url(args[0].toString()).build();
            Request requestReviews = new Request.Builder().url(args[1].toString()).build();

            Response response = client.newCall(requestTrailer).execute();
            mMovie.setTrailers(Parser.getVideoFromJson(response.body().string()));

            response = client.newCall(requestReviews).execute();
            mMovie.setReviews(Parser.getReviewFromJson(response.body().string()));

            return null;
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
    protected void onPostExecute(Void aVoid) {
        mUI.onPostExecute(aVoid);
    }

    /**
     * Interface implemented by UI component to get information after AsyncTask finish and handler
     * UI components.
     */
    public interface TrailerReviewTaskCallback {
        void onPreExecute();

        void onPostExecute(Void aVoid);
    }

}
