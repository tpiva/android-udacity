/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 * Handler details of movies like title, release date, average vote and synopsis.
 */

package com.popmovies.android.popmovies;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.popmovies.android.popmovies.adapters.MovieAdapter;
import com.popmovies.android.popmovies.adapters.TrailerAdapter;
import com.popmovies.android.popmovies.bo.Movie;
import com.popmovies.android.popmovies.bo.Review;
import com.popmovies.android.popmovies.bo.Trailer;
import com.popmovies.android.popmovies.db.PopMoviesContract;
import com.popmovies.android.popmovies.webservice.FetchTrailerReview;
import com.popmovies.android.popmovies.webservice.RequestMovies;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import static com.popmovies.android.popmovies.adapters.MovieAdapter.URL_LOAD_IMAGE;

public class DetailMovieActivity extends AppCompatActivity implements
        FetchTrailerReview.TrailerReviewTaskCallback{

    // TODO change build.enabled to avoid findView...

    public static final String EXTRA_MOVIE ="extra_movie";

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mYearReleaseTextView;
    private TextView mRatingTextView;
    private TextView mSynopsisTextView;
    private ToggleButton mFavoriteTg;
    private ProgressDialog mProgressDialog;

    private RecyclerView mMovieTrailerRv;
    private TextView mTrailerTitleTextView;

    private LinearLayout mMovieReviewLl;
    private View mDividerTrailerReview;
    private TextView mReviewTitleTextView;

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
        mMovieReviewLl = (LinearLayout) findViewById(R.id.detail_movie_reviews_ln);
        mDividerTrailerReview = findViewById(R.id.divider_trailers_reviews) ;
        mReviewTitleTextView = (TextView) findViewById(R.id.detail_movie_review_title);
        mFavoriteTg = (ToggleButton) findViewById(R.id.detail_button_favorites);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMovieTrailerRv.setLayoutManager(layoutManager);
        mMovieTrailerRv.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this);
        mMovieTrailerRv.setAdapter(mTrailerAdapter);

        if (mCurrentMovie != null) {
            mFavoriteTg.setChecked(mCurrentMovie.isMarkAsFavorite());
        }

        mFavoriteTg.setClickable(true);
        mFavoriteTg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mCurrentMovie.setMarkAsFavorite(true);
                    new HelpFavorites().execute();
                } else {
                    mCurrentMovie.setMarkAsFavorite(false);
                    getContentResolver().
                            delete(PopMoviesContract.buildUriWithId(mCurrentMovie.getId()), null, null);
                }
            }
        });

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
            if(mCurrentMovie.getPosterPath() == null
                    || (mCurrentMovie.getPosterPath() != null && "".equalsIgnoreCase(mCurrentMovie.getPosterPath()))) {
                if (mCurrentMovie.getPosterImage() != null) {
                    byte[] imageAsByte = mCurrentMovie.getPosterImage();
                    mPosterImageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length));
                }
            } else {
                Picasso.with(this).load(URL_LOAD_IMAGE + mCurrentMovie.getPosterPath()).fit().into(mPosterImageView);
            }
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
        // COMPLETED handler trailers
        List<Trailer> trailers = mCurrentMovie.getTrailers();
        List<Review> reviews = mCurrentMovie.getReviews();
        if (trailers != null && !trailers.isEmpty()) {
            mTrailerTitleTextView.setVisibility(View.VISIBLE);
            mMovieTrailerRv.setVisibility(View.VISIBLE);
            mTrailerAdapter.setTrailers(trailers);
        } else {
            // set invisible title
            mTrailerTitleTextView.setVisibility(View.GONE);
            mMovieTrailerRv.setVisibility(View.GONE);
        }

        if (reviews != null && !reviews.isEmpty()) {
            if (mMovieReviewLl != null) {
                mMovieReviewLl.setVisibility(View.VISIBLE);
                mReviewTitleTextView.setVisibility(View.VISIBLE);
                mDividerTrailerReview.setVisibility(View.VISIBLE);

                for(Review item : reviews) {

                    View view = LayoutInflater.from(this).inflate(R.layout.movie_review_item, null);
                    TextView contentTextView = (TextView) view.findViewById(R.id.movie_review_item_content);
                    TextView authorTextView = (TextView) view.findViewById(R.id.movie_review_item_author);
                    TextView linkTextView = (TextView) view.findViewById(R.id.movie_review_item_link);
                    contentTextView.setText(item.getContent().trim());
                    authorTextView.setText(item.getAuthor());
                    linkTextView.setText(item.getUrl());
                    if(view != null) {
                        mMovieReviewLl.addView(view);
                    }
                }
            }
        } else {
            mDividerTrailerReview.setVisibility(View.GONE);
            if (mMovieReviewLl != null) {
                mMovieReviewLl.setVisibility(View.GONE);
                mReviewTitleTextView.setVisibility(View.GONE);
            }
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

    private class HelpFavorites extends AsyncTask<Void, Void, Void> {

        private final String TAG = HelpFavorites.class.getName();

        @Override
        protected Void doInBackground(Void... params) {
            getContentResolver()
                    .insert(PopMoviesContract.CONTENT_URI, createContentValues());

            return null;
        }

        private ContentValues createContentValues() {
            Bitmap bitmap = null;
            try {
                bitmap = Picasso.with(DetailMovieActivity.this).load(URL_LOAD_IMAGE + mCurrentMovie.getPosterPath()).get();
            } catch (IOException e) {
                Log.e(TAG,"IOException",e);
            }

            ContentValues values = new ContentValues();
            values.put(PopMoviesContract.COLUMN_OVERVIEW, mCurrentMovie.getOverview());
            values.put(PopMoviesContract.COLUMN_RELEASE_DATE, Utility.
                    getFormatDateAsString(mCurrentMovie.getReleaseDate()));
            values.put(PopMoviesContract.COLUMN_ORIGINAL_TITLE, mCurrentMovie.getOriginalTitle());
            values.put(PopMoviesContract.COLUMN_VOTE_AVERAGE, mCurrentMovie.getVoteAverage());
            values.put(PopMoviesContract.COLUMN_VOTE_COUNT, mCurrentMovie.getVoteCount());
            values.put(PopMoviesContract.COLUMN_POSTER, Utility.getByteFromBitmap(bitmap) );
            values.put(PopMoviesContract.COLUMN_MOVIE_ID, mCurrentMovie.getId());

            return values;
        }
    }
}
