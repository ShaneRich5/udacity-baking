package com.shane.baking.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.models.Ingredient;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Shane on 10/5/2017.
 */
@Dao
public interface IngredientDao {
    @Insert(onConflict = REPLACE)
    int insert(Ingredient ingredient);

    @Insert(onConflict = REPLACE)
    int insertAll(Ingredient[] ingredients);

    @Query("SELECT * FROM " + IngredientEntry.TABLE_NAME + " WHERE " +
            IngredientEntry.COLUMN_RECIPE + " = :recipeId")
    Cursor findAllByRecipe(int recipeId);

    @Update(onConflict = REPLACE)
    int update(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Query("DELETE FROM " + IngredientEntry.TABLE_NAME + " WHERE " +
            IngredientEntry.COLUMN_RECIPE + " = :recipeId")
    int deleteByRecipeId(long id);
}
