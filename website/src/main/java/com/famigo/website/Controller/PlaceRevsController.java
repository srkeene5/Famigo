package com.famigo.website.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.famigo.website.Service.PlaceRevsService;
import com.famigo.website.model.*;

@Controller
public class PlaceRevsController {

    @Autowired
    PlaceRevsService service;

    @RequestMapping(value = "/placeRevs", method = RequestMethod.GET)
    public String showPlaceRevList(ModelMap model) {
        model.addAttribute("userIds", service.getAllUserIds());
        model.addAttribute("stars", service.getAllStars());
        model.addAttribute("text", service.getAllRevText());
        model.addAttribute("revIds", service.getAllRevIds());
        return "placeRevs";
    }

    @RequestMapping(value = "/placeRevs", method = RequestMethod.POST)
    public ResponseEntity<String> likeOrDislike(@RequestParam(value="vals[]") int[] vals) {

        System.out.println(vals[0]);
        System.out.println(vals[1]);

        // Save like/dislike action
        if (vals[0] == 1) {
            // Like button pressed
            
        } else {
            // Dislike button pressed
            
        }

        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }

}
