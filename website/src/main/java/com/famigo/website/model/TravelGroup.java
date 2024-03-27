package com.famigo.website.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TravelGroup {
    private String id;
    private String name;
    private User owner;
    private LocalDateTime created;
    private String description;
    private ArrayList<User> members;
    private String conversationID;

    public TravelGroup(String id, String name, User owner, LocalDateTime created, String description, ArrayList<User> members, String conversationID) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.created = created;
        this.description = description;
        this.members = members;
        this.conversationID = conversationID;
    }

    public String getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public User getOwner() {
        return owner;
    }
    public LocalDateTime getCreated() {
        return created;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<User> getMembers() {
        return members;
    }
    public String getConversationID() {
        return conversationID;
    }
}
