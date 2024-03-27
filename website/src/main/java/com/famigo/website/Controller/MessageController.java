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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.famigo.website.model.Conversation;
import com.famigo.website.model.Message;
import com.famigo.website.model.SubConversation;
import com.famigo.website.model.SubMessage;
import com.famigo.website.model.User;
import com.famigo.website.repositories.MessageRepository;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.Utilities;
import com.famigo.website.utilities.Role;
import com.famigo.website.utilities.Status;

@Controller
public class MessageController {

    @Autowired
    MessageRepository mr;
    @Autowired
    UserRepository ur;

    @GetMapping("/conversations")
    public String getConversations(Model model) {
        String userID = Utilities.getUserID();

        // Minor addition to make sure mod convo exists for mod users
        // Don't want to screw it up for presentation after dropping page
        User u = ur.getUserByUsername(userID);
        String modCID = Utilities.getModConvID();
        if (u.getRole() == Role.MODERATOR && !mr.conversationExists(modCID)) {
            System.out.println("Adding Convo");
            Conversation c = new Conversation(modCID, "Mod Message Board", ur.getAllMods());
            System.out.println("adding conversation");
            mr.addConversation(c);
        }
        // end mod addition

        ArrayList<Conversation> c = mr.getConversations(userID);
        ArrayList<String> users = ur.getAllUsernames();
        users.remove(Utilities.getUserID());
        model.addAttribute("conversations", c);
        model.addAttribute("usernames", users);
        return "viewConversations";
    }

    @RequestMapping(value = "/submitConversation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createConversation(@RequestBody SubConversation members) {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(ur.getUser("id", Utilities.getUserID()));
        for (String user : members.getMembers()) {
            User u = ur.getUser("id", user);
            if (u != null) {
                userList.add(u);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        Conversation c = new Conversation(Utilities.generateID(30), "null", userList);
        mr.addConversation(c);
        Map<String, String> result = new HashMap<String, String>();
        result.put("cid", c.getID());
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    @GetMapping("/conversations/{cid}")
    public String seeMessages(Model model, @PathVariable String cid) {
        ArrayList<Message> m = mr.getMessages(cid);
        mr.getUnread(Utilities.getUserID(), cid);
        model.addAttribute("user", Utilities.getUserID());
        model.addAttribute("messages", m);
        return "viewMessages";
    }

    @RequestMapping(value = "/conversations/{cid}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessage(Model model, @PathVariable String cid, @RequestBody SubMessage content) {
        String username = Utilities.getUserID();
        Message m = new Message(Utilities.generateID(50), username, content.getContent(), LocalDateTime.now(), false,
                cid, Status.UNREAD);
        mr.addMessage(m);
        mr.addToUnread(m, mr.getConversation(cid), username);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/conversations/{cid}/getupdates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<Message>> updateMessages(@PathVariable String cid) {
        ArrayList<Message> messageList = mr.getUnread(Utilities.getUserID(), cid);
        if (messageList == null || messageList.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }

}
