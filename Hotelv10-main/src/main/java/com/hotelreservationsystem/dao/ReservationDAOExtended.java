package com.hotelreservationsystem.dao;

import com.hotelreservationsystem.config.DBConfig;
import com.hotelreservationsystem.model.Reservation;
import com.hotelreservationsystem.util.HotelException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ReservationDAOExtended - Extended Data Access Object for Reservation operations
 * 
 * Handles additional reservation queries including filtering and reporting.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class ReservationDAOExtended {
    
    /**
     * Get all reservations
     * 
     * @return list of all reservations
     * @throws HotelException if database error occurs
     */
    public static List<Reservation> getAllReservations() throws HotelException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations ORDER BY reservation_id DESC";
        
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new HotelException("Error fetching reservations: " + e.getMessage(), e);
        }
        
        return reservations;
    }
    
    /**
     * Get reservations by date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of reservations
     * @throws HotelException if database error occurs
     */
    public static List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE check_in_date >= ? AND check_out_date <= ? ORDER BY check_in_date";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error fetching reservations: " + e.getMessage(), e);
        }
        
        return reservations;
    }
    
    /**
     * Get reservations by status
     * 
     * @param status the reservation status
     * @return list of reservations
     * @throws HotelException if database error occurs
     */
    public static List<Reservation> getReservationsByStatus(String status) throws HotelException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE reservation_status = ? ORDER BY check_in_date DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error fetching reservations: " + e.getMessage(), e);
        }
        
        return reservations;
    }
    
    private static Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setGuestId(rs.getInt("guest_id"));
        reservation.setRoomId(rs.getInt("room_id"));
        reservation.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
        reservation.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
        reservation.setCheckInTime(rs.getTime("check_in_time") != null ? rs.getTime("check_in_time").toLocalTime() : null);
        reservation.setCheckOutTime(rs.getTime("check_out_time") != null ? rs.getTime("check_out_time").toLocalTime() : null);
        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
        reservation.setReservationStatus(rs.getString("reservation_status"));
        reservation.setNumberOfNights(rs.getInt("number_of_nights"));
        reservation.setRoomRate(rs.getDouble("room_rate"));
        reservation.setTotalPrice(rs.getDouble("total_price"));
        reservation.setDiscountApplied(rs.getDouble("discount_applied"));
        reservation.setFinalTotal(rs.getDouble("final_total"));
        reservation.setReservationDate(rs.getDate("reservation_date").toLocalDate());
        reservation.setNotes(rs.getString("notes"));
        return reservation;
    }
}
