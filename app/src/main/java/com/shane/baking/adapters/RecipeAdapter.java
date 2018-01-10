package com.shane.baking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shane.baking.R;
import com.shane.baking.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shane on 9/29/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final OnClickHandler clickHandler;
    private final Context context;
    private List<Recipe> recipes = new ArrayList<>();
    private final boolean showChoicesLayout;

    public interface OnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(@NonNull OnClickHandler clickHandler, @NonNull Context context,
                         boolean showChoicesLayout) {
        this.clickHandler = clickHandler;
        this.context = context;
        this.showChoicesLayout = showChoicesLayout;
    }

    public RecipeAdapter(@NonNull OnClickHandler clickHandler, @NonNull Context context) {
        this(clickHandler, context, false);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(getRecipeLayout(showChoicesLayout), parent, false);
        return showChoicesLayout
                ? new RecipeSimpleViewHolder(view)
                : new RecipeDetailedViewHolder(view);
    }

    private int getRecipeLayout(boolean showChoicesLayout) {
        return showChoicesLayout ? R.layout.item_recipe_choice : R.layout.item_recipe;
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

    abstract class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int index = getAdapterPosition();
            Recipe recipe = recipes.get(index);
            clickHandler.onClick(recipe);
        }

        abstract void bind(@NonNull Recipe recipe);
    }

    class RecipeSimpleViewHolder extends RecipeViewHolder {
        @BindView(R.id.name_text_view)
        TextView nameTextView;

        RecipeSimpleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(@NonNull Recipe recipe) {
            nameTextView.setText(recipe.getName());
            itemView.setId((int) recipe.getId());
        }
    }

    class RecipeDetailedViewHolder extends RecipeViewHolder {

        @BindView(R.id.recipe_image) ImageView recipeImageView;
        @BindView(R.id.recipe_name_text) TextView recipeNameTextView;
        @BindView(R.id.serving_size_text) TextView servingsTextView;

        RecipeDetailedViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        void bind(@NonNull Recipe recipe) {
            recipeNameTextView.setText(recipe.getName());
            servingsTextView.setText(String.valueOf(recipe.getServings()));

            if ( ! TextUtils.isEmpty(recipe.getImageUrl())) {
                Picasso.with(context).load(recipe.getImageUrl()).into(recipeImageView);
            }

            itemView.setId((int) recipe.getId());
        }
    }
}
