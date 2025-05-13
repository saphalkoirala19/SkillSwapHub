package com.skillswaphub.servlet;

import com.skillswaphub.dao.SkillDAO;
import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.model.Skill;
import com.skillswaphub.model.User;
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
 * Servlet for handling user profile management
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ProfileServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        skillDAO = new SkillDAO();
    }
    
    /**
     * Handle GET requests - display profile page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get user data with skills
        User user = userDAO.getUserById(userId);
        
        if (user == null) {
            // This should not happen if the filter is working correctly
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login?error=invalid_session");
            return;
        }
        
        // Get all available skills for adding to profile
        List<Skill> allSkills = skillDAO.getAllSkills();
        List<String> categories = skillDAO.getAllCategories();
        
        // Set attributes for the JSP
        request.setAttribute("user", user);
        request.setAttribute("allSkills", allSkills);
        request.setAttribute("categories", categories);
        
        // Check if we're in edit mode
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/editProfile.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        }
    }
    
    /**
     * Handle POST requests - process profile updates
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get the action parameter
        String action = request.getParameter("action");
        
        if ("updateProfile".equals(action)) {
            // Handle profile information update
            updateProfileInfo(request, response, userId);
        } else if ("addOfferedSkill".equals(action)) {
            // Handle adding an offered skill
            addOfferedSkill(request, response, userId);
        } else if ("addWantedSkill".equals(action)) {
            // Handle adding a wanted skill
            addWantedSkill(request, response, userId);
        } else if ("removeOfferedSkill".equals(action)) {
            // Handle removing an offered skill
            removeOfferedSkill(request, response, userId);
        } else if ("removeWantedSkill".equals(action)) {
            // Handle removing a wanted skill
            removeWantedSkill(request, response, userId);
        } else {
            // Invalid action
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
    
    /**
     * Update user profile information
     */
    private void updateProfileInfo(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        // Get current user data
        User user = userDAO.getUserById(userId);
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get form parameters
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String bio = request.getParameter("bio");
        
        // Validate input
        boolean hasErrors = false;
        
        if (firstName == null || firstName.trim().isEmpty()) {
            request.setAttribute("firstNameError", "First name is required");
            hasErrors = true;
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            request.setAttribute("lastNameError", "Last name is required");
            hasErrors = true;
        }
        
        // If there are validation errors, redisplay the form with error messages
        if (hasErrors) {
            // Preserve entered values
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("bio", bio);
            
            request.setAttribute("user", user);
            request.setAttribute("action", "edit");
            request.getRequestDispatcher("/WEB-INF/views/editProfile.jsp").forward(request, response);
            return;
        }
        
        // Update user object
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBio(bio);
        
        // Save to database
        boolean success = userDAO.updateUser(user);
        
        if (success) {
            // Update session with new user data
            request.getSession().setAttribute("user", user);
            
            // Set success message
            request.getSession().setAttribute("message", "Profile updated successfully");
        } else {
            // Set error message
            request.getSession().setAttribute("errorMessage", "Failed to update profile");
        }
        
        // Redirect back to profile page
        response.sendRedirect(request.getContextPath() + "/profile");
    }
    
    /**
     * Add a skill to user's offered skills
     */
    private void addOfferedSkill(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        // Get form parameters
        String skillIdStr = request.getParameter("skillId");
        String proficiencyLevel = request.getParameter("proficiencyLevel");
        String description = request.getParameter("description");
        
        // Validate input
        if (skillIdStr == null || proficiencyLevel == null) {
            request.getSession().setAttribute("errorMessage", "Invalid skill information");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        try {
            int skillId = Integer.parseInt(skillIdStr);
            
            // Sanitize description
            if (description != null) {
                description = ValidationUtil.sanitizeInput(description);
            }
            
            // Add skill to user's offered skills
            boolean success = skillDAO.addOfferedSkill(userId, skillId, proficiencyLevel, description);
            
            if (success) {
                request.getSession().setAttribute("message", "Skill added to your offered skills");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to add skill");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid skill ID");
        }
        
        // Redirect back to profile page
        response.sendRedirect(request.getContextPath() + "/profile");
    }
    
    /**
     * Add a skill to user's wanted skills
     */
    private void addWantedSkill(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        // Get form parameters
        String skillIdStr = request.getParameter("skillId");
        String currentLevel = request.getParameter("currentLevel");
        String description = request.getParameter("description");
        
        // Validate input
        if (skillIdStr == null || currentLevel == null) {
            request.getSession().setAttribute("errorMessage", "Invalid skill information");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        try {
            int skillId = Integer.parseInt(skillIdStr);
            
            // Sanitize description
            if (description != null) {
                description = ValidationUtil.sanitizeInput(description);
            }
            
            // Add skill to user's wanted skills
            boolean success = skillDAO.addWantedSkill(userId, skillId, currentLevel, description);
            
            if (success) {
                request.getSession().setAttribute("message", "Skill added to your wanted skills");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to add skill");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid skill ID");
        }
        
        // Redirect back to profile page
        response.sendRedirect(request.getContextPath() + "/profile");
    }
    
    /**
     * Remove a skill from user's offered skills
     */
    private void removeOfferedSkill(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        // Get skill ID
        String skillIdStr = request.getParameter("skillId");
        
        if (skillIdStr == null) {
            request.getSession().setAttribute("errorMessage", "Invalid skill ID");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        try {
            int skillId = Integer.parseInt(skillIdStr);
            
            // Remove skill from user's offered skills
            boolean success = skillDAO.removeOfferedSkill(userId, skillId);
            
            if (success) {
                request.getSession().setAttribute("message", "Skill removed from your offered skills");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to remove skill");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid skill ID");
        }
        
        // Redirect back to profile page
        response.sendRedirect(request.getContextPath() + "/profile");
    }
    
    /**
     * Remove a skill from user's wanted skills
     */
    private void removeWantedSkill(HttpServletRequest request, HttpServletResponse response, int userId) 
            throws ServletException, IOException {
        // Get skill ID
        String skillIdStr = request.getParameter("skillId");
        
        if (skillIdStr == null) {
            request.getSession().setAttribute("errorMessage", "Invalid skill ID");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        try {
            int skillId = Integer.parseInt(skillIdStr);
            
            // Remove skill from user's wanted skills
            boolean success = skillDAO.removeWantedSkill(userId, skillId);
            
            if (success) {
                request.getSession().setAttribute("message", "Skill removed from your wanted skills");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to remove skill");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid skill ID");
        }
        
        // Redirect back to profile page
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
