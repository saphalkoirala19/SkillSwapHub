package com.skillswaphub.dao;

import com.skillswaphub.model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Skill-related database operations
 */
public class SkillDAO {
    private static final Logger LOGGER = Logger.getLogger(SkillDAO.class.getName());

    /**
     * Get all skills from the database
     * @return List of all skills
     */
    public List<Skill> getAllSkills() {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT * FROM skills ORDER BY skill_name";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Skill skill = extractSkillFromResultSet(rs);
                skills.add(skill);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all skills", e);
        }

        return skills;
    }

    /**
     * Get skills by category
     * @param category Category to filter by
     * @return List of skills in the specified category
     */
    public List<Skill> getSkillsByCategory(String category) {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT * FROM skills WHERE category = ? ORDER BY skill_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Skill skill = extractSkillFromResultSet(rs);
                    skills.add(skill);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting skills by category", e);
        }

        return skills;
    }

    /**
     * Get a skill by ID
     * @param skillId Skill ID to search for
     * @return Skill object if found, null otherwise
     */
    public Skill getSkillById(int skillId) {
        String sql = "SELECT * FROM skills WHERE skill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, skillId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractSkillFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting skill by ID", e);
        }

        return null;
    }

    /**
     * Add a skill to a user's offered skills
     * @param userId User ID
     * @param skillId Skill ID
     * @param proficiencyLevel Proficiency level
     * @param description Description
     * @return true if successful, false otherwise
     */
    public boolean addOfferedSkill(int userId, int skillId, String proficiencyLevel, String description) {
        String sql = "INSERT INTO user_offered_skills (user_id, skill_id, proficiency_level, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, skillId);
            stmt.setString(3, proficiencyLevel);
            stmt.setString(4, description);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding offered skill", e);
            return false;
        }
    }

    /**
     * Add a skill to a user's wanted skills
     * @param userId User ID
     * @param skillId Skill ID
     * @param currentLevel Current level
     * @param description Description
     * @return true if successful, false otherwise
     */
    public boolean addWantedSkill(int userId, int skillId, String currentLevel, String description) {
        String sql = "INSERT INTO user_wanted_skills (user_id, skill_id, current_level, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, skillId);
            stmt.setString(3, currentLevel);
            stmt.setString(4, description);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding wanted skill", e);
            return false;
        }
    }

    /**
     * Remove a skill from a user's offered skills
     * @param userId User ID
     * @param skillId Skill ID
     * @return true if successful, false otherwise
     */
    public boolean removeOfferedSkill(int userId, int skillId) {
        String sql = "DELETE FROM user_offered_skills WHERE user_id = ? AND skill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, skillId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing offered skill", e);
            return false;
        }
    }

    /**
     * Remove a skill from a user's wanted skills
     * @param userId User ID
     * @param skillId Skill ID
     * @return true if successful, false otherwise
     */
    public boolean removeWantedSkill(int userId, int skillId) {
        String sql = "DELETE FROM user_wanted_skills WHERE user_id = ? AND skill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, skillId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing wanted skill", e);
            return false;
        }
    }

    /**
     * Get all skill categories
     * @return List of distinct categories
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM skills ORDER BY category";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all categories", e);
        }

        return categories;
    }

    /**
     * Add a new skill to the system
     * @param skill Skill object to add
     * @return true if successful, false otherwise
     */
    public boolean addSkill(Skill skill) {
        String sql = "INSERT INTO skills (skill_name, category, image_url) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, skill.getSkillName());
            stmt.setString(2, skill.getCategory());
            stmt.setString(3, skill.getImageUrl());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    skill.setSkillId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding new skill", e);
            return false;
        }
    }

    /**
     * Search skills by name or category
     * @param searchTerm Term to search for
     * @return List of matching skills
     */
    public List<Skill> searchSkills(String searchTerm) {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT s.*, " +
                    "(SELECT COUNT(*) FROM user_offered_skills WHERE skill_id = s.skill_id) AS offer_count, " +
                    "(SELECT COUNT(*) FROM user_wanted_skills WHERE skill_id = s.skill_id) AS want_count " +
                    "FROM skills s " +
                    "WHERE s.skill_name LIKE ? OR s.category LIKE ? " +
                    "ORDER BY s.skill_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Skill skill = extractSkillFromResultSet(rs);
                    skill.setOfferCount(rs.getInt("offer_count"));
                    skill.setWantCount(rs.getInt("want_count"));
                    skills.add(skill);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching skills", e);
        }

        return skills;
    }

    /**
     * Get popular skills (skills with the most offers or wants)
     * @param limit Maximum number of skills to return
     * @return List of popular skills
     */
    public List<Skill> getPopularSkills(int limit) {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT s.*, " +
                    "(SELECT COUNT(*) FROM user_offered_skills WHERE skill_id = s.skill_id) AS offer_count, " +
                    "(SELECT COUNT(*) FROM user_wanted_skills WHERE skill_id = s.skill_id) AS want_count " +
                    "FROM skills s " +
                    "ORDER BY (offer_count + want_count) DESC " +
                    "LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Skill skill = extractSkillFromResultSet(rs);
                    skill.setOfferCount(rs.getInt("offer_count"));
                    skill.setWantCount(rs.getInt("want_count"));
                    skill.setPopular(true);
                    skills.add(skill);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting popular skills", e);
        }

        return skills;
    }

    /**
     * Get skills with pagination
     * @param page Page number (1-based)
     * @param pageSize Number of skills per page
     * @return List of skills for the specified page
     */
    public List<Skill> getSkillsWithPagination(int page, int pageSize) {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT s.*, " +
                    "(SELECT COUNT(*) FROM user_offered_skills WHERE skill_id = s.skill_id) AS offer_count, " +
                    "(SELECT COUNT(*) FROM user_wanted_skills WHERE skill_id = s.skill_id) AS want_count " +
                    "FROM skills s " +
                    "ORDER BY s.skill_name " +
                    "LIMIT ? OFFSET ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int offset = (page - 1) * pageSize;
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Skill skill = extractSkillFromResultSet(rs);
                    skill.setOfferCount(rs.getInt("offer_count"));
                    skill.setWantCount(rs.getInt("want_count"));
                    skills.add(skill);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting skills with pagination", e);
        }

        return skills;
    }

    /**
     * Get total number of skills
     * @return Total number of skills
     */
    public int getTotalSkillCount() {
        String sql = "SELECT COUNT(*) FROM skills";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total skill count", e);
        }

        return 0;
    }

    /**
     * Get users who offer a specific skill
     * @param skillId Skill ID
     * @return List of user IDs who offer the skill
     */
    public List<Integer> getUsersOfferingSkill(int skillId) {
        List<Integer> userIds = new ArrayList<>();
        String sql = "SELECT user_id FROM user_offered_skills WHERE skill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, skillId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    userIds.add(rs.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users offering skill", e);
        }

        return userIds;
    }

    /**
     * Get users who want to learn a specific skill
     * @param skillId Skill ID
     * @return List of user IDs who want to learn the skill
     */
    public List<Integer> getUsersWantingSkill(int skillId) {
        List<Integer> userIds = new ArrayList<>();
        String sql = "SELECT user_id FROM user_wanted_skills WHERE skill_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, skillId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    userIds.add(rs.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting users wanting skill", e);
        }

        return userIds;
    }

    // Helper methods

    /**
     * Extract skill data from a ResultSet
     * @param rs ResultSet containing skill data
     * @return Skill object
     * @throws SQLException if a database access error occurs
     */
    private Skill extractSkillFromResultSet(ResultSet rs) throws SQLException {
        Skill skill = new Skill();
        skill.setSkillId(rs.getInt("skill_id"));
        skill.setSkillName(rs.getString("skill_name"));
        skill.setCategory(rs.getString("category"));
        skill.setCreatedAt(rs.getTimestamp("created_at"));

        // Try to get image URL if it exists in the result set
        try {
            skill.setImageUrl(rs.getString("image_url"));
        } catch (SQLException e) {
            // Column doesn't exist, ignore
        }

        return skill;
    }
}
