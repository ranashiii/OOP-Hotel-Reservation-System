package HotelReservationMainSystem;

import Models.User;

/**
 * SessionManager - Singleton session management for logged-in users
 * 
 * Tracks current user information and manages user session state.
 * Used throughout the application to access current user data.
 */
public class SessionManager {
    
    private static SessionManager instance;
    private User currentUser;
    private String currentUsername;
    private String currentAccessLevel;
    private int currentUserId;
    
    private SessionManager() {}
    
    /**
     * Get singleton instance of SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Initialize session with logged-in user
     */
    public void initializeSession(User user) {
        this.currentUser = user;
        this.currentUsername = user.getUsername();
        this.currentAccessLevel = user.getAccessLevel();
        this.currentUserId = user.getUserId();
    }
    
    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get current username
     */
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    /**
     * Get current access level
     */
    public String getCurrentAccessLevel() {
        return currentAccessLevel;
    }
    
    /**
     * Get current user ID
     */
    public int getCurrentUserId() {
        return currentUserId;
    }
    
    /**
     * Check if user is admin
     */
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(currentAccessLevel);
    }
    
    /**
     * Check if user is receptionist
     */
    public boolean isReceptionist() {
        return "Receptionist".equalsIgnoreCase(currentAccessLevel);
    }
    
    /**
     * Check if user is guest
     */
    public boolean isGuest() {
        return "Guest".equalsIgnoreCase(currentAccessLevel);
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null && currentUserId > 0;
    }
    
    /**
     * Logout - clear session
     */
    public void logout() {
        currentUser = null;
        currentUsername = null;
        currentAccessLevel = null;
        currentUserId = 0;
    }
    
    /**
     * Clear all session data
     */
    public void clearSession() {
        logout();
    }
}
