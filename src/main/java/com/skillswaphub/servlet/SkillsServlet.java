package com.skillswaphub.servlet;

import com.skillswaphub.dao.SkillDAO;
import com.skillswaphub.model.Skill;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling skill browsing
 */
@WebServlet("/skills")
public class SkillsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SkillsServlet.class.getName());
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_PAGE_SIZE = 12;
    
    private SkillDAO skillDAO;
    
    @Override
    public void init() {
        skillDAO = new SkillDAO();
    }
    
    /**
     * Handle GET requests - display skills list with search and filter options
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get search parameters
        String searchTerm = request.getParameter("search");
        String category = request.getParameter("category");
        String pageStr = request.getParameter("page");
        String sortBy = request.getParameter("sortBy");
        
        // Default page is 1
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid page number: {0}", pageStr);
            }
        }
        
        // Get skills based on search parameters
        List<Skill> skills;
        int totalSkills;
        
        if (searchTerm != null && !searchTerm.isEmpty()) {
            // Search by term
            skills = skillDAO.searchSkills(searchTerm);
            totalSkills = skills.size();
            
            // Set search term for display
            request.setAttribute("searchTerm", searchTerm);
        } else if (category != null && !category.isEmpty()) {
            // Filter by category
            skills = skillDAO.getSkillsByCategory(category);
            totalSkills = skills.size();
            
            // Set selected category for display
            request.setAttribute("selectedCategory", category);
        } else {
            // Get paginated skills
            skills = skillDAO.getSkillsWithPagination(page, DEFAULT_PAGE_SIZE);
            totalSkills = skillDAO.getTotalSkillCount();
        }
        
        // Sort skills if needed
        if (sortBy != null && !sortBy.isEmpty()) {
            sortSkills(skills, sortBy);
            request.setAttribute("sortBy", sortBy);
        }
        
        // Get all categories for filter dropdown
        List<String> categories = skillDAO.getAllCategories();
        
        // Get popular skills for sidebar
        List<Skill> popularSkills = skillDAO.getPopularSkills(5);
        
        // Calculate pagination info
        int totalPages = (int) Math.ceil((double) totalSkills / DEFAULT_PAGE_SIZE);
        
        // Set attributes for JSP
        request.setAttribute("skills", skills);
        request.setAttribute("categories", categories);
        request.setAttribute("popularSkills", popularSkills);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalSkills", totalSkills);
        
        // Forward to skills page
        request.getRequestDispatcher("/WEB-INF/views/skills.jsp").forward(request, response);
    }
    
    /**
     * Sort skills based on the specified criteria
     * @param skills List of skills to sort
     * @param sortBy Sort criteria (name, category, popularity)
     */
    private void sortSkills(List<Skill> skills, String sortBy) {
        switch (sortBy) {
            case "name":
                skills.sort((s1, s2) -> s1.getSkillName().compareToIgnoreCase(s2.getSkillName()));
                break;
            case "nameDesc":
                skills.sort((s1, s2) -> s2.getSkillName().compareToIgnoreCase(s1.getSkillName()));
                break;
            case "category":
                skills.sort((s1, s2) -> s1.getCategory().compareToIgnoreCase(s2.getCategory()));
                break;
            case "categoryDesc":
                skills.sort((s1, s2) -> s2.getCategory().compareToIgnoreCase(s1.getCategory()));
                break;
            case "popularity":
                skills.sort((s1, s2) -> Integer.compare(s2.getOfferCount() + s2.getWantCount(), 
                                                       s1.getOfferCount() + s1.getWantCount()));
                break;
            case "offers":
                skills.sort((s1, s2) -> Integer.compare(s2.getOfferCount(), s1.getOfferCount()));
                break;
            case "wants":
                skills.sort((s1, s2) -> Integer.compare(s2.getWantCount(), s1.getWantCount()));
                break;
            default:
                // Default sort by name
                skills.sort((s1, s2) -> s1.getSkillName().compareToIgnoreCase(s2.getSkillName()));
        }
    }
}
