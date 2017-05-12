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

public class FetchMovies extends AsyncTask<String, Void, List<Movie>> {
    // COMPLETED change to another lib for get data from webservice

    private static final String LOG = FetchMovies.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String POPULAR_MOVIE_BASE_URL = BASE_URL + "/movie/popular?";
    private static final String TOP_RATED_MOVIE_BASE_URL = BASE_URL + "/movie/top_rated?";
    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    private final Context mContext;
    private final MovieTaskCallback mUI;

    public FetchMovies(Context context, MovieTaskCallback ui) {
        this.mContext = context;
        this.mUI = ui;
    }

    @Override
    protected List<Movie> doInBackground(String... args) {
        Log.d(LOG, "Initializing task of Popular Movie.");

        if(args == null) {
            return new ArrayList<>();
        }

        if(!Utility.isOnline(mContext)) {
            return new ArrayList<>();
        }

        try {
            boolean isPopularSearch = args[0] != null && MainActivity.SEARCH_TYPE_POPULAR.equals(args[0]);
            String page = args[1];

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder builder = HttpUrl.parse(isPopularSearch ?
                    POPULAR_MOVIE_BASE_URL : TOP_RATED_MOVIE_BASE_URL).newBuilder();
            builder.addQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .addQueryParameter(PAGE_PARAM, page);

            Request request = new Request.Builder().url(builder.toString()).build();
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
