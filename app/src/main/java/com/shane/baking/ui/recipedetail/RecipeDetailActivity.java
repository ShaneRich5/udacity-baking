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
import com.shane.baking.ui.BaseActivity;
import com.shane.baking.ui.steps.StepFragment;

import butterknife.BindView;

public class RecipeDetailActivity extends BaseActivity implements StepAdapter.OnClickHandler {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.container_detail)
    FrameLayout detailContainerLayout;

    public static final String EXTRA_RECIPE = "extra_recipe";

    RecipeDetailContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setSupportActionBar(toolbar);

        Intent startingIntent = getIntent();

        if ( ! startingIntent.hasExtra(EXTRA_RECIPE)) {
            Toast.makeText(this, "Failed to load recipe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final Recipe recipe = getIntent()
                .getParcelableExtra(EXTRA_RECIPE);

        RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        presenter = new RecipeDetailPresenter(recipeDetailFragment, recipe);

        setupActionBar(getSupportActionBar(), recipe.getName());
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
            presenter.openRecipeSteps(step);
        } else {
            StepFragment fragment = StepFragment.newInstance(step);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(detailContainerLayout.getId(), fragment)
                    .commit();
        }
    }
}
