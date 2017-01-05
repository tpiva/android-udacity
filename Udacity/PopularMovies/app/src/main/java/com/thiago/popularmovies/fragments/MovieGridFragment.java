package com.thiago.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.thiago.popularmovies.MovieAdapter;
import com.thiago.popularmovies.R;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.dto.Movie;
import com.thiago.popularmovies.webservice.FetchMovies;

import java.util.ArrayList;

/**
 * Created by tmagalhaes on 04-Jan-17.
 */

public class MovieGridFragment extends Fragment {

    private static final String SEARCH_POPULAR = "popular";
    private static final String SEARCH_TOP_RATED = "toprated";

    private static String mLastSearch;

    private MovieAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView movieGrid = (GridView) rootView.findViewById(R.id.movies_grid);

        mAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        movieGrid.setAdapter(mAdapter);

        // get last search, popular or to rated
        mLastSearch = getLastSearch();

        return rootView;
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
            }

            FetchMovies fetchMovies = new FetchMovies(getActivity(), mAdapter);
            fetchMovies.execute("1");
        }
    }

    private String getLastSearch() {
        return (Utility.isPopular(getActivity())) ? SEARCH_POPULAR : SEARCH_TOP_RATED;
    }
}
