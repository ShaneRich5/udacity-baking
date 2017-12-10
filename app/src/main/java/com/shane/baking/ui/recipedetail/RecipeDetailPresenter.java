package com.shane.baking.ui.recipedetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shane.baking.data.Recipe;
import com.shane.baking.data.Step;

import io.reactivex.disposables.CompositeDisposable;

public class RecipeDetailPresenter implements RecipeDetailContract.Presenter {

    @NonNull
    private RecipeDetailContract.View recipeDetailView;

    @NonNull
    private CompositeDisposable compositeDisposable;

    @NonNull
    private Recipe recipe;

    RecipeDetailPresenter(@NonNull RecipeDetailContract.View view,
                          @NonNull Recipe recipe) {
        this.recipe = recipe;
        recipeDetailView = view;
        recipeDetailView.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        recipeDetailView.showRecipe(recipe);
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void openRecipeSteps(@Nullable Step step) {
        recipeDetailView.showStepScreen(recipe.getSteps(), step);
    }
}
