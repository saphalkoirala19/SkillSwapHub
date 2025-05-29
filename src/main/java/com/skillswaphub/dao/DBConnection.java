package com.skillswaphub.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database connection utility class
 */
public class DBConnection {
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

    // Database connection parameters
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/skillswaphub?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
            LOGGER.log(Level.INFO, "JDBC driver loaded successfully: " + JDBC_DRIVER);

            // Test connection
            try (Connection testConn = getConnection()) {
                LOGGER.log(Level.INFO, "Database connection test successful");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database connection test failed", e);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading JDBC driver: " + JDBC_DRIVER, e);
        }
    }

    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
            throw e; // Rethrow to let callers handle it
        }
    }

    /**
     * Close the database connection
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing connection", e);
            }
        }
    }
}
