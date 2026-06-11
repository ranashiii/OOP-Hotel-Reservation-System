package com.hotelreservationsystem.dao;

import com.hotelreservationsystem.config.DBConfig;
import com.hotelreservationsystem.model.Payment;
import com.hotelreservationsystem.util.HotelException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Payment Data Access Object (DAO)
 * 
 * Handles all database operations for Payment entities.
 * Manages payment processing, recording, refunds, and transaction history.
 * Uses PreparedStatements to prevent SQL injection attacks.
 * 
 * Operations:
 * - Create new payment records
 * - Retrieve payments by ID, reservation, status, date range
 * - Update payment information and status
 * - Process refunds
 * - List all payments with filters
 * - Get payment totals
 */
public class PaymentDAO {
    
    /**
     * Creates a new payment record in the database
     * 
     * @param payment Payment object containing payment details
     * @return generated payment ID
     * @throws HotelException if creation fails
     */
    public int createPayment(Payment payment) throws HotelException {
        String query = "INSERT INTO payments (reservation_id, payment_amount, payment_method, payment_type_details, " +
                       "payment_status, payment_date, payment_time, transaction_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payment.getReservationId());
            pstmt.setBigDecimal(2, payment.getPaymentAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setString(4, payment.getPaymentTypeDetails());
            pstmt.setString(5, payment.getPaymentStatus());
            pstmt.setDate(6, payment.getPaymentDate() != null ? java.sql.Date.valueOf(payment.getPaymentDate()) : null);
            pstmt.setTime(7, payment.getPaymentTime() != null ? java.sql.Time.valueOf(payment.getPaymentTime()) : null);
            pstmt.setString(8, payment.getTransactionId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new HotelException("Failed to create payment: no rows affected");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new HotelException("Failed to create payment: no ID generated");
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error creating payment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves a payment by payment ID
     * 
     * @param paymentId the payment ID to search for
     * @return Payment object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public Payment getPaymentById(int paymentId) throws HotelException {
        String query = "SELECT * FROM payments WHERE payment_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, paymentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayment(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving payment by ID: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Retrieves all payments for a specific reservation
     * 
     * @param reservationId the reservation ID
     * @return List of Payment objects for the reservation
     * @throws HotelException if retrieval fails
     */
    public List<Payment> getPaymentsByReservationId(int reservationId) throws HotelException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE reservation_id = ? ORDER BY payment_date DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, reservationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving payments by reservation ID: " + e.getMessage(), e);
        }
        return payments;
    }
    
    /**
     * Retrieves payments by status
     * Valid statuses: Pending, Completed, Failed, Refunded
     * 
     * @param status the status to filter by
     * @return List of Payment objects with matching status
     * @throws HotelException if retrieval fails
     */
    public List<Payment> getPaymentsByStatus(String status) throws HotelException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE payment_status = ? ORDER BY payment_date DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving payments by status: " + e.getMessage(), e);
        }
        return payments;
    }
    
    /**
     * Retrieves payments within a date range
     * 
     * @param startDate start date for range
     * @param endDate end date for range
     * @return List of Payment objects within date range
     * @throws HotelException if retrieval fails
     */
    public List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE payment_date BETWEEN ? AND ? ORDER BY payment_date DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving payments by date range: " + e.getMessage(), e);
        }
        return payments;
    }
    
    /**
     * Updates an existing payment's information
     * 
     * @param payment Payment object with updated information
     * @return true if update successful, false otherwise
     * @throws HotelException if update fails
     */
    public boolean updatePayment(Payment payment) throws HotelException {
        String query = "UPDATE payments SET payment_amount = ?, payment_method = ?, payment_type_details = ?, " +
                       "payment_status = ?, payment_date = ?, payment_time = ?, transaction_id = ?, updated_at = CURRENT_TIMESTAMP " +
                       "WHERE payment_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setBigDecimal(1, payment.getPaymentAmount());
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setString(3, payment.getPaymentTypeDetails());
            pstmt.setString(4, payment.getPaymentStatus());
            pstmt.setDate(5, payment.getPaymentDate() != null ? java.sql.Date.valueOf(payment.getPaymentDate()) : null);
            pstmt.setTime(6, payment.getPaymentTime() != null ? java.sql.Time.valueOf(payment.getPaymentTime()) : null);
            pstmt.setString(7, payment.getTransactionId());
            pstmt.setInt(8, payment.getPaymentId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error updating payment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Updates payment status
     * Valid statuses: Pending, Completed, Failed, Refunded
     * 
     * @param paymentId the payment ID
     * @param newStatus the new status
     * @return true if update successful, false otherwise
     * @throws HotelException if update fails
     */
    public boolean updatePaymentStatus(int paymentId, String newStatus) throws HotelException {
        String query = "UPDATE payments SET payment_status = ?, updated_at = CURRENT_TIMESTAMP WHERE payment_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, paymentId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error updating payment status: " + e.getMessage(), e);
        }
    }
    
    /**
     * Processes a refund for a payment
     * Updates payment with refund information
     * 
     * @param paymentId the payment ID to refund
     * @param refundAmount the amount to refund
     * @param refundReason the reason for refund
     * @return true if refund processed successfully, false otherwise
     * @throws HotelException if refund fails
     */
    public boolean processRefund(int paymentId, java.math.BigDecimal refundAmount, String refundReason) throws HotelException {
        String query = "UPDATE payments SET refund_amount = ?, refund_date = CURRENT_DATE, refund_reason = ?, " +
                       "payment_status = 'Refunded', updated_at = CURRENT_TIMESTAMP WHERE payment_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setBigDecimal(1, refundAmount);
            pstmt.setString(2, refundReason);
            pstmt.setInt(3, paymentId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error processing refund: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves all payments from the database
     * 
     * @return List of all Payment objects
     * @throws HotelException if retrieval fails
     */
    public List<Payment> getAllPayments() throws HotelException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments ORDER BY payment_date DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving all payments: " + e.getMessage(), e);
        }
        return payments;
    }
    
    /**
     * Gets total payment amount for a date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return total payment amount in the period
     * @throws HotelException if query fails
     */
    public java.math.BigDecimal getTotalPaymentsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        String query = "SELECT COALESCE(SUM(payment_amount), 0) FROM payments " +
                       "WHERE payment_status = 'Completed' AND payment_date BETWEEN ? AND ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error getting total payments: " + e.getMessage(), e);
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Gets total refunded amount for a date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return total refunded amount in the period
     * @throws HotelException if query fails
     */
    public java.math.BigDecimal getTotalRefundsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        String query = "SELECT COALESCE(SUM(refund_amount), 0) FROM payments " +
                       "WHERE refund_date BETWEEN ? AND ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error getting total refunds: " + e.getMessage(), e);
        }
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * Maps a ResultSet row to a Payment object
     * Helper method used by query methods
     * 
     * @param rs ResultSet containing payment data
     * @return Payment object populated with data from ResultSet
     * @throws SQLException if data extraction fails
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setReservationId(rs.getInt("reservation_id"));
        payment.setPaymentAmount(rs.getBigDecimal("payment_amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentTypeDetails(rs.getString("payment_type_details"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        
        java.sql.Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDate());
        }
        
        java.sql.Time paymentTime = rs.getTime("payment_time");
        if (paymentTime != null) {
            payment.setPaymentTime(paymentTime.toLocalTime());
        }
        
        payment.setRefundAmount(rs.getBigDecimal("refund_amount"));
        
        java.sql.Date refundDate = rs.getDate("refund_date");
        if (refundDate != null) {
            payment.setRefundDate(refundDate.toLocalDate());
        }
        
        payment.setRefundReason(rs.getString("refund_reason"));
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setCreatedAt(rs.getTimestamp("created_at"));
        payment.setUpdatedAt(rs.getTimestamp("updated_at"));
        return payment;
    }
}
