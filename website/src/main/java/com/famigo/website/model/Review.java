package com.famigo.website.model;

import java.time.LocalDateTime;

public class Review {
    private int userId;
    private int revId;
    private String review;
    private int stars;
    private LocalDateTime timeStamp;
    private boolean edited;

    public Review(int userId) {
        this.userId = userId;
        this.revId = newReviewId();
        String[] revAr = this.addReview();
        this.timeStamp = java.time.LocalDateTime.now();
        this.review = revAr[0];
        this.stars = Integer.parseInt(revAr[1]);
        this.edited = false;
    }

    private String[] addReview() {
        String[] review = new String[2];
        int stars = 5;
        review[0] = "review";
        review[1] = Integer.toString(stars);
        return review;
    }

    public int getUserId() {
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

    private int newReviewId() {
        return -1;
    }

    public static void main(String[] args) {
        Review r1 = new Review(1);
        r1.isEdited();
    }
}
