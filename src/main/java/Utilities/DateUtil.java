package Utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * DateUtil - Date and Time Utility Functions
 * 
 * Provides utility methods for date/time calculations and operations.
 * Used for reservation date handling, night calculations, and checkout fee calculations.
 * 
 * Features:
 * - Calculate number of nights between dates
 * - Calculate late checkout fees based on time
 * - Check if date is within refund window
 * - Calculate refund percentages
 * - Validate date ranges
 * - Date difference calculations
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class DateUtil {
    
    /**
     * Calculate the number of nights between check-in and check-out dates
     * Includes the check-in day but excludes the check-out day (standard hotel practice)
     * 
     * Example:
     * Check-in: 2024-01-15, Check-out: 2024-01-18 = 3 nights
     * 
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return number of nights (minimum 1)
     * @throws IllegalArgumentException if checkOutDate is not after checkInDate
     */
    public static int calculateNights(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return (int) nights;
    }
    
    /**
     * Calculate late checkout fee based on checkout time
     * 
     * Fee Structure:
     * - 1-2 hours late: PHP 500
     * - 2-4 hours late: PHP 1,000
     * - Over 4 hours: PHP 2,000 or charge for extra night
     * 
     * @param checkOutTime the actual checkout time
     * @param standardCheckoutTime the standard checkout time (default 11:00 AM)
     * @return late checkout fee in PHP (0 if not late)
     */
    public static double calculateLateCheckoutFee(LocalTime checkOutTime, LocalTime standardCheckoutTime) {
        if (checkOutTime == null || standardCheckoutTime == null) {
            return 0.0;
        }
        
        // If checkout is not late, return 0
        if (!checkOutTime.isAfter(standardCheckoutTime)) {
            return 0.0;
        }
        
        // Calculate minutes late
        long minutesLate = ChronoUnit.MINUTES.between(standardCheckoutTime, checkOutTime);
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
     * Check if cancellation date is within refund window
     * 
     * Refund Policy:
     * - More than 7 days before check-in: 100% refund
     * - Exactly 7 days before check-in: 90% refund
     * - Less than 7 days before check-in: No refund
     * - After check-in: No refund
     * 
     * @param cancellationDate the date the reservation was cancelled
     * @param checkInDate the scheduled check-in date
     * @return number of days between cancellation and check-in
     */
    public static int daysUntilCheckIn(LocalDate cancellationDate, LocalDate checkInDate) {
        if (cancellationDate == null || checkInDate == null) {
            return 0;
        }
        
        long days = ChronoUnit.DAYS.between(cancellationDate, checkInDate);
        return (int) days;
    }
    
    /**
     * Get refund percentage based on cancellation date
     * 
     * Refund Policy:
     * - More than 7 days before check-in: 100% refund
     * - Exactly 7 days before check-in: 90% refund
     * - Less than 7 days before check-in: 0% refund
     * - After check-in: 0% refund
     * 
     * @param cancellationDate the date the reservation was cancelled
     * @param checkInDate the scheduled check-in date
     * @return refund percentage (0.0 to 1.0)
     */
    public static double getRefundPercentage(LocalDate cancellationDate, LocalDate checkInDate) {
        int daysBeforeCheckIn = daysUntilCheckIn(cancellationDate, checkInDate);
        
        if (daysBeforeCheckIn > Constants.REFUND_FULL_DAYS) {
            // More than 7 days before: 100% refund
            return 1.0;
        } else if (daysBeforeCheckIn == Constants.REFUND_PARTIAL_DAYS) {
            // Exactly 7 days before: 90% refund
            return Constants.REFUND_PARTIAL_PERCENT;
        } else if (daysBeforeCheckIn > 0) {
            // Less than 7 days but still before check-in: No refund
            return 0.0;
        } else {
            // On or after check-in: No refund
            return 0.0;
        }
    }
    
    /**
     * Check if a date range is valid
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return true if endDate is after startDate, false otherwise
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return endDate.isAfter(startDate);
    }
    
    /**
     * Check if a date is in the past
     * 
     * @param date the date to check
     * @return true if date is before today, false otherwise
     */
    public static boolean isInPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if a date is today or in the future
     * 
     * @param date the date to check
     * @return true if date is today or after today, false otherwise
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(LocalDate.now());
    }
    
    /**
     * Get date difference in days
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return number of days between dates (negative if endDate is before startDate)
     */
    public static int getDateDifferenceDays(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) days;
    }
    
    /**
     * Get date difference in hours
     * 
     * @param startDateTime the start date-time
     * @param endDateTime the end date-time
     * @return number of hours between date-times
     */
    public static int getDateTimeDifferenceHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        long hours = ChronoUnit.HOURS.between(startDateTime, endDateTime);
        return (int) hours;
    }
    
    /**
     * Check if guest is at least 18 years old based on date of birth
     * 
     * @param dateOfBirth the guest's date of birth
     * @return true if guest is at least 18 years old, false otherwise
     */
    public static boolean isAtLeast18YearsOld(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        
        LocalDate today = LocalDate.now();
        LocalDate eighteenYearsAgo = today.minusYears(18);
        
        return !dateOfBirth.isAfter(eighteenYearsAgo);
    }
    
    /**
     * Get current date
     * 
     * @return today's date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Get current date and time
     * 
     * @return current date and time
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
