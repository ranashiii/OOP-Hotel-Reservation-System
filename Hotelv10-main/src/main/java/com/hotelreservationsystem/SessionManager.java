package com.hotelreservationsystem;

import com.hotelreservationsystem.model.User;
import com.hotelreservationsystem.util.HotelException;

/**
 * SessionManager - User Session Management (Singleton Pattern)
 * 
 * Manages the current user session throughout the application.
 * Stores and provides access to logged-in user information including:
 * - User ID
 * - Username
 * - User role/access level
 * - Email
 * 
 * This class uses the Singleton pattern to ensure only one session
 * exists at a time across the entire application.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class SessionManager {
    
    // Singleton instance
    private static SessionManager instance;
    
    // Current logged-in user
    private User currentUser;
    
    /**
     * Private constructor to prevent instantiation
     */
    private SessionManager() {
        this.currentUser = null;
    }
    
    /**
     * Get the singleton instance of SessionManager
     * 
     * @return SessionManager instance
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Login user - Set current user session
     * 
     * @param user the User object to set as current session
     * @throws HotelException if user is null
     */
    public void login(User user) throws HotelException {
        if (user == null) {
            throw new HotelException("Cannot login with null user");
        }
        this.currentUser = user;
    }
    
    /**
     * Logout user - Clear current session
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Get current logged-in user
     * 
     * @return User object or null if not logged in
     */
    public User getCurrentUser() {
        return this.currentUser;
    }
    
    /**
     * Check if user is logged in
     * 
     * @return true if user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return this.currentUser != null;
    }
    
    /**
     * Get current user ID
     * 
     * @return user ID or -1 if not logged in
     */
    public int getCurrentUserId() {
        if (this.currentUser == null) {
            return -1;
        }
        return this.currentUser.getUserId();
    }
    
    /**
     * Get current user's username
     * 
     * @return username or null if not logged in
     */
    public String getCurrentUsername() {
        if (this.currentUser == null) {
            return null;
        }
        return this.currentUser.getUsername();
    }
    
    /**
     * Get current user's access level
     * 
     * @return access level or null if not logged in
     */
    public String getCurrentAccessLevel() {
        if (this.currentUser == null) {
            return null;
        }
        return this.currentUser.getAccessLevel();
    }
    
    /**
     * Get current user's email
     * 
     * @return email or null if not logged in
     */
    public String getCurrentEmail() {
        if (this.currentUser == null) {
            return null;
        }
        return this.currentUser.getEmail();
    }
    
    /**
     * Check if current user is an Admin
     * 
     * @return true if access level is "Admin", false otherwise
     */
    public boolean isAdmin() {
        if (this.currentUser == null) {
            return false;
        }
        return "Admin".equalsIgnoreCase(this.currentUser.getAccessLevel());
    }
    
    /**
     * Check if current user is a Receptionist
     * 
     * @return true if access level is "Receptionist", false otherwise
     */
    public boolean isReceptionist() {
        if (this.currentUser == null) {
            return false;
        }
        return "Receptionist".equalsIgnoreCase(this.currentUser.getAccessLevel());
    }
    
    /**
     * Check if current user is a Guest
     * 
     * @return true if access level is "Guest", false otherwise
     */
    public boolean isGuest() {
        if (this.currentUser == null) {
            return false;
        }
        return "Guest".equalsIgnoreCase(this.currentUser.getAccessLevel());
    }
    
    /**
     * Clear all session data (typically called on logout)
     */
    public void clearSession() {
        this.currentUser = null;
    }
    
    /**
     * Get session information as string
     * 
     * @return formatted session information
     */
    @Override
    public String toString() {
        if (this.currentUser == null) {
            return "Session: Not Logged In";
        }
        return "Session {" +
                "userId=" + this.currentUser.getUserId() +
                ", username='" + this.currentUser.getUsername() + '\'' +
                ", accessLevel='" + this.currentUser.getAccessLevel() + '\'' +
                ", email='" + this.currentUser.getEmail() + '\'' +
                '}';
    }
}
