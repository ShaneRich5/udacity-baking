package com.shane.baking.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.shane.baking.data.dao.IngredientDao;
import com.shane.baking.data.dao.RecipeDao;
import com.shane.baking.data.dao.RecipesAndAllChildrenDao;
import com.shane.baking.data.dao.StepDao;
import com.shane.baking.models.Ingredient;
import com.shane.baking.models.Recipe;
import com.shane.baking.models.Step;

/**
 * Created by Shane on 10/5/2017.
 */
@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "recipe.db";

    private static RecipeDatabase instance;

    public static synchronized RecipeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDatabase.class, DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract RecipeDao recipeDao();

    public abstract IngredientDao ingredientDao();

    public abstract StepDao stepDao();

    public abstract RecipesAndAllChildrenDao recipesAndAllChildrenDao();
}
