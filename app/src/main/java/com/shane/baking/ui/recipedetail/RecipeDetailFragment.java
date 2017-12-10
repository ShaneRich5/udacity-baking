package com.shane.baking.ui.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shane.baking.R;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.Step;
import com.shane.baking.ui.BaseFragment;
import com.shane.baking.ui.steps.StepActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailFragment extends BaseFragment implements RecipeDetailContract.View {

    private RecipeDetailContract.Presenter presenter;

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void setPresenter(RecipeDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showRecipe(@NonNull Recipe recipe) {

    }

    @Override
    public void showStepScreen(@NonNull List<Step> steps, @Nullable Step selectedStep) {
        final Intent intent = new Intent(getContext(), StepActivity.class);
        intent.putParcelableArrayListExtra(StepActivity.EXTRA_STEP_LIST, new ArrayList<Parcelable>(steps));

        if (selectedStep != null) {
            intent.putExtra(StepActivity.EXTRA_STEP_SELECTED, selectedStep);
        }

        startActivity(intent);
    }
}
