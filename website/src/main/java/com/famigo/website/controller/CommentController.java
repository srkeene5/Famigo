package com.famigo.website.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.famigo.website.model.*;
import com.famigo.website.repositories.CommentRepository;
import com.famigo.website.repositories.ReviewRepository;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.Utilities;

@Controller
@SessionAttributes("name")
public class CommentController {

    boolean testing = true;

    @Autowired
    CommentRepository comRepo;

    @Autowired
    UserRepository userRepo;

    @RequestMapping(value = "/addComment", method = RequestMethod.GET)
    public String showCommentPage(ModelMap model) {
        return "addComment";
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<String> submitComment(@RequestParam("comment") String commentBody, @RequestParam("url") String url) {
        System.out.println("ADDCOMMENT POST REACHED");
        System.out.println("The comment: " + commentBody);

        //Comment comment = new Comment(Utilities.getUserID(), subCom.getComPlace(), subCom.getComText(), Integer.parseInt(subCom.getComLikes()));
        User user = userRepo.getUser("id", Utilities.getUserID());
        /*String reviewID = 
        Comment comment = new Comment(user.getID(), reviewID, commentBody, 0);
        comRepo.addComment(comment);
        System.out.println("User: " + comment.getUserId());
        System.out.println("ReviewID: " + comment.getComId());
        System.out.println("Review: " + comment.getComment());
        //System.out.println("PlaceID: " + comment.getPlaceId());
        System.out.println("Likes: " + comment.getLikes());
        System.out.println("Dislikes: " + comment.getDislikes());
        if (testing) {
            ArrayList<Comment> uCommentArr = comRepo.getCommentsByUser(Utilities.getUserID());
            //ArrayList<Comment> pRevArr = comRepo.getReviewsByPlace(subCom.getrPlace());
            System.out.println("_____Print Review By UserID_____");
            for (Comment uComment : uCommentArr) {
                System.out.println("User: " + uComment.getUserId());
                //System.out.println("ReviewID: " + uRev.getRevId());
                //System.out.println("Review: " + uRev.getReview());
                //System.out.println("Stars: " + uRev.getStars());
                //System.out.println("PlaceID: " + uRev.getPlaceId());
                System.out.println("Likes: " + uComment.getLikes());
                System.out.println("Dislikes: " + uComment.getDislikes());
                System.out.println("Comment: " + uComment.getComment());
                System.out.println();
            }
            /*System.out.println("_____Print Review By PlaceID_____");
            for (Review pRev : pRevArr) {
                System.out.println("User: " + pRev.getUserId());
                System.out.println("ReviewID: " + pRev.getRevId());
                System.out.println("Review: " + pRev.getReview());
                System.out.println("Stars: " + pRev.getStars());
                System.out.println("PlaceID: " + pRev.getPlaceId());
                System.out.println();
            }*/
        //}


        // Keep us on same page
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build(); 
    }

}
