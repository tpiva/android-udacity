package com.thiago.popularmovies.webservice;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.thiago.popularmovies.BuildConfig;
import com.thiago.popularmovies.dto.ExtraMovieInfo;
import com.thiago.popularmovies.dto.Review;
import com.thiago.popularmovies.dto.Video;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by tmagalhaes on 09-Jan-17.
 */

public class FetchExtraInfoMovie extends AsyncTask<Integer, Void, ExtraMovieInfo> {

    private static final String LOG = FetchExtraInfoMovie.class.getSimpleName();

    private static final String VIDEOS_PATH = "/videos";
    private static final String REVIEWS_PATH = "/reviews";

    private ExtraMovieInfo mExtraInformations;
    private ArrayList<Video> mVideos;
    private ArrayList<Review> mReviews;

    @Override
    protected ExtraMovieInfo doInBackground(Integer... param) {
        Uri buildUri = null;

        try {

            // operation 1 both, operation 2 - only reviews
            switch (param[0]) {
                case 1:
                    StringBuilder sb = new StringBuilder();
                    sb.append(FetchMovies.BASE_URL).append(FetchMovies.MOVIE_PATH).append(param[1]).append(VIDEOS_PATH).append("?");
                    doResult(
                            doRequest(
                            (buildUri.parse(sb.toString()).buildUpon()
                                    .appendQueryParameter(FetchMovies.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)).toString()),
                            1);

                    sb = new StringBuilder();
                    sb.append(FetchMovies.BASE_URL).append(FetchMovies.MOVIE_PATH).append(param[1]).append(REVIEWS_PATH).append("?");
                    doResult(
                            doRequest(
                                    (buildUri.parse(sb.toString()).buildUpon()
                                            .appendQueryParameter(FetchMovies.API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY))
                                            .appendQueryParameter(FetchMovies.PAGE_PARAM, String.valueOf(param[2])).toString()),
                            2);
                    mExtraInformations = new ExtraMovieInfo();
                    mExtraInformations.setVideos(mVideos);
                    mExtraInformations.setReviews(mReviews);
                    break;
                case 2:
                    break;

            }

        } catch (MalformedURLException e) {
            Log.e(LOG, e.getLocalizedMessage(), e);
        } catch (ProtocolException e) {
            Log.e(LOG, e.getLocalizedMessage(), e);
        } catch (IOException e) {
            Log.e(LOG, e.getLocalizedMessage(), e);
        } catch (JSONException e) {
            Log.e(LOG, e.getLocalizedMessage(), e);
        }

        return mExtraInformations;
    }

    private InputStream doRequest(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        // Read the input stream into a String
        return urlConnection.getInputStream();
    }

    private void doResult(InputStream responseStream, int typeOperation) throws IOException, JSONException {
        StringBuffer buffer = new StringBuffer();
        if (responseStream == null) {
            // Nothing to do.
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            return;
        }

        switch (typeOperation) {
            case 1:
                mVideos = Parser.getVideoFromJson(buffer.toString());
                break;
            case 2:
                mReviews = Parser.getReviewFromJson(buffer.toString());
                break;
        }
    }
}
