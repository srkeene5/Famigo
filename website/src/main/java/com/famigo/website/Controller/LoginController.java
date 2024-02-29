package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.famigo.website.model.Login;
import com.famigo.website.repositories.UserRepository;

@Controller
public class LoginController {
	@Autowired
	UserRepository userRepository;

	@GetMapping("/login")
	public String greeting(Model model) {
		model.addAttribute("login", new Login());
		return "register";
	}
}
 
