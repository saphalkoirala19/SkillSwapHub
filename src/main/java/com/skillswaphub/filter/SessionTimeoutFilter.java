package com.skillswaphub.filter;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter for handling session timeout and last activity tracking
 */
@WebFilter("/*")
public class SessionTimeoutFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(SessionTimeoutFilter.class.getName());
    private static final long MAX_INACTIVE_TIME = 30 * 60 * 1000; // 30 minutes in milliseconds

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // If there is an active session
        if (session != null) {
            // Check if this is a new session
            if (session.getAttribute("lastActivity") == null) {
                // Set initial last activity time
                session.setAttribute("lastActivity", System.currentTimeMillis());
            } else {
                // Get last activity time
                long lastActivity = (Long) session.getAttribute("lastActivity");
                long currentTime = System.currentTimeMillis();
                
                // Check if session has been inactive for too long
                if (currentTime - lastActivity > MAX_INACTIVE_TIME) {
                    // Session timeout - invalidate session
                    session.invalidate();
                    
                    // Redirect to login page with timeout message
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?timeout=true");
                    return;
                }
                
                // Update last activity time
                session.setAttribute("lastActivity", currentTime);
            }
        }
        
        // Continue with the request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
