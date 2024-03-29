package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.User;
import com.famigo.website.utilities.Role;
import com.famigo.website.utilities.Visibility;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createUser(User user) {
        jdbcTemplate.update(
                "INSERT INTO user (id, username, email, password, name, description, visibility, role, enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)",
                new PreparedStatementSetter() {

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

    public User getUser(String column, String value) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM user WHERE " + column + "=?", new RowMapper<User>() {

                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    try {
                        User newUser = new User(rs.getString("id"), rs.getString("username"), rs.getString("email"),
                                rs.getString("password"), rs.getString("name"), rs.getString("description"),
                                Visibility.valueOf(rs.getString("visibility")), Role.valueOf(rs.getString("role")));
                        return newUser;
                    } catch (Exception e) {
                        return null;
                    }
                }

            }, value);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        List<Map<String, Object>> userList = jdbcTemplate.queryForList("SELECT * FROM user");
        for (Map<String, Object> user : userList) {
            User u = new User((String) user.get("id"), (String) user.get("username"), (String) user.get("email"),
                    (String) user.get("password"), (String) user.get("name"), (String) user.get("description"),
                    Visibility.valueOf((String) user.get("visibility")), Role.valueOf((String) user.get("role")));
            users.add(u);
        }
        return users;
    }

    public ArrayList<String> getAllUsernames() {
        ArrayList<String> users = new ArrayList<>();
        List<Map<String, Object>> userList = jdbcTemplate.queryForList("SELECT username FROM user");
        for (Map<String, Object> user : userList) {
            users.add((String) user.get("username"));
        }
        return users;
    }

    public User getUserByUsername(String userID) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM user WHERE username=?", new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    try {
                        User newUser = new User(rs.getString("id"), rs.getString("username"), rs.getString("email"),
                                rs.getString("password"), rs.getString("name"), rs.getString("description"),
                                Visibility.valueOf(rs.getString("visibility")), Role.valueOf(rs.getString("role")));
                        return newUser;
                    } catch (Exception e) {
                        System.out.println(e);
                        return null;
                    }
                }

            }, userID);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public String getPassword(String userID) {
        String password = jdbcTemplate.queryForObject("SELECT password FROM user WHERE id=?", new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub
                return rs.getString("password");
            }
            
        }, userID);
        return password;
    }

    public void deleteAccount(String userID) {
        jdbcTemplate.update("UPDATE user SET enabled=0 WHERE id=?", userID);
        jdbcTemplate.update("DELETE FROM unread WHERE userID=?", userID);
        jdbcTemplate.update("DELETE FROM followers WHERE id=? OR following_id=?", userID, userID);
    }

    public boolean isUserID(String userID) {
        boolean answer = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM user WHERE id=?", new RowMapper<Boolean>() {

            @Override
            public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub
                return rs.getBoolean(1);
            }
            
        }, userID);
        return answer;
    }

    public void addVerificationLink(String link, String userID) {
        String sql = "INSERT INTO verification (userID, link) VALUES (?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, userID);
                ps.setString(2, link);
            }
            
        });
    }

    public boolean verify(String link) {
        String sql = "SELECT COUNT(1) FROM verification WHERE link=?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, link);
        if (result == 1) {
            String userID = jdbcTemplate.queryForObject("SELECT userID FROM verification WHERE link=?", String.class, link);
            jdbcTemplate.update("DELETE FROM verification WHERE userID=?", new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    // TODO Auto-generated method stub
                    ps.setString(1, userID);
                }
                
            });
            jdbcTemplate.update("UPDATE user SET enabled=1 WHERE id=?", new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    // TODO Auto-generated method stub
                    ps.setString(1, userID);
                }
                
            });
            return true;
        }
        else {
            return false;
        }
    }
}
