package com.shane.baking.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RemoteViews;

import com.shane.baking.R;
import com.shane.baking.adapters.RecipeAdapter;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.RecipeContract;
import com.shane.baking.data.source.RecipeDataSource;
import com.shane.baking.data.source.RecipeRepository;
import com.shane.baking.data.source.local.RecipeDatabase;
import com.shane.baking.data.source.local.RecipeLocalDataSource;
import com.shane.baking.data.source.remote.RecipeRemoteDataSource;
import com.shane.baking.network.RecipeApi;
import com.shane.baking.ui.base.BaseActivity;
import com.shane.baking.ui.recipedetail.RecipeDetailActivity;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RecipeChoiceActivity extends BaseActivity implements RecipeAdapter.OnClickHandler {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_choice);
        setSupportActionBar(toolbar);

        recipeAdapter = new RecipeAdapter(this, this, true);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setAdapter(recipeAdapter);

        RecipeDatabase recipeDatabase = RecipeDatabase.getInstance(this);

        RecipeDataSource localDataSource = RecipeLocalDataSource.getInstance(recipeDatabase.recipeDao());
        RecipeDataSource remoteDataSource = RecipeRemoteDataSource.getInstance(RecipeApi.Factory.create());

        RecipeRepository repository = RecipeRepository.getInstance(remoteDataSource, localDataSource);

        Disposable disposable = repository.getRecipes()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipes -> recipeAdapter.setRecipes(recipes), Timber::e);

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent startingIntent = getIntent();
        int widgetId = startingIntent.getIntExtra(RecipeWidgetProvider.WIDGET_ID, 0);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.recipe_widget);

        remoteViews.setTextViewText(R.id.recipe_name_text, recipe.getName());

        Intent remoteAdapterIntent = new Intent(this, RecipeWidgetService.class);
        remoteAdapterIntent.setData(RecipeContract.RecipeEntry.buildRecipeUri(recipe.getId()));

        remoteViews.setRemoteAdapter(R.id.ingredient_list, remoteAdapterIntent);

        Intent recipeIntent = new Intent(this, RecipeDetailActivity.class);
        recipeIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);

        PendingIntent recipePendingIntent = PendingIntent.getActivity(this, (int) recipe.getId(), recipeIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.container, recipePendingIntent);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
        finish();
    }
}
