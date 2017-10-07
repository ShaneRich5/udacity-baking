package com.shane.baking.widget;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shane.baking.R;
import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Shane on 10/7/2017.
 */

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public static final String TAG = ListRemoteViewsFactory.class.getName();

    private final Context context;
    private final long recipeId;
    private List<Ingredient> ingredients = new ArrayList<>();

    ListRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        recipeId = ContentUris.parseId(intent.getData());
        Timber.i("constructor");
    }

    @Override
    public void onCreate() {
        Timber.tag(TAG);
        Timber.i("onCreate");
    }

    @Override
    public void onDataSetChanged() {
        Timber.i("onDataSetChanged");
        final Uri ingredientUri = IngredientEntry.buildIngredientUri(recipeId);
        final Cursor cursor = context.getContentResolver().query(ingredientUri,
                null, null, null, IngredientEntry._ID);
        ingredients = mapCursorToIngredients(cursor);
        Timber.i(ingredients.toString());
        Timber.i(ingredientUri.toString());
    }

    private List<Ingredient> mapCursorToIngredients(Cursor cursor) {
        Timber.i("mapCursor");
        List<Ingredient> ingredients = new ArrayList<>();
        if (cursor == null) return ingredients;

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(IngredientEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(IngredientEntry.COLUMN_NAME));
            double quantity = cursor.getDouble(cursor.getColumnIndex(IngredientEntry.COLUMN_QUALITY));
            String unit = cursor.getString(cursor.getColumnIndex(IngredientEntry.COLUMN_UNIT));

            ingredients.add(new Ingredient(id, name, quantity, unit));
        }
        return ingredients;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Timber.i(ingredients.toString());
        Timber.i("getCount" + ingredients.size());
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int index) {
        Timber.i("getViewAt " + index);
        Ingredient ingredient = ingredients.get(index);
        Timber.i("ingredient: " + ingredient.toString());
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_widget_ingredient_list);

        String amountText = context.getResources().getString(R.string.amount_item,
                String.valueOf(ingredient.getQuantity()),
                ingredient.getUnit().toLowerCase());

        remoteViews.setTextViewText(R.id.ingredient_name, ingredient.getName());
        remoteViews.setTextViewText(R.id.ingredient_amount, amountText);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}