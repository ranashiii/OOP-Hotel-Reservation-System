package HotelReservationMainSystem;

import Models.User;
import Models.Guest;

/**
 * SessionManager - Singleton Session Management
 * 
 * Manages the current user session throughout the application.
 * Stores logged-in user information and guest profile when applicable.
 */
public class SessionManager {
    
    private static SessionManager instance;
    private User currentUser;
    private Guest currentGuest;
    
    /**
     * Private constructor for singleton pattern
     */
    private SessionManager() {}
    
    /**
     * Get singleton instance
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Set the current user session
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Get the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get current user ID
     */
    public int getCurrentUserId() {
        if (currentUser == null) {
            return -1;
        }
        return currentUser.getUserId();
    }
    
    /**
     * Get current user's access level
     */
    public String getCurrentUserAccessLevel() {
        if (currentUser == null) {
            return null;
        }
        return currentUser.getAccessLevel();
    }
    
    /**
     * Check if user is admin
     */
    public boolean isAdmin() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.isAdmin();
    }
    
    /**
     * Check if user is receptionist
     */
    public boolean isReceptionist() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.isReceptionist();
    }
    
    /**
     * Check if user is guest
     */
    public boolean isGuest() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.isGuest();
    }
    
    /**
     * Set current guest profile
     */
    public void setCurrentGuest(Guest guest) {
        this.currentGuest = guest;
    }
    
    /**
     * Get current guest profile
     */
    public Guest getCurrentGuest() {
        return currentGuest;
    }
    
    /**
     * Get current guest ID
     */
    public int getCurrentGuestId() {
        if (currentGuest == null) {
            return -1;
        }
        return currentGuest.getGuestId();
    }
    
    /**
     * Clear session (logout)
     */
    public void clearSession() {
        this.currentUser = null;
        this.currentGuest = null;
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
