package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.repositories.FollowingRepository;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.Utilities;
import com.famigo.website.model.User;
import java.util.List;

@Controller
public class FollowController {
        @Autowired
        UserRepository userRepository;

        @Autowired
        FollowingRepository followRepo;

        @GetMapping("/user/{username}/followers")
        public String followerList(@PathVariable String username, Model model) {
                List<String> followers = followRepo.getFollowersList(username);
                System.out.println("the list: " + followers);
                model.addAttribute("followers", followers);
                return "followersPage";
        }
        @GetMapping("/user/{username}/following")
        public String followingList(@PathVariable String username, Model model) {
                List<String> followings = followRepo.getFollowingList(username);
                System.out.println("the list: " + followings);
                model.addAttribute("followings", followings);
                return "followingPage";
        }

        @PostMapping("/follow")
        public String followUser(Model model, @RequestParam("userDoingFollowing") String userDoingFollowing,
                        @RequestParam("userToBeFollowed") String userToBeFollowed,
                        @RequestParam("followerCount") int followerCount,
                        @RequestParam("isFollowing") Boolean isFollowing) {
                User user = userRepository.getUser("id", Utilities.getUserID());

                System.out.println("GOT TO FOLLOW CONTROLLER");
                System.out.println("This is the user doing the following: " + userDoingFollowing);
                System.out.println("This is the user being followed: " + userToBeFollowed);
                System.out.println("This is the follower count: " + followerCount);
                System.out.println("Is the user following? " + isFollowing);

                System.out.println("USERNAME::::" + (userRepository.getUserByUsername(userToBeFollowed)).getUsername());

                // followRepo.createFollow(userRepository.getUserByUsername(userToBeFollowed));

                // followRepo.followUser(followerCount);

                userToBeFollowed = userRepository.getUserByUsername(userToBeFollowed).getID();
                userDoingFollowing = userRepository.getUserByUsername(userDoingFollowing).getID();

                followRepo.followUser(userToBeFollowed, userDoingFollowing);

                System.out.print("user being followed aka " + userToBeFollowed + " has...");
                System.out.print(followRepo.getNumFollowers(userToBeFollowed) + " followers\n");
                System.out.println();
                System.out.print("(testing out of curiousity) user doing follow aka " + userDoingFollowing + " has...");
                System.out.print(followRepo.getNumFollowers(userDoingFollowing) + "followers\n");

                System.out.println("----------------------");

                System.out.print("user being followed aka " + userToBeFollowed + " has...");
                System.out.print(followRepo.getNumFollowers(userToBeFollowed) + " followers\n");
                System.out.println();
                System.out.print("(testing out of curiousity) user doing follow aka " + userDoingFollowing + " has...");
                System.out.print(followRepo.getNumFollowers(userDoingFollowing) + "followers\n");

                System.out.println("----------------------");

                System.out.println(userToBeFollowed + " aka user to be followed follower list");
                System.out.print(followRepo.getFollowersList(userToBeFollowed));
                System.out.println();
                System.out.println(userDoingFollowing + " aka logged in user follower list");
                System.out.print(followRepo.getFollowersList(userDoingFollowing));
                System.out.println();


                System.out.println("XXXXXXXXX");
                //System.out.println("friends?" + followRepo.areFriends(userToBeFollowed, userDoingFollowing));
                System.out.println("XXXXXXXXX");
                return "redirect:/user/" + user.getUsername(); // takes user back to their own page; placeholder until i
                                                               // figure out how to redirect to same page
        }

        @PostMapping("/unfollow")
        public String unfollowUser(Model model, @RequestParam("userDoingUnFollowing") String userDoingUnFollowing,
                        @RequestParam("userToBeUnFollowed") String userToBeUnFollowed,
                        @RequestParam("followerCount") int followerCount,
                        @RequestParam("isFollowing") Boolean isFollowing) {
                //User user = userRepository.getUser("id", Utilities.getUserID());

                followRepo.unfollowUser(userToBeUnFollowed, userDoingUnFollowing);

            

                System.out.print("(testing out of curiousity) user doing unfollow aka " + userDoingUnFollowing + " has...");
                System.out.print(followRepo.getNumFollowers(userDoingUnFollowing) + "followers\n");

                System.out.println("----------------------");

                System.out.println(userToBeUnFollowed + " aka user to be unfollowed follower list");
                System.out.print(followRepo.getFollowersList(userToBeUnFollowed));
                System.out.println();
                System.out.println(userDoingUnFollowing + " aka logged in user follower list");
                System.out.print(followRepo.getFollowersList(userToBeUnFollowed));
                System.out.println();
                return "Unfollow successful";
        }

}
