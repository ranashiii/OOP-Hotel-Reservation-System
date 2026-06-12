package Utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * CurrencyUtil - Currency Formatting and Calculation Utility
 * 
 * Provides methods for currency formatting, tax calculations, price calculations,
 * and money conversion for Philippine Peso (PHP).
 */
public class CurrencyUtil {
    
    /**
     * Format amount as PHP currency string
     * Example: PHP 5,000.00
     */
    public static String formatCurrency(double amount) {
        return String.format(Constants.CURRENCY_FORMAT, amount);
    }
    
    /**
     * Format BigDecimal amount as PHP currency
     */
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return formatCurrency(0.0);
        }
        return formatCurrency(amount.doubleValue());
    }
    
    /**
     * Calculate tax (12%) on given amount
     */
    public static BigDecimal calculateTax(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal taxRate = new BigDecimal(Constants.TAX_RATE);
        return amount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate tax on double amount
     */
    public static double calculateTax(double amount) {
        if (amount <= 0) {
            return 0.0;
        }
        return Math.round(amount * Constants.TAX_RATE * 100.0) / 100.0;
    }
    
    /**
     * Calculate subtotal (room rate × number of nights)
     */
    public static BigDecimal calculateSubtotal(BigDecimal roomRate, int numberOfNights) {
        if (roomRate == null || numberOfNights <= 0) {
            return BigDecimal.ZERO;
        }
        
        return roomRate.multiply(new BigDecimal(numberOfNights)).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate final total with tax
     */
    public static BigDecimal calculateFinalTotal(BigDecimal subtotal, BigDecimal discountApplied) {
        if (subtotal == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal discount = (discountApplied != null) ? discountApplied : BigDecimal.ZERO;
        BigDecimal tax = calculateTax(subtotal);
        
        return subtotal.add(tax).subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Convert string input to BigDecimal safely
     */
    public static BigDecimal convertToDecimal(String amountString) {
        if (amountString == null || amountString.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        try {
            return new BigDecimal(amountString.trim()).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Round amount to 2 decimal places
     */
    public static BigDecimal roundAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calculate change from payment
     */
    public static BigDecimal calculateChange(BigDecimal amountPaid, BigDecimal amountDue) {
        if (amountPaid == null || amountDue == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal change = amountPaid.subtract(amountDue);
        
        if (change.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        
        return change.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Check if amount is valid (positive and not exceeding max)
     */
    public static boolean isValidAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        return amount.compareTo(new BigDecimal("999999.99")) <= 0;
    }
}
