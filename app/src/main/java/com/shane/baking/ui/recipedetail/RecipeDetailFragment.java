package com.shane.baking.ui.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shane.baking.R;
import com.shane.baking.adapters.IngredientAdapter;
import com.shane.baking.adapters.StepAdapter;
import com.shane.baking.data.Recipe;
import com.shane.baking.data.Step;
import com.shane.baking.ui.base.BaseFragment;
import com.shane.baking.ui.steps.StepActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecipeDetailFragment extends BaseFragment implements RecipeDetailContract.View {

    @BindView(R.id.ingredient_recycler)
    RecyclerView ingredientRecycler;

    @BindView(R.id.step_recycler)
    RecyclerView stepRecycler;

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

    private RecipeDetailContract.Presenter presenter;

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if ( ! (getActivity() instanceof StepAdapter.OnClickHandler)) {
            throw new ClassCastException("Activity must implement StepAdapter.OnClickHandler");
        }

        ingredientAdapter = new IngredientAdapter(getContext());
        ingredientRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ingredientRecycler.setAdapter(ingredientAdapter);
        ingredientRecycler.setHasFixedSize(true);

        stepAdapter = new StepAdapter(getContext(), ((StepAdapter.OnClickHandler) getActivity()));
        stepRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        stepRecycler.setAdapter(stepAdapter);
        stepRecycler.setHasFixedSize(true);

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
        ingredientAdapter.setIngredients(recipe.getIngredients());
        stepAdapter.setSteps(recipe.getSteps());
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
