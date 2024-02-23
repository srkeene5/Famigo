package com.famigo.website;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addPlace(Place place) {
        String sql = "INSERT INTO place (id, name, address, rating) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                // TODO Auto-generated method stub
                ps.setString(1, place.getId());
                ps.setString(2, place.getName());
                ps.setString(3, place.getAddress());
                ps.setString(4, place.getRating());
            }
            
        });
    }
}
