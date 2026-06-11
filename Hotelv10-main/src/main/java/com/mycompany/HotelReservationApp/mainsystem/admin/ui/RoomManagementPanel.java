package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoomManagementPanel extends JPanel {

    private static final Color DARK = Color.decode("#222222");

    private final JFrame parent;
    private final RoomService roomService;
    private JLabel lblCount;

    public RoomManagementPanel(JFrame parent, RoomService roomService) {
        this.parent = parent;
        this.roomService = roomService;
        setLayout(null);
        setBackground(Color.decode("#F5F5F5"));
        buildUI();
    }

    private void buildUI() {
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(20, 20, 830, 470);
        formPanel.setBackground(Color.WHITE);
        add(formPanel);

        JLabel lblTitle = new JLabel("ROOM MANAGEMENT");
        lblTitle.setBounds(30, 20, 500, 35);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        formPanel.add(lblTitle);

        lblCount = new JLabel("Total rooms on record: " + roomService.findAllRooms().size());
        lblCount.setBounds(30, 60, 400, 25);
        lblCount.setFont(new Font("Arial Black", Font.PLAIN, 13));
        formPanel.add(lblCount);

        addLabel(formPanel, "Room Number:", 30, 100);
        JTextField txtRoomNumber = new JTextField();
        txtRoomNumber.setBounds(30, 128, 250, 40);
        txtRoomNumber.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtRoomNumber);

        addLabel(formPanel, "Room Type:", 310, 100);
        JComboBox<String> cmbRoomType = new JComboBox<>(new String[]{
        "Single Standard", "Double Standard", "Double Deluxe", "Suite Deluxe"
        });
        cmbRoomType.setBounds(310, 128, 250, 40);
        cmbRoomType.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cmbRoomType);


        addLabel(formPanel, "Nightly Rate:", 30, 185);
        JTextField txtRate = new JTextField();
        txtRate.setBounds(30, 213, 250, 40);
        txtRate.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtRate);

        addLabel(formPanel, "Capacity:", 310, 185);
        JComboBox<String> cmbCapacity = new JComboBox<>(new String[]{
        "1","2","3","4","5","6","7","8","9","10"
        });
        cmbCapacity.setBounds(310, 213, 250, 40);
        cmbCapacity.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cmbCapacity);

        addLabel(formPanel, "Status:", 30, 270);
        JComboBox<String> cmbStatus = new JComboBox<>(
        new String[]{"AVAILABLE", "OCCUPIED", "MAINTENANCE"});
        cmbStatus.setBounds(30, 298, 250, 40);
        cmbStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cmbStatus);

        addLabel(formPanel, "Floor Number:", 310, 270);
        JComboBox<String> cmbFloor = new JComboBox<>(new String[]{
        "1","2","3","4","5","6","7","8","9","10"
        });
        cmbFloor.setBounds(310, 298, 250, 40);
        cmbFloor.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cmbFloor);

        JButton btnAdd = createDarkButton("ADD ROOM");
        btnAdd.setBounds(30, 370, 160, 45);
        btnAdd.addActionListener(e -> {
            String rn  = txtRoomNumber.getText().trim();
            String rt  = (String) cmbRoomType.getSelectedItem();
            String ra  = txtRate.getText().trim();
            String cap = (String) cmbCapacity.getSelectedItem();
            String floor = (String) cmbFloor.getSelectedItem();
            String st  = (String) cmbStatus.getSelectedItem();

            if (rn.isEmpty() || rt.isEmpty() || ra.isEmpty() || cap.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Fill out all fields.",
                    "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            try { Double.parseDouble(ra); } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parent, "Nightly rate must be a valid number.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE); return;
            }
            if (roomService.roomExists(rn)) {
                JOptionPane.showMessageDialog(parent, "Room number already exists.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE); return;
            }
            roomService.addRoom(rn, rt, ra, cap, st, floor);
            JOptionPane.showMessageDialog(parent, "Room added successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            txtRoomNumber.setText("");
            cmbRoomType.setSelectedIndex(0);
            txtRate.setText("");
            cmbCapacity.setSelectedIndex(0);
            cmbFloor.setSelectedIndex(0);
            cmbStatus.setSelectedIndex(0);
            refreshCount();
        });
        formPanel.add(btnAdd);

        JButton btnEdit = createDarkButton("EDIT ROOM");
        btnEdit.setBounds(210, 370, 160, 45);
        btnEdit.addActionListener(e -> {
            if (roomService.findAllRooms().isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "No rooms available to edit. Please add a room first.",
                    "Edit Room", JOptionPane.WARNING_MESSAGE); return;
            }
            showEditDialog();
        });
        formPanel.add(btnEdit);

        JButton btnView = createDarkButton("VIEW ALL ROOMS");
        btnView.setBounds(390, 370, 190, 45);
        btnView.addActionListener(e -> showRoomListDialog());
        formPanel.add(btnView);
    }

    void showEditDialog() {
        JDialog dlg = new JDialog(parent, "Edit Room", true);
        dlg.setSize(620, 480);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setLocationRelativeTo(parent);

        JLabel lbl = new JLabel("Select a room to edit:");
        lbl.setBounds(20, 15, 300, 25);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        dlg.add(lbl);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String[] r : roomService.findAllRooms())
            model.addElement(r[0] + " | " + r[1] + " | PHP " + r[2]
    + " | Cap " + r[3] + " | " + r[4] + " | Floor " + (r.length > 5 ? r[5] : "N/A"));
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(20, 50, 560, 180);
        dlg.add(sp);

        JLabel lblSt = new JLabel("New Status:");
        lblSt.setBounds(20, 245, 120, 22);
        lblSt.setFont(new Font("Arial Black", Font.BOLD, 13));
        dlg.add(lblSt);

        JComboBox<String> cmbSt = new JComboBox<>(
            new String[]{"AVAILABLE", "OCCUPIED", "MAINTENANCE"});
        cmbSt.setBounds(150, 245, 200, 35);
        cmbSt.setFont(new Font("Arial", Font.PLAIN, 13));
        dlg.add(cmbSt);

        JLabel lblRt = new JLabel("New Type:");
        lblRt.setBounds(20, 295, 120, 22);
        lblRt.setFont(new Font("Arial Black", Font.BOLD, 13));
        dlg.add(lblRt);

        JTextField txtType = new JTextField();
        txtType.setBounds(150, 295, 200, 35);
        txtType.setFont(new Font("Arial", Font.PLAIN, 13));
        dlg.add(txtType);

        JLabel lblRa = new JLabel("New Rate:");
        lblRa.setBounds(370, 245, 100, 22);
        lblRa.setFont(new Font("Arial Black", Font.BOLD, 13));
        dlg.add(lblRa);

        JTextField txtRa = new JTextField();
        txtRa.setBounds(480, 245, 100, 35);
        txtRa.setFont(new Font("Arial", Font.PLAIN, 13));
        dlg.add(txtRa);

        list.addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) {
                int idx = list.getSelectedIndex();
                if (idx >= 0) {
                    String[] r = roomService.findAllRooms().get(idx);
                    txtType.setText(r[1]);
                    txtRa.setText(r[2]);
                    cmbSt.setSelectedItem(r[4]);
                }
            }
        });

        JButton btnSave = createDarkButton("UPDATE ROOM");
        btnSave.setBounds(20, 390, 180, 40);
        btnSave.addActionListener(e2 -> {
            int idx = list.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(dlg, "Select a room first.",
                    "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            String[] r  = roomService.findAllRooms().get(idx);
            String type = txtType.getText().trim();
            String rate = txtRa.getText().trim();
            String st   = (String) cmbSt.getSelectedItem();
            if (type.isEmpty() || rate.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Type and Rate cannot be empty.",
                    "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            try { Double.parseDouble(rate); } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Rate must be a valid number.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE); return;
            }
            roomService.updateRoom(r[0], type, rate, r[3], st);
            JOptionPane.showMessageDialog(dlg, "Room updated successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dlg.dispose();
            refreshCount();
        });
        dlg.add(btnSave);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(215, 390, 100, 40);
        btnClose.setBackground(new Color(220, 220, 220));
        btnClose.setForeground(Color.DARK_GRAY);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e2 -> dlg.dispose());
        dlg.add(btnClose);

        dlg.setVisible(true);
    }

    void showRoomListDialog() {
        JDialog dlg = new JDialog(parent, "All Rooms", true);
        dlg.setSize(600, 420);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setLocationRelativeTo(parent);

        JLabel lbl = new JLabel("All Rooms on Record");
        lbl.setBounds(20, 15, 300, 25);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        dlg.add(lbl);

        DefaultListModel<String> model = new DefaultListModel<>();
        if (roomService.findAllRooms().isEmpty()) {
            model.addElement("No rooms added yet.");
        } else {
            for (String[] r : roomService.findAllRooms())
                model.addElement(r[0] + " | " + r[1] + " | PHP " + r[2]
                    + " | Cap " + r[3] + " | " + r[4] + " | Floor " + (r.length > 5 ? r[5] : "N/A"));
        }
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(20, 50, 545, 300);
        dlg.add(sp);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(220, 360, 120, 35);
        btnClose.setBackground(new Color(220, 220, 220));
        btnClose.setForeground(Color.DARK_GRAY);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dlg.dispose());
        dlg.add(btnClose);

        dlg.setVisible(true);
    }

    void refreshCount() {
        lblCount.setText("Total rooms on record: " + roomService.findAllRooms().size());
    }

    void addLabel(JPanel p, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 250, 25);
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
