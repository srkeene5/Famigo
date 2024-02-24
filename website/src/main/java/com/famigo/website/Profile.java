package com.famigo.website;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Repository
@Component
@Scope("prototype")
public class Profile extends JdbcDaoSupport {
    
    String id;

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public Profile() {
        this.id = null;
    }

    public Profile(String id) {
        this.id = id;
    }

    public void createProfile(String username, String visibility, String email, String firstName, String lastName, String description) {
        String sql = "INSERT INTO user (id, username, email, visibility, name, description) VALUES (?, ?, ?, ?, ?, ?)";
        id = Integer.toString(new Random().nextInt(1000));
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, id);
                ps.setString(2, username);
                ps.setString(3, email);
                ps.setString(4, visibility);
                ps.setString(5, firstName + " " + lastName);
                ps.setString(6, description);
            }

            @Override
            public int getBatchSize() {
                // TODO Auto-generated method stub
                return 1;
            }
        });
    }

    public void printProfile() {
        String sql = "SELECT * FROM user WHERE id = ?";
        Profile profile = (Profile)getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<Profile>() {

            @Override
            @Nullable
            public Profile mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub
                Profile p = new Profile(rs.getString("id"));
                System.out.println(rs.getString("id")+" "+rs.getString("username"));
                return p;
            }
            
        });
    }

    public String getID() {
        return id;
    }
}
