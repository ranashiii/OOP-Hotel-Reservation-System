package Utilities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * DateUtil - Date and Time Calculation Utility
 * 
 * Provides methods for date calculations, refund policy calculations,
 * late checkout fee calculations, and date validations.
 */
public class DateUtil {
    
    /**
     * Calculate number of nights between check-in and check-out
     */
    public static long calculateNights(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    /**
     * Check if cancellation is within refund window (7 days before check-in)
     */
    public static boolean isWithinRefundWindow(LocalDate checkInDate, LocalDate cancellationDate) {
        if (checkInDate == null || cancellationDate == null) {
            return false;
        }
        
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(cancellationDate, checkInDate);
        return daysBeforeCheckIn > 0;
    }
    
    /**
     * Calculate refund percentage based on cancellation date
     * More than 7 days before: 100%
     * Exactly 7 days before: 90%
     * Less than 7 days before: 0%
     */
    public static double getRefundPercentage(LocalDate checkInDate, LocalDate cancellationDate) {
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(cancellationDate, checkInDate);
        
        if (daysBeforeCheckIn > Constants.REFUND_FULL_DAYS) {
            return 1.0;
        } else if (daysBeforeCheckIn == Constants.REFUND_PARTIAL_DAYS) {
            return Constants.REFUND_PARTIAL_PERCENT;
        }
        
        return 0.0;
    }
    
    /**
     * Calculate date difference in days
     */
    public static long dateDifference(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        return ChronoUnit.DAYS.between(date1, date2);
    }
    
    /**
     * Validate date range (check-in before check-out)
     */
    public static boolean isValidDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return false;
        }
        
        return checkOutDate.isAfter(checkInDate);
    }
    
    /**
     * Check if date is in the past
     */
    public static boolean isInPast(LocalDate date) {
        if (date == null) {
            return true;
        }
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.equals(LocalDate.now());
    }
    
    /**
     * Check if time is after standard checkout time (11 AM)
     */
    public static boolean isLateCheckout(LocalTime checkoutTime) {
        if (checkoutTime == null) {
            return false;
        }
        return checkoutTime.getHour() >= Constants.STANDARD_CHECKOUT_HOUR;
    }
    
    /**
     * Calculate late checkout fee based on checkout time
     * 1-2 hours late: 500 PHP
     * 2-4 hours late: 1000 PHP
     * Over 4 hours: 2000 PHP
     */
    public static double calculateLateCheckoutFee(LocalTime checkoutTime) {
        if (checkoutTime == null) {
            return 0.0;
        }
        
        int standardCheckoutHour = Constants.STANDARD_CHECKOUT_HOUR;
        int checkoutHour = checkoutTime.getHour();
        
        if (checkoutHour <= standardCheckoutHour) {
            return 0.0;
        }
        
        int hoursLate = checkoutHour - standardCheckoutHour;
        
        if (hoursLate <= 2) {
            return Constants.LATE_CHECKOUT_FEE_1_2_HOURS;
        } else if (hoursLate <= 4) {
            return Constants.LATE_CHECKOUT_FEE_2_4_HOURS;
        } else {
            return Constants.LATE_CHECKOUT_FEE_4_PLUS_HOURS;
        }
    }
}
