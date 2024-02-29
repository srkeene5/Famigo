package com.famigo.website.model;

import java.util.ArrayList;

//@Component
public class Conversation {

    private String id;
    private String name;
    private ArrayList<Message> messages;
    private ArrayList<User> members;

    public Conversation(String id, String name, ArrayList<User> members, ArrayList<Message> messages) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.messages = messages;
    }

    public Conversation() {
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

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message m) {
        messages.add(m);
    }

    public void removeMessage(Message m) {
        messages.remove(m);
    }

}
