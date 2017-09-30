package com.shane.baking.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shane.baking.R;
import com.shane.baking.models.Recipe;

import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailFragment extends Fragment {

    public static final String TAG = RecipeDetailFragment.class.getName();

    private Recipe recipe;

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeDetailActivity.EXTRA_RECIPE, recipe);
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

        if (getArguments() == null
                || getArguments().getSerializable(RecipeDetailActivity.EXTRA_RECIPE) == null) {
            return;
        }

        recipe = (Recipe) getArguments().getSerializable(RecipeDetailActivity.EXTRA_RECIPE);
    }
}
