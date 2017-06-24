package com.thiago.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Ingredient;
import com.thiago.bakingapp.bean.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingRecipeDetailsFragment extends Fragment {

    private Recipe mRecipe;

    @BindView(R.id.recipe_details_ingredients)
    LinearLayout mRecipeDetailsIngredients;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details_item, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    public void updateContent(Recipe recipe) {
        this.mRecipe = recipe;
        updateIngredients();
    }

    private void updateIngredients() {
        if (mRecipeDetailsIngredients != null) {
            for (Ingredient ingredient : mRecipe.getIngredients()) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_recipe_details_ingredients, null);
                TextView textIngredients = (TextView) view.findViewById(R.id.text_ingredient);
                StringBuilder builder = new StringBuilder();
                builder.append(ingredient.getQuantity()).append(" ")
                        .append(ingredient.getMeasure()).append(" ")
                        .append(ingredient.getIngredient());

                textIngredients.setText(builder.toString());
                if (view != null) {
                    mRecipeDetailsIngredients.addView(view);
                }
            }
        }

    }

    private void updateSteps() {
        //TODO update steps here
    }

}
