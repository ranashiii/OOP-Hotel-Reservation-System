package GUIAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {

    private JPanel sidePan, topPan, contentPanel;
    JButton btnUserManagement, btnRoomManagement, btnReports, btnReservations, btnLogout;
    private JButton activeBtn = null;
    private JLabel lblPageTitle;

    final RoomService roomService = new RoomService();
    final UserService userService = new UserService();
    final ReservationService reservationService = new ReservationService();
    final PaymentService paymentService = new PaymentService();

    private static final Color SIDEBAR_BG = Color.decode("#222222");
    private static final Color ACTIVE_BG = Color.WHITE;
    private static final Color ACTIVE_FG = Color.BLACK;
    private static final Color INACTIVE_BG = Color.decode("#222222");
    private static final Color INACTIVE_FG = Color.WHITE;

    public AdminDashboard() {
        setTitle("Hotel Reservation System - ADMIN ACCESS");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        sidePan = new JPanel();
        sidePan.setBounds(0, 0, 300, 700);
        sidePan.setLayout(null);
        sidePan.setBackground(SIDEBAR_BG);
        add(sidePan);

        JLabel lblHotel = new JLabel("HOTEL");
        lblHotel.setBounds(10, 10, 280, 50);
        lblHotel.setFont(new Font("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidePan.add(lblHotel);

        JLabel lblMgmt = new JLabel("MANAGEMENT");
        lblMgmt.setBounds(10, 55, 280, 40);
        lblMgmt.setFont(new Font("Arial Black", Font.BOLD, 26));
        lblMgmt.setForeground(Color.WHITE);
        sidePan.add(lblMgmt);

        JButton btnHomePage = createSidebarButton("Home Page");
        btnHomePage.setBounds(0, 160, 300, 50);
        btnHomePage.addActionListener(e -> {
        dispose();
        new HomePage().setVisible(true);
        });
        sidePan.add(btnHomePage);

        btnUserManagement = createSidebarButton("User Management");
        btnUserManagement.setBounds(0, 230, 300, 50);
        sidePan.add(btnUserManagement);

        btnRoomManagement = createSidebarButton("Room Management");
        btnRoomManagement.setBounds(0, 300, 300, 50);
        sidePan.add(btnRoomManagement);

        btnReports = createSidebarButton("Reports");
        btnReports.setBounds(0, 370, 300, 50);
        sidePan.add(btnReports);

        btnReservations = createSidebarButton("Reservations");
        btnReservations.setBounds(0, 440, 300, 50);
        sidePan.add(btnReservations);

        btnLogout = createSidebarButton("Logout");
        btnLogout.setBounds(0, 600, 300, 50);
        sidePan.add(btnLogout);

        topPan = new JPanel();
        topPan.setBounds(0, 0, 1200, 150);
        topPan.setLayout(null);
        topPan.setBackground(Color.WHITE);
        add(topPan);

        lblPageTitle = new JLabel("ADMIN DASHBOARD");
        lblPageTitle.setBounds(350, 50, 800, 80);
        lblPageTitle.setFont(new Font("Arial Black", Font.BOLD, 55));
        lblPageTitle.setForeground(Color.BLACK);
        topPan.add(lblPageTitle);

        contentPanel = new JPanel();
        contentPanel.setBounds(310, 160, 870, 510);
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.decode("#F5F5F5"));
        add(contentPanel);

        btnUserManagement.addActionListener(e ->
            switchTo(btnUserManagement, "USER MANAGEMENT", new UserManagementPanel(this, userService)));

        btnRoomManagement.addActionListener(e ->
            switchTo(btnRoomManagement, "ROOM MANAGEMENT", new RoomManagementPanel(this, roomService)));

        btnReports.addActionListener(e ->
            switchTo(btnReports, "REPORTS & STATISTICS", new ReportsPanel(this, roomService, userService, reservationService, paymentService)));

        btnReservations.addActionListener(e ->
            switchTo(btnReservations, "RESERVATIONS", new ReservationsPanel(this, reservationService)));

        btnLogout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) System.exit(0);
        });

        switchTo(btnReports, "REPORTS & STATISTICS",
        new ReportsPanel(this, roomService, userService, reservationService, paymentService));
        }

    void switchTo(JButton clicked, String title, JPanel panel) {
        if (activeBtn != null) {
            activeBtn.setBackground(INACTIVE_BG);
            activeBtn.setForeground(INACTIVE_FG);
        }
        clicked.setBackground(ACTIVE_BG);
        clicked.setForeground(ACTIVE_FG);
        activeBtn = clicked;

        lblPageTitle.setText(title);

        contentPanel.removeAll();
        panel.setBounds(0, 0, 870, 510);
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial Black", Font.BOLD, 18));
        btn.setBackground(INACTIVE_BG);
        btn.setForeground(INACTIVE_FG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(null);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
