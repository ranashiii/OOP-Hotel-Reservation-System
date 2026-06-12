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
    
    private static Connection connection;
    
    public static void initializeDriver() throws HotelException {
        try {
            Class.forName(Constants.DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new HotelException("Database driver not found: " + e.getMessage(), e);
        }
    }
    
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
            throw new HotelException("Database connection failed: " + e.getMessage(), e);
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            return testConn != null && !testConn.isClosed();
        } catch (Exception e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
