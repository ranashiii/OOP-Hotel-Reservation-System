package com.hotelreservationsystem.dao;

import com.hotelreservationsystem.config.DBConfig;
import com.hotelreservationsystem.model.Payment;
import com.hotelreservationsystem.util.HotelException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentDAOExtended - Extended Data Access Object for Payment operations
 * 
 * Handles additional payment queries for reporting and analytics.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class PaymentDAOExtended {
    
    /**
     * Get all payments
     * 
     * @return list of all payments
     * @throws HotelException if database error occurs
     */
    public static List<Payment> getAllPayments() throws HotelException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments ORDER BY payment_id DESC";
        
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new HotelException("Error fetching payments: " + e.getMessage(), e);
        }
        
        return payments;
    }
    
    /**
     * Get payments by date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of payments
     * @throws HotelException if database error occurs
     */
    public static List<Payment> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE payment_date >= ? AND payment_date <= ? ORDER BY payment_date DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapResultSetToPayment(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error fetching payments: " + e.getMessage(), e);
        }
        
        return payments;
    }
    
    /**
     * Get total revenue by date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return total revenue
     * @throws HotelException if database error occurs
     */
    public static double getTotalRevenueByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        String sql = "SELECT COALESCE(SUM(payment_amount), 0) as total FROM payments WHERE payment_status = 'Completed' AND payment_date >= ? AND payment_date <= ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error calculating revenue: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    private static Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setReservationId(rs.getInt("reservation_id"));
        payment.setPaymentAmount(rs.getDouble("payment_amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentTypeDetails(rs.getString("payment_type_details"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        payment.setPaymentDate(rs.getDate("payment_date") != null ? rs.getDate("payment_date").toLocalDate() : null);
        payment.setPaymentTime(rs.getTime("payment_time") != null ? rs.getTime("payment_time").toLocalTime() : null);
        payment.setRefundAmount(rs.getDouble("refund_amount"));
        payment.setRefundDate(rs.getDate("refund_date") != null ? rs.getDate("refund_date").toLocalDate() : null);
        payment.setRefundReason(rs.getString("refund_reason"));
        payment.setTransactionId(rs.getString("transaction_id"));
        return payment;
    }
}
