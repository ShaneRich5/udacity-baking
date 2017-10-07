package com.shane.baking.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.shane.baking.R;
import com.shane.baking.adapters.RecipeAdapter;
import com.shane.baking.data.RecipeContract;
import com.shane.baking.models.Recipe;
import com.shane.baking.utils.Constants;
import com.shane.baking.widget.RecipeWidgetService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shane on 10/7/2017.
 */

public class RecipeWidgetActivity extends AppCompatActivity implements RecipeAdapter.OnClickHandler {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_widget);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if ( ! getIntent().hasExtra(Constants.WIDGET_ID)) {
            Toast.makeText(this, "Error identifying widget", Toast.LENGTH_SHORT).show();
            finish();
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Choose a recipe...");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent startIntent = getIntent();

        int widgetId = startIntent.getIntExtra(Constants.WIDGET_ID, 0);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(RecipeWidgetActivity.this);
        RemoteViews remoteViews = new RemoteViews(RecipeWidgetActivity.this.getPackageName(), R.layout.recipe_widget);
//        ComponentName recipeWidget = new ComponentName(RecipeWidgetActivity.this, RecipeWidgetProvider.class);

        remoteViews.setTextViewText(R.id.recipe_name_text, recipe.getName());

        Intent remoteAdapterIntent = new Intent(RecipeWidgetActivity.this, RecipeWidgetService.class);
        remoteAdapterIntent.setData(RecipeContract.RecipeEntry.buildRecipeUri(recipe.getId()));

        remoteViews.setRemoteAdapter(R.id.ingredient_list, remoteAdapterIntent);


//        Intent recipeIntent = new Intent(this, RecipeDetailActivity.class);
//        recipeIntent.putExtra(Constants.EXTRA_RECIPE, recipe);
//        PendingIntent recipePendingIntent = PendingIntent.getActivity(this, 0, recipeIntent, 0);
//        remoteViews.setOnClickPendingIntent(R.id.ingredient_list, recipePendingIntent);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
        finish();
    }
}
