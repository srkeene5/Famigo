package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.famigo.website.model.*;
import com.famigo.website.service.PlaceRevsService;

@Controller
public class PlaceRevsController {

    @Autowired
    PlaceRevsService service;

    @RequestMapping(value = "/placeRevs", method = RequestMethod.GET)
    public String showPlaceRevList(ModelMap model) {
        model.addAttribute("reviewTextCombined", service.getAllReviewText());
        return "placeRevs";
    }

}