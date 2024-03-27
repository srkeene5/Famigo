package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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


    /*public boolean areFriends(String username1, String username2) {
        User user = userRepository.getUser("username", username1);
        User user2 = userRepository.getUser("username", username2);
        List<String> user1Followers = getFollowersList(user.getUsername());
        return user1Followers.contains(user2.getUsername());
    }*/

    /**
     * Checks whether the primary user is following the other user, and whether the other user is following
     * the primary user.
     * @return Integer with value -1, 0, 1, or 2. A value of 0 means neither user follows the other, 1 means
     * exactly one user follows the other, 2 means both users follow each other, and -1 means the two users
     * are the same.
     */
    public int getFollowerStatus(String primaryUserId, String otherUserId) {
        if (primaryUserId.equals(otherUserId)) { return -1; }
        int statusVal = 0;

        // Check one follow direction
        Map<String, Object> follow1;
        try {
            follow1 = jdbcTemplate.queryForMap("SELECT * FROM followers WHERE id=? AND following_id=?",
                    new Object[] { primaryUserId, otherUserId });
        } catch (DataAccessException e) {
            follow1 = null;
        }
    
        // Check opposite follow direction
        Map<String, Object> follow2;
        try {
            follow2 = jdbcTemplate.queryForMap("SELECT * FROM followers WHERE id=? AND following_id=?",
                    new Object[] { otherUserId, primaryUserId });
        } catch (DataAccessException e) {
            follow2 = null;
        }

        if (follow1 != null) { statusVal++; }
        if (follow2 != null) { statusVal++; }
        return statusVal;
    }
}
