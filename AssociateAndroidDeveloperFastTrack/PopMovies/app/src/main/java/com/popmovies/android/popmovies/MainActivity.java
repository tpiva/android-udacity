package com.popmovies.android.popmovies;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.popmovies.android.popmovies.webservice.FetchMovies;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FetchMovies.MovieTaskCallback, MovieAdapter.OnItemClickListener {

    private static final String CURRENT_LIST = "current_list";
    private static final String CURRENT_SEARCH = "current_search";
    private static final String CURRENT_PAGE = "current_page";
    private static final String CURRENT_POSITION_RV = "current_position_rv";

    public static final String POPULAR_MOVIE_SEARCH = "popular_movie";
    private static final String TOP_RATED_MOVIE_SEARCH = "top_rated_movie";
    public static final int NUMBER_COLUMNS_LANDSCAPE = 3;
    public static final int NUMBER_COLUMNS_PORTRAIT = 2;

    private GridLayoutManager mGridLayoutManager;
    private ProgressBar mLoadingProgressBar;
    private TextView mMessageLoaginErrorTextView;
    private RecyclerView mMoviesGridRecycleView;

    private MovieAdapter mAdapter;

    private int mPageCount = 1;
    private boolean isFetching = false;

    private ArrayList<Movie> mCurrentMovies = new ArrayList<>();

    private String mCurrentSearchType = POPULAR_MOVIE_SEARCH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGridRecycleView = (RecyclerView) findViewById(R.id.rc_grid_movies);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading_movies);
        mMessageLoaginErrorTextView = (TextView) findViewById(R.id.tv_message_error_loading);

        int orientation = getResources().getConfiguration().orientation;

        mGridLayoutManager = new GridLayoutManager(this,
                orientation == Configuration.ORIENTATION_LANDSCAPE ? NUMBER_COLUMNS_LANDSCAPE : NUMBER_COLUMNS_PORTRAIT);

        mMoviesGridRecycleView.setLayoutManager(mGridLayoutManager);
        mAdapter = new MovieAdapter(this);

        mMoviesGridRecycleView.setAdapter(mAdapter);

        mMoviesGridRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItens = mGridLayoutManager.getChildCount();
                    int totalItens = mGridLayoutManager.getItemCount();
                    int firstVisible = mGridLayoutManager.findFirstVisibleItemPosition();

                    if ((firstVisible + visibleItens) >= totalItens && !isFetching) {
                        mPageCount++;
                        fetchMovies();
                    }
                }
            }
        });

        if(savedInstanceState != null) {
            mCurrentMovies = savedInstanceState.getParcelableArrayList(CURRENT_LIST);
            mAdapter.setmMovieList(mCurrentMovies);

            mMoviesGridRecycleView.getLayoutManager().scrollToPosition(savedInstanceState.getInt(CURRENT_POSITION_RV));
        } else {
            fetchMovies();
        }

    }

    @Override
    public void onPreExecute() {
        isFetching = true;
        showProgress();
    }

    @Override
    public void onPostExecute(List<Movie> movies) {
        isFetching = false;
        showContent();
        if (movies != null) {
            mCurrentMovies.addAll(movies);
            mAdapter.setmMovieList(mCurrentMovies);
        } else {
            showMessageError();
        }
    }

    private void showProgress() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        mMessageLoaginErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void showContent() {
        if (!mMoviesGridRecycleView.isShown()) {
            mMessageLoaginErrorTextView.setVisibility(View.VISIBLE);
        }

        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        mMessageLoaginErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void fetchMovies() {
        FetchMovies fetchMovies = new FetchMovies(this, this);
        fetchMovies.execute(mCurrentSearchType, String.valueOf(mPageCount));
    }

    private void showMessageError() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        mMessageLoaginErrorTextView.setVisibility(View.VISIBLE);
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
        boolean isChangedSearch = false;
        switch (id) {
            case R.id.action_popular:
                mCurrentSearchType = POPULAR_MOVIE_SEARCH;
                isChangedSearch = true;
                break;
            case R.id.action_top_rated:
                mCurrentSearchType = TOP_RATED_MOVIE_SEARCH;
                isChangedSearch = true;
                break;
        }

        if(isChangedSearch) {
            reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        mCurrentMovies.clear();
        mAdapter.notifyDataSetChanged();
        mPageCount = 1;
        fetchMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(CURRENT_LIST, mCurrentMovies);
        outState.putString(CURRENT_SEARCH, mCurrentSearchType);
        outState.putInt(CURRENT_PAGE, mPageCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCurrentSearchType = savedInstanceState.getString(CURRENT_SEARCH);
        mPageCount = savedInstanceState.getInt(CURRENT_PAGE);
    }
}
