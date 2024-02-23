package com.famigo.website.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.famigo.website.model.*;

@Controller
@SessionAttributes("name")
public class addRevController {

    @RequestMapping(value = "/addRev", method = RequestMethod.GET)
    public String showRevPage(ModelMap model) {
        System.out.println("addRev Requested");
        return "addRev";
    }

    @RequestMapping(value = "/addRev", method = RequestMethod.POST)
    public ResponseEntity<String> submitReview(@RequestBody SubReview subReview) {
        System.out.println("Received");
        System.out.println(subReview.getrStar());
        System.out.println(subReview.getrText());
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
