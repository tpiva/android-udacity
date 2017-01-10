package com.thiago.popularmovies.webservice;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.thiago.popularmovies.BuildConfig;
import com.thiago.popularmovies.MovieAdapter;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.dto.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmagalhaes on 04-Jan-17.
 */

public class FetchMovies extends AsyncTask<String, Void, List<Movie>> {

    private static final String LOG = FetchMovies.class.getSimpleName();

    protected static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String POPULAR_MOVIE_BASE_URL = BASE_URL + "/movie/popular?";
    private static final String TOP_RATED_MOVIE_BASE_URL = BASE_URL + "/movie/top_rated?";
    protected static final String API_KEY_PARAM = "api_key";
    protected static final String PAGE_PARAM = "page";
    protected static final String MOVIE_PATH = "/movie/";

    private Context mContext;
    private MovieTaskCallback mUI;

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
            Uri buildUri = Uri.parse(Utility.isPopular(mContext) ? POPULAR_MOVIE_BASE_URL : TOP_RATED_MOVIE_BASE_URL)
                    .buildUpon().appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(PAGE_PARAM, args[0]).build();

            URL url = new URL(buildUri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            return Parser.getMoviesFromJson(buffer.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG,"MalformedURLException", e);
        } catch (IOException e) {
            Log.e(LOG,"IOException", e);
        } catch (JSONException e) {
            Log.e(LOG,"JSONException", e);
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

    public interface MovieTaskCallback {
        void onPreExecute();

        void onPostExecute(List<Movie> movies);
    }
}
