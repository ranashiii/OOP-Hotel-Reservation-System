package com.mycompany.HotelReservationApp.mainsystem.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Room Model Class
 * Represents a hotel room with all its properties
 * Handles room information, status, and pricing
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int roomID;
    private String roomNumber;
    private String roomType; // "Single Standard", "Double Standard", "Double Deluxe", "Suite Deluxe"
    private int floor;
    private int capacity;
    private double pricePerNight;
    private String amenities;
    private String roomImage;
    private String status; // "Available", "Occupied", "Maintenance", "Cleaning"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Constructor for Room
     */
    public Room(int roomID, String roomNumber, String roomType, int floor,
                int capacity, double pricePerNight) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.floor = floor;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.status = "Available";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getRoomID() {
        return roomID;
    }
    
    public void setRoomID(int roomID) {
        this.roomID = roomID;
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
        this.updatedAt = LocalDateTime.now();
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
        this.updatedAt = LocalDateTime.now();
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
    
    /**
     * Check if room is available for booking
     */
    public boolean isAvailable() {
        return status.equals("Available");
    }
    
    /**
     * Check if room is occupied
     */
    public boolean isOccupied() {
        return status.equals("Occupied");
    }
    
    /**
     * Check if room is under maintenance
     */
    public boolean isUnderMaintenance() {
        return status.equals("Maintenance");
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", floor=" + floor +
                ", capacity=" + capacity +
                ", pricePerNight=" + pricePerNight +
                ", status='" + status + '\'' +
                '}';
    }
}