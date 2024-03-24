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
    CommentRepository commentRepo;

    @Autowired
    UserRepository userRepo;


    @Autowired
    ReviewRepository reviewRepo;

    @RequestMapping(value = "/addComment", method = RequestMethod.GET)
    public String showCommentPage(ModelMap model) {
        return "addComment";
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResponseEntity<String> submitComment(@RequestParam("comment") String commentBody, @RequestParam("url") String url, @RequestParam("revId") String reviewID) {
        System.out.println("ADDCOMMENT POST REACHED");
        System.out.println("The comment: " + commentBody);

        //Comment comment = new Comment(Utilities.getUserID(), subCom.getComPlace(), subCom.getComText(), Integer.parseInt(subCom.getComLikes()));
        User user = userRepo.getUser("id", Utilities.getUserID());
        //Review review = reviewRepo.get;
        System.out.println("{reviewID: " + reviewID + "}");
        Comment comment = new Comment(user.getID(), reviewID, commentBody, 0);
        commentRepo.addComment(comment);

        // Testing
        int test = 2;
        if (test == 1) {
            System.out.println("-------------------\nCOMMENT INFO:");
            System.out.println("User: " + comment.getUserId());
            System.out.println("ComID: " + comment.getComId());
            System.out.println("RevID: " + comment.getReviewId());
            System.out.println("Comment: " + comment.getComment());
            System.out.println("Likes: " + comment.getLikes());
            System.out.println("Dislikes: " + comment.getDislikes());
            System.out.println("-------------------");
        } else if (test == 2) {
            System.out.println("[Reviews for id: " + comment.getReviewId() + "]");
            ArrayList<Comment> comments = commentRepo.getCommentsByReview(comment.getReviewId());
            for (int i = 0; i < comments.size(); i++) {
                Comment c = comments.get(i);
                System.out.println("User: " + c.getUserId());
                System.out.println("ComID: " + c.getComId());
                System.out.println("RevID: " + c.getReviewId());
                System.out.println("Comment: " + c.getComment());
                System.out.println("Likes: " + c.getLikes());
                System.out.println("Dislikes: " + c.getDislikes());
                System.out.println("-------------------");
            }
        }

        // Keep us on same page after commenting
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build(); 
    }

}
