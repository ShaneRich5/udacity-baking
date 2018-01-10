package com.shane.baking.data.source.remote;

import android.support.annotation.NonNull;

import com.shane.baking.data.Ingredient;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.source.RecipeDataSource;
import com.shane.baking.network.RecipeApi;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

public class RecipeRemoteDataSource implements RecipeDataSource {

    private static RecipeRemoteDataSource INSTANCE;

    @NonNull
    private RecipeApi recipeApi;

    public RecipeRemoteDataSource(@NonNull RecipeApi recipeApi) {
        this.recipeApi = recipeApi;
    }

    public static RecipeRemoteDataSource getInstance(@NonNull RecipeApi recipeApi) {
        if (INSTANCE == null) {
            INSTANCE = new RecipeRemoteDataSource(recipeApi);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Flowable<List<Recipe>> getRecipes() {
        return recipeApi.getRecipes();
    }

    @Override
    public Flowable<Recipe> getRecipe(long id) {
        return getRecipes().flatMap(new Function<List<Recipe>, Publisher<Recipe>>() {
            @Override
            public Publisher<Recipe> apply(List<Recipe> recipes) throws Exception {
                for (Recipe recipe : recipes) {
                    if (recipe.getId() == id) {
                        return Flowable.just(recipe);
                    }
                }

                return Flowable.empty();
            }
        });
    }

    @Override
    public Flowable<List<Ingredient>> getIngredientsForRecipe(long recipeId) {
        return getRecipe(recipeId).map(Recipe::getIngredients);
    }

    @Override
    public void refreshRecipes() {
        getRecipes();
    }

    @Override
    public Completable saveRecipe(@NonNull Recipe recipe) {
        // static json feed being used, no save functionality permitted
        return Completable.complete();
    }
}
