package com.thiago.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Recipe;
import com.thiago.bakingapp.bean.Step;
import com.thiago.bakingapp.fragments.BakingRecipeDetailsFragment;

import java.util.ArrayList;

import static com.thiago.bakingapp.utils.Constants.EXTRA_STEP_SELECTED;
import static com.thiago.bakingapp.utils.Constants.EXTRA_RECIPE_SELECTED;
import static com.thiago.bakingapp.utils.Constants.EXTRA_LIST_STEPS;

public class RecipeDetailsActivity extends AppCompatActivity
        implements BakingRecipeDetailsFragment.OnStepSelected {

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_RECIPE_SELECTED)) {
            mRecipe = intent.getParcelableExtra(EXTRA_RECIPE_SELECTED);
            BakingRecipeDetailsFragment fragment =
                    (BakingRecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_details_master);
            fragment.updateContent(mRecipe);
        } else {
            // TODO shows error.
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNextStepClicked(int id) {

    }

    @Override
    public void onStepClicked(Step step) {
        Intent intent = new Intent(this, RecipeStepsDetailsActivity.class);
        intent.putExtra(EXTRA_STEP_SELECTED, step);
        intent.putParcelableArrayListExtra(EXTRA_LIST_STEPS,
                (ArrayList<? extends Parcelable>) mRecipe.getSteps());
        startActivity(intent);
    }
}
