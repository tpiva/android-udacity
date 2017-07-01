package com.thiago.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Recipe;
import com.thiago.bakingapp.bean.Step;
import com.thiago.bakingapp.fragments.BakingRecipeDetailsFragment;
import com.thiago.bakingapp.fragments.StepDetailsDescriptionFragment;

import java.util.ArrayList;

import static com.thiago.bakingapp.utils.Constants.EXTRA_LIST_STEPS;
import static com.thiago.bakingapp.utils.Constants.EXTRA_RECIPE_SELECTED;
import static com.thiago.bakingapp.utils.Constants.EXTRA_STEP_SELECTED;

public class RecipeDetailsActivity extends AppCompatActivity
        implements BakingRecipeDetailsFragment.OnStepSelected {

    private static final String CURRENT_RECIPE = "current_recipe";
    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private Recipe mRecipe;
    private boolean mTwoPanel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent intent = getIntent();
        Recipe tempRecipe = null;
        if (intent.hasExtra(EXTRA_RECIPE_SELECTED)) {
            tempRecipe = intent.getParcelableExtra(EXTRA_RECIPE_SELECTED);
            Log.d(TAG, "Extra recipe id: " + tempRecipe.getId());
        } else {
            // TODO shows error.
        }

        if (findViewById(R.id.recipe_details_steps_two_panel) != null) {
            mTwoPanel = true;
            mFragmentManager = getSupportFragmentManager();
            mRecipe = tempRecipe;

            if (savedInstanceState == null) {
                StepDetailsDescriptionFragment descriptionFragment = new StepDetailsDescriptionFragment();
                descriptionFragment.setStep(mRecipe.getSteps().get(0));
                mFragmentManager.beginTransaction()
                        .add(R.id.recipe_step_details_description, descriptionFragment)
                        .commit();
            }

        } else {
            mTwoPanel = false;
            if (savedInstanceState == null) {
                mRecipe = tempRecipe;
            } else {
                mRecipe = savedInstanceState.getParcelable(CURRENT_RECIPE);
            }
        }

        BakingRecipeDetailsFragment fragment =
                (BakingRecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_details_master);
        fragment.updateContent(mRecipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipe = savedInstanceState.getParcelable(CURRENT_RECIPE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CURRENT_RECIPE, mRecipe);
        super.onSaveInstanceState(outState);
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
    public void onStepClicked(Step step) {
        if (mTwoPanel) {
            StepDetailsDescriptionFragment descriptionFragment = new StepDetailsDescriptionFragment();
            descriptionFragment.setStep(step);
            mFragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_details_description, descriptionFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepsDetailsActivity.class);
            intent.putExtra(EXTRA_STEP_SELECTED, step);
            intent.putParcelableArrayListExtra(EXTRA_LIST_STEPS,
                    (ArrayList<? extends Parcelable>) mRecipe.getSteps());
            startActivity(intent);
        }
    }
}
