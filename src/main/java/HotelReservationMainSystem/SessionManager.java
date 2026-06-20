package HotelReservationMainSystem;

/**
 * Manages the current user session.
 * Stores user ID, guest ID, username, and access level.
 */
public class SessionManager {

    private static SessionManager instance;
    private int userId;
    private int guestId;
    private String username;
    private String accessLevel;
    private boolean loggedIn;

    private SessionManager() {
        clearSession();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(int userId, int guestId, String username, String accessLevel) {
        this.userId = userId;
        this.guestId = guestId;
        this.username = username;
        this.accessLevel = accessLevel;
        this.loggedIn = true;
    }

    public void logout() {
        clearSession();
    }

    private void clearSession() {
        this.userId = 0;
        this.guestId = 0;
        this.username = null;
        this.accessLevel = null;
        this.loggedIn = false;
    }

    public int getUserId() { return userId; }
    public int getGuestId() { return guestId; }
    public String getCurrentUsername() { return username; }
    public String getAccessLevel() { return accessLevel; }
    public boolean isLoggedIn() { return loggedIn; }
}