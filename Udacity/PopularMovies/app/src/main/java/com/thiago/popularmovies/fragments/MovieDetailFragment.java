package com.thiago.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.thiago.popularmovies.R;
import com.thiago.popularmovies.TrailerMovieAdapter;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.dto.Movie;
import com.thiago.popularmovies.dto.ReviewItem;
import com.thiago.popularmovies.dto.TrailerItem;
import com.thiago.popularmovies.webservice.FetchReviews;
import com.thiago.popularmovies.webservice.FetchTrailers;

import java.util.ArrayList;

/**
 * Created by tmagalhaes on 06-Jan-17.
 */

public class MovieDetailFragment extends Fragment implements FetchReviews.FetchReviewsCallback{

    private static final String URL_LOAD_IMAGE = "http://image.tmdb.org/t/p/w185/";
    private TextView mTitleView;
    private ImageView mPosterView;
    private TextView mYearView;
    private TextView mUserRatingView;
    private TextView mSynopsis;
    private ToggleButton mFavoriteTg;
    private RecyclerView mRecyclerView;
    private LinearLayout mReviewLayout;
    private TextView mReviewTitle;

    private ArrayList<TrailerItem> mTrailerItens;
    private TrailerMovieAdapter mTrailerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        Movie currentMovie = null;
        if(arguments != null) {
            currentMovie = arguments.getParcelable(MovieGridFragment.DETAIL_MOVIE);
        }

        mTitleView = (TextView) view.findViewById(R.id.detail_movie_item_title);
        mPosterView = (ImageView) view.findViewById(R.id.detail_movie_item_poster);
        mYearView = (TextView) view.findViewById(R.id.detail_movie_item_year);
        mUserRatingView = (TextView) view.findViewById(R.id.detail_movie_item_user_rating);
        mSynopsis = (TextView) view.findViewById(R.id.detail_movie_item_synopsis);
        mFavoriteTg = (ToggleButton) view.findViewById(R.id.detail_button_favorites);
        mReviewLayout = (LinearLayout) view.findViewById(R.id.detail_movie_view_reviews);
        mReviewTitle = (TextView) view.findViewById(R.id.detail_title_reviews);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.detail_movie_item_recycle_view_trailers);

        // set params of RecyclerView
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        mRecyclerView.setLayoutManager(layoutManager);

        // instance of review list and adapter
        mTrailerItens = new ArrayList<>();
        mTrailerAdapter = new TrailerMovieAdapter(mTrailerItens);
        mRecyclerView.setAdapter(mTrailerAdapter);

        fillMovieDetails(currentMovie);
        foundReviewsAndVideos(currentMovie);

        return view;
    }

    private void fillMovieDetails(Movie movie) {
        mTitleView.setText(movie.getOriginalTitle());

        Picasso.with(getContext()).load(URL_LOAD_IMAGE + movie.getPosterPath()).into(mPosterView);

        String year = Utility.getYearOfReleaseDate(movie.getReleaseDate());
        mYearView.setText(year);

        String formatUserRating = getActivity().getString((R.string.format_user_rating),movie.getVoteAverage(), String.valueOf(movie.getVoteCount()));
        mUserRatingView.setText(formatUserRating);

        mUserRatingView.setText(String.valueOf(movie.getVoteAverage()));
        mSynopsis.setText(movie.getOverview());
    }

    private void foundReviewsAndVideos(Movie currentMovie) {
        if(currentMovie != null) {
            new FetchTrailers(mTrailerAdapter).execute(currentMovie.getId());
            new FetchReviews(this).execute(currentMovie.getId(), 1);
        }
    }

    private void fillReviews(ArrayList<ReviewItem> reviewItems) {
        if(mReviewLayout != null && (reviewItems != null && !reviewItems.isEmpty())) {
            mReviewTitle.setVisibility(View.VISIBLE);

            for(ReviewItem item : reviewItems) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_movie_review, null);
                TextView contentTextView = (TextView) view.findViewById(R.id.list_item_movie_review_content);
                TextView authorTextView = (TextView) view.findViewById(R.id.list_item_movie_review_author);
                TextView linkTextView = (TextView) view.findViewById(R.id.list_item_movie_review_link);

                contentTextView.setText(item.getContent().trim());
                authorTextView.setText(getActivity().getString(R.string.format_review_author,item.getAuthor()));
                linkTextView.setText(getActivity().getString(R.string.format_review_link,item.getUrl()));

                if(view != null) {
                    mReviewLayout.addView(view);
                }
            }
        } else {
            mReviewTitle.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPreExecute() {
        //TODO progress dialog
    }

    @Override
    public void onPostExecute(ArrayList<ReviewItem> reviewItems) {
        fillReviews(reviewItems);
    }
}
