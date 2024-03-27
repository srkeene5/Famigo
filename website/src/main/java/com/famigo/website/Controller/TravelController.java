package com.famigo.website.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.famigo.website.model.Conversation;
import com.famigo.website.model.Event;
import com.famigo.website.model.Place;
import com.famigo.website.model.SubEvent;
import com.famigo.website.model.SubInvite;
import com.famigo.website.model.SubTrip;
import com.famigo.website.model.TravelGroup;
import com.famigo.website.model.Trip;
import com.famigo.website.model.User;
import com.famigo.website.repositories.MessageRepository;
import com.famigo.website.repositories.PlaceRepository;
import com.famigo.website.repositories.TravelRepository;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.IDSize;
import com.famigo.website.utilities.InviteStatus;
import com.famigo.website.utilities.Utilities;

@Controller
public class TravelController {
    
    @Autowired
    UserRepository ur;
    @Autowired
    TravelRepository tr;
    @Autowired
    PlaceRepository pr;
    @Autowired
    MessageRepository mr;

    @GetMapping("/trips/{tripID}")
    public String events(Model model, @PathVariable String tripID) {
        Trip t = tr.getTrip(tripID);
        List<Event> events = tr.getEvents(tripID);
        List<Place> places = pr.getPlacesFromNames(pr.getPlaces());
        model.addAttribute("trip", t);
        model.addAttribute("events", events);
        model.addAttribute("places", places);
        model.addAttribute("isowner", Utilities.getUserID().equals(t.getOwner().getID()));
        return "trip.html";
    }

    @RequestMapping(value="/trips/{tripID}/new-event", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createEvent(@RequestBody SubEvent event, @PathVariable String tripID) {
        Event e = new Event(Utilities.generateID(IDSize.EVENTID), event.getName(), Utilities.getUserID(), pr.getPlaceById(event.getPlace()), LocalDateTime.parse(event.getStart()), LocalDateTime.parse(event.getEnd()), event.getDescription(), tripID);
        tr.createEvent(e);
        Map<String, String> result = new HashMap<String, String>();
        result.put("eid", e.getID());
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/trips/{tripID}/{eventID}/edit-event", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> editEvent(@PathVariable String tripID, @PathVariable String eventID, @RequestBody SubEvent event) {
        Event e = new Event(eventID, event.getName(), Utilities.getUserID(), pr.getPlaceById(Integer.parseInt(event.getPlace())), LocalDateTime.parse(event.getStart()), LocalDateTime.parse(event.getEnd()), event.getDescription(), tripID);
        tr.editEvent(e);
        Map<String, String> result = new HashMap<String, String>();
        result.put("eid", e.getID());
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/trips/{tripID}/{eventID}/delete-event", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable String tripID, @PathVariable String eventID) {
        Event e = tr.getEvent(eventID);
        Trip t = tr.getTrip(tripID);
        if (Utilities.getUserID().equals(e.getCreator()) || Utilities.getUserID().equals(t.getOwner().getID()) || !t.getTravelGroupID().equals("NULL")) {
            tr.deleteEvent(eventID);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/trips/{tripID}/{eventID}")
    public String viewEvent(Model model, @PathVariable String eventID) {
        Event e = tr.getEvent(eventID);
        List<Place> places = pr.getPlacesFromNames(pr.getPlaces());
        model.addAttribute("event", e);
        model.addAttribute("places", places);
        return "viewEvent.html";
    }

    @GetMapping("/trips")
    public String trips(Model model) {
        ArrayList<Trip> tripsArr = tr.getTrips();
        ArrayList<Trip> inviteList = tr.getTripInvites();
        ArrayList<String> users = ur.getAllUsernames();
        users.remove(Utilities.getUsername());
        model.addAttribute("trips", tripsArr);
        model.addAttribute("invites", inviteList);
        model.addAttribute("users", users);
        return "trips.html";
    }

    @RequestMapping(value="/trips", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> evaluateInvite(Model model, @RequestBody SubInvite result) {
        if (result.getResult().equals("Accept")) {
            tr.evaluateRequest(result.getTripID(), Utilities.getUserID(), InviteStatus.ACCEPTED);
        }
        else {
            tr.evaluateRequest(result.getTripID(), Utilities.getUserID(), InviteStatus.REFUSED);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @RequestMapping(value="/trips/new-trip", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createTrip(Model model, @RequestBody SubTrip result) {
        ArrayList<User> members = new ArrayList<>();
        members.add(ur.getUser("id", Utilities.getUserID()));
        for (int i = 0; i < result.getMembers().size(); i++) {
            members.add(ur.getUserByUsername(result.getMembers().get(i)));
        }
        Trip t = new Trip(Utilities.generateID(IDSize.TRIPID), result.getName(), ur.getUser("id", Utilities.getUserID()), members, LocalDateTime.now(), result.getDescription(), "NULL");
        tr.createTrip(t);
        Map<String, Object> map = new HashMap<>();
        map.put("id", t.getID());
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @RequestMapping(value="/trips/{tripID}/delete-trip", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteTrip(@PathVariable String tripID) {
        Trip t = tr.getTrip(tripID);
        if (Utilities.getUserID().equals(t.getOwner().getID()) || (!t.getTravelGroupID().equals("NULL") && tr.getGroup(t.getTravelGroupID()).getOwner().getID().equals(Utilities.getUserID()))) {
            tr.deleteTrip(tripID);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/groups")
    public String getGroups(Model model) {
        ArrayList<TravelGroup> travelGroups = tr.getTravelGroups();
        ArrayList<TravelGroup> invites = tr.getGroupInvites();
        ArrayList<String> allUsers = ur.getAllUsernames();
        model.addAttribute("groups", travelGroups);
        model.addAttribute("invites", invites);
        model.addAttribute("users", allUsers);
        return "groups.html";
    }

    @RequestMapping(value="/groups", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> evaluateGroupInvite(Model model, @RequestBody SubInvite result) {
        if (result.getResult().equals("Accept")) {
            tr.evaluateGroupRequest(result.getTripID(), InviteStatus.ACCEPTED);
            TravelGroup g = tr.getGroup(result.getTripID());
            mr.addUserToConversation(Utilities.getUserID(), g.getConversationID());
        }
        else {
            tr.evaluateGroupRequest(result.getTripID(), InviteStatus.REFUSED);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @RequestMapping(value="/groups/new-group", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createGroup(Model model, @RequestBody SubTrip result) {
        ArrayList<User> members = new ArrayList<>();
        members.add(ur.getUser("id", Utilities.getUserID()));
        for (int i = 0; i < result.getMembers().size(); i++) {
            members.add(ur.getUserByUsername(result.getMembers().get(i)));
        }
        ArrayList<User> current = new ArrayList<>();
        current.add(ur.getUser("id", Utilities.getUserID()));
        Conversation c = new Conversation(Utilities.generateID(IDSize.CONVERSATIONID), result.getName(), current);
        mr.addConversation(c);
        TravelGroup t = new TravelGroup(Utilities.generateID(IDSize.TRIPID), result.getName(), ur.getUser("id", Utilities.getUserID()), LocalDateTime.now(), result.getDescription(), members, c.getID());
        tr.createTravelGroup(t);
        Map<String, Object> map = new HashMap<>();
        map.put("id", t.getID());
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

    @RequestMapping(value="/groups/{groupID}/delete-group", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable String groupID) {
        TravelGroup t = tr.getGroup(groupID);
        if (Utilities.getUserID().equals(t.getOwner().getID())) {
            tr.deleteGroup(groupID);
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/groups/{groupID}")
    public String group(Model model, @PathVariable String groupID) {
        TravelGroup group = tr.getGroup(groupID);
        ArrayList<Trip> trips = tr.getGroupTrips(groupID);
        model.addAttribute("group", group);
        model.addAttribute("trips", trips);
        model.addAttribute("users", group.getMembers());
        model.addAttribute("currentuser", Utilities.getUsername());
        model.addAttribute("isowner", Utilities.getUserID().equals(group.getOwner().getID()));
        return "group.html";
    }

    @RequestMapping(value="/groups/{groupID}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createGroupTrip(Model model, @PathVariable String groupID, @RequestBody SubTrip result) {
        ArrayList<User> members = new ArrayList<>();
        members.add(ur.getUser("id", Utilities.getUserID()));
        for (int i = 0; i < result.getMembers().size(); i++) {
            members.add(ur.getUserByUsername(result.getMembers().get(i)));
        }
        Trip t = new Trip(Utilities.generateID(IDSize.TRIPID), result.getName(), ur.getUser("id", Utilities.getUserID()), members, LocalDateTime.now(), result.getDescription(), groupID);
        tr.createTrip(t);
        Map<String, Object> map = new HashMap<>();
        map.put("id", t.getID());
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }

}
