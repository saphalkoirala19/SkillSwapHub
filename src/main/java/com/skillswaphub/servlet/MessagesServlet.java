package com.skillswaphub.servlet;

import com.skillswaphub.dao.MessageDAO;
import com.skillswaphub.dao.UserDAO;
import com.skillswaphub.model.Message;
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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for handling messages and conversations
 */
@WebServlet("/messages")
public class MessagesServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MessagesServlet.class.getName());
    private static final long serialVersionUID = 1L;
    
    private MessageDAO messageDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() {
        messageDAO = new MessageDAO();
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display messages page
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
        
        // Get conversation partner ID from request parameter
        String partnerIdStr = request.getParameter("userId");
        Integer partnerId = null;
        User partner = null;
        
        if (partnerIdStr != null && !partnerIdStr.isEmpty()) {
            try {
                partnerId = Integer.parseInt(partnerIdStr);
                
                // Get partner user
                partner = userDAO.getUserById(partnerId);
                
                if (partner == null) {
                    session.setAttribute("errorMessage", "User not found");
                    response.sendRedirect(request.getContextPath() + "/messages");
                    return;
                }
                
                // Mark messages from this user as read
                messageDAO.markMessagesAsRead(partnerId, currentUserId);
                
                // Get conversation
                List<Message> conversation = messageDAO.getConversation(currentUserId, partnerId);
                request.setAttribute("conversation", conversation);
                request.setAttribute("partner", partner);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid user ID: " + partnerIdStr, e);
                session.setAttribute("errorMessage", "Invalid user ID");
                response.sendRedirect(request.getContextPath() + "/messages");
                return;
            }
        }
        
        // Get all conversations
        Map<Integer, Message> conversations = messageDAO.getConversations(currentUserId);
        request.setAttribute("conversations", conversations);
        
        // Get users for each conversation
        Map<Integer, User> conversationUsers = new java.util.HashMap<>();
        for (Integer userId : conversations.keySet()) {
            User user = userDAO.getUserById(userId);
            if (user != null) {
                conversationUsers.put(userId, user);
            }
        }
        request.setAttribute("conversationUsers", conversationUsers);
        
        // Get unread message counts
        Map<Integer, Integer> unreadCounts = new java.util.HashMap<>();
        for (Integer userId : conversations.keySet()) {
            int count = messageDAO.getUnreadMessageCountFromUser(currentUserId, userId);
            unreadCounts.put(userId, count);
        }
        request.setAttribute("unreadCounts", unreadCounts);
        
        // Set active conversation
        request.setAttribute("activeConversation", partnerId);
        
        // Forward to messages page
        request.getRequestDispatcher("/WEB-INF/views/messages.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - send a message
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
        String content = request.getParameter("content");
        
        // Validate input
        if (receiverIdStr == null || receiverIdStr.isEmpty() || content == null || content.isEmpty()) {
            session.setAttribute("errorMessage", "Receiver ID and message content are required");
            response.sendRedirect(request.getContextPath() + "/messages");
            return;
        }
        
        // Sanitize content
        content = ValidationUtil.sanitizeInput(content);
        
        try {
            int receiverId = Integer.parseInt(receiverIdStr);
            
            // Check if receiver exists
            User receiver = userDAO.getUserById(receiverId);
            if (receiver == null) {
                session.setAttribute("errorMessage", "Receiver not found");
                response.sendRedirect(request.getContextPath() + "/messages");
                return;
            }
            
            // Create message
            Message message = new Message(senderId, receiverId, content);
            
            // Save to database
            boolean success = messageDAO.sendMessage(message);
            
            if (success) {
                // Redirect to conversation
                response.sendRedirect(request.getContextPath() + "/messages?userId=" + receiverId);
            } else {
                session.setAttribute("errorMessage", "Failed to send message. Please try again.");
                response.sendRedirect(request.getContextPath() + "/messages?userId=" + receiverId);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid receiver ID: " + receiverIdStr, e);
            session.setAttribute("errorMessage", "Invalid receiver ID");
            response.sendRedirect(request.getContextPath() + "/messages");
        }
    }
}
