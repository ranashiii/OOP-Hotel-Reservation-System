package GUIAdmin;

import UI.StyledButton;
import Utilities.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame implements ActionListener {

    // Shared services - package-visible so HomePage and the panel classes can use them
    final RoomService roomService;
    final UserService userService;
    final ReservationService reservationService;
    final PaymentService paymentService;

    // Sidebar nav buttons - referenced by HomePage when deciding which panel to switch to
    JButton btnReports;
    JButton btnUserManagement;
    JButton btnRoomManagement;
    JButton btnReservations;

    private JPanel contentPanel;
    private JLabel lblHeader;
    private JPanel sidebar;

    private static final Color SIDEBAR_BG = new Color(45, 85, 130);
    private static final Color SIDEBAR_ACTIVE = new Color(30, 60, 95);

    public AdminDashboard() {
        this.roomService = new RoomService();
        this.userService = new UserService();
        this.reservationService = new ReservationService();
        this.paymentService = new PaymentService();

        initWindow();
        createComponents();
        Logger.getInstance().info("AdminDashboard opened");
    }

    private void initWindow() {
        setTitle("Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void createComponents() {
        // Top header bar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(SIDEBAR_BG);

        lblHeader = new JLabel("DASHBOARD");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.add(lblHeader, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        // Sidebar navigation
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        btnReports = createNavButton("REPORTS");
        btnUserManagement = createNavButton("USER MANAGEMENT");
        btnRoomManagement = createNavButton("ROOM MANAGEMENT");
        btnReservations = createNavButton("RESERVATIONS");

        btnReports.addActionListener(e -> switchTo(btnReports, "REPORTS & STATISTICS",
            new ReportsPanel(this, roomService, userService, reservationService, paymentService)));
        btnUserManagement.addActionListener(e -> switchTo(btnUserManagement, "USER MANAGEMENT",
            new UserManagementPanel(this, userService)));
        btnRoomManagement.addActionListener(e -> switchTo(btnRoomManagement, "ROOM MANAGEMENT",
            new RoomManagementPanel(this, roomService)));
        btnReservations.addActionListener(e -> switchTo(btnReservations, "RESERVATIONS",
            new ReservationsPanel(this, reservationService)));

        sidebar.add(btnReports);
        sidebar.add(btnUserManagement);
        sidebar.add(btnRoomManagement);
        sidebar.add(btnReservations);
        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);

        // Content area - panels get swapped in here via switchTo()
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);

        // Default view on open
        switchTo(btnReports, "REPORTS & STATISTICS",
            new ReportsPanel(this, roomService, userService, reservationService, paymentService));
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 55));
        btn.setPreferredSize(new Dimension(220, 55));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Swaps the visible panel in the content area, updates the header text,
     * and highlights the active sidebar button.
     */
    void switchTo(JButton activeButton, String headerText, JPanel panel) {
        lblHeader.setText(headerText);

        for (Component c : sidebar.getComponents()) {
            if (c instanceof JButton) {
                c.setBackground(SIDEBAR_BG);
            }
        }
        if (activeButton != null) {
            activeButton.setBackground(SIDEBAR_ACTIVE);
        }

        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}