package com.famigo.website.repositories;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.Comment;
import com.famigo.website.model.Review;

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
                ps.setInt(4, comment.getClikes());
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


    // returns comment reactions with the key being the review id and the entries being the reaction value
    // 1 = user liked comment, -1 = user disliked, 0 = neither
    public HashMap<Integer, List<Integer>> getUserCommentReactions(String userId, Map<Integer, List<Comment>> commentsOnReviews) {
        HashMap<Integer, List<Integer>> commentReactions = new HashMap<>();
        for (Map.Entry<Integer, List<Comment>> entry : commentsOnReviews.entrySet()) {
            int i = 0;
            Integer review = entry.getKey();
            List<Comment> comments = entry.getValue();
            if (comments != null) {
                List<Integer> reactions = new ArrayList<>();
                for (Comment comment : comments) {
                    Map<String, Object> reaction;
                    try {
                        reaction = jdbcTemplate.queryForMap("SELECT * FROM commentReaction WHERE userID=? AND commentID=?",
                                new Object[] { userId, comment.getComId() });
                    } catch (DataAccessException e) {
                        reaction = null;
                    }
                    int reactionNum = 0; // original status
                    if (reaction != null && !reaction.isEmpty()) {
                        boolean isLike = (boolean) reaction.get("isLike");
                        if (isLike) reactionNum = 1;
                        else reactionNum = -1;
                    }
                    reactions.add(reactionNum);
                }
                commentReactions.put(review, reactions);

            }  
        }

        // print for testing
        for (Map.Entry<Integer, List<Integer>> entry_ : commentReactions.entrySet()) {
            Integer reviewId = entry_.getKey(); 
            List<Integer> reactions = entry_.getValue(); 
            for (Integer reaction : reactions) { 
                System.out.println("reviewID=" + reviewId + ", Reaction=" + reaction); 
            } 
        }
        
        return commentReactions;
    }

    // This method fires when a user presses the like or dislike button on a review, and
    // adds and/or removes reactions as necessary.
    public void alterCommentReaction(String uid, int cid, boolean isLike) {
        System.out.println("uid, cid, and islike-->" + uid + " " + cid + " " + isLike);
        // First, check if the user has liked or disliked this comment already
        Map<String, Object> pastReaction;
        try {
            pastReaction = jdbcTemplate.queryForMap("SELECT * FROM commentReaction WHERE userID=? AND commentID=?",
                    new Object[] { uid, cid });
        } catch (DataAccessException e) {
            pastReaction = null;
        }

        // Check if past reaction exists
        if (pastReaction != null) {

            if (((boolean) pastReaction.get("isLike")) == isLike) {
                // If past reaction and current reaction match, the user is removing an earlier reaction
                jdbcTemplate.update("DELETE FROM commentReaction WHERE userID=? AND commentID=?",
                        new Object[] {uid, cid});
                if (isLike) {
                    jdbcTemplate.update("UPDATE comments SET likes = likes - 1 WHERE comID=?",
                            new Object[] {cid});
                } else {
                    jdbcTemplate.update("UPDATE comments SET dislikes = dislikes - 1 WHERE comID=?",
                            new Object[] {cid});
                }

            } else {
                // If past reaction and current reaction don't match, the user is adding the opposite
                // reaction, in which case their previous reaction must also be deleted. So we can
                // just update the isLike value in the SQL entry.
                String sqlStr = "UPDATE commentReaction SET isLike = NOT isLike WHERE userID=? AND commentID=?";
                jdbcTemplate.update(sqlStr, new Object[] {uid, cid});
                if (isLike) {
                    jdbcTemplate.update("UPDATE comments SET likes = likes + 1 WHERE comID=?",
                            new Object[] {cid});
                    jdbcTemplate.update("UPDATE comments SET dislikes = dislikes - 1 WHERE comID=?",
                            new Object[] {cid});
                } else {
                    jdbcTemplate.update("UPDATE comments SET dislikes = dislikes + 1 WHERE comID=?",
                            new Object[] {cid});
                    jdbcTemplate.update("UPDATE comments SET likes = likes - 1 WHERE comID=?",
                            new Object[] {cid});
                }
            }
        } else {
            // There is no past reaction, so just add the current one
            String sqlStr = "INSERT INTO commentReaction (userID, commentID, isLike) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlStr, new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                    ps.setString(1, uid);
                    ps.setInt(2, cid);
                    ps.setBoolean(3, isLike);
                }
            });
            if (isLike) {
                jdbcTemplate.update("UPDATE comments SET likes = likes + 1 WHERE comID=?",
                        new Object[] {cid});
            } else {
                jdbcTemplate.update("UPDATE comments SET dislikes = dislikes + 1 WHERE comID=?",
                        new Object[] {cid});
            }
        }
    }

    public void printAllCommentReactions(String userid) {
        String sql = "SELECT * FROM commentReaction WHERE userID=?"; 
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[] {userid});
        int i = 1;
        for (Map<String, Object> row : rows) {
            System.out.println(i);
            System.out.println("userID: " + row.get("userID"));
            System.out.println("commentID: " + row.get("commentID"));
            System.out.println("isLike: " + row.get("isLike"));
            System.out.println("~~~~~~~~~~~~~~");
            i++;
        }
    }

    public void printAllCommentReactions() {
        String sql = "SELECT * FROM commentReaction"; 
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        int i = 1;
        for (Map<String, Object> row : rows) {
            System.out.println(i);
            System.out.println("userID: " + row.get("userID"));
            System.out.println("commentID: " + row.get("commentID"));
            System.out.println("isLike: " + row.get("isLike"));
            System.out.println("~~~~~~~~~~~~~~");
            i++;
        }
    }

}
