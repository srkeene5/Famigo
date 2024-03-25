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

import com.famigo.website.model.Comment;

@Repository
public class CommentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addComment(Comment comment) {
        String sql = "INSERT INTO comments (comID, userID, comment, likes, timestamp, edited, reviewID) VALUES (?, ?, ?, ?, CAST(? AS DATETIME), ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, comment.getComId());
                ps.setString(2, comment.getComUserId());
                ps.setString(3, comment.getComment());
                ps.setInt(4, comment.getLikes());
                ps.setTimestamp(5, Timestamp.valueOf(comment.getTimeStamp()));
                ps.setBoolean(6, comment.isEdited());
                ps.setInt(7, comment.getReviewId());
            }

        });
    }

    public ArrayList<Comment> getCommentsByUser(String uid) {
        List<Map<String, Object>> commentList = jdbcTemplate.queryForList("SELECT * FROM comments WHERE userID=?",
                new Object[] { uid });
        if (commentList == null || commentList.isEmpty()) {
            return null;
        }
        ArrayList<Comment> comments = new ArrayList<>();
        for (Map<String, Object> o : commentList) {
            comments.add(new Comment(uid, (int) o.get("comID"),
                    (String) o.get("comment"), (int) o.get("likes"),
                    (LocalDateTime) o.get("timestamp"), (boolean) o.get("edited"),
                    (int) o.get("reviewID")));
        }
        return comments;
    }

    public ArrayList<Comment> getCommentsByReview(int rid) {
        List<Map<String, Object>> commentList = jdbcTemplate.queryForList("SELECT * FROM comments WHERE reviewID=?",
                new Object[] { rid });
        if (commentList == null || commentList.isEmpty()) {
            return null;
        }
        ArrayList<Comment> comments = new ArrayList<>();
        for (Map<String, Object> o : commentList) {
            comments.add(new Comment((String) o.get("userID"),
                    (int) o.get("comID"), (String) o.get("comment"),
                    (int) o.get("likes"), (LocalDateTime) o.get("timestamp"),
                    (boolean) o.get("edited"), rid));
        }
        return comments;
    }

}
