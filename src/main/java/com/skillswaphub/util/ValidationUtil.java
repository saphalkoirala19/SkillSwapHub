package com.skillswaphub.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtil {

    // Regular expressions for validation
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,20}$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
    private static final String URL_PATTERN = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    // Compiled patterns for better performance
    private static final Pattern usernamePattern = Pattern.compile(USERNAME_PATTERN);
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private static final Pattern urlPattern = Pattern.compile(URL_PATTERN);

    /**
     * Validate username format
     * @param username Username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        return usernamePattern.matcher(username).matches();
    }

    /**
     * Validate email format
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return emailPattern.matcher(email).matches();
    }

    /**
     * Validate password strength
     * @param password Password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return passwordPattern.matcher(password).matches();
    }

    /**
     * Validate URL format
     * @param url URL to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUrl(String url) {
        if (url == null) {
            return false;
        }
        return urlPattern.matcher(url).matches();
    }

    /**
     * Sanitize input to prevent XSS attacks
     * @param input Input to sanitize
     * @return Sanitized input
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        // Replace potentially dangerous characters
        String sanitized = input
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");

        return sanitized;
    }
}
