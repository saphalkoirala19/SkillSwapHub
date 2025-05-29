package com.skillswaphub.dao;

import com.skillswaphub.model.Rating;
import com.skillswaphub.model.User;
import com.skillswaphub.model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Rating-related database operations
 */
public class RatingDAO {
    private static final Logger LOGGER = Logger.getLogger(RatingDAO.class.getName());
    
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    
    public RatingDAO() {
        this.userDAO = new UserDAO();
        this.skillDAO = new SkillDAO();
    }
    
    /**
     * Create a new rating
     * @param rating Rating object to create
     * @return true if successful, false otherwise
     */
    public boolean createRating(Rating rating) {
        String sql = "INSERT INTO ratings (rater_id, rated_id, skill_id, rating_value, feedback) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, rating.getRaterId());
            stmt.setInt(2, rating.getRatedId());
            stmt.setInt(3, rating.getSkillId());
            stmt.setInt(4, rating.getRatingValue());
            stmt.setString(5, rating.getFeedback());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rating.setRatingId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating rating", e);
            return false;
        }
    }
    
    /**
     * Update an existing rating
     * @param rating Rating object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateRating(Rating rating) {
        String sql = "UPDATE ratings SET rating_value = ?, feedback = ? WHERE rating_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, rating.getRatingValue());
            stmt.setString(2, rating.getFeedback());
            stmt.setInt(3, rating.getRatingId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating rating", e);
            return false;
        }
    }
    
    /**
     * Delete a rating
     * @param ratingId Rating ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteRating(int ratingId) {
        String sql = "DELETE FROM ratings WHERE rating_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ratingId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting rating", e);
            return false;
        }
    }
    
    /**
     * Get a rating by ID
     * @param ratingId Rating ID to search for
     * @return Rating object if found, null otherwise
     */
    public Rating getRatingById(int ratingId) {
        String sql = "SELECT * FROM ratings WHERE rating_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ratingId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rating rating = extractRatingFromResultSet(rs);
                    
                    // Load related entities
                    loadRelatedEntities(rating);
                    
                    return rating;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting rating by ID", e);
        }
        
        return null;
    }
    
    /**
     * Get all ratings for a user (as the rated user)
     * @param userId User ID to get ratings for
     * @return List of ratings for the user
     */
    public List<Rating> getRatingsForUser(int userId) {
        String sql = "SELECT * FROM ratings WHERE rated_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Rating> ratings = new ArrayList<>();
                
                while (rs.next()) {
                    Rating rating = extractRatingFromResultSet(rs);
                    loadRelatedEntities(rating);
                    ratings.add(rating);
                }
                
                return ratings;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting ratings for user", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get all ratings by a user (as the rater)
     * @param userId User ID who gave the ratings
     * @return List of ratings by the user
     */
    public List<Rating> getRatingsByUser(int userId) {
        String sql = "SELECT * FROM ratings WHERE rater_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Rating> ratings = new ArrayList<>();
                
                while (rs.next()) {
                    Rating rating = extractRatingFromResultSet(rs);
                    loadRelatedEntities(rating);
                    ratings.add(rating);
                }
                
                return ratings;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting ratings by user", e);
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Get the average rating for a user
     * @param userId User ID to get average rating for
     * @return Average rating value, or 0 if no ratings
     */
    public double getAverageRatingForUser(int userId) {
        String sql = "SELECT AVG(rating_value) FROM ratings WHERE rated_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting average rating for user", e);
        }
        
        return 0;
    }
    
    /**
     * Check if a user has already rated another user for a specific skill
     * @param raterId Rater user ID
     * @param ratedId Rated user ID
     * @param skillId Skill ID
     * @return true if rating exists, false otherwise
     */
    public boolean hasRated(int raterId, int ratedId, int skillId) {
        String sql = "SELECT COUNT(*) FROM ratings WHERE rater_id = ? AND rated_id = ? AND skill_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, raterId);
            stmt.setInt(2, ratedId);
            stmt.setInt(3, skillId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user has rated", e);
        }
        
        return false;
    }
    
    // Helper methods
    
    /**
     * Extract rating data from a ResultSet
     * @param rs ResultSet containing rating data
     * @return Rating object
     * @throws SQLException if a database access error occurs
     */
    private Rating extractRatingFromResultSet(ResultSet rs) throws SQLException {
        Rating rating = new Rating();
        rating.setRatingId(rs.getInt("rating_id"));
        rating.setRaterId(rs.getInt("rater_id"));
        rating.setRatedId(rs.getInt("rated_id"));
        rating.setSkillId(rs.getInt("skill_id"));
        rating.setRatingValue(rs.getInt("rating_value"));
        rating.setFeedback(rs.getString("feedback"));
        rating.setCreatedAt(rs.getTimestamp("created_at"));
        return rating;
    }
    
    /**
     * Load related entities (users and skill) for a rating
     * @param rating Rating to load entities for
     */
    private void loadRelatedEntities(Rating rating) {
        // Load rater
        User rater = userDAO.getUserById(rating.getRaterId());
        rating.setRater(rater);
        
        // Load rated user
        User rated = userDAO.getUserById(rating.getRatedId());
        rating.setRated(rated);
        
        // Load skill
        Skill skill = skillDAO.getSkillById(rating.getSkillId());
        rating.setSkill(skill);
    }
}
