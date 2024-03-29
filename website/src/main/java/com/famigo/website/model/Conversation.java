package com.famigo.website.model;

import java.util.ArrayList;

public class Conversation {

    private String id;
    private String name;
    private ArrayList<User> members;

    public Conversation(String id, String name, ArrayList<User> members) {
        this.id = id;
        this.name = name;
        this.members = members;
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

    public ArrayList<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }

}
