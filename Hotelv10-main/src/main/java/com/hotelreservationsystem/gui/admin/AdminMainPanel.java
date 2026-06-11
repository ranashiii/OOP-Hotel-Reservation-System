package com.hotelreservationsystem.gui.admin;

import com.hotelreservationsystem.service.DashboardService;
import com.hotelreservationsystem.util.HotelException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * AdminMainPanel - Main Dashboard Panel for Administrators
 * 
 * Shows system-wide statistics including total rooms, occupancy, and revenue metrics.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class AdminMainPanel extends JPanel {
    
    private JLabel totalRoomsLabel;
    private JLabel occupiedRoomsLabel;
    private JLabel availableRoomsLabel;
    private JLabel occupancyRateLabel;
    
    public AdminMainPanel() {
        setLayout(null);
        initializeComponents();
        loadDashboardData();
    }
    
    private void initializeComponents() {
        // Date display
        LocalDate today = LocalDate.now();
        JLabel dateLabel = new JLabel("System Overview - " + today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        dateLabel.setBounds(30, 20, 400, 30);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(dateLabel);
        
        // Statistics boxes
        addStatisticBox(30, 80, "Total Rooms", "totalRooms");
        addStatisticBox(280, 80, "Occupied Rooms", "occupiedRooms");
        addStatisticBox(530, 80, "Available Rooms", "availableRooms");
        addStatisticBox(30, 220, "Occupancy Rate", "occupancyRate");
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
            case "totalRooms":
                totalRoomsLabel = valueLabel;
                break;
            case "occupiedRooms":
                occupiedRoomsLabel = valueLabel;
                break;
            case "availableRooms":
                availableRoomsLabel = valueLabel;
                break;
            case "occupancyRate":
                occupancyRateLabel = valueLabel;
                break;
        }
        
        boxPanel.add(valueLabel, BorderLayout.CENTER);
        add(boxPanel);
    }
    
    private void loadDashboardData() {
        try {
            int totalRooms = DashboardService.getTotalRoomsCount();
            int occupiedRooms = DashboardService.getOccupiedRoomsCount();
            int availableRooms = DashboardService.getAvailableRoomsCount();
            double occupancyRate = DashboardService.getOccupancyRate();
            
            totalRoomsLabel.setText(String.valueOf(totalRooms));
            occupiedRoomsLabel.setText(String.valueOf(occupiedRooms));
            availableRoomsLabel.setText(String.valueOf(availableRooms));
            occupancyRateLabel.setText(String.format("%.1f%%", occupancyRate));
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
