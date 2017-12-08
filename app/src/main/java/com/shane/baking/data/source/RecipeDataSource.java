package com.shane.baking.data.source;

import com.shane.baking.data.Recipe;

import java.util.List;
import java.util.Optional;

import io.reactivex.Flowable;

public interface RecipeDataSource {

    Flowable<List<Recipe>> getRecipes();

    Flowable<Optional<Recipe>> getRecipe(int id);

    void refreshTasks();
}
