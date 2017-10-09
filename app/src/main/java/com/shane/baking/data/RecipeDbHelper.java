package com.shane.baking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.data.RecipeContract.StepEntry;

/**
 * Created by Shane on 10/8/2017.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipe.db";

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createRecipeTable(db);
        createIngredientTable(db);
        createStepTable(db);
    }

    private void createStepTable(SQLiteDatabase db) {
        final String CREATE_STATEMENT = "CREATE TABLE " + StepEntry.TABLE_NAME + " (" +
                StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StepEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                StepEntry.COLUMN_SUMMARY + " TEXT NOT NULL, " +
                StepEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                StepEntry.COLUMN_VIDEO_URL + " TEXT, " +
                StepEntry.COLUMN_THUMBNAIL_URL + " TEXT, " +
                StepEntry.COLUMN_RECIPE + " INTEGER NOT NULL);";
        db.execSQL(CREATE_STATEMENT);
    }

    private void createIngredientTable(SQLiteDatabase db) {
        final String CREATE_STATEMENT = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                IngredientEntry.COLUMN_QUALITY + " INTEGER NOT NULL, " +
                IngredientEntry.COLUMN_UNIT + " TEXT NOT NULL, " +
                IngredientEntry.COLUMN_RECIPE + " INTEGER NOT NULL);";
        db.execSQL(CREATE_STATEMENT);
    }

    private void createRecipeTable(SQLiteDatabase db) {
        final String CREATE_STATEMENT = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY," +
                RecipeEntry.COLUMN_NAME + " TEXT NOT NULL," +
                RecipeEntry.COLUMN_SERVINGS + " INTEGER NOT NULL," +
                RecipeEntry.COLUMN_IMAGE_URL + " TEXT);";
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
    }
}