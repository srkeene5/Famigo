package com.famigo.website.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.famigo.website.model.Signup;

@Controller
public class UserPageController {

	@GetMapping("/user")
	public String greeting(Model model) {
		model.addAttribute("signup", new Signup());
		return "userpage";
	}

	@PostMapping("/user")
	public String homeButton(Model model) {
		model.addAttribute("signup", new Signup());
		return "userpage";
	}
}