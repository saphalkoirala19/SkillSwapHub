package com.skillswaphub.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling errors in servlets
 */
public class ErrorHandler {
    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getName());
    
    /**
     * Handle database errors in servlets
     * @param e Exception to handle
     * @param request HTTP request
     * @param response HTTP response
     * @param errorMessage Error message to display to user
     * @param redirectPath Path to redirect to
     * @throws IOException if an I/O error occurs
     */
    public static void handleDatabaseError(SQLException e, HttpServletRequest request, 
                                          HttpServletResponse response, String errorMessage, 
                                          String redirectPath) throws IOException {
        LOGGER.log(Level.SEVERE, errorMessage, e);
        
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", errorMessage + ": " + e.getMessage());
        
        response.sendRedirect(request.getContextPath() + redirectPath);
    }
    
    /**
     * Handle general errors in servlets
     * @param e Exception to handle
     * @param request HTTP request
     * @param response HTTP response
     * @param errorMessage Error message to display to user
     * @param redirectPath Path to redirect to
     * @throws IOException if an I/O error occurs
     */
    public static void handleGeneralError(Exception e, HttpServletRequest request, 
                                         HttpServletResponse response, String errorMessage, 
                                         String redirectPath) throws IOException {
        LOGGER.log(Level.SEVERE, errorMessage, e);
        
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", errorMessage);
        
        response.sendRedirect(request.getContextPath() + redirectPath);
    }
}
