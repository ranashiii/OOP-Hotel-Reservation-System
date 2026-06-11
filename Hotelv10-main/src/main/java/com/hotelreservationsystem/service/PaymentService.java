package com.hotelreservationsystem.service;

import com.hotelreservationsystem.dao.PaymentDAO;
import com.hotelreservationsystem.dao.ReservationDAO;
import com.hotelreservationsystem.model.Payment;
import com.hotelreservationsystem.model.Reservation;
import com.hotelreservationsystem.util.HotelException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Payment Service Layer
 * 
 * Handles all payment-related business logic including payment processing,
 * validation, refund processing, and payment history.
 */
public class PaymentService {
    private PaymentDAO paymentDAO = new PaymentDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    
    /**
     * Process payment for reservation
     * 
     * @param payment the payment object with details
     * @return Payment object if processing successful
     * @throws HotelException if processing fails or validation fails
     */
    public Payment processPayment(Payment payment) throws HotelException {
        if (payment == null) {
            throw new HotelException("Payment object cannot be null");
        }
        
        Reservation reservation = reservationDAO.getReservationById(payment.getReservationId());
        if (reservation == null) {
            throw new HotelException("Reservation not found");
        }
        
        if (payment.getPaymentAmount() == null || payment.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new HotelException("Payment amount must be greater than 0");
        }
        
        if (payment.getPaymentAmount().compareTo(new BigDecimal("999999.99")) > 0) {
            throw new HotelException("Payment amount cannot exceed 999999.99");
        }
        
        if (!isValidPaymentMethod(payment.getPaymentMethod())) {
            throw new HotelException("Invalid payment method. Must be: Cash, Credit Card, E-Wallet");
        }
        
        validatePaymentMethod(payment);
        
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentTime(LocalTime.now());
        payment.setPaymentStatus("Completed");
        
        if (payment.getTransactionId() == null) {
            payment.setTransactionId(UUID.randomUUID().toString());
        }
        
        int paymentId = paymentDAO.createPayment(payment);
        payment.setPaymentId(paymentId);
        return payment;
    }
    
    /**
     * Validate payment details based on payment method
     * 
     * @param payment the payment object to validate
     * @throws HotelException if validation fails
     */
    private void validatePaymentMethod(Payment payment) throws HotelException {
        if (payment.getPaymentMethod().equals("Cash")) {
            return;
        }
        
        if (payment.getPaymentMethod().equals("Credit Card")) {
            validateCreditCard(payment);
        } else if (payment.getPaymentMethod().equals("E-Wallet")) {
            validateEWallet(payment);
        }
    }
    
    /**
     * Validate credit card payment details
     * 
     * @param payment the payment object containing card details
     * @throws HotelException if validation fails
     */
    private void validateCreditCard(Payment payment) throws HotelException {
        if (payment.getPaymentTypeDetails() == null || payment.getPaymentTypeDetails().isEmpty()) {
            throw new HotelException("Card number is required");
        }
        
        String cardNumber = payment.getPaymentTypeDetails().replaceAll("\\s", "");
        
        if (!cardNumber.matches("^[0-9]{13,19}$")) {
            throw new HotelException("Card number must be 13-19 digits");
        }
        
        if (!isValidLuhn(cardNumber)) {
            throw new HotelException("Invalid card number (Luhn validation failed)");
        }
    }
    
    /**
     * Validate E-Wallet payment details (phone number)
     * 
     * @param payment the payment object containing e-wallet details
     * @throws HotelException if validation fails
     */
    private void validateEWallet(Payment payment) throws HotelException {
        if (payment.getPaymentTypeDetails() == null || payment.getPaymentTypeDetails().isEmpty()) {
            throw new HotelException("Phone number is required for E-Wallet");
        }
        
        if (!isValidPhoneNumber(payment.getPaymentTypeDetails())) {
            throw new HotelException("Invalid phone number format (must be Philippine format)");
        }
    }
    
    /**
     * Validate Luhn algorithm for credit card
     * 
     * @param cardNumber the card number to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean isSecond = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            
            if (isSecond) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            
            sum += digit;
            isSecond = !isSecond;
        }
        
        return sum % 10 == 0;
    }
    
    /**
     * Validate Philippine phone number format
     * 
     * @param phoneNumber the phone number to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && 
               (phoneNumber.matches("^09\\d{9}$") || phoneNumber.matches("^\\+639\\d{9}$"));
    }
    
    /**
     * Process refund for payment
     * 
     * @param paymentId the payment ID
     * @param refundAmount the amount to refund
     * @param refundReason the reason for refund
     * @return true if refund processed successfully
     * @throws HotelException if refund fails
     */
    public boolean processRefund(int paymentId, BigDecimal refundAmount, String refundReason) throws HotelException {
        Payment payment = paymentDAO.getPaymentById(paymentId);
        if (payment == null) {
            throw new HotelException("Payment not found");
        }
        
        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new HotelException("Refund amount cannot be negative");
        }
        
        if (refundAmount.compareTo(payment.getPaymentAmount()) > 0) {
            throw new HotelException("Refund amount cannot exceed payment amount");
        }
        
        return paymentDAO.processRefund(paymentId, refundAmount, refundReason);
    }
    
    /**
     * Get payment by ID
     * 
     * @param paymentId the payment ID
     * @return Payment object if found, null otherwise
     * @throws HotelException if query fails
     */
    public Payment getPaymentById(int paymentId) throws HotelException {
        return paymentDAO.getPaymentById(paymentId);
    }
    
    /**
     * Get all payments for a reservation
     * 
     * @param reservationId the reservation ID
     * @return List of Payment objects for the reservation
     * @throws HotelException if query fails
     */
    public List<Payment> getPaymentsByReservationId(int reservationId) throws HotelException {
        return paymentDAO.getPaymentsByReservationId(reservationId);
    }
    
    /**
     * Get all payments
     * 
     * @return List of all Payment objects
     * @throws HotelException if query fails
     */
    public List<Payment> getAllPayments() throws HotelException {
        return paymentDAO.getAllPayments();
    }
    
    /**
     * Get total payments for date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return total payment amount in the period
     * @throws HotelException if query fails
     */
    public BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        if (startDate == null || endDate == null) {
            throw new HotelException("Date range cannot be null");
        }
        return paymentDAO.getTotalPaymentsByDateRange(startDate, endDate);
    }
    
    /**
     * Validate payment method
     * Valid methods: Cash, Credit Card, E-Wallet
     * 
     * @param paymentMethod the payment method to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null && 
               (paymentMethod.equals("Cash") || 
                paymentMethod.equals("Credit Card") || 
                paymentMethod.equals("E-Wallet"));
    }
}
