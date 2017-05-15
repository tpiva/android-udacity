package com.popmovies.android.popmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popmovies.android.popmovies.R;
import com.popmovies.android.popmovies.bo.Trailer;

import java.util.List;

/**
 * Created by Thiago on 15/05/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailers;

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_movie_trailers_item, parent, false);
        return (new TrailerViewHolder(view));
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bindTrailerTitle(trailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (trailers == null) {
            return 0;
        }

        return trailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        ImageView movieTrailerImageView;
        TextView movieTrailerNameTextView;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            movieTrailerImageView = (ImageView) itemView.findViewById(R.id.rc_item_movie_trailer_image);
            movieTrailerNameTextView = (TextView) itemView.findViewById(R.id.rc_item_movie_trailer_name);

            movieTrailerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO open youtube
                }
            });
        }

        public void bindTrailerTitle(String title) {
            movieTrailerNameTextView.setText(title);
        }

    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }
}
