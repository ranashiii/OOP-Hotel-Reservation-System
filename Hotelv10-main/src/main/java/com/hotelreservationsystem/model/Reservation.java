package com.hotelreservationsystem.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Reservation - Reservation Model Class
 * 
 * Represents a hotel room reservation with dates, guest info, pricing,
 * and status tracking throughout the reservation lifecycle.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
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
    private double roomRate;
    private double totalPrice;
    private double discountApplied;
    private double finalTotal;
    private LocalDate reservationDate;
    private String notes;
    private LocalDateTime cancelledDate;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ============ CONSTRUCTORS ============
    public Reservation() {}
    
    public Reservation(int guestId, int roomId, LocalDate checkInDate, 
                      LocalDate checkOutDate, int numberOfGuests, double totalPrice) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
        this.reservationStatus = "Confirmed";
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
    
    public double getRoomRate() {
        return roomRate;
    }
    
    public void setRoomRate(double roomRate) {
        this.roomRate = roomRate;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public double getDiscountApplied() {
        return discountApplied;
    }
    
    public void setDiscountApplied(double discountApplied) {
        this.discountApplied = discountApplied;
    }
    
    public double getFinalTotal() {
        return finalTotal;
    }
    
    public void setFinalTotal(double finalTotal) {
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
    public boolean isConfirmed() {
        return "Confirmed".equalsIgnoreCase(this.reservationStatus);
    }
    
    public boolean isCheckedIn() {
        return "Checked-In".equalsIgnoreCase(this.reservationStatus);
    }
    
    public boolean isCheckedOut() {
        return "Checked-Out".equalsIgnoreCase(this.reservationStatus);
    }
    
    public boolean isCancelled() {
        return "Cancelled".equalsIgnoreCase(this.reservationStatus);
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", guestId=" + guestId +
                ", roomId=" + roomId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", status='" + reservationStatus + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
