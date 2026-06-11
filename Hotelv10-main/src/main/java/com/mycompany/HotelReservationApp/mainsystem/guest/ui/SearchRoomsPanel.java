package com.mycompany.HotelReservationApp.mainsystem.guest.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

/**
 * SearchRoomsPanel - Search and Filter Available Rooms
 * Displays available rooms with filters by date, type, capacity, and price
 */
public class SearchRoomsPanel extends JPanel implements ActionListener {
    
    private JTextField txtCheckIn, txtCheckOut, txtMinPrice, txtMaxPrice;
    private JComboBox<String> cmbRoomType, cmbCapacity;
    private JButton btnSearch, btnClear;
    private JTable roomsTable;
    private DefaultTableModel tableModel;
    private JLabel lblErrorMessage;
    
    public SearchRoomsPanel() {
        setLayout(null);
        setBackground(Color.decode("#F5F5F5"));
        createComponents();
    }
    
    private void createComponents() {
        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(30, 20, 880, 50);
        titleBar.setBackground(Color.decode("#222222"));
        add(titleBar);
        
        JLabel lblTitle = new JLabel("SEARCH AVAILABLE ROOMS");
        lblTitle.setBounds(15, 8, 400, 34);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);
        
        lblErrorMessage = new JLabel();
        lblErrorMessage.setFont(new Font("Arial", Font.PLAIN, 11));
        lblErrorMessage.setForeground(new Color(244, 67, 54));
        lblErrorMessage.setBounds(30, 75, 880, 20);
        lblErrorMessage.setVisible(false);
        add(lblErrorMessage);
        
        JPanel filterPanel = new JPanel(null);
        filterPanel.setBounds(30, 100, 880, 80);
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(filterPanel);
        
        JLabel lblCheckIn = new JLabel("Check-In (YYYY-MM-DD):");
        lblCheckIn.setBounds(15, 10, 150, 20);
        lblCheckIn.setFont(new Font("Arial", Font.BOLD, 11));
        filterPanel.add(lblCheckIn);
        
        txtCheckIn = new JTextField();
        txtCheckIn.setBounds(170, 8, 120, 25);
        filterPanel.add(txtCheckIn);
        
        JLabel lblCheckOut = new JLabel("Check-Out (YYYY-MM-DD):");
        lblCheckOut.setBounds(310, 10, 150, 20);
        lblCheckOut.setFont(new Font("Arial", Font.BOLD, 11));
        filterPanel.add(lblCheckOut);
        
        txtCheckOut = new JTextField();
        txtCheckOut.setBounds(465, 8, 120, 25);
        filterPanel.add(txtCheckOut);
        
        JLabel lblType = new JLabel("Room Type:");
        lblType.setBounds(15, 40, 150, 20);
        lblType.setFont(new Font("Arial", Font.BOLD, 11));
        filterPanel.add(lblType);
        
        cmbRoomType = new JComboBox<>(new String[]{
            "- All Types -", "Single Standard", "Double Standard", "Double Deluxe", "Suite Deluxe"
        });
        cmbRoomType.setBounds(170, 38, 120, 25);
        filterPanel.add(cmbRoomType);
        
        JLabel lblCapacity = new JLabel("Capacity:");
        lblCapacity.setBounds(310, 40, 150, 20);
        lblCapacity.setFont(new Font("Arial", Font.BOLD, 11));
        filterPanel.add(lblCapacity);
        
        cmbCapacity = new JComboBox<>(new String[]{
            "- Any -", "1 Guest", "2 Guests", "3 Guests", "4+ Guests"
        });
        cmbCapacity.setBounds(465, 38, 120, 25);
        filterPanel.add(cmbCapacity);
        
        JLabel lblPrice = new JLabel("Price Range:");
        lblPrice.setBounds(610, 40, 100, 20);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 11));
        filterPanel.add(lblPrice);
        
        txtMinPrice = new JTextField("0");
        txtMinPrice.setBounds(710, 38, 50, 25);
        filterPanel.add(txtMinPrice);
        
        JLabel lblTo = new JLabel("to");
        lblTo.setBounds(765, 40, 20, 20);
        filterPanel.add(lblTo);
        
        txtMaxPrice = new JTextField("10000");
        txtMaxPrice.setBounds(785, 38, 60, 25);
        filterPanel.add(txtMaxPrice);
        
        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(650, 8, 100, 25);
        btnSearch.setBackground(new Color(76, 175, 80));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 11));
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(this);
        filterPanel.add(btnSearch);
        
        btnClear = new JButton("CLEAR");
        btnClear.setBounds(760, 8, 100, 25);
        btnClear.setBackground(new Color(255, 193, 7));
        btnClear.setForeground(Color.BLACK);
        btnClear.setFont(new Font("Arial", Font.BOLD, 11));
        btnClear.setBorderPainted(false);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(this);
        filterPanel.add(btnClear);
        
        String[] columns = {"Room #", "Type", "Floor", "Capacity", "Price/Night", "Amenities"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        roomsTable = new JTable(tableModel);
        roomsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        roomsTable.setRowHeight(22);
        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        scrollPane.setBounds(30, 190, 880, 330);
        add(scrollPane);
        
        loadSampleRooms();
    }
    
    private void loadSampleRooms() {
        tableModel.addRow(new Object[]{"101", "Single Standard", "1", "1", "₱2,500", "WiFi, TV, AC"});
        tableModel.addRow(new Object[]{"102", "Double Standard", "1", "2", "₱4,000", "WiFi, TV, AC, Bathtub"});
        tableModel.addRow(new Object[]{"201", "Double Deluxe", "2", "2", "₱5,000", "WiFi, TV, AC, Minibar, Balcony"});
        tableModel.addRow(new Object[]{"301", "Suite Deluxe", "3", "4", "₱8,000", "WiFi, TV, AC, Minibar, Kitchenette"});
        tableModel.addRow(new Object[]{"103", "Single Standard", "1", "1", "₱2,500", "WiFi, TV, AC"});
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            performSearch();
        } else if (e.getSource() == btnClear) {
            clearFilters();
        }
    }
    
    private void performSearch() {
        String checkIn = txtCheckIn.getText().trim();
        String checkOut = txtCheckOut.getText().trim();
        
        if (checkIn.isEmpty() || checkOut.isEmpty()) {
            showError("Please enter check-in and check-out dates");
            return;
        }
        
        try {
            LocalDate inDate = LocalDate.parse(checkIn);
            LocalDate outDate = LocalDate.parse(checkOut);
            
            if (outDate.isBefore(inDate) || outDate.equals(inDate)) {
                showError("Check-out date must be after check-in date");
                return;
            }
            
            if (inDate.isBefore(LocalDate.now())) {
                showError("Check-in date cannot be in the past");
                return;
            }
            
            JOptionPane.showMessageDialog(this, "Searching for available rooms...");
            lblErrorMessage.setVisible(false);
        } catch (Exception ex) {
            showError("Invalid date format. Use YYYY-MM-DD");
        }
    }
    
    private void clearFilters() {
        txtCheckIn.setText("");
        txtCheckOut.setText("");
        cmbRoomType.setSelectedIndex(0);
        cmbCapacity.setSelectedIndex(0);
        txtMinPrice.setText("0");
        txtMaxPrice.setText("10000");
        lblErrorMessage.setVisible(false);
    }
    
    private void showError(String message) {
        lblErrorMessage.setText("⚠ " + message);
        lblErrorMessage.setVisible(true);
    }
}
