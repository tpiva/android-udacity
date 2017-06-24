package com.thiago.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Step;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsStepAdapter extends RecyclerView.Adapter<RecipeDetailsStepAdapter.RecipeStepViewHolder> {

    private List<Step> mSteps;

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

    class RecipeStepViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.step_id)
        TextView mTextViewStepId;
        @BindView(R.id.step_description)
        TextView mTextViewDescription;
        @BindString(R.string.recipe_details_step_prefix)
        String mPrefixStep;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // TODO add itemClick
        }

        public void bind(Step step) {
            mTextViewStepId.setText(mPrefixStep + " " + step.getId());
            mTextViewDescription.setText(step.getDescription());
        }
    }

    public void setSteps(List<Step> mSteps) {
        this.mSteps = mSteps;
        notifyDataSetChanged();
    }
}
