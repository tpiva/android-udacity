package com.thiago.popularmovies.dto;

import java.util.ArrayList;

/**
 * Created by tmagalhaes on 10-Jan-17.
 */

public class ExtraMovieInfo {

    private ArrayList<Video> videos;
    private ArrayList<Review> reviews;

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
