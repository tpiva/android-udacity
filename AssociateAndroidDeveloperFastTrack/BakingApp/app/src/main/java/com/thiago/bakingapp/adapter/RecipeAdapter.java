/*
 * Create by Thiago Piva Magalhães on  01/07/17 20:39
 * Copyright (c) 2017. All right reserved.
 * File RecipeAdapter.java belongs to Project BakingApp
 *
 * Last modified 01/07/17 16:18
 *
 */
package com.thiago.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter of main list of recipes that bind recipes and action of click for user choice one step
 * to see its details.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;
    private RecipeClickListener mCallback;
    private Context mContext;

    public RecipeAdapter(RecipeClickListener recipeClickListener, Context context) {
        this.mCallback = recipeClickListener;
        this.mContext = context;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return (new RecipeViewHolder(view));
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }

        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipe_name_list_item)
        TextView mTextVIewName;
        @BindView(R.id.recipe_servings_list_item)
        TextView mTextViewServing;
        @BindView(R.id.recipe_image)
        ImageView mImageViewRecipe;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            mTextVIewName.setText(recipe.getName());
            mTextViewServing.setText(String.valueOf(recipe.getServing()));
            if (recipe.getImage() != null
                    && !"".equals(recipe.getImage())) {
                Picasso.with(mContext)
                        .load(recipe.getImage())
                        .placeholder(R.drawable.folder_food)
                        .error(R.drawable.folder_food)
                        .into(mImageViewRecipe);
            }
        }

        @Override
        public void onClick(View view) {
            mCallback.onItemClicked(mRecipes.get(getAdapterPosition()));
        }
    }

    /**
     * Sets mRecipes to show for user.
     * @param mRecipes
     */
    public void setRecipes(List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
        notifyDataSetChanged();
    }

    /**
     * Interface for callback of an item clicked.
     */
    public interface RecipeClickListener {
        void onItemClicked(Recipe recipe);
    }

}
