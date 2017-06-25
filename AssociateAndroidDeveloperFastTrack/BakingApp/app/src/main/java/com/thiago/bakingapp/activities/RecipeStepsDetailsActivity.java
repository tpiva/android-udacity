package com.thiago.bakingapp.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Step;
import com.thiago.bakingapp.fragments.StepDetailsDescriptionFragment;
import com.thiago.bakingapp.fragments.StepDetailsNavigateFragment;

import java.util.List;

import static com.thiago.bakingapp.utils.Constants.EXTRA_STEP_SELECTED;
import static com.thiago.bakingapp.utils.Constants.EXTRA_LIST_STEPS;

public class RecipeStepsDetailsActivity extends AppCompatActivity {

    private List<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_details);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_STEP_SELECTED)
                || intent.hasExtra(EXTRA_LIST_STEPS)) {
            mSteps = intent.getParcelableArrayListExtra(EXTRA_LIST_STEPS);

            Step step = intent.getParcelableExtra(EXTRA_STEP_SELECTED);

            StepDetailsDescriptionFragment descriptionFragment = new StepDetailsDescriptionFragment();
            descriptionFragment.setStep(step);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_details_description, descriptionFragment)
                    .commit();

            StepDetailsNavigateFragment navigateFragment = new StepDetailsNavigateFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_details_next, navigateFragment)
                    .commit();
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

}
