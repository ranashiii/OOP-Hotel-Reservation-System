package GUIReceptionist;

import Models.Guest;
import Models.User;
import Services.GuestService;
import Services.UserService;
import Utilities.HotelException;
import Utilities.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterGuestPanel extends JFrame {

    private JTextField txtFirstName, txtMiddleName, txtLastName, txtEmail, txtPhone, txtAddress;
    private JTextField txtUsername, txtPassword, txtConfirmPassword;
    private JComboBox<String> cbNationality, cbDocType;
    private JTextField txtDocNumber;
    private JFormattedTextField txtDob;
    private JButton btnRegister, btnBack;

    public RegisterGuestPanel() {
        setTitle("Register Guest");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Personal Information Section
        addLabelAndField("First Name:", txtFirstName = new JTextField(20), row++, gbc);
        addLabelAndField("Middle Name:", txtMiddleName = new JTextField(20), row++, gbc);
        addLabelAndField("Last Name:", txtLastName = new JTextField(20), row++, gbc);
        addLabelAndField("Email:", txtEmail = new JTextField(20), row++, gbc);
        addLabelAndField("Phone (09XXXXXXXXX):", txtPhone = new JTextField(20), row++, gbc);
        addLabelAndField("Address:", txtAddress = new JTextField(20), row++, gbc);

        // Date of Birth
        JLabel lblDob = new JLabel("Date of Birth (yyyy-MM-dd):");
        txtDob = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        txtDob.setColumns(20);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(lblDob, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtDob, gbc);
        row++;

        // Nationality
        JLabel lblNat = new JLabel("Nationality:");
        cbNationality = new JComboBox<>(new String[]{"Filipino", "American", "British", "Other"});
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(lblNat, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(cbNationality, gbc);
        row++;

        // ID Document
        JLabel lblDocType = new JLabel("ID Document Type:");
        cbDocType = new JComboBox<>(new String[]{"Passport", "Driver's License", "National ID"});
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(lblDocType, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(cbDocType, gbc);
        row++;

        addLabelAndField("ID Document Number:", txtDocNumber = new JTextField(20), row++, gbc);

        // Account Credentials
        JLabel sep = new JLabel("--- Account Credentials ---");
        sep.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3;
        add(sep, gbc);
        row++;

        addLabelAndField("Username:", txtUsername = new JTextField(20), row++, gbc);
        addLabelAndField("Password:", txtPassword = new JPasswordField(20), row++, gbc);
        addLabelAndField("Confirm Password:", txtConfirmPassword = new JPasswordField(20), row++, gbc);

        // Buttons
        btnRegister = new JButton("Register Guest");
        btnBack = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3;
        add(buttonPanel, gbc);

        btnRegister.addActionListener(e -> registerGuest());
        btnBack.addActionListener(e -> { dispose(); new HomePage().setVisible(true); });
    }

    private void addLabelAndField(String labelText, JTextField field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(label, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(field, gbc);
    }

    private void registerGuest() {
        // 1. Validate all fields
        String firstName = txtFirstName.getText().trim();
        String middleName = txtMiddleName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress.getText().trim();
        String dobText = txtDob.getText().trim();
        String nationality = (String) cbNationality.getSelectedItem();
        String docType = (String) cbDocType.getSelectedItem();
        String docNumber = txtDocNumber.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(((JPasswordField) txtPassword).getPassword());
        String confirm = new String(((JPasswordField) txtConfirmPassword).getPassword());

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() ||
            address.isEmpty() || dobText.isEmpty() || docNumber.isEmpty() || username.isEmpty() ||
            password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationUtil.validateEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ValidationUtil.validatePhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Phone must be 09XXXXXXXXX.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ValidationUtil.validatePassword(password)) {
            JOptionPane.showMessageDialog(this, "Password must be 8+ chars with uppercase, lowercase, and number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date dob;
        try {
            dob = new SimpleDateFormat("yyyy-MM-dd").parse(dobText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 2. Create user account
            UserService userService = new UserService();
            User user = userService.registerUser(username, password, confirm, email, "Guest");

            // 3. Create guest profile
            Guest guest = new Guest();
            guest.setUserId(user.getUserId());
            guest.setFirstName(firstName);
            guest.setMiddleName(middleName);
            guest.setLastName(lastName);
            guest.setEmail(email);
            guest.setPhoneNumber(phone);
            guest.setAddress(address);
            guest.setDateOfBirth(dob);
            guest.setNationality(nationality);
            guest.setIdDocumentType(docType);
            guest.setIdDocumentNumber(docNumber);

            GuestService guestService = new GuestService();
            guestService.registerGuest(guest);

            JOptionPane.showMessageDialog(this, "Guest registered successfully!\nUser ID: " + user.getUserId(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new HomePage().setVisible(true);

        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}