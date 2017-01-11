package com.thiago.popularmovies.webservice;

import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.dto.Movie;
import com.thiago.popularmovies.dto.ReviewItem;
import com.thiago.popularmovies.dto.VideoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmagalhaes on 09-Jan-17.
 */

public class Parser {

    private static final String OWN_RESULTS = "results";

    protected static List<Movie> getMoviesFromJson(String responseStream) throws JSONException {
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

    protected static ArrayList<VideoItem> getVideoFromJson(String responseStream) throws JSONException {

        ArrayList<VideoItem> videoItems = new ArrayList<>();

        final String OWN_ID = "id";
        final String OWN_ISO_639_1 = "iso_639_1";
        final String OWN_ISO_3166_1 = "iso_3166_1";
        final String OWN_KEY = "key";
        final String OWN_NAME = "name";
        final String OWN_SITE = "site";
        final String OWN_SIZE = "size";
        final String OWN_TYPE = "type";

        JSONObject moviesJson = new JSONObject(responseStream);

        JSONArray resultsJson = moviesJson.getJSONArray(OWN_RESULTS);

        for(int i = 0; i < resultsJson.length(); i++) {
            JSONObject result = resultsJson.getJSONObject(i);

            VideoItem videoItem = new VideoItem();

            String id = result.getString(OWN_ID);
            videoItem.setId(id);

            String iso6391 = result.getString(OWN_ISO_639_1);
            videoItem.setIso6391(iso6391);

            String iso31661 = result.getString(OWN_ISO_3166_1);
            videoItem.setIso31661(iso31661);

            String key = result.getString(OWN_KEY);
            videoItem.setKey(key);

            String name = result.getString(OWN_NAME);
            videoItem.setName(name);

            String site = result.getString(OWN_SITE);
            videoItem.setSite(site);

            Integer size = result.getInt(OWN_SIZE);
            videoItem.setSite(site);

            String type = result.getString(OWN_TYPE);
            videoItem.setType(type);

            videoItems.add(videoItem);
        }

        return videoItems;
    }

    protected static ArrayList<ReviewItem> getReviewFromJson(String responseStream) throws JSONException {

        ArrayList<ReviewItem> reviewItems = new ArrayList<>();

        final String OWN_ID = "id";
        final String OWN_AUTHOR = "author";
        final String OWN_CONTENT = "content";
        final String OWN_URL = "url";

        JSONObject moviesJson = new JSONObject(responseStream);

        JSONArray resultsJson = moviesJson.getJSONArray(OWN_RESULTS);

        for(int i = 0; i < resultsJson.length(); i++) {
            JSONObject result = resultsJson.getJSONObject(i);

            ReviewItem reviewItem = new ReviewItem();
            String id = result.getString(OWN_ID);
            reviewItem.setId(id);

            String author = result.getString(OWN_AUTHOR);
            reviewItem.setAuthor(author);

            String content = result.getString(OWN_CONTENT);
            reviewItem.setContent(content);

            String url = result.getString(OWN_URL);
            reviewItem.setUrl(url);

            reviewItems.add(reviewItem);
        }

        return reviewItems;
    }
}
