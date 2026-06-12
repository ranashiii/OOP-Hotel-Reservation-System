package MainAndGUIGuest;

import GUILogin.LoginFrame;
import HotelReservationMainSystem.SessionManager;
import Utilities.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GuestDashboard - Main Guest Interface
 * Provides navigation to all guest features
 * Features: Session management, panel switching, logout
 */
public class GuestDashboard extends JFrame implements ActionListener {
    
    private JLabel lblWelcome, lblHotel, lblManagement;
    private JPanel sidebar, topPan, contentArea;
    private JButton btnRooms, btnMake, btnView, btnCancel, btnProfile, btnLogout;
    private JButton currentlyHighlightedButton;
    
    public GuestDashboard() {
        setTitle("Hotel Reservation System - Guest Portal");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        
        try {
            // FIX: Use getInstance() to get singleton, then call instance method
            String username = SessionManager.getInstance().getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                Logger.getInstance().info("GuestDashboard opened for user: " + username);
            }
        } catch (Exception ex) {
            Logger.getInstance().warn("Session not initialized when opening GuestDashboard");
        }
        
        createComponents();
    }
    
    private void createComponents() {
        sidebar = new JPanel();
        sidebar.setBounds(0, 0, 250, 700);
        sidebar.setLayout(null);
        sidebar.setBackground(Color.decode("#222222"));
        
        lblHotel = new JLabel("HOTEL");
        lblHotel.setBounds(10, 10, 230, 50);
        lblHotel.setFont(new Font("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidebar.add(lblHotel);
        
        lblManagement = new JLabel("GUEST PORTAL");
        lblManagement.setBounds(10, 50, 230, 40);
        lblManagement.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblManagement.setForeground(Color.WHITE);
        sidebar.add(lblManagement);
        
        btnRooms = createSideButton("Search Rooms", 160);
        btnMake = createSideButton("Make Reservation", 230);
        btnView = createSideButton("View Reservations", 300);
        btnCancel = createSideButton("Cancel Reservation", 370);
        btnProfile = createSideButton("Guest Profile", 440);
        btnLogout = createSideButton("Logout", 610);
        
        btnRooms.addActionListener(this);
        btnMake.addActionListener(this);
        btnView.addActionListener(this);
        btnCancel.addActionListener(this);
        btnProfile.addActionListener(this);
        btnLogout.addActionListener(this);
        
        sidebar.add(btnRooms);
        sidebar.add(btnMake);
        sidebar.add(btnView);
        sidebar.add(btnCancel);
        sidebar.add(btnProfile);
        sidebar.add(btnLogout);
        
        topPan = new JPanel();
        topPan.setBounds(250, 0, 950, 150);
        topPan.setLayout(null);
        topPan.setBackground(Color.WHITE);
        topPan.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#CCCCCC")));
        
        String welcomeText = "Welcome, Guest!";
        try {
            // FIX: Use getInstance() then call instance method
            String username = SessionManager.getInstance().getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                welcomeText = "Welcome, " + username + "!";
            }
        } catch (Exception ex) {
            Logger.getInstance().warn("Error retrieving username for welcome message");
        }
        
        lblWelcome = new JLabel(welcomeText);
        lblWelcome.setBounds(280, 50, 650, 60);
        lblWelcome.setFont(new Font("Arial Black", Font.BOLD, 48));
        lblWelcome.setForeground(Color.decode("#333333"));
        topPan.add(lblWelcome);
        
        add(sidebar);
        add(topPan);
    }
    
    private JButton createSideButton(String text, int yPos) {
        JButton btn = new JButton(text);
        btn.setBounds(10, yPos, 230, 50);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setBackground(Color.decode("#333333"));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusPainted(false);
        return btn;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Action handler implementation
    }
}