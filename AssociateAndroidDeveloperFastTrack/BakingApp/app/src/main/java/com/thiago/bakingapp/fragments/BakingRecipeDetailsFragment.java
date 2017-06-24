package com.thiago.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Recipe;

public class BakingRecipeDetailsFragment extends Fragment {

    private Recipe recipe;

    private TextView c;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details_item, container, false);
        c = (TextView) view.findViewById(R.id.test);
        return view;
    }

    public void setRecipe(Recipe recipe) {
        c.setText(recipe.getName());
    }
}
