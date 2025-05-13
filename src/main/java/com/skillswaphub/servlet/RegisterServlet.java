package com.skillswaphub.servlet;

import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.model.User;
import com.skillswaphub.util.ValidationUtil;

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
 * Servlet for handling user registration
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display registration form
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
        
        // Forward to registration page
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process registration form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get form parameters
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String bio = request.getParameter("bio");
        
        // Validate input
        boolean hasErrors = false;
        
        // Username validation
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("usernameError", "Username is required");
            hasErrors = true;
        } else if (!ValidationUtil.isValidUsername(username)) {
            request.setAttribute("usernameError", "Username must be 3-20 characters and contain only letters, numbers, and underscores");
            hasErrors = true;
        } else if (userDAO.usernameExists(username)) {
            request.setAttribute("usernameError", "Username already exists");
            hasErrors = true;
        }
        
        // Email validation
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("emailError", "Email is required");
            hasErrors = true;
        } else if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("emailError", "Invalid email format");
            hasErrors = true;
        } else if (userDAO.emailExists(email)) {
            request.setAttribute("emailError", "Email already exists");
            hasErrors = true;
        }
        
        // Password validation
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("passwordError", "Password is required");
            hasErrors = true;
        } else if (!ValidationUtil.isValidPassword(password)) {
            request.setAttribute("passwordError", "Password must be at least 8 characters and include at least one uppercase letter, one lowercase letter, and one number");
            hasErrors = true;
        }
        
        // Confirm password validation
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("confirmPasswordError", "Please confirm your password");
            hasErrors = true;
        } else if (!password.equals(confirmPassword)) {
            request.setAttribute("confirmPasswordError", "Passwords do not match");
            hasErrors = true;
        }
        
        // Name validation
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
            // Preserve entered values (except password for security)
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("bio", bio);
            
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        // Create user object
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBio(bio);
        
        // Save user to database
        boolean success = userDAO.createUser(user);
        
        if (success) {
            // Set success message
            request.getSession().setAttribute("message", "Registration successful! Please log in.");
            
            // Redirect to login page
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            // Set error message
            request.setAttribute("errorMessage", "Registration failed. Please try again.");
            
            // Preserve entered values (except password for security)
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("bio", bio);
            
            // Redisplay the form
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
