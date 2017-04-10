package com.popmovies.android.popmovies.webservice;

import com.popmovies.android.popmovies.Utility;
import com.popmovies.android.popmovies.bo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

class Parser {

    private static final String OWN_RESULTS = "results";

    static List<Movie> getMoviesFromJson(String responseStream) throws JSONException {
        List<Movie> movies = new ArrayList<>();

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
}
