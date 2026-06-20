package Services;

import DAO.PaymentDAO;
import DAO.ReservationDAO;
import Models.Payment;
import Models.Reservation;
import Utilities.HotelException;
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
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null &&
               (phoneNumber.matches("^09\\d{9}$") || phoneNumber.matches("^\\+639\\d{9}$"));
    }

    /**
     * Process refund for payment
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
     */
    public Payment getPaymentById(int paymentId) throws HotelException {
        return paymentDAO.getPaymentById(paymentId);
    }

    /**
     * Get all payments for a reservation
     */
    public List<Payment> getPaymentsByReservationId(int reservationId) throws HotelException {
        return paymentDAO.getPaymentsByReservationId(reservationId);
    }

    /**
     * Get all payments
     */
    public List<Payment> getAllPayments() throws HotelException {
        return paymentDAO.getAllPayments();
    }

    /**
     * Get total payments for date range
     */
    public BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        if (startDate == null || endDate == null) {
            throw new HotelException("Date range cannot be null");
        }
        return paymentDAO.getTotalPaymentsByDateRange(startDate, endDate);
    }

    /**
     * Validate payment method
     */
    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null &&
               (paymentMethod.equals("Cash") ||
                paymentMethod.equals("Credit Card") ||
                paymentMethod.equals("E-Wallet"));
    }

    // =============================================================
    //  ADMIN CONVENIENCE METHOD (added to fix compilation errors)
    // =============================================================

    /**
     * Get all payments (alias for admin panels)
     */
    public List<Payment> findAllPayments() throws HotelException {
        return paymentDAO.getAllPayments();
    }
}