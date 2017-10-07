package com.shane.baking.ui;

import android.view.View;

import com.shane.baking.R;
import com.shane.baking.network.RecipeApi;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shane on 10/6/2017.
 */

public class BrowseRecipesFragment extends RecipeListFragment {

    public BrowseRecipesFragment() {
    }

    @Override
    protected void loadRecipes() {
        RecipeApi recipeApi = RecipeApi.Factory.create();
        recipeApi.getRecipe()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(recipeObserver);
    }

    @OnClick(R.id.error_button)
    public void onErrorButtonClick(View view) {
        loadRecipes();
    }
}
