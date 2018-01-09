package com.shane.baking.ui.recipedetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shane.baking.data.Recipe;
import com.shane.baking.data.Step;
import com.shane.baking.ui.base.BasePresenter;
import com.shane.baking.ui.base.BaseView;

import java.util.List;

public interface RecipeDetailContract {

    interface View extends BaseView<Presenter> {
        void showRecipe(@NonNull Recipe recipe);

        void showStepScreen(@NonNull List<Step> steps, @Nullable Step selectedStep);
    }

    interface Presenter extends BasePresenter {
        void openRecipeSteps(@Nullable Step step);
    }
}
