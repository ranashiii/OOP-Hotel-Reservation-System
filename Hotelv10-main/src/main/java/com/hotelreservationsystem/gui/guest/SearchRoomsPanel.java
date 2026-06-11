package com.hotelreservationsystem.gui.guest;

import com.hotelreservationsystem.dao.RoomDAO;
import com.hotelreservationsystem.model.Room;
import com.hotelreservationsystem.service.RoomService;
import com.hotelreservationsystem.util.Constants;
import com.hotelreservationsystem.util.DateUtil;
import com.hotelreservationsystem.util.HotelException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * SearchRoomsPanel - Room Search and Booking Panel for Guests
 * 
 * Allows guests to search for available rooms with filters and make reservations.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class SearchRoomsPanel extends JPanel {
    
    private JSpinner checkInDateSpinner;
    private JSpinner checkOutDateSpinner;
    private JComboBox<String> roomTypeCombo;
    private JSpinner capacitySpinner;
    private JTable resultsTable;
    private JButton searchButton;
    private JButton bookButton;
    
    public SearchRoomsPanel() {
        setLayout(null);
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Check-in date
        JLabel checkInLabel = new JLabel("Check-in Date:");
        checkInLabel.setBounds(30, 30, 120, 25);
        add(checkInLabel);
        
        checkInDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(checkInDateSpinner, "yyyy-MM-dd");
        checkInDateSpinner.setEditor(dateEditor);
        checkInDateSpinner.setBounds(150, 30, 120, 25);
        add(checkInDateSpinner);
        
        // Check-out date
        JLabel checkOutLabel = new JLabel("Check-out Date:");
        checkOutLabel.setBounds(290, 30, 120, 25);
        add(checkOutLabel);
        
        checkOutDateSpinner = new JSpinner(new SpinnerDateModel());
        dateEditor = new JSpinner.DateEditor(checkOutDateSpinner, "yyyy-MM-dd");
        checkOutDateSpinner.setEditor(dateEditor);
        checkOutDateSpinner.setBounds(410, 30, 120, 25);
        add(checkOutDateSpinner);
        
        // Room type
        JLabel roomTypeLabel = new JLabel("Room Type:");
        roomTypeLabel.setBounds(550, 30, 100, 25);
        add(roomTypeLabel);
        
        roomTypeCombo = new JComboBox<>(new String[]{
            "All", "Single Standard", "Double Standard", "Double Deluxe", "Suite Deluxe"
        });
        roomTypeCombo.setBounds(650, 30, 120, 25);
        add(roomTypeCombo);
        
        // Capacity
        JLabel capacityLabel = new JLabel("Capacity:");
        capacityLabel.setBounds(30, 70, 120, 25);
        add(capacityLabel);
        
        capacitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        capacitySpinner.setBounds(150, 70, 120, 25);
        add(capacitySpinner);
        
        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(290, 70, 100, 25);
        searchButton.addActionListener(e -> performSearch());
        add(searchButton);
        
        // Results table
        resultsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBounds(30, 120, 740, 300);
        add(scrollPane);
        
        // Book button
        bookButton = new JButton("Book Selected Room");
        bookButton.setBounds(30, 440, 150, 35);
        bookButton.addActionListener(e -> bookRoom());
        add(bookButton);
    }
    
    private void performSearch() {
        try {
            LocalDate checkInDate = LocalDate.now();
            LocalDate checkOutDate = LocalDate.now().plusDays(1);
            
            List<Room> availableRooms = RoomService.getAvailableRooms(checkInDate, checkOutDate);
            
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Room #");
            model.addColumn("Type");
            model.addColumn("Floor");
            model.addColumn("Capacity");
            model.addColumn("Price/Night");
            model.addColumn("Status");
            
            for (Room room : availableRooms) {
                model.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getFloor(),
                    room.getCapacity(),
                    "PHP " + room.getPricePerNight(),
                    room.getStatus()
                });
            }
            
            resultsTable.setModel(model);
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void bookRoom() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow >= 0) {
            JOptionPane.showMessageDialog(this, "Booking feature coming soon!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room first", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
