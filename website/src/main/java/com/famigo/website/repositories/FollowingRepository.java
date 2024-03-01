package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class FollowingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    public void followUser(int followerCount/*
                                             * other values later: String userDoingFollowing, String userToBeFollowed,
                                             * Boolean isFollowing
                                             */) {
        jdbcTemplate.update("INSERT INTO userStats (follower_count) VALUES (?)", new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, followerCount);
            }

        });
    }

    public int getFollowerCount(String username/*
                                                * String userDoingFollowing, String userToBeFollowed, int followerCount,
                                                * Boolean isFollowing
                                                */) {
        String sql = "SELECT follower_count FROM userStats WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

}
