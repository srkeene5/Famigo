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
        String sql = "INSERT INTO reports (repID, userID, repText, timestamp, placeID, revID, comID) VALUES (?, ?, ?, CAST(? AS DATETIME), ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, 0);
                ps.setString(2, report.getUserID());
                ps.setString(3, report.getExplanation());
                ps.setTimestamp(4, Timestamp.valueOf(report.getTimeStamp()));
                ps.setInt(5, report.getPlaceId());
                ps.setInt(6, report.getRevId());
                ps.setInt(7, report.getComId());
            }

        });
    }

    public ArrayList<Report> getReportsByUser(String uid) {
        List<Map<String, Object>> reportList = jdbcTemplate.queryForList("SELECT * FROM reviews WHERE userID=?",
                new Object[] { uid });
        if (reportList == null || reportList.isEmpty()) {
            return null;
        }
        ArrayList<Report> reports = new ArrayList<>();
        for (Map<String, Object> o : reportList) {
            reports.add(new Report((int) o.get("repID"), uid, (String) o.get("repText"),
                    (LocalDateTime) o.get("timestamp"), pr.getPlaceByID((int) o.get("placeID")),
                    rr.getReviewByID((int) o.get("revID")), cr.getCommentByID((int) o.get("comID"))));
        }
        return reports;
    }

    public ArrayList<Report> getReports() {
        List<Map<String, Object>> reportList = jdbcTemplate.queryForList("SELECT * FROM reports");
        if (reportList == null || reportList.isEmpty()) {
            return null;
        }
        ArrayList<Report> reports = new ArrayList<>();
        for (Map<String, Object> o : reportList) {
            reports.add(new Report((int) o.get("repID"),
                    (String) o.get("userID"), (String) o.get("repText"),
                    (LocalDateTime) o.get("timestamp"), pr.getPlaceByID((int) o.get("placeID")),
                    rr.getReviewByID((int) o.get("revID")),
                    cr.getCommentByID((int) o.get("comID"))));
        }
        return reports;
    }
}
