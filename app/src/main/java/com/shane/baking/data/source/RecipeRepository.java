package com.shane.baking.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shane.baking.data.Ingredient;
import com.shane.baking.data.Recipe;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class RecipeRepository implements RecipeDataSource {

    @Nullable
    private static RecipeRepository INSTANCE = null;

    @NonNull
    private final RecipeDataSource recipeLocalDataSource;

    @NonNull
    private final RecipeDataSource recipeRemoteDataSource;

    @Nullable
    Map<Long, Recipe> cachedRecipes;

    boolean cacheIsDirty = false;

    private RecipeRepository(@NonNull RecipeDataSource recipeRemoteDataSource,
                             @NonNull RecipeDataSource recipeLocalDataSource) {
        this.recipeRemoteDataSource = recipeRemoteDataSource;
        this.recipeLocalDataSource = recipeLocalDataSource;
    }

    public static RecipeRepository getInstance(@NonNull RecipeDataSource recipeRemoteDataSource,
                                               @NonNull RecipeDataSource recipeLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipeRepository(recipeRemoteDataSource, recipeLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Flowable<List<Recipe>> getRecipes() {
        if (cachedRecipes != null && ! cacheIsDirty) {
            return Flowable.fromIterable(cachedRecipes.values()).toList().toFlowable();
        } else if (cachedRecipes == null) {
            cachedRecipes = new LinkedHashMap<>();
        }

        Flowable<List<Recipe>> remoteRecipes = getAndSaveRemoteRecipes();

        if (cacheIsDirty) {
            return remoteRecipes;
        } else {
            Flowable<List<Recipe>> localRecipes = getAndCacheLocalRecipes();
            return Flowable.concat(remoteRecipes, localRecipes)
                    .filter(recipes -> ! recipes.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }

    private Flowable<List<Recipe>> getAndSaveRemoteRecipes() {
        return recipeRemoteDataSource.getRecipes()
                .flatMap(recipes -> Flowable.fromIterable(recipes).doOnNext(recipe -> {
                    recipeLocalDataSource.saveRecipe(recipe);
                    cachedRecipes.put(recipe.getId(), recipe);
                }).toList().toFlowable())
                .doOnComplete(() -> cacheIsDirty = false);
    }

    private Flowable<List<Recipe>> getAndCacheLocalRecipes() {
        return recipeLocalDataSource.getRecipes()
                .flatMap(recipes -> Flowable.fromIterable(recipes)
                .doOnNext(recipe -> cachedRecipes.put(recipe.getId(), recipe))
                .toList()
                .toFlowable());
    }

    @Override
    public Flowable<Recipe> getRecipe(long id) {
        final Recipe cachedRecipe = getRecipeFromCacheWithId(id);

        if (cachedRecipe != null) {
            return Flowable.just(cachedRecipe);
        }

        if (cachedRecipes == null) {
            cachedRecipes = new HashMap<>();
        }

        Flowable<Recipe> localRecipe = recipeLocalDataSource
                .getRecipe(id);

        Flowable<Recipe> remoteRecipe = recipeRemoteDataSource
                .getRecipe(id).doOnNext(recipeLocalDataSource::saveRecipe);

        return Flowable.concat(localRecipe, remoteRecipe)
                .filter(recipe -> recipe != null)
                .firstOrError()
                .toFlowable();
    }

    @Nullable
    private Recipe getRecipeFromCacheWithId(long id) {
        if (cachedRecipes == null || cachedRecipes.isEmpty()) {
            return null;
        } else {
            return cachedRecipes.get(id);
        }
    }

    @Override
    public Flowable<List<Ingredient>> getIngredientsForRecipe(long recipeId) {
        return getRecipe(recipeId).map(Recipe::getIngredients);
    }

    @Override
    public void refreshRecipes() {
        cacheIsDirty = false;
    }

    @Override
    public Completable saveRecipe(@NonNull Recipe recipe) {
        return recipeLocalDataSource.saveRecipe(recipe);
    }
}
