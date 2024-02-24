package com.famigo.website;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.famigo.website.model.Conversation;
import com.famigo.website.model.Message;
import com.famigo.website.repositories.MessageRepository;
import com.famigo.website.repositories.Profile;

import java.time.LocalDateTime;

@SpringBootApplication
public class WebsiteApplication {

    static Profile profile;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebsiteApplication.class, args);
        /*
         * MessageRepository message = context.getBean(MessageRepository.class);
         * Profile p = context.getBean(Profile.class);
         * p.createProfile("bobby", "visible", "email@email.email", "bob", "bobberson",
         * "this is a description");
         * Profile r = context.getBean(Profile.class);
         * r.createProfile("billy", "visible", "email@email.email", "billy",
         * "billerson", "this is a description");
         * ArrayList<Profile> profiles = new ArrayList<>();
         * profiles.add(p);
         * profiles.add(r);
         * Conversation c = context.getBean(Conversation.class);
         * c.createConversation(Util.generateID(30), "The best conversation", profiles,
         * null);
         * message.addConversation(c);
         * System.out.println("Conversation added " + c.getID());
         * if (c.getName().equals(message.getConversation(c.getID()).getName())) {
         * System.out.println("Conversation retrieved");
         * }
         * else {
         * System.out.println("Conversation not retrieved");
         * }
         * message.addMessage(new Message(Util.generateID(50), p.getID(),
         * "This is the best message", LocalDateTime.now(), false, c.getID()));
         * message.addMessage(new Message(Util.generateID(50), r.getID(),
         * "This is the best other message", LocalDateTime.now(), false, c.getID()));
         * ArrayList<Message> messages = message.getMessages(c.getID());
         * for (Message m : messages) {
         * System.out.println(m.getSender() + " " + m.getContent());
         * }
         */
    }

}
