package com.shane.baking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.shane.baking.R;
import com.shane.baking.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "extra_recipe";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fragment_container) FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent startingIntent = getIntent();

        if (! startingIntent.hasExtra(EXTRA_RECIPE)) {
            Toast.makeText(this, "Error loading recipe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Recipe recipe = startingIntent.getParcelableExtra(EXTRA_RECIPE);

        toolbar.setTitle(recipe.getName());

        FragmentManager manager = getSupportFragmentManager();

        if (manager.findFragmentById(fragmentContainer.getId()) == null) {
            RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(recipe);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragmentContainer.getId(), fragment, RecipeDetailFragment.TAG);
            transaction.commit();
        }
    }
}
