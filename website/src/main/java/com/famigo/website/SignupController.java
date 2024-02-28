package com.famigo.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

	@Autowired
	UserRepository userRepository;

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
		//TODO: make email verification

		if (ValidateText.isPasswordValid(signup.getPassword()) && signup.getPassword().equals(signup.getConfirmPassword())) {
			if (signup.getFirstName() != null && signup.getLastName() != null && signup.getUsername() != null && signup.getEmail() != null && !signup.getGender().equals("")) {
				User existing = userRepository.getUser(signup.getUsername());
				if (existing == null) {
					User user = new User(signup.getUsername(), signup.getUsername(), signup.getEmail(), signup.getPassword(), signup.getFirstName().concat(" " + signup.getLastName()), "", Visibility.ALL, Role.USER);
					userRepository.createUser(user);
					return "redirect:/user";
				}
				signup.setUsername("");
				return "failedenroll";
			}
		}

		return "failedenroll";
  	}
}