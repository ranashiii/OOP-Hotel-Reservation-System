package com.hotelreservationsystem.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * DateUtil - Date and Time Utility Methods
 * 
 * Provides methods for date calculations, conversions, and business logic
 * including refund calculations, checkout fee calculations, and date validations.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class DateUtil {
    
    /**
     * Calculate number of nights between check-in and check-out
     * 
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return number of nights
     */
    public static long calculateNights(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    /**
     * Check if date is in the past
     * 
     * @param date the date to check
     * @return true if date is in the past
     */
    public static boolean isPastDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if date is in the future
     * 
     * @param date the date to check
     * @return true if date is in the future
     */
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Check if date is today
     * 
     * @param date the date to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isEqual(LocalDate.now());
    }
    
    /**
     * Calculate refund percentage based on days until check-in
     * 
     * @param checkInDate the check-in date
     * @return refund percentage (0.0 to 1.0)
     */
    public static double getRefundPercentage(LocalDate checkInDate) {
        if (checkInDate == null) {
            return 0.0;
        }
        
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), checkInDate);
        
        if (daysUntilCheckIn > Constants.REFUND_FULL_DAYS) {
            return 1.0; // 100% refund
        } else if (daysUntilCheckIn == Constants.REFUND_PARTIAL_DAYS) {
            return Constants.REFUND_PARTIAL_PERCENT; // 90% refund
        } else if (daysUntilCheckIn > 0) {
            return 0.0; // No refund
        } else {
            return 0.0; // Already checked in, no refund
        }
    }
    
    /**
     * Calculate late checkout fee based on checkout time
     * 
     * @param checkOutTime the actual checkout time
     * @return late checkout fee amount
     */
    public static double calculateLateCheckoutFee(LocalTime checkOutTime) {
        if (checkOutTime == null) {
            return 0.0;
        }
        
        LocalTime standardCheckout = LocalTime.of(Constants.STANDARD_CHECKOUT_HOUR, 0);
        
        if (checkOutTime.isBefore(standardCheckout) || checkOutTime.equals(standardCheckout)) {
            return 0.0; // No late fee
        }
        
        long minutesLate = ChronoUnit.MINUTES.between(standardCheckout, checkOutTime);
        long hoursLate = minutesLate / 60;
        
        if (hoursLate <= 2) {
            return Constants.LATE_CHECKOUT_FEE_1_2_HOURS;
        } else if (hoursLate <= 4) {
            return Constants.LATE_CHECKOUT_FEE_2_4_HOURS;
        } else {
            return Constants.LATE_CHECKOUT_FEE_4_PLUS_HOURS;
        }
    }
    
    /**
     * Check if date range is valid (start before end)
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return true if date range is valid
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return startDate.isBefore(endDate);
    }
    
    /**
     * Format date as string
     * 
     * @param date the date to format
     * @return formatted date string
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
        return date.format(formatter);
    }
    
    /**
     * Format date for display
     * 
     * @param date the date to format
     * @return formatted display date string
     */
    public static String formatDisplayDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DISPLAY_DATE_FORMAT);
        return date.format(formatter);
    }
    
    /**
     * Parse date string
     * 
     * @param dateString the date string to parse
     * @return LocalDate object
     */
    public static LocalDate parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
            return LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
