package com.hotelreservationsystem.gui.guest;

import com.hotelreservationsystem.SessionManager;
import com.hotelreservationsystem.util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * GuestDashboard - Main Guest Dashboard
 * 
 * Provides main interface for guests to access all guest features including
 * search, reservations, profile management, and cancellations.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class GuestDashboard extends JFrame {
    
    private JTabbedPane tabbedPane;
    private SearchRoomsPanel searchRoomsPanel;
    private ViewReservationsPanel viewReservationsPanel;
    private GuestProfilePanel profilePanel;
    
    public GuestDashboard() {
        setTitle("Hotel Reservation System - Guest Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Top panel with welcome and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(25, 118, 210));
        topPanel.setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, 60));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + SessionManager.getInstance().getCurrentUsername());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(100, 40));
        logoutButton.addActionListener(this::handleLogout);
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        
        searchRoomsPanel = new SearchRoomsPanel();
        tabbedPane.addTab("Search & Book", searchRoomsPanel);
        
        viewReservationsPanel = new ViewReservationsPanel();
        tabbedPane.addTab("My Reservations", viewReservationsPanel);
        
        profilePanel = new GuestProfilePanel();
        tabbedPane.addTab("My Profile", profilePanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void handleLogout(ActionEvent e) {
        SessionManager.getInstance().logout();
        dispose();
        new LoginFrame().setVisible(true);
    }
}
