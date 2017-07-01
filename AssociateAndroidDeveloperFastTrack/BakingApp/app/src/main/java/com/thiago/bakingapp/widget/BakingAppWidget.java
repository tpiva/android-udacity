/*
 * Create by Thiago Piva Magalhães on  01/07/17 20:47
 * Copyright (c) 2017. All right reserved.
 * File BakingAppWidget.java belongs to Project BakingApp
 *
 * Last modified 01/07/17 11:29
 *
 */
package com.thiago.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.thiago.bakingapp.R;
import com.thiago.bakingapp.activities.MainActivity;
import com.thiago.bakingapp.activities.RecipeDetailsActivity;
import com.thiago.bakingapp.bean.Ingredient;
import com.thiago.bakingapp.bean.Recipe;
import com.thiago.bakingapp.utils.Constants;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String TAG = BakingAppWidget.class.getSimpleName();

    /**
     * Update in facts widget content.
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param recipe
     * @param isFirstLaunch It identify if is necessary to load intent of details on widget.
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe, boolean isFirstLaunch) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent intent = null;
        if (recipe == null && isFirstLaunch) {
            // call MainActivity
            intent = new Intent(context, MainActivity.class);
            final Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.EXTRA_FIRST_TIME_WIDGET, Boolean.TRUE);
            bundle.putInt(Constants.EXTRA_WIDGET_ID, appWidgetId);
            intent.putExtras(bundle);

        } else {
            // call DetailsActivity
            intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_RECIPE_SELECTED, recipe);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            views.setViewVisibility(R.id.appwidget_recipe_title, View.VISIBLE);
            views.setTextViewText(R.id.appwidget_recipe_title, recipe != null ? recipe.getName() : "");
            views.setViewVisibility(R.id.appwidget_image_button, View.GONE);
            views.setViewVisibility(R.id.appwidget_image_expandable, View.VISIBLE);

            StringBuilder builder = new StringBuilder();
            if (recipe != null) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    builder.append(ingredient.getQuantity()).append(" ").append(ingredient.getMeasure())
                            .append(" ").append(ingredient.getIngredient()).append("\n");
                }
            }
            views.setTextViewText(R.id.appwidget_text, builder.toString());
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_image_button, pendingIntent);
        views.setOnClickPendingIntent(R.id.appwidget_image_expandable, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Initial update of widget after user choices recipe to load on it widget.
     * @param context
     * @param appWidgetManager
     * @param ids widget ids
     * @param recipe current recipe choice by user.
     */
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int[] ids, Recipe recipe) {
        for (Integer id : ids) {
            updateAppWidget(context, appWidgetManager, id, recipe, false);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (Integer id : appWidgetIds) {
            Log.d(TAG, "AppWidgetIntent id: " + id);
            updateAppWidget(context, appWidgetManager, id, null, true);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

