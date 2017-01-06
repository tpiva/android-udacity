package com.thiago.popularmovies.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.thiago.popularmovies.MovieAdapter;
import com.thiago.popularmovies.R;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.dto.Movie;
import com.thiago.popularmovies.webservice.FetchMovies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmagalhaes on 04-Jan-17.
 */

public class MovieGridFragment extends Fragment implements FetchMovies.MovieTaskCallback {

    private static final String SEARCH_POPULAR = "popular";
    private static final String SEARCH_TOP_RATED = "top_rated";
    private static final String LAST_SEARCH = "last_search";
    private static final String LAST_PAGE = "last_page";

    private static String mLastSearch;
    private static int mCurrentPage = 1;

    private MovieAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView movieGrid = (GridView) rootView.findViewById(R.id.movies_grid);

        mAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        movieGrid.setAdapter(mAdapter);

        // check if is recreate
        if(savedInstanceState != null) {
            mLastSearch = savedInstanceState.getString(LAST_SEARCH);
            mCurrentPage = savedInstanceState.getInt(LAST_PAGE);
        }

        // get last search, popular or to rated
        mLastSearch = getLastSearch();

        movieGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("Teste", "firstVisibleItem " + firstVisibleItem + " visibleItemCount " + visibleItemCount + " totalItemCount " + totalItemCount);
                if(firstVisibleItem + visibleItemCount == totalItemCount) {
                    mCurrentPage++;
                    updateMovieList();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAST_PAGE, mCurrentPage);
        outState.putString(LAST_SEARCH,mLastSearch);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    private void updateMovieList() {
        if(mAdapter != null) {
            // get actual search
            String lastSearch = getLastSearch();
            if(!lastSearch.equals(mLastSearch)) {
                // clear adapter
                mAdapter.clear();
                mLastSearch = lastSearch;
                mCurrentPage = 1;
            }

            FetchMovies fetchMovies = new FetchMovies(getActivity(), this);
            fetchMovies.execute(String.valueOf(mCurrentPage));
        }
    }

    private String getLastSearch() {
        return (Utility.isPopular(getActivity())) ? SEARCH_POPULAR : SEARCH_TOP_RATED;
    }

    @Override
    public void onPreExecute() {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(getActivity().getString(R.string.fetch_movies_loading));
            mProgressDialog.show();
        }
    }

    @Override
    public void onPostExecute(List<Movie> movies) {
        mProgressDialog.dismiss();
        mAdapter.addAll(movies);
        mAdapter.notifyDataSetChanged();
    }
}
