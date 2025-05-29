package com.skillswaphub.dao;

import com.skillswaphub.model.Session;
import com.skillswaphub.model.User;
import com.skillswaphub.model.Skill;
import com.skillswaphub.model.SkillRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Session-related database operations
 */
public class SessionDAO {
    private static final Logger LOGGER = Logger.getLogger(SessionDAO.class.getName());
    
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    private SkillRequestDAO requestDAO;
    
    public SessionDAO() {
        this.userDAO = new UserDAO();
        this.skillDAO = new SkillDAO();
        this.requestDAO = new SkillRequestDAO();
    }
    
    /**
     * Create a new session
     * @param session Session object to create
     * @return true if successful, false otherwise
     */
    public boolean createSession(Session session) {
        String sql = "INSERT INTO sessions (request_id, teacher_id, student_id, skill_id, session_date, duration, location, notes, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, session.getRequestId());
            stmt.setInt(2, session.getTeacherId());
            stmt.setInt(3, session.getStudentId());
            stmt.setInt(4, session.getSkillId());
            stmt.setTimestamp(5, Timestamp.valueOf(session.getSessionDate()));
            stmt.setInt(6, session.getDuration());
            stmt.setString(7, session.getLocation());
            stmt.setString(8, session.getNotes());
            stmt.setString(9, session.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    session.setSessionId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating session", e);
            return false;
        }
    }
    
    /**
     * Update an existing session
     * @param session Session object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateSession(Session session) {
        String sql = "UPDATE sessions SET session_date = ?, duration = ?, location = ?, notes = ?, status = ? WHERE session_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(session.getSessionDate()));
            stmt.setInt(2, session.getDuration());
            stmt.setString(3, session.getLocation());
            stmt.setString(4, session.getNotes());
            stmt.setString(5, session.getStatus());
            stmt.setInt(6, session.getSessionId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating session", e);
            return false;
        }
    }
    
    /**
     * Update session status
     * @param sessionId Session ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateSessionStatus(int sessionId, String status) {
        String sql = "UPDATE sessions SET status = ? WHERE session_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, sessionId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating session status", e);
            return false;
        }
    }
    
    /**
     * Get a session by ID
     * @param sessionId Session ID to search for
     * @return Session object if found, null otherwise
     */
    public Session getSessionById(int sessionId) {
        String sql = "SELECT * FROM sessions WHERE session_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sessionId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Session session = extractSessionFromResultSet(rs);
                    
                    // Load related entities
                    loadRelatedEntities(session);
                    
                    return session;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting session by ID", e);
        }
        
        return null;
    }
    
    /**
     * Get all sessions for a user (as teacher or student)
     * @param userId User ID
     * @return List of sessions for the user
     */
    public List<Session> getSessionsForUser(int userId) {
        String sql = "SELECT * FROM sessions WHERE teacher_id = ? OR student_id = ? ORDER BY session_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Session> sessions = new ArrayList<>();
                
                while (rs.next()) {
                    Session session = extractSessionFromResultSet(rs);
                    loadRelatedEntities(session);
                    sessions.add(session);
                }
                
                return sessions;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting sessions for user", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get upcoming sessions for a user
     * @param userId User ID
     * @return List of upcoming sessions
     */
    public List<Session> getUpcomingSessionsForUser(int userId) {
        String sql = "SELECT * FROM sessions WHERE (teacher_id = ? OR student_id = ?) AND session_date > NOW() AND status = 'Scheduled' ORDER BY session_date ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Session> sessions = new ArrayList<>();
                
                while (rs.next()) {
                    Session session = extractSessionFromResultSet(rs);
                    loadRelatedEntities(session);
                    sessions.add(session);
                }
                
                return sessions;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting upcoming sessions for user", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get past sessions for a user
     * @param userId User ID
     * @return List of past sessions
     */
    public List<Session> getPastSessionsForUser(int userId) {
        String sql = "SELECT * FROM sessions WHERE (teacher_id = ? OR student_id = ?) AND (session_date < NOW() OR status = 'Completed') ORDER BY session_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Session> sessions = new ArrayList<>();
                
                while (rs.next()) {
                    Session session = extractSessionFromResultSet(rs);
                    loadRelatedEntities(session);
                    sessions.add(session);
                }
                
                return sessions;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting past sessions for user", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get sessions for a specific request
     * @param requestId Request ID
     * @return List of sessions for the request
     */
    public List<Session> getSessionsForRequest(int requestId) {
        String sql = "SELECT * FROM sessions WHERE request_id = ? ORDER BY session_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Session> sessions = new ArrayList<>();
                
                while (rs.next()) {
                    Session session = extractSessionFromResultSet(rs);
                    loadRelatedEntities(session);
                    sessions.add(session);
                }
                
                return sessions;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting sessions for request", e);
        }
        
        return new ArrayList<>();
    }
    
    // Helper methods
    
    /**
     * Extract session data from a ResultSet
     * @param rs ResultSet containing session data
     * @return Session object
     * @throws SQLException if a database access error occurs
     */
    private Session extractSessionFromResultSet(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setSessionId(rs.getInt("session_id"));
        session.setRequestId(rs.getInt("request_id"));
        session.setTeacherId(rs.getInt("teacher_id"));
        session.setStudentId(rs.getInt("student_id"));
        session.setSkillId(rs.getInt("skill_id"));
        session.setSessionDate(rs.getTimestamp("session_date").toLocalDateTime());
        session.setDuration(rs.getInt("duration"));
        session.setLocation(rs.getString("location"));
        session.setNotes(rs.getString("notes"));
        session.setStatus(rs.getString("status"));
        session.setCreatedAt(rs.getTimestamp("created_at"));
        session.setUpdatedAt(rs.getTimestamp("updated_at"));
        return session;
    }
    
    /**
     * Load related entities for a session
     * @param session Session to load entities for
     */
    private void loadRelatedEntities(Session session) {
        // Load teacher
        User teacher = userDAO.getUserById(session.getTeacherId());
        session.setTeacher(teacher);
        
        // Load student
        User student = userDAO.getUserById(session.getStudentId());
        session.setStudent(student);
        
        // Load skill
        Skill skill = skillDAO.getSkillById(session.getSkillId());
        session.setSkill(skill);
        
        // Load request
        SkillRequest request = requestDAO.getRequestById(session.getRequestId());
        session.setRequest(request);
    }
}
