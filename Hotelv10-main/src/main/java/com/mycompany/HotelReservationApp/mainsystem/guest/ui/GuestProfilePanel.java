package com.mycompany.HotelReservationApp.mainsystem.guest.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GuestProfilePanel - Guest Profile Management
 * Allows guests to view and edit their personal information and account settings
 */
public class GuestProfilePanel extends JPanel implements ActionListener {
    
    private JTextField txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress;
    private JPasswordField txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    private JButton btnEditProfile, btnSaveProfile, btnChangePassword, btnCancel;
    private JLabel lblErrorMessage, lblSuccessMessage;
    private boolean isEditingProfile = false;
    
    public GuestProfilePanel() {
        setLayout(null);
        setBackground(Color.decode("#F5F5F5"));
        createComponents();
    }
    
    private void createComponents() {
        // Title Bar
        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(20, 15, 920, 50);
        titleBar.setBackground(Color.decode("#222222"));
        add(titleBar);
        
        JLabel lblTitle = new JLabel("MY PROFILE");
        lblTitle.setBounds(15, 8, 600, 34);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);
        
        // Messages
        lblErrorMessage = new JLabel();
        lblErrorMessage.setFont(new Font("Arial", Font.PLAIN, 11));
        lblErrorMessage.setForeground(new Color(244, 67, 54));
        lblErrorMessage.setBounds(20, 75, 450, 20);
        lblErrorMessage.setVisible(false);
        add(lblErrorMessage);
        
        lblSuccessMessage = new JLabel();
        lblSuccessMessage.setFont(new Font("Arial", Font.PLAIN, 11));
        lblSuccessMessage.setForeground(new Color(76, 175, 80));
        lblSuccessMessage.setBounds(490, 75, 450, 20);
        lblSuccessMessage.setVisible(false);
        add(lblSuccessMessage);
        
        // Profile Information Panel
        JPanel profilePanel = new JPanel(null);
        profilePanel.setBounds(20, 105, 440, 365);
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(profilePanel);
        
        JLabel lblProfileTitle = new JLabel("PERSONAL INFORMATION");
        lblProfileTitle.setBounds(15, 15, 400, 25);
        lblProfileTitle.setFont(new Font("Arial Black", Font.BOLD, 13));
        profilePanel.add(lblProfileTitle);
        
        // First Name
        JLabel lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(15, 50, 100, 20);
        lblFirstName.setFont(new Font("Arial", Font.BOLD, 11));
        profilePanel.add(lblFirstName);
        
        txtFirstName = new JTextField("John");
        txtFirstName.setBounds(15, 72, 200, 25);
        txtFirstName.setEditable(false);
        profilePanel.add(txtFirstName);
        
        // Last Name
        JLabel lblLastName = new JLabel("Last Name:");
        lblLastName.setBounds(225, 50, 100, 20);
        lblLastName.setFont(new Font("Arial", Font.BOLD, 11));
        profilePanel.add(lblLastName);
        
        txtLastName = new JTextField("Doe");
        txtLastName.setBounds(225, 72, 200, 25);
        txtLastName.setEditable(false);
        profilePanel.add(txtLastName);
        
        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(15, 105, 100, 20);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 11));
        profilePanel.add(lblEmail);
        
        txtEmail = new JTextField("john.doe@example.com");
        txtEmail.setBounds(15, 127, 410, 25);
        txtEmail.setEditable(false);
        profilePanel.add(txtEmail);
        
        // Phone
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(15, 160, 100, 20);
        lblPhone.setFont(new Font("Arial", Font.BOLD, 11));
        profilePanel.add(lblPhone);
        
        txtPhone = new JTextField("09123456789");
        txtPhone.setBounds(15, 182, 410, 25);
        txtPhone.setEditable(false);
        profilePanel.add(txtPhone);
        
        // Address
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(15, 215, 100, 20);
        lblAddress.setFont(new Font("Arial", Font.BOLD, 11));
        profilePanel.add(lblAddress);
        
        txtAddress = new JTextField("123 Main St, City, Country");
        txtAddress.setBounds(15, 237, 410, 50);
        txtAddress.setEditable(false);
        profilePanel.add(txtAddress);
        
        // Buttons
        btnEditProfile = new JButton("EDIT PROFILE");
        btnEditProfile.setBounds(15, 305, 195, 35);
        btnEditProfile.setBackground(new Color(33, 150, 243));
        btnEditProfile.setForeground(Color.WHITE);
        btnEditProfile.setFont(new Font("Arial", Font.BOLD, 11));
        btnEditProfile.setBorderPainted(false);
        btnEditProfile.setFocusPainted(false);
        btnEditProfile.addActionListener(this);
        profilePanel.add(btnEditProfile);
        
        btnSaveProfile = new JButton("SAVE CHANGES");
        btnSaveProfile.setBounds(220, 305, 205, 35);
        btnSaveProfile.setBackground(new Color(76, 175, 80));
        btnSaveProfile.setForeground(Color.WHITE);
        btnSaveProfile.setFont(new Font("Arial", Font.BOLD, 11));
        btnSaveProfile.setBorderPainted(false);
        btnSaveProfile.setFocusPainted(false);
        btnSaveProfile.setEnabled(false);
        btnSaveProfile.addActionListener(this);
        profilePanel.add(btnSaveProfile);
        
        // Change Password Panel
        JPanel passwordPanel = new JPanel(null);
        passwordPanel.setBounds(480, 105, 460, 365);
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(passwordPanel);
        
        JLabel lblPasswordTitle = new JLabel("CHANGE PASSWORD");
        lblPasswordTitle.setBounds(15, 15, 400, 25);
        lblPasswordTitle.setFont(new Font("Arial Black", Font.BOLD, 13));
        passwordPanel.add(lblPasswordTitle);
        
        // Current Password
        JLabel lblCurrentPassword = new JLabel("Current Password:");
        lblCurrentPassword.setBounds(15, 50, 150, 20);
        lblCurrentPassword.setFont(new Font("Arial", Font.BOLD, 11));
        passwordPanel.add(lblCurrentPassword);
        
        txtCurrentPassword = new JPasswordField();
        txtCurrentPassword.setBounds(15, 72, 425, 25);
        passwordPanel.add(txtCurrentPassword);
        
        // New Password
        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setBounds(15, 105, 150, 20);
        lblNewPassword.setFont(new Font("Arial", Font.BOLD, 11));
        passwordPanel.add(lblNewPassword);
        
        txtNewPassword = new JPasswordField();
        txtNewPassword.setBounds(15, 127, 425, 25);
        passwordPanel.add(txtNewPassword);
        
        // Confirm Password
        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setBounds(15, 160, 150, 20);
        lblConfirmPassword.setFont(new Font("Arial", Font.BOLD, 11));
        passwordPanel.add(lblConfirmPassword);
        
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(15, 182, 425, 25);
        passwordPanel.add(txtConfirmPassword);
        
        // Password Requirements
        JLabel lblRequirements = new JLabel();
        lblRequirements.setBounds(15, 215, 425, 60);
        lblRequirements.setFont(new Font("Arial", Font.PLAIN, 10));
        lblRequirements.setText(
            "<html>" +
            "Password Requirements:<br>" +
            "• Minimum 8 characters<br>" +
            "• At least one uppercase letter<br>" +
            "• At least one lowercase letter<br>" +
            "• At least one number" +
            "</html>"
        );
        passwordPanel.add(lblRequirements);
        
        // Buttons
        btnChangePassword = new JButton("CHANGE PASSWORD");
        btnChangePassword.setBounds(15, 305, 205, 35);
        btnChangePassword.setBackground(new Color(156, 39, 176));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFont(new Font("Arial", Font.BOLD, 11));
        btnChangePassword.setBorderPainted(false);
        btnChangePassword.setFocusPainted(false);
        btnChangePassword.addActionListener(this);
        passwordPanel.add(btnChangePassword);
        
        btnCancel = new JButton("CANCEL");
        btnCancel.setBounds(235, 305, 205, 35);
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 11));
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(this);
        passwordPanel.add(btnCancel);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditProfile) {
            enableEditing();
        } else if (e.getSource() == btnSaveProfile) {
            saveProfile();
        } else if (e.getSource() == btnChangePassword) {
            changePassword();
        } else if (e.getSource() == btnCancel) {
            cancelChanges();
        }
    }
    
    private void enableEditing() {
        txtFirstName.setEditable(true);
        txtLastName.setEditable(true);
        txtEmail.setEditable(true);
        txtPhone.setEditable(true);
        txtAddress.setEditable(true);
        txtFirstName.setBackground(Color.WHITE);
        txtLastName.setBackground(Color.WHITE);
        txtEmail.setBackground(Color.WHITE);
        txtPhone.setBackground(Color.WHITE);
        txtAddress.setBackground(Color.WHITE);
        isEditingProfile = true;
        btnEditProfile.setEnabled(false);
        btnSaveProfile.setEnabled(true);
        lblErrorMessage.setVisible(false);
        lblSuccessMessage.setVisible(false);
    }
    
    private void saveProfile() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            showError("All fields are required");
            return;
        }
        
        // Disable editing
        txtFirstName.setEditable(false);
        txtLastName.setEditable(false);
        txtEmail.setEditable(false);
        txtPhone.setEditable(false);
        txtAddress.setEditable(false);
        txtFirstName.setBackground(Color.decode("#F5F5F5"));
        txtLastName.setBackground(Color.decode("#F5F5F5"));
        txtEmail.setBackground(Color.decode("#F5F5F5"));
        txtPhone.setBackground(Color.decode("#F5F5F5"));
        txtAddress.setBackground(Color.decode("#F5F5F5"));
        isEditingProfile = false;
        btnEditProfile.setEnabled(true);
        btnSaveProfile.setEnabled(false);
        
        showSuccess("Profile updated successfully!");
        lblErrorMessage.setVisible(false);
    }
    
    private void changePassword() {
        String currentPassword = new String(txtCurrentPassword.getPassword());
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("All password fields are required");
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            showError("New passwords do not match");
            return;
        }
        
        if (newPassword.length() < 8) {
            showError("Password must be at least 8 characters long");
            return;
        }
        
        if (!newPassword.matches(".*[A-Z].*")) {
            showError("Password must contain at least one uppercase letter");
            return;
        }
        
        if (!newPassword.matches(".*[a-z].*")) {
            showError("Password must contain at least one lowercase letter");
            return;
        }
        
        if (!newPassword.matches(".*[0-9].*")) {
            showError("Password must contain at least one number");
            return;
        }
        
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
        showSuccess("Password changed successfully!");
        lblErrorMessage.setVisible(false);
    }
    
    private void cancelChanges() {
        if (isEditingProfile) {
            txtFirstName.setText("John");
            txtLastName.setText("Doe");
            txtEmail.setText("john.doe@example.com");
            txtPhone.setText("09123456789");
            txtAddress.setText("123 Main St, City, Country");
            txtFirstName.setEditable(false);
            txtLastName.setEditable(false);
            txtEmail.setEditable(false);
            txtPhone.setEditable(false);
            txtAddress.setEditable(false);
            txtFirstName.setBackground(Color.decode("#F5F5F5"));
            txtLastName.setBackground(Color.decode("#F5F5F5"));
            txtEmail.setBackground(Color.decode("#F5F5F5"));
            txtPhone.setBackground(Color.decode("#F5F5F5"));
            txtAddress.setBackground(Color.decode("#F5F5F5"));
            isEditingProfile = false;
            btnEditProfile.setEnabled(true);
            btnSaveProfile.setEnabled(false);
        }
        txtCurrentPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
        lblErrorMessage.setVisible(false);
        lblSuccessMessage.setVisible(false);
    }
    
    private void showError(String message) {
        lblErrorMessage.setText("⚠ " + message);
        lblErrorMessage.setVisible(true);
        lblSuccessMessage.setVisible(false);
    }
    
    private void showSuccess(String message) {
        lblSuccessMessage.setText("✓ " + message);
        lblSuccessMessage.setVisible(true);
        lblErrorMessage.setVisible(false);
    }
}
