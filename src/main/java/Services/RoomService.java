package Services;

import DAO.ReservationDAO;
import DAO.RoomDAO;
import Models.Room;
import Utilities.HotelException;
import Utilities.ValidationUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private ReservationDAO reservationDAO = new ReservationDAO();

    /**
     * Add new room to inventory
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
     */
    public Room getRoomById(int roomId) throws HotelException {
        return roomDAO.getRoomById(roomId);
    }

    /**
     * Get room by room number
     */
    public Room getRoomByNumber(String roomNumber) throws HotelException {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new HotelException("Room number cannot be empty");
        }
        return roomDAO.getRoomByNumber(roomNumber);
    }

    /**
     * Get all rooms
     */
    public List<Room> getAllRooms() throws HotelException {
        return roomDAO.getAllRooms();
    }

    /**
     * Get rooms by type
     */
    public List<Room> getRoomsByType(String roomType) throws HotelException {
        if (!isValidRoomType(roomType)) {
            throw new HotelException("Invalid room type");
        }
        return roomDAO.getRoomsByType(roomType);
    }

    /**
     * Get rooms by minimum capacity
     */
    public List<Room> getRoomsByCapacity(int capacity) throws HotelException {
        if (capacity < 1 || capacity > 10) {
            throw new HotelException("Capacity must be between 1 and 10");
        }
        return roomDAO.getRoomsByCapacity(capacity);
    }

    /**
     * Get rooms within price range
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
     * Get available rooms (status = 'Available')
     */
    public List<Room> getAvailableRooms() throws HotelException {
        return roomDAO.getAvailableRooms();
    }

    /**
     * Get count of available rooms
     */
    public int getAvailableRoomsCount() throws HotelException {
        return roomDAO.getAvailableRoomsCount();
    }

    /**
     * Update room information
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
     */
    public boolean updateRoomStatus(int roomId, String status) throws HotelException {
        if (!isValidRoomStatus(status)) {
            throw new HotelException("Invalid status. Must be: Available, Occupied, Maintenance, Cleaning");
        }
        return roomDAO.updateRoomStatus(roomId, status);
    }

    /**
     * Delete room from inventory
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
     */
    private boolean isValidRoomStatus(String status) {
        return status != null &&
               (status.equals("Available") ||
                status.equals("Occupied") ||
                status.equals("Maintenance") ||
                status.equals("Cleaning"));
    }

    // =============================================================
    //  NEW: Availability by date (for guest search)
    // =============================================================
    /**
     * Get rooms of a specific type that are available for the given dates
     * and have capacity >= minCapacity.
     */
    public List<Room> getAvailableRoomsByTypeAndDate(String roomType, int minCapacity,
                                                     LocalDate checkIn, LocalDate checkOut) throws HotelException {
        List<Room> allRooms = roomDAO.getRoomsByType(roomType);
        return allRooms.stream()
            .filter(r -> r.getCapacity() >= minCapacity)
            .filter(r -> {
                try {
                    return reservationDAO.isRoomAvailable(r.getRoomId(), checkIn, checkOut);
                } catch (HotelException e) {
                    return false;
                }
            })
            .collect(Collectors.toList());
    }

    // =============================================================
    //  ADMIN CONVENIENCE METHODS (added to fix compilation errors)
    // =============================================================

    /**
     * Alias for getAllRooms() – used by admin panels.
     */
    public List<Room> findAllRooms() throws HotelException {
        return getAllRooms();
    }

    /**
     * Check if a room number already exists.
     */
    public boolean roomExists(String roomNumber) throws HotelException {
        return getRoomByNumber(roomNumber) != null;
    }

    /**
     * Add room using six string parameters (admin UI style).
     */
    public Room addRoom(String roomNumber, String roomType, String floorStr,
                        String capacityStr, String priceStr, String status) throws HotelException {
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setFloor(Integer.parseInt(floorStr));
        room.setCapacity(Integer.parseInt(capacityStr));
        room.setPricePerNight(new BigDecimal(priceStr));
        room.setStatus(status);
        room.setAmenities("");
        room.setRoomImage("");
        return addRoom(room);
    }

    /**
     * Update room using five string parameters (admin UI style).
     * Finds the room by number and updates type, floor, capacity, price.
     * Status is left unchanged.
     */
    public Room updateRoom(String roomNumber, String roomType, String floorStr,
                           String capacityStr, String priceStr) throws HotelException {
        Room existing = getRoomByNumber(roomNumber);
        if (existing == null) {
            throw new HotelException("Room not found");
        }
        existing.setRoomType(roomType);
        existing.setFloor(Integer.parseInt(floorStr));
        existing.setCapacity(Integer.parseInt(capacityStr));
        existing.setPricePerNight(new BigDecimal(priceStr));
        // keep status, amenities, image as they were
        updateRoom(existing);
        return existing;
    }
}