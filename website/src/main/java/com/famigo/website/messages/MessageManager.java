package com.famigo.website.messages;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.Util;

@Controller
public class MessageManager {

    @Autowired
    MessageRepository mr;
    String conversationID = null;

    @GetMapping("/conversations")
    public String getConversations(Model model, @RequestParam String userID) {
        ArrayList<Conversation> c = mr.getConversations(userID);
        model.addAttribute("conversations", c);
        return "viewConversations";
    }

    @GetMapping("/conversations/{cid}")
    public String seeMessages(Model model, @PathVariable String cid) {
        conversationID = cid;
        ArrayList<Message> m = mr.getMessages(cid);
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
