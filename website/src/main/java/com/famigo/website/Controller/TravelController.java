package com.famigo.website.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.famigo.website.model.Event;
import com.famigo.website.model.Place;
import com.famigo.website.model.SubEvent;
import com.famigo.website.repositories.PlaceRepository;
import com.famigo.website.repositories.TravelRepository;
import com.famigo.website.utilities.IDSize;
import com.famigo.website.utilities.Utilities;

@Controller
public class TravelController {
    
    @Autowired
    TravelRepository tr;
    @Autowired
    PlaceRepository pr;

    @GetMapping("/events")
    public String events(Model model) {
        List<Event> events = tr.getEvents(Utilities.getUserID());
        List<Place> places = pr.getPlacesFromNames(pr.getPlaces());
        model.addAttribute("events", events);
        model.addAttribute("places", places);
        return "events.html";
    }

    @RequestMapping(value="/new-event", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createEvent(@RequestBody SubEvent event) {
        Event e = new Event(Utilities.generateID(IDSize.EVENTID), event.getName(), Utilities.getUserID(), pr.getPlaceById(event.getPlace()), LocalDateTime.parse(event.getStart()), LocalDateTime.parse(event.getEnd()), event.getDescription());
        tr.createEvent(e);
        Map<String, String> result = new HashMap<String, String>();
        result.put("eid", e.getID());
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/events/{eventID}/edit-event", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> editEvent(@PathVariable String eventID, @RequestBody SubEvent event) {
        Event e = new Event(eventID, event.getName(), Utilities.getUserID(), pr.getPlaceById(event.getPlace()), LocalDateTime.parse(event.getStart()), LocalDateTime.parse(event.getEnd()), event.getDescription());
        tr.editEvent(e);
        Map<String, String> result = new HashMap<String, String>();
        result.put("eid", e.getID());
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    @GetMapping("/events/{eventID}")
    public String viewEvent(Model model, @PathVariable String eventID) {
        Event e = tr.getEvent(eventID);
        List<Place> places = pr.getPlacesFromNames(pr.getPlaces());
        model.addAttribute("event", e);
        model.addAttribute("places", places);
        return "viewEvent.html";
    }

}
