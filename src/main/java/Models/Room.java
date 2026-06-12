package Models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Room - Room Model Class
 * 
 * Represents a hotel room with its properties, status, and amenities.
 * Used for room management and availability checking.
 * 
 * Properties include room details, pricing, amenities, status, and timestamps.
 * 
 * @author Hotel Reservation System Team
 * @version 2.0.0
 */
public class Room {
    
    private int roomId;
    private String roomNumber;
    private String roomType;
    private int floor;
    private int capacity;
    private BigDecimal pricePerNight;  // Fixed: Changed from double to BigDecimal for currency precision
    private String amenities;
    private String roomImage;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Room() {
    }
    
    /**
     * Constructor with essential room information
     * 
     * @param roomNumber the room number
     * @param roomType the type of room
     * @param floor the floor number
     * @param capacity the room capacity (number of guests)
     * @param pricePerNight the price per night
     */
    public Room(String roomNumber, String roomType, int floor, int capacity, BigDecimal pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.floor = floor;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.status = "Available";
    }
    
    /**
     * Constructor with all details
     * 
     * @param roomNumber the room number
     * @param roomType the type of room
     * @param floor the floor number
     * @param capacity the room capacity
     * @param pricePerNight the price per night
     * @param amenities the amenities description
     * @param status the current room status
     */
    public Room(String roomNumber, String roomType, int floor, int capacity, 
                BigDecimal pricePerNight, String amenities, String status) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.floor = floor;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
        this.status = status;
    }
    
    // ============ GETTERS & SETTERS ============
    
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    
    public int getFloor() {
        return floor;
    }
    
    public void setFloor(int floor) {
        this.floor = floor;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    /**
     * Get price per night as BigDecimal
     * Uses BigDecimal for precise currency calculations
     * 
     * @return price per night
     */
    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }
    
    /**
     * Set price per night as BigDecimal
     * 
     * @param pricePerNight the price per night
     */
    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    
    /**
     * Set price per night from double (for convenience)
     * Converts double to BigDecimal with 2 decimal places
     * 
     * @param pricePerNight the price per night as double
     */
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = new BigDecimal(pricePerNight).setScale(2, java.math.RoundingMode.HALF_UP);
    }
    
    public String getAmenities() {
        return amenities;
    }
    
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
    
    public String getRoomImage() {
        return roomImage;
    }
    
    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ============ UTILITY METHODS ============
    
    /**
     * Check if room is available
     * 
     * @return true if room status is "Available"
     */
    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(this.status);
    }
    
    /**
     * Check if room is occupied
     * 
     * @return true if room status is "Occupied"
     */
    public boolean isOccupied() {
        return "Occupied".equalsIgnoreCase(this.status);
    }
    
    /**
     * Check if room is under maintenance
     * 
     * @return true if room status is "Maintenance"
     */
    public boolean isUnderMaintenance() {
        return "Maintenance".equalsIgnoreCase(this.status);
    }
    
    /**
     * Check if room is being cleaned
     * 
     * @return true if room status is "Cleaning"
     */
    public boolean isCleaning() {
        return "Cleaning".equalsIgnoreCase(this.status);
    }
    
    /**
     * Get formatted price for display
     * 
     * @return formatted price string (e.g., "PHP 5,000.00")
     */
    public String getFormattedPrice() {
        return Utilities.CurrencyUtil.formatCurrency(pricePerNight);
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", floor=" + floor +
                ", capacity=" + capacity +
                ", pricePerNight=" + pricePerNight +
                ", amenities='" + amenities + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
