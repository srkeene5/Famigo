package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.User;

import java.util.List;

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

    public Boolean isFollowing(String userLoggedIn, String userBeingViewed) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM followers WHERE id = ? AND following_id = ?", Integer.class, userBeingViewed, userLoggedIn);
        System.out.println("does user follow (1=yes,0=no)? -->" + count);
        return count > 0;
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


    public boolean areFriends(String username1, String username2) {
        //User user = userRepository.getUser("username", username1);
        //User user2 = userRepository.getUser("username", username2);
        List<String> user1Followers = getFollowersList(username1);
        List<String> user2Followers = getFollowersList(username2);
        return user1Followers.contains(username2) && user2Followers.contains(username1);
        /*if (user1Followers.contains(username2) && user2Followers.contains(username1)) {
            return true;
        }
        return user1Followers.contains(user2.getUsername());*/
    }
}
