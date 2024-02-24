package com.famigo.website.model;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.famigo.website.Profile;

@Component
public class Conversation {

    private String id;
    private String name;
    private ArrayList<Message> messages;
    private ArrayList<Profile> members;

    public void createConversation(String id, String name, ArrayList<Profile> members, ArrayList<Message> messages) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.messages = messages;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Profile> getMembers() {
        return members;
    }

}
