package com.shane.baking.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.data.RecipeContract.StepEntry;

public class RecipeProvider extends ContentProvider {
    private static final int RECIPE = 100;
    private static final int RECIPE_ID = 101;
    private static final int INGREDIENT = 200;
    private static final int INGREDIENT_ID = 201;
    private static final int STEP = 300;
    private static final int STEP_ID = 301;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private Context context;

    private RecipeDbHelper recipeDbHelper;

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
        recipeDbHelper = new RecipeDbHelper(getContext());
        return true;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        long id;
        Uri returnUri;

        switch (URI_MATCHER.match(uri)) {
            case RECIPE:
                Recipe recipe = Recipe.fromContentValues(values);

                values.remove(RecipeEntry.COLUMN_STEPS);
                values.remove(RecipeEntry.COLUMN_INGREDIENTS);

                id = database.insert(RecipeEntry.TABLE_NAME, null, values);

                for (Ingredient ingredient : recipe.getIngredients()) {
                    ingredient.setRecipeId(recipe.getId());
                    insert(IngredientEntry.CONTENT_URI, ingredient.toContentValues());
                }

                for (Step step : recipe.getSteps()) {
                    step.setRecipeId(recipe.getId());
                    insert(StepEntry.CONTENT_URI, step.toContentValues());
                }

                if (id > 0) {
                    returnUri = RecipeEntry.buildRecipeUri(id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }
                break;
            case INGREDIENT:
                id = database.insert(IngredientEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = IngredientEntry.buildIngredientUri(id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }
                break;
            case STEP:
                id = database.insert(StepEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = StepEntry.buildStepUri(id);
                } else {
                    throw new SQLException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        notifyChange(uri);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] arguments) {
        final SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        int numberOfRowsDeleted;

        if (null == selection) selection = "1";

        switch (URI_MATCHER.match(uri)) {
            case RECIPE:
                numberOfRowsDeleted = database.delete(RecipeEntry.TABLE_NAME, selection, arguments);
                break;
            case RECIPE_ID:
                String recipeId = uri.getPathSegments().get(1);
                numberOfRowsDeleted = database.delete(RecipeEntry.TABLE_NAME, RecipeEntry._ID + "=?", new String[]{recipeId});
                delete(IngredientEntry.buildIngredientUri(ContentUris.parseId(uri)), null, null);
                delete(StepEntry.buildStepUri(ContentUris.parseId(uri)), null, null);
                break;
            case INGREDIENT:
                numberOfRowsDeleted = database.delete(IngredientEntry.TABLE_NAME, selection, arguments);
                break;
            case INGREDIENT_ID:
                String ingredientId = uri.getPathSegments().get(1);
                numberOfRowsDeleted = database.delete(IngredientEntry.TABLE_NAME, IngredientEntry.COLUMN_RECIPE_ID + "=?", new String[]{ingredientId});
                break;
            case STEP:
                numberOfRowsDeleted = database.delete(StepEntry.TABLE_NAME, selection, arguments);
                break;
            case STEP_ID:
                String stepId = uri.getPathSegments().get(1);
                numberOfRowsDeleted = database.delete(StepEntry.TABLE_NAME, StepEntry.COLUMN_RECIPE_ID + "=?", new String[]{stepId});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsDeleted != 0) notifyChange(uri);
        return numberOfRowsDeleted;
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



    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArguments, String sortOrder) {
        final SQLiteDatabase database = recipeDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (URI_MATCHER.match(uri)) {
            case RECIPE:
                cursor = database.query(RecipeEntry.TABLE_NAME, projection, selection,
                        selectionArguments, null, null, sortOrder);
                break;
            case RECIPE_ID:
                cursor = database.query(RecipeEntry.TABLE_NAME, projection,
                        IngredientEntry._ID + "=?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT_ID:
                cursor = database.query(IngredientEntry.TABLE_NAME,
                        projection,
                        IngredientEntry.COLUMN_RECIPE_ID + "=?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;
            case STEP_ID:
                cursor = database.query(StepEntry.TABLE_NAME,
                        projection,
                        StepEntry.COLUMN_RECIPE_ID + "=?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
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
        final SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        int numberOfRowsUpdated;

        switch (URI_MATCHER.match(uri)) {
            case RECIPE:
                numberOfRowsUpdated = database
                        .update(RecipeEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case INGREDIENT:
                numberOfRowsUpdated = database
                        .update(IngredientEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case STEP:
                numberOfRowsUpdated = database
                        .update(StepEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsUpdated != 0) {
            notifyChange(uri);
        }

        return numberOfRowsUpdated;
    }
    private void notifyChange(@NonNull Uri uri) {
        if (context == null) return;
        context.getContentResolver().notifyChange(uri, null);
    }
}
