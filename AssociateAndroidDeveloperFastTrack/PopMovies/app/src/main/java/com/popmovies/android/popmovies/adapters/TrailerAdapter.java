package com.popmovies.android.popmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    private static final String APP_YOUTUBE = "vnd.youtube:";
    private static final String BROWSER_YOUTUBE = "http://www.youtube.com/watch?v=";

    private List<Trailer> trailers;

    private Context mContext;

    public TrailerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_movie_trailers_item, parent, false);
        return (new TrailerViewHolder(view));
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bindTrailerTitle(trailers.get(position).getName());
        final String key = trailers.get(position).getKey();
        holder.movieTrailerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( APP_YOUTUBE + key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BROWSER_YOUTUBE + key));
                try {
                    mContext.startActivity(appIntent);
                } catch (ActivityNotFoundException e) {
                    mContext.startActivity(webIntent);

                }
            }
        });
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
            movieTrailerImageView.setClickable(true);
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
