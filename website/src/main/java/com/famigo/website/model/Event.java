package com.famigo.website.model;

import java.time.LocalDateTime;

public class Event {
    
    private String id;
    private String name;
    private String creator;
    private Place place;
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private String tripID;
    
    public Event(String id, String name, String creator, Place place, LocalDateTime start, LocalDateTime end, String description, String tripID) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.place = place;
        this.start = start;
        this.end = end;
        this.description = description;
        this.tripID = tripID;
    }

    public String getID() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public Place getPlace() {
        return place;
    }
    public void setPlace(Place place) {
        this.place = place;
    }
    public LocalDateTime getStart() {
        return start;
    }
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    public LocalDateTime getEnd() {
        return end;
    }
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTripID() {
        return tripID;
    }
    public void setTripID(String tripID) {
        this.tripID = tripID;
    }
}
