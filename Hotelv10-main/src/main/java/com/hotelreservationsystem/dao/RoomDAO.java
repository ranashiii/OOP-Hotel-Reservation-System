package com.hotelreservationsystem.dao;

import com.hotelreservationsystem.config.DBConfig;
import com.hotelreservationsystem.model.Room;
import com.hotelreservationsystem.util.HotelException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Room Data Access Object (DAO)
 * 
 * Handles all database operations for Room entities.
 * Manages room inventory, availability, status, and pricing.
 * Uses PreparedStatements to prevent SQL injection attacks.
 * 
 * Operations:
 * - Create new rooms
 * - Retrieve rooms by ID, type, status, capacity, price
 * - Update room information and status
 * - Delete rooms
 * - Check room availability
 * - List all rooms with filters
 */
public class RoomDAO {
    
    /**
     * Creates a new room in the database
     * 
     * @param room Room object containing room details
     * @return generated room ID
     * @throws HotelException if creation fails
     */
    public int createRoom(Room room) throws HotelException {
        String query = "INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, room_image, status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setInt(3, room.getFloor());
            pstmt.setInt(4, room.getCapacity());
            pstmt.setBigDecimal(5, room.getPricePerNight());
            pstmt.setString(6, room.getAmenities());
            pstmt.setString(7, room.getRoomImage());
            pstmt.setString(8, room.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new HotelException("Failed to create room: no rows affected");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new HotelException("Failed to create room: no ID generated");
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new HotelException("Room number already exists: " + room.getRoomNumber(), e);
            }
            throw new HotelException("Error creating room: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves a room by room ID
     * 
     * @param roomId the room ID to search for
     * @return Room object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public Room getRoomById(int roomId) throws HotelException {
        String query = "SELECT * FROM rooms WHERE room_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, roomId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving room by ID: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Retrieves a room by room number
     * 
     * @param roomNumber the room number to search for
     * @return Room object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public Room getRoomByNumber(String roomNumber) throws HotelException {
        String query = "SELECT * FROM rooms WHERE room_number = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, roomNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving room by number: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Updates an existing room's information
     * 
     * @param room Room object with updated information
     * @return true if update successful, false otherwise
     * @throws HotelException if update fails
     */
    public boolean updateRoom(Room room) throws HotelException {
        String query = "UPDATE rooms SET room_type = ?, floor = ?, capacity = ?, price_per_night = ?, amenities = ?, " +
                       "room_image = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE room_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, room.getRoomType());
            pstmt.setInt(2, room.getFloor());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setBigDecimal(4, room.getPricePerNight());
            pstmt.setString(5, room.getAmenities());
            pstmt.setString(6, room.getRoomImage());
            pstmt.setString(7, room.getStatus());
            pstmt.setInt(8, room.getRoomId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error updating room: " + e.getMessage(), e);
        }
    }
    
    /**
     * Updates room status (Available, Occupied, Maintenance, Cleaning)
     * 
     * @param roomId the room ID
     * @param status the new status
     * @return true if update successful, false otherwise
     * @throws HotelException if update fails
     */
    public boolean updateRoomStatus(int roomId, String status) throws HotelException {
        String query = "UPDATE rooms SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE room_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, roomId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error updating room status: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deletes a room from the database
     * 
     * @param roomId the room ID to delete
     * @return true if deletion successful, false otherwise
     * @throws HotelException if deletion fails
     */
    public boolean deleteRoom(int roomId) throws HotelException {
        String query = "DELETE FROM rooms WHERE room_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, roomId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error deleting room: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves all rooms from the database
     * 
     * @return List of all Room objects
     * @throws HotelException if retrieval fails
     */
    public List<Room> getAllRooms() throws HotelException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms ORDER BY room_number";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving all rooms: " + e.getMessage(), e);
        }
        return rooms;
    }
    
    /**
     * Retrieves rooms by type
     * Valid types: Single Standard, Double Standard, Double Deluxe, Suite Deluxe
     * 
     * @param roomType the room type to filter by
     * @return List of Room objects matching the type
     * @throws HotelException if retrieval fails
     */
    public List<Room> getRoomsByType(String roomType) throws HotelException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE room_type = ? ORDER BY room_number";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, roomType);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving rooms by type: " + e.getMessage(), e);
        }
        return rooms;
    }
    
    /**
     * Retrieves rooms by capacity
     * 
     * @param capacity the minimum capacity to filter by
     * @return List of Room objects with capacity >= specified
     * @throws HotelException if retrieval fails
     */
    public List<Room> getRoomsByCapacity(int capacity) throws HotelException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE capacity >= ? ORDER BY room_number";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, capacity);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving rooms by capacity: " + e.getMessage(), e);
        }
        return rooms;
    }
    
    /**
     * Retrieves rooms by price range
     * 
     * @param minPrice minimum price per night
     * @param maxPrice maximum price per night
     * @return List of Room objects within price range
     * @throws HotelException if retrieval fails
     */
    public List<Room> getRoomsByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) throws HotelException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE price_per_night BETWEEN ? AND ? ORDER BY price_per_night";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setBigDecimal(1, minPrice);
            pstmt.setBigDecimal(2, maxPrice);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving rooms by price range: " + e.getMessage(), e);
        }
        return rooms;
    }
    
    /**
     * Retrieves rooms by status
     * Valid statuses: Available, Occupied, Maintenance, Cleaning
     * 
     * @param status the status to filter by
     * @return List of Room objects with matching status
     * @throws HotelException if retrieval fails
     */
    public List<Room> getRoomsByStatus(String status) throws HotelException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE status = ? ORDER BY room_number";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving rooms by status: " + e.getMessage(), e);
        }
        return rooms;
    }
    
    /**
     * Retrieves available rooms (status = 'Available')
     * 
     * @return List of available Room objects
     * @throws HotelException if retrieval fails
     */
    public List<Room> getAvailableRooms() throws HotelException {
        return getRoomsByStatus("Available");
    }
    
    /**
     * Gets count of available rooms
     * 
     * @return number of available rooms
     * @throws HotelException if query fails
     */
    public int getAvailableRoomsCount() throws HotelException {
        String query = "SELECT COUNT(*) FROM rooms WHERE status = 'Available'";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new HotelException("Error counting available rooms: " + e.getMessage(), e);
        }
        return 0;
    }
    
    /**
     * Maps a ResultSet row to a Room object
     * Helper method used by query methods
     * 
     * @param rs ResultSet containing room data
     * @return Room object populated with data from ResultSet
     * @throws SQLException if data extraction fails
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(rs.getString("room_type"));
        room.setFloor(rs.getInt("floor"));
        room.setCapacity(rs.getInt("capacity"));
        room.setPricePerNight(rs.getBigDecimal("price_per_night"));
        room.setAmenities(rs.getString("amenities"));
        room.setRoomImage(rs.getString("room_image"));
        room.setStatus(rs.getString("status"));
        room.setCreatedAt(rs.getTimestamp("created_at"));
        room.setUpdatedAt(rs.getTimestamp("updated_at"));
        return room;
    }
}
