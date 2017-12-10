package com.shane.baking.data.source.remote;

import android.support.annotation.NonNull;

import com.shane.baking.data.Recipe;
import com.shane.baking.data.source.RecipeDataSource;
import com.shane.baking.network.RecipeApi;

import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

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
    public Flowable<Maybe<Recipe>> getRecipe(long id) {
        return getRecipes().flatMap(recipes -> new Flowable<Maybe<Recipe>>() {
            @Override
            protected void subscribeActual(Subscriber<? super Maybe<Recipe>> sub) {
                for (Recipe recipe : recipes) {
                    if (recipe.getId() == id) {
                        sub.onNext(Maybe.just(recipe));
                        return;
                    }
                }
                sub.onNext(Maybe.empty());
            }
        });
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
