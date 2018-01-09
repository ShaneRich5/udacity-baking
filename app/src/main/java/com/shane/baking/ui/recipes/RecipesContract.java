package com.shane.baking.ui.recipes;

import android.support.annotation.NonNull;

import com.shane.baking.data.Recipe;
import com.shane.baking.ui.base.BasePresenter;
import com.shane.baking.ui.base.BaseView;

import java.util.List;

public interface RecipesContract {

    interface View extends BaseView<Presenter> {

        void showRecipes(@NonNull List<Recipe> recipes);

        void showNoRecipes();

        void showLoadingRecipeError();

        void showRecipeDetailScreen(@NonNull Recipe recipe);

        void showLoadingIndicator(boolean shouldShow);
    }

    interface Presenter extends BasePresenter {

        void loadRecipes(boolean forceUpdate);

        void openRecipeDetail(@NonNull Recipe recipe);
    }
}
