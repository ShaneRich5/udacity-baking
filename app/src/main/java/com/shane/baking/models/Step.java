package com.shane.baking.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.shane.baking.data.RecipeContract.StepEntry;

import java.util.Locale;

/**
 * Created by Shane on 9/29/2017.
 */
@Entity(tableName = StepEntry.TABLE_NAME)
public class Step implements Parcelable {
    @PrimaryKey
    private int id;

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

    public Step() {}

    public Step(int id, String summary, String description, String videoUrl, String thumbnailUrl) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


    protected Step(Parcel in) {
        id = in.readInt();
        summary = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
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
        parcel.writeInt(id);
        parcel.writeString(summary);
        parcel.writeString(description);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnailUrl);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Summary: {id: %d, summary: %s, description: %s, videoUrl: %s, thumbnailUrl: %s}",
                id, summary, description, videoUrl, thumbnailUrl);
    }
}
