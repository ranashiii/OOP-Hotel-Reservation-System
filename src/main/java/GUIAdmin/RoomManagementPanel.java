package GUIAdmin;

import Models.Room;
import Services.RoomService;
import Utilities.HotelException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RoomManagementPanel extends JPanel implements ActionListener {

    private JTable roomTable;
    private DefaultTableModel model;
    private JLabel lblTotal;
    private JButton btnRefresh, btnAdd, btnUpdate, btnDelete;
    private JTextField txtRoomNumber, txtRoomType, txtFloor, txtCapacity, txtPrice, txtStatus;

    private RoomService roomService = new RoomService();

    public RoomManagementPanel() {
        setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotal = new JLabel("Total Rooms: 0");
        topPanel.add(lblTotal);
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(this);
        topPanel.add(btnRefresh);
        btnAdd = new JButton("Add Room");
        btnAdd.addActionListener(this);
        topPanel.add(btnAdd);
        btnUpdate = new JButton("Update Selected");
        btnUpdate.addActionListener(this);
        topPanel.add(btnUpdate);
        btnDelete = new JButton("Delete Selected");
        btnDelete.addActionListener(this);
        topPanel.add(btnDelete);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Room No.", "Type", "Floor", "Capacity", "Price/Night", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        roomTable = new JTable(model);
        roomTable.setRowHeight(25);
        roomTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));
        roomTable.getTableHeader().setBackground(Color.decode("#222222"));
        roomTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(roomTable);
        add(scroll, BorderLayout.CENTER);

        // Form panel (bottom)
        JPanel formPanel = new JPanel(new GridLayout(3, 6, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Room Details"));
        formPanel.add(new JLabel("Room Number:"));
        txtRoomNumber = new JTextField();
        formPanel.add(txtRoomNumber);
        formPanel.add(new JLabel("Type:"));
        txtRoomType = new JTextField();
        formPanel.add(txtRoomType);
        formPanel.add(new JLabel("Floor:"));
        txtFloor = new JTextField();
        formPanel.add(txtFloor);
        formPanel.add(new JLabel("Capacity:"));
        txtCapacity = new JTextField();
        formPanel.add(txtCapacity);
        formPanel.add(new JLabel("Price/Night:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);
        formPanel.add(new JLabel("Status:"));
        txtStatus = new JTextField();
        formPanel.add(txtStatus);
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveRoom());
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
            List<Room> rooms = roomService.findAllRooms();
            lblTotal.setText("Total Rooms: " + rooms.size());
            for (Room r : rooms) {
                model.addRow(new Object[]{
                    r.getRoomId(),
                    r.getRoomNumber(),
                    r.getRoomType(),
                    r.getFloor(),
                    r.getCapacity(),
                    String.format("%.2f", r.getPricePerNight()),
                    r.getStatus()
                });
            }
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + ex.getMessage());
        }
    }

    private void saveRoom() {
        String roomNumber = txtRoomNumber.getText().trim();
        String roomType = txtRoomType.getText().trim();
        String floor = txtFloor.getText().trim();
        String capacity = txtCapacity.getText().trim();
        String price = txtPrice.getText().trim();
        String status = txtStatus.getText().trim();

        if (roomNumber.isEmpty() || roomType.isEmpty() || floor.isEmpty() || capacity.isEmpty() || price.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            // Check if room exists
            if (roomService.roomExists(roomNumber)) {
                // Update
                roomService.updateRoom(roomNumber, roomType, floor, capacity, price);
                JOptionPane.showMessageDialog(this, "Room updated successfully.");
            } else {
                // Add
                roomService.addRoom(roomNumber, roomType, floor, capacity, price, status);
                JOptionPane.showMessageDialog(this, "Room added successfully.");
            }
            loadData();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtRoomNumber.setText("");
        txtRoomType.setText("");
        txtFloor.setText("");
        txtCapacity.setText("");
        txtPrice.setText("");
        txtStatus.setText("");
    }

    private void fillFormFromSelected() {
        int row = roomTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a room first.");
            return;
        }
        txtRoomNumber.setText((String) model.getValueAt(row, 1));
        txtRoomType.setText((String) model.getValueAt(row, 2));
        txtFloor.setText(String.valueOf(model.getValueAt(row, 3)));
        txtCapacity.setText(String.valueOf(model.getValueAt(row, 4)));
        txtPrice.setText(String.valueOf(model.getValueAt(row, 5)).replace(",", ""));
        txtStatus.setText((String) model.getValueAt(row, 6));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadData();
        } else if (e.getSource() == btnAdd) {
            clearForm();
        } else if (e.getSource() == btnUpdate) {
            fillFormFromSelected();
        } else if (e.getSource() == btnDelete) {
            int row = roomTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a room to delete.");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete room ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    roomService.deleteRoom(id);
                    JOptionPane.showMessageDialog(this, "Room deleted.");
                    loadData();
                } catch (HotelException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        }
    }
}