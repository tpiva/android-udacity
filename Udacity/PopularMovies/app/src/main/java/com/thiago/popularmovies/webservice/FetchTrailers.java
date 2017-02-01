package com.thiago.popularmovies.webservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.thiago.popularmovies.BuildConfig;
import com.thiago.popularmovies.TrailerMovieAdapter;
import com.thiago.popularmovies.dto.TrailerItem;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by tmagalhaes on 11-Jan-17.
 */

public class FetchTrailers extends AsyncTask<Integer, Void, ArrayList<TrailerItem>> {

    private static final String TAG = FetchTrailers.class.getSimpleName();

    private static final String VIDEOS_PATH = "/videos";

    private TrailerMovieAdapter mAdapter;

    private FetchTrailerCallback mUI;

    public FetchTrailers(TrailerMovieAdapter adapter, FetchTrailerCallback ui) {
        this.mAdapter = adapter;
        this.mUI = ui;
    }

    @Override
    protected ArrayList<TrailerItem> doInBackground(Integer... params) {

        ArrayList<TrailerItem> videos = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(FetchMovies.BASE_URL).append(FetchMovies.MOVIE_PATH).append(params[0]).append(VIDEOS_PATH).append("?");
        Uri buildUri = Uri.parse(sb.toString())
                .buildUpon().appendQueryParameter(FetchMovies.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();
        try {
            URL url = new URL(buildUri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
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

            videos = Parser.getVideoFromJson(buffer.toString());
        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        return videos;
    }

    @Override
    protected void onPostExecute(ArrayList<TrailerItem> trailerItems) {
        mAdapter.updateData(trailerItems);
        mUI.onFetchCompleted(trailerItems);
    }

    public interface FetchTrailerCallback {
        void onFetchCompleted(ArrayList<TrailerItem> trailerItems);
    }
}
