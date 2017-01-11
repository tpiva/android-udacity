package com.thiago.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiago.popularmovies.dto.TrailerItem;

import java.util.ArrayList;

/**
 * Created by tmagalhaes on 11-Jan-17.
 */

public class TrailerMovieAdapter extends RecyclerView.Adapter<TrailerMovieAdapter.ViewHolder> {

    private static final String TRAILER = "Trailer";

    private ArrayList<TrailerItem> mTrailerItens;

    public TrailerMovieAdapter(ArrayList<TrailerItem> trailerItems) {
        this.mTrailerItens = trailerItems;
    }

    @Override
    public TrailerMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_movie_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(TrailerMovieAdapter.ViewHolder holder, int position) {
        holder.mTrailerName.setText((TRAILER + " " + (position + 1)));
    }

    @Override
    public int getItemCount() {
        return mTrailerItens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTrailerName;

        public ViewHolder(View view) {
            super(view);
            mTrailerName = (TextView) view.findViewById(R.id.list_item_movie_trailer_name);
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
