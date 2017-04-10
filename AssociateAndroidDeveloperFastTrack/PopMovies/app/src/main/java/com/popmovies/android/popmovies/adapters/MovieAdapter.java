package com.popmovies.android.popmovies.adapters;

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
 *
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    public static final String URL_LOAD_IMAGE = "http://image.tmdb.org/t/p/w185/";
    private List<Movie> mMovieList;

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

        public void bind(Movie movie) {
            Picasso.with(itemView.getContext()).load(URL_LOAD_IMAGE + movie.getPosterPath()).fit().into(moviePoster);
        }

        @Override
        public void onClick(View v) {
            mListener.onClicked(mMovieList.get(getAdapterPosition()));
        }
    }

    public void setmMovieList(List<Movie> mMovieList) {
        this.mMovieList = mMovieList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClicked(Movie movie);
    }
}
