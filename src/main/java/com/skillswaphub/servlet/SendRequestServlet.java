package com.skillswaphub.servlet;

import com.skillswaphub.dao.SkillDAO;
import com.skillswaphub.dao.SkillRequestDAO;
import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.model.Skill;
import com.skillswaphub.model.SkillRequest;
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
 * Servlet for handling sending skill swap requests
 */
@WebServlet("/send-request")
public class SendRequestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SendRequestServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private SkillRequestDAO skillRequestDAO;
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() {
        skillRequestDAO = new SkillRequestDAO();
        userDAO = new UserDAO();
        skillDAO = new SkillDAO();
    }
    
    /**
     * Handle GET requests - display form for sending a skill swap request
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
        User currentUser = userDAO.getUserById(currentUserId);
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get receiver ID from request parameter
        String receiverIdStr = request.getParameter("receiverId");
        String skillIdStr = request.getParameter("skillId");
        
        if (receiverIdStr == null || skillIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/skills");
            return;
        }
        
        try {
            int receiverId = Integer.parseInt(receiverIdStr);
            int skillId = Integer.parseInt(skillIdStr);
            
            // Get receiver user
            User receiver = userDAO.getUserById(receiverId);
            
            if (receiver == null) {
                response.sendRedirect(request.getContextPath() + "/skills");
                return;
            }
            
            // Get the skill the receiver is offering
            Skill receiverSkill = skillDAO.getSkillById(skillId);
            
            if (receiverSkill == null) {
                response.sendRedirect(request.getContextPath() + "/skills");
                return;
            }
            
            // Get current user's offered skills
            List<Skill> currentUserOfferedSkills = currentUser.getOfferedSkills();
            
            // Set attributes for JSP
            request.setAttribute("receiver", receiver);
            request.setAttribute("receiverSkill", receiverSkill);
            request.setAttribute("currentUserOfferedSkills", currentUserOfferedSkills);
            
            // Forward to send request page
            request.getRequestDispatcher("/WEB-INF/views/sendRequest.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/skills");
        }
    }
    
    /**
     * Handle POST requests - process sending a skill swap request
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
        Integer senderId = (Integer) session.getAttribute("userId");
        
        // Get form parameters
        String receiverIdStr = request.getParameter("receiverId");
        String offeredSkillIdStr = request.getParameter("offeredSkillId");
        String wantedSkillIdStr = request.getParameter("wantedSkillId");
        String message = request.getParameter("message");
        
        // Validate input
        boolean hasErrors = false;
        
        if (receiverIdStr == null || offeredSkillIdStr == null || wantedSkillIdStr == null) {
            session.setAttribute("errorMessage", "Invalid request parameters");
            response.sendRedirect(request.getContextPath() + "/skills");
            return;
        }
        
        try {
            int receiverId = Integer.parseInt(receiverIdStr);
            int offeredSkillId = Integer.parseInt(offeredSkillIdStr);
            int wantedSkillId = Integer.parseInt(wantedSkillIdStr);
            
            // Sanitize message
            if (message != null) {
                message = ValidationUtil.sanitizeInput(message);
            }
            
            // Check if sender and receiver are the same
            if (senderId == receiverId) {
                session.setAttribute("errorMessage", "You cannot send a request to yourself");
                response.sendRedirect(request.getContextPath() + "/skills");
                return;
            }
            
            // Check if request already exists
            if (skillRequestDAO.requestExists(senderId, receiverId, offeredSkillId, wantedSkillId)) {
                session.setAttribute("errorMessage", "A similar request already exists");
                response.sendRedirect(request.getContextPath() + "/requests");
                return;
            }
            
            // Create skill request
            SkillRequest skillRequest = new SkillRequest(senderId, receiverId, offeredSkillId, wantedSkillId, message);
            
            // Save to database
            boolean success = skillRequestDAO.createRequest(skillRequest);
            
            if (success) {
                // Set success message
                session.setAttribute("message", "Skill swap request sent successfully");
                
                // Redirect to requests page
                response.sendRedirect(request.getContextPath() + "/requests");
            } else {
                // Set error message
                session.setAttribute("errorMessage", "Failed to send request. Please try again.");
                
                // Redirect back to send request page
                response.sendRedirect(request.getContextPath() + "/send-request?receiverId=" + receiverId + "&skillId=" + wantedSkillId);
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid request parameters");
            response.sendRedirect(request.getContextPath() + "/skills");
        }
    }
}
