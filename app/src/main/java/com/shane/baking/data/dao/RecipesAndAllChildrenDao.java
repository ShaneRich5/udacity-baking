package com.shane.baking.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.models.RecipesAndAllChildren;

import java.util.List;

/**
 * Created by Shane on 10/7/2017.
 */
@Dao
public interface RecipesAndAllChildrenDao {

    @Query("SELECT * from " + RecipeEntry.TABLE_NAME)
    List<RecipesAndAllChildren> selectAllRecipesWithRelations();
}
