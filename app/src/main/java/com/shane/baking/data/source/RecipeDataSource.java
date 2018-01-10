package com.shane.baking.data.source;

import android.support.annotation.NonNull;

import com.shane.baking.data.Ingredient;
import com.shane.baking.data.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface RecipeDataSource {

    Flowable<List<Recipe>> getRecipes();

    Flowable<Recipe> getRecipe(long id);

    Flowable<List<Ingredient>> getIngredientsForRecipe(long recipeId);

    void refreshRecipes();

    Completable saveRecipe(@NonNull Recipe recipe);
}
