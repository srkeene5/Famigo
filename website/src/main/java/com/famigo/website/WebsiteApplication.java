package com.famigo.website;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.famigo.website.messages.Conversation;
import com.famigo.website.messages.Message;
import com.famigo.website.messages.MessageRepository;
import java.time.LocalDateTime;

@SpringBootApplication
public class WebsiteApplication {
    
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebsiteApplication.class, args);
        UserRepository ur = context.getBean(UserRepository.class);
        ur.createUser(new User(Util.generateID(20), "bob", "bob@bob.bob", "password", Role.USER, Visibility.ALL, "bob", "bob"));
        
        /*
        MessageRepository message = context.getBean(MessageRepository.class);
        Profile r = context.getBean(Profile.class);
        r.createProfile("billy", "visible", "email@email.email", "billy", "billerson", "this is a description");
        ArrayList<Profile> profiles = new ArrayList<>();
        profiles.add(p);
        profiles.add(r);
        Conversation c = context.getBean(Conversation.class);
        c.createConversation(Util.generateID(30), "The best conversation", profiles, null);
        message.addConversation(c);
        System.out.println("Conversation added " + c.getID());
        if (c.getName().equals(message.getConversation(c.getID()).getName())) {
            System.out.println("Conversation retrieved");
        }
        else {
            System.out.println("Conversation not retrieved");
        }
        message.addMessage(new Message(Util.generateID(50), p.getID(), "This is the best message", LocalDateTime.now(), false, c.getID()));
        message.addMessage(new Message(Util.generateID(50), r.getID(), "This is the best other message", LocalDateTime.now(), false, c.getID()));
        ArrayList<Message> messages = message.getMessages(c.getID());
        for (Message m : messages) {
            System.out.println(m.getSender() + " " + m.getContent());
        }
        */
    }

}
