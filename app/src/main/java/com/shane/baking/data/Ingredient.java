package com.shane.baking.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.shane.baking.data.RecipeContract.IngredientEntry;
import com.shane.baking.data.RecipeContract.RecipeEntry;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = IngredientEntry.TABLE_NAME,
        foreignKeys = {
        @ForeignKey(entity = Recipe.class,
                parentColumns = RecipeEntry._ID,
                childColumns = IngredientEntry.COLUMN_RECIPE_ID,
                onDelete = CASCADE)},
        indices = {@Index(value = IngredientEntry.COLUMN_RECIPE_ID)})
public class Ingredient implements Parcelable {
    @PrimaryKey
    private long id;

    @SerializedName("ingredient")
    @ColumnInfo(name = IngredientEntry.COLUMN_NAME)
    private String name;

    @SerializedName("measure")
    @ColumnInfo(name = IngredientEntry.COLUMN_UNIT)
    private String unit;

    @ColumnInfo(name = IngredientEntry.COLUMN_QUALITY)
    private double quantity;

    @ColumnInfo(name = IngredientEntry.COLUMN_RECIPE_ID)
    private long recipeId;

    public Ingredient() {
    }

    @Ignore
    public Ingredient(String name, double quantity, String unit, long recipeId) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.recipeId = recipeId;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                ", recipeId=" + recipeId +
                '}';
    }

    protected Ingredient(Parcel in) {
        name = in.readString();
        unit = in.readString();
        quantity = in.readDouble();
        recipeId = in.readLong();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(unit);
        parcel.writeDouble(quantity);
        parcel.writeLong(recipeId);
    }

    public static class Builder {
        ContentValues values = new ContentValues();

        public Builder name(String name) {
            values.put(IngredientEntry.COLUMN_NAME, name);
            return this;
        }

        public Builder unit(String unit) {
            values.put(IngredientEntry.COLUMN_UNIT, unit);
            return this;
        }

        public Builder quantity(double quantity) {
            values.put(IngredientEntry.COLUMN_QUALITY, quantity);
            return this;
        }

        public Builder recipeId(long recipeId) {
            values.put(IngredientEntry.COLUMN_RECIPE_ID, recipeId);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }

    public ContentValues toContentValues() {
        return new Builder()
                .name(name)
                .unit(unit)
                .quantity(quantity)
                .recipeId(recipeId)
                .build();
    }
}
