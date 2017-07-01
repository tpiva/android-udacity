package com.thiago.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.thiago.bakingapp.activities.MainActivity;
import com.thiago.bakingapp.adapter.RecipeAdapter;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void onScrollAllItens() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.scrollToPosition(4));
    }

    @Test
    public void onItemsShow() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));

        onView(withId(R.id.recipe_list)).perform(
                RecyclerViewActions.scrollToHolder(
                        withContent("Nutella Pie")
                )
        );
    }

    @Test
    public void onClickRecipe() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.recipe_details_ingredients_title))
                .check(matches(withText(mMainActivityRule.getActivity().getResources().getString(R.string.ingredients_title))));
        onView(withId(R.id.recipe_details_steps_title))
                .check(matches(withText(mMainActivityRule.getActivity().getResources().getString(R.string.steps_title))));
        onView(allOf(withId(R.id.recipe_details_ingredients),
                hasDescendant(withId(R.id.img_mark)))).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_details_steps)).check(matches(isDisplayed()));
    }

    private static Matcher<RecyclerView.ViewHolder> withContent(final String textToDisplay) {
        return new BoundedMatcher<RecyclerView.ViewHolder, RecipeAdapter.RecipeViewHolder>
                (RecipeAdapter.RecipeViewHolder.class) {
            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeViewHolder item) {
                TextView recipeTitle = (TextView) item.itemView.findViewById(R.id.recipe_name_list_item);
                return recipeTitle.getText().toString().equalsIgnoreCase(textToDisplay);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("view holder with title: " + textToDisplay);
            }
        };
    }
}
