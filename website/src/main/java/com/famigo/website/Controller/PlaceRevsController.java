package com.famigo.website.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

// Might not work
import com.famigo.website.Service.PlaceRevsService;
import com.famigo.website.model.*;

@Controller
public class PlaceRevsController {

    @Autowired
    PlaceRevsService service;

    @RequestMapping(value = "/placeRevs", method = RequestMethod.GET)
    public String showPlaceRevList(ModelMap model) {

        model.addAttribute("testString", "hello this is a test");

        model.addAttribute("userIds", service.getAllUserIds());
        model.addAttribute("stars", service.getAllStars());
        model.addAttribute("text", service.getAllRevText());
        return "placeRevs";
    }

}
