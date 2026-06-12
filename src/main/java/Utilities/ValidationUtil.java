package Utilities;

import java.time.LocalDate;
import java.time.Period;

/**
 * ValidationUtil - Comprehensive Input Validation Utility
 * 
 * Provides validation methods for all system inputs including usernames,
 * passwords, emails, phone numbers, dates, and other business data.
 */
public class ValidationUtil {
    
    /**
     * Validate username format
     * 5-20 alphanumeric characters (letters, numbers, underscore only)
     */
    public static boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return username.matches(Constants.REGEX_USERNAME);
    }
    
    /**
     * Validate password strength
     * At least 8 characters with uppercase, lowercase, and number
     */
    public static boolean validatePassword(String password) {
        if (password == null || password.length() < Constants.PASSWORD_MIN_LENGTH) {
            return false;
        }
        
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        
        return hasUpper && hasLower && hasDigit;
    }
    
    /**
     * Validate email format
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches(Constants.REGEX_EMAIL);
    }
    
    /**
     * Validate name (first name, last name, middle name)
     * Letters only, 2-50 characters
     */
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        if (name.length() < Constants.FIRST_NAME_MIN_LENGTH || 
            name.length() > Constants.FIRST_NAME_MAX_LENGTH) {
            return false;
        }
        
        return name.matches("^[a-zA-Z\\s]+$");
    }
    
    /**
     * Validate Philippine phone number format
     * Format: 09XXXXXXXXX or +639XXXXXXXXX
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return phoneNumber.matches(Constants.REGEX_PHONE);
    }
    
    /**
     * Validate date of birth (must be at least 18 years old)
     */
    public static boolean validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        
        if (dateOfBirth.isAfter(LocalDate.now())) {
            return false;
        }
        
        LocalDate eighteenYearsAgo = LocalDate.now().minus(Period.ofYears(18));
        return !dateOfBirth.isAfter(eighteenYearsAgo);
    }
    
    /**
     * Validate address
     * Not empty and maximum 255 characters
     */
    public static boolean validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return address.length() <= Constants.ADDRESS_MAX_LENGTH;
    }
    
    /**
     * Validate date range (check-in before check-out)
     */
    public static boolean validateDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return false;
        }
        
        if (checkInDate.isBefore(LocalDate.now())) {
            return false;
        }
        
        return checkOutDate.isAfter(checkInDate);
    }
    
    /**
     * Validate payment amount
     * Greater than 0 and not exceeding 999,999.99
     */
    public static boolean validatePaymentAmount(double amount) {
        return amount > 0 && amount <= 999999.99;
    }
    
    /**
     * Validate credit card number (13-19 digits)
     */
    public static boolean validateCreditCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        
        String cleaned = cardNumber.replaceAll("\\s", "");
        return cleaned.matches("^[0-9]{13,19}$");
    }
    
    /**
     * Validate CVV (3-4 digits)
     */
    public static boolean validateCVV(String cvv) {
        if (cvv == null || cvv.isEmpty()) {
            return false;
        }
        return cvv.matches("^[0-9]{3,4}$");
    }
    
    /**
     * Validate OTP (6 digits)
     */
    public static boolean validateOTP(String otp) {
        if (otp == null || otp.isEmpty()) {
            return false;
        }
        return otp.matches("^[0-9]{6}$");
    }
    
    /**
     * Validate room capacity (1-10)
     */
    public static boolean validateRoomCapacity(int capacity) {
        return capacity >= Constants.MIN_ROOM_CAPACITY && 
               capacity <= Constants.MAX_ROOM_CAPACITY;
    }
    
    /**
     * Validate ID document number (minimum 5 characters)
     */
    public static boolean validateIdDocumentNumber(String idNumber) {
        if (idNumber == null || idNumber.trim().isEmpty()) {
            return false;
        }
        return idNumber.length() >= Constants.ID_DOCUMENT_MIN_LENGTH;
    }
    
    /**
     * Validate number of nights (1-365)
     */
    public static boolean validateNumberOfNights(int nights) {
        return nights >= Constants.MIN_STAY_NIGHTS && 
               nights <= Constants.MAX_STAY_NIGHTS;
    }
}
