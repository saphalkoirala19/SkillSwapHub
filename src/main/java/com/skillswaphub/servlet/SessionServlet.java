package com.skillswaphub.servlet;

import com.skillswaphub.dao.SessionDAO;
import com.skillswaphub.dao.SkillRequestDAO;
import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.dao.SkillDAO;
import com.skillswaphub.model.Session;
import com.skillswaphub.model.SkillRequest;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling skill swap sessions
 */
@WebServlet("/sessions")
public class SessionServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SessionServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private SessionDAO sessionDAO;
    private SkillRequestDAO requestDAO;
    private UserDAO userDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() {
        sessionDAO = new SessionDAO();
        requestDAO = new SkillRequestDAO();
        userDAO = new UserDAO();
        skillDAO = new SkillDAO();
    }
    
    /**
     * Handle GET requests - display sessions page
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
        
        // Get action from request parameter
        String action = request.getParameter("action");
        
        if ("schedule".equals(action)) {
            // Handle schedule form display
            String requestIdStr = request.getParameter("requestId");
            
            if (requestIdStr == null || requestIdStr.isEmpty()) {
                session.setAttribute("errorMessage", "Missing request ID");
                response.sendRedirect(request.getContextPath() + "/requests");
                return;
            }
            
            try {
                int requestId = Integer.parseInt(requestIdStr);
                
                // Get request
                SkillRequest skillRequest = requestDAO.getRequestById(requestId);
                
                if (skillRequest == null) {
                    session.setAttribute("errorMessage", "Request not found");
                    response.sendRedirect(request.getContextPath() + "/requests");
                    return;
                }
                
                // Check if user is involved in the request
                if (skillRequest.getSenderId() != userId && skillRequest.getReceiverId() != userId) {
                    session.setAttribute("errorMessage", "You are not authorized to schedule a session for this request");
                    response.sendRedirect(request.getContextPath() + "/requests");
                    return;
                }
                
                // Check if request is accepted
                if (!skillRequest.isAccepted()) {
                    session.setAttribute("errorMessage", "Request must be accepted before scheduling a session");
                    response.sendRedirect(request.getContextPath() + "/requests");
                    return;
                }
                
                // Set attributes for JSP
                request.setAttribute("skillRequest", skillRequest);
                
                // Forward to schedule session page
                request.getRequestDispatcher("/WEB-INF/views/scheduleSession.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid request ID: " + requestIdStr, e);
                session.setAttribute("errorMessage", "Invalid request ID");
                response.sendRedirect(request.getContextPath() + "/requests");
                return;
            }
        } else if ("view".equals(action)) {
            // Handle view session details
            String sessionIdStr = request.getParameter("sessionId");
            
            if (sessionIdStr == null || sessionIdStr.isEmpty()) {
                session.setAttribute("errorMessage", "Missing session ID");
                response.sendRedirect(request.getContextPath() + "/sessions");
                return;
            }
            
            try {
                int sessionId = Integer.parseInt(sessionIdStr);
                
                // Get session
                Session skillSession = sessionDAO.getSessionById(sessionId);
                
                if (skillSession == null) {
                    session.setAttribute("errorMessage", "Session not found");
                    response.sendRedirect(request.getContextPath() + "/sessions");
                    return;
                }
                
                // Check if user is involved in the session
                if (skillSession.getTeacherId() != userId && skillSession.getStudentId() != userId) {
                    session.setAttribute("errorMessage", "You are not authorized to view this session");
                    response.sendRedirect(request.getContextPath() + "/sessions");
                    return;
                }
                
                // Set attributes for JSP
                request.setAttribute("skillSession", skillSession);
                
                // Forward to session detail page
                request.getRequestDispatcher("/WEB-INF/views/sessionDetail.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid session ID: " + sessionIdStr, e);
                session.setAttribute("errorMessage", "Invalid session ID");
                response.sendRedirect(request.getContextPath() + "/sessions");
                return;
            }
        }
        
        // Default: show sessions list
        
        // Get upcoming sessions
        List<Session> upcomingSessions = sessionDAO.getUpcomingSessionsForUser(userId);
        request.setAttribute("upcomingSessions", upcomingSessions);
        
        // Get past sessions
        List<Session> pastSessions = sessionDAO.getPastSessionsForUser(userId);
        request.setAttribute("pastSessions", pastSessions);
        
        // Forward to sessions page
        request.getRequestDispatcher("/WEB-INF/views/sessions.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - create or update a session
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
        
        // Get action from request parameter
        String action = request.getParameter("action");
        
        if ("schedule".equals(action)) {
            // Handle schedule session
            String requestIdStr = request.getParameter("requestId");
            String sessionDateStr = request.getParameter("sessionDate");
            String sessionTimeStr = request.getParameter("sessionTime");
            String durationStr = request.getParameter("duration");
            String location = request.getParameter("location");
            String notes = request.getParameter("notes");
            
            // Validate input
            if (requestIdStr == null || sessionDateStr == null || sessionTimeStr == null || durationStr == null) {
                session.setAttribute("errorMessage", "Missing required parameters");
                response.sendRedirect(request.getContextPath() + "/sessions");
                return;
            }
            
            try {
                int requestId = Integer.parseInt(requestIdStr);
                int duration = Integer.parseInt(durationStr);
                
                // Get request
                SkillRequest skillRequest = requestDAO.getRequestById(requestId);
                
                if (skillRequest == null) {
                    session.setAttribute("errorMessage", "Request not found");
                    response.sendRedirect(request.getContextPath() + "/sessions");
                    return;
                }
                
                // Check if user is involved in the request
                if (skillRequest.getSenderId() != userId && skillRequest.getReceiverId() != userId) {
                    session.setAttribute("errorMessage", "You are not authorized to schedule a session for this request");
                    response.sendRedirect(request.getContextPath() + "/sessions");
                    return;
                }
                
                // Parse session date and time
                LocalDateTime sessionDateTime;
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    sessionDateTime = LocalDateTime.parse(sessionDateStr + " " + sessionTimeStr, formatter);
                } catch (DateTimeParseException e) {
                    LOGGER.log(Level.WARNING, "Invalid date or time format", e);
                    session.setAttribute("errorMessage", "Invalid date or time format");
                    response.sendRedirect(request.getContextPath() + "/sessions?action=schedule&requestId=" + requestId);
                    return;
                }
                
                // Check if session date is in the future
                if (sessionDateTime.isBefore(LocalDateTime.now())) {
                    session.setAttribute("errorMessage", "Session date must be in the future");
                    response.sendRedirect(request.getContextPath() + "/sessions?action=schedule&requestId=" + requestId);
                    return;
                }
                
                // Sanitize input
                if (location != null) {
                    location = ValidationUtil.sanitizeInput(location);
                }
                
                if (notes != null) {
                    notes = ValidationUtil.sanitizeInput(notes);
                }
                
                // Determine teacher and student
                int teacherId, studentId, skillId;
                if (skillRequest.getSenderId() == userId) {
                    // Current user is the sender (offering the skill)
                    teacherId = userId;
                    studentId = skillRequest.getReceiverId();
                    skillId = skillRequest.getOfferedSkillId();
                } else {
                    // Current user is the receiver (offering the skill)
                    teacherId = userId;
                    studentId = skillRequest.getSenderId();
                    skillId = skillRequest.getWantedSkillId();
                }
                
                // Create session
                Session skillSession = new Session(requestId, teacherId, studentId, skillId, sessionDateTime, duration, location, notes);
                
                boolean success = sessionDAO.createSession(skillSession);
                
                if (success) {
                    session.setAttribute("message", "Session scheduled successfully");
                    response.sendRedirect(request.getContextPath() + "/sessions");
                } else {
                    session.setAttribute("errorMessage", "Failed to schedule session");
                    response.sendRedirect(request.getContextPath() + "/sessions?action=schedule&requestId=" + requestId);
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid parameter", e);
                session.setAttribute("errorMessage", "Invalid parameter");
                response.sendRedirect(request.getContextPath() + "/sessions");
            }
        } else if ("update".equals(action)) {
            // Handle update session status
            String sessionIdStr = request.getParameter("sessionId");
            String status = request.getParameter("status");
            
            if (sessionIdStr == null || status == null) {
                session.setAttribute("errorMessage", "Missing required parameters");
                response.sendRedirect(request.getContextPath() + "/sessions");
                return;
            }
            
            try {
                int sessionId = Integer.parseInt(sessionIdStr);
                
                // Get session
                Session skillSession = sessionDAO.getSessionById(sessionId);
                
                if (skillSession == null) {
                    session.setAttribute("errorMessage", "Session not found");
                    response.sendRedirect(request.getContextPath() + "/sessions");
                    return;
                }
                
                // Check if user is involved in the session
                if (skillSession.getTeacherId() != userId && skillSession.getStudentId() != userId) {
                    session.setAttribute("errorMessage", "You are not authorized to update this session");
                    response.sendRedirect(request.getContextPath() + "/sessions");
                    return;
                }
                
                // Validate status
                if (!status.equals("Completed") && !status.equals("Cancelled")) {
                    session.setAttribute("errorMessage", "Invalid status");
                    response.sendRedirect(request.getContextPath() + "/sessions");
                    return;
                }
                
                // Update session status
                boolean success = sessionDAO.updateSessionStatus(sessionId, status);
                
                if (success) {
                    session.setAttribute("message", "Session status updated successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to update session status");
                }
                
                response.sendRedirect(request.getContextPath() + "/sessions");
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid session ID: " + sessionIdStr, e);
                session.setAttribute("errorMessage", "Invalid session ID");
                response.sendRedirect(request.getContextPath() + "/sessions");
            }
        }
    }
}
