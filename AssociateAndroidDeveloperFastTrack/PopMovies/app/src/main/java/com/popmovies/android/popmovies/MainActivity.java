package com.popmovies.android.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.popmovies.android.popmovies.adapters.MovieAdapter;
import com.popmovies.android.popmovies.bo.Movie;
import com.popmovies.android.popmovies.webservice.FetchMovies;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FetchMovies.MovieTaskCallback, MovieAdapter.OnItemClickListener {

    private RecyclerView mMoviesGridRecycleView;
    private ProgressBar mLoadingProgressBar;
    private MovieAdapter mAdapter;

    private int pageCount = 1;
    private boolean isFetching = false;

    private List<Movie> mCurrentMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGridRecycleView = (RecyclerView) findViewById(R.id.rc_grid_movies);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading_movies);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mMoviesGridRecycleView.setLayoutManager(gridLayoutManager);

        mAdapter = new MovieAdapter(this);

        mMoviesGridRecycleView.setAdapter(mAdapter);

        mMoviesGridRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    int visibleItens = gridLayoutManager.getChildCount();
                    int totalItens = gridLayoutManager.getItemCount();
                    int firstVisible = gridLayoutManager.findFirstVisibleItemPosition();

                    if ((firstVisible + visibleItens) >= totalItens && !isFetching) {
                        pageCount++;
                        fecthMovies();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(mAdapter != null) {
//            // clean
//            mAdapter = null;
//            mAdapter = new MovieAdapter();
//            pageCount = 1;
//        }

        fecthMovies();
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
            mAdapter.setMovieList(mCurrentMovies);
        }
    }

    private void showProgress() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    private void fecthMovies() {
        FetchMovies fetchMovies = new FetchMovies(this, this);
        fetchMovies.execute(String.valueOf(pageCount));
    }

    @Override
    public void onClicked(Movie movie) {
        Class targetClass = DetailMovieActivity.class;
        Intent detailIntent = new Intent(this, targetClass);
        detailIntent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie);
        startActivity(detailIntent);
    }
}
