package GUIAdmin;

import Models.User;
import Services.UserService;
import Utilities.HotelException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementPanel extends JPanel implements ActionListener {

    private JTable userTable;
    private DefaultTableModel model;
    private JLabel lblTotal;
    private JButton btnRefresh, btnAdd, btnDelete;
    private JTextField txtUsername, txtPassword, txtEmail;
    private JComboBox<String> cbAccessLevel;

    private UserService userService = new UserService();

    public UserManagementPanel() {
        setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotal = new JLabel("Total Users: 0");
        topPanel.add(lblTotal);
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(this);
        topPanel.add(btnRefresh);
        btnAdd = new JButton("Add User");
        btnAdd.addActionListener(this);
        topPanel.add(btnAdd);
        btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(this);
        topPanel.add(btnDelete);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Username", "Email", "Access Level", "Active"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        userTable = new JTable(model);
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));
        userTable.getTableHeader().setBackground(Color.decode("#222222"));
        userTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(userTable);
        add(scroll, BorderLayout.CENTER);

        // Form panel (bottom)
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add/Edit User"));
        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);
        formPanel.add(new JLabel("Password:"));
        txtPassword = new JTextField();
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Access Level:"));
        cbAccessLevel = new JComboBox<>(new String[]{"Guest", "Receptionist", "Admin"});
        formPanel.add(cbAccessLevel);
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveUser());
        formPanel.add(btnSave);
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> clearForm());
        formPanel.add(btnClear);
        add(formPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<User> users = userService.findAllUsers();
            lblTotal.setText("Total Users: " + users.size());
            for (User u : users) {
                model.addRow(new Object[]{
                    u.getUserId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getAccessLevel(),
                    u.isActive() ? "Yes" : "No"
                });
            }
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }

    private void saveUser() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String email = txtEmail.getText().trim();
        String accessLevel = (String) cbAccessLevel.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            userService.addUser(username, password, email);
            // Note: addUser in UserService uses "Guest" as default, but we want to set accessLevel.
            // We need to update the user after creation. Let's do it manually:
            User user = userService.getUserByUsername(username);
            if (user != null) {
                user.setAccessLevel(accessLevel);
                userService.updateUser(user);
            }
            JOptionPane.showMessageDialog(this, "User added successfully.");
            loadData();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        cbAccessLevel.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadData();
        } else if (e.getSource() == btnAdd) {
            clearForm();
        } else if (e.getSource() == btnDelete) {
            int row = userTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a user to delete.");
                return;
            }
            String username = (String) model.getValueAt(row, 1);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete user '" + username + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    userService.removeUser(username);
                    JOptionPane.showMessageDialog(this, "User deleted.");
                    loadData();
                } catch (HotelException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        }
    }
}