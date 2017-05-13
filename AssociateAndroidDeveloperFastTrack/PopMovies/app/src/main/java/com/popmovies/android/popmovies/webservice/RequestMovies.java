package com.popmovies.android.popmovies.webservice;

import android.content.Context;

import com.popmovies.android.popmovies.BuildConfig;
import com.popmovies.android.popmovies.MainActivity;

import okhttp3.HttpUrl;

/**
 * Created by Thiago on 13/05/2017.
 */

public class RequestMovies {

    private static final String BASE_URL = "http://api.themoviedb.org/3";
    protected static final String POPULAR_MOVIE_BASE_URL = BASE_URL + "/movie/popular?";
    protected static final String TOP_RATED_MOVIE_BASE_URL = BASE_URL + "/movie/top_rated?";
    private static final String API_KEY_PARAM = "api_key";
    private static final String PAGE_PARAM = "page";

    public static void requestMovies(String search, int pageNumber,
                                     FetchMovies.MovieTaskCallback ui, Context context) {
        boolean isPopularSearch = search != null && MainActivity.SEARCH_TYPE_POPULAR.equals(search);

        HttpUrl.Builder builder = HttpUrl.parse(isPopularSearch ?
                POPULAR_MOVIE_BASE_URL : TOP_RATED_MOVIE_BASE_URL).newBuilder();
        builder.addQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .addQueryParameter(PAGE_PARAM, String.valueOf(pageNumber));

        FetchMovies fetchMovies = new FetchMovies(context, ui);
        fetchMovies.execute(builder);
    }
}
