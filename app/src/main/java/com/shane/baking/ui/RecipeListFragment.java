package com.shane.baking.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shane.baking.R;
import com.shane.baking.adapters.RecipeAdapter;
import com.shane.baking.models.Recipe;
import com.shane.baking.network.RecipeApi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Shane on 9/29/2017.
 */

public class RecipeListFragment extends Fragment {

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

        recipeAdapter = new RecipeAdapter(getContext());

        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setAdapter(recipeAdapter);

        RecipeApi recipeApi = RecipeApi.Factory.create();
        recipeApi.getRecipe().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Recipe>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Recipe> recipes) {
                        recipeAdapter.setRecipes(recipes);
                    }

                    @Override
                    public void onError(@NonNull Throwable error) {
                        Timber.e(error);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
