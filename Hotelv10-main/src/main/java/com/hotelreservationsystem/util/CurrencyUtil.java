package com.hotelreservationsystem.util;

import java.text.DecimalFormat;

/**
 * CurrencyUtil - Currency Formatting and Calculation Utility
 * 
 * Provides methods for currency formatting, currency calculations including
 * tax calculations, subtotals, and total price calculations.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class CurrencyUtil {
    
    private static final DecimalFormat currencyFormatter = new DecimalFormat("0.00");
    
    /**
     * Format currency value for display
     * 
     * @param amount the amount to format
     * @return formatted currency string (e.g., "PHP 1,234.56")
     */
    public static String formatCurrency(double amount) {
        String formatted = currencyFormatter.format(amount);
        return Constants.CURRENCY_SYMBOL + " " + formatted;
    }
    
    /**
     * Calculate tax on amount
     * 
     * @param subtotal the subtotal before tax
     * @return tax amount
     */
    public static double calculateTax(double subtotal) {
        return subtotal * Constants.TAX_RATE;
    }
    
    /**
     * Calculate subtotal from room rate and nights
     * 
     * @param roomRate the room rate per night
     * @param numberOfNights the number of nights
     * @return subtotal amount
     */
    public static double calculateSubtotal(double roomRate, long numberOfNights) {
        return roomRate * numberOfNights;
    }
    
    /**
     * Calculate final total with tax and discount
     * 
     * @param roomRate the room rate per night
     * @param numberOfNights the number of nights
     * @param discountApplied the discount amount (if any)
     * @return final total price
     */
    public static double calculateFinalTotal(double roomRate, long numberOfNights, double discountApplied) {
        double subtotal = calculateSubtotal(roomRate, numberOfNights);
        double tax = calculateTax(subtotal);
        return (subtotal + tax) - discountApplied;
    }
    
    /**
     * Calculate final total with tax (no discount)
     * 
     * @param roomRate the room rate per night
     * @param numberOfNights the number of nights
     * @return final total price
     */
    public static double calculateFinalTotal(double roomRate, long numberOfNights) {
        return calculateFinalTotal(roomRate, numberOfNights, 0.0);
    }
    
    /**
     * Parse currency string to double
     * 
     * @param currencyString the currency string to parse
     * @return double value
     */
    public static double parseCurrency(String currencyString) {
        try {
            if (currencyString == null || currencyString.isEmpty()) {
                return 0.0;
            }
            // Remove currency symbol and spaces
            String cleaned = currencyString.replaceAll("[^0-9.]", "");
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Round currency to 2 decimal places
     * 
     * @param amount the amount to round
     * @return rounded amount
     */
    public static double roundCurrency(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }
    
    /**
     * Check if amount is positive and valid
     * 
     * @param amount the amount to validate
     * @return true if amount is valid
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 999999.99;
    }
}
