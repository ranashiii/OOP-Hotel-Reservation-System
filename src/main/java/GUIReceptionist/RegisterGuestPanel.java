package GUIReceptionist;

import DAO.GuestDAO;
import DAO.UserDAO;
import Models.Guest;
import Models.User;
import Services.GuestService;
import Services.UserService;
import Utilities.Constants;
import Utilities.MessageBox;
import Utilities.ValidationUtil;
import Utilities.PasswordUtil;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * RegisterGuestPanel - Guest registration interface for receptionists
 * 
 * Allows receptionists to register new guests and create user accounts.
 * Validates all guest information before database insertion.
 */
public class RegisterGuestPanel extends JPanel {
    
    private UserService userService;
    private GuestService guestService;
    private GuestDAO guestDAO;
    private UserDAO userDAO;
    
    private JTextField txtUsername, txtEmail, txtFirstName, txtLastName, txtMiddleName;
    private JTextField txtPhone, txtAddress, txtDateOfBirth, txtNationality, txtIdType, txtIdNumber;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegister, btnReset;
    
    public RegisterGuestPanel() {
        this.userService = new UserService();
        this.guestService = new GuestService();
        this.guestDAO = new GuestDAO();
        this.userDAO = new UserDAO();
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel formPanel = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(13, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);
        
        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);
        
        panel.add(new JLabel("Confirm Password:"));
        txtConfirmPassword = new JPasswordField();
        panel.add(txtConfirmPassword);
        
        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);
        
        panel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField();
        panel.add(txtFirstName);
        
        panel.add(new JLabel("Middle Name:"));
        txtMiddleName = new JTextField();
        panel.add(txtMiddleName);
        
        panel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField();
        panel.add(txtLastName);
        
        panel.add(new JLabel("Phone Number:"));
        txtPhone = new JTextField();
        panel.add(txtPhone);
        
        panel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        panel.add(txtAddress);
        
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        txtDateOfBirth = new JTextField();
        panel.add(txtDateOfBirth);
        
        panel.add(new JLabel("Nationality:"));
        txtNationality = new JTextField();
        panel.add(txtNationality);
        
        panel.add(new JLabel("ID Document Type:"));
        txtIdType = new JComboBox<>(new String[]{"Passport", "Driver's License", "National ID", "Postal ID"}).getComponent(0) instanceof JComboBox ?
            (JTextField) new JComboBox<>(new String[]{"Passport", "Driver's License", "National ID", "Postal ID"}) :
            new JTextField();
        panel.add(txtIdType);
        
        panel.add(new JLabel("ID Document Number:"));
        txtIdNumber = new JTextField();
        panel.add(txtIdNumber);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnRegister = new JButton("Register Guest");
        btnRegister.setBackground(new Color(76, 175, 80));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(e -> registerGuest());
        panel.add(btnRegister);
        
        btnReset = new JButton("Clear Form");
        btnReset.setBackground(new Color(158, 158, 158));
        btnReset.setForeground(Color.WHITE);
        btnReset.addActionListener(e -> clearForm());
        panel.add(btnReset);
        
        return panel;
    }
    
    private void registerGuest() {
        try {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            String email = txtEmail.getText().trim();
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String middleName = txtMiddleName.getText().trim();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            String dateOfBirthStr = txtDateOfBirth.getText().trim();
            String nationality = txtNationality.getText().trim();
            String idType = txtIdType.getText().trim();
            String idNumber = txtIdNumber.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || 
                firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || 
                address.isEmpty() || dateOfBirthStr.isEmpty() || idNumber.isEmpty()) {
                MessageBox.showError("Validation Error", "All required fields must be filled");
                return;
            }
            
            if (!ValidationUtil.validateUsername(username)) {
                MessageBox.showError("Validation Error", "Username must be 5-20 alphanumeric characters");
                return;
            }
            
            if (!ValidationUtil.validatePassword(password)) {
                MessageBox.showError("Validation Error", "Password must contain uppercase, lowercase, and numbers");
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                MessageBox.showError("Validation Error", "Passwords do not match");
                return;
            }
            
            if (!ValidationUtil.validateEmail(email)) {
                MessageBox.showError("Validation Error", "Invalid email format");
                return;
            }
            
            if (!ValidationUtil.validateName(firstName)) {
                MessageBox.showError("Validation Error", "First name is invalid");
                return;
            }
            
            if (!ValidationUtil.validateName(lastName)) {
                MessageBox.showError("Validation Error", "Last name is invalid");
                return;
            }
            
            if (!ValidationUtil.validatePhoneNumber(phone)) {
                MessageBox.showError("Validation Error", "Phone number must be in Philippine format");
                return;
            }
            
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
            if (!ValidationUtil.validateDateOfBirth(dateOfBirth)) {
                MessageBox.showError("Validation Error", "Guest must be at least 18 years old");
                return;
            }
            
            User newUser = userService.registerUser(username, password, confirmPassword, email, Constants.ACCESS_GUEST);
            
            Guest newGuest = new Guest();
            newGuest.setUserId(newUser.getUserId());
            newGuest.setFirstName(firstName);
            newGuest.setMiddleName(middleName);
            newGuest.setLastName(lastName);
            newGuest.setEmail(email);
            newGuest.setPhoneNumber(phone);
            newGuest.setAddress(address);
            newGuest.setDateOfBirth(dateOfBirth);
            newGuest.setNationality(nationality);
            newGuest.setIdDocumentType(idType);
            newGuest.setIdDocumentNumber(idNumber);
            
            guestService.registerGuest(newGuest);
            
            MessageBox.showInfo("Success", "Guest registered successfully!\nUsername: " + username);
            clearForm();
            
        } catch (Exception e) {
            MessageBox.showError("Registration Error", "Failed to register guest: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtEmail.setText("");
        txtFirstName.setText("");
        txtMiddleName.setText("");
        txtLastName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtDateOfBirth.setText("");
        txtNationality.setText("");
        txtIdType.setText("");
        txtIdNumber.setText("");
    }
}
