package com.famigo.website;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("login", new Login());
		return "register";
	}

	@PostMapping("/login")
  	public String greetingSubmit(@ModelAttribute Login login, Model model) {
    	model.addAttribute("login", login);
    	
		if (login.getPassword() == null && login.getUsername() == null) {
			return "signup";
		}

		//TODO: Change this to check database for the username and password combo
		if (ValidateText.isPasswordValid(login.getPassword())) {
			return "userpage";
		}
		
		login.setPassword(null);
		login.setUsername(null);
		return "failedlogin";
  	}
}
 