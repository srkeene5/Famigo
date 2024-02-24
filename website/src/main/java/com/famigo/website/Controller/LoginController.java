package com.famigo.website.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.famigo.website.model.Login;
import com.famigo.website.service.ValidateText;

@Controller
public class LoginController {

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

		// TODO: Change this to check database for the username/email and password combo
		if (ValidateText.isPasswordValid(login.getPassword())) {
			return "redirect:/user";
		}

		login.setPassword(null);
		login.setUsername(null);
		return "failedlogin";
	}
}
