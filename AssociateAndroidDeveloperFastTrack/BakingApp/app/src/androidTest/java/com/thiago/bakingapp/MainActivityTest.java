package com.thiago.bakingapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thiago.bakingapp.activities.MainActivity;
import com.thiago.bakingapp.adapter.RecipeAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasLinks;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;


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

    @Test
    public void onClickStep() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.recipe_details_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.step_description)).check(matches(withText("Recipe Introduction")));
        onView(allOf(withId(R.id.step_description_details),
                hasDescendant(withId(R.id.step_exo_video)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.step_description_details),hasLinks()));

    }

    @Test
    public void onClickNextStep() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.recipe_details_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.step_navigation_next)).perform(click());
        onView(withId(R.id.step_description))
                .check(matches(withText(startsWith("1. Preheat the oven to"))));
        onView(allOf(withId(R.id.step_description_details),
                hasDescendant(withId(R.id.step_image)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.step_description_details),not(hasLinks())));
        onView(withId(R.id.step_image)).check(matches(withImage(R.drawable.default_food)));
    }

    @Test
    public void onClickPreviousStep() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.recipe_details_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.step_navigation_previous)).perform(click());
        onView(withId(R.id.step_description))
                .check(matches(withText(startsWith("9. Cut and serve."))));
        onView(allOf(withId(R.id.step_description_details),
                hasDescendant(withId(R.id.step_exo_video)))).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.step_description_details),hasLinks()));
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

    private static TypeSafeMatcher<View> withImage(final int expectedId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("comparingImages");
            }

            @Override
            protected boolean matchesSafely(View target) {
                if (!(target instanceof ImageView)){
                    return false;
                }
                ImageView imageView = (ImageView) target;
                if (expectedId < 0){
                    return imageView.getDrawable() == null;
                }
                Resources resources = target.getContext().getResources();
                Drawable expectedDrawable = resources.getDrawable(expectedId);
                if (expectedDrawable == null) {
                    return false;
                }
                BitmapDrawable bmd = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = bmd.getBitmap();
                BitmapDrawable expected = (BitmapDrawable) expectedDrawable;
                Bitmap otherBitmap = expected.getBitmap();
                return bitmap.sameAs(otherBitmap);
            }
        };
    }
}
