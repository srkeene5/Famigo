package com.famigo.website.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.famigo.website.model.Event;

@Repository
public class TravelRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository ur;

    @Autowired
    PlaceRepository pr;

    public void createEvent(Event e) {
        String sql = "INSERT INTO event (id, name, creator, place, start, end, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, e.getID());
                ps.setString(2, e.getName());
                ps.setString(3, e.getCreator());
                ps.setInt(4, e.getPlace().getId());
                ps.setTimestamp(5, Timestamp.valueOf(e.getStart()));
                ps.setTimestamp(6, Timestamp.valueOf(e.getEnd()));
                ps.setString(7, e.getDescription());
            }
            
        });
    }

    public void editEvent(Event e) {
        String sql = "UPDATE event SET name=?, creator=?, place=?, start=?, end=?, description=? WHERE id=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, e.getName());
                ps.setString(2, e.getCreator());
                ps.setInt(3, e.getPlace().getId());
                ps.setTimestamp(4, Timestamp.valueOf(e.getStart()));
                ps.setTimestamp(5, Timestamp.valueOf(e.getEnd()));
                ps.setString(6, e.getDescription());
                ps.setString(7, e.getID());
            }
            
        });
    }

    public List<Event> getEvents(String userID) {
        List<Map<String, Object>> eventList = jdbcTemplate.queryForList("SELECT * FROM event WHERE creator=?", userID);
        ArrayList<Event> events = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            Map<String, Object> event = eventList.get(i);
            events.add(convertEvent(event));
        }
        return events;
    }

    public Event convertEvent(Map<String, Object> event) {
        return new Event((String) event.get("id"), (String) event.get("name"), (String) event.get("creator"), pr.getPlaceById((int) event.get("place")), (LocalDateTime) event.get("start"), (LocalDateTime) event.get("end"), (String) event.get("description"));
    }

    public Event getEvent(String eventID) {
        Event e = jdbcTemplate.queryForObject("SELECT * FROM event WHERE id=?", new RowMapper<Event>() {

            @Override
            public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub
                Event event = new Event(rs.getString("id"), rs.getString("name"), rs.getString("creator"), pr.getPlaceById(rs.getInt("place")), (LocalDateTime) rs.getObject("start"), (LocalDateTime) rs.getObject("end"), rs.getString("description"));
                return event;
            }
            
        }, eventID);
        return e;
    }

}
