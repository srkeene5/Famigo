package com.famigo.website;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


//@Component
@Repository
public class Place {
    private String rating;
    private String name;
    private String address;
    private String id;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Place() {
    }

    public Place(String rating, String name, String address) {
        //this.jdbcTemplate = null;
        this.rating = rating;
        this.name = name;
        this.address = address;
    }

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

    // Getters
    public String getRating() {
        return rating;
    }

    protected String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}