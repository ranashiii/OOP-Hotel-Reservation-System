package GUILogin;

import Config.AppConfig;
import Services.UserService;
import Utilities.Constants;
import Utilities.HotelException;
import Utilities.MessageBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * RegistrationFrame - Guest Account Registration Dialog
 *
 * Allows new users to register a Guest account by providing a username,
 * email address, password, and password confirmation. All inputs are
 * validated via UserService before the account is created.
 *
 * Displayed as a modal dialog launched from LoginFrame.
 *
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class RegistrationFrame extends JDialog implements ActionListener {

    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;

    private final UserService userService = new UserService();

    /**
     * Constructor - creates a modal registration dialog attached to the login window.
     *
     * @param parent the LoginFrame that opened this dialog
     */
    public RegistrationFrame(JFrame parent) {
        super(parent, "Create New Account", true);
        setSize(460, 410);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
        createComponents();
    }

    /**
     * Build and lay out all UI components using absolute positioning (setLayout = null).
     */
    private void createComponents() {
        // ── Title ──────────────────────────────────────────────────────────────
        JLabel lblTitle = new JLabel("Create New Account");
        lblTitle.setFont(AppConfig.FONT_HEADING);
        lblTitle.setForeground(AppConfig.COLOR_PRIMARY_DARK);
        lblTitle.setBounds(30, 15, 400, 30);
        add(lblTitle);

        JLabel lblSubtitle = new JLabel("Fill in the details below to register as a Guest.");
        lblSubtitle.setFont(AppConfig.FONT_SMALL);
        lblSubtitle.setForeground(AppConfig.COLOR_TEXT_LIGHT);
        lblSubtitle.setBounds(30, 48, 400, 16);
        add(lblSubtitle);

        // ── Username ───────────────────────────────────────────────────────────
        addFieldLabel("Username: (5-20 chars, letters/numbers/underscore)", 75);
        txtUsername = new JTextField();
        txtUsername.setFont(AppConfig.FONT_REGULAR);
        txtUsername.setBounds(30, 98, 400, AppConfig.TEXT_FIELD_HEIGHT);
        txtUsername.setBorder(BorderFactory.createLineBorder(AppConfig.COLOR_BORDER));
        add(txtUsername);

        // ── Email ──────────────────────────────────────────────────────────────
        addFieldLabel("Email Address:", 145);
        txtEmail = new JTextField();
        txtEmail.setFont(AppConfig.FONT_REGULAR);
        txtEmail.setBounds(30, 168, 400, AppConfig.TEXT_FIELD_HEIGHT);
        txtEmail.setBorder(BorderFactory.createLineBorder(AppConfig.COLOR_BORDER));
        add(txtEmail);

        // ── Password ───────────────────────────────────────────────────────────
        addFieldLabel("Password: (min 8 chars, upper + lower + digit)", 215);
        txtPassword = new JPasswordField();
        txtPassword.setFont(AppConfig.FONT_REGULAR);
        txtPassword.setBounds(30, 238, 400, AppConfig.TEXT_FIELD_HEIGHT);
        txtPassword.setBorder(BorderFactory.createLineBorder(AppConfig.COLOR_BORDER));
        add(txtPassword);

        // ── Confirm Password ───────────────────────────────────────────────────
        addFieldLabel("Confirm Password:", 285);
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(AppConfig.FONT_REGULAR);
        txtConfirmPassword.setBounds(30, 308, 400, AppConfig.TEXT_FIELD_HEIGHT);
        txtConfirmPassword.setBorder(BorderFactory.createLineBorder(AppConfig.COLOR_BORDER));
        add(txtConfirmPassword);

        // ── Buttons ────────────────────────────────────────────────────────────
        btnRegister = new JButton("REGISTER");
        btnRegister.setFont(AppConfig.FONT_BUTTON);
        btnRegister.setBackground(AppConfig.COLOR_SUCCESS);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBounds(30, 360, 185, AppConfig.BUTTON_HEIGHT);
        btnRegister.setBorderPainted(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(this);
        add(btnRegister);

        btnCancel = new JButton("CANCEL");
        btnCancel.setFont(AppConfig.FONT_BUTTON);
        btnCancel.setBackground(AppConfig.COLOR_ERROR);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBounds(245, 360, 185, AppConfig.BUTTON_HEIGHT);
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(this);
        add(btnCancel);
    }

    /**
     * Helper to add a right-aligned field label at the given y-coordinate.
     *
     * @param text label text
     * @param y    vertical position
     */
    private void addFieldLabel(String text, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(AppConfig.FONT_LABEL);
        lbl.setBounds(30, y, 400, 20);
        add(lbl);
    }

    /**
     * Handle button actions.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            handleRegister();
        } else if (e.getSource() == btnCancel) {
            dispose();
        }
    }

    /**
     * Validate fields and call UserService to create the account.
     * Shows appropriate success or error dialogs.
     */
    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String email    = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm  = new String(txtConfirmPassword.getPassword());

        // Basic empty-field check before sending to service
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            MessageBox.showError("Validation Error", "All fields are required.");
            return;
        }

        try {
            // UserService handles all further validation (format, uniqueness, strength)
            userService.registerUser(username, password, confirm, email, Constants.ACCESS_GUEST);
            MessageBox.showInfo("Registration Successful",
                    "Your account has been created.\nYou can now log in with your username and password.");
            dispose();
        } catch (HotelException ex) {
            MessageBox.showError("Registration Failed", ex.getMessage());
        }
    }
}