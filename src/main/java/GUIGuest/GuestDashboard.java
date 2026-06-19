package GUIGuest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GuestDashboard - Main guest menu screen.
 * Entry point after login. Contains sidebar navigation to all guest screens.
 */
public class GuestDashboard extends JFrame implements ActionListener {

    private JLabel lblWelcome;
    private JPanel sidebar;
    private JPanel topPan;
    private JPanel contentArea;
    private JButton btnRooms;
    private JButton btnMake;
    private JButton btnView;
    private JButton btnCancel;
    private JButton btnGProfile;
    private JButton btnLogout;

    GuestDashboard() {
        setTitle("Hotel Guest System - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        // ===================== SIDEBAR =====================
        sidebar = new JPanel(null);
        sidebar.setBounds(0, 0, 250, 700);
        sidebar.setBackground(Color.decode("#222222"));
        add(sidebar);

        JLabel lblHotel = new JLabel("HOTEL");
        lblHotel.setBounds(10, 10, 230, 50);
        lblHotel.setFont(new Font("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidebar.add(lblHotel);

        JLabel lblPortal = new JLabel("GUEST PORTAL");
        lblPortal.setBounds(10, 50, 230, 40);
        lblPortal.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblPortal.setForeground(Color.WHITE);
        sidebar.add(lblPortal);

        btnRooms    = makeSideButton("Search Rooms",       160);
        btnMake     = makeSideButton("Make Reservation",   230);
        btnView     = makeSideButton("View Reservations",  300);
        btnCancel   = makeSideButton("Cancel Reservation", 370);
        btnGProfile = makeSideButton("Guest Profile",      440);
        btnLogout   = makeSideButton("Logout",             610);

        sidebar.add(btnRooms);
        sidebar.add(btnMake);
        sidebar.add(btnView);
        sidebar.add(btnCancel);
        sidebar.add(btnGProfile);
        sidebar.add(btnLogout);

        btnRooms.addActionListener(this);
        btnMake.addActionListener(this);
        btnView.addActionListener(this);
        btnCancel.addActionListener(this);
        btnGProfile.addActionListener(this);
        btnLogout.addActionListener(this);

        // ===================== TOP BANNER =====================
        topPan = new JPanel(null);
        topPan.setBounds(250, 0, 950, 150);
        topPan.setBackground(Color.WHITE);
        add(topPan);

        // TODO: DB CONNECT [WELCOME MESSAGE] - SessionManager.getCurrentUsername()
        // Replace "GUEST DASHBOARD" with "Welcome, <username>!"
        // Example: lblWelcome.setText("Welcome, " + SessionManager.getCurrentUsername() + "!");
        lblWelcome = new JLabel("GUEST DASHBOARD");
        lblWelcome.setBounds(20, 45, 800, 70);
        lblWelcome.setFont(new Font("Arial Black", Font.BOLD, 50));
        topPan.add(lblWelcome);

        // ===================== CONTENT AREA =====================
        contentArea = new JPanel(null);
        contentArea.setBounds(250, 150, 950, 550);
        contentArea.setBackground(Color.decode("#F5F5F5"));
        add(contentArea);

        // Quick access menu cards
        addMenuCard(contentArea, "Search Rooms",       "Find available rooms by date,\ntype and capacity",       30,  20,  Color.decode("#333333"));
        addMenuCard(contentArea, "Make Reservation",   "Book a room and complete\nyour payment",                 320, 20,  Color.decode("#333333"));
        addMenuCard(contentArea, "View Reservations",  "See all your current and\npast reservations",            610, 20,  Color.decode("#333333"));
        addMenuCard(contentArea, "Cancel Reservation", "Cancel a booking and\ncheck your refund",                30,  200, Color.decode("#333333"));
        addMenuCard(contentArea, "Guest Profile",      "Update your personal info\nand change password",         320, 200, Color.decode("#333333"));
    }

    private void addMenuCard(JPanel parent, String title, String desc, int x, int y, Color bg) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 260, 150);
        card.setBackground(bg);
        card.setBorder(BorderFactory.createLineBorder(Color.decode("#444444")));

        JLabel lblCardTitle = new JLabel(title);
        lblCardTitle.setBounds(15, 15, 230, 25);
        lblCardTitle.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblCardTitle.setForeground(Color.WHITE);
        card.add(lblCardTitle);

        // Multi-line description
        String[] lines = desc.split("\n");
        for (int i = 0; i < lines.length; i++) {
            JLabel lblLine = new JLabel(lines[i]);
            lblLine.setBounds(15, 50 + (i * 20), 230, 18);
            lblLine.setFont(new Font("Arial", Font.PLAIN, 12));
            lblLine.setForeground(Color.LIGHT_GRAY);
            card.add(lblLine);
        }

        JButton btnOpen = new JButton("OPEN →");
        btnOpen.setBounds(15, 105, 100, 28);
        btnOpen.setBackground(Color.WHITE);
        btnOpen.setForeground(Color.BLACK);
        btnOpen.setFont(new Font("Arial Black", Font.BOLD, 11));
        btnOpen.setBorderPainted(false);
        btnOpen.setFocusPainted(false);
        btnOpen.addActionListener(ev -> navigateTo(title));
        card.add(btnOpen);

        parent.add(card);
    }

    private void navigateTo(String screen) {
        JFrame next = null;
        switch (screen) {
            case "Search Rooms":       next = new SearchRooms();       break;
            case "Make Reservation":   next = new MakeReservation();   break;
            case "View Reservations":  next = new ViewReservations();  break;
            case "Cancel Reservation": next = new CancelReservation(); break;
            case "Guest Profile":      next = new GuestProfile();      break;
        }
        if (next != null) {
            next.setVisible(true);
            this.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRooms)    navigateTo("Search Rooms");
        if (e.getSource() == btnMake)     navigateTo("Make Reservation");
        if (e.getSource() == btnView)     navigateTo("View Reservations");
        if (e.getSource() == btnCancel)   navigateTo("Cancel Reservation");
        if (e.getSource() == btnGProfile) navigateTo("Guest Profile");
        if (e.getSource() == btnLogout) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: DB CONNECT [LOGOUT] - SessionManager.clearSession()
                // SessionManager.clearSession();
                // new LoginFrame().setVisible(true);
                this.dispose();
            }
        }
    }

    private JButton makeSideButton(String text, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(0, y, 250, 50);
        btn.setBackground(Color.decode("#222222"));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(Color.WHITE); btn.setForeground(Color.BLACK); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(Color.decode("#222222")); btn.setForeground(Color.WHITE); }
        });
        return btn;
    }
}
