package DAO;

import Config.DBConfig;
import Models.Reservation;
import Utilities.HotelException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reservation Data Access Object (DAO)
 * 
 * Handles all database operations for Reservation entities.
 * Manages reservation creation, retrieval, updates, and cancellations.
 * Implements availability checking and reservation conflict detection.
 * Uses PreparedStatements to prevent SQL injection attacks.
 * 
 * Operations:
 * - Create new reservations
 * - Retrieve reservations by ID, guest ID, room ID, date range
 * - Update reservation information and status
 * - Cancel reservations
 * - Check room availability for date ranges
 * - Detect overlapping/conflicting reservations
 * - List all reservations with filters
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class ReservationDAO {
    
    /**
     * Creates a new reservation in the database
     * 
     * @param reservation Reservation object containing reservation details
     * @return generated reservation ID
     * @throws HotelException if creation fails
     */
    public int createReservation(Reservation reservation) throws HotelException {
        String query = "INSERT INTO reservations (guest_id, room_id, check_in_date, check_out_date, " +
                       "check_in_time, check_out_time, number_of_guests, reservation_status, " +
                       "number_of_nights, room_rate, total_price, discount_applied, final_total, " +
                       "reservation_date, notes) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, reservation.getGuestId());
            pstmt.setInt(2, reservation.getRoomId());
            pstmt.setDate(3, java.sql.Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(reservation.getCheckOutDate()));
            
            if (reservation.getCheckInTime() != null) {
                pstmt.setTime(5, java.sql.Time.valueOf(reservation.getCheckInTime()));
            } else {
                pstmt.setNull(5, Types.TIME);
            }
            
            if (reservation.getCheckOutTime() != null) {
                pstmt.setTime(6, java.sql.Time.valueOf(reservation.getCheckOutTime()));
            } else {
                pstmt.setNull(6, Types.TIME);
            }
            
            pstmt.setInt(7, reservation.getNumberOfGuests());
            pstmt.setString(8, reservation.getReservationStatus());
            pstmt.setInt(9, reservation.getNumberOfNights());
            pstmt.setBigDecimal(10, reservation.getRoomRate());
            pstmt.setBigDecimal(11, reservation.getTotalPrice());
            pstmt.setBigDecimal(12, reservation.getDiscountApplied());
            pstmt.setBigDecimal(13, reservation.getFinalTotal());
            pstmt.setDate(14, java.sql.Date.valueOf(reservation.getReservationDate()));
            pstmt.setString(15, reservation.getNotes());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new HotelException("Failed to create reservation: no rows affected");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new HotelException("Failed to create reservation: no ID generated");
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error creating reservation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves a reservation by reservation ID
     * 
     * @param reservationId the reservation ID to search for
     * @return Reservation object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public Reservation getReservationById(int reservationId) throws HotelException {
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, reservationId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving reservation by ID: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Retrieves all reservations for a specific guest
     * 
     * @param guestId the guest ID
     * @return List of Reservation objects for the guest
     * @throws HotelException if database error occurs
     */
    public List<Reservation> getReservationsByGuestId(int guestId) throws HotelException {
        String query = "SELECT * FROM reservations WHERE guest_id = ? ORDER BY check_in_date DESC";
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, guestId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving reservations by guest ID: " + e.getMessage(), e);
        }
        return reservations;
    }
    
    /**
     * Retrieves all reservations for a specific room
     * 
     * @param roomId the room ID
     * @return List of Reservation objects for the room
     * @throws HotelException if database error occurs
     */
    public List<Reservation> getReservationsByRoomId(int roomId) throws HotelException {
        String query = "SELECT * FROM reservations WHERE room_id = ? ORDER BY check_in_date DESC";
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, roomId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving reservations by room ID: " + e.getMessage(), e);
        }
        return reservations;
    }
    
    /**
     * Retrieves all reservations within a date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return List of Reservation objects within the date range
     * @throws HotelException if database error occurs
     */
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        String query = "SELECT * FROM reservations WHERE check_in_date >= ? AND check_out_date <= ? " +
                       "ORDER BY check_in_date ASC";
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving reservations by date range: " + e.getMessage(), e);
        }
        return reservations;
    }
    
    /**
     * Retrieves all reservations with a specific status
     * 
     * @param status the reservation status (Confirmed, Checked-In, Checked-Out, Cancelled)
     * @return List of Reservation objects with the specified status
     * @throws HotelException if database error occurs
     */
    public List<Reservation> getReservationsByStatus(String status) throws HotelException {
        String query = "SELECT * FROM reservations WHERE reservation_status = ? ORDER BY check_in_date DESC";
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving reservations by status: " + e.getMessage(), e);
        }
        return reservations;
    }
    
    /**
     * Retrieves all reservations in the system
     * 
     * @return List of all Reservation objects
     * @throws HotelException if database error occurs
     */
    public List<Reservation> getAllReservations() throws HotelException {
        String query = "SELECT * FROM reservations ORDER BY check_in_date DESC";
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving all reservations: " + e.getMessage(), e);
        }
        return reservations;
    }
    
    /**
     * Updates an existing reservation
     * 
     * @param reservation the Reservation object with updated information
     * @return true if update was successful
     * @throws HotelException if update fails
     */
    public boolean updateReservation(Reservation reservation) throws HotelException {
        String query = "UPDATE reservations SET guest_id = ?, room_id = ?, check_in_date = ?, " +
                       "check_out_date = ?, check_in_time = ?, check_out_time = ?, " +
                       "number_of_guests = ?, reservation_status = ?, number_of_nights = ?, " +
                       "room_rate = ?, total_price = ?, discount_applied = ?, final_total = ?, " +
                       "notes = ?, updated_at = CURRENT_TIMESTAMP " +
                       "WHERE reservation_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, reservation.getGuestId());
            pstmt.setInt(2, reservation.getRoomId());
            pstmt.setDate(3, java.sql.Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(reservation.getCheckOutDate()));
            
            if (reservation.getCheckInTime() != null) {
                pstmt.setTime(5, java.sql.Time.valueOf(reservation.getCheckInTime()));
            } else {
                pstmt.setNull(5, Types.TIME);
            }
            
            if (reservation.getCheckOutTime() != null) {
                pstmt.setTime(6, java.sql.Time.valueOf(reservation.getCheckOutTime()));
            } else {
                pstmt.setNull(6, Types.TIME);
            }
            
            pstmt.setInt(7, reservation.getNumberOfGuests());
            pstmt.setString(8, reservation.getReservationStatus());
            pstmt.setInt(9, reservation.getNumberOfNights());
            pstmt.setBigDecimal(10, reservation.getRoomRate());
            pstmt.setBigDecimal(11, reservation.getTotalPrice());
            pstmt.setBigDecimal(12, reservation.getDiscountApplied());
            pstmt.setBigDecimal(13, reservation.getFinalTotal());
            pstmt.setString(14, reservation.getNotes());
            pstmt.setInt(15, reservation.getReservationId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new HotelException("Error updating reservation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Updates reservation status
     * 
     * @param reservationId the reservation ID
     * @param newStatus the new status
     * @return true if update was successful
     * @throws HotelException if update fails
     */
    public boolean updateReservationStatus(int reservationId, String newStatus) throws HotelException {
        String query = "UPDATE reservations SET reservation_status = ?, updated_at = CURRENT_TIMESTAMP " +
                       "WHERE reservation_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, reservationId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new HotelException("Error updating reservation status: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cancels a reservation and records cancellation details
     * 
     * @param reservationId the reservation ID to cancel
     * @param cancellationReason the reason for cancellation
     * @return true if cancellation was successful
     * @throws HotelException if cancellation fails
     */
    public boolean cancelReservation(int reservationId, String cancellationReason) throws HotelException {
        String query = "UPDATE reservations SET reservation_status = ?, cancelled_date = CURRENT_TIMESTAMP, " +
                       "cancellation_reason = ?, updated_at = CURRENT_TIMESTAMP WHERE reservation_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "Cancelled");
            pstmt.setString(2, cancellationReason);
            pstmt.setInt(3, reservationId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new HotelException("Error cancelling reservation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Checks if a room has conflicting reservations for a given date range
     * Implements overlap detection logic to find conflicting reservations
     * 
     * Overlap exists when:
     * - existing_start < new_end AND existing_end > new_start
     * 
     * @param roomId the room ID to check
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return List of conflicting Reservation objects (empty if no conflicts)
     * @throws HotelException if query fails
     */
    public List<Reservation> getConflictingReservations(int roomId, LocalDate checkInDate, LocalDate checkOutDate) throws HotelException {
        String query = "SELECT * FROM reservations WHERE room_id = ? " +
                       "AND reservation_status != 'Cancelled' " +
                       "AND check_in_date < ? " +
                       "AND check_out_date > ? " +
                       "ORDER BY check_in_date ASC";
        
        List<Reservation> conflicts = new ArrayList<>();
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, roomId);
            pstmt.setDate(2, java.sql.Date.valueOf(checkOutDate));
            pstmt.setDate(3, java.sql.Date.valueOf(checkInDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    conflicts.add(mapResultSetToReservation(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error checking conflicting reservations: " + e.getMessage(), e);
        }
        return conflicts;
    }
    
    /**
     * Checks if a room is available for the given date range
     * Returns true only if no active (non-cancelled) reservations conflict
     * 
     * @param roomId the room ID to check
     * @param checkInDate the requested check-in date
     * @param checkOutDate the requested check-out date
     * @return true if room is available, false otherwise
     * @throws HotelException if query fails
     */
    public boolean isRoomAvailable(int roomId, LocalDate checkInDate, LocalDate checkOutDate) throws HotelException {
        List<Reservation> conflicts = getConflictingReservations(roomId, checkInDate, checkOutDate);
        return conflicts.isEmpty();
    }
    
    /**
     * Maps a ResultSet row to a Reservation object
     * 
     * @param rs the ResultSet containing reservation data
     * @return Reservation object populated with data from ResultSet
     * @throws SQLException if column access fails
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setGuestId(rs.getInt("guest_id"));
        reservation.setRoomId(rs.getInt("room_id"));
        
        // Handle date conversions
        java.sql.Date checkInDate = rs.getDate("check_in_date");
        if (checkInDate != null) {
            reservation.setCheckInDate(checkInDate.toLocalDate());
        }
        
        java.sql.Date checkOutDate = rs.getDate("check_out_date");
        if (checkOutDate != null) {
            reservation.setCheckOutDate(checkOutDate.toLocalDate());
        }
        
        // Handle time conversions
        java.sql.Time checkInTime = rs.getTime("check_in_time");
        if (checkInTime != null) {
            reservation.setCheckInTime(checkInTime.toLocalTime());
        }
        
        java.sql.Time checkOutTime = rs.getTime("check_out_time");
        if (checkOutTime != null) {
            reservation.setCheckOutTime(checkOutTime.toLocalTime());
        }
        
        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
        reservation.setReservationStatus(rs.getString("reservation_status"));
        reservation.setNumberOfNights(rs.getInt("number_of_nights"));
        reservation.setRoomRate(rs.getBigDecimal("room_rate"));
        reservation.setTotalPrice(rs.getBigDecimal("total_price"));
        reservation.setDiscountApplied(rs.getBigDecimal("discount_applied"));
        reservation.setFinalTotal(rs.getBigDecimal("final_total"));
        
        java.sql.Date reservationDate = rs.getDate("reservation_date");
        if (reservationDate != null) {
            reservation.setReservationDate(reservationDate.toLocalDate());
        }
        
        reservation.setNotes(rs.getString("notes"));
        
        java.sql.Timestamp cancelledDate = rs.getTimestamp("cancelled_date");
        if (cancelledDate != null) {
            reservation.setCancelledDate(cancelledDate.toLocalDateTime());
        }
        
        reservation.setCancellationReason(rs.getString("cancellation_reason"));
        
        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            reservation.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        java.sql.Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            reservation.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return reservation;
    }
}
