package GUIGuest;

import Models.Reservation;
import Models.Room;
import Services.ReservationService;
import Services.RoomService;
import Utilities.HotelException;
import HotelReservationMainSystem.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ViewReservations extends JFrame {

    private JTable resTable;
    private JScrollPane scrollPane;
    private JPanel contentArea;

    public ViewReservations() {
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
        sidebar.add(makeSideBtn("Logout",             610, "View Reservations", this, () -> {
            int c = JOptionPane.showConfirmDialog(ViewReservations.this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                SessionManager.getInstance().logout();
                dispose();
                // new GUILogin.LoginFrame().setVisible(true);
            }
        }));

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

        // ========== LOAD DATA ==========
        String[] columns = {"ID", "Room", "Check In", "Check Out", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        try {
            int guestId = SessionManager.getInstance().getGuestId();
            if (guestId == 0) {
                JOptionPane.showMessageDialog(this, "Please log in first.", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            ReservationService resService = new ReservationService();
            RoomService roomService = new RoomService();
            List<Reservation> reservations = resService.getReservationsByGuestId(guestId);

            for (Reservation r : reservations) {
                Room room = roomService.getRoomById(r.getRoomId());
                String roomNumber = (room != null) ? room.getRoomNumber() : "N/A";
                model.addRow(new Object[]{
                    r.getReservationId(),
                    roomNumber,
                    r.getCheckInDate(),
                    r.getCheckOutDate(),
                    r.getReservationStatus()
                });
            }
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        resTable = new JTable(model);
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

    // ========== Sidebar helpers ==========
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