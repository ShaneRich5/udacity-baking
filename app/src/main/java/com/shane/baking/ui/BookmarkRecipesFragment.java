package com.shane.baking.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.data.RecipeContract.StepEntry;
import com.shane.baking.data.Ingredient;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.Step;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkRecipesFragment extends RecipeListFragment {


    public BookmarkRecipesFragment() {}

    @SuppressLint("Recycle")
    @Override
    protected void loadRecipes() {


        Observable.create((ObservableOnSubscribe<Cursor>)
                emitter -> {
                    Cursor cursor = getContext().getContentResolver()
                            .query(RecipeEntry.CONTENT_URI, null, null, null, null);
                    emitter.onNext(cursor);

                })
                .flatMap(new Function<Cursor, ObservableSource<List<Recipe>>>() {
                    @Override
                    public ObservableSource<List<Recipe>> apply(@NonNull Cursor cursor) throws Exception {
                        return new Observable<List<Recipe>>() {
                            @Override
                            protected void subscribeActual(Observer<? super List<Recipe>> observer) {
                                List<Recipe> recipes = new ArrayList<>();

                                while (cursor.moveToNext()) {
                                    long id = cursor.getLong(cursor.getColumnIndex(RecipeEntry._ID));
                                    String name = cursor.getString(cursor.getColumnIndex(RecipeEntry.COLUMN_NAME));
                                    int servings = cursor.getInt(cursor.getColumnIndex(RecipeEntry.COLUMN_SERVINGS));
                                    String imageUrl = cursor.getString(cursor.getColumnIndex(RecipeEntry.COLUMN_IMAGE_URL));

                                    recipes.add(new Recipe(id, name, servings, imageUrl));
                                }

                                observer.onNext(recipes);
                            }
                        };
                    }
                })
                .flatMap(new Function<List<Recipe>, ObservableSource<List<Recipe>>>() {
                    @Override
                    public ObservableSource<List<Recipe>> apply(@NonNull List<Recipe> recipes) throws Exception {
                        return new Observable<List<Recipe>>() {
                            @Override
                            protected void subscribeActual(Observer<? super List<Recipe>> observer) {
                                for (Recipe recipe: recipes) {
                                    Cursor ingredientCursor = getContext().getContentResolver()
                                            .query(IngredientEntry.buildIngredientUri(recipe.getId()), null, null, null, null);

                                    Cursor stepCursor = getContext().getContentResolver()
                                            .query(StepEntry.buildStepUri(recipe.getId()), null, null, null, null);


                                    if (ingredientCursor == null || stepCursor == null) {
                                        observer.onError(new IllegalStateException("Failed to load recipes"));
                                        return;
                                    }

                                    while (ingredientCursor.moveToNext()) {
                                        String name = ingredientCursor.getString(ingredientCursor.getColumnIndex(IngredientEntry.COLUMN_NAME));
                                        String unit = ingredientCursor.getString(ingredientCursor.getColumnIndex(IngredientEntry.COLUMN_UNIT));
                                        double quantity = ingredientCursor.getDouble(ingredientCursor.getColumnIndex(IngredientEntry.COLUMN_QUALITY));
                                        long recipeId = ingredientCursor.getLong(ingredientCursor.getColumnIndex(IngredientEntry.COLUMN_RECIPE));

                                        recipe.getIngredients().add(new Ingredient(name, quantity, unit, recipeId));
                                    }

                                    while (stepCursor.moveToNext()) {
                                        long number = stepCursor.getLong(stepCursor.getColumnIndex(StepEntry.COLUMN_NUMBER));
                                        String description = stepCursor.getString(stepCursor.getColumnIndex(StepEntry.COLUMN_DESCRIPTION));
                                        String summary = stepCursor.getString(stepCursor.getColumnIndex(StepEntry.COLUMN_SUMMARY));
                                        String thumbnailUrl = stepCursor.getString(stepCursor.getColumnIndex(StepEntry.COLUMN_THUMBNAIL_URL));
                                        String videoUrl = stepCursor.getString(stepCursor.getColumnIndex(StepEntry.COLUMN_VIDEO_URL));
                                        long recipeId = stepCursor.getLong(stepCursor.getColumnIndex(StepEntry.COLUMN_RECIPE));

                                        recipe.getSteps().add(new Step(number, summary, description, videoUrl, thumbnailUrl, recipeId));
                                    }
                                }

                                observer.onNext(recipes);
                            }
                        };
                    }
                })
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
}
