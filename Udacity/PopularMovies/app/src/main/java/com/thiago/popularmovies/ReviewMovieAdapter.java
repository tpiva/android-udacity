package com.thiago.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiago.popularmovies.dto.ReviewItem;

import java.util.ArrayList;

/**
 * Created by tmagalhaes on 11-Jan-17.
 */

public class ReviewMovieAdapter extends RecyclerView.Adapter<ReviewMovieAdapter.ViewHolder> {

    private ArrayList<ReviewItem> mReviewItems;
    private Context mContext;

    public ReviewMovieAdapter(Context context, ArrayList<ReviewItem> reviewItems) {
        this.mReviewItems = reviewItems;
        this.mContext = context;
    }

    @Override
    public ReviewMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_movie_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewMovieAdapter.ViewHolder holder, int position) {
        holder.mReviewContent.setText(mReviewItems.get(position).getContent().trim());
        holder.mReviewAuthor.setText(mContext.getString(R.string.format_review_author,mReviewItems.get(position).getAuthor()));
        holder.mReviewLink.setText(mContext.getString(R.string.format_review_link,mReviewItems.get(position).getUrl()));
    }

    @Override
    public int getItemCount() {
        return mReviewItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mReviewContent;
        public final TextView mReviewAuthor;
        public final TextView mReviewLink;

        public ViewHolder(View view) {
            super(view);
            mReviewContent = (TextView) view.findViewById(R.id.list_item_movie_review_content);
            mReviewAuthor = (TextView) view.findViewById(R.id.list_item_movie_review_author);
            mReviewLink = (TextView) view.findViewById(R.id.list_item_movie_review_link);
        }
    }

    public void updateData(ArrayList<ReviewItem> newData) {
        if(mReviewItems != null) {
            mReviewItems.clear();
            mReviewItems.addAll(newData);
            notifyDataSetChanged();
        }
    }
}
