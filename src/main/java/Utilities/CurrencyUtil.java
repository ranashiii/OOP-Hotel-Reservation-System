package Utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * CurrencyUtil - Currency and Money Utility Functions
 * 
 * Provides utility methods for currency calculations and formatting.
 * All calculations use BigDecimal to ensure precision with monetary values.
 * All prices are in Philippine Pesos (PHP).
 * 
 * Features:
 * - Format currency for display
 * - Calculate subtotals
 * - Calculate taxes (12% VAT)
 * - Calculate final totals with tax
 * - Calculate refunds
 * - Convert strings to BigDecimal
 * - Validate currency amounts
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class CurrencyUtil {
    
    // Rounding mode for all calculations - HALF_UP ensures proper rounding
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final int DECIMAL_PLACES = 2;
    
    /**
     * Format currency amount for display
     * Returns formatted string like "PHP 5,000.00"
     * 
     * @param amount the amount to format
     * @return formatted currency string
     */
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return Constants.CURRENCY_SYMBOL + " 0.00";
        }
        
        // Ensure proper rounding and decimal places
        BigDecimal rounded = amount.setScale(DECIMAL_PLACES, ROUNDING_MODE);
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return Constants.CURRENCY_SYMBOL + " " + df.format(rounded);
    }
    
    /**
     * Format currency amount for display (double variant)
     * 
     * @param amount the amount to format
     * @return formatted currency string
     */
    public static String formatCurrency(double amount) {
        return formatCurrency(new BigDecimal(amount));
    }
    
    /**
     * Format currency without symbol
     * Returns formatted string like "5,000.00"
     * 
     * @param amount the amount to format
     * @return formatted number string
     */
    public static String formatCurrencyAmount(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        
        BigDecimal rounded = amount.setScale(DECIMAL_PLACES, ROUNDING_MODE);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(rounded);
    }
    
    /**
     * Calculate subtotal (room rate × number of nights)
     * 
     * Example:
     * Room rate: PHP 5,000 per night
     * Number of nights: 5
     * Subtotal: PHP 25,000
     * 
     * @param roomRate the price per night
     * @param numberOfNights the number of nights
     * @return subtotal amount
     */
    public static BigDecimal calculateSubtotal(BigDecimal roomRate, int numberOfNights) {
        if (roomRate == null || numberOfNights < 1) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal nights = new BigDecimal(numberOfNights);
        return roomRate.multiply(nights).setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Calculate subtotal (double variant)
     * 
     * @param roomRate the price per night
     * @param numberOfNights the number of nights
     * @return subtotal amount
     */
    public static BigDecimal calculateSubtotal(double roomRate, int numberOfNights) {
        return calculateSubtotal(new BigDecimal(roomRate), numberOfNights);
    }
    
    /**
     * Calculate tax amount (12% of subtotal)
     * 
     * Tax Formula:
     * tax = subtotal × 0.12
     * 
     * Example:
     * Subtotal: PHP 25,000
     * Tax (12%): PHP 3,000
     * 
     * @param subtotal the subtotal amount
     * @return tax amount
     */
    public static BigDecimal calculateTax(BigDecimal subtotal) {
        if (subtotal == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal taxRate = new BigDecimal(Constants.TAX_RATE);
        return subtotal.multiply(taxRate).setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Calculate final total (subtotal + tax - discount)
     * 
     * Formula:
     * finalTotal = (subtotal + tax) - discount
     * 
     * Example:
     * Subtotal: PHP 25,000
     * Tax (12%): PHP 3,000
     * Discount: PHP 0
     * Final Total: PHP 28,000
     * 
     * @param subtotal the subtotal amount
     * @param discount the discount amount (can be 0)
     * @return final total amount
     */
    public static BigDecimal calculateFinalTotal(BigDecimal subtotal, BigDecimal discount) {
        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }
        
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }
        
        BigDecimal tax = calculateTax(subtotal);
        BigDecimal total = subtotal.add(tax).subtract(discount);
        
        return total.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Calculate final total without discount
     * 
     * @param subtotal the subtotal amount
     * @return final total amount
     */
    public static BigDecimal calculateFinalTotal(BigDecimal subtotal) {
        return calculateFinalTotal(subtotal, BigDecimal.ZERO);
    }
    
    /**
     * Calculate refund amount based on refund percentage
     * 
     * Formula:
     * refund = totalPrice × refundPercentage
     * 
     * Example:
     * Original price: PHP 28,000
     * Refund percentage: 100% (1.0)
     * Refund amount: PHP 28,000
     * 
     * Example:
     * Original price: PHP 28,000
     * Refund percentage: 90% (0.9)
     * Refund amount: PHP 25,200
     * 
     * @param totalPrice the original reservation total
     * @param refundPercentage the refund percentage (0.0 to 1.0)
     * @return refund amount
     */
    public static BigDecimal calculateRefund(BigDecimal totalPrice, double refundPercentage) {
        if (totalPrice == null || refundPercentage < 0 || refundPercentage > 1.0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal percentage = new BigDecimal(refundPercentage);
        return totalPrice.multiply(percentage).setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Calculate change amount
     * 
     * Formula:
     * change = paymentAmount - totalDue
     * 
     * @param paymentAmount the amount paid
     * @param totalDue the total amount due
     * @return change amount (0 if no change)
     */
    public static BigDecimal calculateChange(BigDecimal paymentAmount, BigDecimal totalDue) {
        if (paymentAmount == null || totalDue == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal change = paymentAmount.subtract(totalDue);
        
        // Return 0 if change is negative (underpayment)
        if (change.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        
        return change.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Calculate balance due
     * 
     * Formula:
     * balance = totalDue - amountPaid
     * 
     * @param totalDue the total amount due
     * @param amountPaid the amount already paid
     * @return balance remaining (0 if fully paid)
     */
    public static BigDecimal calculateBalance(BigDecimal totalDue, BigDecimal amountPaid) {
        if (totalDue == null || amountPaid == null) {
            return totalDue != null ? totalDue : BigDecimal.ZERO;
        }
        
        BigDecimal balance = totalDue.subtract(amountPaid);
        
        // Return 0 if balance is negative (overpaid)
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        
        return balance.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Convert string to BigDecimal
     * Validates and converts input string to BigDecimal
     * 
     * @param value the string value to convert
     * @return BigDecimal value, or BigDecimal.ZERO if invalid
     */
    public static BigDecimal convertToDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        try {
            // Remove commas if present
            String cleanValue = value.replace(",", "").trim();
            BigDecimal decimal = new BigDecimal(cleanValue);
            return decimal.setScale(DECIMAL_PLACES, ROUNDING_MODE);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Check if amount is valid (positive and not exceeding maximum)
     * 
     * @param amount the amount to validate
     * @return true if amount is valid (> 0 and <= 999999.99)
     */
    public static boolean isValidAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal maxAmount = new BigDecimal("999999.99");
        
        return amount.compareTo(zero) > 0 && amount.compareTo(maxAmount) <= 0;
    }
    
    /**
     * Check if payment amount is sufficient to cover due amount
     * 
     * @param paymentAmount the amount paid
     * @param dueAmount the amount due
     * @return true if payment covers the due amount
     */
    public static boolean isPaymentSufficient(BigDecimal paymentAmount, BigDecimal dueAmount) {
        if (paymentAmount == null || dueAmount == null) {
            return false;
        }
        
        return paymentAmount.compareTo(dueAmount) >= 0;
    }
    
    /**
     * Calculate total revenue for multiple reservation totals
     * 
     * @param amounts list of amounts (can be passed as varargs)
     * @return sum of all amounts
     */
    public static BigDecimal calculateTotalRevenue(BigDecimal... amounts) {
        BigDecimal total = BigDecimal.ZERO;
        
        if (amounts == null) {
            return total;
        }
        
        for (BigDecimal amount : amounts) {
            if (amount != null) {
                total = total.add(amount);
            }
        }
        
        return total.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Calculate discount amount based on percentage
     * 
     * Formula:
     * discount = subtotal × discountPercentage
     * 
     * @param subtotal the subtotal amount
     * @param discountPercentage the discount percentage (0.0 to 1.0)
     * @return discount amount
     */
    public static BigDecimal calculateDiscount(BigDecimal subtotal, double discountPercentage) {
        if (subtotal == null || discountPercentage < 0 || discountPercentage > 1.0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal percentage = new BigDecimal(discountPercentage);
        return subtotal.multiply(percentage).setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Round amount to nearest centavo
     * 
     * @param amount the amount to round
     * @return rounded amount
     */
    public static BigDecimal roundAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        
        return amount.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
    
    /**
     * Compare two currency amounts
     * 
     * @param amount1 first amount
     * @param amount2 second amount
     * @return 0 if equal, -1 if amount1 < amount2, 1 if amount1 > amount2
     */
    public static int compareCurrency(BigDecimal amount1, BigDecimal amount2) {
        if (amount1 == null || amount2 == null) {
            return 0;
        }
        
        return amount1.compareTo(amount2);
    }
}
