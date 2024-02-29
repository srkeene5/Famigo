package com.famigo.website.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.model.Conversation;
import com.famigo.website.model.Message;
import com.famigo.website.model.SubConversation;
import com.famigo.website.model.User;
import com.famigo.website.repositories.MessageRepository;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.Util;

@Controller
public class MessageController {

    @Autowired
    MessageRepository mr;
    @Autowired
    UserRepository ur;
    String conversationID = null;

    @GetMapping("/conversations")
    public String getConversations(Model model) {
        String userID = Util.getUserID();
        ArrayList<Conversation> c = mr.getConversations(userID);
        model.addAttribute("conversations", c);
        return "viewConversations";
    }

    @RequestMapping(value="/submitConversation", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createConversation(@RequestBody SubConversation members) {
        ArrayList<User> userList = new ArrayList<>();
        for (String user : members.getMembers()) {   
            User u = ur.getUser("id", user);
            if (u != null) {
                userList.add(u);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        Conversation c = new Conversation(Util.generateID(30), "null", userList, null);
        mr.addConversation(c);
        Map<String, String> result = new HashMap<String, String>();
        result.put("cid", c.getID());
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    // for Testing
    @GetMapping("/conversationsTest")
    public String getConversationsTest(Model model) {
        ArrayList<Conversation> c = mr.getConversations("user");
        model.addAttribute("conversations", c);
        return "viewConversations";
    }
    // for Testing

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
        Message m = new Message(Util.generateID(50), username, content, LocalDateTime.now(), false, conversationID);
        mr.addMessage(m);
        return "messageSent.html";
    }

}
