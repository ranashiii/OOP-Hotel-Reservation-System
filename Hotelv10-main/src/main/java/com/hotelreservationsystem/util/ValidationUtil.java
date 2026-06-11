package com.hotelreservationsystem.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * ValidationUtil - Comprehensive Input Validation Utility
 * 
 * Provides all validation methods for user inputs including usernames,
 * passwords, emails, phone numbers, dates, and payment information.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class ValidationUtil {
    
    // ============ USERNAME VALIDATION ============
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.matches(Constants.REGEX_USERNAME);
    }
    
    public static String getUsernameErrorMessage(String username) {
        if (username == null || username.isEmpty()) {
            return "Username cannot be empty";
        }
        if (username.length() < Constants.USERNAME_MIN_LENGTH) {
            return "Username must be at least " + Constants.USERNAME_MIN_LENGTH + " characters";
        }
        if (username.length() > Constants.USERNAME_MAX_LENGTH) {
            return "Username must not exceed " + Constants.USERNAME_MAX_LENGTH + " characters";
        }
        if (!username.matches(Constants.REGEX_USERNAME)) {
            return "Username can only contain letters, numbers, and underscores";
        }
        return "";
    }
    
    // ============ PASSWORD VALIDATION ============
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return isStrongPassword(password);
    }
    
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < Constants.PASSWORD_MIN_LENGTH) {
            return false;
        }
        
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        
        return hasUppercase && hasLowercase && hasDigit;
    }
    
    public static String getPasswordErrorMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        if (password.length() < Constants.PASSWORD_MIN_LENGTH) {
            return "Password must be at least " + Constants.PASSWORD_MIN_LENGTH + " characters";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }
        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one number";
        }
        return "";
    }
    
    // ============ EMAIL VALIDATION ============
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(Constants.REGEX_EMAIL);
    }
    
    public static String getEmailErrorMessage(String email) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }
        if (!isValidEmail(email)) {
            return "Please enter a valid email address";
        }
        return "";
    }
    
    // ============ PHONE NUMBER VALIDATION ============
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        return phoneNumber.matches(Constants.REGEX_PHONE);
    }
    
    public static String getPhoneNumberErrorMessage(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "Phone number cannot be empty";
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            return "Phone number must be in format: 09XXXXXXXXX or +639XXXXXXXXX";
        }
        return "";
    }
    
    // ============ NAME VALIDATION ============
    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return name.length() >= 2 && name.length() <= 50 && name.matches(Constants.REGEX_LETTERS_ONLY);
    }
    
    public static String getNameErrorMessage(String name) {
        if (name == null || name.isEmpty()) {
            return "Name cannot be empty";
        }
        if (name.length() < 2) {
            return "Name must be at least 2 characters";
        }
        if (name.length() > 50) {
            return "Name must not exceed 50 characters";
        }
        if (!name.matches(Constants.REGEX_LETTERS_ONLY)) {
            return "Name can only contain letters and spaces";
        }
        return "";
    }
    
    // ============ DATE VALIDATION ============
    public static boolean isValidDate(LocalDate date) {
        return date != null;
    }
    
    public static boolean isValidDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return false;
        }
        return checkInDate.isBefore(checkOutDate);
    }
    
    public static String getDateRangeErrorMessage(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null) {
            return "Check-in date cannot be empty";
        }
        if (checkOutDate == null) {
            return "Check-out date cannot be empty";
        }
        if (checkInDate.isBefore(LocalDate.now())) {
            return "Check-in date cannot be in the past";
        }
        if (checkOutDate.isBefore(LocalDate.now())) {
            return "Check-out date cannot be in the past";
        }
        if (!checkInDate.isBefore(checkOutDate)) {
            return "Check-out date must be after check-in date";
        }
        return "";
    }
    
    // ============ NUMBER OF GUESTS VALIDATION ============
    public static boolean isValidNumberOfGuests(int numberOfGuests, int roomCapacity) {
        return numberOfGuests >= 1 && numberOfGuests <= roomCapacity;
    }
    
    public static String getNumberOfGuestsErrorMessage(int numberOfGuests, int roomCapacity) {
        if (numberOfGuests < 1) {
            return "Number of guests must be at least 1";
        }
        if (numberOfGuests > roomCapacity) {
            return "Number of guests cannot exceed room capacity of " + roomCapacity;
        }
        return "";
    }
    
    // ============ PAYMENT VALIDATION ============
    public static boolean isValidPaymentAmount(double amount) {
        return amount > 0 && amount <= 999999.99;
    }
    
    public static String getPaymentAmountErrorMessage(double amount) {
        if (amount <= 0) {
            return "Payment amount must be greater than 0";
        }
        if (amount > 999999.99) {
            return "Payment amount exceeds maximum limit";
        }
        return "";
    }
    
    // ============ CREDIT CARD VALIDATION ============
    public static boolean isValidCreditCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        
        String cleaned = cardNumber.replaceAll("\\s+", "");
        if (cleaned.length() < Constants.CREDIT_CARD_MIN_LENGTH || 
            cleaned.length() > Constants.CREDIT_CARD_MAX_LENGTH) {
            return false;
        }
        
        if (!cleaned.matches("\\d+")) {
            return false;
        }
        
        return luhnAlgorithm(cleaned);
    }
    
    private static boolean luhnAlgorithm(String cardNumber) {
        int sum = 0;
        boolean isSecond = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (isSecond) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            
            sum += digit;
            isSecond = !isSecond;
        }
        
        return sum % 10 == 0;
    }
    
    public static String getCreditCardErrorMessage(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return "Card number cannot be empty";
        }
        
        String cleaned = cardNumber.replaceAll("\\s+", "");
        if (cleaned.length() < Constants.CREDIT_CARD_MIN_LENGTH) {
            return "Card number is too short";
        }
        if (cleaned.length() > Constants.CREDIT_CARD_MAX_LENGTH) {
            return "Card number is too long";
        }
        if (!cleaned.matches("\\d+")) {
            return "Card number must contain only digits";
        }
        if (!luhnAlgorithm(cleaned)) {
            return "Invalid card number (Luhn check failed)";
        }
        return "";
    }
    
    // ============ CVV VALIDATION ============
    public static boolean isValidCVV(String cvv) {
        if (cvv == null || cvv.isEmpty()) {
            return false;
        }
        return cvv.length() >= Constants.CVV_LENGTH_MIN && 
               cvv.length() <= Constants.CVV_LENGTH_MAX && 
               cvv.matches("\\d+");
    }
    
    public static String getCVVErrorMessage(String cvv) {
        if (cvv == null || cvv.isEmpty()) {
            return "CVV cannot be empty";
        }
        if (cvv.length() < Constants.CVV_LENGTH_MIN || cvv.length() > Constants.CVV_LENGTH_MAX) {
            return "CVV must be " + Constants.CVV_LENGTH_MIN + " to " + Constants.CVV_LENGTH_MAX + " digits";
        }
        if (!cvv.matches("\\d+")) {
            return "CVV must contain only numbers";
        }
        return "";
    }
    
    // ============ OTP VALIDATION ============
    public static boolean isValidOTP(String otp) {
        if (otp == null || otp.isEmpty()) {
            return false;
        }
        return otp.length() == Constants.OTP_LENGTH && otp.matches("\\d+");
    }
    
    // ============ ID DOCUMENT VALIDATION ============
    public static boolean isValidIDDocument(String idNumber) {
        if (idNumber == null || idNumber.isEmpty()) {
            return false;
        }
        return idNumber.length() >= Constants.ID_DOCUMENT_MIN_LENGTH;
    }
    
    // ============ ADDRESS VALIDATION ============
    public static boolean isValidAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }
        return address.length() <= Constants.ADDRESS_MAX_LENGTH;
    }
}
