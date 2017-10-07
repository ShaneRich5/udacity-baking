package com.shane.baking.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.data.RecipeContract.StepEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane on 10/7/2017.
 */

public class RecipesAndAllChildren {
    @Embedded
    Recipe recipe;

    @Relation(parentColumn = RecipeEntry._ID, entityColumn = StepEntry.COLUMN_RECIPE, entity = Step.class)
    List<Step> steps = new ArrayList<>();

    @Relation(parentColumn = RecipeEntry._ID, entityColumn = IngredientEntry.COLUMN_RECIPE, entity = Ingredient.class)
    List<Ingredient> ingredients = new ArrayList<>();

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
