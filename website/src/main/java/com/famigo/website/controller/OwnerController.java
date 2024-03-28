package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.model.OwnerRequest;
import com.famigo.website.model.SubReview;
import com.famigo.website.repositories.FollowingRepository;
import com.famigo.website.repositories.OwnerRepository;
import com.famigo.website.repositories.PlaceRepository;
import com.famigo.website.repositories.ReviewRepository;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.Utilities;

@Controller
public class OwnerController {

    @Autowired
    private PlaceRepository pr;
    @Autowired
    private OwnerRepository or;

    @RequestMapping(value = "/places/{name}/requestOwnerStatus", method = RequestMethod.POST)
    public ResponseEntity<String> sendReqToModForOwnerStatus(@RequestParam("placeId") int placeId, @RequestParam("sendReq") Boolean sendReq) {
        //Now we know user wants to be the owner of {name}
        System.out.println("reached: sendreq from " + placeId + " is " + sendReq + "!");
        
        //Want to add request to repo
        String userId = Utilities.getUserID();
        //int placeId = pr.getPlaceByName(name).getId();
        OwnerRequest request = new OwnerRequest(userId, placeId);
        or.storeRequest(request);
        System.out.println("Request = " + userId + " wants to be owner of " + placeId + ", which is " + pr.getPlaceByID(placeId).getName());
        or.printAllRequests();
        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }

    @RequestMapping(value = "/mod/giveOwnerStatus", method = RequestMethod.POST)
    public ResponseEntity<String> giveOwnerStatus(@RequestParam("give") Boolean give, @RequestParam("ownerReqId") int ownerReqId) {
        //Now we know user wants to be the owner of {name}
        System.out.println("ownerReqId=" + ownerReqId + ", give=" + give);
        System.out.println("HELLOOOOOOOO");
        /*//Want to add request to repo
        String userId = Utilities.getUserID();
        //int placeId = pr.getPlaceByName(name).getId();
        OwnerRequest request = new OwnerRequest(userId, placeName);
        or.storeRequest(request);
        System.out.println("Request = " + userId + " wants to be owner of " + placeName + ", which is " + pr.getPlaceByID(placeId).getName());
        or.printAllRequests();*/
        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }
}
