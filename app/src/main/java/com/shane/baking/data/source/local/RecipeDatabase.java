package com.shane.baking.data.source.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.shane.baking.data.Ingredient;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.Step;

import static com.shane.baking.data.source.local.RecipeDatabase.VERSION_NUMBER;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = VERSION_NUMBER)
public abstract class RecipeDatabase extends RoomDatabase {
    public static final String DB_NAME = "recipes.db";
    public static final int VERSION_NUMBER = 1;

    private static RecipeDatabase INSTANCE;

    public abstract RecipeDao recipeDao();

    public static synchronized RecipeDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
