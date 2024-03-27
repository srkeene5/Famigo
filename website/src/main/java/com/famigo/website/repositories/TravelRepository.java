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
import com.famigo.website.model.TravelGroup;
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

    /* Event Stuff */

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
                Event event = new Event(rs.getString("id"), rs.getString("name"), rs.getString("creator"), pr.getPlaceById(rs.getString("place")), (LocalDateTime) rs.getObject("start"), (LocalDateTime) rs.getObject("end"), rs.getString("description"), rs.getString("tripID"));
                return event;
            }
            
        }, eventID);
        return e;
    }

    /* Trip Stuff */

    public void createTrip(Trip t) {
        String sql = "INSERT INTO trip (id, name, owner, created, description, travelGroupID) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, t.getID());
                ps.setString(2, t.getName());
                ps.setString(3, t.getOwner().getID());
                ps.setTimestamp(4, Timestamp.valueOf(t.getCreated()));
                ps.setString(5, t.getDescription());
                ps.setString(6, t.getTravelGroupID());
            }

        });
        for (int i = 0; i < t.getMembers().size(); i++) {
            inviteTripMember(t.getMembers().get(i).getID(), t.getID());
            if (!t.getTravelGroupID().equals("NULL")) {
                evaluateRequest(t.getID(), t.getMembers().get(i).getID(), InviteStatus.ACCEPTED);
            }
        }
        evaluateRequest(t.getID(), Utilities.getUserID(), InviteStatus.ACCEPTED);
    }

    public void inviteTripMember(String userID, String tripID) {
        String sql = "INSERT INTO tripMembers (tripID, userID, inviteStatus) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, tripID);
                ps.setString(2, userID);
                ps.setInt(3, InviteStatus.INVITED.getInviteStatusInt());
            }
            
        });
    }

    public void evaluateRequest(String tripID, String userID, InviteStatus i) {
        String sql = "UPDATE tripMembers SET inviteStatus=? WHERE userID=? AND tripID=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, i.getInviteStatusInt());
                ps.setString(2, userID);
                ps.setString(3, tripID);
            }
            
        });
    }

    public ArrayList<Trip> getTripInvites() {
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
        Trip t = jdbcTemplate.queryForObject(sql, new RowMapper<Trip>() {

            @Override
            public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
                Trip newTrip = new Trip(tripID, rs.getString("name"), ur.getUser("id", rs.getString("owner")), members, rs.getTimestamp("created").toLocalDateTime(), rs.getString("description"), rs.getString("travelGroupID"));
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
                ps.setString(1, tripID);
            }
            
        });
        jdbcTemplate.update(sql2, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, tripID);
            }
            
        });
        jdbcTemplate.update(sql3, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, tripID);
            }
            
        });
    }
    /* Travel Group Stuff */

    public void createTravelGroup(TravelGroup t) {
        String sql = "INSERT INTO travelGroup (id, name, owner, created, description, conversationID) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, t.getID());
                ps.setString(2, t.getName());
                ps.setString(3, t.getOwner().getID());
                ps.setTimestamp(4, Timestamp.valueOf(t.getCreated()));
                ps.setString(5, t.getDescription());
                ps.setString(6, t.getConversationID());
            }

        });
        for (int i = 0; i < t.getMembers().size(); i++) {
            inviteTravelGroupMember(t.getMembers().get(i).getID(), t.getID());
        }
        evaluateGroupRequest(t.getID(), InviteStatus.ACCEPTED);
    }

    public void deleteGroup(String travelGroupID) {
        String sql = "DELETE FROM travelGroupMembers WHERE travelGroupID=?";
        String sql2 = "UPDATE trip SET travelGroupID=? WHERE travelGroupID=?";
        String sql3 = "DELETE FROM travelGroup WHERE id=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, travelGroupID);
            }
            
        });
        jdbcTemplate.update(sql2, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, "NULL");
                ps.setString(2, travelGroupID);
            }
            
        });
        jdbcTemplate.update(sql3, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, travelGroupID);
            }
            
        });
    }

    public void inviteTravelGroupMember(String userID, String travelGroupID) {
        String sql = "INSERT INTO travelGroupMembers (travelGroupID, userID, inviteStatus) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, travelGroupID);
                ps.setString(2, userID);
                ps.setInt(3, InviteStatus.INVITED.getInviteStatusInt());
            }
            
        });
    }

    public void evaluateGroupRequest(String travelGroupID, InviteStatus i) {
        String sql = "UPDATE travelGroupMembers SET inviteStatus=? WHERE userID=? AND travelGroupID=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, i.getInviteStatusInt());
                ps.setString(2, Utilities.getUserID());
                ps.setString(3, travelGroupID);
            }
            
        });
    }

    public ArrayList<TravelGroup> getGroupInvites() {
        String sql = "SELECT travelGroupID FROM travelGroupMembers WHERE userID=? AND inviteStatus=?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, Utilities.getUserID(), InviteStatus.INVITED.getInviteStatusInt());
        ArrayList<TravelGroup> travelGroups = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            travelGroups.add(getGroup((String) result.get(i).get("travelGroupID")));
        }
        return travelGroups;
    }

    public TravelGroup getGroup(String travelGroupID) {
        String sql = "SELECT * FROM travelGroup WHERE id=?";
        String sql2 = "SELECT userID FROM travelGroupMembers WHERE travelGroupID=? AND inviteStatus=?";
        ArrayList<User> members = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql2, travelGroupID, InviteStatus.ACCEPTED.getInviteStatusInt());
        for (int i = 0; i < result.size(); i++) {
            members.add(ur.getUser("id", (String) result.get(i).get("userID")));
        }
        TravelGroup t = jdbcTemplate.queryForObject(sql, new RowMapper<TravelGroup>() {

            @Override
            public TravelGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
                TravelGroup newTravelGroup = new TravelGroup(travelGroupID, rs.getString("name"), ur.getUser("id", rs.getString("owner")), rs.getTimestamp("created").toLocalDateTime(), rs.getString("description"), members, rs.getString("conversationID"));
                return newTravelGroup;
            }
            
        }, travelGroupID);
        return t;
    }

    public ArrayList<TravelGroup> getTravelGroups() {
        String sql = "SELECT travelGroupID FROM travelGroupMembers WHERE userID=? AND inviteStatus=?";
        ArrayList<TravelGroup> travelGroups = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, Utilities.getUserID(), InviteStatus.ACCEPTED.getInviteStatusInt());
        for (int i = 0; i < result.size(); i++) {
            travelGroups.add(getGroup((String) result.get(i).get("travelGroupID")));
        }
        return travelGroups;
    }

    public ArrayList<Trip> getGroupTrips(String groupID) {
        String sql = "SELECT tripID FROM tripMembers WHERE userID=? AND inviteStatus=?";
        ArrayList<Trip> trips = new ArrayList<>();
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, Utilities.getUserID(), InviteStatus.ACCEPTED.getInviteStatusInt());
        for (int i = 0; i < result.size(); i++) {
            Trip t = getTrip((String) result.get(i).get("tripID"));
            if (t.getTravelGroupID().equals(groupID)) {
                trips.add(t);
            }
        }
        return trips;
    }

}
