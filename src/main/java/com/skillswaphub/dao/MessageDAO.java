package com.skillswaphub.dao;

import com.skillswaphub.model.Message;
import com.skillswaphub.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Message-related database operations
 */
public class MessageDAO {
    private static final Logger LOGGER = Logger.getLogger(MessageDAO.class.getName());
    
    private UserDAO userDAO;
    
    public MessageDAO() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Send a new message
     * @param message Message object to send
     * @return true if successful, false otherwise
     */
    public boolean sendMessage(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, message.getSenderId());
            stmt.setInt(2, message.getReceiverId());
            stmt.setString(3, message.getContent());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setMessageId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error sending message", e);
            return false;
        }
    }
    
    /**
     * Get a message by ID
     * @param messageId Message ID to search for
     * @return Message object if found, null otherwise
     */
    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM messages WHERE message_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, messageId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Message message = extractMessageFromResultSet(rs);
                    
                    // Load related users
                    loadRelatedUsers(message);
                    
                    return message;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting message by ID", e);
        }
        
        return null;
    }
    
    /**
     * Get conversation between two users
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @return List of messages between the two users
     */
    public List<Message> getConversation(int userId1, int userId2) {
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
                     "ORDER BY created_at ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId1);
            stmt.setInt(2, userId2);
            stmt.setInt(3, userId2);
            stmt.setInt(4, userId1);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Message> messages = new ArrayList<>();
                
                while (rs.next()) {
                    Message message = extractMessageFromResultSet(rs);
                    loadRelatedUsers(message);
                    messages.add(message);
                }
                
                return messages;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting conversation", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get all conversations for a user
     * @param userId User ID
     * @return Map of user IDs to the most recent message with that user
     */
    public Map<Integer, Message> getConversations(int userId) {
        String sql = "SELECT m.* FROM messages m " +
                     "INNER JOIN ( " +
                     "    SELECT " +
                     "        CASE " +
                     "            WHEN sender_id = ? THEN receiver_id " +
                     "            ELSE sender_id " +
                     "        END AS other_user_id, " +
                     "        MAX(created_at) AS latest_message_time " +
                     "    FROM messages " +
                     "    WHERE sender_id = ? OR receiver_id = ? " +
                     "    GROUP BY other_user_id " +
                     ") latest ON " +
                     "    ((m.sender_id = ? AND m.receiver_id = latest.other_user_id) OR " +
                     "     (m.sender_id = latest.other_user_id AND m.receiver_id = ?)) " +
                     "    AND m.created_at = latest.latest_message_time " +
                     "ORDER BY m.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            stmt.setInt(4, userId);
            stmt.setInt(5, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Message> conversations = new HashMap<>();
                
                while (rs.next()) {
                    Message message = extractMessageFromResultSet(rs);
                    loadRelatedUsers(message);
                    
                    int otherUserId = message.getSenderId() == userId ? message.getReceiverId() : message.getSenderId();
                    conversations.put(otherUserId, message);
                }
                
                return conversations;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting conversations", e);
        }
        
        return new HashMap<>();
    }
    
    /**
     * Mark messages as read
     * @param senderId Sender user ID
     * @param receiverId Receiver user ID
     * @return Number of messages marked as read
     */
    public int markMessagesAsRead(int senderId, int receiverId) {
        String sql = "UPDATE messages SET is_read = TRUE WHERE sender_id = ? AND receiver_id = ? AND is_read = FALSE";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            
            return stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error marking messages as read", e);
            return 0;
        }
    }
    
    /**
     * Get unread message count for a user
     * @param userId User ID
     * @return Count of unread messages
     */
    public int getUnreadMessageCount(int userId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_read = FALSE";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting unread message count", e);
        }
        
        return 0;
    }
    
    /**
     * Get unread message count from a specific user
     * @param receiverId Receiver user ID
     * @param senderId Sender user ID
     * @return Count of unread messages
     */
    public int getUnreadMessageCountFromUser(int receiverId, int senderId) {
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND sender_id = ? AND is_read = FALSE";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, receiverId);
            stmt.setInt(2, senderId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting unread message count from user", e);
        }
        
        return 0;
    }
    
    // Helper methods
    
    /**
     * Extract message data from a ResultSet
     * @param rs ResultSet containing message data
     * @return Message object
     * @throws SQLException if a database access error occurs
     */
    private Message extractMessageFromResultSet(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setMessageId(rs.getInt("message_id"));
        message.setSenderId(rs.getInt("sender_id"));
        message.setReceiverId(rs.getInt("receiver_id"));
        message.setContent(rs.getString("content"));
        message.setRead(rs.getBoolean("is_read"));
        message.setCreatedAt(rs.getTimestamp("created_at"));
        return message;
    }
    
    /**
     * Load related users for a message
     * @param message Message to load users for
     */
    private void loadRelatedUsers(Message message) {
        // Load sender
        User sender = userDAO.getUserById(message.getSenderId());
        message.setSender(sender);
        
        // Load receiver
        User receiver = userDAO.getUserById(message.getReceiverId());
        message.setReceiver(receiver);
    }
}
