package GUIGuest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewReservations extends JFrame {

    private JTable resTable;
    private JScrollPane scrollPane;
    private JPanel contentArea;

    ViewReservations() {
        setTitle("Hotel Guest System - View Reservations");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        // ===================== SIDEBAR =====================
        JPanel sidebar = new JPanel(null);
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

        sidebar.add(makeSideBtn("Search Rooms",       160, "View Reservations", this, () -> openFrame(new SearchRooms())));
        sidebar.add(makeSideBtn("Make Reservation",   230, "View Reservations", this, () -> openFrame(new MakeReservation())));
        sidebar.add(makeSideBtn("View Reservations",  300, "View Reservations", this, () -> openFrame(new ViewReservations())));
        sidebar.add(makeSideBtn("Cancel Reservation", 370, "View Reservations", this, () -> openFrame(new CancelReservation())));
        sidebar.add(makeSideBtn("Guest Profile",      440, "View Reservations", this, () -> openFrame(new GuestProfile())));
        sidebar.add(makeSideBtn("Logout",             610, "View Reservations", this, () -> {{
            int c = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {{
                // TODO: DB CONNECT [LOGOUT] - SessionManager.clearSession()
                // SessionManager.clearSession();
                // new LoginFrame().setVisible(true);
                dispose();
            }}
        }}));

        contentArea = new JPanel(null);
        contentArea.setBounds(250, 0, 950, 700);
        contentArea.setBackground(Color.decode("#F5F5F5"));
        add(contentArea);

        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 950, 80);
        titleBar.setBackground(Color.decode("#222222"));
        contentArea.add(titleBar);

        JLabel lblTitle = new JLabel("VIEW RESERVATIONS");
        lblTitle.setBounds(20, 15, 600, 50);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);

        String[] columns = {"ID", "Room", "Check In", "Check Out", "Status"};

        // TODO: DB CONNECT [LOAD RESERVATIONS] - ReservationDAO.getReservationsByGuestId(guestId)
        // TABLE: reservations JOIN rooms ON reservations.room_id = rooms.room_id
        // Query: SELECT reservation_id, rooms.room_number, check_in_date, check_out_date, reservation_status
        //        FROM reservations JOIN rooms ON reservations.room_id = rooms.room_id
        //        WHERE guest_id = SessionManager.getCurrentGuestId()
        //        ORDER BY check_in_date DESC
        // After query: build Object[][] data from ResultSet and pass to JTable
        Object[][] data = {};

        resTable = new JTable(data, columns);
        resTable.setFont(new Font("Arial", Font.PLAIN, 13));
        resTable.setRowHeight(28);
        resTable.getTableHeader().setBackground(Color.decode("#222222"));
        resTable.getTableHeader().setForeground(Color.WHITE);
        resTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));

        scrollPane = new JScrollPane(resTable);
        scrollPane.setBounds(0, 85, 950, 615);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        contentArea.add(scrollPane);
    }

    private JButton makeSideBtn(String text, int y, String active, JFrame frame, Runnable action) {
        JButton btn = new JButton(text);
        btn.setBounds(0, y, 250, 50);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        boolean isActive = text.equals(active);
        btn.setBackground(isActive ? Color.decode("#444444") : Color.decode("#222222"));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isActive) { btn.setBackground(Color.WHITE); btn.setForeground(Color.BLACK); }
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(isActive ? Color.decode("#444444") : Color.decode("#222222"));
                btn.setForeground(Color.WHITE);
            }
        });
        btn.addActionListener(ev -> { if (!isActive) action.run(); });
        return btn;
    }

    private void openFrame(JFrame next) {
        next.setVisible(true);
        dispose();
    }

}