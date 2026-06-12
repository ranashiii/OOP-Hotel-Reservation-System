package Config;

import Utilities.HotelException;

/**
 * Application configuration and initialization
 */
public class AppConfig {
    
    private static boolean initialized = false;
    
    public static void initialize() throws HotelException {
        if (initialized) {
            return;
        }
        
        try {
            DBConfig.initializeDriver();
            
            if (!DBConfig.testConnection()) {
                throw new HotelException("Database connection failed. Check your database configuration.");
            }
            
            initialized = true;
        } catch (Exception e) {
            throw new HotelException("Initialization failed: " + e.getMessage(), e);
        }
    }
    
    public static boolean isInitialized() {
        return initialized;
    }
    
    public static void shutdown() {
        DBConfig.closeConnection();
        initialized = false;
    }
}
