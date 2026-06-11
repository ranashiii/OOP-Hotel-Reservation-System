package com.hotelreservationsystem.util;

/**
 * HotelException - Custom Exception for Hotel Reservation System
 * 
 * Used for all business logic errors and validation failures.
 * Provides detailed error messages for debugging and user feedback.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class HotelException extends Exception {
    
    /**
     * Constructor with error message
     * 
     * @param message the error message
     */
    public HotelException(String message) {
        super(message);
    }
    
    /**
     * Constructor with error message and cause
     * 
     * @param message the error message
     * @param cause the root cause exception
     */
    public HotelException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with cause only
     * 
     * @param cause the root cause exception
     */
    public HotelException(Throwable cause) {
        super(cause);
    }
}
