package com.skillswaphub.servlet;

import com.skillswaphub.dao.SkillRequestDAO;
import com.skillswaphub.model.SkillRequest;

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
 * Servlet for handling skill swap request management (accept/decline)
 */
@WebServlet("/manage-request")
public class ManageRequestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ManageRequestServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private SkillRequestDAO skillRequestDAO;
    
    @Override
    public void init() {
        skillRequestDAO = new SkillRequestDAO();
    }
    
    /**
     * Handle POST requests - process request management actions
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
        
        // Get request parameters
        String requestIdStr = request.getParameter("requestId");
        String action = request.getParameter("action");
        
        if (requestIdStr == null || action == null) {
            session.setAttribute("errorMessage", "Invalid request parameters");
            response.sendRedirect(request.getContextPath() + "/requests");
            return;
        }
        
        try {
            int requestId = Integer.parseInt(requestIdStr);
            
            // Get the request
            SkillRequest skillRequest = skillRequestDAO.getRequestById(requestId);
            
            if (skillRequest == null) {
                session.setAttribute("errorMessage", "Request not found");
                response.sendRedirect(request.getContextPath() + "/requests");
                return;
            }
            
            // Check if the current user is the receiver of the request
            if (skillRequest.getReceiverId() != userId) {
                session.setAttribute("errorMessage", "You are not authorized to manage this request");
                response.sendRedirect(request.getContextPath() + "/requests");
                return;
            }
            
            // Check if the request is already processed
            if (!skillRequest.isPending()) {
                session.setAttribute("errorMessage", "This request has already been processed");
                response.sendRedirect(request.getContextPath() + "/requests");
                return;
            }
            
            boolean success = false;
            String message = "";
            
            // Process the action
            switch (action) {
                case "accept":
                    success = skillRequestDAO.updateRequestStatus(requestId, "Accepted");
                    message = "Request accepted successfully";
                    break;
                    
                case "decline":
                    success = skillRequestDAO.updateRequestStatus(requestId, "Declined");
                    message = "Request declined successfully";
                    break;
                    
                default:
                    session.setAttribute("errorMessage", "Invalid action");
                    response.sendRedirect(request.getContextPath() + "/requests");
                    return;
            }
            
            if (success) {
                session.setAttribute("message", message);
            } else {
                session.setAttribute("errorMessage", "Failed to process request. Please try again.");
            }
            
            // Redirect back to requests page
            response.sendRedirect(request.getContextPath() + "/requests");
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid request ID");
            response.sendRedirect(request.getContextPath() + "/requests");
        }
    }
}
