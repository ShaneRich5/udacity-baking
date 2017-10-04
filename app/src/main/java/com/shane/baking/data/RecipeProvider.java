package com.shane.baking.data;

import android.content.ContentProvider;
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

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private RecipeDbHelper recipeDbHelper;

    static {
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, RecipeContract.PATH_RECIPE, RECIPE);
        uriMatcher.addURI(authority, RecipeContract.PATH_RECIPE + "/#", RECIPE_ID);

        uriMatcher.addURI(authority, RecipeContract.PATH_INGREDIENT, INGREDIENT);
        uriMatcher.addURI(authority, RecipeContract.PATH_INGREDIENT + "/#", INGREDIENT_ID);

        uriMatcher.addURI(authority, RecipeContract.PATH_STEP, STEP);
        uriMatcher.addURI(authority, RecipeContract.PATH_STEP + "/#", STEP_ID);
    }


    @Override
    public boolean onCreate() {
        recipeDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        long id;
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case RECIPE:
                id = database.insert(RecipeEntry.TABLE_NAME, null, values);
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

    private void notifyChange(@NonNull Uri uri) {
        Context context = getContext();
        if (context == null) return;
        context.getContentResolver().notifyChange(uri, null);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] arguments) {
        final SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
        int numberOfRowsDeleted;

        if (null == selection) selection = "1";

        switch (uriMatcher.match(uri)) {
            case RECIPE:
                numberOfRowsDeleted = database.delete(RecipeEntry.TABLE_NAME, selection, arguments);
                break;
            case RECIPE_ID:
                String movieId = uri.getPathSegments().get(1);
                numberOfRowsDeleted = database.delete(RecipeEntry.TABLE_NAME, RecipeEntry._ID + "=?", new String[]{movieId});
                break;
            case INGREDIENT:
                numberOfRowsDeleted = database.delete(IngredientEntry.TABLE_NAME, selection, arguments);
                break;
            case INGREDIENT_ID:
                String ingredientId = uri.getPathSegments().get(1);
                numberOfRowsDeleted = database.delete(IngredientEntry.TABLE_NAME, IngredientEntry.COLUMN_RECIPE + "=?", new String[]{ingredientId});
                break;
            case STEP:
                numberOfRowsDeleted = database.delete(StepEntry.TABLE_NAME, selection, arguments);
                break;
            case STEP_ID:
                String stepId = uri.getPathSegments().get(1);
                numberOfRowsDeleted = database.delete(StepEntry.TABLE_NAME, StepEntry.COLUMN_RECIPE + "=?", new String[]{stepId});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numberOfRowsDeleted != 0) notifyChange(uri);
        return numberOfRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
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

        switch (uriMatcher.match(uri)) {
            case RECIPE:
                cursor = database.query(RecipeEntry.TABLE_NAME, projection, selection,
                        selectionArguments, null, null, sortOrder);
                break;
            case RECIPE_ID:
                String recipeId = uri.getPathSegments().get(1);
                cursor = database.query(RecipeEntry.TABLE_NAME,
                        projection,
                        RecipeEntry._ID + "=?",
                        new String[]{recipeId},
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT:
                cursor = database.query(IngredientEntry.TABLE_NAME, projection, selection,
                        selectionArguments, null, null, sortOrder);
                break;
            case INGREDIENT_ID:
                String ingredientId = uri.getPathSegments().get(1);
                cursor = database.query(IngredientEntry.TABLE_NAME,
                        projection,
                        IngredientEntry.COLUMN_RECIPE + "=?",
                        new String[]{ingredientId},
                        null,
                        null,
                        sortOrder);
                break;
            case STEP:
                cursor = database.query(StepEntry.TABLE_NAME, projection, selection,
                        selectionArguments, null, null, sortOrder);
                break;
            case STEP_ID:
                String stepId = uri.getPathSegments().get(1);
                cursor = database.query(StepEntry.TABLE_NAME,
                        projection,
                        StepEntry.COLUMN_RECIPE + "=?",
                        new String[]{stepId},
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

        switch (uriMatcher.match(uri)) {
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
}
