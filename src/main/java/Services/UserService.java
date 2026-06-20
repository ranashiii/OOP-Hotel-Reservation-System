package Services;

import DAO.UserDAO;
import Models.User;
import Utilities.HotelException;
import Utilities.ValidationUtil;
import java.util.List;

/**
 * User Service Layer
 * 
 * Handles all user-related business logic including authentication,
 * registration, account management, and validation.
 */
public class UserService {
    private UserDAO userDAO = new UserDAO();

    /**
     * Authenticate user with username and password
     */
    public User login(String username, String password) throws HotelException {
        if (username == null || username.trim().isEmpty()) {
            throw new HotelException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new HotelException("Password cannot be empty");
        }
        User user = userDAO.authenticateUser(username, password);
        if (user == null) {
            throw new HotelException("Invalid username or password");
        }
        return user;
    }

    /**
     * Register a new user account
     */
    public User registerUser(String username, String password, String confirmPassword,
                             String email, String accessLevel) throws HotelException {
        if (!ValidationUtil.validateUsername(username)) {
            throw new HotelException("Username must be 5-20 alphanumeric characters");
        }
        if (!ValidationUtil.validatePassword(password)) {
            throw new HotelException("Password must be at least 8 characters with uppercase, lowercase, and number");
        }
        if (!password.equals(confirmPassword)) {
            throw new HotelException("Passwords do not match");
        }
        if (!ValidationUtil.validateEmail(email)) {
            throw new HotelException("Invalid email format");
        }
        if (userDAO.usernameExists(username)) {
            throw new HotelException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setAccessLevel(accessLevel);
        user.setActive(true);
        int userId = userDAO.createUser(user);
        user.setUserId(userId);
        return user;
    }

    /**
     * Change user password (with current password verification)
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword,
                                  String confirmPassword) throws HotelException {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new HotelException("User not found");
        }
        if (!ValidationUtil.validatePassword(newPassword)) {
            throw new HotelException("New password must be at least 8 characters with uppercase, lowercase, and number");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new HotelException("New passwords do not match");
        }
        User authUser = userDAO.authenticateUser(user.getUsername(), currentPassword);
        if (authUser == null) {
            throw new HotelException("Current password is incorrect");
        }
        return userDAO.updatePassword(userId, newPassword);
    }

    /**
     * Get user by ID
     */
    public User getUserById(int userId) throws HotelException {
        return userDAO.getUserById(userId);
    }

    /**
     * Get user by username
     */
    public User getUserByUsername(String username) throws HotelException {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Update user information
     */
    public boolean updateUser(User user) throws HotelException {
        if (user == null || user.getUserId() == 0) {
            throw new HotelException("Invalid user object");
        }
        return userDAO.updateUser(user);
    }

    /**
     * Delete user account
     */
    public boolean deleteUser(int userId) throws HotelException {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new HotelException("User not found");
        }
        return userDAO.deleteUser(userId);
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() throws HotelException {
        return userDAO.getAllUsers();
    }

    // =============================================================
    //  ADMIN CONVENIENCE METHODS (added to fix compilation errors)
    // =============================================================

    public List<User> findAllUsers() throws HotelException {
        return getAllUsers();
    }

    public boolean isValidEmail(String email) {
        return ValidationUtil.validateEmail(email);
    }

    public boolean userExists(String username) throws HotelException {
        return userDAO.usernameExists(username);
    }

    public User addUser(String username, String password, String email) throws HotelException {
        return registerUser(username, password, password, email, "Guest");
    }

    public boolean removeUser(String username) throws HotelException {
        User user = getUserByUsername(username);
        if (user == null) return false;
        return deleteUser(user.getUserId());
    }

    /**
     * Simple password update without checking current password (for admin resets / guest profile).
     */
    public boolean updatePassword(int userId, String newPassword) throws HotelException {
        return userDAO.updatePassword(userId, newPassword);
    }
}