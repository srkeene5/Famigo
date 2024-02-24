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

public class Place {
    private String rating;
    private String name;
    private String address;
    private String id;

    public Place(String rating, String name, String address) {
        //this.jdbcTemplate = null;
        this.rating = rating;
        this.name = name;
        this.address = address;
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