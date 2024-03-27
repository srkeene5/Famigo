package com.famigo.website.model;

import java.time.LocalDateTime;

public class Comment {
    private String userId;
    private int comId;
    private String comment;
    private int likes;
    private LocalDateTime timeStamp;
    private boolean edited;
    private int reviewId;

    public Comment(String userId, int reviewId, String comment, int likes) {
        this.userId = userId;
        this.timeStamp = java.time.LocalDateTime.now();
        this.comment = comment;
        this.likes = likes;
        this.edited = false;
        this.reviewId = reviewId;
    }

    public Comment(String userId, int comId, String comment, int likes, LocalDateTime timeStamp, boolean edited,
            int reviewId) {
        this.userId = userId;
        this.comId = comId;
        this.comment = comment;
        this.likes = likes;
        this.timeStamp = timeStamp;
        this.edited = edited;
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public int getComId() {
        return comId;
    }

    public String getComment() {
        return comment;
    }

    public int getLikes() {
        return likes;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public boolean isEdited() {
        return edited;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
