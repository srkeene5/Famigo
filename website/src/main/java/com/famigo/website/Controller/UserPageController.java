package com.famigo.website.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.famigo.website.model.Signup;
import com.famigo.website.utilities.Utilities;

import com.famigo.website.repositories.UserRepository;
import com.famigo.website.repositories.FollowingRepository;
import com.famigo.website.model.User;
import com.famigo.website.model.SubFollow;

@Controller
public class UserPageController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FollowingRepository followRepository;

	@GetMapping("/user")
	public String greeting(Model model) {
		User user = userRepository.getUser("id", Utilities.getUserID());
		model.addAttribute("signup", new Signup());
		model.addAttribute("userpage", user);
		model.addAttribute("user_logged_in", user);
		// Should probably return a different user page or use some th:if statements so
		// that you don't have to return this
		return "userpage";
	}

	@GetMapping("/user/{username}")
	public String userPage(@PathVariable String username, Model model) {
		// UserRepository userRepo = new UserRepository();
		User user_being_viewed = userRepository.getUser("username", username/* util.getUserID() */); // accidentally
																										// made the user
																										// always go to
																										// their own
																										// page, oops
		User user_logged_in = userRepository.getUser("username", Utilities.getUserID());
		SubFollow followerCount = new SubFollow();
		// followerCount.setFollowerCount(followRepository.getFollowerCount(user_being_viewed.getUsername()));
		// debugging
		if (user_being_viewed != null) {
			System.out.println("username of user's page being viewed: " + user_being_viewed.getUsername());
			// System.out.println("password of user's page being viewed: " +
			// user_being_viewed.getPassword());
			System.out.println("Logged in user: " + user_logged_in.getUsername());
			// System.out.println("Num followers of logged-in user: " +
			// user_logged_in.getFollowerCount());
			// System.out.println("Num followers of USER BEING VIEWED: " +
			// user_being_viewed.getFollowerCount());
		}
		System.out.println("~~~~~~~user page controller~~~~~~~~~~~");
		System.out.println("Follower count: " + followRepository.getNumFollowers(user_being_viewed.getUsername()));
		System.out.println("Following count " + followRepository.getNumFollowing(user_being_viewed.getUsername()));
		System.out.println("Followers list " + followRepository.getFollowersList(user_being_viewed.getUsername()));
		System.out.println("Following list " + followRepository.getFollowingList(user_being_viewed.getUsername()));
		System.out.println("~~~~~~~~~~~~~~~~~~");

		model.addAttribute("userpage", user_being_viewed); // get Username
		model.addAttribute("user_logged_in", user_logged_in); // get Username
		model.addAttribute("follower_count_of_user", followRepository.getNumFollowers(user_being_viewed.getUsername()));
		model.addAttribute("following_count_of_user",
				followRepository.getNumFollowing(user_being_viewed.getUsername()));
		return "userpage";
	}
}