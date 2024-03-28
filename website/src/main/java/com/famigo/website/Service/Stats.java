package com.famigo.website.Service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.famigo.website.model.Comment;
import com.famigo.website.model.Review;

@Service
public class Stats {
    private String userID;
    private int totStars;
    private int avgStars;
    private int totLikes;
    private int avgLikes;
    private int totDis;
    private int avgDis;

    private ArrayList<Review> reviews;
    private ArrayList<Comment> comments;

    public Stats() {
    }

    public Stats(String userID, ArrayList<Review> reviews, ArrayList<Comment> comments) {
        this.userID = userID;

        this.totLikes = 0;
        this.totDis = 0;
        this.totStars = 0;
        this.avgLikes = 0;
        this.avgDis = 0;
        this.avgStars = 0;

        this.reviews = reviews;
        this.comments = comments;

        if (reviews != null && !reviews.isEmpty()) {
            for (Review review : reviews) {
                totStars += review.getStars();
                totLikes += review.getLikes();
                totDis += review.getDislikes();
            }
            int i = reviews.size();
            this.avgStars = totStars / i;
            this.avgLikes = totLikes / i;
            this.avgDis = totDis / i;
        }
    }

    public String getUserID() {
        return userID;
    }

    public int getTotStars() {
        return totStars;
    }

    public int getAvgStars() {
        return avgStars;
    }

    public int getTotLikes() {
        return totLikes;
    }

    public int getAvgLikes() {
        return avgLikes;
    }

    public int getTotDis() {
        return totDis;
    }

    public int getAvgDis() {
        return avgDis;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
