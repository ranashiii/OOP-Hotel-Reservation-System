package com.hotelreservationsystem.gui.receptionist;

import com.hotelreservationsystem.service.DashboardService;
import com.hotelreservationsystem.util.HotelException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ReceptionistMainPanel - Main Dashboard Panel for Receptionists
 * 
 * Shows today's statistics, check-ins, check-outs, and occupancy information.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class ReceptionistMainPanel extends JPanel {
    
    private JLabel todayCheckInsLabel;
    private JLabel todayCheckOutsLabel;
    private JLabel availableRoomsLabel;
    private JLabel occupancyLabel;
    
    public ReceptionistMainPanel() {
        setLayout(null);
        initializeComponents();
        loadDashboardData();
    }
    
    private void initializeComponents() {
        // Date display
        LocalDate today = LocalDate.now();
        JLabel dateLabel = new JLabel("Date: " + today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        dateLabel.setBounds(30, 20, 400, 30);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(dateLabel);
        
        // Statistics boxes
        addStatisticBox(30, 80, "Today's Check-ins", "todayCheckIns");
        addStatisticBox(280, 80, "Today's Check-outs", "todayCheckOuts");
        addStatisticBox(530, 80, "Available Rooms", "availableRooms");
        addStatisticBox(30, 220, "Occupancy Rate", "occupancy");
    }
    
    private void addStatisticBox(int x, int y, String label, String key) {
        JPanel boxPanel = new JPanel();
        boxPanel.setBounds(x, y, 220, 120);
        boxPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        boxPanel.setLayout(new BorderLayout());
        boxPanel.setBackground(Color.WHITE);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        labelComponent.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        boxPanel.add(labelComponent, BorderLayout.NORTH);
        
        JLabel valueLabel = new JLabel("--");
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        switch (key) {
            case "todayCheckIns":
                todayCheckInsLabel = valueLabel;
                break;
            case "todayCheckOuts":
                todayCheckOutsLabel = valueLabel;
                break;
            case "availableRooms":
                availableRoomsLabel = valueLabel;
                break;
            case "occupancy":
                occupancyLabel = valueLabel;
                break;
        }
        
        boxPanel.add(valueLabel, BorderLayout.CENTER);
        add(boxPanel);
    }
    
    private void loadDashboardData() {
        try {
            int availableRooms = DashboardService.getAvailableRoomsCount();
            double occupancy = DashboardService.getOccupancyRate();
            
            availableRoomsLabel.setText(String.valueOf(availableRooms));
            occupancyLabel.setText(String.format("%.1f%%", occupancy));
            todayCheckInsLabel.setText("0");
            todayCheckOutsLabel.setText("0");
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
