/*
 * Create by Thiago Piva Magalhães on  01/07/17 20:51
 * Copyright (c) 2017. All right reserved.
 * File RecipeDetailsStepTest.java belongs to Project BakingApp
 *
 * Last modified 01/07/17 17:03
 *
 */
package com.thiago.bakingapp;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.thiago.bakingapp.activities.RecipeStepsDetailsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation test, which will execute on an Android device for validate RecipeAcitivyt.
 *
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailsStepTest {

    @Rule
    public ActivityTestRule<RecipeStepsDetailsActivity> mStepsDetailsActivityActivityTestRule =
            new ActivityTestRule<>(RecipeStepsDetailsActivity.class);

    @Test
    public void checkNavigateButtons() {
        onView(withId(R.id.step_navigation_next)).check(matches(isDisplayed()));
        onView(withId(R.id.step_navigation_previous)).check(matches(isDisplayed()));
    }

    @Test
    public void checkNotVisibleImage() {
        onView(withId(R.id.step_image)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}
