package com.thiago.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.adapter.RecipeDetailsStepAdapter;
import com.thiago.bakingapp.bean.Ingredient;
import com.thiago.bakingapp.bean.Recipe;
import com.thiago.bakingapp.bean.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingRecipeDetailsFragment extends Fragment
        implements RecipeDetailsStepAdapter.RecipeStepDetailListener{

    private Recipe mRecipe;

    @BindView(R.id.recipe_details_ingredients)
    LinearLayout mRecipeDetailsIngredients;
    @BindView(R.id.recipe_details_steps)
    RecyclerView mRecipeDetailsSteps;

    private RecipeDetailsStepAdapter mAdapter;
    private OnStepSelected mCallback;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details_item, container, false);
        ButterKnife.bind(this,view);

        // initialize recycle view stuffs
        mAdapter = new RecipeDetailsStepAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecipeDetailsSteps.setLayoutManager(linearLayoutManager);
        mRecipeDetailsSteps.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    /**
     * Receives recipe selected and attach its information on view.
     * @param recipe
     */
    public void updateContent(Recipe recipe) {
        this.mRecipe = recipe;
        if (recipe != null) {
            updateIngredients();
            updateSteps();
        }
    }

    /**
     * Set ingredients to be bind on view
     */
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

    /**
     * Set steps to be bind on view
     */
    private void updateSteps() {
        mAdapter.setSteps(mRecipe.getSteps());
    }

    @Override
    public void onItemClicked(Step step) {
        mCallback.onStepClicked(step);
    }

    /**
     * Interface to communicate with master fragment.
     */
    public interface OnStepSelected {
        void onStepClicked(Step step);
    }
}
