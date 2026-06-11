package com.hotelreservationsystem.dao;

import com.hotelreservationsystem.config.DBConfig;
import com.hotelreservationsystem.model.Guest;
import com.hotelreservationsystem.util.HotelException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Guest Data Access Object (DAO)
 * 
 * Handles all database operations for Guest entities.
 * Manages guest registration, profile management, and information updates.
 * Uses PreparedStatements to prevent SQL injection attacks.
 * 
 * Operations:
 * - Create new guest profiles
 * - Retrieve guest by ID, email, phone
 * - Update guest information
 * - Delete guest accounts
 * - List all guests
 * - Search guests
 */
public class GuestDAO {
    
    /**
     * Creates a new guest in the database
     * 
     * @param guest Guest object containing guest details
     * @return generated guest ID
     * @throws HotelException if creation fails
     */
    public int createGuest(Guest guest) throws HotelException {
        String query = "INSERT INTO guests (user_id, first_name, middle_name, last_name, email, phone_number, address, " +
                       "date_of_birth, nationality, id_document_type, id_document_number) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, guest.getUserId());
            pstmt.setString(2, guest.getFirstName());
            pstmt.setString(3, guest.getMiddleName());
            pstmt.setString(4, guest.getLastName());
            pstmt.setString(5, guest.getEmail());
            pstmt.setString(6, guest.getPhoneNumber());
            pstmt.setString(7, guest.getAddress());
            pstmt.setDate(8, guest.getDateOfBirth() != null ? new java.sql.Date(guest.getDateOfBirth().getTime()) : null);
            pstmt.setString(9, guest.getNationality());
            pstmt.setString(10, guest.getIdDocumentType());
            pstmt.setString(11, guest.getIdDocumentNumber());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new HotelException("Failed to create guest: no rows affected");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new HotelException("Failed to create guest: no ID generated");
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error creating guest: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves a guest by guest ID
     * 
     * @param guestId the guest ID to search for
     * @return Guest object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public Guest getGuestById(int guestId) throws HotelException {
        String query = "SELECT * FROM guests WHERE guest_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, guestId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving guest by ID: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Retrieves a guest by user ID
     * 
     * @param userId the user ID associated with guest
     * @return Guest object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public Guest getGuestByUserId(int userId) throws HotelException {
        String query = "SELECT * FROM guests WHERE user_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving guest by user ID: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Retrieves a guest by email address
     * 
     * @param email the email to search for
     * @return Guest object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public Guest getGuestByEmail(String email) throws HotelException {
        String query = "SELECT * FROM guests WHERE email = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving guest by email: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Retrieves a guest by phone number
     * 
     * @param phoneNumber the phone number to search for
     * @return Guest object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public Guest getGuestByPhoneNumber(String phoneNumber) throws HotelException {
        String query = "SELECT * FROM guests WHERE phone_number = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, phoneNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving guest by phone number: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Updates an existing guest's information
     * 
     * @param guest Guest object with updated information
     * @return true if update successful, false otherwise
     * @throws HotelException if update fails
     */
    public boolean updateGuest(Guest guest) throws HotelException {
        String query = "UPDATE guests SET first_name = ?, middle_name = ?, last_name = ?, email = ?, " +
                       "phone_number = ?, address = ?, date_of_birth = ?, nationality = ?, " +
                       "id_document_type = ?, id_document_number = ?, updated_at = CURRENT_TIMESTAMP " +
                       "WHERE guest_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getMiddleName());
            pstmt.setString(3, guest.getLastName());
            pstmt.setString(4, guest.getEmail());
            pstmt.setString(5, guest.getPhoneNumber());
            pstmt.setString(6, guest.getAddress());
            pstmt.setDate(7, guest.getDateOfBirth() != null ? new java.sql.Date(guest.getDateOfBirth().getTime()) : null);
            pstmt.setString(8, guest.getNationality());
            pstmt.setString(9, guest.getIdDocumentType());
            pstmt.setString(10, guest.getIdDocumentNumber());
            pstmt.setInt(11, guest.getGuestId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error updating guest: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deletes a guest account
     * 
     * @param guestId the guest ID to delete
     * @return true if deletion successful, false otherwise
     * @throws HotelException if deletion fails
     */
    public boolean deleteGuest(int guestId) throws HotelException {
        String query = "DELETE FROM guests WHERE guest_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, guestId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error deleting guest: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves all guests from the database
     * 
     * @return List of all Guest objects
     * @throws HotelException if retrieval fails
     */
    public List<Guest> getAllGuests() throws HotelException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM guests ORDER BY created_at DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                guests.add(mapResultSetToGuest(rs));
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving all guests: " + e.getMessage(), e);
        }
        return guests;
    }
    
    /**
     * Searches for guests by name
     * 
     * @param searchName part of first or last name to search
     * @return List of Guest objects matching search criteria
     * @throws HotelException if search fails
     */
    public List<Guest> searchGuestsByName(String searchName) throws HotelException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM guests WHERE CONCAT(first_name, ' ', last_name) LIKE ? ORDER BY last_name, first_name";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "% " + searchName + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    guests.add(mapResultSetToGuest(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error searching guests by name: " + e.getMessage(), e);
        }
        return guests;
    }
    
    /**
     * Maps a ResultSet row to a Guest object
     * Helper method used by query methods
     * 
     * @param rs ResultSet containing guest data
     * @return Guest object populated with data from ResultSet
     * @throws SQLException if data extraction fails
     */
    private Guest mapResultSetToGuest(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setGuestId(rs.getInt("guest_id"));
        guest.setUserId(rs.getInt("user_id"));
        guest.setFirstName(rs.getString("first_name"));
        guest.setMiddleName(rs.getString("middle_name"));
        guest.setLastName(rs.getString("last_name"));
        guest.setEmail(rs.getString("email"));
        guest.setPhoneNumber(rs.getString("phone_number"));
        guest.setAddress(rs.getString("address"));
        guest.setDateOfBirth(rs.getDate("date_of_birth"));
        guest.setNationality(rs.getString("nationality"));
        guest.setIdDocumentType(rs.getString("id_document_type"));
        guest.setIdDocumentNumber(rs.getString("id_document_number"));
        guest.setCreatedAt(rs.getTimestamp("created_at"));
        guest.setUpdatedAt(rs.getTimestamp("updated_at"));
        return guest;
    }
}
