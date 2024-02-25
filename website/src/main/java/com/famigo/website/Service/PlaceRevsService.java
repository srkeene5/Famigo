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

    public int[] getAllUserIds() {
        int[] ids = new int[reviews.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = reviews.get(i).getUserId();
        }
        return ids;
    }

    public int[] getAllStars() {
        int[] ratings = new int[reviews.size()];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = reviews.get(i).getStars();
        }
        return ratings;
    }

    public String[] getAllRevText() {
        String[] revs = new String[reviews.size()];
        for (int i = 0; i < revs.length; i++) {
            revs[i] = reviews.get(i).getReview();
        }
        return revs;
    }

}
