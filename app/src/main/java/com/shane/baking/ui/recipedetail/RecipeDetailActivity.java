package com.shane.baking.ui.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shane.baking.R;
import com.shane.baking.adapters.StepAdapter;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.Step;
import com.shane.baking.exceptions.RecipeNotFoundException;
import com.shane.baking.ui.base.BaseActivity;
import com.shane.baking.ui.steps.StepContract;
import com.shane.baking.ui.steps.StepFragment;
import com.shane.baking.ui.steps.StepPresenter;

import butterknife.BindView;

public class RecipeDetailActivity extends BaseActivity implements StepAdapter.OnClickHandler {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.container_detail)
    FrameLayout detailContainerLayout;

    public static final String EXTRA_RECIPE = "extra_recipe";

    RecipeDetailContract.Presenter recipePresenter;
    StepContract.Presenter stepPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setSupportActionBar(toolbar);

        Intent startingIntent = getIntent();

        try {
            final Recipe recipe = retrieveRecipeFromIntent(startingIntent);

            RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment);

            recipePresenter = new RecipeDetailPresenter(recipeDetailFragment, recipe);
            setupActionBar(getSupportActionBar(), recipe.getName());
        } catch (RecipeNotFoundException exception) {
            Toast.makeText(this, "Failed to load recipe", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @NonNull
    private Recipe retrieveRecipeFromIntent(@NonNull Intent startingIntent) throws RecipeNotFoundException {
        if ( ! startingIntent.hasExtra(EXTRA_RECIPE)) {
            throw new RecipeNotFoundException();
        }

        Recipe recipe = startingIntent.getParcelableExtra(EXTRA_RECIPE);

        if (recipe == null) {
            throw new RecipeNotFoundException();
        }

        return recipe;
    }

    private void setupActionBar(ActionBar actionBar, @NonNull String name) {
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Step step) {
        if (detailContainerLayout == null) {
            recipePresenter.openRecipeSteps(step);
        } else {
            StepFragment fragment = new StepFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(detailContainerLayout.getId(), fragment)
                    .commit();
            stepPresenter = new StepPresenter(fragment, step);
        }
    }
}
