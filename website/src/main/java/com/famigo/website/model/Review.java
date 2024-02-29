package com.famigo.website.model;

import java.time.LocalDateTime;

public class Review {
    private String userId;
    private int revId;
    private String review;
    private int stars;
    private LocalDateTime timeStamp;
    private boolean edited;
    private String placeId;

    public Review(String userId, String placeId, String review, int stars) {
        this.userId = userId;
        this.timeStamp = java.time.LocalDateTime.now();
        this.review = review;
        this.stars = stars;
        this.edited = false;
        this.placeId = placeId;
    }

    public Review(String userId, int revId, String review, int stars, LocalDateTime timeStamp, boolean edited,
            String placeId) {
        this.userId = userId;
        this.revId = revId;
        this.review = review;
        this.stars = stars;
        this.timeStamp = timeStamp;
        this.edited = edited;
        this.placeId = placeId;
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

    public String getPlaceId() {
        return placeId;
    }

    public static void main(String[] args) {
        Review r1 = new Review("1", "p", "review", 3);
        r1.isEdited();
    }
}
