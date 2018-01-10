package com.shane.baking;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.shane.baking.ui.recipes.RecipesActivity;
import com.shane.baking.utils.SimpleIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {
    private SimpleIdlingResource idlingResource;

    @Rule
    public ActivityTestRule<RecipesActivity> activityRule =
            new ActivityTestRule<>(RecipesActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = activityRule.getActivity().getIdlingResource();
        registerIdlingResources(idlingResource);
    }

    @Test
    public void recipesLoaded() {
        // is the recycler view in view
        onView(withId(R.id.recipe_recycler_view)).check(matches(isDisplayed()));
    }



    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            unregisterIdlingResources(idlingResource);
        }
    }
}
