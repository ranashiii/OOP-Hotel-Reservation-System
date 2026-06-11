package com.mycompany.HotelReservationApp.mainsystem.login;

import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.ui.StyledButton;
import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.util.MessageBox;
import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.util.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * RegistrationFrame - Complete Guest Registration
 * Comprehensive validation for all guest fields
 * Features: Password strength, age verification, format validation, error feedback
 */
public class RegistrationFrame extends JFrame implements ActionListener, KeyListener {
    
    private JTextField txtFirstName;
    private JTextField txtMiddleName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhoneNumber;
    private JTextField txtDateOfBirth;
    private JTextField txtAddress;
    private JComboBox<String> cmbIdType;
    private JTextField txtIdNumber;
    private JComboBox<String> cmbNationality;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<String> cmbRole;
    private JButton btnRegister, btnCancel, btnClear;
    private JLabel lblTitle;
    private JLabel lblErrorMessage;
    private JLabel lblCharCount;
    private LoginFrame parentLoginFrame;
    
    // Validation patterns
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^(09|\\+639)\\d{9}$");
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{5,20}$");
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-Z\\s'-]{2,50}$");
    
    public RegistrationFrame(LoginFrame parentFrame) {
        this.parentLoginFrame = parentFrame;
        initWindow();
        createComponents();
        Logger.getInstance().info("RegistrationFrame initialized with full validation");
    }
    
    private void initWindow() {
        setTitle("Hotel Reservation System - Create Account");
        setSize(620, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
    }
    
    private void createComponents() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 620, 900);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(600, 1100));
        
        // Title
        lblTitle = new JLabel("CREATE GUEST ACCOUNT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(70, 70, 70));
        lblTitle.setBounds(40, 15, 520, 35);
        mainPanel.add(lblTitle);
        
        JLabel lblSubtitle = new JLabel("Please fill in all required fields");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(120, 120, 120));
        lblSubtitle.setBounds(40, 50, 520, 15);
        mainPanel.add(lblSubtitle);
        
        // Error Message Panel
        lblErrorMessage = new JLabel();
        lblErrorMessage.setFont(new Font("Arial", Font.PLAIN, 11));
        lblErrorMessage.setForeground(new Color(244, 67, 54));
        lblErrorMessage.setBounds(40, 70, 520, 20);
        lblErrorMessage.setVisible(false);
        mainPanel.add(lblErrorMessage);
        
        int yPos = 95;
        int fieldHeight = 30;
        int labelHeight = 18;
        int spacing = 8;
        
        // ACCOUNT INFORMATION SECTION
        JLabel secAccount = new JLabel("ACCOUNT INFORMATION");
        secAccount.setFont(new Font("Arial", Font.BOLD, 12));
        secAccount.setForeground(new Color(33, 150, 243));
        secAccount.setBounds(40, yPos, 520, 15);
        mainPanel.add(secAccount);
        yPos += 25;
        
        // Username
        JLabel lblUsername = new JLabel("Username (5-20 alphanumeric): *");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 11));
        lblUsername.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblUsername);
        yPos += labelHeight + 3;
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        txtUsername.setBounds(40, yPos, 520, fieldHeight);
        txtUsername.addKeyListener(this);
        mainPanel.add(txtUsername);
        yPos += fieldHeight + spacing;
        
        // Password
        JLabel lblPassword = new JLabel("Password (Min 8 chars, 1 uppercase, 1 lowercase, 1 digit): *");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 11));
        lblPassword.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblPassword);
        yPos += labelHeight + 3;
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setBounds(40, yPos, 520, fieldHeight);
        txtPassword.addKeyListener(this);
        mainPanel.add(txtPassword);
        yPos += fieldHeight + spacing;
        
        // Confirm Password
        JLabel lblConfirmPassword = new JLabel("Confirm Password: *");
        lblConfirmPassword.setFont(new Font("Arial", Font.BOLD, 11));
        lblConfirmPassword.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblConfirmPassword);
        yPos += labelHeight + 3;
        
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtConfirmPassword.setBounds(40, yPos, 520, fieldHeight);
        mainPanel.add(txtConfirmPassword);
        yPos += fieldHeight + spacing + 10;
        
        // PERSONAL INFORMATION SECTION
        JLabel secPersonal = new JLabel("PERSONAL INFORMATION");
        secPersonal.setFont(new Font("Arial", Font.BOLD, 12));
        secPersonal.setForeground(new Color(33, 150, 243));
        secPersonal.setBounds(40, yPos, 520, 15);
        mainPanel.add(secPersonal);
        yPos += 25;
        
        // First Name
        JLabel lblFirstName = new JLabel("First Name: *");
        lblFirstName.setFont(new Font("Arial", Font.BOLD, 11));
        lblFirstName.setBounds(40, yPos, 240, labelHeight);
        mainPanel.add(lblFirstName);
        yPos += labelHeight + 3;
        
        txtFirstName = new JTextField();
        txtFirstName.setFont(new Font("Arial", Font.PLAIN, 12));
        txtFirstName.setBounds(40, yPos, 240, fieldHeight);
        mainPanel.add(txtFirstName);
        
        yPos -= labelHeight + 3;
        JLabel lblMiddleName = new JLabel("Middle Name:");
        lblMiddleName.setFont(new Font("Arial", Font.BOLD, 11));
        lblMiddleName.setBounds(300, yPos, 260, labelHeight);
        mainPanel.add(lblMiddleName);
        yPos += labelHeight + 3;
        
        txtMiddleName = new JTextField();
        txtMiddleName.setFont(new Font("Arial", Font.PLAIN, 12));
        txtMiddleName.setBounds(300, yPos, 260, fieldHeight);
        mainPanel.add(txtMiddleName);
        yPos += fieldHeight + spacing;
        
        // Last Name
        JLabel lblLastName = new JLabel("Last Name: *");
        lblLastName.setFont(new Font("Arial", Font.BOLD, 11));
        lblLastName.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblLastName);
        yPos += labelHeight + 3;
        
        txtLastName = new JTextField();
        txtLastName.setFont(new Font("Arial", Font.PLAIN, 12));
        txtLastName.setBounds(40, yPos, 520, fieldHeight);
        mainPanel.add(txtLastName);
        yPos += fieldHeight + spacing;
        
        // Date of Birth
        JLabel lblDateOfBirth = new JLabel("Date of Birth (YYYY-MM-DD, must be 18+): *");
        lblDateOfBirth.setFont(new Font("Arial", Font.BOLD, 11));
        lblDateOfBirth.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblDateOfBirth);
        yPos += labelHeight + 3;
        
        txtDateOfBirth = new JTextField();
        txtDateOfBirth.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDateOfBirth.setBounds(40, yPos, 200, fieldHeight);
        mainPanel.add(txtDateOfBirth);
        yPos += fieldHeight + spacing;
        
        // Email
        JLabel lblEmail = new JLabel("Email: *");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 11));
        lblEmail.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblEmail);
        yPos += labelHeight + 3;
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setBounds(40, yPos, 520, fieldHeight);
        mainPanel.add(txtEmail);
        yPos += fieldHeight + spacing;
        
        // Phone Number
        JLabel lblPhoneNumber = new JLabel("Phone Number (09XXXXXXXXX or +639XXXXXXXXX): *");
        lblPhoneNumber.setFont(new Font("Arial", Font.BOLD, 11));
        lblPhoneNumber.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblPhoneNumber);
        yPos += labelHeight + 3;
        
        txtPhoneNumber = new JTextField();
        txtPhoneNumber.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPhoneNumber.setBounds(40, yPos, 520, fieldHeight);
        mainPanel.add(txtPhoneNumber);
        yPos += fieldHeight + spacing;
        
        // Address
        JLabel lblAddress = new JLabel("Address (Max 255 characters): *");
        lblAddress.setFont(new Font("Arial", Font.BOLD, 11));
        lblAddress.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblAddress);
        yPos += labelHeight + 3;
        
        txtAddress = new JTextField();
        txtAddress.setFont(new Font("Arial", Font.PLAIN, 12));
        txtAddress.setBounds(40, yPos, 500, fieldHeight);
        mainPanel.add(txtAddress);
        
        lblCharCount = new JLabel("0/255");
        lblCharCount.setFont(new Font("Arial", Font.PLAIN, 9));
        lblCharCount.setForeground(new Color(150, 150, 150));
        lblCharCount.setBounds(545, yPos, 35, fieldHeight);
        mainPanel.add(lblCharCount);
        yPos += fieldHeight + spacing;
        
        // Nationality
        JLabel lblNationality = new JLabel("Nationality: *");
        lblNationality.setFont(new Font("Arial", Font.BOLD, 11));
        lblNationality.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblNationality);
        yPos += labelHeight + 3;
        
        cmbNationality = new JComboBox<>(new String[]{"- Select -", "Filipino", "American", "British", "Chinese", "Japanese", "Korean", "Other"});
        cmbNationality.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbNationality.setBounds(40, yPos, 200, fieldHeight);
        mainPanel.add(cmbNationality);
        yPos += fieldHeight + spacing + 10;
        
        // ID DOCUMENT SECTION
        JLabel secId = new JLabel("ID DOCUMENT INFORMATION");
        secId.setFont(new Font("Arial", Font.BOLD, 12));
        secId.setForeground(new Color(33, 150, 243));
        secId.setBounds(40, yPos, 520, 15);
        mainPanel.add(secId);
        yPos += 25;
        
        // ID Type
        JLabel lblIdType = new JLabel("ID Type: *");
        lblIdType.setFont(new Font("Arial", Font.BOLD, 11));
        lblIdType.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblIdType);
        yPos += labelHeight + 3;
        
        cmbIdType = new JComboBox<>(new String[]{"- Select -", "Passport", "Driver's License", "National ID", "Postal ID", "SSS", "Other"});
        cmbIdType.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbIdType.setBounds(40, yPos, 200, fieldHeight);
        mainPanel.add(cmbIdType);
        yPos += fieldHeight + spacing;
        
        // ID Number
        JLabel lblIdNumber = new JLabel("ID Number (Min 5 characters): *");
        lblIdNumber.setFont(new Font("Arial", Font.BOLD, 11));
        lblIdNumber.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblIdNumber);
        yPos += labelHeight + 3;
        
        txtIdNumber = new JTextField();
        txtIdNumber.setFont(new Font("Arial", Font.PLAIN, 12));
        txtIdNumber.setBounds(40, yPos, 520, fieldHeight);
        mainPanel.add(txtIdNumber);
        yPos += fieldHeight + spacing + 10;
        
        // Role Selection (Guests can only select "Guest")
        JLabel lblRole = new JLabel("Account Type: *");
        lblRole.setFont(new Font("Arial", Font.BOLD, 11));
        lblRole.setBounds(40, yPos, 520, labelHeight);
        mainPanel.add(lblRole);
        yPos += labelHeight + 3;
        
        cmbRole = new JComboBox<>(new String[]{"Guest"});
        cmbRole.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbRole.setBounds(40, yPos, 200, fieldHeight);
        cmbRole.setEnabled(false);
        mainPanel.add(cmbRole);
        yPos += fieldHeight + 20;
        
        // Buttons
        btnRegister = new StyledButton("REGISTER", "success");
        btnRegister.setBounds(40, yPos, 160, 40);
        btnRegister.addActionListener(this);
        mainPanel.add(btnRegister);
        
        btnClear = new JButton("CLEAR");
        btnClear.setFont(new Font("Arial", Font.PLAIN, 12));
        btnClear.setBounds(220, yPos, 160, 40);
        btnClear.setBackground(new Color(255, 193, 7));
        btnClear.setForeground(Color.BLACK);
        btnClear.setBorderPainted(false);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(this);
        mainPanel.add(btnClear);
        
        btnCancel = new JButton("CANCEL");
        btnCancel.setFont(new Font("Arial", Font.PLAIN, 12));
        btnCancel.setBounds(400, yPos, 160, 40);
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(this);
        mainPanel.add(btnCancel);
        
        scrollPane.setViewportView(mainPanel);
        add(scrollPane);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            handleRegister();
        } else if (e.getSource() == btnCancel) {
            handleCancel();
        } else if (e.getSource() == btnClear) {
            clearForm();
        }
    }
    
    private void handleRegister() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phoneNumber = txtPhoneNumber.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        // Validate all fields
        if (!validateInput(firstName, null, lastName, email, phoneNumber, 
                          null, null, null, null, null, username, password, confirmPassword)) {
            return;
        }

        // Register user with correct method signature
        try {
            RegistrationManager registrationManager = RegistrationManager.getInstance();
            boolean registerSuccess = registrationManager.registerUser(
                firstName, lastName, email, phoneNumber, username, password, "Guest"
            );

            if (registerSuccess) {
                MessageBox.showInfo("Registration Successful", 
                    "Account created successfully!\n\nUsername: " + username + 
                    "\n\nYou can now login with your credentials.");
                Logger.getInstance().info("New guest registered: " + username);
                clearForm();
                dispose();
                parentLoginFrame.setVisible(true);
            } else {
                showError("Username already exists. Please choose a different username.");
                Logger.getInstance().warn("Registration failed - Username already exists: " + username);
            }
        } catch (Exception ex) {
            showError("Registration error: " + ex.getMessage());
            Logger.getInstance().error("Registration exception", ex);
        }
    }
    
    private boolean validateInput(String firstName, String middleName, String lastName, String email,
                                   String phoneNumber, String dob, String address, String nationality,
                                   String idType, String idNumber, String username, String password,
                                   String confirmPassword) {
        // Required fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
            phoneNumber.isEmpty() || dob.isEmpty() || address.isEmpty() || 
            username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields marked with * are required");
            return false;
        }
        
        // Nationality & ID Type validation
        if (nationality.equals("- Select -")) {
            showError("Please select a nationality");
            return false;
        }
        if (idType.equals("- Select -")) {
            showError("Please select an ID type");
            return false;
        }
        
        // First and Last Name validation (letters only)
        if (!NAME_PATTERN.matcher(firstName).matches()) {
            showError("First name must contain letters only (2-50 characters)");
            return false;
        }
        if (!NAME_PATTERN.matcher(lastName).matches()) {
            showError("Last name must contain letters only (2-50 characters)");
            return false;
        }
        if (!middleName.isEmpty() && !NAME_PATTERN.matcher(middleName).matches()) {
            showError("Middle name must contain letters only (2-50 characters)");
            return false;
        }
        
        // Email validation
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Please enter a valid email address");
            return false;
        }
        
        // Phone validation (Philippine format)
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            showError("Phone must be 09XXXXXXXXX (11 digits) or +639XXXXXXXXX (13 chars)");
            return false;
        }
        
        // Date of Birth validation
        if (!isValidAge(dob)) {
            return false;
        }
        
        // Address validation
        if (address.length() < 5 || address.length() > 255) {
            showError("Address must be 5-255 characters");
            return false;
        }
        
        // ID Number validation
        if (idNumber.length() < 5) {
            showError("ID number must be at least 5 characters");
            return false;
        }
        
        // Username validation
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            showError("Username must be 5-20 alphanumeric characters (can include underscore)");
            return false;
        }
        
        // Password validation
        if (!isValidPassword(password)) {
            return false;
        }
        
        // Confirm password validation
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return false;
        }
        
        return true;
    }
    
    private boolean isValidAge(String dobString) {
        try {
            LocalDate dob = LocalDate.parse(dobString, DateTimeFormatter.ISO_DATE);
            LocalDate today = LocalDate.now();
            int age = today.getYear() - dob.getYear();
            
            if (dob.plusYears(age).isAfter(today)) {
                age--;
            }
            
            if (age < 18) {
                showError("You must be at least 18 years old to register");
                return false;
            }
            if (dob.isAfter(today)) {
                showError("Date of birth cannot be in the future");
                return false;
            }
            return true;
        } catch (Exception ex) {
            showError("Invalid date format. Use YYYY-MM-DD");
            return false;
        }
    }
    
    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            showError("Password must be at least 8 characters long");
            return false;
        }
        
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasLower = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        
        if (!hasUpper || !hasLower || !hasDigit) {
            showError("Password must contain: uppercase letter, lowercase letter, and digit");
            return false;
        }
        
        return true;
    }
    
    private void clearForm() {
        txtFirstName.setText("");
        txtMiddleName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhoneNumber.setText("");
        txtDateOfBirth.setText("");
        txtAddress.setText("");
        cmbNationality.setSelectedIndex(0);
        cmbIdType.setSelectedIndex(0);
        txtIdNumber.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        lblCharCount.setText("0/255");
        lblErrorMessage.setVisible(false);
    }
    
    private void showError(String message) {
        lblErrorMessage.setText("⚠ " + message);
        lblErrorMessage.setVisible(true);
    }
    
    private void handleCancel() {
        dispose();
        parentLoginFrame.setVisible(true);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() != txtAddress) {
            handleRegister();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Update character counter for address
        if (e.getSource() == txtAddress) {
            int length = txtAddress.getText().length();
            lblCharCount.setText(length + "/255");
            if (length > 255) {
                txtAddress.setText(txtAddress.getText().substring(0, 255));
                lblCharCount.setText("255/255");
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
}