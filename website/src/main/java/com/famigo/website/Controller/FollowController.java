package com.famigo.website.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.model.Conversation;
import com.famigo.website.model.Signup;
import com.famigo.website.model.SubConversation;
import com.famigo.website.model.SubFollow;
import com.famigo.website.repository.FollowingRepository;
import com.famigo.website.repository.UserRepository;
import com.famigo.website.utilities.Util;
import com.famigo.website.model.User;
import java.util.Objects;
import java.util.List;

@Controller
public class FollowController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowingRepository followRepo;

    @GetMapping("/user/{username}/followers")
    public String followUser(@PathVariable String username, Model model) {
        List<String> followers = followRepo.getFollowersList(username);
        System.out.println("the list: " + followers);
        model.addAttribute("followers", followers);
        return "followersPage";
    }

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


        System.out.println("USERNAME::::" + (userRepository.getUserByUsername(userToBeFollowed)).getUsername());


        //followRepo.createFollow(userRepository.getUserByUsername(userToBeFollowed));

        //followRepo.followUser(followerCount);

        followRepo.followUser(userToBeFollowed, userDoingFollowing);

        System.out.print("user being followed aka " + userToBeFollowed + " has...");
        System.out.print(followRepo.getNumFollowers(userToBeFollowed) + " followers\n");
        System.out.println();
        System.out.print("(testing out of curiousity) user doing follow aka " + userDoingFollowing  + " has...");
        System.out.print(followRepo.getNumFollowers(userDoingFollowing) + "followers\n");
       
        System.out.println("----------------------");

        System.out.println(userToBeFollowed + " aka user to be followed follower list");
        System.out.print(followRepo.getFollowersList(userToBeFollowed));
        System.out.println();
        System.out.println(userDoingFollowing + " aka logged in user follower list");
        System.out.print(followRepo.getFollowersList(userDoingFollowing));
        System.out.println();
        return "redirect:/user/" + user.getUsername(); // takes user back to their own page; placeholder until i figure out how to redirect to same page
    }


}

        