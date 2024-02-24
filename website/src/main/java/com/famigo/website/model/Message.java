package com.famigo.website.model;

import java.time.LocalDateTime;

public class Message {

    private String id;
    private String sender;
    private LocalDateTime timestamp;
    private String content;
    private Boolean edited;
    private String conversation;

    public Message(String id, String sender, String content, LocalDateTime timestamp, Boolean edited,
            String conversation) {
        this.id = id;
        this.sender = sender;
        this.timestamp = timestamp;
        this.content = content;
        this.edited = edited;
        this.conversation = conversation;
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public String getConversation() {
        return conversation;
    }

}
