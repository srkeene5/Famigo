package com.famigo.website.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.famigo.website.model.*;
import com.famigo.website.repositories.ReviewRepository;

@Controller
@SessionAttributes("name")
public class addRevController {

    boolean testing = true;

    @Autowired
    ReviewRepository rr;

    @RequestMapping(value = "/addRev", method = RequestMethod.GET)
    public String showRevPage(ModelMap model) {
        // System.out.println("addRev Requested");
        return "addRev";
    }

    @RequestMapping(value = "/addRev", method = RequestMethod.POST)
    public ResponseEntity<String> submitReview(@RequestBody SubReview sr) {
        // System.out.println("Received");
        rr.addReview(new Review(sr.getrUser(), sr.getrPlace(),
                sr.getrText(), Integer.parseInt(sr.getrStar())));
        if (testing) {
            ArrayList<Review> uRevArr = rr.getReviewsByUser(sr.getrUser());
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
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
