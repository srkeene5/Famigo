package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PathVariable;
import com.famigo.website.model.Place;
import com.famigo.website.repositories.PlaceRepository;

@Controller
public class PlaceController {
    // @Autowired
    // private Place place;
    // Currently we can't pull data from the database, but I think this should work
    // when we can?

    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping("/places/{name}")
    public String showPlaceDetails(@PathVariable String name, Model model) {
        Place place = placeRepository.getPlaceByName(name);

        System.out.println("name: " + place.getName());

        System.out.println(place.getName());

        model.addAttribute("place", place);
        model.addAttribute("selectedPlace", place);

        System.out.println(place.getName() + "this is the name");
        System.out.println(place.getRating() + "this is the rate");
        System.out.println(place.getAddress() + "this is the address");
        return "place-details";

    }

    @PostMapping("/places")
    public String places(Model model) {

        model.addAttribute("placeNamesList", placeRepository.getPlaces());

        return "place-search";

    }
}