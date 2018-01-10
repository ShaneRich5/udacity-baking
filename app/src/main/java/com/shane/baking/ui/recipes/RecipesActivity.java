package com.shane.baking.ui.recipes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.Toolbar;

import com.shane.baking.R;
import com.shane.baking.adapters.RecipeAdapter;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.source.RecipeDataSource;
import com.shane.baking.data.source.RecipeRepository;
import com.shane.baking.data.source.local.RecipeDatabase;
import com.shane.baking.data.source.local.RecipeLocalDataSource;
import com.shane.baking.data.source.remote.RecipeRemoteDataSource;
import com.shane.baking.network.RecipeApi;
import com.shane.baking.ui.base.BaseActivity;
import com.shane.baking.utils.SimpleIdlingResource;

import butterknife.BindView;

public class RecipesActivity extends BaseActivity implements RecipeAdapter.OnClickHandler {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    RecipesContract.Presenter presenter;

    @Nullable
    private SimpleIdlingResource idlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        setSupportActionBar(toolbar);
        getIdlingResource();

        RecipeDatabase recipeDatabase = RecipeDatabase.getInstance(this);

        RecipeDataSource localDataSource = RecipeLocalDataSource.getInstance(recipeDatabase.recipeDao());
        RecipeDataSource remoteDataSource = RecipeRemoteDataSource.getInstance(RecipeApi.Factory.create());

        RecipeRepository repository = RecipeRepository.getInstance(remoteDataSource, localDataSource);

        RecipesFragment recipesFragment = (RecipesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);

        presenter = new RecipesPresenter(recipesFragment, repository, getIdlingResource());
    }

    @Override
    public void onClick(Recipe recipe) {
        presenter.openRecipeDetail(recipe);
    }
}
