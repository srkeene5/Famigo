package com.famigo.website;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createUser(User user) {
        jdbcTemplate.update("INSERT INTO user (id, username, email, password, name, description, visibility, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, user.getID());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getPassword());
                ps.setString(5, user.getName());
                ps.setString(6, user.getDescription());
                ps.setString(7, user.getVisibility().toString());
                ps.setString(8, user.getRole().toString());
            }
            
        });
    }

    public User getUser(String userID) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM user WHERE id=?", new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    try {
                        User newUser = new User(rs.getString("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("name"), rs.getString("description"), Visibility.valueOf(rs.getString("visibility")), Role.valueOf(rs.getString("role")));
                        return newUser;
                    }
                    catch(Exception e) {
                        return null;
                    }
                }
                
            }, userID);
            return user;
        } catch (Exception e) {
            return null;
        }
    }
}
