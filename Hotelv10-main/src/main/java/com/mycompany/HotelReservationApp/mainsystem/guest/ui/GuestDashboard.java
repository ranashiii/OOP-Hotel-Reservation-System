package com.mycompany.HotelReservationApp.mainsystem.guest.ui;

import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.session.SessionManager;
import com.mycompany.HotelReservationApp.mainsystem.login.LoginFrame;
import com.mycompany.HotelReservationApp.mainsystem.hotelreservation.util.Logger;

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
            String username = SessionManager.getCurrentUser();
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
            String username = SessionManager.getCurrentUser();
            if (username != null && !username.isEmpty()) {
                welcomeText = "Welcome, " + username + "!";
            }
        } catch (Exception ex) {
            Logger.getInstance().warn("Error retrieving username for welcome message");
        }
        
        lblWelcome = new JLabel(welcomeText);
        lblWelcome.setBounds(280, 50, 650, 60);
        lblWelcome.setFont(new Font("Arial Black", Font.BOLD, 48));
        lblWelcome.setForeground(Color.decode("#222222"));
        topPan.add(lblWelcome);
        
        contentArea = new JPanel();
        contentArea.setBounds(250, 150, 950, 550);
        contentArea.setLayout(null);
        contentArea.setBackground(Color.decode("#F5F5F5"));
        
        add(sidebar);
        add(topPan);
        add(contentArea);
        
        loadDashboardPanel();
    }
    
    private JButton createSideButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(0, y, 250, 50);
        btn.setBackground(Color.decode("#222222"));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != currentlyHighlightedButton) {
                    btn.setBackground(Color.decode("#222222"));
                    btn.setForeground(Color.WHITE);
                }
            }
        });
        return btn;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRooms) {
            highlightButton(btnRooms);
            switchPanel(new SearchRoomsPanel());
        } else if (e.getSource() == btnMake) {
            highlightButton(btnMake);
            switchPanel(new MakeReservationPanel());
        } else if (e.getSource() == btnView) {
            highlightButton(btnView);
            switchPanel(new ViewReservationsPanel());
        } else if (e.getSource() == btnCancel) {
            highlightButton(btnCancel);
            switchPanel(new CancelReservationPanel());
        } else if (e.getSource() == btnProfile) {
            highlightButton(btnProfile);
            switchPanel(new GuestProfilePanel());
        } else if (e.getSource() == btnLogout) {
            handleLogout();
        }
    }
    
    private void highlightButton(JButton btn) {
        if (currentlyHighlightedButton != null) {
            currentlyHighlightedButton.setBackground(Color.decode("#222222"));
            currentlyHighlightedButton.setForeground(Color.WHITE);
        }
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        currentlyHighlightedButton = btn;
    }
    
    private void switchPanel(JPanel panel) {
        contentArea.removeAll();
        panel.setBounds(0, 0, 950, 550);
        contentArea.add(panel);
        contentArea.revalidate();
        contentArea.repaint();
    }
    
    private void loadDashboardPanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(null);
        welcomePanel.setBackground(Color.decode("#F5F5F5"));
        
        JLabel lblMessage = new JLabel("Select an option from the menu to get started");
        lblMessage.setBounds(300, 200, 400, 30);
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        lblMessage.setForeground(new Color(100, 100, 100));
        welcomePanel.add(lblMessage);
        
        switchPanel(welcomePanel);
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String username = SessionManager.getCurrentUser();
                if (username != null) {
                    Logger.getInstance().info("User logged out: " + username);
                }
            } catch (Exception ex) {
                Logger.getInstance().warn("Error logging logout event");
            }
            SessionManager.logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
