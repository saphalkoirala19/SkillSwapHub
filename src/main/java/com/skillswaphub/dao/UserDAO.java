package com.skillswaphub.dao;

import com.skillswaphub.model.User;
import com.skillswaphub.model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for User-related database operations
 */
public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    
    /**
     * Create a new user in the database
     * @param user User object to create
     * @return true if successful, false otherwise
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, email, password, first_name, last_name, bio) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getBio());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
            return false;
        }
    }
    
    /**
     * Get a user by username
     * @param username Username to search for
     * @return User object if found, null otherwise
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by username", e);
        }
        
        return null;
    }
    
    /**
     * Get a user by email
     * @param email Email to search for
     * @return User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email", e);
        }
        
        return null;
    }
    
    /**
     * Get a user by ID
     * @param userId User ID to search for
     * @return User object if found, null otherwise
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    
                    // Load user's offered skills
                    loadUserOfferedSkills(user, conn);
                    
                    // Load user's wanted skills
                    loadUserWantedSkills(user, conn);
                    
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID", e);
        }
        
        return null;
    }
    
    /**
     * Update user profile information
     * @param user User object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, first_name = ?, last_name = ?, bio = ?, profile_image = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getBio());
            stmt.setString(6, user.getProfileImage());
            stmt.setInt(7, user.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
            return false;
        }
    }
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPassword New password (should be already hashed)
     * @return true if successful, false otherwise
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating password", e);
            return false;
        }
    }
    
    /**
     * Check if username already exists
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if username exists", e);
        }
        
        return false;
    }
    
    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if email exists", e);
        }
        
        return false;
    }
    
    // Helper methods
    
    /**
     * Extract user data from a ResultSet
     * @param rs ResultSet containing user data
     * @return User object
     * @throws SQLException if a database access error occurs
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setBio(rs.getString("bio"));
        user.setProfileImage(rs.getString("profile_image"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
    
    /**
     * Load offered skills for a user
     * @param user User to load skills for
     * @param conn Database connection
     * @throws SQLException if a database access error occurs
     */
    private void loadUserOfferedSkills(User user, Connection conn) throws SQLException {
        String sql = "SELECT s.skill_id, s.skill_name, s.category, uos.proficiency_level, uos.description " +
                     "FROM user_offered_skills uos " +
                     "JOIN skills s ON uos.skill_id = s.skill_id " +
                     "WHERE uos.user_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Skill skill = new Skill(
                        rs.getInt("skill_id"),
                        rs.getString("skill_name"),
                        rs.getString("category"),
                        rs.getString("proficiency_level"),
                        rs.getString("description")
                    );
                    user.addOfferedSkill(skill);
                }
            }
        }
    }
    
    /**
     * Load wanted skills for a user
     * @param user User to load skills for
     * @param conn Database connection
     * @throws SQLException if a database access error occurs
     */
    private void loadUserWantedSkills(User user, Connection conn) throws SQLException {
        String sql = "SELECT s.skill_id, s.skill_name, s.category, uws.current_level, uws.description " +
                     "FROM user_wanted_skills uws " +
                     "JOIN skills s ON uws.skill_id = s.skill_id " +
                     "WHERE uws.user_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Skill skill = new Skill(
                        rs.getInt("skill_id"),
                        rs.getString("skill_name"),
                        rs.getString("category"),
                        rs.getString("current_level"),
                        rs.getString("description"),
                        true
                    );
                    user.addWantedSkill(skill);
                }
            }
        }
    }
}
