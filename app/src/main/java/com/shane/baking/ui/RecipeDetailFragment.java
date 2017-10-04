package com.shane.baking.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shane.baking.R;
import com.shane.baking.adapters.IngredientAdapter;
import com.shane.baking.adapters.StepAdapter;
import com.shane.baking.models.Ingredient;
import com.shane.baking.models.Recipe;
import com.shane.baking.models.Step;
import com.shane.baking.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment implements StepAdapter.OnClickHandler{

    public static final String TAG = RecipeDetailFragment.class.getName();

    @BindView(R.id.ingredient_recycler) RecyclerView ingredientRecyclerView;
    @BindView(R.id.step_recycler) RecyclerView stepRecyclerView;

    private Recipe recipe;
    private InteractionListener listener;

    interface InteractionListener {
        void onStepSelected(Step step);
    }

    public RecipeDetailFragment() {}

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(Constants.EXTRA_RECIPE, recipe);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipe = getArguments().getParcelable(Constants.EXTRA_RECIPE);
        if (recipe == null) return;

        List<Ingredient> ingredients = recipe.getIngredients();
        List<Step> steps = recipe.getSteps();

        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredients);
        StepAdapter stepAdapter = new StepAdapter(getContext(), this, steps);

        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ingredientRecyclerView.setAdapter(ingredientAdapter);
        stepRecyclerView.setAdapter(stepAdapter);

        ViewCompat.setNestedScrollingEnabled(ingredientRecyclerView, false);
        ViewCompat.setNestedScrollingEnabled(stepRecyclerView, false);
    }

    @Override
    public void onClick(Step step) {
        if (recipe == null) return;
        listener.onStepSelected(step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDetailActivity)
            listener = (InteractionListener) context;
        else
            throw new IllegalStateException("Activity must implement InteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
