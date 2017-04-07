package com.popmovies.android.popmovies.webservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.popmovies.android.popmovies.BuildConfig;
import com.popmovies.android.popmovies.bo.ReviewItem;

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

public class FetchReviews extends AsyncTask<Integer, Void, ArrayList<ReviewItem>> {

    private static final String TAG = FetchMovies.class.getSimpleName();

    private static final String REVIEWS_PATH = "/reviews";

    private FetchReviewsCallback mUi;

    public FetchReviews(FetchReviewsCallback ui) {
        this.mUi = ui;
    }

    @Override
    protected ArrayList<ReviewItem> doInBackground(Integer... params) {
        ArrayList<ReviewItem> reviews = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(FetchMovies.BASE_URL).append(FetchMovies.MOVIE_PATH).append(params[0]).append(REVIEWS_PATH).append("?");
        Uri buildUri = Uri.parse(sb.toString())
                .buildUpon().appendQueryParameter(FetchMovies.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(FetchMovies.PAGE_PARAM, String.valueOf(params[1])).build();
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

            reviews = Parser.getReviewFromJson(buffer.toString());
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }

        return reviews;
    }

    @Override
    protected void onPreExecute() {
        mUi.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewItem> reviewItems) {
        mUi.onPostExecute(reviewItems);
    }

    public interface FetchReviewsCallback {
        void onPreExecute();
        void onPostExecute(ArrayList<ReviewItem> reviewItems);
    }
}
