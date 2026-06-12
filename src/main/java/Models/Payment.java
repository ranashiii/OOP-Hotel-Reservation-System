package Models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Payment - Payment Model Class
 * 
 * Represents a payment transaction for a reservation.
 * Tracks payment amount, method, status, and refund information.
 * 
 * Properties include payment details, amounts, timestamps, and refund tracking.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class Payment {
    
    private int paymentId;
    private int reservationId;
    private BigDecimal paymentAmount;
    private String paymentMethod;
    private String paymentTypeDetails;
    private String paymentStatus;
    private LocalDate paymentDate;
    private LocalTime paymentTime;
    private BigDecimal refundAmount;
    private LocalDate refundDate;
    private String refundReason;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Payment() {
    }
    
    /**
     * Constructor with essential payment information
     * 
     * @param reservationId the reservation ID
     * @param paymentAmount the payment amount
     * @param paymentMethod the payment method (Cash, Credit Card, E-Wallet)
     */
    public Payment(int reservationId, BigDecimal paymentAmount, String paymentMethod) {
        this.reservationId = reservationId;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "Pending";
        this.refundAmount = BigDecimal.ZERO;
    }
    
    /**
     * Constructor with complete payment details
     * 
     * @param reservationId the reservation ID
     * @param paymentAmount the payment amount
     * @param paymentMethod the payment method
     * @param paymentTypeDetails additional payment details
     * @param paymentStatus the payment status
     * @param paymentDate the payment date
     * @param paymentTime the payment time
     */
    public Payment(int reservationId, BigDecimal paymentAmount, String paymentMethod,
                   String paymentTypeDetails, String paymentStatus, LocalDate paymentDate,
                   LocalTime paymentTime) {
        this.reservationId = reservationId;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentTypeDetails = paymentTypeDetails;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.refundAmount = BigDecimal.ZERO;
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
    
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }
    
    public void setPaymentAmount(BigDecimal paymentAmount) {
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
    
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }
    
    public void setRefundAmount(BigDecimal refundAmount) {
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
    
    /**
     * Check if payment is pending
     * 
     * @return true if status is "Pending"
     */
    public boolean isPending() {
        return "Pending".equalsIgnoreCase(this.paymentStatus);
    }
    
    /**
     * Check if payment is completed
     * 
     * @return true if status is "Completed"
     */
    public boolean isCompleted() {
        return "Completed".equalsIgnoreCase(this.paymentStatus);
    }
    
    /**
     * Check if payment failed
     * 
     * @return true if status is "Failed"
     */
    public boolean isFailed() {
        return "Failed".equalsIgnoreCase(this.paymentStatus);
    }
    
    /**
     * Check if payment has been refunded
     * 
     * @return true if status is "Refunded"
     */
    public boolean isRefunded() {
        return "Refunded".equalsIgnoreCase(this.paymentStatus);
    }
    
    /**
     * Check if payment is by cash
     * 
     * @return true if payment method is "Cash"
     */
    public boolean isCash() {
        return "Cash".equalsIgnoreCase(this.paymentMethod);
    }
    
    /**
     * Check if payment is by credit card
     * 
     * @return true if payment method is "Credit Card"
     */
    public boolean isCreditCard() {
        return "Credit Card".equalsIgnoreCase(this.paymentMethod);
    }
    
    /**
     * Check if payment is by e-wallet
     * 
     * @return true if payment method is "E-Wallet"
     */
    public boolean isEWallet() {
        return "E-Wallet".equalsIgnoreCase(this.paymentMethod);
    }
    
    /**
     * Get formatted payment amount for display
     * 
     * @return formatted amount (e.g., "PHP 28,000.00")
     */
    public String getFormattedPaymentAmount() {
        return Utilities.CurrencyUtil.formatCurrency(paymentAmount);
    }
    
    /**
     * Get formatted refund amount for display
     * 
     * @return formatted refund amount (e.g., "PHP 28,000.00")
     */
    public String getFormattedRefundAmount() {
        return Utilities.CurrencyUtil.formatCurrency(refundAmount);
    }
    
    /**
     * Get payment method display name
     * Returns a friendly display name for the payment method
     * 
     * @return payment method display name
     */
    public String getPaymentMethodDisplay() {
        if (paymentMethod == null) {
            return "Unknown";
        }
        
        if (isCash()) {
            return "Cash";
        } else if (isCreditCard()) {
            return "Credit Card" + (paymentTypeDetails != null ? " - " + paymentTypeDetails : "");
        } else if (isEWallet()) {
            return "E-Wallet" + (paymentTypeDetails != null ? " - " + paymentTypeDetails : "");
        }
        
        return paymentMethod;
    }
    
    /**
     * Get payment status display name
     * Returns a friendly display name for the payment status
     * 
     * @return status display name
     */
    public String getPaymentStatusDisplay() {
        if (paymentStatus == null) {
            return "Unknown";
        }
        
        return paymentStatus;
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", reservationId=" + reservationId +
                ", paymentAmount=" + paymentAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentDate=" + paymentDate +
                ", paymentTime=" + paymentTime +
                ", refundAmount=" + refundAmount +
                ", transactionId='" + transactionId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
