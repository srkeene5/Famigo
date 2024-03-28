package com.famigo.website.model;

import java.time.LocalDateTime;

public class Review {

    private String userId;
    private int revId;
    private String review;
    private int stars;
    private LocalDateTime timeStamp;
    private boolean edited;
    private int placeId;
    private int likes;
    private int dislikes;

    // Constructor for new review
    public Review(String userId, int placeId, String review, int stars) {
        this.userId = userId;
        this.timeStamp = java.time.LocalDateTime.now();
        this.review = review;
        this.stars = stars;
        this.edited = false;
        this.placeId = placeId;
        this.likes = 0;
        this.dislikes = 0;
    }

    // Constructor for review pulled from database
    public Review(String userId, int revId, String review, int stars, LocalDateTime timeStamp, boolean edited,
            int placeId, int likes, int dislikes) {
        this.userId = userId;
        this.revId = revId;
        this.review = review;
        this.stars = stars;
        this.timeStamp = timeStamp;
        this.edited = edited;
        this.placeId = placeId;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    /*
     * private String[] addReview() {
     * String[] review = new String[2];
     * int stars = 5;
     * review[0] = "review";
     * review[1] = Integer.toString(stars);
     * return review;
     * }
     */

    public String getUserId() {
        return userId;
    }

    public int getRevId() {
        return revId;
    }

    public String getReview() {
        return review;
    }

    public int getStars() {
        return stars;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public boolean isEdited() {
        return edited;
    }

    public int getPlaceId() {
        return placeId;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void updateLikes(int likes) {
        this.likes = likes;
    }

    public void updateDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

}
