package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.repositories.FollowingRepository;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.Util;
import com.famigo.website.model.User;

@Controller
public class FollowController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/follow")
    public String followUser(Model model, @RequestParam("userDoingFollowing") String userDoingFollowing,
            @RequestParam("userToBeFollowed") String userToBeFollowed,
            @RequestParam("followerCount") int followerCount,
            @RequestParam("isFollowing") Boolean isFollowing) {
        User user = userRepository.getUser("username", Util.getUserID());

        System.out.println("GOT TO FOLLOW CONTROLLER");
        System.out.println("This is the user doing the following: " + userDoingFollowing);
        System.out.println("This is the user being followed: " + userToBeFollowed);
        System.out.println("This is the follower count: " + followerCount);
        System.out.println("Is the user following? " + isFollowing);
        FollowingRepository followRepo = new FollowingRepository();
        followRepo.followUser(followerCount/* userDoingFollowing, userToBeFollowed, isFollowing */);
        return "redirect:/user/" + user.getUsername(); // takes user back to their own page; placeholder until i figure
                                                       // out how to redirect to same page
    }
}
