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

@Repository
public class PlaceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addPlace(Place place) {
        String sql = "INSERT INTO place (id, name, address, rating, description) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, 0);
                ps.setString(2, place.getName());
                ps.setString(3, place.getAddress());
                ps.setString(4, place.getRating());
                ps.setString(5, place.getDescription());
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

    public Place getPlaceByName(String name) {
        List<Map<String, Object>> placeList = jdbcTemplate.queryForList("SELECT * FROM place WHERE name=?", new Object[] {name});
        if (placeList == null || placeList.isEmpty()) {
            return null;
        }
        Place place = null;
        // int count = 0;
        for (Map<String, Object> o : placeList) {
            if (name.equals(o.get("name"))) {
                place = new Place((String) o.get("rating"), (String) o.get("name"), (String) o.get("address"), (String) o.get("description"), (int) o.get("id"));
            }
            // count++;
        }
        return place;
    }

    public Place getPlaceById(int id) {
        Map<String, Object> place;
        try {
            place = jdbcTemplate.queryForMap("SELECT * FROM place WHERE id=?",
                    new Object[] {id});
        } catch (DataAccessException e) {
            place = null;
        }

        if (place != null) {
            return new Place((String) place.get("rating"),
                    (String) place.get("name"),
                    (String) place.get("address"),
                    (String) place.get("description"),
                    ((int) place.get("id"))
            );
        }
        return null;
    }

    public ArrayList<Place> getPlacesFromNames(ArrayList<String> placeNames) {
        ArrayList<Place> places = new ArrayList<>();
        for (int i = 0; i < placeNames.size(); i++) {
            places.add(getPlaceByName(placeNames.get(i)));
        }
        return places;
    }
}
