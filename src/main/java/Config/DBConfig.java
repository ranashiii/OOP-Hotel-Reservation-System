package Config;

import Utilities.Constants;
import Utilities.HotelException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration and connection management
 */
public class DBConfig {
    
    // No static connection field – each call returns a new connection
    
    public static void initializeDriver() throws HotelException {
        try {
            Class.forName(Constants.DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new HotelException("Database driver not found: " + e.getMessage(), e);
        }
    }
    
    public static Connection getConnection() throws HotelException {
        try {
            return DriverManager.getConnection(
                Constants.DB_URL,
                Constants.DB_USER,
                Constants.DB_PASSWORD
            );
        } catch (SQLException e) {
            throw new HotelException("Database connection failed: " + e.getMessage(), e);
        }
    }
    
    // No static connection to close – this method is kept for compatibility,
    // but it does nothing now. You can remove it if you prefer.
    public static void closeConnection() {
        // Nothing to close – each caller should close its own connection
        System.out.println("DBConfig.closeConnection() called – no static connection to close.");
    }
    
    public static boolean testConnection() {
        try (Connection testConn = getConnection()) {
            return testConn != null && !testConn.isClosed();
        } catch (Exception e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}