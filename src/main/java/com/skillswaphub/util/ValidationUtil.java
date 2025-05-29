package com.skillswaphub.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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
    private static final Pattern USERNAME_PATTERN_COMPILED = Pattern.compile(USERNAME_PATTERN);
    private static final Pattern EMAIL_PATTERN_COMPILED = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern PASSWORD_PATTERN_COMPILED = Pattern.compile(PASSWORD_PATTERN);
    private static final Pattern URL_PATTERN_COMPILED = Pattern.compile(URL_PATTERN);

    /**
     * Validate username format
     * @param username Username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }
        return USERNAME_PATTERN_COMPILED.matcher(username).matches();
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
        return EMAIL_PATTERN_COMPILED.matcher(email).matches();
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
        return PASSWORD_PATTERN_COMPILED.matcher(password).matches();
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
        return URL_PATTERN_COMPILED.matcher(url).matches();
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
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");

        return sanitized;
    }

    /**
     * Validate and sanitize a required field
     * @param value Field value
     * @param fieldName Field name for error message
     * @param request HTTP request to set error attribute
     * @return Sanitized value if valid, null if invalid
     */
    public static String validateRequiredField(String value, String fieldName, HttpServletRequest request) {
        if (value == null || value.trim().isEmpty()) {
            request.setAttribute(fieldName + "Error", fieldName + " is required");
            return null;
        }

        return sanitizeInput(value.trim());
    }

    /**
     * Validate and sanitize an optional field
     * @param value Field value
     * @return Sanitized value if present, empty string if null or empty
     */
    public static String validateOptionalField(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }

        return sanitizeInput(value.trim());
    }

    /**
     * Validate integer parameter
     * @param value String value to parse
     * @param fieldName Field name for error message
     * @param request HTTP request to set error attribute
     * @return Parsed integer if valid, null if invalid
     */
    public static Integer validateIntParameter(String value, String fieldName, HttpServletRequest request) {
        if (value == null || value.trim().isEmpty()) {
            request.setAttribute(fieldName + "Error", fieldName + " is required");
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            request.setAttribute(fieldName + "Error", fieldName + " must be a valid number");
            return null;
        }
    }
}
