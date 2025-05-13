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
    private static final String DB_URL = "jdbc:mysql://localhost:3306/skillswaphub?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    
    static {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error loading JDBC driver", e);
        }
    }
    
    /**
     * Get a database connection
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
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
