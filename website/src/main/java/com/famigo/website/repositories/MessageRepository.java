package com.famigo.website.repositories;

import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.Conversation;
import com.famigo.website.model.Message;
import com.famigo.website.model.User;
import com.famigo.website.utilities.Status;

@Repository
public class MessageRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository ur;

    public void addMessage(Message message) {
        String sql = "INSERT INTO message (id, sender, content, timestamp, edited, conversation) VALUES (?, ?, ?, CAST(? AS DATETIME), ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                // TODO Auto-generated method stub
                ps.setString(1, message.getId());
                ps.setString(2, message.getSender());
                ps.setString(3, message.getContent());
                ps.setTimestamp(4, Timestamp.valueOf(message.getTimestamp()));
                ps.setBoolean(5, message.getEdited());
                ps.setString(6, message.getConversation());
            }
            
        });
    }

    public void addToUnread(Message message, Conversation conversation, String userID) {
        ArrayList<User> members = conversation.getMembers();
        for (int i = 0; i < members.size(); i++) {
            User user = members.get(i);
            jdbcTemplate.update("INSERT INTO unread (messageID, conversationID, userID) VALUES (?, ?, ?)", new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    // TODO Auto-generated method stub
                    ps.setString(1, message.getId());
                    ps.setString(2, conversation.getID());
                    ps.setString(3, user.getID());
                }
                
            });
        }
    }

    public ArrayList<Message> getUnread(String userID, String cid) {
        List<Map<String, Object>> messageIDList = jdbcTemplate.queryForList("SELECT messageID FROM unread WHERE userID=? AND conversationID=?", userID, cid);
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < messageIDList.size(); i++) {
            String id = (String) messageIDList.get(i).get("messageID");
            messages.add(getMessage(id));
            jdbcTemplate.update("DELETE FROM unread WHERE userID=? AND messageID=?", new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    // TODO Auto-generated method stub
                    ps.setString(1, userID);
                    ps.setString(2, id);
                }

            });
        }
        return messages;
    }

    public ArrayList<Message> getMessages(String cid) {
        List<Map<String, Object>> messageList = jdbcTemplate.queryForList("SELECT * FROM message WHERE conversation=? ORDER BY timestamp", new Object[]{cid});
        if (messageList == null || messageList.isEmpty()) {
            return null;
        }
        ArrayList<Message> messages = new ArrayList<>();
        for (Map<String, Object> o : messageList) {
            messages.add(new Message((String) o.get("id"), (String) o.get("sender"), (String) o.get("content"), (LocalDateTime) o.get("timestamp"), (Boolean) o.get("edited"), (String) o.get("conversation"), Status.READ));
        }
        return messages;
    }

    public void addConversation(Conversation conversation) {
        jdbcTemplate.update("INSERT INTO conversation (id, name) VALUES (?, ?)", new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, conversation.getID());
                ps.setString(2, conversation.getName());
            }
            
        });
        for (User u : conversation.getMembers()) {
            jdbcTemplate.update("INSERT INTO conversationParticipants (conversationID, userID) VALUES (?, ?)", new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                    // TODO Auto-generated method stub
                    ps.setString(1, conversation.getID());
                    ps.setString(2, u.getID());
                }
                
            });
        }
    }

    public void addUserToConversation(String userID, String conversationID) {
        jdbcTemplate.update("INSERT INTO conversationParticipants (conversationID, userID) VALUES (?, ?)", new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException, DataAccessException {
                // TODO Auto-generated method stub
                ps.setString(1, conversationID);
                ps.setString(2, userID);
            }
            
        });
    }

    public ArrayList<Conversation> getConversations(String userID) {
        List<Map<String, Object>> conversationList = jdbcTemplate.queryForList("SELECT conversationID FROM conversationParticipants WHERE userID=?", new Object[]{userID});
        if (conversationList == null) {
            return null;
        }
        ArrayList<Conversation> conversations = new ArrayList<>();
        for (Map<String, Object> o : conversationList) {
            conversations.add(getConversation((String) o.get("conversationID")));
        }
        return conversations;
    }

    public Conversation getConversation(String cid) {
        List<Map<String, Object>> memberList = jdbcTemplate.queryForList("SELECT userID FROM conversationParticipants WHERE conversationID=?", new Object[]{cid});
        ArrayList<User> members = new ArrayList<>();
        for (Map<String, Object> o : memberList) {
            members.add(ur.getUser("id", (String) o.get("userID")));
        }
        Conversation conversationObject = jdbcTemplate.queryForObject("SELECT name FROM conversation WHERE id=?", new RowMapper<Conversation>() {

            @Override
            public Conversation mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs == null) {
                    return null;
                }
                Conversation conversation = new Conversation(cid, rs.getString("name"), members);
                return conversation;
            }
            
        }, cid);
        return conversationObject;
    }

    public Message getMessage(String messageID) {
        try {
            Message message = jdbcTemplate.queryForObject("SELECT * FROM message WHERE id=?", new RowMapper<Message>() {

                @Override
                public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
                    // TODO Auto-generated method stub
                    Message m = new Message(rs.getString("id"), rs.getString("sender"), rs.getString("content"), (LocalDateTime) rs.getObject("timestamp"), rs.getBoolean("edited"), rs.getString("conversation"), Status.READ);
                    return m;
                }

            }, messageID);
            return message;
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

}
