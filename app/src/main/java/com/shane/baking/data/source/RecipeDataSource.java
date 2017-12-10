package com.shane.baking.data.source;

import android.support.annotation.NonNull;

import com.shane.baking.data.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface RecipeDataSource {

    Flowable<List<Recipe>> getRecipes();

    Flowable<Maybe<Recipe>> getRecipe(long id);

    void refreshRecipes();

    Completable saveRecipe(@NonNull Recipe recipe);
}
