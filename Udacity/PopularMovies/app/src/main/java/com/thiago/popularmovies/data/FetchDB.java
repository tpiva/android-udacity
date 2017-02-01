package com.thiago.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.dto.MovieItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmagalhaes on 13-Jan-17.
 */

public class FetchDB extends AsyncTask<Void, Void, List<MovieItem>>{

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.TABLE_NAME + "." + MovieContract._ID,
            MovieContract.COLUMN_OVERVIEW,
            MovieContract.COLUMN_RELEASE_DATE,
            MovieContract.COLUMN_ORIGINAL_TITLE,
            MovieContract.COLUMN_VOTE_AVERAGE,
            MovieContract.COLUMN_VOTE_COUNT,
            MovieContract.COLUMN_POSTER,
            MovieContract.COLUMN_MOVIE_ID
    };

    static final int COL_MOVIE_ID = 0;
    private static final int COL_MOVIE_OVERVIEW = 1;
    private static final int COL_MOVIE_RELEASE_DATE = 2;
    private static final int COL_MOVIE_ORIGINAL_TITLE = 3;
    private static final int COL_MOVIE_VOTE_AVERAGE = 4;
    private static final int COL_MOVIE_VOTE_COUNT = 5;
    private static final int COL_MOVIE_POSTER = 6;
    private static final int COL_MOVIE_MOVIE_ID = 7;

    private Context mContext;

    private FetchDbCallback mUi;

    public FetchDB(Context context, FetchDbCallback ui) {
        this.mContext = context;
        this.mUi = ui;
    }


    @Override
    protected List<MovieItem> doInBackground(Void... params) {

        List<MovieItem> movies = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null);

        if(cursor != null && cursor.moveToFirst()) {
            do {
                MovieItem movie = new MovieItem();
                movie.setId(cursor.getInt(COL_MOVIE_MOVIE_ID));
                movie.setOverview(cursor.getString(COL_MOVIE_OVERVIEW));
                movie.setReleaseDate(Utility.getFormatDate(cursor.getString(COL_MOVIE_RELEASE_DATE)));
                movie.setOriginalTitle(cursor.getString(COL_MOVIE_ORIGINAL_TITLE));
                movie.setVoteCount(cursor.getInt(COL_MOVIE_VOTE_COUNT));
                movie.setVoteAverage(cursor.getDouble(COL_MOVIE_VOTE_AVERAGE));
                movie.setPosterImage(cursor.getBlob(COL_MOVIE_POSTER));
                movies.add(movie);
            } while (cursor.moveToNext());
        }

        if(cursor != null) {
            cursor.close();
        }

        return movies;
    }

    @Override
    protected void onPreExecute() {
        mUi.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<MovieItem> movies) {
        mUi.onPostExecute(movies);
    }

    public interface FetchDbCallback {
        void onPreExecute();

        void onPostExecute(List<MovieItem> movies);
    }
}
