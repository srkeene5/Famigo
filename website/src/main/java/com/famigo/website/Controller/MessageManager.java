package com.famigo.website.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.Util;
import com.famigo.website.model.Conversation;
import com.famigo.website.model.Message;
import com.famigo.website.repositories.MessageRepository;

@Controller
public class MessageManager {

    @Autowired
    MessageRepository mr;
    String conversationID = null;

    @GetMapping("/conversations")
    public String getConversations(Model model, @RequestParam String userID) {
        ArrayList<Conversation> c = mr.getConversations(userID);
        if (c.isEmpty()) {
            return "you have no conversations";
        }
        model.addAttribute("conversations", c);
        return "viewConversations";
    }

    @GetMapping("/message")
    public String seeMessages(Model model, @RequestParam String cid) {
        conversationID = cid;
        ArrayList<Message> m = mr.getMessages(cid);
        if (m.isEmpty()) {
            return "you have no messages";
        }
        model.addAttribute("messages", m);
        return "viewMessages";
    }

    @GetMapping("/newMessage")
    public String newMessage(Model model) {
        return "message";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(Model model, @RequestParam String username, @RequestParam String content) {
        if (conversationID == null) {
            return "no conversation selected";
        }
        Message m = new Message(Util.generateID(50), username, content, LocalDateTime.now(), false, conversationID);
        mr.addMessage(m);
        return "messageSent.html";
    }

}
