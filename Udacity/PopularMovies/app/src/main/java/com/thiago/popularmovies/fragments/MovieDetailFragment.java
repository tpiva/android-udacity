package com.thiago.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thiago.popularmovies.R;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.dto.Movie;

/**
 * Created by tmagalhaes on 06-Jan-17.
 */

public class MovieDetailFragment extends Fragment {

    private static final String URL_LOAD_IMAGE = "http://image.tmdb.org/t/p/w185/";
    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mYearView;
    private TextView mDurationView;
    private TextView mUserRatingView;
    private TextView mSynopsis;

    public MovieDetailFragment(){
        //setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        Movie currentMovie = null;
        if(arguments != null) {
            currentMovie = arguments.getParcelable(MovieGridFragment.DETAIL_MOVIE);
        }

        mTitleView = (TextView) view.findViewById(R.id.detail_movie_item_title);
        mPosterView = (ImageView) view.findViewById(R.id.detail_movie_item_poster);
        mYearView = (TextView) view.findViewById(R.id.detail_movie_item_year);
        mDurationView = (TextView) view.findViewById(R.id.detail_movie_item_duration);
        mUserRatingView = (TextView) view.findViewById(R.id.detail_movie_item_user_rating);
        mSynopsis = (TextView) view.findViewById(R.id.detail_movie_item_synopsis);

        fillMovieDetails(currentMovie);

        return view;
    }

    private void fillMovieDetails(Movie movie) {
        mTitleView.setText(movie.getOriginalTitle());

        Picasso.with(getContext()).load(URL_LOAD_IMAGE + movie.getPosterPath()).fit().into(mPosterView);

        //String year = Utility.getYearOfReleaseDate(movie.getReleaseDate());
        //mYearView.setText(year);

        String formatUserRating = String.format(getActivity().getString(R.string.format_user_rating),movie.getVoteAverage(), movie.getVoteCount());
        mUserRatingView.setText(formatUserRating);

        mUserRatingView.setText(String.valueOf(movie.getVoteAverage()));
        mSynopsis.setText(movie.getOverview());
    }

}
