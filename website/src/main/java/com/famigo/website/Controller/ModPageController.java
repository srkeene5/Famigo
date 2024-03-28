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
import com.famigo.website.repositories.CommentRepository;
import com.famigo.website.repositories.PlaceRepository;
import com.famigo.website.repositories.ReportRepository;
import com.famigo.website.repositories.ReviewRepository;

@Controller
public class ModPageController {

    @Autowired
    private ReportRepository repR;

    @Autowired
    private PlaceRepository pr;

    @Autowired
    private ReviewRepository rr;

    @Autowired
    private CommentRepository cr;

    @RequestMapping(value = "/modPage", method = RequestMethod.GET)
    public String showPlaceDetails(Model model) {
        // Get all Reports
        ArrayList<Report> reports = repR.getActiveReports();
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
        System.out.println(vals[0] + "\t" + vals[1] + "\t" + Integer.parseInt(vals[1]));

        Report delcon = repR.getReportByID(Integer.parseInt(vals[1]));
        int id = Integer.parseInt(vals[1]);
        switch (vals[0]) {
            case "ignore":
            case "appAcc":
                // delete report
                repR.deleteReport(id);
                break;

            case "delete":
                // delete report and content
                removeContent(delcon);
                repR.deleteReport(id);
                break;

            case "delAndBan":
                // delete content, add ban
                delcon.setBan(true);
                repR.updateBan(true, id);
                removeContent(delcon);
                break;

            case "appRej":
                // keep Report, keep ban
                delcon.rejectAppeal();
                break;
            default:
                System.err.println("invalid report name: " + vals[0]);
                break;
        }
        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }

    private void removeContent(Report report) {
        String cls = report.getReportClass();
        int id = report.getReportedId();
        switch (cls) {
            case "place":
                pr.deletePlace(id);
                break;
            case "comment":
                cr.deleteComment(id);
                break;
            case "review":
                rr.deleteReview(id);
                break;
            default:
                System.err.println("invalid report class: " + cls);
                break;
        }
    }
}
