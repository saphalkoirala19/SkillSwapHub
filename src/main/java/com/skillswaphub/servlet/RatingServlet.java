package com.skillswaphub.servlet;

import com.skillswaphub.dao.RatingDAO;
import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.dao.SkillDAO;
import com.skillswaphub.model.Rating;
import com.skillswaphub.model.User;
import com.skillswaphub.model.Skill;
import com.skillswaphub.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling ratings
 */
@WebServlet("/ratings")
public class RatingServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RatingServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private RatingDAO ratingDAO;
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() {
        ratingDAO = new RatingDAO();
        userDAO = new UserDAO();
        skillDAO = new SkillDAO();
    }
    
    /**
     * Handle GET requests - display ratings page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?unauthorized=true");
            return;
        }
        
        // Get current user ID
        Integer currentUserId = (Integer) session.getAttribute("userId");
        
        // Get user ID from request parameter
        String userIdStr = request.getParameter("userId");
        Integer userId = null;
        
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                userId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid user ID: " + userIdStr, e);
                session.setAttribute("errorMessage", "Invalid user ID");
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }
        } else {
            userId = currentUserId;
        }
        
        // Get user
        User user = userDAO.getUserById(userId);
        if (user == null) {
            session.setAttribute("errorMessage", "User not found");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        // Get ratings for user
        List<Rating> ratingsForUser = ratingDAO.getRatingsForUser(userId);
        request.setAttribute("ratingsForUser", ratingsForUser);
        
        // Get ratings by user
        List<Rating> ratingsByUser = ratingDAO.getRatingsByUser(userId);
        request.setAttribute("ratingsByUser", ratingsByUser);
        
        // Get average rating
        double averageRating = ratingDAO.getAverageRatingForUser(userId);
        request.setAttribute("averageRating", averageRating);
        
        // Set user attribute
        request.setAttribute("user", user);
        
        // Set current user attribute
        request.setAttribute("currentUser", userDAO.getUserById(currentUserId));
        
        // Forward to ratings page
        request.getRequestDispatcher("/WEB-INF/views/ratings.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - create or update a rating
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?unauthorized=true");
            return;
        }
        
        // Get current user ID
        Integer raterId = (Integer) session.getAttribute("userId");
        
        // Get form parameters
        String action = request.getParameter("action");
        String ratedIdStr = request.getParameter("ratedId");
        String skillIdStr = request.getParameter("skillId");
        String ratingValueStr = request.getParameter("ratingValue");
        String feedback = request.getParameter("feedback");
        
        // Validate input
        if (ratedIdStr == null || skillIdStr == null || ratingValueStr == null) {
            session.setAttribute("errorMessage", "Missing required parameters");
            response.sendRedirect(request.getContextPath() + "/ratings");
            return;
        }
        
        try {
            int ratedId = Integer.parseInt(ratedIdStr);
            int skillId = Integer.parseInt(skillIdStr);
            int ratingValue = Integer.parseInt(ratingValueStr);
            
            // Validate rating value
            if (ratingValue < 1 || ratingValue > 5) {
                session.setAttribute("errorMessage", "Rating value must be between 1 and 5");
                response.sendRedirect(request.getContextPath() + "/ratings?userId=" + ratedId);
                return;
            }
            
            // Check if user is trying to rate themselves
            if (raterId == ratedId) {
                session.setAttribute("errorMessage", "You cannot rate yourself");
                response.sendRedirect(request.getContextPath() + "/ratings");
                return;
            }
            
            // Sanitize feedback
            if (feedback != null) {
                feedback = ValidationUtil.sanitizeInput(feedback);
            }
            
            // Create or update rating
            if ("create".equals(action)) {
                // Check if rating already exists
                if (ratingDAO.hasRated(raterId, ratedId, skillId)) {
                    session.setAttribute("errorMessage", "You have already rated this user for this skill");
                    response.sendRedirect(request.getContextPath() + "/ratings?userId=" + ratedId);
                    return;
                }
                
                // Create rating
                Rating rating = new Rating(raterId, ratedId, skillId, ratingValue, feedback);
                boolean success = ratingDAO.createRating(rating);
                
                if (success) {
                    session.setAttribute("message", "Rating submitted successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to submit rating");
                }
            } else if ("update".equals(action)) {
                String ratingIdStr = request.getParameter("ratingId");
                if (ratingIdStr == null) {
                    session.setAttribute("errorMessage", "Missing rating ID");
                    response.sendRedirect(request.getContextPath() + "/ratings?userId=" + ratedId);
                    return;
                }
                
                int ratingId = Integer.parseInt(ratingIdStr);
                
                // Get existing rating
                Rating rating = ratingDAO.getRatingById(ratingId);
                
                if (rating == null) {
                    session.setAttribute("errorMessage", "Rating not found");
                    response.sendRedirect(request.getContextPath() + "/ratings?userId=" + ratedId);
                    return;
                }
                
                // Check if user is the rater
                if (rating.getRaterId() != raterId) {
                    session.setAttribute("errorMessage", "You can only update your own ratings");
                    response.sendRedirect(request.getContextPath() + "/ratings?userId=" + ratedId);
                    return;
                }
                
                // Update rating
                rating.setRatingValue(ratingValue);
                rating.setFeedback(feedback);
                boolean success = ratingDAO.updateRating(rating);
                
                if (success) {
                    session.setAttribute("message", "Rating updated successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to update rating");
                }
            }
            
            // Redirect to ratings page
            response.sendRedirect(request.getContextPath() + "/ratings?userId=" + ratedId);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid parameter", e);
            session.setAttribute("errorMessage", "Invalid parameter");
            response.sendRedirect(request.getContextPath() + "/ratings");
        }
    }
}
