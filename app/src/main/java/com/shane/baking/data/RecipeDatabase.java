package com.shane.baking.data;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

import com.shane.baking.models.Ingredient;
import com.shane.baking.models.Recipe;
import com.shane.baking.models.Step;

/**
 * Created by Shane on 10/5/2017.
 */
@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public class RecipeDatabase extends RoomDatabase {
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
