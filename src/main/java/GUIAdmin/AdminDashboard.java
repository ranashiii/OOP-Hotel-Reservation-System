package GUIAdmin;

import Services.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {

    private JPanel contentPanel;
    private JButton btnReports, btnUserManagement, btnRoomManagement, btnReservations;
    private JPanel sidebar;

    private RoomService roomService = new RoomService();
    private UserService userService = new UserService();
    private ReservationService reservationService = new ReservationService();
    private PaymentService paymentService = new PaymentService();

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);   // <-- NULL LAYOUT
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // ---- Sidebar ----
        sidebar = new JPanel(null);
        sidebar.setBounds(0, 0, 220, 700);
        sidebar.setBackground(new Color(34, 34, 34));
        add(sidebar);

        JLabel lblTitle = new JLabel("ADMIN");
        lblTitle.setBounds(30, 20, 180, 40);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        sidebar.add(lblTitle);

        JLabel lblSub = new JLabel("PANEL");
        lblSub.setBounds(30, 60, 180, 30);
        lblSub.setFont(new Font("Arial", Font.BOLD, 16));
        lblSub.setForeground(new Color(200, 200, 200));
        sidebar.add(lblSub);

        // Navigation buttons
        btnReports = createNavButton("REPORTS", 30, 130);
        btnUserManagement = createNavButton("USER MANAGEMENT", 30, 200);
        btnRoomManagement = createNavButton("ROOM MANAGEMENT", 30, 270);
        btnReservations = createNavButton("RESERVATIONS", 30, 340);

        sidebar.add(btnReports);
        sidebar.add(btnUserManagement);
        sidebar.add(btnRoomManagement);
        sidebar.add(btnReservations);

        // Logout button at bottom
        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBounds(30, 600, 160, 40);
        btnLogout.setBackground(new Color(200, 50, 50));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial Black", Font.BOLD, 14));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(AdminDashboard.this,
                    "Are you sure you want to logout?", "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                HotelReservationMainSystem.SessionManager.getInstance().logout();
                dispose();
                // Return to login frame
                new GUILogin.LoginFrame().setVisible(true);
            }
        });
        sidebar.add(btnLogout);

        // ---- Content area ----
        contentPanel = new JPanel(null);
        contentPanel.setBounds(220, 0, 980, 700);
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel);

        // ---- Button actions ----
        btnReports.addActionListener(e -> switchTo(btnReports, "REPORTS & STATISTICS", new ReportsPanel()));
        btnUserManagement.addActionListener(e -> switchTo(btnUserManagement, "USER MANAGEMENT", new UserManagementPanel()));
        btnRoomManagement.addActionListener(e -> switchTo(btnRoomManagement, "ROOM MANAGEMENT", new RoomManagementPanel()));
        btnReservations.addActionListener(e -> switchTo(btnReservations, "RESERVATIONS", new ReservationsPanel()));

        // Default view
        switchTo(btnReports, "REPORTS & STATISTICS", new ReportsPanel());
    }

    private JButton createNavButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 160, 45);
        btn.setBackground(new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(70, 70, 70));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(50, 50, 50));
            }
        });
        return btn;
    }

    private void switchTo(JButton activeButton, String title, JPanel panel) {
        // Reset all buttons to default
        btnReports.setBackground(new Color(50, 50, 50));
        btnUserManagement.setBackground(new Color(50, 50, 50));
        btnRoomManagement.setBackground(new Color(50, 50, 50));
        btnReservations.setBackground(new Color(50, 50, 50));
        activeButton.setBackground(new Color(100, 100, 100));

        // Clear content panel and add new panel
        contentPanel.removeAll();
        // Set the panel's bounds to fill content area
        panel.setBounds(0, 0, 980, 700);
        // Add a title label on top of the panel (optional, but we can keep a border)
        // We'll wrap the panel in a titled border for clarity
        JPanel wrapper = new JPanel(null);
        wrapper.setBounds(0, 0, 980, 700);
        wrapper.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setBounds(20, 10, 400, 30);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 22));
        lblTitle.setForeground(new Color(50, 50, 50));
        wrapper.add(lblTitle);
        // Place the actual panel below the title
        panel.setBounds(10, 50, 960, 630);
        wrapper.add(panel);
        contentPanel.add(wrapper);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}