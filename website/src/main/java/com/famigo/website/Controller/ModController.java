package com.famigo.website.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.famigo.website.model.Report;
import com.famigo.website.model.Review;
import com.famigo.website.repositories.ReportRepository;
import com.famigo.website.repositories.ReviewRepository;
import com.famigo.website.repositories.UserRepository;
import com.famigo.website.utilities.Role;
import com.famigo.website.utilities.Utilities;

@Controller
public class ModController {

    @Autowired
    UserRepository ur;

    @Autowired
    ReportRepository repR;

    @Autowired
    ReviewRepository rr;

    /*
     * Banned Page:
     */
    @RequestMapping(value = "/banPage", method = RequestMethod.GET)
    public String showBanPage(ModelMap model) {
        ArrayList<Report> reports = repR.getReportsByContentUser(Utilities.getUserID());
        ArrayList<Report> bannedReports = new ArrayList<>();
        if (reports != null && !reports.isEmpty()) {
            for (Report report : reports) {
                if (report.isBanned()) {
                    bannedReports.add(report);
                }
            }
        }
        if (bannedReports.isEmpty()) {
            model.addAttribute(new Report[0]);
        } else {
            model.addAttribute("Report", bannedReports.toArray());
        }
        return "bannedPage";
    }

    @RequestMapping(value = "/addAppeal", method = RequestMethod.POST)
    public ResponseEntity<String> AppealReport(@RequestParam(value = "vals[]") String[] vals) {
        Review review = rr.getReviewByID(Integer.valueOf(vals[1]));
        repR.addReport(new Report(Utilities.getUserID(), review.getUserId(), review, vals[0]));
        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }

    /*
     * Report Review
     */

    @RequestMapping(value = "/places/{name}/reportRevReact", method = RequestMethod.POST)
    public ResponseEntity<String> reportReview(@RequestParam(value = "vals[]") String[] vals) {
        Review review = rr.getReviewByID(Integer.valueOf(vals[1]));
        repR.addReport(new Report(Utilities.getUserID(), review.getUserId(), review, vals[0]));
        return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
    }

    /*
     * Mod Requests
     */

    @RequestMapping(value = "/makeMod/{uid}", method = RequestMethod.POST)
    public String makeMod(@PathVariable String uid) {
        System.out.println(uid);
        ur.updateRole(uid, Role.MODERATOR);
        return "redirect:/user/" + uid;
    }

    @RequestMapping(value = "/unMod/{uid}", method = RequestMethod.POST)
    public String unMod(@PathVariable String uid) {
        System.out.println(uid);
        ur.updateRole(uid, Role.USER);
        return "redirect:/user/" + uid;
    }

}
