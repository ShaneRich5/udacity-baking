package com.shane.baking.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.shane.baking.data.RecipeContract.StepEntry;

/**
 * Created by Shane on 9/29/2017.
 */
public class Step implements Parcelable {

    @SerializedName("id")
    private long number;

    @SerializedName("shortDescription")
    private String summary;

    private String description;

    @SerializedName("videoURL")
    private String videoUrl;

    @SerializedName("thumbnailURL")
    private String thumbnailUrl;

    private long recipeId;

    public Step() {}

    public Step(long number, String summary, String description, String videoUrl, String thumbnailUrl, long recipeId) {
        this.number = number;
        this.summary = summary;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.recipeId = recipeId;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
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

    protected Step(Parcel in) {
        number = in.readLong();
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
        parcel.writeLong(number);
        parcel.writeString(summary);
        parcel.writeString(description);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnailUrl);
        parcel.writeLong(recipeId);
    }

    @Override
    public String toString() {
        return "Step{" +
                "number=" + number +
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

        public Builder number(long number) {
            values.put(StepEntry.COLUMN_NUMBER, number);
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

    public ContentValues toContentValues() {
        return new Builder()
                .number(number)
                .summary(summary)
                .description(description)
                .videoUrl(videoUrl)
                .thumbnailUrl(thumbnailUrl)
                .recipeId(recipeId).build();
    }
}
