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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling viewing skill swap requests
 */
@WebServlet("/requests")
public class RequestsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RequestsServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private SkillRequestDAO skillRequestDAO;
    
    @Override
    public void init() {
        skillRequestDAO = new SkillRequestDAO();
    }
    
    /**
     * Handle GET requests - display requests page
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
        
        // Get tab parameter (default to "received")
        String tab = request.getParameter("tab");
        if (tab == null || (!tab.equals("sent") && !tab.equals("received"))) {
            tab = "received";
        }
        
        // Get requests based on tab
        List<SkillRequest> requests;
        
        if (tab.equals("sent")) {
            requests = skillRequestDAO.getSentRequestsByUser(userId);
        } else {
            requests = skillRequestDAO.getReceivedRequestsByUser(userId);
        }
        
        // Set attributes for JSP
        request.setAttribute("requests", requests);
        request.setAttribute("activeTab", tab);
        
        // Forward to requests page
        request.getRequestDispatcher("/WEB-INF/views/requests.jsp").forward(request, response);
    }
}
