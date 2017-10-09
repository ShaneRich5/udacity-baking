package com.shane.baking.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.shane.baking.data.RecipeContract.RecipeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shane on 9/29/2017.
 */
public class Recipe implements Parcelable {

    private long id;

    private String name;

    private int servings;

    @SerializedName("image")
    private String imageUrl;

    private List<Step> steps = new ArrayList<>();

    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {}

    public Recipe(long id, String name, int servings, String imageUrl) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        servings = in.readInt();
        imageUrl = in.readString();
        steps = in.createTypedArrayList(Step.CREATOR);
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(imageUrl);
        parcel.writeTypedList(steps);
        parcel.writeTypedList(ingredients);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Recipe: { id: %d, name: %s, servings: %d}", id, name, servings);
    }

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(RecipeEntry._ID, id);
            return this;
        }

        public Builder name (String name) {
            values.put(RecipeEntry.COLUMN_NAME, name);
            return this;
        }
        public Builder servings(int servings) {
            values.put(RecipeEntry.COLUMN_SERVINGS, servings);
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            values.put(RecipeEntry.COLUMN_IMAGE_URL, imageUrl);
            return this;
        }

        public Builder steps(List<Step> steps) {
            values.put(RecipeEntry.COLUMN_STEPS, new Gson().toJson(steps));
            return this;
        }

        public Builder ingredients(List<Ingredient> ingredients) {
            values.put(RecipeEntry.COLUMN_INGREDIENTS, new Gson().toJson(ingredients));
            return this;
        }

        public ContentValues build() {
            return values;
        }

        public ContentValues build(Recipe recipe) {
            id(recipe.id);
            name(recipe.name);
            servings(recipe.servings);
            imageUrl(recipe.imageUrl);
            steps(recipe.steps);
            ingredients(recipe.ingredients);
            return build();
        }
    }

    public static Recipe fromContentValues(ContentValues values) {
        Gson gson = new Gson();
        Recipe recipe = new Recipe();
        recipe.id = values.getAsShort(RecipeEntry._ID);
        recipe.name = values.getAsString(RecipeEntry.COLUMN_NAME);
        recipe.servings = values.getAsInteger(RecipeEntry.COLUMN_SERVINGS);
        recipe.imageUrl = values.getAsString(RecipeEntry.COLUMN_IMAGE_URL);
        recipe.steps = gson.fromJson(values.getAsString(RecipeEntry.COLUMN_STEPS),
                new TypeToken<List<Step>>() {}.getType());
        recipe.ingredients = gson.fromJson(values.getAsString(RecipeEntry.COLUMN_INGREDIENTS),
                new TypeToken<List<Ingredient>>() {}.getType());
        return recipe;
    }
}
