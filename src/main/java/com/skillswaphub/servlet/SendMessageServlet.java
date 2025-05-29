package com.skillswaphub.servlet;

import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for initiating a new conversation
 */
@WebServlet("/send-message")
public class SendMessageServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SendMessageServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - redirect to messages page with user ID
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
        
        // Get user ID from request parameter
        String userIdStr = request.getParameter("userId");
        
        if (userIdStr == null || userIdStr.isEmpty()) {
            session.setAttribute("errorMessage", "User ID is required");
            response.sendRedirect(request.getContextPath() + "/messages");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            
            // Check if user exists
            User user = userDAO.getUserById(userId);
            if (user == null) {
                session.setAttribute("errorMessage", "User not found");
                response.sendRedirect(request.getContextPath() + "/messages");
                return;
            }
            
            // Redirect to messages page with user ID
            response.sendRedirect(request.getContextPath() + "/messages?userId=" + userId);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid user ID: " + userIdStr, e);
            session.setAttribute("errorMessage", "Invalid user ID");
            response.sendRedirect(request.getContextPath() + "/messages");
        }
    }
}
