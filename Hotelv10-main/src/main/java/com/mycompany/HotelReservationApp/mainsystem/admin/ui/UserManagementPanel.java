package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserManagementPanel extends JPanel {

    private static final Color DARK = Color.decode("#222222");

    private final JFrame parent;
    private final UserService userService;
    private JLabel lblCount;

    public UserManagementPanel(JFrame parent, UserService userService) {
        this.parent = parent;
        this.userService = userService;
        setLayout(null);
        setBackground(Color.decode("#F5F5F5"));
        buildUI();
    }

    private void buildUI() {
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(20, 20, 830, 460);
        formPanel.setBackground(Color.WHITE);
        add(formPanel);

        JLabel lblTitle = new JLabel("USER MANAGEMENT");
        lblTitle.setBounds(30, 20, 500, 35);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        formPanel.add(lblTitle);

        lblCount = new JLabel("Total staff accounts: " + userService.findAllUsers().size());
        lblCount.setBounds(30, 60, 400, 25);
        lblCount.setFont(new Font("Arial Black", Font.PLAIN, 13));
        formPanel.add(lblCount);

        addLabel(formPanel, "Full Name:", 30, 100);
        JTextField txtName = new JTextField();
        txtName.setBounds(30, 128, 360, 40);
        txtName.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtName);

        addLabel(formPanel, "Email:", 30, 180);
        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(30, 208, 360, 40);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtEmail);

        addLabel(formPanel, "Password:", 30, 260);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(30, 288, 360, 40);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtPassword);

        JButton btnAdd = createDarkButton("ADD USER");
        btnAdd.setBounds(30, 360, 160, 45);
        btnAdd.addActionListener(e -> {
            String name  = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String pass  = new String(txtPassword.getPassword()).trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "All fields are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE); return;
            }
            if (!userService.isValidEmail(email)) {
                JOptionPane.showMessageDialog(parent, "Enter a valid email address.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE); return;
            }
            if (userService.userExists(email)) {
                JOptionPane.showMessageDialog(parent, "A user with that email already exists.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE); return;
            }
            userService.addUser(name, email, pass);
            JOptionPane.showMessageDialog(parent, "Staff account added successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            txtName.setText(""); txtEmail.setText(""); txtPassword.setText("");
            refreshCount();
        });
        formPanel.add(btnAdd);

        JButton btnRemove = createDarkButton("REMOVE USER");
        btnRemove.setBounds(210, 360, 160, 45);
        btnRemove.addActionListener(e -> {
            if (userService.findAllUsers().isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "No users available to remove.", "Remove User",
                    JOptionPane.WARNING_MESSAGE); return;
            }
            showRemoveDialog();
        });
        formPanel.add(btnRemove);

        JButton btnView = createDarkButton("VIEW ALL USERS");
        btnView.setBounds(390, 360, 190, 45);
        btnView.addActionListener(e -> showUserListDialog());
        formPanel.add(btnView);
    }

    void showRemoveDialog() {
        JDialog dlg = new JDialog(parent, "Remove User", true);
        dlg.setSize(500, 420);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setLocationRelativeTo(parent);

        JLabel lbl = new JLabel("Select a user to remove:");
        lbl.setBounds(20, 15, 350, 25);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        dlg.add(lbl);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String[] u : userService.findAllUsers())
            model.addElement(u[0] + " | " + u[1] + " | " + u[3]);

        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(20, 50, 445, 260);
        dlg.add(sp);

        JButton btnDo = createDarkButton("REMOVE SELECTED");
        btnDo.setBounds(20, 325, 200, 40);
        btnDo.addActionListener(e2 -> {
            int idx = list.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(dlg, "Please select a user first.",
                    "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            int c = JOptionPane.showConfirmDialog(dlg, "Remove this user?",
                "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                String email = userService.findAllUsers().get(idx)[1];
                userService.removeUser(email);
                JOptionPane.showMessageDialog(dlg, "User removed successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
                refreshCount();
            }
        });
        dlg.add(btnDo);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(235, 325, 100, 40);
        btnClose.setBackground(new Color(220, 220, 220));
        btnClose.setForeground(Color.DARK_GRAY);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e2 -> dlg.dispose());
        dlg.add(btnClose);

        dlg.setVisible(true);
    }

    void showUserListDialog() {
        JDialog dlg = new JDialog(parent, "All Staff Accounts", true);
        dlg.setSize(500, 400);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setLocationRelativeTo(parent);

        JLabel lbl = new JLabel("All Staff Accounts");
        lbl.setBounds(20, 15, 300, 25);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        dlg.add(lbl);

        DefaultListModel<String> model = new DefaultListModel<>();
        if (userService.findAllUsers().isEmpty()) {
            model.addElement("No staff accounts yet.");
        } else {
            for (String[] u : userService.findAllUsers())
                model.addElement(u[0] + " | " + u[1] + " | " + u[3]);
        }
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(20, 50, 445, 280);
        dlg.add(sp);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(185, 340, 120, 35);
        btnClose.setBackground(new Color(220, 220, 220));
        btnClose.setForeground(Color.DARK_GRAY);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dlg.dispose());
        dlg.add(btnClose);

        dlg.setVisible(true);
    }

    void refreshCount() {
        lblCount.setText("Total staff accounts: " + userService.findAllUsers().size());
    }

    void addLabel(JPanel p, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 300, 25);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        p.add(lbl);
    }

    JButton createDarkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
        btn.setBackground(DARK);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
