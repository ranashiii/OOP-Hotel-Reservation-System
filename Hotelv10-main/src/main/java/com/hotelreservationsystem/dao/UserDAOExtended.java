package com.hotelreservationsystem.dao;

import com.hotelreservationsystem.config.DBConfig;
import com.hotelreservationsystem.model.User;
import com.hotelreservationsystem.util.HotelException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Extended Data Access Object for User operations
 * 
 * Handles additional user queries including listing all users and filtering.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class UserDAOExtended {
    
    /**
     * Get all users
     * 
     * @return list of all users
     * @throws HotelException if database error occurs
     */
    public static List<User> getAllUsers() throws HotelException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new HotelException("Error fetching users: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    /**
     * Get users by access level
     * 
     * @param accessLevel the access level
     * @return list of users
     * @throws HotelException if database error occurs
     */
    public static List<User> getUsersByAccessLevel(String accessLevel) throws HotelException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE access_level = ?";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accessLevel);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            throw new HotelException("Error fetching users: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setAccessLevel(rs.getString("access_level"));
        user.setActive(rs.getBoolean("is_active"));
        user.setCreatedAt(rs.getTimestamp("created_at") != null ? 
            rs.getTimestamp("created_at").toLocalDateTime() : null);
        user.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
            rs.getTimestamp("updated_at").toLocalDateTime() : null);
        return user;
    }
}
