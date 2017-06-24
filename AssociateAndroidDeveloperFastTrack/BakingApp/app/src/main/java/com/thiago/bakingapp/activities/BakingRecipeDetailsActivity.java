package com.thiago.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Recipe;
import com.thiago.bakingapp.fragments.BakingRecipeDetailsFragment;

public class BakingRecipeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_recipe_details);

        Intent intent = getIntent();
        if (intent.hasExtra("recipe_selected")) {
            Recipe recipe = intent.getParcelableExtra("recipe_selected");
            BakingRecipeDetailsFragment fragment =
                    (BakingRecipeDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_details_master);
            fragment.updateContent(recipe);
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
}
