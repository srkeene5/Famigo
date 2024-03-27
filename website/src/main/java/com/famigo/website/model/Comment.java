package com.famigo.website.model;

import java.time.LocalDateTime;

public class Comment {
    private String comUserId;
    private int comId;
    private String comment;
    private int clikes;
    private int cdislikes;
    private LocalDateTime timeStamp;
    private boolean edited;
    private int reviewId;

    public Comment(String comUserId, int reviewId, String comment, int clikes, int cdislikes) {
        this.comUserId = comUserId;
        this.timeStamp = java.time.LocalDateTime.now();
        this.comment = comment;
        this.clikes = clikes;
        this.cdislikes = cdislikes;
        this.edited = false;
        this.reviewId = reviewId;
    }

    public Comment(String comUserId, int comId, String comment, int clikes, LocalDateTime timeStamp, boolean edited,
            int reviewId) {
        this.comUserId = comUserId;
        this.comId = comId;
        this.comment = comment;
        this.clikes = clikes;
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

    public int getClikes() {
        return clikes;
    }

    public int getCdislikes() {
        return cdislikes;
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

    public void setClikes(int clikes) {
        this.clikes = clikes;
    }

    public void setCdislikes(int cdislikes) {
        this.cdislikes = cdislikes;
    }
}
