package com.skillswaphub.servlet;

import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.dao.RatingDAO;
import com.skillswaphub.dao.SessionDAO;
import com.skillswaphub.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.skillswaphub.dao.DBConnection;

/**
 * Servlet for displaying the leaderboard
 */
@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LeaderboardServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    private RatingDAO ratingDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        ratingDAO = new RatingDAO();
    }
    
    /**
     * Handle GET requests - display leaderboard page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get top users by rating
        List<User> topRatedUsers = getTopRatedUsers(10);
        request.setAttribute("topRatedUsers", topRatedUsers);
        
        // Get top users by number of skills offered
        List<User> topSkillProviders = getTopSkillProviders(10);
        request.setAttribute("topSkillProviders", topSkillProviders);
        
        // Get top users by number of sessions completed
        List<User> mostActiveUsers = getMostActiveUsers(10);
        request.setAttribute("mostActiveUsers", mostActiveUsers);
        
        // Forward to leaderboard page
        request.getRequestDispatcher("/WEB-INF/views/leaderboard.jsp").forward(request, response);
    }
    
    /**
     * Get top users by average rating
     * @param limit Number of users to return
     * @return List of top rated users
     */
    private List<User> getTopRatedUsers(int limit) {
        List<User> topUsers = new ArrayList<>();
        Map<Integer, Double> userRatings = new HashMap<>();
        
        String sql = "SELECT rated_id, AVG(rating_value) as avg_rating, COUNT(*) as rating_count " +
                     "FROM ratings " +
                     "GROUP BY rated_id " +
                     "HAVING COUNT(*) >= 3 " +  // Require at least 3 ratings
                     "ORDER BY avg_rating DESC, rating_count DESC " +
                     "LIMIT ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("rated_id");
                    double avgRating = rs.getDouble("avg_rating");
                    
                    User user = userDAO.getUserById(userId);
                    if (user != null) {
                        user.setAverageRating(avgRating);
                        topUsers.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting top rated users", e);
        }
        
        return topUsers;
    }
    
    /**
     * Get top users by number of skills offered
     * @param limit Number of users to return
     * @return List of top skill providers
     */
    private List<User> getTopSkillProviders(int limit) {
        List<User> topUsers = new ArrayList<>();
        
        String sql = "SELECT user_id, COUNT(*) as skill_count " +
                     "FROM user_offered_skills " +
                     "GROUP BY user_id " +
                     "ORDER BY skill_count DESC " +
                     "LIMIT ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    
                    User user = userDAO.getUserById(userId);
                    if (user != null) {
                        topUsers.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting top skill providers", e);
        }
        
        return topUsers;
    }
    
    /**
     * Get top users by number of sessions completed
     * @param limit Number of users to return
     * @return List of most active users
     */
    private List<User> getMostActiveUsers(int limit) {
        List<User> topUsers = new ArrayList<>();
        
        String sql = "SELECT user_id, session_count FROM (" +
                     "  SELECT teacher_id as user_id, COUNT(*) as session_count " +
                     "  FROM sessions " +
                     "  WHERE status = 'Completed' " +
                     "  GROUP BY teacher_id " +
                     "  UNION ALL " +
                     "  SELECT student_id as user_id, COUNT(*) as session_count " +
                     "  FROM sessions " +
                     "  WHERE status = 'Completed' " +
                     "  GROUP BY student_id" +
                     ") as combined " +
                     "GROUP BY user_id " +
                     "ORDER BY SUM(session_count) DESC " +
                     "LIMIT ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    
                    User user = userDAO.getUserById(userId);
                    if (user != null) {
                        topUsers.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting most active users", e);
        }
        
        return topUsers;
    }
}
