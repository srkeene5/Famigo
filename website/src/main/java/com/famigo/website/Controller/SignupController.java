package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.famigo.website.model.Signup;
import com.famigo.website.model.User;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.service.EmailService;
import com.famigo.website.utilities.Role;
import com.famigo.website.utilities.Utilities;
import com.famigo.website.utilities.ValidateText;
import com.famigo.website.utilities.Visibility;
import com.famigo.website.utilities.IDSize;

@Controller
public class SignupController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailService es;

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
				User existing = userRepository.getUser("username", signup.getUsername());
				if (existing == null) {
					String verification = Utilities.generateID(50);
					String password = "{bcrypt}"+BCrypt.hashpw(signup.getPassword(), BCrypt.gensalt());
					String userID = Utilities.generateID(IDSize.USERID);
					while (userRepository.isUserID(userID)) {
						userID = Utilities.generateID(IDSize.USERID);
					}
					User user = new User(userID, signup.getUsername(), signup.getEmail(), password, signup.getFirstName().concat(" " + signup.getLastName()), "", Visibility.ALL, Role.USER);
					userRepository.createUser(user);
					userRepository.addVerificationLink(verification, userID);
					SimpleMailMessage message = new SimpleMailMessage();
					message.setTo(signup.getEmail());
					message.setSubject("Verify Registration");
					message.setText("Click this link to verify registration: http://localhost:8080/signup/" + verification);
					es.sendEmail(message);
					return "emailMessage.html";
				}
				signup.setUsername("");
				return "failedenroll";
			}
		}

		return "failedenroll";
  	}

	@GetMapping("/signup/{link}")
	public String verifyPage(Model model) {
		model.addAttribute("confirm", "none");
		return "verify.html";
	}

	@RequestMapping(value="/signup/{link}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpStatus> confirmSignup(Model model, @PathVariable String link) {
		if (userRepository.verify(link)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
}