package com.hotelreservationsystem.gui.auth;

import com.hotelreservationsystem.SessionManager;
import com.hotelreservationsystem.config.AppConfig;
import com.hotelreservationsystem.model.User;
import com.hotelreservationsystem.service.UserService;
import com.hotelreservationsystem.util.HotelException;
import com.hotelreservationsystem.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LoginFrame - Hotel Reservation System Login Interface
 * 
 * Provides secure login functionality for users with different access levels:
 * - Admin
 * - Receptionist  
 * - Guest
 * 
 * Features:
 * - Input validation
 * - Password field masking
 * - Session management
 * - Account locking after 3 failed attempts
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class LoginFrame extends JFrame implements ActionListener {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit, btnCreateAccount;
    private JLabel lblTitle, lblUsername, lblPassword, lblErrorMessage;
    private JCheckBox chkRememberMe;
    
    // Login attempt tracking
    private int failedAttempts = 0;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    
    private UserService userService;
    
    /**
     * Constructor - Initialize login frame
     */
    public LoginFrame() {
        this.userService = new UserService();
        initWindow();
        createComponents();
    }
    
    /**
     * Initialize window properties
     */
    private void initWindow() {
        setTitle("Hotel Reservation System - Login");
        setSize(AppConfig.LOGIN_WINDOW_WIDTH, AppConfig.LOGIN_WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
    }
    
    /**
     * Create and layout all UI components
     */
    private void createComponents() {
        // Title
        lblTitle = new JLabel("HOTEL RESERVATION SYSTEM");
        lblTitle.setFont(AppConfig.FONT_TITLE);
        lblTitle.setForeground(AppConfig.COLOR_PRIMARY_DARK);
        lblTitle.setBounds(30, 30, 540, 40);
        add(lblTitle);
        
        // Subtitle
        JLabel lblSubtitle = new JLabel("Login to your account");
        lblSubtitle.setFont(AppConfig.FONT_REGULAR);
        lblSubtitle.setForeground(new Color(120, 120, 120));
        lblSubtitle.setBounds(30, 75, 540, 15);
        add(lblSubtitle);
        
        // Error Message Label
        lblErrorMessage = new JLabel();
        lblErrorMessage.setFont(AppConfig.FONT_SMALL);
        lblErrorMessage.setForeground(AppConfig.COLOR_ERROR);
        lblErrorMessage.setBounds(30, 95, 540, 25);
        lblErrorMessage.setVisible(false);
        add(lblErrorMessage);
        
        // Username Label
        lblUsername = new JLabel("Username:");
        lblUsername.setFont(AppConfig.FONT_LABEL);
        lblUsername.setBounds(30, 130, 150, 20);
        add(lblUsername);
        
        // Username TextField
        txtUsername = new JTextField();
        txtUsername.setFont(AppConfig.FONT_REGULAR);
        txtUsername.setBounds(30, 155, 540, 35);
        txtUsername.setBorder(BorderFactory.createLineBorder(AppConfig.COLOR_BORDER));
        add(txtUsername);
        
        // Password Label
        lblPassword = new JLabel("Password:");
        lblPassword.setFont(AppConfig.FONT_LABEL);
        lblPassword.setBounds(30, 200, 150, 20);
        add(lblPassword);
        
        // Password Field
        txtPassword = new JPasswordField();
        txtPassword.setFont(AppConfig.FONT_REGULAR);
        txtPassword.setBounds(30, 225, 540, 35);
        txtPassword.setBorder(BorderFactory.createLineBorder(AppConfig.COLOR_BORDER));
        add(txtPassword);
        
        // Remember Me Checkbox
        chkRememberMe = new JCheckBox("Remember me");
        chkRememberMe.setFont(AppConfig.FONT_REGULAR);
        chkRememberMe.setBounds(30, 270, 200, 25);
        chkRememberMe.setBackground(Color.WHITE);
        add(chkRememberMe);
        
        // Login Button
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(AppConfig.FONT_BUTTON);
        btnLogin.setBackground(AppConfig.COLOR_SUCCESS);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(30, 315, 150, 40);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(this);
        add(btnLogin);
        
        // Create Account Button
        btnCreateAccount = new JButton("CREATE ACCOUNT");
        btnCreateAccount.setFont(AppConfig.FONT_BUTTON);
        btnCreateAccount.setBackground(AppConfig.COLOR_SECONDARY);
        btnCreateAccount.setForeground(Color.WHITE);
        btnCreateAccount.setBounds(200, 315, 180, 40);
        btnCreateAccount.setBorderPainted(false);
        btnCreateAccount.setFocusPainted(false);
        btnCreateAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreateAccount.addActionListener(this);
        add(btnCreateAccount);
        
        // Exit Button
        btnExit = new JButton("EXIT");
        btnExit.setFont(AppConfig.FONT_BUTTON);
        btnExit.setBackground(AppConfig.COLOR_ERROR);
        btnExit.setForeground(Color.WHITE);
        btnExit.setBounds(410, 315, 160, 40);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(this);
        add(btnExit);
        
        // Test Accounts Info
        JLabel lblTestAccounts = new JLabel("Test Accounts: admin / receptionist / guest (password: Admin@123)");
        lblTestAccounts.setFont(AppConfig.FONT_SMALL);
        lblTestAccounts.setForeground(new Color(150, 150, 150));
        lblTestAccounts.setBounds(30, 420, 540, 15);
        add(lblTestAccounts);
    }
    
    /**
     * Handle button click events
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            handleLogin();
        } else if (e.getSource() == btnCreateAccount) {
            openRegistration();
        } else if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }
    
    /**
     * Handle login process
     */
    private void handleLogin() {
        // Clear previous error messages
        lblErrorMessage.setVisible(false);
        
        // Get input values
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required");
            return;
        }
        
        try {
            // Authenticate user
            User user = userService.authenticateUser(username, password);
            
            if (user != null && user.isActive()) {
                // Login successful - store session
                SessionManager.getInstance().login(user);
                
                // Close login frame
                this.dispose();
                
                // Open appropriate dashboard based on access level
                openDashboard(user.getAccessLevel());
                
                // Reset failed attempts
                failedAttempts = 0;
            } else {
                handleFailedLogin();
            }
        } catch (HotelException ex) {
            handleFailedLogin();
        } catch (Exception ex) {
            showError("An unexpected error occurred: " + ex.getMessage());
        }
    }
    
    /**
     * Handle failed login attempt
     */
    private void handleFailedLogin() {
        failedAttempts++;
        
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            showError("Account locked due to multiple failed login attempts");
            btnLogin.setEnabled(false);
            txtUsername.setEditable(false);
            txtPassword.setEditable(false);
        } else {
            int attemptsLeft = MAX_FAILED_ATTEMPTS - failedAttempts;
            showError("Invalid username or password. Attempts remaining: " + attemptsLeft);
        }
        
        // Clear password field
        txtPassword.setText("");
    }
    
    /**
     * Open appropriate dashboard based on user access level
     */
    private void openDashboard(String accessLevel) {
        if ("Admin".equalsIgnoreCase(accessLevel)) {
            new com.hotelreservationsystem.gui.admin.AdminDashboard().setVisible(true);
        } else if ("Receptionist".equalsIgnoreCase(accessLevel)) {
            new com.hotelreservationsystem.gui.receptionist.ReceptionistDashboard().setVisible(true);
        } else if ("Guest".equalsIgnoreCase(accessLevel)) {
            new com.hotelreservationsystem.gui.guest.GuestDashboard().setVisible(true);
        }
    }
    
    /**
     * Open registration frame for new account creation
     */
    private void openRegistration() {
        new RegistrationFrame(this).setVisible(true);
    }
    
    /**
     * Display error message
     */
    private void showError(String message) {
        lblErrorMessage.setText(message);
        lblErrorMessage.setVisible(true);
    }
}
