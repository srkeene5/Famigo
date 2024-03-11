package com.famigo.website.repositories;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.Review;

@Repository
public class ReviewRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addReview(Review review) {
        String sql = "INSERT INTO reviews (revID, userID, review, stars, timestamp, edited, placeID, likes, dislikes) VALUES (?, ?, ?, ?, CAST(? AS DATETIME), ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, review.getRevId());
                ps.setString(2, review.getUserId());
                ps.setString(3, review.getReview());
                ps.setInt(4, review.getStars());
                ps.setTimestamp(5, Timestamp.valueOf(review.getTimeStamp()));
                ps.setBoolean(6, review.isEdited());
                ps.setInt(7, review.getPlaceId());
                ps.setInt(8, 0);
                ps.setInt(9, 0);
            }

        });
    }

    public ArrayList<Review> getReviewsByUser(String uid) {
        List<Map<String, Object>> reviewList = jdbcTemplate.queryForList("SELECT * FROM reviews WHERE userID=?",
                new Object[] { uid });
        if (reviewList == null || reviewList.isEmpty()) {
            return null;
        }
        ArrayList<Review> reviews = new ArrayList<>();
        for (Map<String, Object> o : reviewList) {
            reviews.add(new Review(uid, (int) o.get("revID"),
                    (String) o.get("review"), (int) o.get("stars"),
                    (LocalDateTime) o.get("timestamp"), (boolean) o.get("edited"),
                    (int) o.get("placeID"), (int) o.get("likes"), (int) o.get("dislikes")));
        }
        return reviews;
    }

    public ArrayList<Review> getReviewsByPlace(int pid) {
        List<Map<String, Object>> reviewList = jdbcTemplate.queryForList("SELECT * FROM reviews WHERE placeID=?",
                new Object[] { pid });
        if (reviewList == null || reviewList.isEmpty()) {
            return null;
        }
        ArrayList<Review> reviews = new ArrayList<>();
        for (Map<String, Object> o : reviewList) {
            reviews.add(new Review((String) o.get("userID"),
                    (int) o.get("revID"), (String) o.get("review"),
                    (int) o.get("stars"), (LocalDateTime) o.get("timestamp"),
                    (boolean) o.get("edited"),
                    pid,
                    (int) o.get("likes"),
                    (int) o.get("dislikes")));
        }
        return reviews;
    }

    // This method fires when a user presses the like or dislike button on a review, and
    // adds and/or removes reactions as necessary.
    public void alterReviewReaction(String uid, int rid, boolean isLike) {
        // First, check if the user has liked or disliked this review already
        Map<String, Object> pastReaction;
        try {
            pastReaction = jdbcTemplate.queryForMap("SELECT * FROM reviewReaction WHERE userID=? AND reviewID=?",
                    new Object[] { uid, rid });
        } catch (DataAccessException e) {
            pastReaction = null;
        }

        // Check if past reaction exists
        if (pastReaction != null) {

            if (((boolean) pastReaction.get("isLike")) == isLike) {
                // If past reaction and current reaction match, the user is removing an earlier reaction
                jdbcTemplate.update("DELETE FROM reviewReaction WHERE userID=? AND reviewID=?",
                        new Object[] {uid, rid});
                if (isLike) {
                    jdbcTemplate.update("UPDATE reviews SET likes = likes - 1 WHERE revID=?",
                            new Object[] {rid});
                } else {
                    jdbcTemplate.update("UPDATE reviews SET dislikes = dislikes - 1 WHERE revID=?",
                            new Object[] {rid});
                }

            } else {
                // If past reaction and current reaction don't match, the user is adding the opposite
                // reaction, in which case their previous reaction must also be deleted. So we can
                // just update the isLike value in the SQL entry.
                String sqlStr = "UPDATE reviewReaction SET isLike = NOT isLike WHERE userID=? AND reviewID=?";
                jdbcTemplate.update(sqlStr, new Object[] {uid, rid});
                if (isLike) {
                    jdbcTemplate.update("UPDATE reviews SET likes = likes + 1 WHERE revID=?",
                            new Object[] {rid});
                    jdbcTemplate.update("UPDATE reviews SET dislikes = dislikes - 1 WHERE revID=?",
                            new Object[] {rid});
                } else {
                    jdbcTemplate.update("UPDATE reviews SET dislikes = dislikes + 1 WHERE revID=?",
                            new Object[] {rid});
                    jdbcTemplate.update("UPDATE reviews SET likes = likes - 1 WHERE revID=?",
                            new Object[] {rid});
                }
            }

        } else {
            // There is no past reaction, so just add the current one
            String sqlStr = "INSERT INTO reviewReaction (userID, reviewID, isLike) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlStr, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1, uid);
                    ps.setInt(2, rid);
                    ps.setBoolean(3, isLike);
                }
            });
            if (isLike) {
                jdbcTemplate.update("UPDATE reviews SET likes = likes + 1 WHERE revID=?",
                        new Object[] {rid});
            } else {
                jdbcTemplate.update("UPDATE reviews SET dislikes = dislikes + 1 WHERE revID=?",
                        new Object[] {rid});
            }
        }
    }

    // For each review, returns 1 if the given user has liked the given review, -1 if they disliked,
    // and 0 if they haven't done either.
    public int[] getUserReviewReactions(String userId, ArrayList<Review> revs) {
        int[] reactions = new int[revs.size()];
        for (int i = 0; i < reactions.length; i++) {
            Map<String, Object> reaction;
            try {
                reaction = jdbcTemplate.queryForMap("SELECT * FROM reviewReaction WHERE userID=? AND reviewID=?",
                        new Object[] { userId, revs.get(i).getRevId() });
            } catch (DataAccessException e) {
                reaction = null;
            }

            if (reaction == null || reaction.isEmpty()) {
                reactions[i] = 0;
            } else if ((boolean) reaction.get("isLike") == true) {
                reactions[i] = 1;
            } else {
                reactions[i] = -1;
            }
        }
        return reactions;
    }

    public Review getReviewByID(int rid) {
        List<Map<String, Object>> commentList = jdbcTemplate.queryForList("SELECT * FROM reviews WHERE revID=?",
                new Object[] { rid });
        if (commentList == null || commentList.isEmpty()) {
            return null;
        }
        Review review = null;
        for (Map<String, Object> o : commentList) {
            // System.out.println("\n\n" + o.get("placeID") + "\n\n");
            review = new Review((String) o.get("userID"),
                    rid, (String) o.get("review"), (int) o.get("stars"),
                    (LocalDateTime) o.get("timestamp"), (boolean) o.get("edited"),
                    (int) o.get("placeID"),
                    (int) o.get("likes"),
                    (int) o.get("dislikes"));
        }
        return review;
    }

}
