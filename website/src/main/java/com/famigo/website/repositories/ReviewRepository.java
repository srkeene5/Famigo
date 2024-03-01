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
                ps.setString(7, review.getPlaceId());
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
                    (String) o.get("placeID"), (int) o.get("likes"), (int) o.get("dislikes")));
        }
        return reviews;
    }

    public ArrayList<Review> getReviewsByPlace(String pid) {
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
                    (boolean) o.get("edited"), pid, (int) o.get("likes"), (int) o.get("dislikes")));
        }
        return reviews;
    }

    public void addReviewReaction(String uid, int rid, boolean isLike) {
        String sql = "INSERT INTO reviewReaction (userID, reviewID, isLike) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, uid);
                ps.setInt(2, rid);
                ps.setBoolean(3, isLike);
            }

        });
    }

    // Returns 1 if the given user has liked the given review, -1 if they disliked, and 0 if they haven't done either.
    public int[] getUserReviewReactions(String userId, ArrayList<Review> revs) {
        int[] reactions = new int[revs.size()];
        for (int i = 0; i < reactions.length; i++) {
            Map<String, Object> reaction = jdbcTemplate.queryForMap("SELECT * FROM reviewReaction WHERE userID=?, reviewID=?",
                    new Object[] {userId, revs.get(i).getRevId()});

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

}
