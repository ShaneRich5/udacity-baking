package com.shane.baking.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shane.baking.data.Ingredient;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.Step;
import com.shane.baking.data.source.RecipeDataSource;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

public class RecipeLocalDataSource implements RecipeDataSource {

    @Nullable
    private static volatile RecipeLocalDataSource INSTANCE;

    @NonNull
    private RecipeDao recipeDao;

    private RecipeLocalDataSource(@NonNull RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    public static RecipeLocalDataSource getInstance(@NonNull RecipeDao recipeDao) {
        if (INSTANCE == null) {
            synchronized (RecipeLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeLocalDataSource(recipeDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<Recipe>> getRecipes() {
        return recipeDao.getAllRecipesWithRelations()
                .map(DATA_TO_RECIPES_MAPPER);
    }

    Function<List<RecipeWithStepsAndIngredients>, List<Recipe>> DATA_TO_RECIPES_MAPPER = (recipesWithRelations) -> {
        List<Recipe> recipes = new ArrayList<>();

        for (RecipeWithStepsAndIngredients recipeWithStepsAndIngredients : recipesWithRelations) {
            Recipe recipe = recipeWithStepsAndIngredients.recipe;
            recipe.setIngredients(recipeWithStepsAndIngredients.ingredients);
            recipe.setSteps(recipeWithStepsAndIngredients.steps);
            recipes.add(recipe);
        }

        return recipes;
    };

    @Override
    public Flowable<Maybe<Recipe>> getRecipe(long id) {
        return recipeDao.getRecipeWithRelations(id).flatMap(recipeWithRelations -> new Flowable<Maybe<Recipe>>() {
            @Override
            protected void subscribeActual(Subscriber<? super Maybe<Recipe>> emitter) {
                if (recipeWithRelations == null) {
                    Maybe.empty();
                    return;
                }

                Recipe recipe = recipeWithRelations.recipe;
                recipe.setSteps(recipeWithRelations.steps);
                recipe.setIngredients(recipeWithRelations.ingredients);

                Maybe.just(recipe);
            }
        });
    }

    @Override
    public void refreshRecipes() {
        // implemented in repository layer
    }

    @Override
    public Completable saveRecipe(@NonNull Recipe recipe) {
        return Completable.create(emitter -> {
           recipeDao.insert(recipe);
           saveRecipeIngredients(recipe.getId(), recipe.getIngredients());
           saveRecipeSteps(recipe.getId(), recipe.getSteps());
        });
    }

    private void saveRecipeSteps(long id, @NonNull List<Step> steps) {
        for (Step step : steps) {
            step.setRecipeId(id);
        }
        recipeDao.insertSteps(steps);
    }

    private void saveRecipeIngredients(long id, @NonNull List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            ingredient.setRecipeId(id);
        }
        recipeDao.insertIngredients(ingredients);
    }
}
