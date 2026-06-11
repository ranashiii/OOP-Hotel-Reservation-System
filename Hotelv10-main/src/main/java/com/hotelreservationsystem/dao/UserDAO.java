package com.hotelreservationsystem.dao;

import com.hotelreservationsystem.config.DBConfig;
import com.hotelreservationsystem.model.User;
import com.hotelreservationsystem.util.HotelException;
import com.hotelreservationsystem.util.PasswordUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User Data Access Object (DAO)
 * 
 * Handles all database operations for User entities.
 * Manages user authentication, registration, and account management.
 * Uses PreparedStatements to prevent SQL injection attacks.
 * 
 * Operations:
 * - Create new user accounts
 * - Retrieve user by ID, username
 * - Update user information
 * - Delete user accounts
 * - Authenticate users with password verification
 * - List all users
 */
public class UserDAO {
    
    /**
     * Creates a new user in the database
     * 
     * @param user User object containing user details
     * @return generated user ID
     * @throws HotelException if creation fails
     */
    public int createUser(User user) throws HotelException {
        String query = "INSERT INTO users (username, password, email, access_level, is_active) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            // Hash password before storing
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getAccessLevel());
            pstmt.setBoolean(5, user.isActive());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new HotelException("Failed to create user: no rows affected");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new HotelException("Failed to create user: no ID generated");
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new HotelException("Username already exists: " + user.getUsername(), e);
            }
            throw new HotelException("Error creating user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Retrieves a user by user ID
     * 
     * @param userId the user ID to search for
     * @return User object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public User getUserById(int userId) throws HotelException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving user by ID: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Retrieves a user by username
     * 
     * @param username the username to search for
     * @return User object if found, null otherwise
     * @throws HotelException if database error occurs
     */
    public User getUserByUsername(String username) throws HotelException {
        String query = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving user by username: " + e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Updates an existing user's information
     * 
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     * @throws HotelException if update fails
     */
    public boolean updateUser(User user) throws HotelException {
        String query = "UPDATE users SET email = ?, access_level = ?, is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getAccessLevel());
            pstmt.setBoolean(3, user.isActive());
            pstmt.setInt(4, user.getUserId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error updating user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Changes a user's password
     * Password is hashed before storage
     * 
     * @param userId the user ID
     * @param newPassword the new password (will be hashed)
     * @return true if password changed successfully, false otherwise
     * @throws HotelException if password change fails
     */
    public boolean updatePassword(int userId, String newPassword) throws HotelException {
        String query = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error updating password: " + e.getMessage(), e);
        }
    }
    
    /**
     * Deletes a user account (soft delete - marks as inactive)
     * 
     * @param userId the user ID to delete
     * @return true if deletion successful, false otherwise
     * @throws HotelException if deletion fails
     */
    public boolean deleteUser(int userId) throws HotelException {
        String query = "UPDATE users SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new HotelException("Error deleting user: " + e.getMessage(), e);
        }
    }
    
    /**
     * Authenticates a user with username and password
     * 
     * @param username the username
     * @param password the password to verify
     * @return User object if authentication successful, null otherwise
     * @throws HotelException if authentication fails
     */
    public User authenticateUser(String username, String password) throws HotelException {
        User user = getUserByUsername(username);
        
        if (user == null) {
            return null;
        }
        
        if (!user.isActive()) {
            throw new HotelException("User account is inactive");
        }
        
        // Verify password
        if (PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }
    
    /**
     * Retrieves all users from the database
     * 
     * @return List of all User objects
     * @throws HotelException if retrieval fails
     */
    public List<User> getAllUsers() throws HotelException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving all users: " + e.getMessage(), e);
        }
        return users;
    }
    
    /**
     * Retrieves all users by access level
     * 
     * @param accessLevel the access level to filter by
     * @return List of User objects with matching access level
     * @throws HotelException if retrieval fails
     */
    public List<User> getUsersByAccessLevel(String accessLevel) throws HotelException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE access_level = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, accessLevel);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error retrieving users by access level: " + e.getMessage(), e);
        }
        return users;
    }
    
    /**
     * Checks if a username already exists in the database
     * Useful for validation during registration
     * 
     * @param username the username to check
     * @return true if username exists, false otherwise
     * @throws HotelException if check fails
     */
    public boolean usernameExists(String username) throws HotelException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error checking username existence: " + e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * Maps a ResultSet row to a User object
     * Helper method used by query methods
     * 
     * @param rs ResultSet containing user data
     * @return User object populated with data from ResultSet
     * @throws SQLException if data extraction fails
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setAccessLevel(rs.getString("access_level"));
        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}
