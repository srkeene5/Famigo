package com.famigo.website.model;

import java.time.LocalDateTime;

public class Report {
    private int reportID;
    private String reportingUserID;
    private String contentUserID;
    private String reportText;
    private LocalDateTime timeStamp;
    private String appeal;
    private boolean ban;

    private Place place;
    private Comment comment;
    private Review review;

    public Report() {
    }

    public Report(String reportingUserID, String contentUserID, Place place, String reportText) {
        reportID = 0;
        this.contentUserID = contentUserID;
        this.reportingUserID = reportingUserID;
        this.place = place;
        this.reportText = reportText;
        this.timeStamp = java.time.LocalDateTime.now();
        comment = null;
        review = null;
        this.appeal = null;
        this.ban = false;
    }

    public Report(String reportingUserID, String contentUserID, Comment comment, String reportText) {
        reportID = 0;
        this.contentUserID = contentUserID;
        this.reportingUserID = reportingUserID;
        this.comment = comment;
        this.reportText = reportText;
        this.timeStamp = java.time.LocalDateTime.now();
        place = null;
        review = null;
        this.appeal = null;
        this.ban = false;
    }

    public Report(String reportingUserID, String contentUserID, Review review, String reportText) {
        reportID = 0;
        this.contentUserID = contentUserID;
        this.reportingUserID = reportingUserID;
        this.review = review;
        this.reportText = reportText;
        this.timeStamp = java.time.LocalDateTime.now();
        place = null;
        comment = null;
        this.appeal = null;
        this.ban = false;
    }

    public Report(int reportID, String reportingUserID, String contentUserID, String reportText, String appeal,
            LocalDateTime timeStamp, Place place,
            Review review,
            Comment comment, boolean ban) {
        this.place = place;
        this.comment = comment;
        this.review = review;
        this.reportID = reportID;
        this.contentUserID = contentUserID;
        this.reportingUserID = reportingUserID;
        this.reportText = reportText;
        this.timeStamp = timeStamp;
        this.appeal = appeal;
        this.ban = ban;
    }

    public String getContentUserID() {
        return contentUserID;
    }

    public String getReportingUserID() {
        return reportingUserID;
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

    public String getAppeal() {
        return appeal;
    }

    public void setAppeal(String appeal) {
        this.appeal = appeal;
    }

    public boolean isBanned() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public void rejectAppeal() {

    }

}
