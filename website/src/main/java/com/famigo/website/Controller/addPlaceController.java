package com.famigo.website.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.PostMapping;

import com.famigo.website.Place;
import com.famigo.website.PlaceRepository;

@Controller
public class addPlaceController {

    @Autowired
    PlaceRepository placeRepository;
    
    @RequestMapping(value = "/addplace", method = RequestMethod.GET)
    public String add_place_site(ModelMap model) {
        System.out.println("adding place...");
        return "addplace";
    }

    @PostMapping(value = "/addplace"/*, method = RequestMethod.POST*/)
    public ResponseEntity<String> submit_place(@RequestBody Place place) {
        System.out.println(place.getName());
        Place placeObject = new Place("0", place.getName(), "100");
        System.out.println("BEFORE");
        placeRepository.addPlace(placeObject);
        System.out.println("place added...");
        System.out.println("List:\n" + placeRepository.getPlaces());
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
