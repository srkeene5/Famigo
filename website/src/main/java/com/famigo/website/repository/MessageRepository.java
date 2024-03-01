package com.famigo.website.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.Conversation;
import com.famigo.website.model.Message;
import com.famigo.website.model.User;

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
                ps.setDate(4, Date.valueOf(message.getTimestamp().toLocalDate()));
                ps.setBoolean(5, message.getEdited());
                ps.setString(6, message.getConversation());
            }
            
        });
    }

    public ArrayList<Message> getMessages(String cid) {
        List<Map<String, Object>> messageList = jdbcTemplate.queryForList("SELECT * FROM message WHERE conversation=?", new Object[]{cid});
        if (messageList == null || messageList.isEmpty()) {
            return null;
        }
        ArrayList<Message> messages = new ArrayList<>();
        for (Map<String, Object> o : messageList) {
            messages.add(new Message((String) o.get("id"), (String) o.get("sender"), (String) o.get("content"), (LocalDateTime) o.get("timestamp"), (Boolean) o.get("edited"), (String) o.get("conversation")));
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
        ArrayList<Message> messages = getMessages(cid);
        Conversation conversationObject = jdbcTemplate.queryForObject("SELECT name FROM conversation WHERE id=?", new RowMapper<Conversation>() {

            @Override
            public Conversation mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs == null) {
                    return null;
                }
                Conversation conversation = new Conversation(cid, rs.getString("name"), members, messages);
                return conversation;
            }
            
        }, cid);
        return conversationObject;
    }

}