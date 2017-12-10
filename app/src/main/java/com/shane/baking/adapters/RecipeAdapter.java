package com.shane.baking.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shane.baking.R;
import com.shane.baking.data.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shane on 9/29/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final OnClickHandler clickHandler;
    private List<Recipe> recipes = new ArrayList<>();

    public interface OnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(@NonNull OnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(@NonNull List<Recipe> newRecipes) {
        recipes = newRecipes;
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_image) ImageView recipeImageView;
        @BindView(R.id.recipe_name_text) TextView recipeNameTextView;
        @BindView(R.id.serving_size_text) TextView servingsTextView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
            recipeNameTextView.setText(recipe.getName());
            servingsTextView.setText(String.valueOf(recipe.getServings()));
            itemView.setId((int) recipe.getId());
        }

        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            Recipe recipe = recipes.get(index);
            clickHandler.onClick(recipe);
        }
    }
}
