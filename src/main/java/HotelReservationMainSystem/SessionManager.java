package HotelReservationMainSystem;

import Models.User;
import Utilities.HotelException;


public class SessionManager {
    
    // Singleton instance
    private static SessionManager instance;
    
    // Current logged-in user
    private User currentUser;
    

    private SessionManager() {
        this.currentUser = null;
    }
    

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    

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
    

    public User getCurrentUser() {
        return this.currentUser;
    }
    

    public boolean isLoggedIn() {
        return this.currentUser != null;
    }
    

    public int getCurrentUserId() {
        if (this.currentUser == null) {
            return -1;
        }
        return this.currentUser.getUserId();
    }
    

    public String getCurrentUsername() {
        if (this.currentUser == null) {
            return null;
        }
        return this.currentUser.getUsername();
    }
    

    public String getCurrentAccessLevel() {
        if (this.currentUser == null) {
            return null;
        }
        return this.currentUser.getAccessLevel();
    }
    

    public String getCurrentEmail() {
        if (this.currentUser == null) {
            return null;
        }
        return this.currentUser.getEmail();
    }
    

    public boolean isAdmin() {
        if (this.currentUser == null) {
            return false;
        }
        return "Admin".equalsIgnoreCase(this.currentUser.getAccessLevel());
    }
    

    public boolean isReceptionist() {
        if (this.currentUser == null) {
            return false;
        }
        return "Receptionist".equalsIgnoreCase(this.currentUser.getAccessLevel());
    }
    

    public boolean isGuest() {
        if (this.currentUser == null) {
            return false;
        }
        return "Guest".equalsIgnoreCase(this.currentUser.getAccessLevel());
    }
    

    public void clearSession() {
        this.currentUser = null;
    }
    

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
