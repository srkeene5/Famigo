package com.famigo.website.Service;

import java.util.ArrayList;
import com.famigo.website.model.Review;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PlaceRevsService {

    private static ArrayList<Review> reviews = new ArrayList<Review>();

    // Add test reviews to display
    static {
        reviews.add(new Review(0));
        reviews.add(new Review(1));
        reviews.add(new Review(2));
        reviews.add(new Review(3));
    }

    public String getAllReviewText() {
        String combinedText = "";
        for (Review r : reviews) {
            combinedText += (r.getUserId() + '\n' + r.getReview() + "\n\n");
        }
        return combinedText;
    }

}
