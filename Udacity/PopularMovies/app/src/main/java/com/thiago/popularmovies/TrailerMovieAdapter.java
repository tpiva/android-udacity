package com.thiago.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thiago.popularmovies.dto.TrailerItem;

import java.util.ArrayList;

/**
 * Created by tmagalhaes on 11-Jan-17.
 */

public class TrailerMovieAdapter extends RecyclerView.Adapter<TrailerMovieAdapter.ViewHolder> {

    private static final String TRAILER = "Trailer";
    private static final String APP_YOUTUBE = "vnd.youtube:";
    private static final String BROWSER_YOUTUBE = "http://www.youtube.com/watch?v=";

    private ArrayList<TrailerItem> mTrailerItens;
    private Context mContext;

    public TrailerMovieAdapter(Context context, ArrayList<TrailerItem> trailerItems) {
        this.mTrailerItens = trailerItems;
        this.mContext = context;
    }

    @Override
    public TrailerMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_movie_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(TrailerMovieAdapter.ViewHolder holder, int position) {
        final String key = mTrailerItens.get(position).getKey();
        holder.mTrailerName.setText((TRAILER + " " + (position + 1)));
        holder.mTrailerVideo.setOnClickListener(new View.OnClickListener() {
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
        return mTrailerItens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTrailerName;
        public final ImageButton mTrailerVideo;

        public ViewHolder(View view) {
            super(view);
            mTrailerName = (TextView) view.findViewById(R.id.list_item_movie_trailer_name);
            mTrailerVideo = (ImageButton) view.findViewById(R.id.list_item_movie_trailer_image);
        }
    }

    public void updateData(ArrayList<TrailerItem> newData) {
        if(mTrailerItens != null) {
            mTrailerItens.clear();
            mTrailerItens.addAll(newData);
            notifyDataSetChanged();
        }
    }
}
