package com.hotelreservationsystem.model;

import java.time.LocalDateTime;

/**
 * Room - Room Model Class
 * 
 * Represents a hotel room with its properties, status, and amenities.
 * Used for room management and availability checking.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class Room {
    
    private int roomId;
    private String roomNumber;
    private String roomType;
    private int floor;
    private int capacity;
    private double pricePerNight;
    private String amenities;
    private String roomImage;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ============ CONSTRUCTORS ============
    public Room() {}
    
    public Room(String roomNumber, String roomType, int floor, int capacity, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.floor = floor;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.status = "Available";
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
    
    public double getPricePerNight() {
        return pricePerNight;
    }
    
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
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
    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(this.status);
    }
    
    public boolean isOccupied() {
        return "Occupied".equalsIgnoreCase(this.status);
    }
    
    public boolean isUnderMaintenance() {
        return "Maintenance".equalsIgnoreCase(this.status);
    }
    
    public boolean isCleaning() {
        return "Cleaning".equalsIgnoreCase(this.status);
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
                ", status='" + status + '\'' +
                '}';
    }
}
