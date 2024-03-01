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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.model.Place;
import com.famigo.website.repositories.PlaceRepository;
import com.famigo.website.repositories.ReviewRepository;
import com.famigo.website.model.Review;
import com.famigo.website.utilities.Util;

@Controller
public class PlaceController {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ReviewRepository rr;

    @GetMapping("/places/{name}")
    public String showPlaceDetails(@PathVariable String name, Model model) {
        Place place = placeRepository.getPlaceByName(name);

        System.out.println("name: " + place.getName());

        System.out.println(place.getName());

        model.addAttribute("place", place);
        model.addAttribute("selectedPlace", place);

        // Get reviews for this place
        ArrayList<Review> reviews = rr.getReviewsByPlace(place.getId());
        if (reviews != null) {
            model.addAttribute("reviews", reviews.toArray());
            int[] userReactions = rr.getUserReviewReactions(Util.getUserID(), reviews);
            model.addAttribute("userReactions", userReactions);
        } else {
            model.addAttribute("reviews", new Review[0]);
            model.addAttribute("userReactions", new int[0]);
        }

        return "place-details";
    }

    @PostMapping("/places")
    public String places(Model model) {

        model.addAttribute("placeNamesList", placeRepository.getPlaces());

        return "place-search";

    }

    /*
    @RequestMapping(value = "/placeRevs", method = RequestMethod.POST)
    public ResponseEntity<String> likeOrDislike(@RequestParam(value="vals[]") int[] vals) {

        System.out.println(vals[0]);
        System.out.println(vals[1]);

        // Save like/dislike action
        if (vals[0] == 1) {
            // Like button pressed
            
        } else {
            // Dislike button pressed
            
        }

        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }
    */
}