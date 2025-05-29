package com.skillswaphub.dao;

import com.skillswaphub.model.SkillRequest;
import com.skillswaphub.model.User;
import com.skillswaphub.model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for SkillRequest-related database operations
 */
public class SkillRequestDAO {
    private static final Logger LOGGER = Logger.getLogger(SkillRequestDAO.class.getName());
    
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    
    public SkillRequestDAO() {
        this.userDAO = new UserDAO();
        this.skillDAO = new SkillDAO();
    }
    
    /**
     * Create a new skill request
     * @param request SkillRequest object to create
     * @return true if successful, false otherwise
     */
    public boolean createRequest(SkillRequest request) {
        String sql = "INSERT INTO skill_requests (sender_id, receiver_id, offered_skill_id, wanted_skill_id, message) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, request.getSenderId());
            stmt.setInt(2, request.getReceiverId());
            stmt.setInt(3, request.getOfferedSkillId());
            stmt.setInt(4, request.getWantedSkillId());
            stmt.setString(5, request.getMessage());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    request.setRequestId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating skill request", e);
            return false;
        }
    }
    
    /**
     * Get a skill request by ID
     * @param requestId Request ID to search for
     * @return SkillRequest object if found, null otherwise
     */
    public SkillRequest getRequestById(int requestId) {
        String sql = "SELECT * FROM skill_requests WHERE request_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SkillRequest request = extractRequestFromResultSet(rs);
                    
                    // Load related entities
                    loadRelatedEntities(request);
                    
                    return request;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting request by ID", e);
        }
        
        return null;
    }
    
    /**
     * Get all pending requests received by a user
     * @param userId User ID
     * @return List of pending requests
     */
    public List<SkillRequest> getPendingRequestsForUser(int userId) {
        return getRequestsByUserAndStatus(userId, "receiver_id", "Pending");
    }
    
    /**
     * Get all requests sent by a user
     * @param userId User ID
     * @return List of sent requests
     */
    public List<SkillRequest> getSentRequestsByUser(int userId) {
        String sql = "SELECT * FROM skill_requests WHERE sender_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<SkillRequest> requests = new ArrayList<>();
                
                while (rs.next()) {
                    SkillRequest request = extractRequestFromResultSet(rs);
                    loadRelatedEntities(request);
                    requests.add(request);
                }
                
                return requests;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting sent requests", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get all requests received by a user
     * @param userId User ID
     * @return List of received requests
     */
    public List<SkillRequest> getReceivedRequestsByUser(int userId) {
        String sql = "SELECT * FROM skill_requests WHERE receiver_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<SkillRequest> requests = new ArrayList<>();
                
                while (rs.next()) {
                    SkillRequest request = extractRequestFromResultSet(rs);
                    loadRelatedEntities(request);
                    requests.add(request);
                }
                
                return requests;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting received requests", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get the count of pending requests for a user
     * @param userId User ID
     * @return Count of pending requests
     */
    public int getPendingRequestCount(int userId) {
        String sql = "SELECT COUNT(*) FROM skill_requests WHERE receiver_id = ? AND status = 'Pending'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting pending request count", e);
        }
        
        return 0;
    }
    
    /**
     * Update the status of a skill request
     * @param requestId Request ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateRequestStatus(int requestId, String status) {
        String sql = "UPDATE skill_requests SET status = ? WHERE request_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating request status", e);
            return false;
        }
    }
    
    /**
     * Check if a request already exists between two users for specific skills
     * @param senderId Sender user ID
     * @param receiverId Receiver user ID
     * @param offeredSkillId Offered skill ID
     * @param wantedSkillId Wanted skill ID
     * @return true if request exists, false otherwise
     */
    public boolean requestExists(int senderId, int receiverId, int offeredSkillId, int wantedSkillId) {
        String sql = "SELECT COUNT(*) FROM skill_requests " +
                     "WHERE sender_id = ? AND receiver_id = ? AND offered_skill_id = ? AND wanted_skill_id = ? " +
                     "AND status IN ('Pending', 'Accepted')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setInt(3, offeredSkillId);
            stmt.setInt(4, wantedSkillId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if request exists", e);
        }
        
        return false;
    }
    
    // Helper methods
    
    /**
     * Get requests by user ID, field, and status
     * @param userId User ID
     * @param field Field to filter by (sender_id or receiver_id)
     * @param status Status to filter by
     * @return List of matching requests
     */
    private List<SkillRequest> getRequestsByUserAndStatus(int userId, String field, String status) {
        String sql = "SELECT * FROM skill_requests WHERE " + field + " = ? AND status = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<SkillRequest> requests = new ArrayList<>();
                
                while (rs.next()) {
                    SkillRequest request = extractRequestFromResultSet(rs);
                    loadRelatedEntities(request);
                    requests.add(request);
                }
                
                return requests;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting requests by user and status", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Extract request data from a ResultSet
     * @param rs ResultSet containing request data
     * @return SkillRequest object
     * @throws SQLException if a database access error occurs
     */
    private SkillRequest extractRequestFromResultSet(ResultSet rs) throws SQLException {
        SkillRequest request = new SkillRequest();
        request.setRequestId(rs.getInt("request_id"));
        request.setSenderId(rs.getInt("sender_id"));
        request.setReceiverId(rs.getInt("receiver_id"));
        request.setOfferedSkillId(rs.getInt("offered_skill_id"));
        request.setWantedSkillId(rs.getInt("wanted_skill_id"));
        request.setStatus(rs.getString("status"));
        request.setMessage(rs.getString("message"));
        request.setCreatedAt(rs.getTimestamp("created_at"));
        request.setUpdatedAt(rs.getTimestamp("updated_at"));
        return request;
    }
    
    /**
     * Load related entities (users and skills) for a request
     * @param request SkillRequest to load entities for
     */
    private void loadRelatedEntities(SkillRequest request) {
        // Load sender
        User sender = userDAO.getUserById(request.getSenderId());
        request.setSender(sender);
        
        // Load receiver
        User receiver = userDAO.getUserById(request.getReceiverId());
        request.setReceiver(receiver);
        
        // Load offered skill
        Skill offeredSkill = skillDAO.getSkillById(request.getOfferedSkillId());
        request.setOfferedSkill(offeredSkill);
        
        // Load wanted skill
        Skill wantedSkill = skillDAO.getSkillById(request.getWantedSkillId());
        request.setWantedSkill(wantedSkill);
    }
}
