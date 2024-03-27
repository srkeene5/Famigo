package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.Report;

@Repository
public class ReportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReviewRepository rr;

    @Autowired
    private PlaceRepository pr;

    @Autowired
    private CommentRepository cr;

    public void addReport(Report report) {
        String sql = "INSERT INTO reports (repID, repUserID, conUserID, repText, appText, timestamp, placeID, revID, comID, banned) VALUES (?, ?, ?, ?, ?, CAST(? AS DATETIME), ?, ?, ?, ?)";

        System.out.println(report.getExplanation());

        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, 0);
                ps.setString(2, report.getReportingUserID());
                ps.setString(3, report.getContentUserID());
                ps.setString(4, report.getExplanation());
                ps.setString(5, report.getAppeal());
                ps.setTimestamp(6, Timestamp.valueOf(report.getTimeStamp()));
                ps.setInt(7, report.getPlaceId());
                ps.setInt(8, report.getRevId());
                ps.setInt(9, report.getComId());
                ps.setBoolean(10, report.isBanned());
            }

        });
    }

    public ArrayList<Report> getReportsByReportingUser(String uid) {
        List<Map<String, Object>> reportList = jdbcTemplate.queryForList("SELECT * FROM reports WHERE repUserID=?",
                new Object[] { uid });
        if (reportList == null || reportList.isEmpty()) {
            return null;
        }
        ArrayList<Report> reports = new ArrayList<>();
        for (Map<String, Object> o : reportList) {
            reports.add(new Report((int) o.get("repID"), uid, (String) o.get("conUserID"), (String) o.get("repText"),
                    (String) o.get("appText"),
                    (LocalDateTime) o.get("timestamp"), pr.getPlaceByID((int) o.get("placeID")),
                    rr.getReviewByID((int) o.get("revID")), cr.getCommentByID((int) o.get("comID")),
                    (boolean) o.get("banned")));
        }
        return reports;
    }

    public ArrayList<Report> getReportsByContentUser(String uid) {
        List<Map<String, Object>> reportList = jdbcTemplate.queryForList("SELECT * FROM reports WHERE conUserID=?",
                new Object[] { uid });
        if (reportList == null || reportList.isEmpty()) {
            return null;
        }
        ArrayList<Report> reports = new ArrayList<>();
        for (Map<String, Object> o : reportList) {
            reports.add(new Report((int) o.get("repID"), (String) o.get("repUserID"), uid,
                    (String) o.get("repText"), (String) o.get("appText"),
                    (LocalDateTime) o.get("timestamp"), pr.getPlaceByID((int) o.get("placeID")),
                    rr.getReviewByID((int) o.get("revID")), cr.getCommentByID((int) o.get("comID")),
                    (boolean) o.get("banned")));
        }
        return reports;
    }

    public ArrayList<Report> getReportsAppeals() {
        List<Map<String, Object>> reportList = jdbcTemplate.queryForList("SELECT * FROM reports");
        if (reportList == null || reportList.isEmpty()) {
            System.out.println("Failed to Pull");
            return null;
        }
        ArrayList<Report> reports = new ArrayList<>();
        for (Map<String, Object> o : reportList) {
            reports.add(new Report((int) o.get("repID"),
                    (String) o.get("repUserID"), (String) o.get("conUserID"), (String) o.get("repText"),
                    (String) o.get("appText"),
                    (LocalDateTime) o.get("timestamp"), pr.getPlaceByID((int) o.get("placeID")),
                    rr.getReviewByID((int) o.get("revID")),
                    cr.getCommentByID((int) o.get("comID")), (boolean) o.get("banned")));
        }
        return reports;
    }

    public ArrayList<Report> getReports() {
        List<Map<String, Object>> reportList = jdbcTemplate.queryForList("SELECT * FROM reports WHERE appText IS NULL");
        if (reportList == null || reportList.isEmpty()) {
            System.out.println("Failed to Pull");
            return null;
        }
        ArrayList<Report> reports = new ArrayList<>();
        for (Map<String, Object> o : reportList) {
            reports.add(new Report((int) o.get("repID"),
                    (String) o.get("repUserID"), (String) o.get("conUserID"), (String) o.get("repText"),
                    (String) o.get("appText"),
                    (LocalDateTime) o.get("timestamp"), pr.getPlaceByID((int) o.get("placeID")),
                    rr.getReviewByID((int) o.get("revID")),
                    cr.getCommentByID((int) o.get("comID")), (boolean) o.get("banned")));
        }
        return reports;
    }

    public ArrayList<Report> getAppeals() {
        List<Map<String, Object>> reportList = jdbcTemplate
                .queryForList("SELECT * FROM reports WHERE appText IS NOT NULL");
        if (reportList == null || reportList.isEmpty()) {
            System.out.println("Failed to Pull");
            return null;
        }
        ArrayList<Report> reports = new ArrayList<>();
        for (Map<String, Object> o : reportList) {
            reports.add(new Report((int) o.get("repID"),
                    (String) o.get("repUserID"), (String) o.get("conUserID"), (String) o.get("repText"),
                    (String) o.get("appText"),
                    (LocalDateTime) o.get("timestamp"), pr.getPlaceByID((int) o.get("placeID")),
                    rr.getReviewByID((int) o.get("revID")),
                    cr.getCommentByID((int) o.get("comID")), (boolean) o.get("banned")));
        }
        return reports;
    }

}
