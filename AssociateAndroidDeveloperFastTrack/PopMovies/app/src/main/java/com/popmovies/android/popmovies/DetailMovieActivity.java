package com.popmovies.android.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.popmovies.android.popmovies.adapters.MovieAdapter;
import com.popmovies.android.popmovies.bo.Movie;
import com.squareup.picasso.Picasso;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE ="extra_movie";

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mYearReleaseTextView;
    private TextView mRatingTextView;
    private TextView mSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Movie currentMovie = null;

        Intent movieIntentReceive = getIntent();

        if (movieIntentReceive.hasExtra(EXTRA_MOVIE)) {
            currentMovie = movieIntentReceive.getParcelableExtra(EXTRA_MOVIE);
        }

        mTitleTextView = (TextView) findViewById(R.id.tv_detail_movie_item_title);
        mPosterImageView = (ImageView) findViewById(R.id.img_detail_movie_item_poster);
        mYearReleaseTextView = (TextView) findViewById(R.id.tv_detail_movie_item_year);
        mRatingTextView = (TextView) findViewById(R.id.tv_detail_movie_item_user_rating);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_detail_movie_item_synopsis);

        loadDataOnViews(currentMovie);
    }

    private void loadDataOnViews(Movie movie) {
        if (movie != null) {
            mTitleTextView.setText(movie.getTitle());
            mYearReleaseTextView.setText(getResources().getString(R.string.format_date_released, Utility.getYearOfReleaseDate(movie.getReleaseDate())));
            mRatingTextView.setText(getResources().getString(R.string.format_user_rating, movie.getVoteAverage()));
            Picasso.with(this).load(MovieAdapter.URL_LOAD_IMAGE + movie.getPosterPath()).fit().into(mPosterImageView);
            mSynopsisTextView.setText(movie.getOverview());
        }
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
