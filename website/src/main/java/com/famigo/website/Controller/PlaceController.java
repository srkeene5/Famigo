package com.famigo.website.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.famigo.website.repositories.CommentRepository;
import com.famigo.website.repositories.PlaceRepository;
import com.famigo.website.repositories.ReviewRepository;
import com.famigo.website.model.Review;
import com.famigo.website.model.Comment;
import com.famigo.website.model.SubReview;
import com.famigo.website.utilities.Utilities;

@Controller
public class PlaceController {

    private boolean testing = false;

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ReviewRepository rr;
    @Autowired
    private CommentRepository cr;

    @GetMapping("/places/{name}")
    public String showPlaceDetails(@PathVariable String name, Model model) {
        Place place = placeRepository.getPlaceByName(name);
        System.out.println("place="+place);
        // model.addAttribute("place", place);
        model.addAttribute("selectedPlace", place);
        model.addAttribute("placePath", "/places/" + name);
        model.addAttribute("placeId", place.getId());

        // Get reviews and comments for this place
        ArrayList<Review> reviews = rr.getReviewsByPlace(place.getId());
        if (reviews == null || reviews.isEmpty()) {
            model.addAttribute("reviews", new Review[0]);
            model.addAttribute("userReactions", new int[0]);
            model.addAttribute("comments", new Comment[0]);
        } else {
            model.addAttribute("reviews", reviews.toArray());
            
            Map<Integer, List<Comment>> commentsOnReviews = new HashMap<>();
            for (Review review : reviews) {
                //String ridStr = String.valueOf(review.getRevId());
                //System.out.println("rid: " + review.getRevId());
                List<Comment> comments = cr.getCommentsByReview(review.getRevId());
                commentsOnReviews.put(review.getRevId(), comments);
            }

            // test to see if array is correct
            /*for (Map.Entry<Integer, List<Comment>> entry : commentsOnReviews.entrySet()) {
                int review = entry.getKey();
                List<Comment> commentList = entry.getValue();
                System.out.println("Review: " + review);
                System.out.println("Comment list: ");
                if (commentList == null) continue;
                for (int i = 0; i < commentList.size(); i++) {
                    System.out.println("---" + commentList.get(i).getComment()); 
                }
            }*/

            model.addAttribute("commentsOnReviews", commentsOnReviews);

            int[] revReactions = rr.getUserReviewReactions(Utilities.getUserID(), reviews);
            model.addAttribute("revReactions", revReactions);
            HashMap<Integer, List<Integer>> cReations = cr.getUserCommentReactions(Utilities.getUserID(), commentsOnReviews);
            model.addAttribute("cReactions", cReations);

        }
        return "place-details";
    }
    
    @PostMapping("/places")
    public String placesGet(Model model) {
        model.addAttribute("placeNamesList", placeRepository.getPlaces());
        return "place-search";
    }

    /*@PostMapping("/places")
    public String placesPost(Model model) {
        //model.addAttribute("placeNamesList", placeRepository.getPlaces());
        return "redirect:/places";
    }*/

    @RequestMapping(value = "/places/{name}/addRev", method = RequestMethod.POST)
    public ResponseEntity<String> submitReview(@PathVariable String name, @RequestBody SubReview sr) {
        rr.addReview(new Review(Utilities.getUserID(), sr.getrPlace(),
                sr.getrText(), Integer.parseInt(sr.getrStar())));

        if (testing) {
            ArrayList<Review> uRevArr = rr.getReviewsByUser(Utilities.getUserID());
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
    public ResponseEntity<String> likeOrDislike(@RequestParam(value = "vals[]") int[] vals) {
        // Save like/dislike action
        // vals[0] = like (1) or dislike (0), and values[1] = review ID
        if (vals[0] == 1) {
            // Like button pressed
            rr.alterReviewReaction(Utilities.getUserID(), vals[1], true);
        } else {
            // Dislike button pressed
            rr.alterReviewReaction(Utilities.getUserID(), vals[1], false);
        }

        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }
}