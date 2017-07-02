/*
 * Create by Thiago Piva Magalhães on  01/07/17 20:42
 * Copyright (c) 2017. All right reserved.
 * File Parser.java belongs to Project BakingApp
 *
 * Last modified 25/06/17 12:58
 *
 */
package com.thiago.bakingapp.data;

import com.thiago.bakingapp.bean.Ingredient;
import com.thiago.bakingapp.bean.Recipe;
import com.thiago.bakingapp.bean.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser response data from webservice to java object that represent one recipe.
 */
final class Parser {

    private Parser() {}

    /**
     * Parser string response from webservice to recipe object.
     * @param responseStream
     * @return
     * @throws JSONException
     */
    public static List<Recipe> parserJsonToRecipes(String responseStream) throws JSONException {
        List<Recipe> recipes = new ArrayList<>();

        final String OWN_RECIPE_ID = "id";
        final String OWN_RECIPE_NAME = "name";
        final String OWN_RECIPE_ROOT_OF_INGREDIENTS = "ingredients";
        final String OWN_RECIPE_SERVINGS = "servings";
        final String OWN_RECIPE_IMAGE = "image";
        final String OWN_RECIPE_INGREDIENTS_QUANTITY = "quantity";
        final String OWN_RECIPE_INGREDIENTS_MEASURE = "measure";
        final String OWN_RECIPE_INGREDIENTS_INGREDIENT = "ingredient";
        final String OWN_RECIPE_ROOT_STEPS = "steps";
        final String OWN_RECIPE_STEPS_ID = "id";
        final String OWN_RECIPE_STEPS_SHORT_DESCRIPTION = "shortDescription";
        final String OWN_RECIPE_STEPS_DESCRIPTION = "description";
        final String OWN_RECIPE_STEPS_VIDEO_URL = "videoURL";
        final String OWN_RECIPE_STEPS_THUMBNAIL_URL = "thumbnailURL";

        JSONArray mainArray = new JSONArray(responseStream);

        for (int i = 0; i < mainArray.length(); i++) {
            JSONObject mainJsonObject = mainArray.getJSONObject(i);

            Recipe recipe = new Recipe();
            recipe.setId(mainJsonObject.getInt(OWN_RECIPE_ID));
            recipe.setName(mainJsonObject.getString(OWN_RECIPE_NAME));
            recipe.setServing(mainJsonObject.getInt(OWN_RECIPE_SERVINGS));
            recipe.setImage(mainJsonObject.getString(OWN_RECIPE_IMAGE));

            JSONArray arrayIngredients = mainJsonObject.getJSONArray(OWN_RECIPE_ROOT_OF_INGREDIENTS);
            List<Ingredient> ingredients = new ArrayList<>();

            for (int j = 0; j < arrayIngredients.length(); j++) {
                JSONObject ingredientsObject = arrayIngredients.getJSONObject(j);
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredient(ingredientsObject.getString(OWN_RECIPE_INGREDIENTS_INGREDIENT));
                ingredient.setMeasure(ingredientsObject.getString(OWN_RECIPE_INGREDIENTS_MEASURE));
                ingredient.setQuantity(ingredientsObject.getInt(OWN_RECIPE_INGREDIENTS_QUANTITY));
                ingredients.add(ingredient);
            }

            recipe.setIngredients(ingredients);

            JSONArray arraySteps = mainJsonObject.getJSONArray(OWN_RECIPE_ROOT_STEPS);
            List<Step> steps = new ArrayList<>();

            for (int j = 0; j < arraySteps.length(); j++) {
                JSONObject stepObject = arraySteps.getJSONObject(j);
                Step step = new Step();
                step.setId(stepObject.getInt(OWN_RECIPE_STEPS_ID));
                step.setShortDescripition(stepObject.getString(OWN_RECIPE_STEPS_SHORT_DESCRIPTION));
                step.setDescription(stepObject.getString(OWN_RECIPE_STEPS_DESCRIPTION));
                step.setThumbnailURL(stepObject.getString(OWN_RECIPE_STEPS_THUMBNAIL_URL));
                step.setVideoUrl(stepObject.getString(OWN_RECIPE_STEPS_VIDEO_URL));

                steps.add(step);
            }

            recipe.setSteps(steps);
            recipes.add(recipe);
        }
        return recipes;
    }
}
