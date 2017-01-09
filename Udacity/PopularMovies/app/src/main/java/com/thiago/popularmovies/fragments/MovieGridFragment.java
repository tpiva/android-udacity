package com.thiago.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public static final String DETAIL_MOVIE = "detail_movie";

    private String mLastSearch;
    private int mCurrentPage = 1;
    private boolean mIsFetching = false;

    private MovieAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView movieGrid = (GridView) rootView.findViewById(R.id.movies_grid);

        mAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        movieGrid.setAdapter(mAdapter);

        movieGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!mIsFetching && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                    updateMovieList();
                    mCurrentPage++;
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
            mIsFetching = true;
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(getActivity().getString(R.string.fetch_movies_loading));
            mProgressDialog.show();
        }
    }

    @Override
    public void onPostExecute(List<Movie> movies) {
        mIsFetching = false;
        mProgressDialog.dismiss();
        mAdapter.addAll(movies);
        mAdapter.notifyDataSetChanged();
    }
}
