package com.hotelreservationsystem.gui.guest;

import com.hotelreservationsystem.SessionManager;
import com.hotelreservationsystem.dao.GuestDAO;
import com.hotelreservationsystem.dao.UserDAO;
import com.hotelreservationsystem.model.Guest;
import com.hotelreservationsystem.model.User;
import com.hotelreservationsystem.service.UserService;
import com.hotelreservationsystem.util.HotelException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * GuestProfilePanel - Guest Profile Management
 * 
 * Allows guests to view and edit their profile information and change password.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class GuestProfilePanel extends JPanel {
    
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton updateProfileButton;
    private JButton changePasswordButton;
    private Guest currentGuest;
    
    public GuestProfilePanel() {
        setLayout(null);
        loadGuestProfile();
        initializeComponents();
    }
    
    private void loadGuestProfile() {
        try {
            int userId = SessionManager.getInstance().getCurrentUserId();
            currentGuest = GuestDAO.getGuestByUserId(userId);
            if (currentGuest == null) {
                JOptionPane.showMessageDialog(this, "Guest profile not found");
            }
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeComponents() {
        // Profile section
        JLabel profileLabel = new JLabel("My Profile");
        profileLabel.setBounds(30, 20, 200, 30);
        profileLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(profileLabel);
        
        // First name
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(30, 70, 100, 20);
        add(firstNameLabel);
        
        firstNameField = new JTextField();
        firstNameField.setBounds(140, 70, 200, 25);
        if (currentGuest != null) firstNameField.setText(currentGuest.getFirstName());
        add(firstNameField);
        
        // Last name
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(360, 70, 100, 20);
        add(lastNameLabel);
        
        lastNameField = new JTextField();
        lastNameField.setBounds(470, 70, 200, 25);
        if (currentGuest != null) lastNameField.setText(currentGuest.getLastName());
        add(lastNameField);
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 110, 100, 20);
        add(emailLabel);
        
        emailField = new JTextField();
        emailField.setBounds(140, 110, 200, 25);
        if (currentGuest != null) emailField.setText(currentGuest.getEmail());
        add(emailField);
        
        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(360, 110, 100, 20);
        add(phoneLabel);
        
        phoneField = new JTextField();
        phoneField.setBounds(470, 110, 200, 25);
        if (currentGuest != null) phoneField.setText(currentGuest.getPhoneNumber());
        add(phoneField);
        
        // Address
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(30, 150, 100, 20);
        add(addressLabel);
        
        addressArea = new JTextArea();
        addressArea.setBounds(140, 150, 530, 60);
        if (currentGuest != null) addressArea.setText(currentGuest.getAddress());
        add(addressArea);
        
        // Update profile button
        updateProfileButton = new JButton("Update Profile");
        updateProfileButton.setBounds(140, 230, 150, 35);
        updateProfileButton.addActionListener(e -> updateProfile());
        add(updateProfileButton);
        
        // Password section
        JLabel passwordLabel = new JLabel("Change Password");
        passwordLabel.setBounds(30, 290, 200, 30);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(passwordLabel);
        
        // Current password
        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordLabel.setBounds(30, 330, 120, 20);
        add(currentPasswordLabel);
        
        currentPasswordField = new JPasswordField();
        currentPasswordField.setBounds(160, 330, 200, 25);
        add(currentPasswordField);
        
        // New password
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setBounds(30, 370, 120, 20);
        add(newPasswordLabel);
        
        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(160, 370, 200, 25);
        add(newPasswordField);
        
        // Confirm password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(30, 410, 120, 20);
        add(confirmPasswordLabel);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(160, 410, 200, 25);
        add(confirmPasswordField);
        
        // Change password button
        changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(160, 450, 150, 35);
        changePasswordButton.addActionListener(e -> changePassword());
        add(changePasswordButton);
    }
    
    private void updateProfile() {
        try {
            if (currentGuest != null) {
                currentGuest.setFirstName(firstNameField.getText());
                currentGuest.setLastName(lastNameField.getText());
                currentGuest.setEmail(emailField.getText());
                currentGuest.setPhoneNumber(phoneField.getText());
                currentGuest.setAddress(addressArea.getText());
                
                GuestDAO.updateGuest(currentGuest);
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error updating profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void changePassword() {
        try {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            int userId = SessionManager.getInstance().getCurrentUserId();
            UserService.changePassword(userId, currentPassword, newPassword, confirmPassword);
            
            JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
