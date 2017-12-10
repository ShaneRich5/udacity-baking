package com.shane.baking.data.source.local;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.shane.baking.data.Ingredient;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.data.RecipeContract.StepEntry;
import com.shane.baking.data.Step;

import java.util.List;

public class RecipeWithStepsAndIngredients {
    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = RecipeEntry._ID, entityColumn = IngredientEntry.COLUMN_RECIPE_ID, entity = Ingredient.class)
    public List<Ingredient> ingredients;

    @Relation(parentColumn = RecipeEntry._ID, entityColumn = StepEntry.COLUMN_RECIPE_ID, entity = Step.class)
    public List<Step> steps;
}
