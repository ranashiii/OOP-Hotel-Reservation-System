package Utilities;

/**
 * HotelException - Custom Exception Class
 * 
 * Used for business logic exceptions throughout the Hotel Reservation System.
 * Provides meaningful error messages for validation, database, and business logic failures.
 */
public class HotelException extends Exception {
    
    /**
     * Create exception with error message
     * 
     * @param message the error message
     */
    public HotelException(String message) {
        super(message);
    }
    
    /**
     * Create exception with error message and cause
     * 
     * @param message the error message
     * @param cause the root cause exception
     */
    public HotelException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Create exception with cause only
     * 
     * @param cause the root cause exception
     */
    public HotelException(Throwable cause) {
        super(cause);
    }
}
