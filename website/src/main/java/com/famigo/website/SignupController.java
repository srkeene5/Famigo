package com.famigo.website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

	@GetMapping("/signup")
	public String greeting(Model model) {
		model.addAttribute("signup", new Signup());
		return "enroll";
	}

	@PostMapping("/signup")
  	public String greetingSubmit(@ModelAttribute Signup signup, Model model) {
    	model.addAttribute("signup", signup);
		//kind of a behemoth of a statement but basically the first if checks if the password is valid and the two passwords match
		//second checks to make sure that all of the boxes were entered
		//TODO: make email verification and check for username duplicates (third and maybe fourth if loop)

		if (ValidateText.isPasswordValid(signup.getPassword()) && signup.getPassword().equals(signup.getConfirmPassword())) {
			if (signup.getFirstName() != null && signup.getLastName() != null && signup.getUsername() != null && signup.getEmail() != null && !signup.getGender().equals("")) {
				return "redirect:/user";
			}
		}

		return "failedenroll";
  	}
}