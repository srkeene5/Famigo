package com.famigo.website;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

	@GetMapping("/user")
	public String greeting(Model model) {
		model.addAttribute("signup", new Signup());
        return "userpage";
	}
}