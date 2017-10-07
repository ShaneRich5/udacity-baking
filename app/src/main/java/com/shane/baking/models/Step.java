package com.shane.baking.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.shane.baking.data.RecipeContract.RecipeEntry;
import com.shane.baking.data.RecipeContract.StepEntry;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Shane on 9/29/2017.
 */
@Entity(tableName = StepEntry.TABLE_NAME,
        primaryKeys = {StepEntry._ID, StepEntry.COLUMN_RECIPE},
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = RecipeEntry._ID,
                childColumns = StepEntry.COLUMN_RECIPE,
                onDelete = CASCADE))
public class Step implements Parcelable {
    @ColumnInfo(name = StepEntry._ID)
    private long id;

    @ColumnInfo(name = StepEntry.COLUMN_SUMMARY)
    @SerializedName("shortDescription")
    private String summary;

    @ColumnInfo(name = StepEntry.COLUMN_DESCRIPTION)
    private String description;

    @ColumnInfo(name = StepEntry.COLUMN_VIDEO_URL)
    @SerializedName("videoURL")
    private String videoUrl;

    @ColumnInfo(name = StepEntry.COLUMN_THUMBNAIL_URL)
    @SerializedName("thumbnailURL")
    private String thumbnailUrl;

    @ColumnInfo(name = StepEntry.COLUMN_RECIPE)
    private long recipeId;

    public Step() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @Ignore
    protected Step(Parcel in) {
        id = in.readLong();
        summary = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
        recipeId = in.readLong();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(summary);
        parcel.writeString(description);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnailUrl);
        parcel.writeLong(recipeId);
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", recipeId=" + recipeId +
                '}';
    }

    public static Step fromContentValues(ContentValues values) {
        return new Step();
    }

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder() {}

        public Builder id(long id) {
            values.put(StepEntry._ID, id);
            return this;
        }

        public Builder summary(String summary) {
            values.put(StepEntry.COLUMN_SUMMARY, summary);
            return this;
        }

        public Builder description(String description) {
            values.put(StepEntry.COLUMN_DESCRIPTION, description);
            return this;
        }

        public Builder videoUrl(String videoUrl) {
            values.put(StepEntry.COLUMN_VIDEO_URL, videoUrl);
            return this;
        }

        public Builder thumbnailUrl(String thumbnailUrl) {
            values.put(StepEntry.COLUMN_THUMBNAIL_URL, thumbnailUrl);
            return this;
        }

        public Builder recipeId(long recipeId) {
            values.put(StepEntry.COLUMN_RECIPE, recipeId);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
