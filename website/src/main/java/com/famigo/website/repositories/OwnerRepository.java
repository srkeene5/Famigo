package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.Owner;
import com.famigo.website.model.OwnerRequest;
import com.famigo.website.model.Report;


@Repository
public class OwnerRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PlaceRepository pr;

    public void storeRequest(OwnerRequest request) {
        String sql = "INSERT INTO ownerRequests (ownerReqID, placeID, userID) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, 0);
                ps.setInt(2, request.getPlaceId());
                ps.setString(3, request.getUserId());
            }

        });
    }
    
    //testing
    public void printAllRequests() {
        String sql = "SELECT * FROM ownerRequests"; 
        List<Map<String,Object>> a = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> b : a) {
            System.out.println("ownerReqID: " + b.get("ownerReqID"));
            System.out.println("placeID: " + b.get("placeID"));
            System.out.println("userID: " + b.get("userID"));
            System.out.println("~~~~~~~~~~~~~~");
        }
    }



    public ArrayList<OwnerRequest> getOwnerRequests() {
        List<Map<String, Object>> requestList = jdbcTemplate.queryForList("SELECT * FROM ownerRequests");
        if (requestList == null || requestList.isEmpty()) {
            System.out.println("request list = null");
            return null;
        }
        ArrayList<OwnerRequest> requests = new ArrayList<>();
        for (Map<String, Object> o : requestList) {
            requests.add(new OwnerRequest((int) o.get("ownerReqID"), (String) o.get("userID"), pr.getPlaceByID((int) o.get("placeID")).getName()));
        }
        return requests;
    }

}
