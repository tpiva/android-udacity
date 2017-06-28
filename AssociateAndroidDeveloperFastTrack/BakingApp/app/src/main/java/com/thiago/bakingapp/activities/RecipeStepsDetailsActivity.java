package com.thiago.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Step;
import com.thiago.bakingapp.fragments.StepDetailsDescriptionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static com.thiago.bakingapp.utils.Constants.EXTRA_LIST_STEPS;
import static com.thiago.bakingapp.utils.Constants.EXTRA_STEP_SELECTED;

public class RecipeStepsDetailsActivity extends AppCompatActivity {

    private static final String STATE_CURRENT_POSITION = "current_position";
    private static final String STATE_CURRENT_STEPS_POSITION = "current_steps";
    private static final String STATE_CURRENT_STEPS_FRAGMENT = "fragment_steps";

    private List<Step> mSteps;
    private int mCurrentPosition;
    private StepDetailsDescriptionFragment mFragmentDescription;

    @Nullable
    @BindView(R.id.step_navigation_previous)
    ImageButton mBtnPrevious;
    @Nullable
    @BindView(R.id.step_navigation_next)
    ImageButton mBtnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        Step step = null;
        if (intent.hasExtra(EXTRA_STEP_SELECTED)
                || intent.hasExtra(EXTRA_LIST_STEPS)
                && savedInstanceState == null) {
            mSteps = intent.getParcelableArrayListExtra(EXTRA_LIST_STEPS);

            step = intent.getParcelableExtra(EXTRA_STEP_SELECTED);
            mCurrentPosition = step.getId();
        }

        if (savedInstanceState == null) {
            mFragmentDescription = new StepDetailsDescriptionFragment();
            mFragmentDescription.setStep(step);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_details_description, mFragmentDescription)
                    .commit();
        } else if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STATE_CURRENT_STEPS_POSITION);
            mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_POSITION);
            mFragmentDescription =
                    (StepDetailsDescriptionFragment) getSupportFragmentManager().getFragment(savedInstanceState,
                            STATE_CURRENT_STEPS_FRAGMENT);
            mFragmentDescription.setStep(mSteps.get(mCurrentPosition));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_details_description, mFragmentDescription)
                    .commit();
        }
    }

    @Optional
    @OnClick(R.id.step_navigation_next)
    public void nextStep() {
        if (mCurrentPosition < mSteps.size()-1) {
            mCurrentPosition++;
        } else {
            // restart
            mCurrentPosition = 0;
        }
        nextPreviousStep();
    }

    @Optional
    @OnClick(R.id.step_navigation_previous)
    public void previousStep() {
        if (mCurrentPosition > 0) {
            mCurrentPosition--;
        } else {
            // go back to end, cycle
            mCurrentPosition = (mSteps.size() - 1);
        }
        nextPreviousStep();
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_CURRENT_POSITION, mCurrentPosition);
        outState.putParcelableArrayList(STATE_CURRENT_STEPS_POSITION,
                (ArrayList<? extends Parcelable>) mSteps);
        getSupportFragmentManager().putFragment(outState, STATE_CURRENT_STEPS_FRAGMENT, mFragmentDescription);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSteps = savedInstanceState.getParcelableArrayList(STATE_CURRENT_STEPS_POSITION);
        mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_POSITION);
        mFragmentDescription =
                (StepDetailsDescriptionFragment) getSupportFragmentManager().getFragment(savedInstanceState,
                        STATE_CURRENT_STEPS_FRAGMENT);
        mFragmentDescription.setStep(mSteps.get(mCurrentPosition));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_details_description, mFragmentDescription)
                .commit();
    }

    private void nextPreviousStep() {
        mFragmentDescription = new StepDetailsDescriptionFragment();
        mFragmentDescription.setStep(mSteps.get(mCurrentPosition));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_details_description, mFragmentDescription)
                .commit();
    }
}
