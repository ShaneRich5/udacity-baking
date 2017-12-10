package com.shane.baking;

import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.shane.baking.ui.recipes.RecipesActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<RecipesActivity> activityRule =
            new ActivityTestRule<>(RecipesActivity.class);

}
