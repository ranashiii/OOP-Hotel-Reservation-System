package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import com.mycompany.HotelReservationApp.mainsystem.login.LoginFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * AdminDashboard - Main Admin Interface
 * Provides navigation to all admin features
 * Features: Dashboard overview, room management, user management, reports
 */
public class AdminDashboard extends JFrame implements ActionListener {
    
    private JLabel lblWelcome, lblHotel, lblManagement;
    private JPanel sidebar, topPan, contentArea;
    private JButton btnDashboard, btnRooms, btnUsers, btnReservations, btnReports, btnLogout;
    private JButton currentlyHighlightedButton;
    
    public AdminDashboard() {
        setTitle("Hotel Reservation System - Admin Portal");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
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
        
        lblManagement = new JLabel("ADMIN PORTAL");
        lblManagement.setBounds(10, 50, 230, 40);
        lblManagement.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblManagement.setForeground(Color.WHITE);
        sidebar.add(lblManagement);
        
        btnDashboard = createSideButton("Dashboard", 160);
        btnRooms = createSideButton("Manage Rooms", 230);
        btnUsers = createSideButton("Manage Users", 300);
        btnReservations = createSideButton("All Reservations", 370);
        btnReports = createSideButton("Reports", 440);
        btnLogout = createSideButton("Logout", 610);
        
        btnDashboard.addActionListener(this);
        btnRooms.addActionListener(this);
        btnUsers.addActionListener(this);
        btnReservations.addActionListener(this);
        btnReports.addActionListener(this);
        btnLogout.addActionListener(this);
        
        sidebar.add(btnDashboard);
        sidebar.add(btnRooms);
        sidebar.add(btnUsers);
        sidebar.add(btnReservations);
        sidebar.add(btnReports);
        sidebar.add(btnLogout);
        
        topPan = new JPanel();
        topPan.setBounds(250, 0, 950, 150);
        topPan.setLayout(null);
        topPan.setBackground(Color.WHITE);
        topPan.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#CCCCCC")));
        
        lblWelcome = new JLabel("Welcome, Administrator!");
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
        if (e.getSource() == btnDashboard) {
            highlightButton(btnDashboard);
            loadDashboardPanel();
        } else if (e.getSource() == btnRooms) {
            highlightButton(btnRooms);
            switchPanel(new ManageRoomsPanel());
        } else if (e.getSource() == btnUsers) {
            highlightButton(btnUsers);
            switchPanel(new ManageUsersPanel());
        } else if (e.getSource() == btnReservations) {
            highlightButton(btnReservations);
            switchPanel(new AdminViewReservationsPanel());
        } else if (e.getSource() == btnReports) {
            highlightButton(btnReports);
            switchPanel(new ViewReportsPanel());
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
        JPanel dashPanel = new JPanel(null);
        dashPanel.setLayout(null);
        dashPanel.setBackground(Color.decode("#F5F5F5"));
        
        JPanel card1 = createInfoCard(30, 20, 210, 120, "Total Rooms", "48", new Color(76, 175, 80));
        JPanel card2 = createInfoCard(260, 20, 210, 120, "Available Rooms", "12", new Color(33, 150, 243));
        JPanel card3 = createInfoCard(490, 20, 210, 120, "Today Check-ins", "5", new Color(255, 193, 7));
        JPanel card4 = createInfoCard(720, 20, 210, 120, "Today Check-outs", "3", new Color(244, 67, 54));
        
        dashPanel.add(card1);
        dashPanel.add(card2);
        dashPanel.add(card3);
        dashPanel.add(card4);
        
        JLabel lblStats = new JLabel("Monthly Statistics");
        lblStats.setBounds(30, 160, 300, 25);
        lblStats.setFont(new Font("Arial Black", Font.BOLD, 14));
        dashPanel.add(lblStats);
        
        JPanel statsPanel = new JPanel(null);
        statsPanel.setBounds(30, 190, 900, 150);
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JLabel lblRevenue = new JLabel("Total Revenue: ₱425,600");
        lblRevenue.setBounds(30, 20, 250, 20);
        lblRevenue.setFont(new Font("Arial", Font.BOLD, 14));
        lblRevenue.setForeground(new Color(76, 175, 80));
        statsPanel.add(lblRevenue);
        
        JLabel lblOccupancy = new JLabel("Average Occupancy: 75.5%");
        lblOccupancy.setBounds(30, 50, 250, 20);
        lblOccupancy.setFont(new Font("Arial", Font.BOLD, 14));
        lblOccupancy.setForeground(new Color(33, 150, 243));
        statsPanel.add(lblOccupancy);
        
        JLabel lblBookings = new JLabel("Total Bookings: 156");
        lblBookings.setBounds(30, 80, 250, 20);
        lblBookings.setFont(new Font("Arial", Font.BOLD, 14));
        lblBookings.setForeground(new Color(255, 193, 7));
        statsPanel.add(lblBookings);
        
        JLabel lblCancellations = new JLabel("Cancellations: 8");
        lblCancellations.setBounds(30, 110, 250, 20);
        lblCancellations.setFont(new Font("Arial", Font.BOLD, 14));
        lblCancellations.setForeground(new Color(244, 67, 54));
        statsPanel.add(lblCancellations);
        
        dashPanel.add(statsPanel);
        switchPanel(dashPanel);
    }
    
    private JPanel createInfoCard(int x, int y, int width, int height, String title, String value, Color color) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, width, height);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setBounds(10, 10, width - 20, 20);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTitle.setForeground(new Color(120, 120, 120));
        card.add(lblTitle);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setBounds(10, 40, width - 20, 50);
        lblValue.setFont(new Font("Arial Black", Font.BOLD, 28));
        lblValue.setForeground(color);
        card.add(lblValue);
        
        return card;
    }
    
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}