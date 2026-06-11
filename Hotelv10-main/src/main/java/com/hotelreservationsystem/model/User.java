package com.hotelreservationsystem.model;

import java.time.LocalDateTime;

/**
 * User - User Model Class
 * 
 * Represents a system user with authentication credentials and access level.
 * Used for login, registration, and user management.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class User {
    
    private int userId;
    private String username;
    private String password;
    private String email;
    private String accessLevel;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ============ CONSTRUCTORS ============
    public User() {}
    
    public User(String username, String password, String email, String accessLevel) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.accessLevel = accessLevel;
        this.isActive = true;
    }
    
    public User(int userId, String username, String password, String email, 
                String accessLevel, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.accessLevel = accessLevel;
        this.isActive = isActive;
    }
    
    // ============ GETTERS & SETTERS ============
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAccessLevel() {
        return accessLevel;
    }
    
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
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
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(this.accessLevel);
    }
    
    public boolean isReceptionist() {
        return "Receptionist".equalsIgnoreCase(this.accessLevel);
    }
    
    public boolean isGuest() {
        return "Guest".equalsIgnoreCase(this.accessLevel);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", accessLevel='" + accessLevel + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
