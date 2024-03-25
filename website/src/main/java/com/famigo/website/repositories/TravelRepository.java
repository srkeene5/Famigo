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
import com.famigo.website.model.Trip;
import com.famigo.website.model.User;
import com.famigo.website.utilities.InviteStatus;
import com.famigo.website.utilities.Utilities;

@Repository
public class TravelRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UserRepository ur;

    @Autowired
    PlaceRepository pr;

    public void createEvent(Event e) {
        String sql = "INSERT INTO event (id, name, creator, place, start, end, description, tripID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, e.getID());
                ps.setString(2, e.getName());
                ps.setString(3, e.getCreator());
                ps.setInt(4, Integer.parseInt(e.getPlace().getId()));
                ps.setTimestamp(5, Timestamp.valueOf(e.getStart()));
                ps.setTimestamp(6, Timestamp.valueOf(e.getEnd()));
                ps.setString(7, e.getDescription());
                ps.setString(8, e.getTripID());
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
                ps.setInt(3, Integer.parseInt(e.getPlace().getId()));
                ps.setTimestamp(4, Timestamp.valueOf(e.getStart()));
                ps.setTimestamp(5, Timestamp.valueOf(e.getEnd()));
                ps.setString(6, e.getDescription());
                ps.setString(7, e.getID());
            }
            
        });
    }

    public void deleteEvent(String eventID) {
        String sql = "DELETE FROM event WHERE id=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, eventID);
            }
            
        });
    }

    public List<Event> getEvents(String tripID) {
        List<Map<String, Object>> eventList = jdbcTemplate.queryForList("SELECT * FROM event WHERE tripID=?", tripID);
        ArrayList<Event> events = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            Map<String, Object> event = eventList.get(i);
            events.add(convertEvent(event));
        }
        return events;
    }

    public Event convertEvent(Map<String, Object> event) {
        return new Event((String) event.get("id"), (String) event.get("name"), (String) event.get("creator"), pr.getPlaceById(String.valueOf(event.get("place"))), (LocalDateTime) event.get("start"), (LocalDateTime) event.get("end"), (String) event.get("description"), (String) event.get("tripID"));
    }

    public Event getEvent(String eventID) {
        Event e = jdbcTemplate.queryForObject("SELECT * FROM event WHERE id=?", new RowMapper<Event>() {

            @Override
            public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub
                Event event = new Event(rs.getString("id"), rs.getString("name"), rs.getString("creator"), pr.getPlaceById(rs.getString("place")), (LocalDateTime) rs.getObject("start"), (LocalDateTime) rs.getObject("end"), rs.getString("description"), rs.getString("tripID"));
                return event;
            }
            
        }, eventID);
        return e;
    }

    public void createTrip(Trip t) {
        String sql = "INSERT INTO trip (id, name, owner, created, description) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, t.getID());
                ps.setString(2, t.getName());
                ps.setString(3, t.getOwner().getID());
                ps.setTimestamp(4, Timestamp.valueOf(t.getCreated()));
                ps.setString(5, t.getDescription());
            }

        });
        for (int i = 0; i < t.getMembers().size(); i++) {
            inviteMember(t.getMembers().get(i).getID(), t.getID());
        }
        evaluateRequest(t.getID(), InviteStatus.ACCEPTED);
    }

    public void inviteMember(String userID, String tripID) {
        String sql = "INSERT INTO tripMembers (tripID, userID, inviteStatus) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, tripID);
                ps.setString(2, userID);
                ps.setInt(3, InviteStatus.INVITED.getInviteStatusInt());
            }
            
        });
    }

    public void evaluateRequest(String tripID, InviteStatus i) {
        String sql = "UPDATE tripMembers SET inviteStatus=? WHERE userID=? AND tripID=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setInt(1, i.getInviteStatusInt());
                ps.setString(2, Utilities.getUserID());
                ps.setString(3, tripID);
            }
            
        });
    }

    public ArrayList<Trip> getInvites() {
        String sql = "SELECT tripID FROM tripMembers WHERE userID=? AND inviteStatus=?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, Utilities.getUserID(), InviteStatus.INVITED.getInviteStatusInt());
        ArrayList<Trip> trips = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            trips.add(getTrip((String) result.get(i).get("tripID")));
        }
        return trips;
    }

    public Trip getTrip(String tripID) {
        String sql = "SELECT * FROM trip WHERE id=?";
        String sql2 = "SELECT userID FROM tripMembers WHERE tripID=? AND inviteStatus=?";
        ArrayList<User> members = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2, tripID, InviteStatus.ACCEPTED.getInviteStatusInt());
        for (int i = 0; i < result.size(); i++) {
            members.add(ur.getUser("id", (String) result.get(i).get("userID")));
        }
        System.out.println(members.toString());
        Trip t = jdbcTemplate.queryForObject(sql, new RowMapper<Trip>() {

            @Override
            public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                // TODO Auto-generated method stub
                Trip newTrip = new Trip(tripID, rs.getString("name"), ur.getUser("id", rs.getString("owner")), members, rs.getTimestamp("created").toLocalDateTime(), rs.getString("description"));
                return newTrip;
            }
            
        }, tripID);
        return t;
    }

    public ArrayList<Trip> getTrips() {
        String sql = "SELECT tripID FROM tripMembers WHERE userID=? AND inviteStatus=?";
        ArrayList<Trip> trips = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, Utilities.getUserID(), InviteStatus.ACCEPTED.getInviteStatusInt());
        for (int i = 0; i < result.size(); i++) {
            trips.add(getTrip((String) result.get(i).get("tripID")));
        }
        return trips;
    }

    public void deleteTrip(String tripID) {
        String sql = "DELETE FROM tripMembers WHERE tripID=?";
        String sql2 = "DELETE FROM event WHERE tripID=?";
        String sql3 = "DELETE FROM trip WHERE id=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, tripID);
            }
            
        });
        jdbcTemplate.update(sql2, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, tripID);
            }
            
        });
        jdbcTemplate.update(sql3, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                // TODO Auto-generated method stub
                ps.setString(1, tripID);
            }
            
        });
    }

}
