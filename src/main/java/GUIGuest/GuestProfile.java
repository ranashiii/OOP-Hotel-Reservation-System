package GUIGuest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GuestProfile extends JFrame implements ActionListener {

    // labels - personal info
    private JLabel lblTitle;
    private JLabel secInfo;
    private JLabel lFirstName;
    private JLabel lMiddleName;
    private JLabel lLastName;
    private JLabel lEmail;
    private JLabel lPhone;
    private JLabel lAddress;
    private JLabel lDob;
    private JLabel lNationality;
    private JLabel lblIdTypeLbl;
    private JLabel lIdNumber;

    // labels - change password
    private JLabel lOld;
    private JLabel lNew;
    private JLabel lConf;

    // fields
    private JTextField txtFirstName;
    private JTextField txtMiddleName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JTextField txtDob;
    private JTextField txtNationality;
    private JTextField txtIdNumber;
    private JComboBox<String> cbIdType;
    private JPasswordField txtOldPassword;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    // buttons
    private JButton btnUpdate;
    private JButton btnChangePassword;

    // tabs
    private JTabbedPane tabbedPane;

    // scrollpane
    private JScrollPane mainScroll;
    private JScrollBar vBar;

    // panels
    private JPanel sidebar;
    private JPanel titleBar;
    private JPanel contentArea;
    private JPanel formContent;
    private JPanel card;
    private JPanel personalInfoPanel;
    private JPanel changePasswordPanel;

    GuestProfile() {
        setTitle("Hotel Guest System - Guest Profile");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        // ===================== SIDEBAR =====================
        sidebar = new JPanel(null);
        sidebar.setBounds(0, 0, 250, 700);
        sidebar.setBackground(Color.decode("#222222"));
        add(sidebar);

        JLabel lblHotel = new JLabel("HOTEL");
        lblHotel.setBounds(10, 10, 230, 50);
        lblHotel.setFont(new Font("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidebar.add(lblHotel);

        JLabel lblPortal = new JLabel("GUEST PORTAL");
        lblPortal.setBounds(10, 50, 230, 40);
        lblPortal.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblPortal.setForeground(Color.WHITE);
        sidebar.add(lblPortal);

        sidebar.add(makeSideBtn("Search Rooms",       160, "Guest Profile", this, () -> openFrame(new SearchRooms())));
        sidebar.add(makeSideBtn("Make Reservation",   230, "Guest Profile", this, () -> openFrame(new MakeReservation())));
        sidebar.add(makeSideBtn("View Reservations",  300, "Guest Profile", this, () -> openFrame(new ViewReservations())));
        sidebar.add(makeSideBtn("Cancel Reservation", 370, "Guest Profile", this, () -> openFrame(new CancelReservation())));
        sidebar.add(makeSideBtn("Guest Profile",      440, "Guest Profile", this, () -> openFrame(new GuestProfile())));
        sidebar.add(makeSideBtn("Logout",             610, "Guest Profile", this, () -> {
            int c = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                // TODO: DB CONNECT [LOGOUT] - SessionManager.clearSession()
                // SessionManager.clearSession();
                // new LoginFrame().setVisible(true);
                dispose();
            }
        }));

        // ===================== CONTENT AREA =====================
        contentArea = new JPanel(null);
        contentArea.setBounds(250, 0, 950, 700);
        contentArea.setBackground(Color.decode("#F5F5F5"));
        add(contentArea);

        titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 950, 80);
        titleBar.setBackground(Color.decode("#222222"));
        contentArea.add(titleBar);

        lblTitle = new JLabel("GUEST PROFILE");
        lblTitle.setBounds(20, 15, 600, 50);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);

        // ===================== FORM CONTENT + SCROLL =====================
        formContent = new JPanel(null);
        formContent.setBackground(Color.decode("#F5F5F5"));
        formContent.setPreferredSize(new Dimension(880, 900));

        card = new JPanel(null);
        card.setBounds(30, 10, 580, 870);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        formContent.add(card);

        mainScroll = new JScrollPane(formContent);
        mainScroll.setBounds(0, 85, 950, 615);
        mainScroll.setBorder(null);
        mainScroll.setBackground(Color.decode("#F5F5F5"));
        mainScroll.getViewport().setBackground(Color.decode("#F5F5F5"));
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScroll.getVerticalScrollBar().setUnitIncrement(12);

        vBar = mainScroll.getVerticalScrollBar();
        vBar.setBackground(Color.decode("#222222"));
        vBar.setForeground(Color.decode("#555555"));
        vBar.setPreferredSize(new Dimension(8, 0));
        vBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.decode("#555555");
                this.trackColor = Color.decode("#222222");
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
        });
        contentArea.add(mainScroll);

        // ===================== CARD HEADER =====================
        secInfo = new JLabel("PROFILE INFORMATION");
        secInfo.setBounds(25, 15, 350, 22);
        secInfo.setFont(new Font("Arial Black", Font.BOLD, 14));
        card.add(secInfo);

        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
        UIManager.put("TabbedPane.borderHightlightColor", Color.WHITE);
        UIManager.put("TabbedPane.darkShadow", Color.WHITE);
        UIManager.put("TabbedPane.light", Color.WHITE);
        UIManager.put("TabbedPane.highlight", Color.WHITE);
        UIManager.put("TabbedPane.shadow", Color.WHITE);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(15, 45, 545, 820);
        tabbedPane.setFont(new Font("Arial Black", Font.BOLD, 12));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        SwingUtilities.updateComponentTreeUI(tabbedPane);
        card.add(tabbedPane);

        // ===================== PERSONAL INFORMATION TAB =====================
        personalInfoPanel = new JPanel(null);
        personalInfoPanel.setBackground(Color.WHITE);

        lFirstName = new JLabel("First Name:");
        lFirstName.setBounds(15, 20, 200, 25);
        lFirstName.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lFirstName);
        txtFirstName = new JTextField();
        txtFirstName.setBounds(15, 48, 480, 30);
        txtFirstName.setFont(new Font("Arial", Font.PLAIN, 13));
        txtFirstName.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtFirstName);

        lMiddleName = new JLabel("Middle Name:");
        lMiddleName.setBounds(15, 90, 200, 25);
        lMiddleName.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lMiddleName);
        txtMiddleName = new JTextField();
        txtMiddleName.setBounds(15, 118, 480, 30);
        txtMiddleName.setFont(new Font("Arial", Font.PLAIN, 13));
        txtMiddleName.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtMiddleName);

        lLastName = new JLabel("Last Name:");
        lLastName.setBounds(15, 160, 200, 25);
        lLastName.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lLastName);
        txtLastName = new JTextField();
        txtLastName.setBounds(15, 188, 480, 30);
        txtLastName.setFont(new Font("Arial", Font.PLAIN, 13));
        txtLastName.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtLastName);

        lEmail = new JLabel("Email:");
        lEmail.setBounds(15, 230, 200, 25);
        lEmail.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lEmail);
        txtEmail = new JTextField();
        txtEmail.setBounds(15, 258, 480, 30);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        txtEmail.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtEmail);

        lPhone = new JLabel("Phone Number:");
        lPhone.setBounds(15, 300, 200, 25);
        lPhone.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lPhone);
        txtPhone = new JTextField();
        txtPhone.setBounds(15, 328, 480, 30);
        txtPhone.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPhone.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtPhone);

        lAddress = new JLabel("Address:");
        lAddress.setBounds(15, 370, 200, 25);
        lAddress.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lAddress);
        txtAddress = new JTextField();
        txtAddress.setBounds(15, 398, 480, 30);
        txtAddress.setFont(new Font("Arial", Font.PLAIN, 13));
        txtAddress.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtAddress);

        lDob = new JLabel("Date of Birth (YYYY-MM-D):");
        lDob.setBounds(15, 440, 300, 25);
        lDob.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lDob);
        txtDob = new JTextField();
        txtDob.setBounds(15, 468, 480, 30);
        txtDob.setFont(new Font("Arial", Font.PLAIN, 13));
        txtDob.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtDob);

        lNationality = new JLabel("Nationality:");
        lNationality.setBounds(15, 510, 200, 25);
        lNationality.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lNationality);
        txtNationality = new JTextField();
        txtNationality.setBounds(15, 538, 480, 30);
        txtNationality.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNationality.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtNationality);

        lblIdTypeLbl = new JLabel("ID Document Type:");
        lblIdTypeLbl.setBounds(15, 580, 200, 25);
        lblIdTypeLbl.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lblIdTypeLbl);
        cbIdType = new JComboBox<>(new String[]{"- Select -", "Passport", "Driver's License", "National ID", "SSS ID", "PhilHealth ID", "Voter's ID", "Other"});
        cbIdType.setBounds(15, 608, 480, 30);
        cbIdType.setFont(new Font("Arial", Font.PLAIN, 13));
        personalInfoPanel.add(cbIdType);

        lIdNumber = new JLabel("ID Document Number:");
        lIdNumber.setBounds(15, 650, 200, 25);
        lIdNumber.setFont(new Font("Arial Black", Font.BOLD, 12));
        personalInfoPanel.add(lIdNumber);
        txtIdNumber = new JTextField();
        txtIdNumber.setBounds(15, 678, 480, 30);
        txtIdNumber.setFont(new Font("Arial", Font.PLAIN, 13));
        txtIdNumber.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        personalInfoPanel.add(txtIdNumber);

        // TODO: DB CONNECT [ON PANEL LOAD] - GuestDAO.getGuestByUserId(userId)
        // TABLE: guests
        // Pre-fill all fields: first_name, middle_name, last_name, email, phone_number,
        //   address, date_of_birth, nationality, id_document_type, id_document_number
        // WHERE user_id = SessionManager.getCurrentUserId()

        btnUpdate = new JButton("UPDATE PROFILE");
        btnUpdate.setBounds(15, 730, 200, 38);
        btnUpdate.setBackground(Color.decode("#222222"));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("Arial Black", Font.BOLD, 12));
        btnUpdate.setBorderPainted(false);
        btnUpdate.setFocusPainted(false);
        personalInfoPanel.add(btnUpdate);

        tabbedPane.addTab("Personal Information", personalInfoPanel);

        // ===================== CHANGE PASSWORD TAB =====================
        changePasswordPanel = new JPanel(null);
        changePasswordPanel.setBackground(Color.WHITE);

        lOld = new JLabel("Current Password:");
        lOld.setBounds(15, 40, 200, 25);
        lOld.setFont(new Font("Arial Black", Font.BOLD, 12));
        changePasswordPanel.add(lOld);
        txtOldPassword = new JPasswordField();
        txtOldPassword.setBounds(15, 68, 480, 30);
        txtOldPassword.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        changePasswordPanel.add(txtOldPassword);

        lNew = new JLabel("New Password:");
        lNew.setBounds(15, 115, 200, 25);
        lNew.setFont(new Font("Arial Black", Font.BOLD, 12));
        changePasswordPanel.add(lNew);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(15, 143, 480, 30);
        txtPassword.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        changePasswordPanel.add(txtPassword);

        lConf = new JLabel("Confirm New Password:");
        lConf.setBounds(15, 190, 220, 25);
        lConf.setFont(new Font("Arial Black", Font.BOLD, 12));
        changePasswordPanel.add(lConf);
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(15, 218, 480, 30);
        txtConfirmPassword.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        changePasswordPanel.add(txtConfirmPassword);

        btnChangePassword = new JButton("CHANGE PASSWORD");
        btnChangePassword.setBounds(15, 275, 210, 38);
        btnChangePassword.setBackground(Color.decode("#222222"));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setFont(new Font("Arial Black", Font.BOLD, 12));
        btnChangePassword.setBorderPainted(false);
        btnChangePassword.setFocusPainted(false);
        changePasswordPanel.add(btnChangePassword);

        tabbedPane.addTab("Change Password", changePasswordPanel);

        btnUpdate.addActionListener(this);
        btnChangePassword.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUpdate) {
            String firstName   = txtFirstName.getText().trim();
            String lastName    = txtLastName.getText().trim();
            String email       = txtEmail.getText().trim();
            String phone       = txtPhone.getText().trim();
            String address     = txtAddress.getText().trim();
            String dob         = txtDob.getText().trim();
            String nationality = txtNationality.getText().trim();
            String idNumber    = txtIdNumber.getText().trim();

            if (firstName.isEmpty())  { showErr("First Name cannot be empty.");    return; }
            if (lastName.isEmpty())   { showErr("Last Name cannot be empty.");     return; }
            if (email.isEmpty() || !email.contains("@") || !email.contains(".")) { showErr("Please enter a valid email address."); return; }
            if (phone.isEmpty())      { showErr("Phone Number cannot be empty.");  return; }
            if (address.isEmpty())    { showErr("Address cannot be empty.");       return; }
            if (dob.isEmpty())        { showErr("Date of Birth cannot be empty."); return; }

            LocalDate birthDate;
            try { birthDate = LocalDate.parse(dob, new java.time.format.DateTimeFormatterBuilder().appendPattern("yyyy-M-d").toFormatter()); }
            catch (DateTimeParseException ex) { showErr("Date of Birth must be YYYY-MM-DD (e.g. 2026-12-7)."); return; }
            if (birthDate.isAfter(LocalDate.now())) { showErr("Date of Birth cannot be in the future."); return; }

            if (nationality.isEmpty())            { showErr("Nationality cannot be empty.");        return; }
            if (cbIdType.getSelectedIndex() == 0) { showErr("Please select an ID Document Type."); return; }
            if (idNumber.isEmpty())               { showErr("ID Document Number cannot be empty."); return; }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update your profile?", "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: DB CONNECT [UPDATE PROFILE] - GuestDAO.updateGuest(...)
                // TABLE: guests
                // UPDATE guests SET first_name=?, middle_name=?, last_name=?, email=?,
                //   phone_number=?, address=?, date_of_birth=?, nationality=?,
                //   id_document_type=?, id_document_number=?, updated_at=NOW()
                // WHERE guest_id = SessionManager.getCurrentGuestId()
                // ALSO: UPDATE users SET email=?, updated_at=NOW()
                //       WHERE user_id = SessionManager.getCurrentUserId()
                // ON SUCCESS: JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                System.out.println("Profile update confirmed. Proceeding with GuestDAO update...");
            }
        }

        if (e.getSource() == btnChangePassword) {
            String oldPw  = new String(txtOldPassword.getPassword()).trim();
            String newPw  = new String(txtPassword.getPassword()).trim();
            String confPw = new String(txtConfirmPassword.getPassword()).trim();

            if (oldPw.isEmpty())        { showErr("Current Password cannot be empty.");          return; }
            if (newPw.isEmpty())        { showErr("New Password cannot be empty.");               return; }
            if (newPw.length() < 6)     { showErr("New Password must be at least 6 characters."); return; }
            if (confPw.isEmpty())       { showErr("Please confirm your new password.");           return; }
            if (!newPw.equals(confPw))  { showErr("Passwords do not match.");                    return; }
            if (oldPw.equals(newPw))    { showErr("New Password must differ from current.");     return; }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to change your password?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: DB CONNECT [CHANGE PASSWORD] - UserDAO.changePassword(...)
                // STEP 1: SELECT password FROM users WHERE user_id = SessionManager.getCurrentUserId()
                //         Compare hash(oldPw) with stored → if no match: showErr("Current password is incorrect."); return;
                // STEP 2: UPDATE users SET password = hash(newPw), updated_at = NOW()
                //         WHERE user_id = SessionManager.getCurrentUserId()
                // ON SUCCESS: showMessageDialog("Password changed!"); clear all password fields
                System.out.println("Password change confirmed. Proceeding with UserDAO update...");
            }
        }
    }

    private void showErr(String msg) {
        JOptionPane.showMessageDialog(this, "Error: " + msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private JButton makeSideBtn(String text, int y, String active, JFrame frame, Runnable action) {
        JButton btn = new JButton(text);
        btn.setBounds(0, y, 250, 50);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        boolean isActive = text.equals(active);
        btn.setBackground(isActive ? Color.decode("#444444") : Color.decode("#222222"));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isActive) { btn.setBackground(Color.WHITE); btn.setForeground(Color.BLACK); }
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(isActive ? Color.decode("#444444") : Color.decode("#222222"));
                btn.setForeground(Color.WHITE);
            }
        });
        btn.addActionListener(ev -> { if (!isActive) action.run(); });
        return btn;
    }

    private void openFrame(JFrame next) {
        next.setVisible(true);
        dispose();
    }
}