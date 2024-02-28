package com.famigo.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	@Autowired
	UserRepository userRepository;

	@GetMapping("/login")
	public String greeting(Model model) {
		model.addAttribute("login", new Login());
		return "register";
	}

	@PostMapping("/login")
  	public String greetingSubmit(@ModelAttribute Login login, Model model) {
    	model.addAttribute("login", login);
    	
		if (login.getPassword() == null && login.getUsername() == null) {
			return "redirect:/signup";
		}

		//TODO: Change this to check database for the username/email and password combo
		if (ValidateText.isPasswordValid(login.getPassword())) {
			User user = userRepository.getUser(login.getUsername());

			if (user != null && user.getPassword().equals(login.getPassword())) {
				return "redirect:/user";
			}
		}
		
		login.setPassword(null);
		return "failedlogin";
  	}
}
 