package com.shane.baking.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.data.RecipeContract.StepEntry;
import com.shane.baking.data.dao.IngredientDao;
import com.shane.baking.data.dao.RecipeDao;
import com.shane.baking.data.dao.StepDao;
import com.shane.baking.models.Ingredient;
import com.shane.baking.models.Recipe;
import com.shane.baking.models.Step;

import timber.log.Timber;

public class RecipeProvider extends ContentProvider {
    private static final int RECIPE = 100;
    private static final int RECIPE_ID = 101;
    private static final int INGREDIENT = 200;
    private static final int INGREDIENT_ID = 201;
    private static final int STEP = 300;
    private static final int STEP_ID = 301;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private Context context;
    private StepDao stepDao;
    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;

    static {
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        URI_MATCHER.addURI(authority, RecipeContract.PATH_RECIPE, RECIPE);
        URI_MATCHER.addURI(authority, RecipeContract.PATH_RECIPE + "/#", RECIPE_ID);

        URI_MATCHER.addURI(authority, RecipeContract.PATH_INGREDIENT, INGREDIENT);
        URI_MATCHER.addURI(authority, RecipeContract.PATH_INGREDIENT + "/#", INGREDIENT_ID);

        URI_MATCHER.addURI(authority, RecipeContract.PATH_STEP, STEP);
        URI_MATCHER.addURI(authority, RecipeContract.PATH_STEP + "/#", STEP_ID);
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        if (context == null) return false;
        recipeDao = RecipeDatabase.getInstance(context).recipeDao();
        ingredientDao = RecipeDatabase.getInstance(context).ingredientDao();
        stepDao = RecipeDatabase.getInstance(context).stepDao();
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (URI_MATCHER.match(uri)) {
            case RECIPE:
                Recipe recipe = Recipe.fromContentValues(values);
                final long id = recipeDao.insert(recipe);

                recipe.getSteps().forEach(step -> step.setRecipeId(recipe.getId()));
                recipe.getIngredients().forEach(ingredient -> ingredient.setRecipeId(recipe.getId()));

                stepDao.insertAll(recipe.getSteps());
                ingredientDao.insertAll(recipe.getIngredients());

                Timber.tag(RecipeProvider.class.getName()).i(recipe.getIngredients().toString());
                Timber.tag(RecipeProvider.class.getName()).i(recipe.getSteps().toString());

                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] arguments) {
        int numberOfRowsDeleted;

        switch (URI_MATCHER.match(uri)) {
            case RECIPE_ID:
                numberOfRowsDeleted = recipeDao.deleteById(ContentUris.parseId(uri));
                break;
            case INGREDIENT_ID:
                numberOfRowsDeleted = ingredientDao.deleteByRecipeId(ContentUris.parseId(uri));
                break;
            case STEP_ID:
                numberOfRowsDeleted = stepDao.deleteByRecipeId(ContentUris.parseId(uri));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsDeleted != 0) notifyChange(uri);
        return numberOfRowsDeleted;
    }



    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArguments, String sortOrder) {
        Cursor cursor;

        switch (URI_MATCHER.match(uri)) {
            case RECIPE:
                cursor = recipeDao.selectAll();
                break;
            case RECIPE_ID:
                cursor = recipeDao.selectById(ContentUris.parseId(uri));
                break;
            case INGREDIENT:
                cursor = ingredientDao.selectAll();
                break;
            case INGREDIENT_ID:
                cursor = ingredientDao.selectAllByRecipeId(ContentUris.parseId(uri));
                break;
            case STEP:
                cursor = stepDao.selectAll();
                break;
            case STEP_ID:
                cursor = stepDao.selectById(ContentUris.parseId(uri));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        notifyChange(uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (context == null) return 0;
        int count;

        switch (URI_MATCHER.match(uri)) {
            case RECIPE_ID:
                Recipe recipe = Recipe.fromContentValues(values);
                recipe.setId(ContentUris.parseId(uri));
                count = recipeDao.update(recipe);
                break;
            case INGREDIENT_ID:
                Ingredient ingredient = Ingredient.fromContentValues(values);
                ingredient.setId(ContentUris.parseId(uri));
                count = ingredientDao.update(ingredient);
                break;
            case STEP_ID:
                Step step = Step.fromContentValues(values);
                step.setId(ContentUris.parseId(uri));
                count = stepDao.update(step);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (count != 0) notifyChange(uri);
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case RECIPE:
                return RecipeEntry.CONTENT_TYPE;
            case RECIPE_ID:
                return RecipeEntry.CONTENT_ITEM_TYPE;
            case INGREDIENT:
                return IngredientEntry.CONTENT_TYPE;
            case INGREDIENT_ID:
                return IngredientEntry.CONTENT_ITEM_TYPE;
            case STEP:
                return StepEntry.CONTENT_TYPE;
            case STEP_ID:
                return StepEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private void notifyChange(@NonNull Uri uri) {
        if (context == null) return;
        context.getContentResolver().notifyChange(uri, null);
    }

}
