package GUIAdmin;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    public HomePage() {
        setTitle("Hotel Reservation System - ADMIN DASHBOARD");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.decode("#F0F0F0"));

        JLabel lblTitle = new JLabel("ADMIN DASHBOARD");
        lblTitle.setBounds(0, 40, 1200, 90);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 70));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle);

        String[] labels = {
            "REPORTS",
            "USER MANAGEMENT",
            "ROOM MANAGEMENT",
            "RESERVATIONS"
        };

        int btnW = 380, btnH = 200, gapX = 40, gapY = 30;
        int startX = 190, startY = 190;

        for (int i = 0; i < labels.length; i++) {
            int col = i % 2;
            int row = i / 2;
            int x   = startX + col * (btnW + gapX);
            int y   = startY + row * (btnH + gapY);

            JButton btn = new JButton(labels[i]);
            btn.setBounds(x, y, btnW, btnH);
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial Black", Font.BOLD, 22));
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            final String label = labels[i];
            btn.addActionListener(e -> onButtonClicked(label));
            add(btn);
        }
    }

    private void onButtonClicked(String label) {
        dispose();
        AdminDashboard dashboard = new AdminDashboard();
        dashboard.setVisible(true);

        switch (label) {
            case "REPORTS":
                dashboard.switchTo(dashboard.btnReports, "REPORTS & STATISTICS",
                    new ReportsPanel(dashboard, dashboard.roomService,
                        dashboard.userService, dashboard.reservationService,
                        dashboard.paymentService));
                break;
            case "USER MANAGEMENT":
                dashboard.switchTo(dashboard.btnUserManagement, "USER MANAGEMENT",
                    new UserManagementPanel(dashboard, dashboard.userService));
                break;
            case "ROOM MANAGEMENT":
                dashboard.switchTo(dashboard.btnRoomManagement, "ROOM MANAGEMENT",
                    new RoomManagementPanel(dashboard, dashboard.roomService));
                break;
            case "RESERVATIONS":
                dashboard.switchTo(dashboard.btnReservations, "RESERVATIONS",
                    new ReservationsPanel(dashboard, dashboard.reservationService));
                break;
        }
    }
}