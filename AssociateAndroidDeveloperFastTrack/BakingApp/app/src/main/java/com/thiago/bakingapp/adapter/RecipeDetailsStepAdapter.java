/*
 * Create by Thiago Piva Magalhães on  01/07/17 20:39
 * Copyright (c) 2017. All right reserved.
 * File RecipeDetailsStepAdapter.java belongs to Project BakingApp
 *
 * Last modified 01/07/17 11:58
 *
 */
package com.thiago.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter of step details that bind video/image and description of each step.
 */
public class RecipeDetailsStepAdapter extends RecyclerView.Adapter<RecipeDetailsStepAdapter.RecipeStepViewHolder> {

    private List<Step> mSteps;
    private RecipeStepDetailListener mCallback;

    public RecipeDetailsStepAdapter(RecipeStepDetailListener listener) {
        this.mCallback = listener;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe_details_steps, parent, false);
        ButterKnife.bind(this, view);
        return (new RecipeStepViewHolder(view));
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        holder.bind(mSteps.get(position));
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        }

        return mSteps.size();
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.step_description)
        TextView mTextViewDescription;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Step step) {
            if (step.getId() == 0) {
                mTextViewDescription.setText(step.getShortDescripition());
            } else {
                mTextViewDescription.setText("" + step.getId() + ": " + step.getShortDescripition());
            }

        }

        @Override
        public void onClick(View view) {
            mCallback.onItemClicked(mSteps.get(getAdapterPosition()));
        }
    }

    /**
     * Set steps to be attach to view.
     * @param mSteps
     */
    public void setSteps(List<Step> mSteps) {
        this.mSteps = mSteps;
        notifyDataSetChanged();
    }

    /**
     * Interface to handler click on each view.
     */
    public interface RecipeStepDetailListener {
        void onItemClicked(Step step);
    }
}
