package com.hotelreservationsystem.config;

import com.hotelreservationsystem.util.Constants;
import com.hotelreservationsystem.util.HotelException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConfig - Database Configuration and Connection Management
 * 
 * Handles database connection setup, initialization, and connection pooling.
 * Provides static methods to get database connections using JDBC.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class DBConfig {
    
    private static Connection connection;
    
    /**
     * Initialize database driver
     * 
     * @throws HotelException if driver cannot be loaded
     */
    public static void initializeDriver() throws HotelException {
        try {
            Class.forName(Constants.DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new HotelException("Failed to load database driver: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get database connection
     * 
     * @return Connection object
     * @throws HotelException if connection fails
     */
    public static Connection getConnection() throws HotelException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                    Constants.DB_URL,
                    Constants.DB_USER,
                    Constants.DB_PASSWORD
                );
            }
            return connection;
        } catch (SQLException e) {
            throw new HotelException("Failed to establish database connection: " + e.getMessage(), e);
        }
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Test database connection
     * 
     * @return true if connection is successful
     */
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
