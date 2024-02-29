package com.famigo.website.service;

import java.util.ArrayList;
import com.famigo.website.model.Review;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PlaceRevsService {

    private static ArrayList<Review> reviews = new ArrayList<Review>();

    // Add test reviews to display
    static {
        reviews.add(new Review("user0", "place0", "review0", 1));
        reviews.add(new Review("user1", "place1", "review1", 2));
        reviews.add(new Review("user2", "place2", "review2", 3));
        reviews.add(new Review("user3", "place3", "review3", 3));
    }

    public String getAllReviewText() {
        String combinedText = "";
        for (Review r : reviews) {
            combinedText += (r.getUserId() + '\n' + r.getReview() + "\n\n");
        }
        return combinedText;
    }

}
