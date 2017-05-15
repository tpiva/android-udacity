/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 * Handler details of movies like title, release date, average vote and synopsis.
 */

package com.popmovies.android.popmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.popmovies.android.popmovies.adapters.MovieAdapter;
import com.popmovies.android.popmovies.adapters.TrailerAdapter;
import com.popmovies.android.popmovies.bo.Movie;
import com.popmovies.android.popmovies.bo.Trailer;
import com.popmovies.android.popmovies.webservice.FetchTrailerReview;
import com.popmovies.android.popmovies.webservice.RequestMovies;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailMovieActivity extends AppCompatActivity implements
        FetchTrailerReview.TrailerReviewTaskCallback{

    // TODO change build.enabled to avoid findView...

    public static final String EXTRA_MOVIE ="extra_movie";

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mYearReleaseTextView;
    private TextView mRatingTextView;
    private TextView mSynopsisTextView;
    private ProgressDialog mProgressDialog;

    private RecyclerView mMovieTrailerRv;
    private TextView mTrailerTitleTextView;

    private Movie mCurrentMovie;

    private TrailerAdapter mTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent movieIntentReceive = getIntent();

        if (movieIntentReceive.hasExtra(EXTRA_MOVIE)) {
            mCurrentMovie = movieIntentReceive.getParcelableExtra(EXTRA_MOVIE);
        }

        mTitleTextView = (TextView) findViewById(R.id.tv_detail_movie_item_title);
        mPosterImageView = (ImageView) findViewById(R.id.img_detail_movie_item_poster);
        mYearReleaseTextView = (TextView) findViewById(R.id.tv_detail_movie_item_year);
        mRatingTextView = (TextView) findViewById(R.id.tv_detail_movie_item_user_rating);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_detail_movie_item_synopsis);
        mMovieTrailerRv = (RecyclerView) findViewById(R.id.rc_detail_movie_trailers);
        mTrailerTitleTextView = (TextView)findViewById(R.id.tv_detail_title_trailers);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMovieTrailerRv.setLayoutManager(layoutManager);
        mMovieTrailerRv.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter();
        mMovieTrailerRv.setAdapter(mTrailerAdapter);

        loadDataOnViews();
        loadMovieTrailersAndReviews();
    }

    /**
     * Loads on view components details movie information.
     */
    private void loadDataOnViews() {
        if (mCurrentMovie != null) {
            mTitleTextView.setText(mCurrentMovie.getTitle());
            mYearReleaseTextView.setText(getResources().getString(R.string.format_date_released,
                    Utility.getFormatDateAsString(mCurrentMovie.getReleaseDate())));
            mRatingTextView.setText(getResources().getString(R.string.format_user_rating, mCurrentMovie.getVoteAverage()));
            Picasso.with(this).load(MovieAdapter.URL_LOAD_IMAGE + mCurrentMovie.getPosterPath()).fit().into(mPosterImageView);
            mSynopsisTextView.setText(mCurrentMovie.getOverview());
        }
    }

    private void loadMovieTrailersAndReviews() {
        if (Utility.isOnline(this)) {
            // starts communication with server.
            RequestMovies.requestTrailerOrReview(this, mCurrentMovie, this);
        }
    }

    private void fillTrailerAndReview() {
        // TODO handler trailers
        List<Trailer> trailers = mCurrentMovie.getTrailers();
        if (mCurrentMovie.getTrailers() != null &&
                !mCurrentMovie.getTrailers().isEmpty()) {
            mTrailerTitleTextView.setVisibility(View.VISIBLE);
            mMovieTrailerRv.setVisibility(View.VISIBLE);
            mTrailerAdapter.setTrailers(trailers);
        } else {
            // set invisible title
            mTrailerTitleTextView.setVisibility(View.GONE);
            mMovieTrailerRv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPreExecute() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }

        mProgressDialog.setMessage(getString(R.string.fetch_movies_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void onPostExecute(Void aVoid) {
        mProgressDialog.dismiss();
        fillTrailerAndReview();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
