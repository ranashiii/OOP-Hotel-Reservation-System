package GUIAdmin;

import Models.Payment;
import Models.Reservation;
import Models.Room;
import Models.User;
import Services.PaymentService;
import Services.ReservationService;
import Services.RoomService;
import Services.UserService;
import Utilities.HotelException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private JTable roomTable, userTable, reservationTable, paymentTable;
    private DefaultTableModel roomModel, userModel, reservationModel, paymentModel;
    private JLabel lblRoomCount, lblUserCount, lblReservationCount, lblPaymentCount;

    private RoomService roomService = new RoomService();
    private UserService userService = new UserService();
    private ReservationService reservationService = new ReservationService();
    private PaymentService paymentService = new PaymentService();

    public ReportsPanel() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // --- Room Report Tab ---
        JPanel roomPanel = createRoomReportPanel();
        tabbedPane.addTab("Rooms", roomPanel);

        // --- User Report Tab ---
        JPanel userPanel = createUserReportPanel();
        tabbedPane.addTab("Users", userPanel);

        // --- Reservation Report Tab ---
        JPanel reservationPanel = createReservationReportPanel();
        tabbedPane.addTab("Reservations", reservationPanel);

        // --- Payment Report Tab ---
        JPanel paymentPanel = createPaymentReportPanel();
        tabbedPane.addTab("Payments", paymentPanel);

        add(tabbedPane, BorderLayout.CENTER);
        loadAllReports();
    }

    private JPanel createRoomReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblRoomCount = new JLabel("Total Rooms: 0");
        topPanel.add(lblRoomCount);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadRoomReport());
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Room Number", "Type", "Floor", "Capacity", "Price/Night", "Status"};
        roomModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        roomTable = new JTable(roomModel);
        roomTable.setRowHeight(25);
        roomTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));
        roomTable.getTableHeader().setBackground(Color.decode("#222222"));
        roomTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(roomTable);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUserReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblUserCount = new JLabel("Total Users: 0");
        topPanel.add(lblUserCount);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadUserReport());
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Username", "Email", "Access Level", "Active"};
        userModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        userTable = new JTable(userModel);
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));
        userTable.getTableHeader().setBackground(Color.decode("#222222"));
        userTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(userTable);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReservationReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblReservationCount = new JLabel("Total Reservations: 0");
        topPanel.add(lblReservationCount);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadReservationReport());
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Guest ID", "Room ID", "Check-in", "Check-out", "Status", "Total"};
        reservationModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        reservationTable = new JTable(reservationModel);
        reservationTable.setRowHeight(25);
        reservationTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));
        reservationTable.getTableHeader().setBackground(Color.decode("#222222"));
        reservationTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(reservationTable);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPaymentReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblPaymentCount = new JLabel("Total Payments: 0");
        topPanel.add(lblPaymentCount);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadPaymentReport());
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Reservation ID", "Amount", "Method", "Status", "Date"};
        paymentModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        paymentTable = new JTable(paymentModel);
        paymentTable.setRowHeight(25);
        paymentTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));
        paymentTable.getTableHeader().setBackground(Color.decode("#222222"));
        paymentTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(paymentTable);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void loadAllReports() {
        loadRoomReport();
        loadUserReport();
        loadReservationReport();
        loadPaymentReport();
    }

    private void loadRoomReport() {
        roomModel.setRowCount(0);
        try {
            List<Room> rooms = roomService.findAllRooms();
            lblRoomCount.setText("Total Rooms: " + rooms.size());
            for (Room r : rooms) {
                roomModel.addRow(new Object[]{
                    r.getRoomId(),
                    r.getRoomNumber(),
                    r.getRoomType(),
                    r.getFloor(),
                    r.getCapacity(),
                    String.format("%.2f", r.getPricePerNight()),
                    r.getStatus()
                });
            }
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage());
        }
    }

    private void loadUserReport() {
        userModel.setRowCount(0);
        try {
            List<User> users = userService.findAllUsers();
            lblUserCount.setText("Total Users: " + users.size());
            for (User u : users) {
                userModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getAccessLevel(),
                    u.isActive() ? "Yes" : "No"
                });
            }
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage());
        }
    }

    private void loadReservationReport() {
        reservationModel.setRowCount(0);
        try {
            List<Reservation> reservations = reservationService.findAllReservations();
            lblReservationCount.setText("Total Reservations: " + reservations.size());
            for (Reservation r : reservations) {
                reservationModel.addRow(new Object[]{
                    r.getReservationId(),
                    r.getGuestId(),
                    r.getRoomId(),
                    r.getCheckInDate(),
                    r.getCheckOutDate(),
                    r.getReservationStatus(),
                    String.format("%.2f", r.getFinalTotal())
                });
            }
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage());
        }
    }

    private void loadPaymentReport() {
        paymentModel.setRowCount(0);
        try {
            List<Payment> payments = paymentService.findAllPayments();
            lblPaymentCount.setText("Total Payments: " + payments.size());
            for (Payment p : payments) {
                paymentModel.addRow(new Object[]{
                    p.getPaymentId(),
                    p.getReservationId(),
                    String.format("%.2f", p.getPaymentAmount()),
                    p.getPaymentMethod(),
                    p.getPaymentStatus(),
                    p.getPaymentDate()
                });
            }
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading payments: " + e.getMessage());
        }
    }
}