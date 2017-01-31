package com.thiago.popularmovies.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.thiago.popularmovies.R;
import com.thiago.popularmovies.TrailerMovieAdapter;
import com.thiago.popularmovies.Utility;
import com.thiago.popularmovies.data.MovieContract;
import com.thiago.popularmovies.dto.MovieItem;
import com.thiago.popularmovies.dto.ReviewItem;
import com.thiago.popularmovies.dto.TrailerItem;
import com.thiago.popularmovies.webservice.FetchReviews;
import com.thiago.popularmovies.webservice.FetchTrailers;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tmagalhaes on 06-Jan-17.
 */

public class MovieDetailFragment extends Fragment implements FetchReviews.FetchReviewsCallback, FetchTrailers.FetchTrailerCallback{

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
    private TextView mVideoTitle;
    private View mViewSecondLine;

    private ProgressDialog mProgressDialog;

    private ArrayList<TrailerItem> mTrailerItens;
    private TrailerMovieAdapter mTrailerAdapter;

    private MovieItem currentMovie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        currentMovie = null;
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
        mVideoTitle = (TextView) view.findViewById(R.id.detail_title_videos);
        mViewSecondLine = view.findViewById(R.id.detail_second_line);

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
        mTrailerAdapter = new TrailerMovieAdapter(getActivity(), mTrailerItens);
        mRecyclerView.setAdapter(mTrailerAdapter);

        if(currentMovie != null &&
                ((MovieGridFragment.SEARCH_FAVORITES.equalsIgnoreCase(Utility.getSortOrder(getActivity())))
                || currentMovie.isMarkAsFavorite())) {
            // set checked toogle button
            mFavoriteTg.setChecked(true);
        }

        mFavoriteTg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    currentMovie.setMarkAsFavorite(true);
                        new HelpFavorites().execute(1);
                } else {
                    //delete
                    getContext().getContentResolver()
                            .delete(MovieContract.buildMovieIdUri(currentMovie.getId()),
                                    MovieContract.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(currentMovie.getId())});
                }
            }
        });

        if(currentMovie != null) {
            fillMovieDetails();
            foundReviewsAndVideos();
        }

        return view;
    }

    private void fillMovieDetails() {
        mTitleView.setText(currentMovie.getOriginalTitle());

        if(Utility.getSortOrder(getActivity()).equalsIgnoreCase(MovieGridFragment.SEARCH_FAVORITES)) {
            if(currentMovie.getPosterImage() != null) {
                mPosterView.setImageBitmap(
                        BitmapFactory.decodeByteArray(currentMovie.getPosterImage(), 0, currentMovie.getPosterImage().length));
            }
        } else {
            Picasso.with(getContext()).load(URL_LOAD_IMAGE + currentMovie.getPosterPath()).into(mPosterView);
        }

        String year = Utility.getYearOfReleaseDate(currentMovie.getReleaseDate());
        mYearView.setText(year);

        String formatUserRating = getActivity().getString((R.string.format_user_rating),currentMovie.getVoteAverage());
        mUserRatingView.setText(formatUserRating);

        mUserRatingView.setText(String.valueOf(currentMovie.getVoteAverage()));
        mSynopsis.setText(currentMovie.getOverview());
    }

    private void foundReviewsAndVideos() {
        if(currentMovie != null && Utility.isOnline(getActivity())) {
            // check if view and title is invisible
            if(!mVideoTitle.isShown() && !mViewSecondLine.isShown()) {
                mViewSecondLine.setVisibility(View.VISIBLE);
                mVideoTitle.setVisibility(View.VISIBLE);
            }
            new FetchTrailers(mTrailerAdapter, this).execute(currentMovie.getId());
            new FetchReviews(this).execute(currentMovie.getId(), 1);
        } else {
            // remove second view and text of trailers
            mVideoTitle.setVisibility(View.INVISIBLE);
            mViewSecondLine.setVisibility(View.INVISIBLE);
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
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getActivity().getString(R.string.fetch_movies_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void onPostExecute(ArrayList<ReviewItem> reviewItems) {
        mProgressDialog.dismiss();
        fillReviews(reviewItems);
    }

    @Override
    public void onFetchCompleted(ArrayList<TrailerItem> trailerItems) {
        if(trailerItems == null || (trailerItems != null && trailerItems.isEmpty())) {
            if(mViewSecondLine.isShown() && mVideoTitle.isShown()) {
                mVideoTitle.setVisibility(View.INVISIBLE);
                mViewSecondLine.setVisibility(View.INVISIBLE);
            }
        }
    }


    private class HelpFavorites extends AsyncTask<Integer, Void, Void> {

        private final String TAG = HelpFavorites.class.getName();

        @Override
        protected Void doInBackground(Integer... params) {
            if(params[0] == 1) {
                // insert
                getContext().getContentResolver()
                        .insert(MovieContract.CONTENT_URI, createContentValues(currentMovie));
            }

            return null;
        }

        private ContentValues createContentValues(MovieItem movie) {
            Bitmap bitmap = null;
            try {
                bitmap = Picasso.with(getContext()).load(URL_LOAD_IMAGE + movie.getPosterPath()).get();
            } catch (IOException e) {
                Log.e(TAG,"IOException",e);
            }

            ContentValues values = new ContentValues();
            values.put(MovieContract.COLUMN_OVERVIEW, movie.getOverview());
            values.put(MovieContract.COLUMN_RELEASE_DATE, Utility.getStringOfDate(movie.getReleaseDate()));
            values.put(MovieContract.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
            values.put(MovieContract.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            values.put(MovieContract.COLUMN_VOTE_COUNT, movie.getVoteCount());
            values.put(MovieContract.COLUMN_POSTER, Utility.getByteFromBitmap(bitmap) );
            values.put(MovieContract.COLUMN_MOVIE_ID, movie.getId());

            return values;
        }
    }
}
