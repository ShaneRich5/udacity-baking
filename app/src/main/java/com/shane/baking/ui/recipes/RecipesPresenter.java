package com.shane.baking.ui.recipes;

import android.support.annotation.NonNull;

import com.shane.baking.data.Recipe;
import com.shane.baking.data.source.RecipeDataSource;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RecipesPresenter implements RecipesContract.Presenter {

    @NonNull
    private RecipesContract.View recipesView;

    @NonNull
    private CompositeDisposable compositeDisposable;

    @NonNull
    RecipeDataSource repository;

    private boolean isFirstLoad = true;

    RecipesPresenter(@NonNull RecipesContract.View view,
                     @NonNull RecipeDataSource repository) {
        recipesView = view;
        recipesView.setPresenter(this);
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        loadRecipes(false);
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void loadRecipes(boolean forceUpdate) {
        loadRecipes(forceUpdate || isFirstLoad, true);
        isFirstLoad = false;
    }

    private void loadRecipes(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            recipesView.showLoadingIndicator(true);
        }

        if (forceUpdate) {
            repository.refreshRecipes();
        }

        compositeDisposable.clear();

        Disposable disposable = repository.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipes -> {
                    Timber.i(recipes.toString());
                    if (recipes.isEmpty()) {
                        recipesView.showNoRecipes();
                    } else {
                        recipesView.showRecipes(recipes);
                    }
                }, throwable -> {
                    Timber.e(throwable);
                    recipesView.showLoadingRecipeError();
                }, () -> {
                    recipesView.showLoadingIndicator(false);
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void openRecipeDetail(@NonNull Recipe recipe) {
        recipesView.showRecipeDetailScreen(recipe);
    }
}
