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
import com.famigo.website.utilities.Utilities;

@Controller
public class MessageController {

    @Autowired
    MessageRepository mr;
    @Autowired
    UserRepository ur;
    String conversationID = null;

    @GetMapping("/conversations")
    public String getConversations(Model model) {
        String userID = Utilities.getUserID();
        ArrayList<Conversation> c = mr.getConversations(userID);
        ArrayList<String> users = ur.getAllUsernames();
        model.addAttribute("conversations", c);
        model.addAttribute("usernames", users);
        return "viewConversations";
    }

    @RequestMapping(value="/submitConversation", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createConversation(@RequestBody SubConversation members) {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(ur.getUser("id", Utilities.getUserID()));
        for (String user : members.getMembers()) {   
            User u = ur.getUser("id", user);
            if (u != null) {
                userList.add(u);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        Conversation c = new Conversation(Utilities.generateID(30), "null", userList, null);
        mr.addConversation(c);
        Map<String, String> result = new HashMap<String, String>();
        result.put("cid", c.getID());
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    @GetMapping("/conversations/{cid}")
    public String seeMessages(Model model, @PathVariable String cid) {
        ArrayList<Message> m = mr.getMessages(cid);
        model.addAttribute("messages", m);
        return "viewMessages";
    }

    @GetMapping("/newMessage")
    public String newMessage(Model model) {
        return "message";
    }

    @PostMapping("/conversations/{cid}")
    public String sendMessage(Model model, @PathVariable String cid, @RequestParam String content) {
        String username = Utilities.getUserID();
        Message m = new Message(Utilities.generateID(50), username, content, LocalDateTime.now(), false, cid);
        mr.addMessage(m);
        return "redirect:/conversations/" + cid;
    }

}
