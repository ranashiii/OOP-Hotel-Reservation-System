package com.hotelreservationsystem.util;

/**
 * Constants - System-wide Constants and Configuration Values
 * 
 * Contains all magic numbers, strings, and configuration values used
 * throughout the Hotel Reservation System application.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class Constants {
    
    // ============ DATABASE CONFIGURATION ============
    public static final String DB_URL = "jdbc:mysql://localhost:3306/hotelreservationsystem";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // ============ USER ACCESS LEVELS ============
    public static final String ACCESS_ADMIN = "Admin";
    public static final String ACCESS_RECEPTIONIST = "Receptionist";
    public static final String ACCESS_GUEST = "Guest";
    
    // ============ ROOM STATUS ============
    public static final String ROOM_STATUS_AVAILABLE = "Available";
    public static final String ROOM_STATUS_OCCUPIED = "Occupied";
    public static final String ROOM_STATUS_MAINTENANCE = "Maintenance";
    public static final String ROOM_STATUS_CLEANING = "Cleaning";
    
    // ============ ROOM TYPES ============
    public static final String ROOM_TYPE_SINGLE = "Single Standard";
    public static final String ROOM_TYPE_DOUBLE = "Double Standard";
    public static final String ROOM_TYPE_DOUBLE_DELUXE = "Double Deluxe";
    public static final String ROOM_TYPE_SUITE = "Suite Deluxe";
    
    // ============ RESERVATION STATUS ============
    public static final String RESERVATION_STATUS_CONFIRMED = "Confirmed";
    public static final String RESERVATION_STATUS_CHECKED_IN = "Checked-In";
    public static final String RESERVATION_STATUS_CHECKED_OUT = "Checked-Out";
    public static final String RESERVATION_STATUS_CANCELLED = "Cancelled";
    
    // ============ PAYMENT STATUS ============
    public static final String PAYMENT_STATUS_PENDING = "Pending";
    public static final String PAYMENT_STATUS_COMPLETED = "Completed";
    public static final String PAYMENT_STATUS_FAILED = "Failed";
    public static final String PAYMENT_STATUS_REFUNDED = "Refunded";
    
    // ============ PAYMENT METHODS ============
    public static final String PAYMENT_METHOD_CASH = "Cash";
    public static final String PAYMENT_METHOD_CREDIT_CARD = "Credit Card";
    public static final String PAYMENT_METHOD_E_WALLET = "E-Wallet";
    
    // ============ BUSINESS LOGIC CONSTANTS ============
    public static final double TAX_RATE = 0.12; // 12% tax
    public static final int MAX_STAY_NIGHTS = 365;
    public static final int MIN_STAY_NIGHTS = 1;
    public static final int STANDARD_CHECKOUT_HOUR = 11; // 11 AM
    public static final int MIN_ROOM_CAPACITY = 1;
    public static final int MAX_ROOM_CAPACITY = 10;
    
    // ============ LATE CHECKOUT FEES ============
    public static final double LATE_CHECKOUT_FEE_1_2_HOURS = 500.00;
    public static final double LATE_CHECKOUT_FEE_2_4_HOURS = 1000.00;
    public static final double LATE_CHECKOUT_FEE_4_PLUS_HOURS = 2000.00;
    
    // ============ REFUND POLICY DAYS ============
    public static final int REFUND_FULL_DAYS = 7;
    public static final int REFUND_PARTIAL_DAYS = 7;
    public static final double REFUND_PARTIAL_PERCENT = 0.90;
    
    // ============ VALIDATION CONSTRAINTS ============
    public static final int USERNAME_MIN_LENGTH = 5;
    public static final int USERNAME_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int FIRST_NAME_MIN_LENGTH = 2;
    public static final int FIRST_NAME_MAX_LENGTH = 50;
    public static final int LAST_NAME_MIN_LENGTH = 2;
    public static final int LAST_NAME_MAX_LENGTH = 50;
    public static final int ADDRESS_MAX_LENGTH = 255;
    public static final int PHONE_LENGTH_VALID = 11;
    public static final int ID_DOCUMENT_MIN_LENGTH = 5;
    
    // ============ PAYMENT VALIDATION ============
    public static final int CREDIT_CARD_MIN_LENGTH = 13;
    public static final int CREDIT_CARD_MAX_LENGTH = 19;
    public static final int CVV_LENGTH_MIN = 3;
    public static final int CVV_LENGTH_MAX = 4;
    public static final int OTP_LENGTH = 6;
    
    // ============ GUI WINDOW SIZES ============
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;
    public static final int DIALOG_WIDTH = 500;
    public static final int DIALOG_HEIGHT = 300;
    
    // ============ DATE FORMAT ============
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "MMM dd, yyyy";
    
    // ============ CURRENCY ============
    public static final String CURRENCY_SYMBOL = "PHP";
    public static final String CURRENCY_FORMAT = "PHP %.2f";
    public static final int CURRENCY_DECIMAL_PLACES = 2;
    
    // ============ REGEX PATTERNS ============
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_]{5,20}$";
    public static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
    public static final String REGEX_PHONE = "^(09|\\+639)\\d{9}$";
    public static final String REGEX_LETTERS_ONLY = "^[a-zA-Z\\s]*$";
    public static final String REGEX_NUMBERS_ONLY = "^[0-9]*$";
}
