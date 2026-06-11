package com.mycompany.HotelReservationApp.mainsystem.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Reservation Model Class
 * Represents a hotel reservation/booking
 * Handles reservation information and calculations
 */
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int reservationID;
    private int guestID;
    private int roomID;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private String reservationStatus; // "Confirmed", "Checked-In", "Checked-Out", "Cancelled"
    private long numberOfNights;
    private double roomRate;
    private double subtotal;
    private double tax;
    private double totalPrice;
    private double discountApplied;
    private double finalTotal;
    private LocalDate reservationDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Constructor for Reservation
     */
    public Reservation(int guestID, int roomID, LocalDate checkInDate,
                      LocalDate checkOutDate, int numberOfGuests) {
        this.guestID = guestID;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.reservationStatus = "Confirmed";
        this.reservationDate = LocalDate.now();
        this.numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        this.discountApplied = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getReservationID() {
        return reservationID;
    }
    
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    
    public int getGuestID() {
        return guestID;
    }
    
    public void setGuestID(int guestID) {
        this.guestID = guestID;
    }
    
    public int getRoomID() {
        return roomID;
    }
    
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        this.numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        this.numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
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
        this.updatedAt = LocalDateTime.now();
    }
    
    public long getNumberOfNights() {
        return numberOfNights;
    }
    
    public double getRoomRate() {
        return roomRate;
    }
    
    public void setRoomRate(double roomRate) {
        this.roomRate = roomRate;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getTax() {
        return tax;
    }
    
    public void setTax(double tax) {
        this.tax = tax;
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
    
    /**
     * Calculate total price with tax
     * Subtotal = roomRate * numberOfNights
     * Tax = subtotal * 0.12 (12%)
     * FinalTotal = (subtotal + tax) - discountApplied
     */
    public void calculateTotalPrice() {
        this.subtotal = this.roomRate * this.numberOfNights;
        this.tax = this.subtotal * 0.12; // 12% tax
        this.totalPrice = this.subtotal + this.tax;
        this.finalTotal = this.totalPrice - this.discountApplied;
    }
    
    /**
     * Check if reservation is confirmed
     */
    public boolean isConfirmed() {
        return reservationStatus.equals("Confirmed");
    }
    
    /**
     * Check if reservation is checked in
     */
    public boolean isCheckedIn() {
        return reservationStatus.equals("Checked-In");
    }
    
    /**
     * Check if reservation is checked out
     */
    public boolean isCheckedOut() {
        return reservationStatus.equals("Checked-Out");
    }
    
    /**
     * Check if reservation is cancelled
     */
    public boolean isCancelled() {
        return reservationStatus.equals("Cancelled");
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationID=" + reservationID +
                ", guestID=" + guestID +
                ", roomID=" + roomID +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numberOfGuests=" + numberOfGuests +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", numberOfNights=" + numberOfNights +
                ", finalTotal=" + finalTotal +
                '}';
    }
}