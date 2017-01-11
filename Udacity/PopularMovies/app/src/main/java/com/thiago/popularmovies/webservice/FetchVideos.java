package com.thiago.popularmovies.webservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.thiago.popularmovies.BuildConfig;
import com.thiago.popularmovies.dto.VideoItem;

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

public class FetchVideos extends AsyncTask<Integer, Void, ArrayList<VideoItem>> {

    private static final String TAG = FetchVideos.class.getSimpleName();

    private static final String VIDEOS_PATH = "/videos";

    @Override
    protected ArrayList<VideoItem> doInBackground(Integer... params) {

        ArrayList<VideoItem> videos = new ArrayList<>();

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

            videos = Parser.getVideoFromJson(buffer.toString());
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        return videos;
    }
}
