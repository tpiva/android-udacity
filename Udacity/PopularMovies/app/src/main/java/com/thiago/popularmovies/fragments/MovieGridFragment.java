package com.thiago.popularmovies.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.thiago.popularmovies.MovieAdapter;
import com.thiago.popularmovies.R;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.data.FetchDB;
import com.thiago.popularmovies.dto.MovieItem;
import com.thiago.popularmovies.webservice.FetchMovies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmagalhaes on 04-Jan-17.
 */

public class MovieGridFragment extends Fragment implements FetchMovies.MovieTaskCallback, FetchDB.FetchDbCallback {

    private static final String LOG = MovieGridFragment.class.getSimpleName();

    private static final String SEARCH_POPULAR = "popular";
    private static final String SEARCH_TOP_RATED = "top_rated";
    static final String SEARCH_FAVORITES = "favorites";

    public static final String DETAIL_MOVIE = "detail_movie";

    private String mLastSearch;
    private int mCurrentPage = 1;
    private static boolean mFetching = false;
    private boolean misFirstTime = false;
    private boolean misTwoPane = false;

    private MovieAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    private List<MovieItem> mMovies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView movieGrid = (GridView) rootView.findViewById(R.id.movies_grid);

        if (savedInstanceState != null && mAdapter != null) {
            mAdapter.clear();
        }

        if (mMovies == null) {
            mMovies = new ArrayList<>();
        }

        mAdapter = new MovieAdapter(getActivity(), mMovies);
        movieGrid.setAdapter(mAdapter);

        movieGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!SEARCH_FAVORITES.equalsIgnoreCase(mLastSearch)
                        && !mFetching
                        && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                    updateMovieList();
                    mCurrentPage++;
                }
            }
        });

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieItem movie = (MovieItem) adapterView.getItemAtPosition(position);
                ((Callback) getActivity())
                        .onItemSelected(movie);
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        misFirstTime = true;
        updateMovieList();
    }

    @Override
    public void onStop() {
        super.onStop();
        // clear instance variables and adapter
        if(mAdapter != null) {
            mAdapter.clear();
        }

        if(mMovies != null) {
            mMovies.clear();
        }

        mFetching = false;
        mCurrentPage = 1;
    }

    public void changedSearchOrder() {
        misFirstTime = true;
        updateMovieList();
    }

    private void updateMovieList() {
        Log.i(LOG, "mCurrentPage " + mCurrentPage);
        if(mAdapter != null) {
            // get actual search
            String lastSearch = getLastSearch();
            if(!lastSearch.equals(mLastSearch)) {
                // clear adapter
                mAdapter.clear();
                mLastSearch = lastSearch;
                mCurrentPage = 1;
            }

            if(!mFetching) {
                mFetching = true;
                if(SEARCH_FAVORITES.equalsIgnoreCase(mLastSearch)) {
                    // load from db
                    FetchDB fetchDB = new FetchDB(getActivity(), this);
                    fetchDB.execute();
                } else {
                    FetchMovies fetchMovies = new FetchMovies(getActivity(), this);
                    fetchMovies.execute(String.valueOf(mCurrentPage));
                }
            }
        }
    }

    private String getLastSearch() {
        String lastSearchPref = Utility.getSortOrder(getActivity());
        if (SEARCH_FAVORITES.equalsIgnoreCase(lastSearchPref)) {
            return  SEARCH_FAVORITES;
        } else if (SEARCH_POPULAR.equalsIgnoreCase(lastSearchPref)) {
            return SEARCH_POPULAR;
        } else {
            return SEARCH_TOP_RATED;
        }
    }

    @Override
    public void onPreExecute() {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(getActivity().getString(R.string.fetch_movies_loading));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    @Override
    public void onPostExecute(List<MovieItem> movies) {
        mProgressDialog.dismiss();
        if(mFetching && mMovies != null) {
            mMovies = movies;
            mAdapter.addAll(mMovies);
            mAdapter.notifyDataSetChanged();
            mFetching = false;

            if(misFirstTime && misTwoPane) {
                ((Callback) getActivity())
                        .onItemSelected(mMovies.get(0));
                misFirstTime = false;
            }
        }
    }

    public void setMisTwoPane(boolean misTwoPane) {
        this.misTwoPane = misTwoPane;
    }

    public interface Callback {
        void onItemSelected(MovieItem movie);
    }
}
