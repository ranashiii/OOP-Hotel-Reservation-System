package com.hotelreservationsystem.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Payment - Payment Model Class
 * 
 * Represents a payment transaction for a reservation including payment method,
 * amount, status, and refund information.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class Payment {
    
    private int paymentId;
    private int reservationId;
    private double paymentAmount;
    private String paymentMethod;
    private String paymentTypeDetails;
    private String paymentStatus;
    private LocalDate paymentDate;
    private LocalTime paymentTime;
    private double refundAmount;
    private LocalDate refundDate;
    private String refundReason;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ============ CONSTRUCTORS ============
    public Payment() {}
    
    public Payment(int reservationId, double paymentAmount, String paymentMethod) {
        this.reservationId = reservationId;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "Pending";
    }
    
    // ============ GETTERS & SETTERS ============
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public double getPaymentAmount() {
        return paymentAmount;
    }
    
    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
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
    }
    
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public LocalTime getPaymentTime() {
        return paymentTime;
    }
    
    public void setPaymentTime(LocalTime paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    public double getRefundAmount() {
        return refundAmount;
    }
    
    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }
    
    public LocalDate getRefundDate() {
        return refundDate;
    }
    
    public void setRefundDate(LocalDate refundDate) {
        this.refundDate = refundDate;
    }
    
    public String getRefundReason() {
        return refundReason;
    }
    
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
    public boolean isPending() {
        return "Pending".equalsIgnoreCase(this.paymentStatus);
    }
    
    public boolean isCompleted() {
        return "Completed".equalsIgnoreCase(this.paymentStatus);
    }
    
    public boolean isFailed() {
        return "Failed".equalsIgnoreCase(this.paymentStatus);
    }
    
    public boolean isRefunded() {
        return "Refunded".equalsIgnoreCase(this.paymentStatus);
    }
    
    public boolean isCashPayment() {
        return "Cash".equalsIgnoreCase(this.paymentMethod);
    }
    
    public boolean isCreditCardPayment() {
        return "Credit Card".equalsIgnoreCase(this.paymentMethod);
    }
    
    public boolean isEWalletPayment() {
        return "E-Wallet".equalsIgnoreCase(this.paymentMethod);
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", reservationId=" + reservationId +
                ", paymentAmount=" + paymentAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
