package com.skillswaphub.servlet;

import com.skillswaphub.dao.SkillDAO;
import com.skillswaphub.model.Skill;
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
 * Servlet for handling adding new skills to the system
 */
@WebServlet("/add-skill")
public class AddSkillServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddSkillServlet.class.getName());
    private static final long serialVersionUID = 1L;

    private SkillDAO skillDAO;

    @Override
    public void init() {
        skillDAO = new SkillDAO();
    }

    /**
     * Handle GET requests - display add skill form
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

        // Get all categories for the dropdown
        List<String> categories = skillDAO.getAllCategories();
        request.setAttribute("categories", categories);

        // Forward to add skill page
        request.getRequestDispatcher("/WEB-INF/views/addSkill.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process add skill form
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

        // Get form parameters
        String skillName = request.getParameter("skillName");
        String category = request.getParameter("category");
        String newCategory = request.getParameter("newCategory");
        String imageUrl = request.getParameter("imageUrl");

        // Validate input
        boolean hasErrors = false;

        // Skill name validation
        if (skillName == null || skillName.trim().isEmpty()) {
            request.setAttribute("skillNameError", "Skill name is required");
            hasErrors = true;
        } else if (skillName.length() > 100) {
            request.setAttribute("skillNameError", "Skill name must be at most 100 characters");
            hasErrors = true;
        }

        // Category validation
        if ((category == null || category.trim().isEmpty() || category.equals("new")) &&
            (newCategory == null || newCategory.trim().isEmpty())) {
            request.setAttribute("categoryError", "Category is required");
            hasErrors = true;
        }

        // Use new category if selected
        if (category != null && category.equals("new") && newCategory != null && !newCategory.trim().isEmpty()) {
            category = newCategory.trim();
        }

        // Image URL validation (optional)
        if (imageUrl != null && !imageUrl.trim().isEmpty() && !ValidationUtil.isValidUrl(imageUrl)) {
            request.setAttribute("imageUrlError", "Please enter a valid URL");
            hasErrors = true;
        }

        // If there are validation errors, redisplay the form with error messages
        if (hasErrors) {
            // Get all categories for the dropdown
            List<String> categories = skillDAO.getAllCategories();
            request.setAttribute("categories", categories);

            // Preserve entered values
            request.setAttribute("skillName", skillName);
            request.setAttribute("selectedCategory", category);
            request.setAttribute("newCategory", newCategory);
            request.setAttribute("imageUrl", imageUrl);

            request.getRequestDispatcher("/WEB-INF/views/addSkill.jsp").forward(request, response);
            return;
        }

        // Create skill object
        Skill skill = new Skill();
        skill.setSkillName(skillName);
        skill.setCategory(category);
        skill.setImageUrl(imageUrl);
        skill.setUserId(userId);

        // Save skill to database
        boolean success = skillDAO.addSkill(skill);

        if (success) {
            // Set success message
            session.setAttribute("message", "Skill added successfully!");

            // Redirect to skills page
            response.sendRedirect(request.getContextPath() + "/skills");
        } else {
            // Set error message
            request.setAttribute("errorMessage", "Failed to add skill. Please try again.");

            // Get all categories for the dropdown
            List<String> categories = skillDAO.getAllCategories();
            request.setAttribute("categories", categories);

            // Preserve entered values
            request.setAttribute("skillName", skillName);
            request.setAttribute("selectedCategory", category);
            request.setAttribute("newCategory", newCategory);
            request.setAttribute("imageUrl", imageUrl);

            // Redisplay the form
            request.getRequestDispatcher("/WEB-INF/views/addSkill.jsp").forward(request, response);
        }
    }
}
