package com.shane.baking.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shane.baking.R;
import com.shane.baking.adapters.RecipeAdapter;
import com.shane.baking.models.Recipe;

import java.net.UnknownHostException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by Shane on 9/29/2017.
 */

public abstract class RecipeListFragment extends Fragment {
    public static final String TAG = RecipeListFragment.class.getName();

    @BindView(R.id.recycler_recipe) RecyclerView recipeRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_container) LinearLayout errorContainer;
    @BindView(R.id.error_message) TextView errorMessageTextView;
    @BindView(R.id.error_image) ImageView errorImageView;
    @BindView(R.id.error_button) Button errorButton;

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

    protected void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recipeRecyclerView.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
    }

    protected void addRecipesToAdapter(List<Recipe> recipes) {
        recipeAdapter.setRecipes(recipes);
    }

    protected Observer<List<Recipe>> recipeObserver = new Observer<List<Recipe>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            showLoading();
        }

        @Override
        public void onNext(@NonNull List<Recipe> recipes) {
            handleRecipesLoaded(recipes);
        }

        @Override
        public void onError(@NonNull Throwable error) {
            Timber.e(error);
            handleError(error);
        }

        @Override
        public void onComplete() {
            progressBar.setVisibility(View.GONE);
        }
    };

    protected void handleRecipesLoaded(@NonNull List<Recipe> recipes) {
        recipeRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorContainer.setVisibility(View.GONE);
        addRecipesToAdapter(recipes);
    }

    protected void handleError(Throwable error) {
        if (error instanceof UnknownHostException) {
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_signal_wifi_off_white_48dp, null));
            errorMessageTextView.setText("Unable to connect to the internet");
        } else {
            errorImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_info_outline_white_48dp, null));
            errorMessageTextView.setText("Failed to load recipe");
        }

        showErrors();
    }

    protected void showErrors() {
        errorContainer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recipeRecyclerView.setVisibility(View.GONE);
    }

    protected abstract void loadRecipes();
}
