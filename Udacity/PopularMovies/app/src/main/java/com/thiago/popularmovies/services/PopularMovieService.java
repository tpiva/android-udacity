package com.thiago.popularmovies.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.thiago.popularmovies.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tmagalhaes on 04-Jan-17.
 */

public class PopularMovieService extends IntentService {

    private static final String LOG = PopularMovieService.class.getSimpleName();

    private static final String POPULAR_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
    private static final String API_KEY_PARAM = "api_key";

    public PopularMovieService() {
        this("PopularMovieService");
    }

    public PopularMovieService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG, "Initializing service of Popular Movie.");

        try {
            Uri buildUri = Uri.parse(POPULAR_MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();

            URL url = new URL(buildUri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            String popularMovieStr = buffer.toString();
            Log.i(LOG, popularMovieStr);
        } catch (MalformedURLException e) {
            Log.e(LOG,"MalformedURLException", e);
        } catch (IOException e) {
            Log.e(LOG,"IOException", e);
        }

    }
}
