package com.hotelreservationsystem.service;

import com.hotelreservationsystem.dao.UserDAO;
import com.hotelreservationsystem.model.User;
import com.hotelreservationsystem.util.HotelException;
import com.hotelreservationsystem.util.ValidationUtil;

/**
 * User Service Layer
 * 
 * Handles all user-related business logic including authentication,
 * registration, account management, and validation.
 * Acts as intermediary between UI and DAO layers.
 */
public class UserService {
    private UserDAO userDAO = new UserDAO();
    
    /**
     * Authenticate user with username and password
     * 
     * @param username the username
     * @param password the password
     * @return User object if authentication successful, null otherwise
     * @throws HotelException if authentication fails
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
     * 
     * @param username the username
     * @param password the password
     * @param confirmPassword the password confirmation
     * @param email the email address
     * @param accessLevel the access level
     * @return User object if registration successful
     * @throws HotelException if registration fails or validation fails
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
     * Change user password
     * 
     * @param userId the user ID
     * @param currentPassword the current password
     * @param newPassword the new password
     * @param confirmPassword the new password confirmation
     * @return true if password changed successfully
     * @throws HotelException if change fails or validation fails
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
     * 
     * @param userId the user ID
     * @return User object if found, null otherwise
     * @throws HotelException if query fails
     */
    public User getUserById(int userId) throws HotelException {
        return userDAO.getUserById(userId);
    }
    
    /**
     * Get user by username
     * 
     * @param username the username
     * @return User object if found, null otherwise
     * @throws HotelException if query fails
     */
    public User getUserByUsername(String username) throws HotelException {
        return userDAO.getUserByUsername(username);
    }
    
    /**
     * Update user information
     * 
     * @param user the user object with updated information
     * @return true if update successful
     * @throws HotelException if update fails
     */
    public boolean updateUser(User user) throws HotelException {
        if (user == null || user.getUserId() == 0) {
            throw new HotelException("Invalid user object");
        }
        return userDAO.updateUser(user);
    }
    
    /**
     * Delete user account
     * 
     * @param userId the user ID
     * @return true if deletion successful
     * @throws HotelException if deletion fails
     */
    public boolean deleteUser(int userId) throws HotelException {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new HotelException("User not found");
        }
        return userDAO.deleteUser(userId);
    }
}
