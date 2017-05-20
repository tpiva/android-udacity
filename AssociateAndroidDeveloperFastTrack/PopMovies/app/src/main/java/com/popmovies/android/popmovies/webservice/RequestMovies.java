package com.popmovies.android.popmovies.webservice;

import android.content.Context;

import com.popmovies.android.popmovies.BuildConfig;
import com.popmovies.android.popmovies.MainActivity;
import com.popmovies.android.popmovies.bo.Movie;

import java.util.List;

import okhttp3.HttpUrl;

/**
 * Created by Thiago on 13/05/2017.
 */

public class RequestMovies {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    protected static final String POPULAR_MOVIE_BASE_URL = BASE_URL + "popular?";
    protected static final String TOP_RATED_MOVIE_BASE_URL = BASE_URL + "top_rated?";

    private static final String REVIEWS_PATH = "/reviews";
    private static final String VIDEOS_PATH = "/videos";

    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    public static List<Movie> requestMovies(Context context, String search, int pageNumber) {
        boolean isPopularSearch = search != null && MainActivity.SEARCH_TYPE_POPULAR.equals(search);

        HttpUrl.Builder builder = HttpUrl.parse(isPopularSearch ?
                POPULAR_MOVIE_BASE_URL : TOP_RATED_MOVIE_BASE_URL).newBuilder();
        builder.addQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .addQueryParameter(PAGE_PARAM, String.valueOf(pageNumber));

        return FetchMovies.getMovies(context, builder);
    }

    public static void requestTrailerOrReview(Context context, Movie movie,
                                              FetchTrailerReview.TrailerReviewTaskCallback ui) {
        StringBuilder urlTrailer = new StringBuilder();
        urlTrailer.append(BASE_URL).append(movie.getId()).append(VIDEOS_PATH).append("?");

        StringBuilder urlReview = new StringBuilder();
        urlReview.append(BASE_URL).append(movie.getId()).append(REVIEWS_PATH).append("?");

        HttpUrl.Builder builderTrailer = HttpUrl.parse(urlTrailer.toString()).newBuilder();
        builderTrailer.addQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY);

        HttpUrl.Builder builderReview = HttpUrl.parse(urlReview.toString()).newBuilder();
        builderReview.addQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY);

        FetchTrailerReview fetchTrailerReview = new FetchTrailerReview(context, movie, ui);
        fetchTrailerReview.execute(builderTrailer, builderReview);
    }
}
