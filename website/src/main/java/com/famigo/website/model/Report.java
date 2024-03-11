package com.famigo.website.model;

import java.time.LocalDateTime;

public class Report {
    private int reportID;
    private String userID;
    private String reportText;
    private LocalDateTime timeStamp;

    private Place place;
    private Comment comment;
    private Review review;

    public Report() {
    }

    public Report(String userID, Place place, String reportText) {
        reportID = 0;
        this.userID = userID;
        this.place = place;
        this.reportText = reportText;
        this.timeStamp = java.time.LocalDateTime.now();
        comment = null;
        review = null;
    }

    public Report(String userID, Comment comment, String reportText) {
        reportID = 0;
        this.userID = userID;
        this.comment = comment;
        this.reportText = reportText;
        this.timeStamp = java.time.LocalDateTime.now();
        place = null;
        review = null;
    }

    public Report(String userID, Review review, String reportText) {
        reportID = 0;
        this.userID = userID;
        this.review = review;
        this.reportText = reportText;
        this.timeStamp = java.time.LocalDateTime.now();
        place = null;
        comment = null;
    }

    public Report(int reportID, String userID, String reportText, LocalDateTime timeStamp, Place place, Review review,
            Comment comment) {
        this.place = place;
        this.comment = comment;
        this.review = review;
        this.reportID = reportID;
        this.userID = userID;
        this.reportText = reportText;
        this.timeStamp = timeStamp;
    }

    public String getUserID() {
        return userID;
    }

    public String getReportedText() {
        if (place != null) {
            return place.getDescription();
        } else if (comment != null) {
            return comment.getComment();
        } else if (review != null) {
            return review.getReview();
        }
        return null;
    }

    public int getReportedId() {
        if (place != null) {
            return place.getId();
        } else if (comment != null) {
            return comment.getComId();
        } else if (review != null) {
            return review.getRevId();
        }
        return -1;
    }

    public String getReportClass() {
        if (place != null) {
            return "place";
        } else if (comment != null) {
            return "comment";
        } else if (review != null) {
            return "review";
        }
        return null;
    }

    public int getPlaceId() {
        if (place != null) {
            return place.getId();
        }
        return -1;
    }

    public int getComId() {
        if (comment != null) {
            return comment.getComId();
        }
        return -1;
    }

    public int getRevId() {
        if (review != null) {
            return review.getRevId();
        }
        return -1;
    }

    public int getReportID() {
        return reportID;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getExplanation() {
        return reportText;
    }

    public void deleteReport() {

    }

    public void removeReported() {

    }

    public void removeBanReported() {

    }

}
