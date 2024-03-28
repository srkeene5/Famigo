package com.famigo.website.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.famigo.website.model.Report;

@Service
public class AutherizeAccess {
    public AutherizeAccess() {
    }

    public String authorizeNotBanned(String path, ArrayList<Report> reports) {
        boolean banned = false;
        if (reports != null && !reports.isEmpty()) {
            for (Report report : reports) {
                if (report.isBanned()) {
                    banned = true;
                }
            }
        }
        if (banned) {
            return "redirect:/banPage";
        }
        return path;
    }
}
