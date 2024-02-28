package com.famigo.website;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
		User user = userRepository.getUser(username);
		if (user != null) {
			System.out.println("username: " + user.getName());
			System.out.println("username: " + user.getPassword());
		}
		model.addAttribute("userpage", user);
        return "userpage";
	}
}