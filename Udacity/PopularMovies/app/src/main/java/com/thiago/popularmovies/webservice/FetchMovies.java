package com.thiago.popularmovies.webservice;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.thiago.popularmovies.BuildConfig;
import com.thiago.popularmovies.MovieAdapter;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.data.MovieContract;
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
            String page = args[0];
            Uri buildUri = Uri.parse(Utility.isPopular(mContext) ? POPULAR_MOVIE_BASE_URL : TOP_RATED_MOVIE_BASE_URL)
                    .buildUpon().appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(PAGE_PARAM, page).build();

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

            List<Movie> movies = Parser.getMoviesFromJson(buffer.toString());
            List<Integer> currentMovies = getFavorites();

            // check movies that save as favorites.
            for(Movie movie : movies) {
                if(currentMovies.contains(movie.getId())) {
                    movie.setMarkAsFavorite(true);
                }
            }

            return movies;
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

    private List<Integer> getFavorites() {
        final String[] MOVIE_COLUMNS = {
                MovieContract.TABLE_NAME + "." + MovieContract._ID,
                MovieContract.COLUMN_OVERVIEW,
                MovieContract.COLUMN_RELEASE_DATE,
                MovieContract.COLUMN_ORIGINAL_TITLE,
                MovieContract.COLUMN_VOTE_AVERAGE,
                MovieContract.COLUMN_VOTE_COUNT,
                MovieContract.COLUMN_POSTER,
                MovieContract.COLUMN_MOVIE_ID
        };

        final int COL_MOVIE_ID = 0;
        final int COL_MOVIE_OVERVIEW = 1;
        final int COL_MOVIE_RELEASE_DATE = 2;
        final int COL_MOVIE_ORIGINAL_TITLE = 3;
        final int COL_MOVIE_VOTE_AVERAGE = 4;
        final int COL_MOVIE_VOTE_COUNT = 5;
        final int COL_MOVIE_POSTER = 6;
        final int COL_MOVIE_MOVIE_ID = 7;

        List<Integer> currentMovies = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
            MovieContract.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null);

        if(cursor.moveToFirst()) {
            while(cursor.moveToNext()) {
                currentMovies.add(cursor.getInt(COL_MOVIE_MOVIE_ID));
            }
        }
        return currentMovies;
    }
}
