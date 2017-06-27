package com.thiago.bakingapp.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Step;
import com.thiago.bakingapp.fragments.StepDetailsDescriptionFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.thiago.bakingapp.utils.Constants.EXTRA_STEP_SELECTED;
import static com.thiago.bakingapp.utils.Constants.EXTRA_LIST_STEPS;

public class RecipeStepsDetailsActivity extends AppCompatActivity {

    private List<Step> mSteps;
    private int mCurrentPosition;
    private FragmentManager mFragmentManager;

    @BindView(R.id.step_navigation_previous)
    Button mBtnPrevious;
    @BindView(R.id.step_navigation_next)
    Button mBtnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_STEP_SELECTED)
                || intent.hasExtra(EXTRA_LIST_STEPS)) {
            mSteps = intent.getParcelableArrayListExtra(EXTRA_LIST_STEPS);

            Step step = intent.getParcelableExtra(EXTRA_STEP_SELECTED);
            mCurrentPosition = step.getId();

            StepDetailsDescriptionFragment descriptionFragment = new StepDetailsDescriptionFragment();
            descriptionFragment.setStep(step);
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .add(R.id.recipe_step_details_description, descriptionFragment)
                    .commit();

            mBtnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentPosition < mSteps.size()-1) {
                        mCurrentPosition++;
                    } else {
                        // restart
                        mCurrentPosition = 0;
                    }
                    nextPreviousStep();
                }
            });

            mBtnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentPosition > 0) {
                        mCurrentPosition--;
                    } else {
                        // go back to end, cycle
                        mCurrentPosition = (mSteps.size() - 1);
                    }
                    nextPreviousStep();
                }
            });
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

    private void nextPreviousStep() {
        StepDetailsDescriptionFragment descriptionFragment = new StepDetailsDescriptionFragment();
        descriptionFragment.setStep(mSteps.get(mCurrentPosition));

        mFragmentManager.beginTransaction()
                .replace(R.id.recipe_step_details_description, descriptionFragment)
                .commit();
    }
}
