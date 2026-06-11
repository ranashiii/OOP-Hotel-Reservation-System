package com.hotelreservationsystem.gui.receptionist;

import com.hotelreservationsystem.SessionManager;
import com.hotelreservationsystem.util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * ReceptionistDashboard - Main Receptionist Dashboard
 * 
 * Provides main interface for receptionists to access all reception features
 * including check-in, check-out, payments, and reservations.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class ReceptionistDashboard extends JFrame {
    
    private JTabbedPane tabbedPane;
    
    public ReceptionistDashboard() {
        setTitle("Hotel Reservation System - Receptionist Dashboard");
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
        topPanel.setBackground(new Color(0, 121, 107));
        topPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 60));
        
        JLabel welcomeLabel = new JLabel("Receptionist - " + SessionManager.getInstance().getCurrentUsername());
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
        tabbedPane.addTab("Dashboard", new ReceptionistMainPanel());
        tabbedPane.addTab("Check-in", new JLabel("Check-in Panel"));
        tabbedPane.addTab("Check-out", new JLabel("Check-out Panel"));
        tabbedPane.addTab("Payments", new JLabel("Payment Panel"));
        tabbedPane.addTab("Reservations", new JLabel("Reservations Panel"));
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void handleLogout(ActionEvent e) {
        SessionManager.getInstance().logout();
        dispose();
        new com.hotelreservationsystem.gui.auth.LoginFrame().setVisible(true);
    }
}
