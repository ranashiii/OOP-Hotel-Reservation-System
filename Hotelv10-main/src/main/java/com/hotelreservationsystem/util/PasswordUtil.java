package com.hotelreservationsystem.util;

/**
 * PasswordUtil - Password Hashing and Verification Utility
 * 
 * Provides methods for password hashing and verification using BCrypt-style
 * simple hashing (for educational purposes). In production, use proper
 * encryption libraries.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class PasswordUtil {
    
    /**
     * Hash password using simple algorithm (for demonstration)
     * Note: In production, use BCrypt or Argon2
     * 
     * @param password the plain text password
     * @return hashed password
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        
        // Simple hashing for demonstration
        // In production, use: BCryptPasswordEncoder or similar
        int hash = 0;
        for (int i = 0; i < password.length(); i++) {
            hash = ((hash << 5) - hash) + password.charAt(i);
            hash = hash & hash;
        }
        
        return Integer.toHexString(hash);
    }
    
    /**
     * Verify password against hash
     * 
     * @param password the plain text password to verify
     * @param hash the stored hash
     * @return true if password matches hash
     */
    public static boolean verifyPassword(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }
        
        String passwordHash = hashPassword(password);
        return passwordHash.equals(hash);
    }
    
    /**
     * Check if password meets strength requirements
     * 
     * @param password the password to validate
     * @return true if password is strong enough
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < Constants.PASSWORD_MIN_LENGTH) {
            return false;
        }
        
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        
        return hasUppercase && hasLowercase && hasDigit;
    }
    
    /**
     * Get password strength message
     * 
     * @param password the password to evaluate
     * @return strength message
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        
        if (password.length() < Constants.PASSWORD_MIN_LENGTH) {
            return "Password must be at least " + Constants.PASSWORD_MIN_LENGTH + " characters";
        }
        
        int strength = 0;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[0-9].*")) strength++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':'',./<>?].*")) strength++;
        
        switch (strength) {
            case 1: return "Weak password";
            case 2: return "Fair password";
            case 3: return "Good password";
            case 4: return "Strong password";
            default: return "Invalid password";
        }
    }
}
