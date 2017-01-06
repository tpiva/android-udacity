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

    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String POPULAR_MOVIE_BASE_URL = BASE_URL + "/movie/popular?";
    private static final String TOP_RATED_MOVIE_BASE_URL = BASE_URL + "/movie/top_rated?";
    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

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
            return null;
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
            return getMoviesFromJson(buffer.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG,"MalformedURLException", e);
        } catch (IOException e) {
            Log.e(LOG,"IOException", e);
        } catch (JSONException e) {
            Log.e(LOG,"JSONException", e);
        }

        return null;
    }

    private List<Movie> getMoviesFromJson(String responseStream) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        final String OWN_RESULTS = "results";

        // results fields
        final String OWN_POST_PATH = "poster_path";
        final String OWN_ADULT = "adult";
        final String OWN_OVERVIEW = "overview";
        final String OWN_RELEASE_DATE = "release_date";
        final String OWN_GENRE_IDS = "genre_ids";
        final String OWN_ID = "id";
        final String OWN_ORIGINAL_TITLE ="original_title";
        final String OWN_ORIGINAL_LANGUAGE = "original_language";
        final String OWN_TITLE ="title";
        final String OWN_BACKDROP_PATH = "backdrop_path";
        final String OWN_POPULARITY = "popularity";
        final String OWN_VOTE_COUNT = "vote_count";
        final String OWN_VIDEO = "video";
        final String OWN_VOTE_AVERAGE = "vote_average";

        JSONObject moviesJson = new JSONObject(responseStream);

        JSONArray resultsJson = moviesJson.getJSONArray(OWN_RESULTS);

        for(int i = 0; i < resultsJson.length(); i++) {
            JSONObject result = resultsJson.getJSONObject(i);
            // create an new Movie
            Movie movie = new Movie();

            String postPath = result.getString(OWN_POST_PATH);
            movie.setPosterPath(postPath);

            boolean isAdult = result.getBoolean(OWN_ADULT);
            movie.setAdult(isAdult);

            String overview = result.getString(OWN_OVERVIEW);
            movie.setOverview(overview);

            String releaseDate = result.getString(OWN_RELEASE_DATE);
            movie.setReleaseDate(Utility.getFormatDate(releaseDate));

            int id = result.getInt(OWN_ID);
            movie.setId(id);

            String originalTitle = result.getString(OWN_ORIGINAL_TITLE);
            movie.setOriginalTitle(originalTitle);

            String originalLanguage = result.getString(OWN_ORIGINAL_LANGUAGE);
            movie.setOriginalLanguage(originalLanguage);

            String title = result.getString(OWN_TITLE);
            movie.setTitle(title);

            String backDropPath = result.getString(OWN_BACKDROP_PATH);
            movie.setBackdropPath(backDropPath);

            double popularity = result.getDouble(OWN_POPULARITY);
            movie.setPopularity(popularity);

            int voteCount = result.getInt(OWN_VOTE_COUNT);
            movie.setVoteCount(voteCount);

            boolean isVideo = result.getBoolean(OWN_VIDEO);
            movie.setVideo(isVideo);

            double average = result.getDouble(OWN_VOTE_AVERAGE);
            movie.setVoteAverage(average);

            JSONArray genresIds = result.getJSONArray(OWN_GENRE_IDS);
            int[] genreId = new int[genresIds.length()];
            for(int j = 0; j < genresIds.length(); j++) {
                genreId[j] = (int)genresIds.get(j);
            }
            movie.setGenreIds(genreId);

            movies.add(movie);
        }

        return movies;
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
