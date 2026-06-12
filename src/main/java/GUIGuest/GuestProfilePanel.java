package GUIGuest;

import DAO.GuestDAO;
import Models.Guest;
import Services.GuestService;
import HotelReservationMainSystem.SessionManager;
import Utilities.Constants;
import Utilities.MessageBox;
import Utilities.ValidationUtil;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * GuestProfilePanel - Guest profile management interface
 * 
 * Allows guests to view and edit their personal information,
 * and change their password.
 */
public class GuestProfilePanel extends JPanel {
    
    private GuestService guestService;
    private GuestDAO guestDAO;
    private Guest currentGuest;
    
    private JTextField txtFirstName, txtLastName, txtMiddleName, txtEmail, txtPhone;
    private JTextField txtAddress, txtDateOfBirth, txtNationality, txtIdType, txtIdNumber;
    private JPasswordField txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    private JButton btnEditProfile, btnSaveProfile, btnChangePassword, btnCancel;
    private boolean isEditingProfile = false;
    
    public GuestProfilePanel() {
        this.guestService = new GuestService();
        this.guestDAO = new GuestDAO();
        initUI();
        loadGuestProfile();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Tabbed pane for profile and password
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Personal Information", createProfilePanel());
        tabbedPane.addTab("Change Password", createPasswordPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // First name
        panel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField();
        txtFirstName.setEditable(false);
        panel.add(txtFirstName);
        
        // Middle name
        panel.add(new JLabel("Middle Name:"));
        txtMiddleName = new JTextField();
        txtMiddleName.setEditable(false);
        panel.add(txtMiddleName);
        
        // Last name
        panel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField();
        txtLastName.setEditable(false);
        panel.add(txtLastName);
        
        // Email
        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        txtEmail.setEditable(false);
        panel.add(txtEmail);
        
        // Phone
        panel.add(new JLabel("Phone Number:"));
        txtPhone = new JTextField();
        txtPhone.setEditable(false);
        panel.add(txtPhone);
        
        // Address
        panel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        txtAddress.setEditable(false);
        panel.add(txtAddress);
        
        // Date of birth
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        txtDateOfBirth = new JTextField();
        txtDateOfBirth.setEditable(false);
        panel.add(txtDateOfBirth);
        
        // Nationality
        panel.add(new JLabel("Nationality:"));
        txtNationality = new JTextField();
        txtNationality.setEditable(false);
        panel.add(txtNationality);
        
        // ID type
        panel.add(new JLabel("ID Document Type:"));
        txtIdType = new JTextField();
        txtIdType.setEditable(false);
        panel.add(txtIdType);
        
        // ID number
        panel.add(new JLabel("ID Document Number:"));
        txtIdNumber = new JTextField();
        txtIdNumber.setEditable(false);
        panel.add(txtIdNumber);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        btnEditProfile = new JButton("Edit Profile");
        btnEditProfile.addActionListener(e -> enableEditMode());
        buttonPanel.add(btnEditProfile);
        
        btnSaveProfile = new JButton("Save Changes");
        btnSaveProfile.setBackground(new Color(76, 175, 80));
        btnSaveProfile.setForeground(Color.WHITE);
        btnSaveProfile.setEnabled(false);
        btnSaveProfile.addActionListener(e -> saveProfile());
        buttonPanel.add(btnSaveProfile);
        
        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setEnabled(false);
        btnCancel.addActionListener(e -> cancelEdit());
        buttonPanel.add(btnCancel);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Current password
        panel.add(new JLabel("Current Password:"));
        txtCurrentPassword = new JPasswordField();
        panel.add(txtCurrentPassword);
        
        // New password
        panel.add(new JLabel("New Password:"));
        txtNewPassword = new JPasswordField();
        panel.add(txtNewPassword);
        
        // Confirm password
        panel.add(new JLabel("Confirm New Password:"));
        txtConfirmPassword = new JPasswordField();
        panel.add(txtConfirmPassword);
        
        // Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        btnChangePassword = new JButton("Change Password");
        btnChangePassword.setBackground(new Color(25, 118, 210));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.addActionListener(e -> changePassword());
        buttonPanel.add(btnChangePassword);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private void loadGuestProfile() {
        try {
            int userId = SessionManager.getInstance().getCurrentUserId();
            currentGuest = guestDAO.getGuestByUserId(userId);
            
            if (currentGuest == null) {
                MessageBox.showError("Error", "Guest profile not found");
                return;
            }
            
            txtFirstName.setText(currentGuest.getFirstName() != null ? currentGuest.getFirstName() : "");
            txtMiddleName.setText(currentGuest.getMiddleName() != null ? currentGuest.getMiddleName() : "");
            txtLastName.setText(currentGuest.getLastName() != null ? currentGuest.getLastName() : "");
            txtEmail.setText(currentGuest.getEmail() != null ? currentGuest.getEmail() : "");
            txtPhone.setText(currentGuest.getPhoneNumber() != null ? currentGuest.getPhoneNumber() : "");
            txtAddress.setText(currentGuest.getAddress() != null ? currentGuest.getAddress() : "");
            txtDateOfBirth.setText(currentGuest.getDateOfBirth() != null ? currentGuest.getDateOfBirth().toString() : "");
            txtNationality.setText(currentGuest.getNationality() != null ? currentGuest.getNationality() : "");
            txtIdType.setText(currentGuest.getIdDocumentType() != null ? currentGuest.getIdDocumentType() : "");
            txtIdNumber.setText(currentGuest.getIdDocumentNumber() != null ? currentGuest.getIdDocumentNumber() : "");
            
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to load guest profile: " + e.getMessage());
        }
    }
    
    private void enableEditMode() {
        isEditingProfile = true;
        txtFirstName.setEditable(true);
        txtMiddleName.setEditable(true);
        txtLastName.setEditable(true);
        txtEmail.setEditable(true);
        txtPhone.setEditable(true);
        txtAddress.setEditable(true);
        txtDateOfBirth.setEditable(true);
        txtNationality.setEditable(true);
        txtIdType.setEditable(true);
        txtIdNumber.setEditable(true);
        
        btnEditProfile.setEnabled(false);
        btnSaveProfile.setEnabled(true);
        btnCancel.setEnabled(true);
    }
    
    private void saveProfile() {
        try {
            if (!ValidationUtil.validateName(txtFirstName.getText())) {
                MessageBox.showError("Validation Error", "First name is invalid");
                return;
            }
            
            if (!ValidationUtil.validateName(txtLastName.getText())) {
                MessageBox.showError("Validation Error", "Last name is invalid");
                return;
            }
            
            if (!ValidationUtil.validateEmail(txtEmail.getText())) {
                MessageBox.showError("Validation Error", "Email is invalid");
                return;
            }
            
            if (!ValidationUtil.validatePhoneNumber(txtPhone.getText())) {
                MessageBox.showError("Validation Error", "Phone number must be in Philippine format");
                return;
            }
            
            currentGuest.setFirstName(txtFirstName.getText());
            currentGuest.setMiddleName(txtMiddleName.getText());
            currentGuest.setLastName(txtLastName.getText());
            currentGuest.setEmail(txtEmail.getText());
            currentGuest.setPhoneNumber(txtPhone.getText());
            currentGuest.setAddress(txtAddress.getText());
            currentGuest.setDateOfBirth(LocalDate.parse(txtDateOfBirth.getText()));
            currentGuest.setNationality(txtNationality.getText());
            currentGuest.setIdDocumentType(txtIdType.getText());
            currentGuest.setIdDocumentNumber(txtIdNumber.getText());
            
            guestService.updateGuest(currentGuest);
            
            MessageBox.showInfo("Success", "Profile updated successfully");
            cancelEdit();
            
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to update profile: " + e.getMessage());
        }
    }
    
    private void cancelEdit() {
        isEditingProfile = false;
        loadGuestProfile();
        
        txtFirstName.setEditable(false);
        txtMiddleName.setEditable(false);
        txtLastName.setEditable(false);
        txtEmail.setEditable(false);
        txtPhone.setEditable(false);
        txtAddress.setEditable(false);
        txtDateOfBirth.setEditable(false);
        txtNationality.setEditable(false);
        txtIdType.setEditable(false);
        txtIdNumber.setEditable(false);
        
        btnEditProfile.setEnabled(true);
        btnSaveProfile.setEnabled(false);
        btnCancel.setEnabled(false);
    }
    
    private void changePassword() {
        try {
            String currentPassword = new String(txtCurrentPassword.getPassword());
            String newPassword = new String(txtNewPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                MessageBox.showError("Validation Error", "All password fields are required");
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                MessageBox.showError("Validation Error", "New passwords do not match");
                return;
            }
            
            if (!ValidationUtil.validatePassword(newPassword)) {
                MessageBox.showError("Validation Error", "Password must contain uppercase, lowercase, and numbers");
                return;
            }
            
            MessageBox.showInfo("Success", "Password changed successfully (functionality to be implemented)");
            
            txtCurrentPassword.setText("");
            txtNewPassword.setText("");
            txtConfirmPassword.setText("");
            
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to change password: " + e.getMessage());
        }
    }
}
