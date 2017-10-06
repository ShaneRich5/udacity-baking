package com.shane.baking.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.shane.baking.data.RecipeContract.RecipeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shane on 9/29/2017.
 */
@Entity(tableName = RecipeEntry.TABLE_NAME)
public class Recipe implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = RecipeEntry._ID, index = true)
    private long id;

    @ColumnInfo(name = RecipeEntry.COLUMN_NAME)
    private String name;

    @ColumnInfo(name = RecipeEntry.COLUMN_SERVINGS)
    private int servings;

    @ColumnInfo(name = RecipeEntry.COLUMN_IMAGE_URL)
    @SerializedName("image")
    private String imageUrl;

    @Ignore
    private List<Step> steps = new ArrayList<>();

    @Ignore
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {}

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

    @Ignore
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

        public Builder imageUrl (String imageUrl) {
            values.put(RecipeEntry.COLUMN_IMAGE_URL, imageUrl);
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
            return build();
        }
    }

    public static Recipe fromContentValues(ContentValues values) {
        Recipe recipe = new Recipe();
        recipe.setId(values.getAsShort(RecipeEntry._ID));
        recipe.setName(values.getAsString(RecipeEntry.COLUMN_NAME));
        recipe.setServings(values.getAsInteger(RecipeEntry.COLUMN_SERVINGS));
        recipe.setImageUrl(values.getAsString(RecipeEntry.COLUMN_IMAGE_URL));
        return recipe;
    }
}
