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
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet for handling user login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Redirect to profile page if already logged in
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process login form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get form parameters
        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");
        
        // Validate input
        boolean hasErrors = false;
        
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            request.setAttribute("usernameOrEmailError", "Username or email is required");
            hasErrors = true;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("passwordError", "Password is required");
            hasErrors = true;
        }
        
        // If there are validation errors, redisplay the form with error messages
        if (hasErrors) {
            // Preserve entered username/email
            request.setAttribute("usernameOrEmail", usernameOrEmail);
            
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        // Try to find user by username or email
        User user = null;
        
        // Check if input is an email (contains @)
        if (usernameOrEmail.contains("@")) {
            user = userDAO.getUserByEmail(usernameOrEmail);
        } else {
            user = userDAO.getUserByUsername(usernameOrEmail);
        }
        
        // Verify user exists and password is correct
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            // Create session and store user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            
            // Redirect to profile page
            response.sendRedirect(request.getContextPath() + "/profile");
        } else {
            // Set error message
            request.setAttribute("errorMessage", "Invalid username/email or password");
            
            // Preserve entered username/email
            request.setAttribute("usernameOrEmail", usernameOrEmail);
            
            // Redisplay the form
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
