package com.famigo.website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.model.SubDeleteAccount;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.Utilities;

@Controller
public class SettingsController {

    @Autowired
    UserRepository ur;
    
    @PostMapping("/delete-account")
    public String accountDeleted(@RequestParam("passwordDelete") String passwordDelete) {
        String storedPassword = ur.getPassword(Utilities.getUserID());
        System.out.println(storedPassword.split("\\{bcrypt\\}")[1] + " " + passwordDelete + " " + storedPassword);
        boolean passwordCorrect = BCrypt.checkpw(passwordDelete, storedPassword.split("\\{bcrypt\\}")[1]);
        if (passwordCorrect) {
            ur.deleteAccount(Utilities.getUserID());
            return "redirect:/logout";
        }
        else {
            return "redirect:/user";
        }
    }

}
