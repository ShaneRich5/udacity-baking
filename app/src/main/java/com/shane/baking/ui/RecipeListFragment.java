package com.shane.baking.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shane.baking.R;
import com.shane.baking.adapters.RecipeAdapter;
import com.shane.baking.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shane on 9/29/2017.
 */

public abstract class RecipeListFragment extends Fragment {
    public static final String TAG = RecipeListFragment.class.getName();

    @BindView(R.id.recycler_recipe) RecyclerView recipeRecyclerView;

    private RecipeAdapter recipeAdapter;

    public RecipeListFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int NUM_OF_GRID_COLUMNS = getResources().getInteger(R.integer.recipe_grid_column_count);
        final int PIXEL_GRID_SPACING = 10;

        if (!(getActivity() instanceof RecipeAdapter.OnClickHandler)) {
            throw new ClassCastException("Activity must implement RecipeAdapter.OnClickHandler");
        }

        recipeAdapter = new RecipeAdapter(getContext(), (RecipeAdapter.OnClickHandler) getActivity());

        recipeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUM_OF_GRID_COLUMNS));
        recipeRecyclerView.addItemDecoration(new GridSpaceItemDecoration(PIXEL_GRID_SPACING));
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setAdapter(recipeAdapter);

        loadRecipes();
    }

    protected void addRecipesToAdapter(List<Recipe> recipes) {
        recipeAdapter.setRecipes(recipes);
    }

    protected abstract void loadRecipes();
}
