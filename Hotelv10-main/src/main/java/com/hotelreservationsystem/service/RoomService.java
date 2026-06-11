package com.hotelreservationsystem.service;

import com.hotelreservationsystem.dao.RoomDAO;
import com.hotelreservationsystem.model.Room;
import com.hotelreservationsystem.util.HotelException;
import com.hotelreservationsystem.util.ValidationUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Room Service Layer
 * 
 * Handles all room-related business logic including inventory management,
 * availability checking, filtering, and status updates.
 */
public class RoomService {
    private RoomDAO roomDAO = new RoomDAO();
    
    /**
     * Add new room to inventory
     * 
     * @param room the room object with details
     * @return Room object if creation successful
     * @throws HotelException if creation fails or validation fails
     */
    public Room addRoom(Room room) throws HotelException {
        if (room == null) {
            throw new HotelException("Room object cannot be null");
        }
        
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new HotelException("Room number cannot be empty");
        }
        
        if (!isValidRoomType(room.getRoomType())) {
            throw new HotelException("Invalid room type. Must be: Single Standard, Double Standard, Double Deluxe, Suite Deluxe");
        }
        
        if (room.getFloor() < 1 || room.getFloor() > 100) {
            throw new HotelException("Floor must be between 1 and 100");
        }
        
        if (room.getCapacity() < 1 || room.getCapacity() > 10) {
            throw new HotelException("Capacity must be between 1 and 10 guests");
        }
        
        if (room.getPricePerNight() == null || room.getPricePerNight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new HotelException("Price per night must be greater than 0");
        }
        
        if (room.getPricePerNight().compareTo(new BigDecimal("999999.99")) > 0) {
            throw new HotelException("Price per night cannot exceed 999999.99");
        }
        
        int roomId = roomDAO.createRoom(room);
        room.setRoomId(roomId);
        return room;
    }
    
    /**
     * Get room by ID
     * 
     * @param roomId the room ID
     * @return Room object if found, null otherwise
     * @throws HotelException if query fails
     */
    public Room getRoomById(int roomId) throws HotelException {
        return roomDAO.getRoomById(roomId);
    }
    
    /**
     * Get room by room number
     * 
     * @param roomNumber the room number
     * @return Room object if found, null otherwise
     * @throws HotelException if query fails
     */
    public Room getRoomByNumber(String roomNumber) throws HotelException {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new HotelException("Room number cannot be empty");
        }
        return roomDAO.getRoomByNumber(roomNumber);
    }
    
    /**
     * Get all rooms
     * 
     * @return List of all Room objects
     * @throws HotelException if query fails
     */
    public List<Room> getAllRooms() throws HotelException {
        return roomDAO.getAllRooms();
    }
    
    /**
     * Get rooms by type
     * 
     * @param roomType the room type
     * @return List of Room objects with matching type
     * @throws HotelException if query fails
     */
    public List<Room> getRoomsByType(String roomType) throws HotelException {
        if (!isValidRoomType(roomType)) {
            throw new HotelException("Invalid room type");
        }
        return roomDAO.getRoomsByType(roomType);
    }
    
    /**
     * Get rooms by minimum capacity
     * 
     * @param capacity the minimum capacity
     * @return List of Room objects with capacity >= specified
     * @throws HotelException if query fails
     */
    public List<Room> getRoomsByCapacity(int capacity) throws HotelException {
        if (capacity < 1 || capacity > 10) {
            throw new HotelException("Capacity must be between 1 and 10");
        }
        return roomDAO.getRoomsByCapacity(capacity);
    }
    
    /**
     * Get rooms within price range
     * 
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @return List of Room objects within price range
     * @throws HotelException if query fails or validation fails
     */
    public List<Room> getRoomsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) throws HotelException {
        if (minPrice == null || maxPrice == null) {
            throw new HotelException("Price range cannot be null");
        }
        
        if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new HotelException("Prices cannot be negative");
        }
        
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new HotelException("Minimum price cannot be greater than maximum price");
        }
        
        return roomDAO.getRoomsByPriceRange(minPrice, maxPrice);
    }
    
    /**
     * Get available rooms
     * 
     * @return List of available Room objects
     * @throws HotelException if query fails
     */
    public List<Room> getAvailableRooms() throws HotelException {
        return roomDAO.getAvailableRooms();
    }
    
    /**
     * Get count of available rooms
     * 
     * @return number of available rooms
     * @throws HotelException if query fails
     */
    public int getAvailableRoomsCount() throws HotelException {
        return roomDAO.getAvailableRoomsCount();
    }
    
    /**
     * Update room information
     * 
     * @param room the room object with updated information
     * @return true if update successful
     * @throws HotelException if update fails or validation fails
     */
    public boolean updateRoom(Room room) throws HotelException {
        if (room == null || room.getRoomId() == 0) {
            throw new HotelException("Invalid room object");
        }
        
        if (!isValidRoomType(room.getRoomType())) {
            throw new HotelException("Invalid room type");
        }
        
        if (room.getCapacity() < 1 || room.getCapacity() > 10) {
            throw new HotelException("Capacity must be between 1 and 10");
        }
        
        return roomDAO.updateRoom(room);
    }
    
    /**
     * Update room status
     * 
     * @param roomId the room ID
     * @param status the new status
     * @return true if update successful
     * @throws HotelException if update fails or validation fails
     */
    public boolean updateRoomStatus(int roomId, String status) throws HotelException {
        if (!isValidRoomStatus(status)) {
            throw new HotelException("Invalid status. Must be: Available, Occupied, Maintenance, Cleaning");
        }
        return roomDAO.updateRoomStatus(roomId, status);
    }
    
    /**
     * Delete room from inventory
     * 
     * @param roomId the room ID
     * @return true if deletion successful
     * @throws HotelException if deletion fails
     */
    public boolean deleteRoom(int roomId) throws HotelException {
        Room room = roomDAO.getRoomById(roomId);
        if (room == null) {
            throw new HotelException("Room not found");
        }
        return roomDAO.deleteRoom(roomId);
    }
    
    /**
     * Validate room type
     * Valid types: Single Standard, Double Standard, Double Deluxe, Suite Deluxe
     * 
     * @param roomType the room type to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidRoomType(String roomType) {
        return roomType != null && 
               (roomType.equals("Single Standard") || 
                roomType.equals("Double Standard") || 
                roomType.equals("Double Deluxe") || 
                roomType.equals("Suite Deluxe"));
    }
    
    /**
     * Validate room status
     * Valid statuses: Available, Occupied, Maintenance, Cleaning
     * 
     * @param status the status to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidRoomStatus(String status) {
        return status != null && 
               (status.equals("Available") || 
                status.equals("Occupied") || 
                status.equals("Maintenance") || 
                status.equals("Cleaning"));
    }
}
