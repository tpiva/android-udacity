/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 */

package com.popmovies.android.popmovies.adapters;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.popmovies.android.popmovies.R;
import com.popmovies.android.popmovies.bo.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter class for put data and draw UI of RecycleView.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    public static final String URL_LOAD_IMAGE = "http://image.tmdb.org/t/p/w185/";

    private List<Movie> mMovieList;
    private List<Integer> mFavoriteMovieIds;

    private final OnItemClickListener mListener;

    public MovieAdapter(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return (new MovieViewHolder(view));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(mMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mMovieList == null) {
            return 0;
        }

        return mMovieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView moviePoster;

        public MovieViewHolder(final View itemView) {
            super(itemView);

            moviePoster = (ImageView) itemView.findViewById(R.id.imv_movie_image);
            moviePoster.setOnClickListener(this);
        }

        /**
         * Bind content (movie) with view.
         * @param movie
         */
        public void bind(Movie movie) {
            if(movie.getPosterPath() == null
                    || (movie.getPosterPath() != null && "".equalsIgnoreCase(movie.getPosterPath()))) {
                if (movie.getPosterImage() != null) {
                    byte[] imageAsByte = movie.getPosterImage();
                    moviePoster.setImageBitmap(BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length));
                } else {
                    moviePoster.setImageResource(R.drawable.ic_favorite);
                }
            } else {
                Picasso.with(itemView.getContext()).load(URL_LOAD_IMAGE + movie.getPosterPath()).fit().into(moviePoster);
            }
            moviePoster.setContentDescription(movie.getTitle());

            // set movies as favorite or not
            if (mFavoriteMovieIds != null
                    && mFavoriteMovieIds.contains(movie.getId())) {
                movie.setMarkAsFavorite(true);
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onClicked(mMovieList.get(getAdapterPosition()));
        }
    }

    /**
     * Fill list of movies to display on RecycleView and favorites movies.
     * @param mMovieList
     */
    public void setmMovieListAndFavorites(List<Movie> mMovieList, List<Integer> mFavoriteMovieIds) {
        this.mMovieList = mMovieList;
        this.mFavoriteMovieIds = mFavoriteMovieIds;
        notifyDataSetChanged();
    }

    /**
     * Interface to handler click events on Movie Grid, implemented by MainActivity.
     */
    public interface OnItemClickListener {
        void onClicked(Movie movie);
    }
}
