/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 * Main activity to show grid with movies, start communication with server to get movies and
 * handler menu actions.
 */

package com.popmovies.android.popmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popmovies.android.popmovies.adapters.MovieAdapter;
import com.popmovies.android.popmovies.bo.Movie;
import com.popmovies.android.popmovies.data.PopMoviesPreferences;
import com.popmovies.android.popmovies.databinding.ActivityMainBinding;
import com.popmovies.android.popmovies.db.PopMoviesContract;
import com.popmovies.android.popmovies.webservice.RequestMovies;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<Movie>>{

    // COMPLETED fix saveInstance
    // TODO fix error of duplicated favorite during back key
    private static final int POP_MOVIES_LOADER_ID = 120;

    private static final String SEARCH_CHANGED = "search_changed";
    private static final String CURRENT_LIST = "current_list";
    private static final String CURRENT_SEARCH = "current_search";
    private static final String CURRENT_PAGE = "current_page";
    private static final String CURRENT_POSITION_RV = "current_position_rv";

    public static final int NUMBER_COLUMNS_LANDSCAPE = 3;
    public static final int NUMBER_COLUMNS_PORTRAIT = 2;

    public static final String SEARCH_TYPE_POPULAR = "popular";
    private static final String SEARCH_TYPE_FAVORITES = "favorites";

    private GridLayoutManager mGridLayoutManager;

    private MovieAdapter mAdapter;

    private int mActualPage = 1;
    private boolean isFetching = false;
    private boolean isRestored = false;

    private ArrayList<Movie> mCurrentMovies = new ArrayList<>();

    private static String sCurrentSearchType = "";

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // check orientation to define number of columns on grid
        int numberColumns = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                NUMBER_COLUMNS_LANDSCAPE : NUMBER_COLUMNS_PORTRAIT;

        mGridLayoutManager = new GridLayoutManager(this, numberColumns);

        mBinding.rcGridMovies.setLayoutManager(mGridLayoutManager);

        if (!Utility.isOnline(this)) {
            showMessageError();
        } else {
            if (savedInstanceState != null) {
                mCurrentMovies = savedInstanceState.getParcelableArrayList(CURRENT_LIST);
                mAdapter.setmMovieList(mCurrentMovies);
                mBinding.rcGridMovies.getLayoutManager()
                        .scrollToPosition(savedInstanceState.getInt(CURRENT_POSITION_RV));
                isRestored = true;
            } else {
                mAdapter = new MovieAdapter(this);
            }
        }

        mBinding.rcGridMovies.setAdapter(mAdapter);

        // after end of recycle view load more movies from server side.
        mBinding.rcGridMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItens = mGridLayoutManager.getChildCount();
                    int totalItens = mGridLayoutManager.getItemCount();
                    int firstVisible = mGridLayoutManager.findFirstVisibleItemPosition();

                    if (!SEARCH_TYPE_FAVORITES.equals(sCurrentSearchType)
                        && (firstVisible + visibleItens) >= totalItens && !isFetching) {
                        mActualPage++;
                        getSupportLoaderManager().restartLoader(POP_MOVIES_LOADER_ID, null, MainActivity.this);
                    }
                }
            }
        });

    }

    /**
     * Show progressBar to user to inform more movies are loading.
     */
    private void showProgress() {
        mBinding.pbLoadingMovies.setVisibility(View.VISIBLE);
        mBinding.tvMessageErrorLoading.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows content of grid (movies) and turn invisible other UI elements.
     */
    private void showContent() {
        if (!mBinding.rcGridMovies.isShown()) {
            mBinding.tvMessageErrorLoading.setVisibility(View.VISIBLE);
        }

        mBinding.pbLoadingMovies.setVisibility(View.INVISIBLE);
        mBinding.tvMessageErrorLoading.setVisibility(View.INVISIBLE);
    }

    /**
     * Display error message during loading movies from server.
     */
    private void showMessageError() {
        mBinding.pbLoadingMovies.setVisibility(View.INVISIBLE);
        mBinding.tvMessageErrorLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClicked(Movie movie) {
        Class targetClass = DetailMovieActivity.class;
        Intent detailIntent = new Intent(this, targetClass);
        detailIntent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie);
        startActivity(detailIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(CURRENT_LIST, mCurrentMovies);
        outState.putString(CURRENT_SEARCH, sCurrentSearchType);
        outState.putInt(CURRENT_PAGE, mActualPage);
        outState.putInt(CURRENT_POSITION_RV, mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentMovies = savedInstanceState.getParcelableArrayList(CURRENT_LIST);
        mAdapter.setmMovieList(mCurrentMovies);
        sCurrentSearchType = savedInstanceState.getString(CURRENT_SEARCH);
        mActualPage = savedInstanceState.getInt(CURRENT_PAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String prefSearchType = PopMoviesPreferences.getSearchType(this);
        if (!isRestored
                && !isFetching
                && !sCurrentSearchType.equals(prefSearchType)) {
            sCurrentSearchType = prefSearchType;
            Bundle bundle = new Bundle();
            bundle.putBoolean(SEARCH_CHANGED, true);
            //restart loader
            getSupportLoaderManager().restartLoader(POP_MOVIES_LOADER_ID, bundle, this);
        } else if (isRestored){
            isRestored = false;
        } else {
            getSupportLoaderManager().initLoader(POP_MOVIES_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {
            final String[] MOVIE_COLUMNS = {
                    PopMoviesContract.COLUMN_OVERVIEW,
                    PopMoviesContract.COLUMN_RELEASE_DATE,
                    PopMoviesContract.COLUMN_ORIGINAL_TITLE,
                    PopMoviesContract.COLUMN_VOTE_AVERAGE,
                    PopMoviesContract.COLUMN_VOTE_COUNT,
                    PopMoviesContract.COLUMN_POSTER,
                    PopMoviesContract.COLUMN_MOVIE_ID
            };

            static final int COL_MOVIE_OVERVIEW = 0;
            static final int COL_MOVIE_RELEASE_DATE = 1;
            static final int COL_MOVIE_ORIGINAL_TITLE = 2;
            static final int COL_MOVIE_VOTE_AVERAGE = 3;
            static final int COL_MOVIE_VOTE_COUNT = 4;
            static final int COL_MOVIE_POSTER = 5;
            static final int COL_MOVIE_MOVIE_ID = 6;

            @Override
            protected void onStartLoading() {
                isFetching = true;
                if (args != null) {
                    if (args.getBoolean(SEARCH_CHANGED)) {
                        // reset data
                        mCurrentMovies.clear();
                        mAdapter.notifyDataSetChanged();
                        mActualPage = 1;
                    }
                }
                showProgress();
                forceLoad();
            }

            @Override
            public List<Movie> loadInBackground() {
                if (SEARCH_TYPE_FAVORITES.equals(sCurrentSearchType)) {
                    List<Movie> movies = new ArrayList<>();
                    Cursor cursor = getContentResolver().query(PopMoviesContract.CONTENT_URI,
                            MOVIE_COLUMNS,
                            null,
                            null,
                            null);
                    if (!cursor.moveToFirst()) {
                        return null;
                    } else {
                        do {
                            Movie movie = new Movie();
                            movie.setId(cursor.getInt(COL_MOVIE_MOVIE_ID));
                            movie.setOverview(cursor.getString(COL_MOVIE_OVERVIEW));
                            movie.setReleaseDate(Utility.getFormatDate(cursor.getString(COL_MOVIE_RELEASE_DATE)));
                            movie.setTitle(cursor.getString(COL_MOVIE_ORIGINAL_TITLE));
                            movie.setVoteCount(cursor.getInt(COL_MOVIE_VOTE_COUNT));
                            movie.setVoteAverage(cursor.getDouble(COL_MOVIE_VOTE_AVERAGE));
                            movie.setPosterImage(cursor.getBlob(COL_MOVIE_POSTER));
                            movie.setMarkAsFavorite(true);
                            movies.add(movie);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    return movies;
                } else {
                    return RequestMovies.requestMovies(getContext(), sCurrentSearchType, mActualPage);
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        isFetching = false;
        if (data == null) {
            showMessageError();
        } else {
            showContent();
            mCurrentMovies.addAll(data);
            mAdapter.setmMovieList(mCurrentMovies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
    }
}
