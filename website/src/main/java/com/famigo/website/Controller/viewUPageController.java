package com.famigo.website.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.famigo.website.model.Comment;
import com.famigo.website.model.Review;
import com.famigo.website.repositories.CommentRepository;
import com.famigo.website.repositories.ReviewRepository;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("name")
public class viewUPageController {

    @Autowired
    ReviewRepository rr;

    @Autowired
    CommentRepository cr;

    @RequestMapping(value = "/visitUserPage", method = RequestMethod.GET)
    public String showOtherUserPage(ModelMap model) {
        return "visitUserPage";
    }

    @RequestMapping(value = "/getStats", method = RequestMethod.POST, produces = "text/plain")
    public @ResponseBody String sendStats(@RequestParam String userId) {
        System.out.println(userId);

        StringBuilder buffer = new StringBuilder();
        ArrayList<Review> revs = rr.getReviewsByUser(userId);
        ArrayList<Comment> coms = cr.getCommentsByUser(userId);
        buffer.append("\"");

        buffer.append("Review Statistics\n");
        int starTotal = 0;
        if (revs != null) {
            for (Review rev : revs) {
                starTotal += rev.getStars();
            }
            buffer.append("Total Reviews: " + revs.size() + "\n");
            buffer.append("Average Star Rating: " + (starTotal / revs.size()) + "\n");
        } else {
            buffer.append("No Reviews Posted\n");
        }

        buffer.append("\nComment Statistics\n");
        if (coms != null) {
            int likeTotal = 0;
            for (Comment com : coms) {
                likeTotal += com.getLikes();
            }
            buffer.append("Total Comments: " + coms.size() + "\n");
            buffer.append("Average Likes per Comment: " + (likeTotal / coms.size()) + "\n");
        } else {
            buffer.append("No Comments Posted\n");
        }

        buffer.append("\"");
        System.out.println(buffer.toString());
        return buffer.toString();
    }
}
