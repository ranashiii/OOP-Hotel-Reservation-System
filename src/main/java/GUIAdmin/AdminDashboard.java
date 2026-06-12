package GUIAdmin;

import HotelReservationMainSystem.SessionManager;
import Utilities.Constants;
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
        // NOTE: AdminMainPanel (Dashboard) is fully implemented.
        // ManageRooms, ManageUsers, Reservations, and Reports panels are pending
        // implementation (Member 5 scope). They are wired here as stub panels
        // so the dashboard compiles and the tabs are visible.
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard",    new AdminMainPanel());
        tabbedPane.addTab("Manage Rooms", createStubPanel("Manage Rooms", "Room management interface - Coming soon"));
        tabbedPane.addTab("Manage Users", createStubPanel("Manage Users", "User account management - Coming soon"));
        tabbedPane.addTab("Reservations", createStubPanel("Reservations", "All reservations view - Coming soon"));
        tabbedPane.addTab("Reports",      createStubPanel("Reports",      "Revenue & occupancy reports - Coming soon"));
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void handleLogout(ActionEvent e) {
        SessionManager.getInstance().logout();
        dispose();
        new GUILogin.LoginFrame().setVisible(true);
    }

    /**
     * Creates a temporary placeholder panel for tabs not yet implemented.
     * Replace each call with the real panel class when it is ready.
     *
     * @param heading     bold heading text shown at the top
     * @param description descriptive subtitle shown below the heading
     * @return a JPanel centred placeholder
     */
    private JPanel createStubPanel(String heading, String description) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 189, 189)),
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));

        JLabel lblHeading = new JLabel(heading);
        lblHeading.setFont(new Font("Arial", Font.BOLD, 22));
        lblHeading.setForeground(new Color(33, 33, 33));
        lblHeading.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblHeading);

        card.add(Box.createVerticalStrut(10));

        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDesc.setForeground(new Color(117, 117, 117));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblDesc);

        panel.add(card);
        return panel;
    }
}