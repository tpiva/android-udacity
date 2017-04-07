package com.popmovies.android.popmovies.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.popmovies.android.popmovies.DetailMovieActivity;
import com.popmovies.android.popmovies.R;
import com.popmovies.android.popmovies.bo.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tmagalhaes on 07/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    public static final String URL_LOAD_IMAGE = "http://image.tmdb.org/t/p/w185/";
    private List<Movie> movieList;

    private OnItemClickListener mListener;

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
        holder.bind(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        if(movieList == null) {
            return 0;
        }

        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePoster;

        public MovieViewHolder(final View itemView) {
            super(itemView);

            moviePoster = (ImageView) itemView.findViewById(R.id.imv_movie_image);
            moviePoster.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            Picasso.with(itemView.getContext()).load(URL_LOAD_IMAGE + movie.getPosterPath()).fit().into(moviePoster);
        }

        @Override
        public void onClick(View v) {
            mListener.onClicked(movieList.get(getAdapterPosition()));
        }
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClicked(Movie movie);
    }
}
