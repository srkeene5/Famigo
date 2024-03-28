package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.famigo.website.model.Login;
import com.famigo.website.model.SubForgot;
import com.famigo.website.model.SubPass;
import com.famigo.website.model.User;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.service.EmailService;
import com.famigo.website.utilities.Utilities;
import com.famigo.website.utilities.ValidateText;

@Controller
public class LoginController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailService es;

	@GetMapping("/login")
	public String greeting(Model model) {
		model.addAttribute("login", new Login());
		model.addAttribute("email", new SubForgot());
		return "register";
	}

	@PostMapping("/forgot-password")
	public String email(Model model, @ModelAttribute SubForgot email) {
		User user = userRepository.getUserByUsername(email.getUsername());
		if (user == null || !user.getEmail().equals(email.getEmail())) {
			return "emailMessage.html";
		}
		String verification = Utilities.generateID(50);
		userRepository.removeVerificationLink(user.getID());
		userRepository.addVerificationLink(verification, user.getID(), false);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("Forgot Password");
		message.setText("Click this link to reset your password: http://localhost:8080/login/" + verification);
		es.sendEmail(message);
		return "emailMessage.html";
	}

	@GetMapping("/login/{link}")
	public String verifyPage(Model model) {
		model.addAttribute("confirm", "none");
		return "resetPassword.html";
	}

	@RequestMapping(value="/login/{link}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpStatus> confirmSignup(Model model, @PathVariable String link, @RequestBody SubPass passwords) {
		User user = userRepository.getUserIDFromLink(link);
		if (passwords.getNewPass().equals(passwords.getOldPass()) && ValidateText.isPasswordValid(passwords.getNewPass()) && userRepository.verify(link, false)) {
			user.setPassword(passwords.getNewPass());
			userRepository.changePassword(user);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
}
 
