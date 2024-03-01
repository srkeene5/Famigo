package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.User;

@Repository
public class FollowingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository userRepository;

    public void createFollow(User user) {
        System.out.println("USER GET ID: " + user.getID());
        jdbcTemplate.update("INSERT INTO userStats (id, follower_count) VALUES (?, ?) ON DUPLICATE KEY UPDATE id=id",
                new PreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, user.getID());
                        ps.setInt(2, 157); // TODO: PLACEHOLDERRRRRRRRRRRRRRRRRRRRRRRRRRRRR
                    }

                });
    }

    public int getNumFollowers(String username) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM followers WHERE id = ?", Integer.class, username);
    }

    public int getNumFollowing(String username) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM followers WHERE following_id = ?", Integer.class,
                username);
    }

    public List<String> getFollowersList(String username) {
        return jdbcTemplate.queryForList("SELECT following_id FROM followers WHERE id = ?", String.class, username);
    }

    public List<String> getFollowingList(String username) {
        return jdbcTemplate.queryForList("SELECT id FROM followers WHERE following_id = ?", String.class, username);
    }

    public void followUser(String usernameToBeFollowed, String usernameDoingFollowing) {
        jdbcTemplate.update("INSERT INTO followers (id, following_id) VALUES (?, ?)", usernameToBeFollowed,
        usernameDoingFollowing);
    }

    public void unfollowUser(String usernameToBeUnFollowed, String usernameDoingUnFollowing) {
        jdbcTemplate.update("DELETE FROM followers WHERE id = ? AND following_id = ?", usernameToBeUnFollowed,
        usernameDoingUnFollowing);
    }

    /*
     * public void followUser(int followerCount) { old method
     * jdbcTemplate.
     * update("UPDATE userStats SET follower_count = follower_count + 1 WHERE id = ?"
     * , new PreparedStatementSetter() {
     * 
     * @Override
     * public void setValues(PreparedStatement ps) throws SQLException {
     * ps.setInt(1, followerCount);
     * }
     * 
     * });
     * }
     */

    public int getFollowerCount(String username/*
                                                * String userDoingFollowing, String userToBeFollowed, int followerCount,
                                                * Boolean isFollowing
                                                */) {
        String sql = "SELECT follower_count FROM userStats WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

}
