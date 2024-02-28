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
		model.addAttribute("signup", new Signup());
        return "userpage";
	}

	@GetMapping("/user/{username}")
	public String userPage(@PathVariable String username, Model model) {
		//UserRepository userRepo = new UserRepository();
		//Util util = new Util();
		User user = userRepository.getUser("username", username/*util.getUserID()*/); // accidentally made the user always go to their own page, oops
		if (user != null) {
			System.out.println("username: " + user.getUsername());
			System.out.println("password: " + user.getPassword());
		}
		model.addAttribute("userpage", user);
        return "userpage";
	}
}