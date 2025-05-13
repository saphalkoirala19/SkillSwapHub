package com.skillswaphub.servlet;

import com.skillswaphub.dao.SkillDAO;
import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.model.Skill;
import com.skillswaphub.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for displaying detailed information about a skill
 */
@WebServlet("/skill/*")
public class SkillDetailServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SkillDetailServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private SkillDAO skillDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() {
        skillDAO = new SkillDAO();
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display skill details
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get skill ID from path
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // No skill ID provided, redirect to skills list
            response.sendRedirect(request.getContextPath() + "/skills");
            return;
        }
        
        // Extract skill ID from path
        String skillIdStr = pathInfo.substring(1);
        int skillId;
        
        try {
            skillId = Integer.parseInt(skillIdStr);
        } catch (NumberFormatException e) {
            // Invalid skill ID, redirect to skills list
            response.sendRedirect(request.getContextPath() + "/skills");
            return;
        }
        
        // Get skill details
        Skill skill = skillDAO.getSkillById(skillId);
        
        if (skill == null) {
            // Skill not found, redirect to skills list
            response.sendRedirect(request.getContextPath() + "/skills");
            return;
        }
        
        // Get users who offer this skill
        List<Integer> offeringUserIds = skillDAO.getUsersOfferingSkill(skillId);
        List<User> offeringUsers = new ArrayList<>();
        
        for (Integer userId : offeringUserIds) {
            User user = userDAO.getUserById(userId);
            if (user != null) {
                offeringUsers.add(user);
            }
        }
        
        // Get users who want to learn this skill
        List<Integer> wantingUserIds = skillDAO.getUsersWantingSkill(skillId);
        List<User> wantingUsers = new ArrayList<>();
        
        for (Integer userId : wantingUserIds) {
            User user = userDAO.getUserById(userId);
            if (user != null) {
                wantingUsers.add(user);
            }
        }
        
        // Set skill offer and want counts
        skill.setOfferCount(offeringUsers.size());
        skill.setWantCount(wantingUsers.size());
        
        // Check if current user offers or wants this skill
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            Integer currentUserId = (Integer) session.getAttribute("userId");
            boolean userOffersSkill = offeringUserIds.contains(currentUserId);
            boolean userWantsSkill = wantingUserIds.contains(currentUserId);
            
            request.setAttribute("userOffersSkill", userOffersSkill);
            request.setAttribute("userWantsSkill", userWantsSkill);
        }
        
        // Set attributes for JSP
        request.setAttribute("skill", skill);
        request.setAttribute("offeringUsers", offeringUsers);
        request.setAttribute("wantingUsers", wantingUsers);
        
        // Get related skills (same category)
        List<Skill> relatedSkills = skillDAO.getSkillsByCategory(skill.getCategory());
        // Remove current skill from related skills
        relatedSkills.removeIf(s -> s.getSkillId() == skillId);
        // Limit to 5 related skills
        if (relatedSkills.size() > 5) {
            relatedSkills = relatedSkills.subList(0, 5);
        }
        
        request.setAttribute("relatedSkills", relatedSkills);
        
        // Forward to skill detail page
        request.getRequestDispatcher("/WEB-INF/views/skillDetail.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - add/remove skill to/from user's offered/wanted skills
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
        
        // Get user ID from session
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Get skill ID and action from request
        String skillIdStr = request.getParameter("skillId");
        String action = request.getParameter("action");
        
        if (skillIdStr == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/skills");
            return;
        }
        
        int skillId;
        try {
            skillId = Integer.parseInt(skillIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/skills");
            return;
        }
        
        boolean success = false;
        
        // Process action
        switch (action) {
            case "addOffer":
                String proficiencyLevel = request.getParameter("proficiencyLevel");
                String offerDescription = request.getParameter("description");
                
                if (proficiencyLevel != null && !proficiencyLevel.isEmpty()) {
                    success = skillDAO.addOfferedSkill(userId, skillId, proficiencyLevel, offerDescription);
                }
                break;
                
            case "addWant":
                String currentLevel = request.getParameter("currentLevel");
                String wantDescription = request.getParameter("description");
                
                if (currentLevel != null && !currentLevel.isEmpty()) {
                    success = skillDAO.addWantedSkill(userId, skillId, currentLevel, wantDescription);
                }
                break;
                
            case "removeOffer":
                success = skillDAO.removeOfferedSkill(userId, skillId);
                break;
                
            case "removeWant":
                success = skillDAO.removeWantedSkill(userId, skillId);
                break;
        }
        
        // Set message based on action result
        if (success) {
            session.setAttribute("message", "Skill updated successfully!");
        } else {
            session.setAttribute("errorMessage", "Failed to update skill. Please try again.");
        }
        
        // Redirect back to skill detail page
        response.sendRedirect(request.getContextPath() + "/skill/" + skillId);
    }
}
