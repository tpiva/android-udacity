/*
 * Copyright (C) 2017 Thiago Piva Magalhães
 */

package com.popmovies.android.popmovies.bo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

/**
 * Business object that represents one movie from webservice.
 */

public class Movie implements Parcelable{
    private String posterPath;
    private boolean adult;
    private String overview;
    private Date releaseDate;
    private int[] genreIds;
    private int id;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private double popularity;
    private int voteCount;
    private boolean video;
    private double voteAverage;
    private byte[] posterImage;
    private boolean isMarkAsFavorite;

    private List<Review> reviews;
    private List<Trailer> trailers;

    public Movie(){}

    protected Movie(Parcel in) {
        posterPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        genreIds = in.createIntArray();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        voteAverage = in.readDouble();
        releaseDate = new Date(in.readLong());
        posterImage = in.createByteArray();
        isMarkAsFavorite = in.readByte() != 0;
        if (reviews != null) {
            in.readTypedList(reviews, Review.CREATOR);
        }
        if (trailers != null) {
            in.readTypedList(trailers, Trailer.CREATOR);
        }
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public byte[] getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(byte[] posterImage) {
        this.posterImage = posterImage;
    }

    public boolean isMarkAsFavorite() {
        return isMarkAsFavorite;
    }

    public void setMarkAsFavorite(boolean markAsFavorite) {
        isMarkAsFavorite = markAsFavorite;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterPath);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeIntArray(genreIds);
        parcel.writeInt(id);
        parcel.writeString(originalTitle);
        parcel.writeString(originalLanguage);
        parcel.writeString(title);
        parcel.writeString(backdropPath);
        parcel.writeDouble(popularity);
        parcel.writeInt(voteCount);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeDouble(voteAverage);
        parcel.writeLong(releaseDate.getTime());
        parcel.writeByteArray(posterImage);
        parcel.writeByte((byte) (isMarkAsFavorite ? 1 : 0));
        parcel.writeTypedList(reviews);
        parcel.writeTypedList(trailers);
    }


}
