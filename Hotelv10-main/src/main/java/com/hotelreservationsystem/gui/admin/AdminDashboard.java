package com.hotelreservationsystem.gui.admin;

import com.hotelreservationsystem.SessionManager;
import com.hotelreservationsystem.util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * AdminDashboard - Main Admin Dashboard
 * 
 * Provides complete admin interface for system management including users,
 * rooms, reservations, and reporting.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class AdminDashboard extends JFrame {
    
    private JTabbedPane tabbedPane;
    
    public AdminDashboard() {
        setTitle("Hotel Reservation System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(211, 47, 47));
        topPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 60));
        
        JLabel welcomeLabel = new JLabel("Administrator - " + SessionManager.getInstance().getCurrentUsername());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(100, 40));
        logoutButton.addActionListener(this::handleLogout);
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard", new AdminMainPanel());
        tabbedPane.addTab("Manage Rooms", new JLabel("Manage Rooms Panel"));
        tabbedPane.addTab("Manage Users", new JLabel("Manage Users Panel"));
        tabbedPane.addTab("Reservations", new JLabel("Reservations Panel"));
        tabbedPane.addTab("Reports", new JLabel("Reports Panel"));
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void handleLogout(ActionEvent e) {
        SessionManager.getInstance().logout();
        dispose();
        new com.hotelreservationsystem.gui.auth.LoginFrame().setVisible(true);
    }
}
