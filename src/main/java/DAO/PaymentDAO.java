package DAO;

import Config.DBConfig;
import Models.Payment;
import Utilities.HotelException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Payment Data Access Object (DAO)
 * 
 * Handles all database operations for Payment entities including recording,
 * tracking, and refund processing of payments.
 */
public class PaymentDAO {
    
    /**
     * Create a new payment record in the database
     */
    public int createPayment(Payment payment) throws HotelException {
        String query = "INSERT INTO payments (reservation_id, payment_amount, payment_method, " +
                       "payment_type_details, payment_status, payment_date, payment_time, " +
                       "transaction_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payment.getReservationId());
            pstmt.setBigDecimal(2, new java.math.BigDecimal(payment.getPaymentAmount()));
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setString(4, payment.getPaymentTypeDetails());
            pstmt.setString(5, payment.getPaymentStatus());
            pstmt.setDate(6, payment.getPaymentDate() != null ? 
                java.sql.Date.valueOf(payment.getPaymentDate()) : null);
            pstmt.setTime(7, payment.getPaymentTime() != null ? 
                java.sql.Time.valueOf(payment.getPaymentTime()) : null);
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
     * Retrieve payment by payment ID
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
     * Get all payments for a specific reservation
     */
    public List<Payment> getPaymentsByReservationId(int reservationId) throws HotelException {
        String query = "SELECT * FROM payments WHERE reservation_id = ? ORDER BY payment_date DESC";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, reservationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving payments: " + e.getMessage(), e);
        }
        return payments;
    }
    
    /**
     * Get all payments
     */
    public List<Payment> getAllPayments() throws HotelException {
        String query = "SELECT * FROM payments ORDER BY payment_date DESC";
        List<Payment> payments = new ArrayList<>();
        
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
     * Update payment record
     */
    public boolean updatePayment(Payment payment) throws HotelException {
        String query = "UPDATE payments SET payment_status = ?, refund_amount = ?, " +
                       "refund_date = ?, refund_reason = ? WHERE payment_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, payment.getPaymentStatus());
            pstmt.setDouble(2, payment.getRefundAmount());
            pstmt.setDate(3, payment.getRefundDate() != null ? 
                java.sql.Date.valueOf(payment.getRefundDate()) : null);
            pstmt.setString(4, payment.getRefundReason());
            pstmt.setInt(5, payment.getPaymentId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error updating payment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get payments by status
     */
    public List<Payment> getPaymentsByStatus(String status) throws HotelException {
        String query = "SELECT * FROM payments WHERE payment_status = ? ORDER BY payment_date DESC";
        List<Payment> payments = new ArrayList<>();
        
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
     * Map ResultSet to Payment object
     */
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setReservationId(rs.getInt("reservation_id"));
        payment.setPaymentAmount(rs.getDouble("payment_amount"));
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
        
        payment.setRefundAmount(rs.getDouble("refund_amount"));
        
        java.sql.Date refundDate = rs.getDate("refund_date");
        if (refundDate != null) {
            payment.setRefundDate(refundDate.toLocalDate());
        }
        
        payment.setRefundReason(rs.getString("refund_reason"));
        payment.setTransactionId(rs.getString("transaction_id"));
        
        return payment;
    }
}
