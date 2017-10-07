package com.shane.baking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shane.baking.R;
import com.shane.baking.models.Recipe;
import com.shane.baking.models.Step;
import com.shane.baking.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.shane.baking.utils.Constants.EXTRA_RECIPE;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailFragment.InteractionListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fragment_container) FrameLayout fragmentListContainer;
    @Nullable @BindView(R.id.fragment_detail) FrameLayout fragmentDetailContainer;

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent startingIntent = getIntent();

        if (! startingIntent.hasExtra(Constants.EXTRA_RECIPE)) {
            Toast.makeText(this, "Error loading recipe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipe = startingIntent.getParcelableExtra(EXTRA_RECIPE);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager manager = getSupportFragmentManager();

        if (manager.findFragmentById(fragmentListContainer.getId()) == null) {
            RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(recipe);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragmentListContainer.getId(), fragment, RecipeDetailFragment.TAG);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStepSelected(Step step) {
        Timber.i(step.toString());
        if (fragmentDetailContainer == null) {
            Intent stepIntent = new Intent(this, StepActivity.class);
            stepIntent.putExtra(Constants.EXTRA_RECIPE, recipe);
            stepIntent.putExtra(Constants.EXTRA_STEP_ID, step.getId());
            startActivity(stepIntent);
        } else {
            StepFragment stepFragment = StepFragment.newInstance(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(fragmentDetailContainer.getId(), stepFragment, StepFragment.TAG)
                    .commit();
        }
    }
}
