package com.skillswaphub.servlet;

import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.dao.SkillDAO;
import com.skillswaphub.dao.SkillRequestDAO;
import com.skillswaphub.dao.SessionDAO;
import com.skillswaphub.model.User;
import com.skillswaphub.model.Skill;
import com.skillswaphub.model.SkillRequest;
import com.skillswaphub.model.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.skillswaphub.dao.DBConnection;

/**
 * Servlet for admin panel
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    private SkillRequestDAO requestDAO;
    private SessionDAO sessionDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        skillDAO = new SkillDAO();
        requestDAO = new SkillRequestDAO();
        sessionDAO = new SessionDAO();
    }
    
    /**
     * Handle GET requests - display admin panel
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
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Check if user is admin
        User user = userDAO.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // Get section from request parameter
        String section = request.getParameter("section");
        if (section == null) {
            section = "dashboard";
        }
        
        // Handle different sections
        switch (section) {
            case "users":
                handleUsersSection(request, response);
                break;
            case "skills":
                handleSkillsSection(request, response);
                break;
            case "requests":
                handleRequestsSection(request, response);
                break;
            case "sessions":
                handleSessionsSection(request, response);
                break;
            default:
                handleDashboardSection(request, response);
                break;
        }
    }
    
    /**
     * Handle POST requests - process admin actions
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
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Check if user is admin
        User user = userDAO.getUserById(userId);
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // Get action from request parameter
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }
        
        // Handle different actions
        switch (action) {
            case "deleteUser":
                handleDeleteUser(request, response);
                break;
            case "toggleAdmin":
                handleToggleAdmin(request, response);
                break;
            case "deleteSkill":
                handleDeleteSkill(request, response);
                break;
            case "deleteRequest":
                handleDeleteRequest(request, response);
                break;
            case "deleteSession":
                handleDeleteSession(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin");
                break;
        }
    }
    
    /**
     * Handle dashboard section
     */
    private void handleDashboardSection(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get statistics
        int userCount = getUserCount();
        int skillCount = getSkillCount();
        int requestCount = getRequestCount();
        int sessionCount = getSessionCount();
        
        // Set attributes
        request.setAttribute("userCount", userCount);
        request.setAttribute("skillCount", skillCount);
        request.setAttribute("requestCount", requestCount);
        request.setAttribute("sessionCount", sessionCount);
        request.setAttribute("section", "dashboard");
        
        // Forward to admin page
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
    
    /**
     * Handle users section
     */
    private void handleUsersSection(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get all users
        List<User> users = getAllUsers();
        
        // Set attributes
        request.setAttribute("users", users);
        request.setAttribute("section", "users");
        
        // Forward to admin page
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }
    
    /**
     * Handle skills section
     */
    private void handleSkillsSection(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get all skills
        List<Skill> skills = skillDAO.getAllSkills();
        
        // Set attributes
        request.setAttribute("skills", skills);
        request.setAttribute("section", "skills");
        
        // Forward to admin page
        request.getRequestDispatcher("/WEB-INF/views/admin/skills.jsp").forward(request, response);
    }
    
    /**
     * Handle requests section
     */
    private void handleRequestsSection(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get all requests
        List<SkillRequest> requests = getAllRequests();
        
        // Set attributes
        request.setAttribute("requests", requests);
        request.setAttribute("section", "requests");
        
        // Forward to admin page
        request.getRequestDispatcher("/WEB-INF/views/admin/requests.jsp").forward(request, response);
    }
    
    /**
     * Handle sessions section
     */
    private void handleSessionsSection(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get all sessions
        List<Session> sessions = getAllSessions();
        
        // Set attributes
        request.setAttribute("sessions", sessions);
        request.setAttribute("section", "sessions");
        
        // Forward to admin page
        request.getRequestDispatcher("/WEB-INF/views/admin/sessions.jsp").forward(request, response);
    }
    
    /**
     * Handle delete user action
     */
    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get user ID from request parameter
        String userIdStr = request.getParameter("userId");
        
        if (userIdStr == null) {
            request.getSession().setAttribute("errorMessage", "Missing user ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=users");
            return;
        }
        
        try {
            int userIdToDelete = Integer.parseInt(userIdStr);
            
            // Delete user
            boolean success = deleteUser(userIdToDelete);
            
            if (success) {
                request.getSession().setAttribute("message", "User deleted successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to delete user");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?section=users");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid user ID: " + userIdStr, e);
            request.getSession().setAttribute("errorMessage", "Invalid user ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=users");
        }
    }
    
    /**
     * Handle toggle admin action
     */
    private void handleToggleAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get user ID from request parameter
        String userIdStr = request.getParameter("userId");
        
        if (userIdStr == null) {
            request.getSession().setAttribute("errorMessage", "Missing user ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=users");
            return;
        }
        
        try {
            int userIdToToggle = Integer.parseInt(userIdStr);
            
            // Toggle admin status
            boolean success = toggleAdminStatus(userIdToToggle);
            
            if (success) {
                request.getSession().setAttribute("message", "Admin status toggled successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to toggle admin status");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?section=users");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid user ID: " + userIdStr, e);
            request.getSession().setAttribute("errorMessage", "Invalid user ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=users");
        }
    }
    
    /**
     * Handle delete skill action
     */
    private void handleDeleteSkill(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get skill ID from request parameter
        String skillIdStr = request.getParameter("skillId");
        
        if (skillIdStr == null) {
            request.getSession().setAttribute("errorMessage", "Missing skill ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=skills");
            return;
        }
        
        try {
            int skillId = Integer.parseInt(skillIdStr);
            
            // Delete skill
            boolean success = deleteSkill(skillId);
            
            if (success) {
                request.getSession().setAttribute("message", "Skill deleted successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to delete skill");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?section=skills");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid skill ID: " + skillIdStr, e);
            request.getSession().setAttribute("errorMessage", "Invalid skill ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=skills");
        }
    }
    
    /**
     * Handle delete request action
     */
    private void handleDeleteRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get request ID from request parameter
        String requestIdStr = request.getParameter("requestId");
        
        if (requestIdStr == null) {
            request.getSession().setAttribute("errorMessage", "Missing request ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=requests");
            return;
        }
        
        try {
            int requestId = Integer.parseInt(requestIdStr);
            
            // Delete request
            boolean success = deleteRequest(requestId);
            
            if (success) {
                request.getSession().setAttribute("message", "Request deleted successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to delete request");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?section=requests");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid request ID: " + requestIdStr, e);
            request.getSession().setAttribute("errorMessage", "Invalid request ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=requests");
        }
    }
    
    /**
     * Handle delete session action
     */
    private void handleDeleteSession(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get session ID from request parameter
        String sessionIdStr = request.getParameter("sessionId");
        
        if (sessionIdStr == null) {
            request.getSession().setAttribute("errorMessage", "Missing session ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=sessions");
            return;
        }
        
        try {
            int sessionId = Integer.parseInt(sessionIdStr);
            
            // Delete session
            boolean success = deleteSession(sessionId);
            
            if (success) {
                request.getSession().setAttribute("message", "Session deleted successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to delete session");
            }
            
            response.sendRedirect(request.getContextPath() + "/admin?section=sessions");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid session ID: " + sessionIdStr, e);
            request.getSession().setAttribute("errorMessage", "Invalid session ID");
            response.sendRedirect(request.getContextPath() + "/admin?section=sessions");
        }
    }
    
    // Helper methods
    
    private int getUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user count", e);
        }
        
        return 0;
    }
    
    private int getSkillCount() {
        String sql = "SELECT COUNT(*) FROM skills";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting skill count", e);
        }
        
        return 0;
    }
    
    private int getRequestCount() {
        String sql = "SELECT COUNT(*) FROM skill_requests";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting request count", e);
        }
        
        return 0;
    }
    
    private int getSessionCount() {
        String sql = "SELECT COUNT(*) FROM sessions";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting session count", e);
        }
        
        return 0;
    }
    
    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLocation(rs.getString("location"));
                user.setAdmin(rs.getBoolean("is_admin"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                
                users.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
        }
        
        return users;
    }
    
    private List<SkillRequest> getAllRequests() {
        List<SkillRequest> requests = new ArrayList<>();
        
        String sql = "SELECT * FROM skill_requests ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                SkillRequest request = new SkillRequest();
                request.setRequestId(rs.getInt("request_id"));
                request.setSenderId(rs.getInt("sender_id"));
                request.setReceiverId(rs.getInt("receiver_id"));
                request.setOfferedSkillId(rs.getInt("offered_skill_id"));
                request.setWantedSkillId(rs.getInt("wanted_skill_id"));
                request.setStatus(rs.getString("status"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                
                // Load related entities
                request.setSender(userDAO.getUserById(request.getSenderId()));
                request.setReceiver(userDAO.getUserById(request.getReceiverId()));
                request.setOfferedSkill(skillDAO.getSkillById(request.getOfferedSkillId()));
                request.setWantedSkill(skillDAO.getSkillById(request.getWantedSkillId()));
                
                requests.add(request);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all requests", e);
        }
        
        return requests;
    }
    
    private List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<>();
        
        String sql = "SELECT * FROM sessions ORDER BY session_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Session session = new Session();
                session.setSessionId(rs.getInt("session_id"));
                session.setRequestId(rs.getInt("request_id"));
                session.setTeacherId(rs.getInt("teacher_id"));
                session.setStudentId(rs.getInt("student_id"));
                session.setSkillId(rs.getInt("skill_id"));
                session.setSessionDate(rs.getTimestamp("session_date").toLocalDateTime());
                session.setDuration(rs.getInt("duration"));
                session.setLocation(rs.getString("location"));
                session.setStatus(rs.getString("status"));
                session.setCreatedAt(rs.getTimestamp("created_at"));
                
                // Load related entities
                session.setTeacher(userDAO.getUserById(session.getTeacherId()));
                session.setStudent(userDAO.getUserById(session.getStudentId()));
                session.setSkill(skillDAO.getSkillById(session.getSkillId()));
                
                sessions.add(session);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all sessions", e);
        }
        
        return sessions;
    }
    
    private boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
            return false;
        }
    }
    
    private boolean toggleAdminStatus(int userId) {
        String sql = "UPDATE users SET is_admin = NOT is_admin WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error toggling admin status", e);
            return false;
        }
    }
    
    private boolean deleteSkill(int skillId) {
        String sql = "DELETE FROM skills WHERE skill_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, skillId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting skill", e);
            return false;
        }
    }
    
    private boolean deleteRequest(int requestId) {
        String sql = "DELETE FROM skill_requests WHERE request_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting request", e);
            return false;
        }
    }
    
    private boolean deleteSession(int sessionId) {
        String sql = "DELETE FROM sessions WHERE session_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sessionId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting session", e);
            return false;
        }
    }
}
