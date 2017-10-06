package com.shane.baking.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.shane.baking.data.RecipeContract.StepEntry;
import com.shane.baking.models.Step;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Shane on 10/5/2017.
 */
@Dao
public interface StepDao {
    @Insert(onConflict = REPLACE)
    void insert(Step step);

    @Query("SELECT * FROM " + StepEntry.TABLE_NAME + " WHERE " + StepEntry.COLUMN_RECIPE
            + " = :recipeId")
    List<Step> findAllByRecipeId(long recipeId);

    @Update(onConflict = REPLACE)
    void update(Step step);

    @Delete
    int delete(Step step);
}
