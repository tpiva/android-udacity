package com.thiago.bakingapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thiago.bakingapp.R;

import static com.thiago.bakingapp.utils.Constants.EXTRA_STEP_SELECTED;

public class RecipeStepsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_details);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_STEP_SELECTED)) {
            // TODO inflate fragments.
        }
    }
}
