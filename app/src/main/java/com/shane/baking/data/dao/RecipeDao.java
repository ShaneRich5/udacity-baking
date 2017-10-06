package com.shane.baking.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.models.Recipe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Shane on 10/5/2017.
 */
@Dao
public interface RecipeDao {
    @Insert(onConflict = REPLACE)
    int insert(Recipe recipe);

    @Query("SELECT * FROM " + RecipeEntry.TABLE_NAME + " WHERE " + RecipeEntry._ID + " = :id")
    Cursor findById(long id);

    @Update(onConflict = REPLACE)
    int update(Recipe recipe);


}
