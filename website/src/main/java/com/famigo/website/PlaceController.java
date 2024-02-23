package com.famigo.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.context.ApplicationContext;

@Controller
public class PlaceController {
    //@Autowired
    //private Place place;
    // Currently we can't pull data from the database, but I think this should work when we can?
    
    @Autowired
    private ApplicationContext context;

    

    @GetMapping("/places/{name}")
    public String showPlaceDetails(/*Model model*/ @PathVariable String name, Model model) {
        
        Place place = context.getBean(Place.class);
        //place.createPlace("purdue", "WL", "0"); //--> will work with database i think


        /*model.addAttribute("place.name", place.getName());
        model.addAttribute("address", place.getAddress());
        model.addAttribute("rating", place.getRating());*/
        model.addAttribute("place", place);
        System.out.println(place.getName() + "this is the name");
        System.out.println(place.getRating() + "this is the rate");
        System.out.println(place.getAddress() + "this is the address");
        return "place-details";

    }
}