package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.famigo.website.repositories.ReviewRepository;

@Controller
@SessionAttributes("name")
public class viewUPageController {

    @Autowired
    ReviewRepository rr;

    @RequestMapping(value = "/visitUserPage", method = RequestMethod.GET)
    public String showOtherUserPage(ModelMap model) {
        return "visitUserPage";
    }
}
