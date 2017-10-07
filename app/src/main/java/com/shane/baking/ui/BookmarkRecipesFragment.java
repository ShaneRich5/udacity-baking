package com.shane.baking.ui;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shane.baking.data.RecipeDatabase;
import com.shane.baking.data.dao.RecipesAndAllChildrenDao;
import com.shane.baking.models.Ingredient;
import com.shane.baking.models.Recipe;
import com.shane.baking.models.RecipesAndAllChildren;
import com.shane.baking.models.Step;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkRecipesFragment extends RecipeListFragment {

    RecipesAndAllChildrenDao recipeDao;

    public BookmarkRecipesFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        recipeDao = RecipeDatabase.getInstance(context).recipesAndAllChildrenDao();
    }

    @Override
    protected void loadRecipes() {
        Observable.create((ObservableOnSubscribe<List<RecipesAndAllChildren>>)
                emitter -> emitter.onNext(recipeDao.selectAllRecipesWithRelations()))
            .map(RECIPE_MAPPER)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(recipeObserver);
    }

    @Override
    protected void handleError(Throwable error) {
        super.handleError(error);
        errorButton.setVisibility(View.GONE);
    }

    @Override
    protected void handleRecipesLoaded(@NonNull List<Recipe> recipes) {
        super.handleRecipesLoaded(recipes);
        if (recipes.size() > 0) return;

        errorContainer.setVisibility(View.VISIBLE);
        errorMessageTextView.setText("No Recipes Bookmarked");
        errorButton.setText("Add Recipes");
        Context context = getContext();

        errorButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });
    }

    final Function<List<RecipesAndAllChildren>, List<Recipe>> RECIPE_MAPPER = recipeElements -> {
        List<Recipe> recipes = new ArrayList<>();

        for (RecipesAndAllChildren recipeElement : recipeElements) {
            Recipe recipe = recipeElement.getRecipe();
            List<Ingredient> ingredients = recipeElement.getIngredients();
            List<Step> steps = recipeElement.getSteps();

            if (ingredients != null) {
                recipe.setIngredients(ingredients);
            }

            if (steps != null) {
                recipe.setSteps(steps);
            }

            recipes.add(recipe);
        }

        return recipes;
    };
}
