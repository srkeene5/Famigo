package com.famigo.website.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Review {
    private String userId;
    private int revId;
    private String review;
    private int stars;
    private LocalDateTime timeStamp;
    private boolean edited;
    private String placeId;
    private int likes;
    private int dislikes;

    // Constructor for new review
    public Review(String userId, String placeId, String review, int stars) {
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
            String placeId, int likes, int dislikes) {
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

    public String getPlaceId() {
        return placeId;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public static String[] getUserIdArr(ArrayList<Review> revs) {
        String[] ids = new String[revs.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = revs.get(i).getUserId();
        }
        return ids;
    }

    public static int[] getStarArr(ArrayList<Review> revs) {
        int[] ratings = new int[revs.size()];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = revs.get(i).getStars();
        }
        return ratings;
    }

    public static String[] getTextArr(ArrayList<Review> revs) {
        String[] textArr = new String[revs.size()];
        for (int i = 0; i < textArr.length; i++) {
            textArr[i] = revs.get(i).getReview();
        }
        return textArr;
    }

    public static int[] getRevIdArr(ArrayList<Review> revs) {
        int[] ids = new int[revs.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = revs.get(i).getRevId();
        }
        return ids;
    }

    // Main for testing
    public static void main(String[] args) {
        Review r1 = new Review("1", "p", "review", 3);
        r1.isEdited();
    }
}
