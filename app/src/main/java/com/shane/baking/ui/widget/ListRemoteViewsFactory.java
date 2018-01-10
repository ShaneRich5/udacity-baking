package com.shane.baking.ui.widget;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shane.baking.R;
import com.shane.baking.data.Ingredient;
import com.shane.baking.data.source.RecipeDataSource;
import com.shane.baking.data.source.RecipeRepository;
import com.shane.baking.data.source.local.RecipeDatabase;
import com.shane.baking.data.source.local.RecipeLocalDataSource;
import com.shane.baking.data.source.remote.RecipeRemoteDataSource;
import com.shane.baking.network.RecipeApi;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public static final String TAG = ListRemoteViewsFactory.class.getName();

    private final Context context;
    private final long recipeId;
    private RecipeRepository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<Ingredient> ingredients = new LinkedList<>();

    public ListRemoteViewsFactory(@NonNull Context context, @NonNull Intent intent) {
        this.context = context;
        this.recipeId = ContentUris.parseId(intent.getData());

        RecipeDatabase recipeDatabase = RecipeDatabase.getInstance(context);

        RecipeDataSource localDataSource = RecipeLocalDataSource.getInstance(recipeDatabase.recipeDao());
        RecipeDataSource remoteDataSource = RecipeRemoteDataSource.getInstance(RecipeApi.Factory.create());

        repository = RecipeRepository.getInstance(remoteDataSource, localDataSource);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        Disposable disposable = repository.getIngredientsForRecipe(recipeId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(newIngredients -> {
                    ingredients = newIngredients;
//                    Toast.makeText(context, "ingredients: " + ingredients, Toast.LENGTH_SHORT).show();
                }, Timber::e);
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int index) {
        Ingredient ingredient = ingredients.get(index);

        Timber.i("ingredient: %s", ingredient);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_ingredient_widget);

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
    public long getItemId(int index) {
        Ingredient ingredient = ingredients.get(index);
        return ingredient.getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
