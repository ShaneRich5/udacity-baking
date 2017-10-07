package com.shane.baking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shane.baking.R;
import com.shane.baking.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shane on 9/29/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context context;
    private final OnClickHandler clickHandler;
    private List<Recipe> recipes = new ArrayList<>();

    public interface OnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(@NonNull Context context, @NonNull OnClickHandler clickHandler) {
        this.context = context;
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

        @BindView(R.id.name_text_view) TextView nameTextView;
        @BindView(R.id.servings_text_view) TextView servingsTextView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
            nameTextView.setText(recipe.getName());
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
