package com.famigo.website.model;

import java.time.LocalDateTime;

public class Comment {
    private String comUserId;
    private int comId;
    private String comment;
    private int likes;
    private int dislikes;
    private LocalDateTime timeStamp;
    private boolean edited;
    private int reviewId;

    public Comment(String comUserId, int reviewId, String comment, int likes) {
        this.comUserId = comUserId;
        this.timeStamp = java.time.LocalDateTime.now();
        this.comment = comment;
        this.likes = likes;
        this.edited = false;
        this.reviewId = reviewId;
    }

    public Comment(String comUserId, int comId, String comment, int likes, LocalDateTime timeStamp, boolean edited,
            int reviewId) {
        this.comUserId = comUserId;
        this.comId = comId;
        this.comment = comment;
        this.likes = likes;
        this.timeStamp = timeStamp;
        this.edited = edited;
        this.reviewId = reviewId;
    }

    public String getComUserId() {
        return comUserId;
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

    public int getDislikes() {
        return dislikes;
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

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
