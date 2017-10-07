package com.shane.baking.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.shane.baking.data.RecipeContract;
import com.shane.baking.data.RecipeContract.IngredientEntry;

import static android.arch.persistence.room.ForeignKey.CASCADE;


/**
 * Created by Shane on 9/29/2017.
 */
@Entity(tableName = IngredientEntry.TABLE_NAME,
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = RecipeContract.RecipeEntry._ID,
                childColumns = IngredientEntry.COLUMN_RECIPE,
                onDelete = CASCADE))
public class Ingredient implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = IngredientEntry._ID, index = true)
    private long id;

    @ColumnInfo(name = IngredientEntry.COLUMN_NAME)
    @SerializedName("ingredient")
    private String name;

    @ColumnInfo(name = IngredientEntry.COLUMN_UNIT)
    @SerializedName("measure")
    private String unit;

    @ColumnInfo(name = IngredientEntry.COLUMN_QUALITY)
    private double quantity;

    @ColumnInfo(name = IngredientEntry.COLUMN_RECIPE)
    private long recipeId;

    public Ingredient() {}

    @Ignore
    public Ingredient(long id, String name, double quantity, String unit) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                ", recipeId=" + recipeId +
                '}';
    }

    @Ignore
    protected Ingredient(Parcel in) {
        id = in.readLong();
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
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(unit);
        parcel.writeDouble(quantity);
        parcel.writeLong(recipeId);
    }

    public static Ingredient fromContentValues(ContentValues values) {
        return new Ingredient();
    }
}
