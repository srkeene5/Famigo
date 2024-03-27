package com.famigo.website.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
        ArrayList<Report> appeals = repR.getAppeals();
        if (reports == null || reports.isEmpty()) {
            model.addAttribute("reports", new Report[0]);
        } else {
            System.out.println(reports.get(0).getExplanation());
            model.addAttribute("reports", reports.toArray());
        }
        if (appeals == null || appeals.isEmpty()) {
            model.addAttribute("appeals", new Report[0]);
        } else {
            System.out.println(appeals.get(0).getExplanation());
            model.addAttribute("appeals", appeals.toArray());
        }

        return "/modPage";
    }

    @RequestMapping(value = "/mod/repReact", method = RequestMethod.POST)
    public ResponseEntity<String> likeOrDislike(@RequestParam(value = "vals[]") String[] vals) {
        System.out.println(vals[0] + "\t" + vals[1]);

        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }
}
