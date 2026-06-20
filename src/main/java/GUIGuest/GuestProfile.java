package GUIGuest;

import Models.Guest;
import Models.User;
import Services.GuestService;
import Services.UserService;
import Utilities.HotelException;
import Utilities.ValidationUtil;
import HotelReservationMainSystem.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GuestProfile extends JFrame implements ActionListener {

    private JTextField txtFirstName, txtMiddleName, txtLastName, txtEmail, txtPhone, txtAddress;
    private JFormattedTextField txtDob;
    private JComboBox<String> cbNationality, cbDocType;
    private JTextField txtDocNumber;
    private JPasswordField txtNewPassword, txtConfirmPassword;
    private JButton btnUpdate, btnChangePassword, btnBack;

    private Guest currentGuest;

    public GuestProfile() {
        setTitle("Guest Profile");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        addLabelAndField("First Name:", txtFirstName = new JTextField(20), row++, gbc);
        addLabelAndField("Middle Name:", txtMiddleName = new JTextField(20), row++, gbc);
        addLabelAndField("Last Name:", txtLastName = new JTextField(20), row++, gbc);
        addLabelAndField("Email:", txtEmail = new JTextField(20), row++, gbc);
        addLabelAndField("Phone:", txtPhone = new JTextField(20), row++, gbc);
        addLabelAndField("Address:", txtAddress = new JTextField(20), row++, gbc);

        JLabel lblDob = new JLabel("Date of Birth (yyyy-MM-dd):");
        txtDob = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        txtDob.setColumns(20);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(lblDob, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(txtDob, gbc);
        row++;

        JLabel lblNat = new JLabel("Nationality:");
        cbNationality = new JComboBox<>(new String[]{"Filipino", "American", "British", "Other"});
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(lblNat, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(cbNationality, gbc);
        row++;

        JLabel lblDocType = new JLabel("ID Document Type:");
        cbDocType = new JComboBox<>(new String[]{"Passport", "Driver's License", "National ID"});
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(lblDocType, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(cbDocType, gbc);
        row++;

        addLabelAndField("ID Document Number:", txtDocNumber = new JTextField(20), row++, gbc);

        JLabel sep = new JLabel("--- Change Password (optional) ---");
        sep.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3;
        add(sep, gbc);
        row++;

        addLabelAndField("New Password:", txtNewPassword = new JPasswordField(20), row++, gbc);
        addLabelAndField("Confirm Password:", txtConfirmPassword = new JPasswordField(20), row++, gbc);

        btnUpdate = new JButton("Update Profile");
        btnChangePassword = new JButton("Change Password");
        btnBack = new JButton("Back to Dashboard");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnChangePassword);
        buttonPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 3;
        add(buttonPanel, gbc);

        btnUpdate.addActionListener(this);
        btnChangePassword.addActionListener(this);
        btnBack.addActionListener(e -> { dispose(); new GuestDashboard().setVisible(true); });

        loadProfile();
    }

    private void addLabelAndField(String labelText, JTextField field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        add(label, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(field, gbc);
    }

    private void loadProfile() {
        try {
            int userId = SessionManager.getInstance().getUserId();
            if (userId == 0) {
                JOptionPane.showMessageDialog(this, "User not logged in.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            GuestService guestService = new GuestService();
            currentGuest = guestService.getGuestByUserId(userId);
            if (currentGuest == null) {
                JOptionPane.showMessageDialog(this, "Guest profile not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            txtFirstName.setText(currentGuest.getFirstName());
            txtMiddleName.setText(currentGuest.getMiddleName());
            txtLastName.setText(currentGuest.getLastName());
            txtEmail.setText(currentGuest.getEmail());
            txtPhone.setText(currentGuest.getPhoneNumber());
            txtAddress.setText(currentGuest.getAddress());
            if (currentGuest.getDateOfBirth() != null) {
                txtDob.setText(new SimpleDateFormat("yyyy-MM-dd").format(currentGuest.getDateOfBirth()));
            }
            cbNationality.setSelectedItem(currentGuest.getNationality());
            cbDocType.setSelectedItem(currentGuest.getIdDocumentType());
            txtDocNumber.setText(currentGuest.getIdDocumentNumber());
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUpdate) {
            updateProfile();
        } else if (e.getSource() == btnChangePassword) {
            changePassword();
        }
    }

    private void updateProfile() {
        if (currentGuest == null) return;
        try {
            currentGuest.setFirstName(txtFirstName.getText().trim());
            currentGuest.setMiddleName(txtMiddleName.getText().trim());
            currentGuest.setLastName(txtLastName.getText().trim());
            currentGuest.setEmail(txtEmail.getText().trim());
            currentGuest.setPhoneNumber(txtPhone.getText().trim());
            currentGuest.setAddress(txtAddress.getText().trim());
            currentGuest.setNationality((String) cbNationality.getSelectedItem());
            currentGuest.setIdDocumentType((String) cbDocType.getSelectedItem());
            currentGuest.setIdDocumentNumber(txtDocNumber.getText().trim());

            GuestService guestService = new GuestService();
            boolean updated = guestService.updateGuest(currentGuest);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changePassword() {
        String newPass = new String(txtNewPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());
        if (newPass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill both password fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ValidationUtil.validatePassword(newPass)) {
            JOptionPane.showMessageDialog(this, "Password must be 8+ chars with uppercase, lowercase, and number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            UserService userService = new UserService();
            boolean changed = userService.updatePassword(SessionManager.getInstance().getUserId(), newPass);
            if (changed) {
                JOptionPane.showMessageDialog(this, "Password changed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Password change failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}