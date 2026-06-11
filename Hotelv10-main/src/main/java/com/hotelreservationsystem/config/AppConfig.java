package com.hotelreservationsystem.config;

import com.hotelreservationsystem.util.HotelException;

/**
 * AppConfig - Application Configuration and Initialization
 * 
 * Manages application-level configuration including database setup,
 * look and feel, and other initialization requirements.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class AppConfig {
    
    private static boolean initialized = false;
    
    /**
     * Initialize application configuration
     * 
     * @throws HotelException if initialization fails
     */
    public static void initialize() throws HotelException {
        if (initialized) {
            return;
        }
        
        try {
            // Initialize database driver
            DBConfig.initializeDriver();
            
            // Test database connection
            if (!DBConfig.testConnection()) {
                throw new HotelException("Failed to connect to database. Please check database configuration.");
            }
            
            initialized = true;
        } catch (Exception e) {
            throw new HotelException("Application initialization failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if application is initialized
     * 
     * @return true if initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Shutdown application
     */
    public static void shutdown() {
        DBConfig.closeConnection();
        initialized = false;
    }
}
