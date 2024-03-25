package com.famigo.website.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Trip {

    String id;
    String name;
    User owner;
    ArrayList<User> members;
    LocalDateTime created;
    String description;

    public Trip(String id, String name, User owner, ArrayList<User> members, LocalDateTime created, String description) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.members = members;
        this.created = created;
        this.description = description;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
