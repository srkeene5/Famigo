package com.famigo.website.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.User;
import com.famigo.website.utilities.Role;
import com.famigo.website.utilities.Util;
import com.famigo.website.utilities.Visibility;

@Repository
public class FollowingRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository; 
    public void followUser(int followerCount/*other values later: String userDoingFollowing, String userToBeFollowed, Boolean isFollowing*/) {
        jdbcTemplate.update("INSERT INTO userStats (follower_count) VALUES (?)", new PreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, followerCount);
            }
            
        });
    }

    public int getFollowerCount(String username/*String userDoingFollowing, String userToBeFollowed, int followerCount, Boolean isFollowing*/) {
        String sql = "SELECT follower_count FROM userStats WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

}
