package com.shane.baking.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.shane.baking.data.Ingredient;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.data.Step;

import java.util.List;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {

    @Insert(onConflict = REPLACE)
    void insertAll(List<Recipe> recipes);

    @Insert(onConflict = REPLACE)
    void insert(Recipe recipe);

    @Insert(onConflict = REPLACE)
    void insertIngredients(List<Ingredient> ingredients);

    @Insert(onConflict = REPLACE)
    void insertSteps(List<Step> steps);

    @Query("SELECT * FROM " + IngredientEntry.TABLE_NAME + " WHERE " + IngredientEntry.COLUMN_RECIPE_ID + " = :recipeId")
    Flowable<List<Ingredient>> getAllIngredientsByRecipeId(long recipeId);

    @Query("SELECT * FROM " + RecipeEntry.TABLE_NAME)
    Flowable<List<Recipe>> getAll();

    @Transaction
    @Query("SELECT * FROM " + RecipeEntry.TABLE_NAME)
    Flowable<List<RecipeWithStepsAndIngredients>> getAllRecipesWithRelations();

    @Query("SELECT * FROM " + RecipeEntry.TABLE_NAME + " WHERE " + RecipeEntry._ID + " = :id")
    Flowable<Recipe> getById(long id);

    @Transaction
    @Query("SELECT * FROM " + RecipeEntry.TABLE_NAME + " WHERE " + RecipeEntry._ID + " = :id")
    Flowable<RecipeWithStepsAndIngredients> getRecipeWithRelations(long id);

    @Query("DELETE FROM " + RecipeEntry.TABLE_NAME)
    void deleteAll();
}
