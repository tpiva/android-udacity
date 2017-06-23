package com.thiago.bakingapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {

    private int id;
    private String shortDescripition;
    private String videoUrl;
    private String thumbnailURL;
    private String description;

    public Step() {
    }

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescripition = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailURL = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescripition() {
        return shortDescripition;
    }

    public void setShortDescripition(String shortDescripition) {
        this.shortDescripition = shortDescripition;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescripition);
        parcel.writeString(description);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnailURL);
    }
}
