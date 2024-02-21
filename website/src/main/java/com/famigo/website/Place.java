package com.famigo.website;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

//@Repository
@Component
public class Place extends JdbcDaoSupport {
    private String rating;
    private String name;
    private String address;
    private String id;

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public void createPlace(String name, String address, String rating) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        
        String sql = "INSERT INTO place (id, name, address, rating) VALUES (?, ?, ?, ?)";
        id = Integer.toString(new Random().nextInt(1000));
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
        
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, rating);
        }

        @Override
        public int getBatchSize() {
            // TODO Auto-generated method stub
            return 1;
        }
    });
    }

    public Place() {
        rating = null;
        name = null;
        address = null;
    }

    public Place(String id, String rating, String name, String address) {
        this.id = id;
        this.rating = rating;
        this.name = name;
        this.address = address;
    }

    // Getters
    public String getRating() {
        return rating;
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