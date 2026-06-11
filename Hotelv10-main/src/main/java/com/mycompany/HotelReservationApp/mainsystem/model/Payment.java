package com.mycompany.HotelReservationApp.mainsystem.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Payment Model Class
 * Represents a payment transaction for a reservation
 * Handles payment information and status tracking
 */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int paymentID;
    private int reservationID;
    private double paymentAmount;
    private String paymentMethod; // "Cash", "Credit Card", "E-Wallet"
    private String paymentTypeDetails; // Card info, wallet ID, etc.
    private String paymentStatus; // "Pending", "Completed", "Failed", "Refunded"
    private LocalDateTime paymentDateTime;
    private double refundAmount;
    private LocalDateTime refundDateTime;
    private String refundReason;
    private String transactionID;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Constructor for Payment
     */
    public Payment(int reservationID, double paymentAmount, String paymentMethod) {
        this.reservationID = reservationID;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "Pending";
        this.refundAmount = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getPaymentID() {
        return paymentID;
    }
    
    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
    
    public int getReservationID() {
        return reservationID;
    }
    
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    
    public double getPaymentAmount() {
        return paymentAmount;
    }
    
    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getPaymentTypeDetails() {
        return paymentTypeDetails;
    }
    
    public void setPaymentTypeDetails(String paymentTypeDetails) {
        this.paymentTypeDetails = paymentTypeDetails;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }
    
    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }
    
    public double getRefundAmount() {
        return refundAmount;
    }
    
    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getRefundDateTime() {
        return refundDateTime;
    }
    
    public void setRefundDateTime(LocalDateTime refundDateTime) {
        this.refundDateTime = refundDateTime;
    }
    
    public String getRefundReason() {
        return refundReason;
    }
    
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
    
    public String getTransactionID() {
        return transactionID;
    }
    
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
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
     * Check if payment is completed
     */
    public boolean isCompleted() {
        return paymentStatus.equals("Completed");
    }
    
    /**
     * Check if payment is pending
     */
    public boolean isPending() {
        return paymentStatus.equals("Pending");
    }
    
    /**
     * Check if payment is failed
     */
    public boolean isFailed() {
        return paymentStatus.equals("Failed");
    }
    
    /**
     * Check if payment is refunded
     */
    public boolean isRefunded() {
        return paymentStatus.equals("Refunded");
    }
    
    /**
     * Process refund
     */
    public void processRefund(double refundAmount, String reason) {
        this.refundAmount = refundAmount;
        this.refundReason = reason;
        this.refundDateTime = LocalDateTime.now();
        this.paymentStatus = "Refunded";
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", reservationID=" + reservationID +
                ", paymentAmount=" + paymentAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", transactionID='" + transactionID + '\'' +
                '}';
    }
}