package com.thiago.bakingapp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {

    private int id;
    private String shortDescripition;
    private String videoUrl;
    private String thumbnailURL;

    protected Steps(Parcel in) {
        id = in.readInt();
        shortDescripition = in.readString();
        videoUrl = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescripition);
        parcel.writeString(videoUrl);
        parcel.writeString(thumbnailURL);
    }
}
