package com.shane.baking.ui;

import com.shane.baking.models.Recipe;
import com.shane.baking.network.RecipeApi;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Shane on 10/6/2017.
 */

public class BrowseRecipesFragment extends RecipeListFragment {

    public BrowseRecipesFragment() {
    }

    @Override
    protected void loadRecipes() {
        RecipeApi recipeApi = RecipeApi.Factory.create();
        recipeApi.getRecipe()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(recipeObserver);
    }

    Observer<List<Recipe>> recipeObserver = new Observer<List<Recipe>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull List<Recipe> recipes) {
            addRecipesToAdapter(recipes);
        }

        @Override
        public void onError(@NonNull Throwable error) {
            Timber.e(error);
        }

        @Override
        public void onComplete() {

        }
    };
}
