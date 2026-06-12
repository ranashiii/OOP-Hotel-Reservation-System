package Utilities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

/**
 * ValidationUtil - Comprehensive Input Validation Utility
 * 
 * Provides validation methods for all system inputs including usernames,
 * passwords, emails, phone numbers, dates, and other business data.
 * All validations follow the business requirements from the specification.
 * 
 * Features:
 * - Username and password validation with strength checking
 * - Email and phone number format validation
 * - Name validation (letters only, proper length)
 * - Date validation with range checking
 * - Payment and currency validation
 * - Credit card validation with Luhn algorithm support
 * - ID document validation
 * - Room and reservation validation
 * 
 * @author Hotel Reservation System Team
 * @version 2.0.0
 */
public class ValidationUtil {
    
    /**
     * Validate username format
     * Requirements:
     * - 5-20 characters length
     * - Alphanumeric (letters, numbers, underscore only)
     * - No spaces or special characters
     * 
     * @param username the username to validate
     * @return true if username is valid
     */
    public static boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return username.matches(Constants.REGEX_USERNAME);
    }
    
    /**
     * Validate password strength
     * Requirements:
     * - Minimum 8 characters
     * - Must contain uppercase letter (A-Z)
     * - Must contain lowercase letter (a-z)
     * - Must contain number (0-9)
     * 
     * @param password the password to validate
     * @return true if password meets strength requirements
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
     * Validate that two passwords match
     * 
     * @param password the password
     * @param confirmPassword the confirmation password
     * @return true if passwords match exactly
     */
    public static boolean validatePasswordMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
    
    /**
     * Validate email format
     * Must follow standard email format: user@domain.com
     * 
     * @param email the email to validate
     * @return true if email format is valid
     */
    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches(Constants.REGEX_EMAIL);
    }
    
    /**
     * Validate name (first name, last name, middle name)
     * Requirements:
     * - 2-50 characters
     * - Letters only (no numbers or special characters)
     * - Spaces allowed between names
     * 
     * @param name the name to validate
     * @return true if name is valid
     */
    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        if (name.length() < Constants.FIRST_NAME_MIN_LENGTH || 
            name.length() > Constants.FIRST_NAME_MAX_LENGTH) {
            return false;
        }
        
        // Allow letters and spaces, but not starting/ending with space
        if (name.startsWith(" ") || name.endsWith(" ")) {
            return false;
        }
        
        return name.matches("^[a-zA-Z\\s]+$");
    }
    
    /**
     * Validate Philippine phone number format
     * Requirements:
     * - Format: 09XXXXXXXXX (11 digits starting with 09)
     * - Format: +639XXXXXXXXX (12 digits with +63 prefix)
     * 
     * @param phoneNumber the phone number to validate
     * @return true if phone number is valid Philippine format
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return phoneNumber.matches(Constants.REGEX_PHONE);
    }
    
    /**
     * Validate date of birth
     * Requirements:
     * - Must be valid date (not in future)
     * - Must be at least 18 years old
     * 
     * @param dateOfBirth the date of birth to validate
     * @return true if date of birth is valid and guest is 18+ years old
     */
    public static boolean validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        
        // Must not be in the future
        if (dateOfBirth.isAfter(LocalDate.now())) {
            return false;
        }
        
        // Must be at least 18 years old
        LocalDate eighteenYearsAgo = LocalDate.now().minus(Period.ofYears(18));
        return !dateOfBirth.isAfter(eighteenYearsAgo);
    }
    
    /**
     * Validate address
     * Requirements:
     * - Not empty
     * - Maximum 255 characters
     * 
     * @param address the address to validate
     * @return true if address is valid
     */
    public static boolean validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return address.length() <= Constants.ADDRESS_MAX_LENGTH;
    }
    
    /**
     * Validate check-in and check-out date range
     * Requirements:
     * - Check-in date cannot be in the past
     * - Check-out date must be after check-in date
     * 
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if date range is valid
     */
    public static boolean validateDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return false;
        }
        
        // Check-in cannot be in the past
        if (checkInDate.isBefore(LocalDate.now())) {
            return false;
        }
        
        // Check-out must be after check-in
        return checkOutDate.isAfter(checkInDate);
    }
    
    /**
     * Validate payment amount
     * Requirements:
     * - Greater than 0
     * - Not exceeding 999,999.99
     * 
     * @param amount the payment amount
     * @return true if amount is valid
     */
    public static boolean validatePaymentAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal max = new BigDecimal("999999.99");
        
        return amount.compareTo(zero) > 0 && amount.compareTo(max) <= 0;
    }
    
    /**
     * Validate payment amount (double variant)
     * 
     * @param amount the payment amount
     * @return true if amount is valid
     */
    public static boolean validatePaymentAmount(double amount) {
        return amount > 0 && amount <= 999999.99;
    }
    
    /**
     * Validate credit card number
     * Requirements:
     * - 13-19 digits
     * - Numeric only (spaces allowed for formatting)
     * - Should pass Luhn algorithm validation
     * 
     * @param cardNumber the credit card number
     * @return true if card number format is valid
     */
    public static boolean validateCreditCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        
        String cleaned = cardNumber.replaceAll("\\s", "");
        
        // Check length (13-19 digits)
        if (cleaned.length() < Constants.CREDIT_CARD_MIN_LENGTH || 
            cleaned.length() > Constants.CREDIT_CARD_MAX_LENGTH) {
            return false;
        }
        
        // Check if numeric only
        if (!cleaned.matches("^[0-9]+$")) {
            return false;
        }
        
        // Validate with Luhn algorithm
        return validateLuhn(cleaned);
    }
    
    /**
     * Validate credit card expiry date
     * Requirements:
     * - Format: MM/YY or MM/YYYY
     * - Month must be 01-12
     * - Year must be current year or future
     * - Must not be expired
     * 
     * @param expiryDate the expiry date (format: MM/YY or MM/YYYY)
     * @return true if expiry date is valid and not expired
     */
    public static boolean validateCreditCardExpiry(String expiryDate) {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }
        
        String[] parts = expiryDate.split("/");
        if (parts.length != 2) {
            return false;
        }
        
        try {
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);
            
            // Validate month
            if (month < 1 || month > 12) {
                return false;
            }
            
            // Convert 2-digit year to 4-digit
            if (year < 100) {
                year += 2000;
            }
            
            // Check if not expired
            int currentYear = LocalDate.now().getYear();
            int currentMonth = LocalDate.now().getMonthValue();
            
            if (year < currentYear) {
                return false;
            }
            
            if (year == currentYear && month < currentMonth) {
                return false;
            }
            
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate CVV (Card Verification Value)
     * Requirements:
     * - 3-4 digits
     * - Numeric only
     * 
     * @param cvv the CVV to validate
     * @return true if CVV is valid
     */
    public static boolean validateCVV(String cvv) {
        if (cvv == null || cvv.isEmpty()) {
            return false;
        }
        return cvv.matches("^[0-9]{3,4}$");
    }
    
    /**
     * Validate cardholder name
     * Requirements:
     * - Not empty
     * - Letters and spaces only
     * 
     * @param cardholderName the cardholder name
     * @return true if name is valid
     */
    public static boolean validateCardholderName(String cardholderName) {
        if (cardholderName == null || cardholderName.trim().isEmpty()) {
            return false;
        }
        return cardholderName.matches("^[a-zA-Z\\s]+$");
    }
    
    /**
     * Validate OTP (One-Time Password)
     * Requirements:
     * - Exactly 6 digits
     * - Numeric only
     * 
     * @param otp the OTP to validate
     * @return true if OTP is valid
     */
    public static boolean validateOTP(String otp) {
        if (otp == null || otp.isEmpty()) {
            return false;
        }
        return otp.matches("^[0-9]{6}$");
    }
    
    /**
     * Validate room capacity
     * Requirements:
     * - Between 1 and 10 guests
     * 
     * @param capacity the room capacity
     * @return true if capacity is valid
     */
    public static boolean validateRoomCapacity(int capacity) {
        return capacity >= Constants.MIN_ROOM_CAPACITY && 
               capacity <= Constants.MAX_ROOM_CAPACITY;
    }
    
    /**
     * Validate ID document number
     * Requirements:
     * - Minimum 5 characters
     * 
     * @param idNumber the ID document number
     * @return true if ID number is valid
     */
    public static boolean validateIdDocumentNumber(String idNumber) {
        if (idNumber == null || idNumber.trim().isEmpty()) {
            return false;
        }
        return idNumber.length() >= Constants.ID_DOCUMENT_MIN_LENGTH;
    }
    
    /**
     * Validate number of nights for reservation
     * Requirements:
     * - Between 1 and 365 nights
     * 
     * @param nights the number of nights
     * @return true if night count is valid
     */
    public static boolean validateNumberOfNights(int nights) {
        return nights >= Constants.MIN_STAY_NIGHTS && 
               nights <= Constants.MAX_STAY_NIGHTS;
    }
    
    /**
     * Validate number of guests in reservation
     * Requirements:
     * - At least 1 guest
     * - Not exceeding room capacity
     * 
     * @param numberOfGuests the number of guests
     * @param roomCapacity the room capacity
     * @return true if guest count is valid
     */
    public static boolean validateNumberOfGuests(int numberOfGuests, int roomCapacity) {
        return numberOfGuests >= 1 && numberOfGuests <= roomCapacity;
    }
    
    /**
     * Validate room price per night
     * Requirements:
     * - Greater than 0
     * - Not exceeding 999,999.99
     * 
     * @param pricePerNight the room price
     * @return true if price is valid
     */
    public static boolean validateRoomPrice(BigDecimal pricePerNight) {
        if (pricePerNight == null) {
            return false;
        }
        
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal max = new BigDecimal("999999.99");
        
        return pricePerNight.compareTo(zero) > 0 && pricePerNight.compareTo(max) <= 0;
    }
    
    /**
     * Validate room number format
     * Requirements:
     * - Not empty
     * - Alphanumeric (letters and numbers only)
     * 
     * @param roomNumber the room number
     * @return true if room number is valid
     */
    public static boolean validateRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return false;
        }
        return roomNumber.matches("^[a-zA-Z0-9]+$");
    }
    
    /**
     * Validate room type
     * Requirements:
     * - Must be one of the valid room types
     * 
     * @param roomType the room type to validate
     * @return true if room type is valid
     */
    public static boolean validateRoomType(String roomType) {
        if (roomType == null || roomType.trim().isEmpty()) {
            return false;
        }
        
        return roomType.equals(Constants.ROOM_TYPE_SINGLE) ||
               roomType.equals(Constants.ROOM_TYPE_DOUBLE) ||
               roomType.equals(Constants.ROOM_TYPE_DOUBLE_DELUXE) ||
               roomType.equals(Constants.ROOM_TYPE_SUITE);
    }
    
    /**
     * Validate room status
     * Requirements:
     * - Must be one of the valid statuses
     * 
     * @param status the room status to validate
     * @return true if status is valid
     */
    public static boolean validateRoomStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        
        return status.equals(Constants.ROOM_STATUS_AVAILABLE) ||
               status.equals(Constants.ROOM_STATUS_OCCUPIED) ||
               status.equals(Constants.ROOM_STATUS_MAINTENANCE) ||
               status.equals(Constants.ROOM_STATUS_CLEANING);
    }
    
    /**
     * Validate reservation status
     * Requirements:
     * - Must be one of the valid statuses
     * 
     * @param status the reservation status to validate
     * @return true if status is valid
     */
    public static boolean validateReservationStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }
        
        return status.equals(Constants.RESERVATION_STATUS_CONFIRMED) ||
               status.equals(Constants.RESERVATION_STATUS_CHECKED_IN) ||
               status.equals(Constants.RESERVATION_STATUS_CHECKED_OUT) ||
               status.equals(Constants.RESERVATION_STATUS_CANCELLED);
    }
    
    /**
     * Validate payment method
     * Requirements:
     * - Must be one of the valid payment methods
     * 
     * @param paymentMethod the payment method to validate
     * @return true if payment method is valid
     */
    public static boolean validatePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return false;
        }
        
        return paymentMethod.equals(Constants.PAYMENT_METHOD_CASH) ||
               paymentMethod.equals(Constants.PAYMENT_METHOD_CREDIT_CARD) ||
               paymentMethod.equals(Constants.PAYMENT_METHOD_E_WALLET);
    }
    
    /**
     * Validate Luhn algorithm for credit card numbers
     * Luhn algorithm is the industry standard for credit card validation
     * 
     * @param cardNumber the card number (digits only)
     * @return true if card passes Luhn check
     */
    private static boolean validateLuhn(String cardNumber) {
        int sum = 0;
        boolean isEvenPosition = false;
        
        // Process digits from right to left
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (isEvenPosition) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            
            sum += digit;
            isEvenPosition = !isEvenPosition;
        }
        
        return sum % 10 == 0;
    }
}
