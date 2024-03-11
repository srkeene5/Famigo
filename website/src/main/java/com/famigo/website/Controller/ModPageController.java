package com.famigo.website.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.famigo.website.model.Report;
import com.famigo.website.repositories.ReportRepository;

@Controller
public class ModPageController {

    @Autowired
    ReportRepository repR;

    @RequestMapping(value = "/modPage", method = RequestMethod.GET)
    public String showPlaceDetails(Model model) {
        // Get all Reports
        ArrayList<Report> reports = repR.getReports();
        if (reports == null || reports.isEmpty()) {
            model.addAttribute("reports", new Report[0]);
        } else {
            model.addAttribute("reports", reports.toArray());
        }
        return "/modPage";
    }
}
