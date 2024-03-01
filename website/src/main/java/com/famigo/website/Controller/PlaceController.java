package com.famigo.website.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.model.Place;
import com.famigo.website.repositories.PlaceRepository;
import com.famigo.website.repositories.ReviewRepository;
import com.famigo.website.model.Review;
import com.famigo.website.model.SubReview;
import com.famigo.website.utilities.Util;

@Controller
public class PlaceController {

    private boolean testing = false;

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ReviewRepository rr;

    @GetMapping("/places/{name}")
    public String showPlaceDetails(@PathVariable String name, Model model) {
        Place place = placeRepository.getPlaceByName(name);

        // model.addAttribute("place", place);
        model.addAttribute("selectedPlace", place);
        model.addAttribute("placePath", "/places/" + name);
        model.addAttribute("placeId", place.getId());

        // Get reviews for this place
        ArrayList<Review> reviews = rr.getReviewsByPlace(place.getId());
        if (reviews == null || reviews.isEmpty()) {
            model.addAttribute("reviews", new Review[0]);
            model.addAttribute("userReactions", new int[0]);
        } else {
            model.addAttribute("reviews", reviews.toArray());

            int[] userReactions = rr.getUserReviewReactions(Util.getUserID(), reviews);
            model.addAttribute("userReactions", userReactions);
        }
        return "place-details";
    }

    @PostMapping("/places")
    public String places(Model model) {
        model.addAttribute("placeNamesList", placeRepository.getPlaces());
        return "place-search";
    }

    @RequestMapping(value = "/places/{name}/addRev", method = RequestMethod.POST)
    public ResponseEntity<String> submitReview(@PathVariable String name, @RequestBody SubReview sr) {
        rr.addReview(new Review(Util.getUserID(), sr.getrPlace(),
                sr.getrText(), Integer.parseInt(sr.getrStar())));

        if (testing) {
            ArrayList<Review> uRevArr = rr.getReviewsByUser(Util.getUserID());
            ArrayList<Review> pRevArr = rr.getReviewsByPlace(sr.getrPlace());
            System.out.println("_____Print Review By UserID_____");
            for (Review uRev : uRevArr) {
                System.out.println("User: " + uRev.getUserId());
                System.out.println("ReviewID: " + uRev.getRevId());
                System.out.println("Review: " + uRev.getReview());
                System.out.println("Stars: " + uRev.getStars());
                System.out.println("PlaceID: " + uRev.getPlaceId());
                System.out.println();
            }
            System.out.println("_____Print Review By PlaceID_____");
            for (Review pRev : pRevArr) {
                System.out.println("User: " + pRev.getUserId());
                System.out.println("ReviewID: " + pRev.getRevId());
                System.out.println("Review: " + pRev.getReview());
                System.out.println("Stars: " + pRev.getStars());
                System.out.println("PlaceID: " + pRev.getPlaceId());
                System.out.println();
            }
        }

        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }

    @RequestMapping(value = "/places/{name}/addRevReact", method = RequestMethod.POST)
    public ResponseEntity<String> likeOrDislike(@RequestParam(value="vals[]") int[] vals) {
        // Save like/dislike action
        // vals[0] = like (1) or dislike (0), and values[1] = review ID
        if (vals[0] == 1) {
            // Like button pressed
            rr.addReviewReaction(Util.getUserID(), vals[1], true);
        } else {
            // Dislike button pressed
            rr.addReviewReaction(Util.getUserID(), vals[1], false);
        }

        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }
}