package com.mycompany.HotelReservationApp.mainsystem.login;

import com.mycompany.HotelReservationApp.mainsystem.HomePage;
import com.mycompany.HotelReservationApp.mainsystem.admin.ui.AdminDashboard;
import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.session.SessionManager;
import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.session.AuthManager;
import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.ui.StyledButton;
import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.util.MessageBox;
import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.util.Logger;
import com.mycompany.HotelReservationApp.mainsystem.guest.ui.GuestDashboard;
import com.mycompany.HotelReservationApp.mainsystem.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * LoginFrame - Hotel Reservation System Login Interface
 * Provides secure login and navigation to registration
 * Features: Input validation, password field masking, session management, account locking
 */
public class LoginFrame extends JFrame implements ActionListener, KeyListener {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit, btnCreateAccount;
    private JLabel lblTitle, lblUsername, lblPassword, lblErrorMessage;
    private JCheckBox chkRememberMe;
    private JLabel lblValidationUsername, lblValidationPassword;
    
    // Login attempt tracking
    private int failedAttempts = 0;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    
    public LoginFrame() {
        initWindow();
        createComponents();
        Logger.getInstance().info("LoginFrame initialized");
    }
    
    private void initWindow() {
        setTitle("Hotel Reservation System - Login");
        setSize(500, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
    }
    
    private void createComponents() {
        // Title
        lblTitle = new JLabel("HOTEL RESERVATION SYSTEM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(70, 70, 70));
        lblTitle.setBounds(30, 20, 440, 35);
        add(lblTitle);
        
        // Subtitle
        JLabel lblSubtitle = new JLabel("Login to your account");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(120, 120, 120));
        lblSubtitle.setBounds(30, 55, 440, 15);
        add(lblSubtitle);
        
        // Error Message Panel (initially hidden)
        lblErrorMessage = new JLabel();
        lblErrorMessage.setFont(new Font("Arial", Font.PLAIN, 11));
        lblErrorMessage.setForeground(new Color(244, 67, 54));
        lblErrorMessage.setBounds(30, 75, 440, 25);
        lblErrorMessage.setVisible(false);
        add(lblErrorMessage);
        
        // Username Label & Validation
        lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 12));
        lblUsername.setBounds(30, 105, 150, 20);
        add(lblUsername);
        
        lblValidationUsername = new JLabel();
        lblValidationUsername.setFont(new Font("Arial", Font.PLAIN, 10));
        lblValidationUsername.setBounds(400, 105, 70, 20);
        add(lblValidationUsername);
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        txtUsername.setBounds(30, 128, 440, 35);
        txtUsername.addKeyListener(this);
        add(txtUsername);
        
        // Password Label & Validation
        lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblPassword.setBounds(30, 170, 150, 20);
        add(lblPassword);
        
        lblValidationPassword = new JLabel();
        lblValidationPassword.setFont(new Font("Arial", Font.PLAIN, 10));
        lblValidationPassword.setBounds(400, 170, 70, 20);
        add(lblValidationPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setBounds(30, 193, 440, 35);
        txtPassword.addKeyListener(this);
        add(txtPassword);
        
        // Remember Me Checkbox
        chkRememberMe = new JCheckBox("Remember me");
        chkRememberMe.setFont(new Font("Arial", Font.PLAIN, 11));
        chkRememberMe.setBounds(30, 235, 150, 20);
        chkRememberMe.setBackground(Color.WHITE);
        add(chkRememberMe);
        
        // Login Button
        btnLogin = new StyledButton("LOGIN", "success");
        btnLogin.setBounds(30, 270, 440, 45);
        btnLogin.addActionListener(this);
        add(btnLogin);
        
        // Create Account Button
        btnCreateAccount = new StyledButton("CREATE ACCOUNT", "info");
        btnCreateAccount.setBounds(30, 325, 440, 35);
        btnCreateAccount.setBackground(new Color(33, 150, 243));
        btnCreateAccount.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCreateAccount.addActionListener(this);
        add(btnCreateAccount);
        
        // Exit Button
        btnExit = new JButton("EXIT");
        btnExit.setFont(new Font("Arial", Font.PLAIN, 11));
        btnExit.setBounds(30, 370, 440, 35);
        btnExit.setBackground(new Color(244, 67, 54));
        btnExit.setForeground(Color.WHITE);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);
        btnExit.addActionListener(this);
        add(btnExit);
        
        // Help Text
        JLabel lblHelp = new JLabel("Test: admin / Admin@123");
        lblHelp.setFont(new Font("Arial", Font.ITALIC, 9));
        lblHelp.setForeground(new Color(150, 150, 150));
        lblHelp.setBounds(30, 515, 440, 15);
        add(lblHelp);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            handleLogin();
        } else if (e.getSource() == btnCreateAccount) {
            handleCreateAccount();
        } else if (e.getSource() == btnExit) {
            handleExit();
        }
    }
    
    private void handleLogin() {
        // Clear previous error
        lblErrorMessage.setVisible(false);
        
        // Check if account is locked
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            showError("Account locked after " + MAX_FAILED_ATTEMPTS + " failed attempts. Please try again later.");
            return;
        }
        
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }
        
        AuthManager auth = AuthManager.getInstance();
        if (auth.authenticate(username, password)) {
            failedAttempts = 0;
            User user = auth.getAuthenticatedUserObject();
            String userID = auth.getAuthenticatedUserID();
            String userRole = user.getRole();
            
            // Store remember me preference if checked
            if (chkRememberMe.isSelected()) {
                // TODO: Store username in preferences (encrypted)
            }
            
            SessionManager.login(userID, username, userRole);
            Logger.getInstance().info("User login successful: " + username + " [" + userRole + "]");
            openModule(userRole);
            dispose();
        } else {
            failedAttempts++;
            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                showError("Login failed. Account locked after " + MAX_FAILED_ATTEMPTS + " attempts.");
            } else {
                showError("Invalid username or password. Attempts: " + failedAttempts + "/" + MAX_FAILED_ATTEMPTS);
            }
            Logger.getInstance().warn("Failed login attempt #" + failedAttempts + " for user: " + username);
            txtPassword.setText("");
        }
    }
    
    private void openModule(String role) {
        try {
            switch (role.toUpperCase()) {
                case "GUEST":
                    new GuestDashboard().setVisible(true);
                    break;
                case "RECEPTIONIST":
                    new HomePage().setVisible(true);
                    break;
                case "ADMIN":
                    new AdminDashboard().setVisible(true);
                    break;
                default:
                    showError("Unknown role: " + role);
            }
        } catch (Exception ex) {
            Logger.getInstance().error("Error opening module", ex);
            showError("Failed to open module: " + ex.getMessage());
        }
    }
    
    private void handleCreateAccount() {
        Logger.getInstance().info("Opening registration frame");
        dispose();
        new RegistrationFrame(this).setVisible(true);
    }
    
    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?", "Exit",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Logger.getInstance().info("Application closed by user");
            System.exit(0);
        }
    }
    
    private void showError(String message) {
        lblErrorMessage.setText("⚠ " + message);
        lblErrorMessage.setVisible(true);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            handleLogin();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Validate username in real-time
        if (e.getSource() == txtUsername) {
            String username = txtUsername.getText().trim();
            if (username.isEmpty()) {
                lblValidationUsername.setText("");
            } else if (username.length() < 5) {
                lblValidationUsername.setText("Too short");
                lblValidationUsername.setForeground(new Color(244, 67, 54));
            } else {
                lblValidationUsername.setText("✓");
                lblValidationUsername.setForeground(new Color(76, 175, 80));
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
}