package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.Place;
import com.famigo.website.model.Review;

@Repository
public class PlaceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReviewRepository rr;

    public void addPlace(Place place) {
        String sql = "INSERT INTO place (id, name, address, rating) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, 0);
                ps.setString(2, place.getName());
                ps.setString(3, place.getAddress());
                ps.setString(4, place.getRating());
            }

        });
    }

    public ArrayList<String> getPlaces() {
        List<Map<String, Object>> placeList = jdbcTemplate.queryForList("SELECT name FROM place", new Object[] {});
        if (placeList == null || placeList.isEmpty()) {
            return null;
        }
        ArrayList<String> placeNames = new ArrayList<>();
        for (Map<String, Object> o : placeList) {
            placeNames.add((String) o.get("name"));

            // placeNames.add((String) o.get("id"));

            // placeNames.add((String) o.get("rating"));

            // placeNames.add((String) o.get("address"));
        }
        return placeNames;
    }

    public Place getPlaceByID(int pid) {
        List<Map<String, Object>> placeList = jdbcTemplate.queryForList("SELECT * FROM place WHERE id=?",
                new Object[] { pid });
        if (placeList == null || placeList.isEmpty()) {
            return null;
        }
        Place place = null;
        for (Map<String, Object> o : placeList) {
            place = new Place((String) o.get("rating"), (String) o.get("name"), (String) o.get("address"),
                    (int) o.get("id"));
        }
        return place;
    }

    public Place getPlaceByName(String name) {
        List<Map<String, Object>> placeList = jdbcTemplate.queryForList("SELECT * FROM place WHERE name=?",
                new Object[] { name });
        if (placeList == null || placeList.isEmpty()) {
            return null;
        }
        Place place = null;
        // int count = 0;
        for (Map<String, Object> o : placeList) {
            if (name.equals(o.get("name"))) {
                place = new Place((String) o.get("rating"), (String) o.get("name"), (String) o.get("address"),
                        (int) o.get("id"));
            }
            // count++;
        }
        return place;
    }

    public void deletePlace(int pid) {
        jdbcTemplate.update("DELETE FROM place WHERE id=?", pid);
        ArrayList<Review> reviews = rr.getReviewsByPlace(pid);
        for (Review review : reviews) {
            rr.deleteReview(review);
        }
    }
}
