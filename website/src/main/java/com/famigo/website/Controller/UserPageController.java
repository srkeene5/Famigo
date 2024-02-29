package com.famigo.website.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.famigo.website.model.Signup;
import com.famigo.website.utilities.Util;

import com.famigo.website.repository.UserRepository;
import com.famigo.website.model.User;
@Controller
public class UserPageController {

	@Autowired
    UserRepository userRepository;
	
	@GetMapping("/user")
	public String greeting(Model model) {
		User user = userRepository.getUser("id", Util.getUserID());
		model.addAttribute("signup", new Signup());
		model.addAttribute("userpage", user);
		model.addAttribute("user_logged_in", user);
		//Should probably return a different user page or use some th:if statements so that you don't have to return this
        return "userpage";
	}

	@GetMapping("/user/{username}")
	public String userPage(@PathVariable String username, Model model) {
		//UserRepository userRepo = new UserRepository();
		User user_being_viewed = userRepository.getUser("username", username/*util.getUserID()*/); // accidentally made the user always go to their own page, oops
		User user_logged_in = userRepository.getUser("username", Util.getUserID());
		if (user_being_viewed != null) {
			System.out.println("username of user's page being viewed: " + user_being_viewed.getUsername());
			System.out.println("password of user's page being viewed: " + user_being_viewed.getPassword());
			System.out.println("Logged in user: " + user_logged_in.getUsername());
		}
		model.addAttribute("userpage", user_being_viewed);
		model.addAttribute("user_logged_in", user_logged_in);
        return "userpage";
	}
}