package com.famigo.website.messages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageManager {

    @GetMapping("/message")
    public String seeMessages(Model model) {
        
        return "viewMessages";
    }

}
