package com.thiago.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.thiago.popularmovies.activities.DetailActivity;
import com.thiago.popularmovies.data.FetchDB;
import com.thiago.popularmovies.dto.Movie;
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
    protected static final String SEARCH_FAVORITES = "favorites";

    public static final String DETAIL_MOVIE = "detail_movie";

    private String mLastSearch;
    private int mCurrentPage = 1;
    private int mOldPage = 0;
    private static boolean mFetching = false;

    private MovieAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    private List<Movie> mMovies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = null;
        if(!Utility.isOnline(getActivity())) {
            rootView = inflater.inflate(R.layout.fragment_main_empty, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            GridView movieGrid = (GridView) rootView.findViewById(R.id.movies_grid);

            if(mMovies == null) {
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
                    if(!SEARCH_FAVORITES.equalsIgnoreCase(mLastSearch) && !mFetching && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                        updateMovieList();
                        mOldPage = mCurrentPage++;
                    }
                }
            });

            movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Movie movie = (Movie)adapterView.getItemAtPosition(position);
                    // put on parcelable
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(DETAIL_MOVIE, movie);
                    startActivity(intent);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
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

            if(!mFetching && mOldPage != mCurrentPage) {
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
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();
        }
    }

    @Override
    public void onPostExecute(List<Movie> movies) {
        mProgressDialog.dismiss();
        if(mFetching && mMovies != null) {
            mMovies = movies;
            mAdapter.addAll(mMovies);
            mAdapter.notifyDataSetChanged();
            mFetching = false;
        }
    }
}
