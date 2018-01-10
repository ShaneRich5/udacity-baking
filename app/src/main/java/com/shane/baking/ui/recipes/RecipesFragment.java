package com.shane.baking.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shane.baking.R;
import com.shane.baking.adapters.RecipeAdapter;
import com.shane.baking.data.Recipe;
import com.shane.baking.ui.base.BaseFragment;
import com.shane.baking.ui.GridSpaceItemDecoration;
import com.shane.baking.ui.recipedetail.RecipeDetailActivity;

import java.util.List;

import butterknife.BindView;


public class RecipesFragment extends BaseFragment implements RecipesContract.View {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;

    @BindView(R.id.message_container)
    ConstraintLayout messageContainer;

    @BindView(R.id.message_text)
    TextView messageTextView;

    private RecipesContract.Presenter presenter;
    private RecipeAdapter recipeAdapter;

    public RecipesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int NUM_OF_GRID_COLUMNS = getResources().getInteger(R.integer.recipe_grid_column_count);
        final int PIXEL_GRID_SPACING = 10;


        // The activity handles the click event to make changes in multi-screen mode
        if ( ! (getActivity() instanceof RecipeAdapter.OnClickHandler)) {
            throw new ClassCastException("Activity must implement RecipeAdapter.OnClickHandler");
        }

        recipeAdapter = new RecipeAdapter((RecipeAdapter.OnClickHandler) getActivity(), getActivity());
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUM_OF_GRID_COLUMNS));
        recipeRecyclerView.addItemDecoration(new GridSpaceItemDecoration(PIXEL_GRID_SPACING));
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setAdapter(recipeAdapter);
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
    public void setPresenter(RecipesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showRecipes(@NonNull List<Recipe> recipes) {
        messageContainer.setVisibility(View.GONE);
        recipeAdapter.setRecipes(recipes);
    }

    @Override
    public void showNoRecipes() {
        messageContainer.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.message_no_recipes_found);
    }

    @Override
    public void showLoadingRecipeError() {
        messageContainer.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.message_error_loading_recipes);
    }

    @Override
    public void showRecipeDetailScreen(@NonNull Recipe recipe) {
        Intent recipeIntent = new Intent(getContext(), RecipeDetailActivity.class);
        recipeIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe);
        startActivity(recipeIntent);
    }

    @Override
    public void showLoadingIndicator(boolean shouldShow) {
        swipeRefreshLayout.setRefreshing(shouldShow);


    }
}
