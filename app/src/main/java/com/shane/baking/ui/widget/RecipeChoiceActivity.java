package com.shane.baking.ui.widget;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.shane.baking.R;
import com.shane.baking.ui.base.BaseActivity;

import butterknife.BindView;

public class RecipeChoiceActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_choice);
        setSupportActionBar(toolbar);


    }
}
