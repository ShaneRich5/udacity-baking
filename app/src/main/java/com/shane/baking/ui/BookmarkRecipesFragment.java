package com.shane.baking.ui;


import android.content.Context;
import android.support.v4.app.Fragment;

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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
                .map(new Function<List<RecipesAndAllChildren>, List<Recipe>>() {
                    @Override
                    public List<Recipe> apply(@NonNull List<RecipesAndAllChildren> recipeElements) throws Exception {
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
                    }
                })
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
