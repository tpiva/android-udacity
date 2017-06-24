package com.thiago.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingRecipeAdapter extends RecyclerView.Adapter<BakingRecipeAdapter.RecipeViewHolder>{

    private List<Recipe> recipes;

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return (new RecipeViewHolder(view));
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        }

        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_name_list_item)
        TextView mTextVIewName;
        @BindView(R.id.recipe_servings_list_item)
        TextView mTextViewServing;
        @BindView(R.id.recipe_image)
        ImageView mImageViewRecipe;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(Recipe recipe) {
            mTextVIewName.setText(recipe.getName());
            mTextViewServing.setText(String.valueOf(recipe.getServing()));
            if (recipe.getImage() != null
                    && !"".equals(recipe.getImage())) {
                // TODO some lib as picasso
                mImageViewRecipe.setImageResource(0);
            }
        }
    }

    /**
     * Sets recipes to show for user.
     * @param recipes
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

}
