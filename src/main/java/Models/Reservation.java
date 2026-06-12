package Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Reservation - Reservation Model Class
 * 
 * Represents a hotel room reservation with guest, room, dates, pricing,
 * and status tracking. Core model for the reservation system.
 * 
 * Properties include guest/room IDs, dates, times, pricing, status, and timestamps.
 * Supports complete reservation lifecycle from creation to cancellation.
 * 
 * @author Hotel Reservation System Team
 * @version 2.0.0
 */
public class Reservation {
    
    private int reservationId;
    private int guestId;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private int numberOfGuests;
    private String reservationStatus;
    private int numberOfNights;
    private BigDecimal roomRate;
    private BigDecimal totalPrice;
    private BigDecimal discountApplied;
    private BigDecimal finalTotal;
    private LocalDate reservationDate;
    private String notes;
    private LocalDateTime cancelledDate;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Reservation() {
    }
    
    /**
     * Constructor with essential reservation information
     * 
     * @param guestId the guest ID
     * @param roomId the room ID
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @param numberOfGuests the number of guests
     */
    public Reservation(int guestId, int roomId, LocalDate checkInDate, 
                       LocalDate checkOutDate, int numberOfGuests) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.reservationStatus = "Confirmed";
        this.discountApplied = BigDecimal.ZERO;
        this.reservationDate = LocalDate.now();
    }
    
    /**
     * Constructor with complete reservation details
     * 
     * @param guestId the guest ID
     * @param roomId the room ID
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @param numberOfGuests the number of guests
     * @param numberOfNights the number of nights
     * @param roomRate the room rate per night
     * @param totalPrice the total price before tax
     * @param finalTotal the final total after tax and discount
     */
    public Reservation(int guestId, int roomId, LocalDate checkInDate, LocalDate checkOutDate,
                       int numberOfGuests, int numberOfNights, BigDecimal roomRate,
                       BigDecimal totalPrice, BigDecimal finalTotal) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.numberOfNights = numberOfNights;
        this.roomRate = roomRate;
        this.totalPrice = totalPrice;
        this.finalTotal = finalTotal;
        this.reservationStatus = "Confirmed";
        this.discountApplied = BigDecimal.ZERO;
        this.reservationDate = LocalDate.now();
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public int getGuestId() {
        return guestId;
    }
    
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public LocalTime getCheckInTime() {
        return checkInTime;
    }
    
    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }
    
    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }
    
    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
    
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    public String getReservationStatus() {
        return reservationStatus;
    }
    
    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
    
    public int getNumberOfNights() {
        return numberOfNights;
    }
    
    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }
    
    public BigDecimal getRoomRate() {
        return roomRate;
    }
    
    public void setRoomRate(BigDecimal roomRate) {
        this.roomRate = roomRate;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public BigDecimal getDiscountApplied() {
        return discountApplied;
    }
    
    public void setDiscountApplied(BigDecimal discountApplied) {
        this.discountApplied = discountApplied;
    }
    
    public BigDecimal getFinalTotal() {
        return finalTotal;
    }
    
    public void setFinalTotal(BigDecimal finalTotal) {
        this.finalTotal = finalTotal;
    }
    
    public LocalDate getReservationDate() {
        return reservationDate;
    }
    
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCancelledDate() {
        return cancelledDate;
    }
    
    public void setCancelledDate(LocalDateTime cancelledDate) {
        this.cancelledDate = cancelledDate;
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }
    
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ============ UTILITY METHODS ============
    
    /**
     * Check if reservation is confirmed
     * 
     * @return true if status is "Confirmed"
     */
    public boolean isConfirmed() {
        return "Confirmed".equalsIgnoreCase(this.reservationStatus);
    }
    
    /**
     * Check if guest has checked in
     * 
     * @return true if status is "Checked-In"
     */
    public boolean isCheckedIn() {
        return "Checked-In".equalsIgnoreCase(this.reservationStatus);
    }
    
    /**
     * Check if guest has checked out
     * 
     * @return true if status is "Checked-Out"
     */
    public boolean isCheckedOut() {
        return "Checked-Out".equalsIgnoreCase(this.reservationStatus);
    }
    
    /**
     * Check if reservation is cancelled
     * 
     * @return true if status is "Cancelled"
     */
    public boolean isCancelled() {
        return "Cancelled".equalsIgnoreCase(this.reservationStatus);
    }
    
    /**
     * Get balance due (final total - already paid)
     * Note: This requires payment records to be checked separately
     * 
     * @return final total (balance due if no payments made)
     */
    public BigDecimal getBalanceDue() {
        return finalTotal != null ? finalTotal : BigDecimal.ZERO;
    }
    
    /**
     * Get formatted final total for display
     * 
     * @return formatted total (e.g., "PHP 28,000.00")
     */
    public String getFormattedFinalTotal() {
        return Utilities.CurrencyUtil.formatCurrency(finalTotal);
    }
    
    /**
     * Get reservation duration description
     * 
     * @return description like "3 nights (Jan 15 - Jan 18)"
     */
    public String getDurationDescription() {
        if (checkInDate == null || checkOutDate == null) {
            return "N/A";
        }
        return numberOfNights + " nights (" + checkInDate + " - " + checkOutDate + ")";
    }
    
    /**
     * Check if reservation is in the past
     * 
     * @return true if check-out date has passed
     */
    public boolean isPast() {
        if (checkOutDate == null) {
            return false;
        }
        return checkOutDate.isBefore(LocalDate.now());
    }
    
    /**
     * Check if reservation is upcoming
     * 
     * @return true if check-in date is in the future
     */
    public boolean isUpcoming() {
        if (checkInDate == null) {
            return false;
        }
        return checkInDate.isAfter(LocalDate.now());
    }
    
    /**
     * Check if reservation is currently active (during stay)
     * 
     * @return true if today is between check-in and check-out dates
     */
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        if (checkInDate == null || checkOutDate == null) {
            return false;
        }
        return !today.isBefore(checkInDate) && today.isBefore(checkOutDate);
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", guestId=" + guestId +
                ", roomId=" + roomId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numberOfGuests=" + numberOfGuests +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", numberOfNights=" + numberOfNights +
                ", roomRate=" + roomRate +
                ", totalPrice=" + totalPrice +
                ", finalTotal=" + finalTotal +
                ", reservationDate=" + reservationDate +
                '}';
    }
}
