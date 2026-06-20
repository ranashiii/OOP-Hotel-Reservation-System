package GUIReceptionist;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Config.DBConfig;

public class ReceptionistDashboard extends JFrame implements ActionListener {

    private JButton btnSideDash, btnSideRegister, btnSideRoomBooking, btnSideCheckIn, btnSideCheckOut,
            btnSidePay, btnSideReserve, btnSideCancel, btnHomePage, btnRefresh;
    private JLabel lblReception, lblHotel, lblManagement, lblDate, lblReceptionistName, lblReceptionistRole,
            lblRecent, lblCheckInCount, lblCheckOutCount, lblAvailableRooms, lblOccupancyRate, lblTotalReservations;
    private JPanel sidePan, topPan, dashboardPanel, card1, card2, card3, card4, recentPanel;
    private JTable tblReservations;
    private DefaultTableModel reservationTableModel;
    
    private int todaysCheckIns, todaysCheckOuts;
    private int availableRooms;
    private double occupancyRate;
    private Object[][] reservationData;

    ReceptionistDashboard() {
        this.todaysCheckIns = 0;
        this.todaysCheckOuts = 0;
        this.availableRooms = 0;
        this.occupancyRate = 0.0;
        this.reservationData = new Object[0][];

        sidePanel();
        functionMenu();
        topPanel();
        dashboardPan();

        // Load real data from database
        loadDashboardStats();

        refreshDisplay();

        setSize(1200,700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Hotel Reservation System - Receptionist Dashboard");
    }

    // =============================================================
    //  LOAD DASHBOARD STATS (with ACTIVE reservations only)
    // =============================================================
    private void loadDashboardStats() {
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            String today = java.time.LocalDate.now().toString();

            // 1. Today's check-ins
            ResultSet rs1 = stmt.executeQuery(
                "SELECT COUNT(*) FROM reservations WHERE DATE(check_in_date) = DATE('" + today + "')");
            if (rs1.next()) this.todaysCheckIns = rs1.getInt(1);

            // 2. Today's check-outs
            ResultSet rs2 = stmt.executeQuery(
                "SELECT COUNT(*) FROM reservations WHERE DATE(check_out_date) = DATE('" + today + "')");
            if (rs2.next()) this.todaysCheckOuts = rs2.getInt(1);

            // 3. Available rooms
            ResultSet rs3 = stmt.executeQuery(
                "SELECT COUNT(*) FROM rooms WHERE status = 'Available'");
            if (rs3.next()) this.availableRooms = rs3.getInt(1);

            // 4. Occupancy rate
            ResultSet rs4 = stmt.executeQuery(
                "SELECT (COUNT(CASE WHEN status = 'Occupied' THEN 1 END) * 100.0 / COUNT(*)) FROM rooms");
            if (rs4.next()) this.occupancyRate = rs4.getDouble(1);

            // 5. Recent ACTIVE reservations (only Confirmed or Checked-In)
            //    Ordered by most recent, limit 10
            ResultSet rs5 = stmt.executeQuery(
                "SELECT r.reservation_id, " +
                "       CONCAT(g.first_name, ' ', g.last_name) AS guest_name, " +
                "       rm.room_number, " +
                "       rm.room_type, " +
                "       r.reservation_status, " +
                "       r.check_in_date " +
                "FROM reservations r " +
                "JOIN guests g ON r.guest_id = g.guest_id " +
                "JOIN rooms rm ON r.room_id = rm.room_id " +
                "WHERE r.reservation_status IN ('Confirmed', 'Checked-In') " +
                "ORDER BY r.reservation_id DESC LIMIT 10");

            List<Object[]> list = new ArrayList<>();
            while (rs5.next()) {
                list.add(new Object[]{
                    rs5.getInt("reservation_id"),
                    rs5.getString("guest_name"),
                    rs5.getString("room_number"),
                    rs5.getString("room_type"),
                    rs5.getString("reservation_status"),
                    rs5.getDate("check_in_date")
                });
            }
            this.reservationData = list.toArray(new Object[0][]);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Failed to load dashboard statistics.\n" + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =============================================================
    //  UI SETUP (unchanged – kept as you had it)
    // =============================================================
    private void functionMenu() {
        lblReception = new JLabel ("DASHBOARD");
        lblReception.setBounds(330, 60, 800, 60);
        lblReception.setFont(new Font ("Arial Black", Font.BOLD, 50));
        add(lblReception);
    }

    private void sidePanel() {
        sidePan = new JPanel ();
        sidePan.setBounds (0, 0, 300, 800);
        sidePan.setLayout(null);
        sidePan.setBackground(Color.decode("#222222"));
        add(sidePan);
        
        lblHotel = new JLabel ("HOTEL");
        lblHotel.setBounds(10, 10, 800, 50);
        lblHotel.setFont(new Font ("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidePan.add(lblHotel);
        
        lblManagement = new JLabel ("MANAGEMENT");
        lblManagement.setBounds(10, 50, 800, 50);
        lblManagement.setFont(new Font ("Arial Black", Font.BOLD, 30));
        lblManagement.setForeground(Color.WHITE);
        sidePan.add(lblManagement);
        
        btnHomePage = new JButton ("Home Page");
        btnHomePage.setBounds(0, 160, 300, 50);
        btnHomePage.setBackground(Color.decode("#222222"));
        btnHomePage.setForeground(Color.WHITE);
        btnHomePage.setFont(new Font ("Arial Black", Font.BOLD, 18));
        btnHomePage.setBorderPainted(false);
        btnHomePage.setFocusPainted(false);
        btnHomePage.setBorder(null);
        btnHomePage.addActionListener(this);
        sidePan.add(btnHomePage);
        
        btnSideDash = new JButton ("Dashboard");
        btnSideDash.setBounds(0, 230, 300, 50);
        btnSideDash.setBackground(Color.decode("#FFFFFF"));
        btnSideDash.setForeground(Color.BLACK);
        btnSideDash.setFont(new Font ("Arial Black", Font.BOLD, 18));
        btnSideDash.setBorderPainted(false);
        btnSideDash.setFocusPainted(false);
        btnSideDash.setBorder(null);
        btnSideDash.addActionListener(this);
        sidePan.add(btnSideDash);
        
        btnSideRegister = new JButton ("Guest Register");
        btnSideRegister.setBounds(0, 300, 300, 50);
        btnSideRegister.setBackground(Color.decode("#222222"));
        btnSideRegister.setForeground(Color.WHITE);
        btnSideRegister.setFont(new Font ("Arial Black", Font.BOLD, 18));
        btnSideRegister.setBorderPainted(false);
        btnSideRegister.setFocusPainted(false);
        btnSideRegister.setBorder(null);
        btnSideRegister.addActionListener(this);
        sidePan.add(btnSideRegister);
        
        btnSideRoomBooking = new JButton ("Room Booking");
        btnSideRoomBooking.setBounds(0, 370, 300, 50);
        btnSideRoomBooking.setBackground(Color.decode("#222222"));
        btnSideRoomBooking.setForeground(Color.WHITE);
        btnSideRoomBooking.setFont(new Font ("Arial Black", Font.BOLD, 18));
        btnSideRoomBooking.setBorderPainted(false);
        btnSideRoomBooking.setFocusPainted(false);
        btnSideRoomBooking.setBorder(null);
        btnSideRoomBooking.addActionListener(this);
        sidePan.add(btnSideRoomBooking);
        
        btnSidePay = new JButton ("Payment");
        btnSidePay.setBounds(0, 440, 300, 50);
        btnSidePay.setBackground(Color.decode("#222222"));
        btnSidePay.setForeground(Color.WHITE);
        btnSidePay.setFont(new Font ("Arial Black", Font.BOLD, 18));
        btnSidePay.setBorderPainted(false);
        btnSidePay.setFocusPainted(false);
        btnSidePay.setBorder(null);
        btnSidePay.addActionListener(this);
        sidePan.add(btnSidePay);
        
        btnSideReserve = new JButton ("Reservation");
        btnSideReserve.setBounds(0, 510, 300, 50);;
        btnSideReserve.setBackground(Color.decode("#222222"));
        btnSideReserve.setForeground(Color.WHITE);
        btnSideReserve.setFont(new Font ("Arial Black", Font.BOLD, 18));
        btnSideReserve.setBorderPainted(false);
        btnSideReserve.setFocusPainted(false);
        btnSideReserve.setBorder(null);
        btnSideReserve.addActionListener(this);
        sidePan.add(btnSideReserve);

        btnSideCancel = new JButton ("Cancel Booking");
        btnSideCancel.setBounds(0, 580, 300, 50);
        btnSideCancel.setBackground(Color.decode("#222222"));
        btnSideCancel.setForeground(Color.WHITE);
        btnSideCancel.setFont(new Font ("Arial Black", Font.BOLD, 18));
        btnSideCancel.setBorderPainted(false);
        btnSideCancel.setFocusPainted(false);
        btnSideCancel.setBorder(null);
        btnSideCancel.addActionListener(this);
        sidePan.add(btnSideCancel);
    }

    private void topPanel() {
        topPan = new JPanel ();
        topPan.setBounds (300, 0, 900, 60);
        topPan.setLayout(null);
        topPan.setBackground(Color.decode("#FFFFFF"));
        add(topPan);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        lblDate = new JLabel(dateFormat.format(new Date()));
        lblDate.setBounds(20, 15, 300, 30);
        lblDate.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblDate.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblDate);
    }

    private void dashboardPan() {
        dashboardPanel = new JPanel();
        dashboardPanel.setBounds(300, 130, 900, 520);
        dashboardPanel.setLayout(null);
        dashboardPanel.setBackground(Color.decode("#F5F5F5"));
        add(dashboardPanel);

        card1 = new JPanel();
        card1.setBounds(20, 20, 205, 90);
        card1.setBackground(Color.decode("#2ECC71"));
        card1.setLayout(null);
        dashboardPanel.add(card1);

        JLabel lblCheckInTitle = new JLabel("Today's Check-ins");
        lblCheckInTitle.setBounds(10, 10, 185, 20);
        lblCheckInTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblCheckInTitle.setForeground(Color.WHITE);
        card1.add(lblCheckInTitle);

        lblCheckInCount = new JLabel(String.valueOf(todaysCheckIns));
        lblCheckInCount.setBounds(10, 30, 185, 40);
        lblCheckInCount.setFont(new Font("Arial Black", Font.BOLD, 28));
        lblCheckInCount.setForeground(Color.WHITE);
        card1.add(lblCheckInCount);

        card2 = new JPanel();
        card2.setBounds(240, 20, 205, 90);
        card2.setBackground(Color.decode("#E67E22"));
        card2.setLayout(null);
        dashboardPanel.add(card2);

        JLabel lblCheckOutTitle = new JLabel("Today's Check-outs");
        lblCheckOutTitle.setBounds(10, 10, 185, 20);
        lblCheckOutTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblCheckOutTitle.setForeground(Color.WHITE);
        card2.add(lblCheckOutTitle);

        lblCheckOutCount = new JLabel(String.valueOf(todaysCheckOuts));
        lblCheckOutCount.setBounds(10, 30, 185, 40);
        lblCheckOutCount.setFont(new Font("Arial Black", Font.BOLD, 28));
        lblCheckOutCount.setForeground(Color.WHITE);
        card2.add(lblCheckOutCount);

        card3 = new JPanel();
        card3.setBounds(460, 20, 205, 90);
        card3.setBackground(Color.decode("#3498DB"));
        card3.setLayout(null);
        dashboardPanel.add(card3);

        JLabel lblAvailTitle = new JLabel("Available Rooms");
        lblAvailTitle.setBounds(10, 10, 185, 20);
        lblAvailTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblAvailTitle.setForeground(Color.WHITE);
        card3.add(lblAvailTitle);

        lblAvailableRooms = new JLabel(String.valueOf(availableRooms));
        lblAvailableRooms.setBounds(10, 30, 185, 40);
        lblAvailableRooms.setFont(new Font("Arial Black", Font.BOLD, 28));
        lblAvailableRooms.setForeground(Color.WHITE);
        card3.add(lblAvailableRooms);

        card4 = new JPanel();
        card4.setBounds(680, 20, 200, 90);
        card4.setBackground(Color.decode("#8E44AD"));
        card4.setLayout(null);
        dashboardPanel.add(card4);

        JLabel lblOccupancyTitle = new JLabel("Occupancy Rate");
        lblOccupancyTitle.setBounds(10, 10, 180, 20);
        lblOccupancyTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblOccupancyTitle.setForeground(Color.WHITE);
        card4.add(lblOccupancyTitle);

        lblOccupancyRate = new JLabel(String.format("%.1f%%", occupancyRate));
        lblOccupancyRate.setBounds(10, 30, 180, 40);
        lblOccupancyRate.setFont(new Font("Arial Black", Font.BOLD, 28));
        lblOccupancyRate.setForeground(Color.WHITE);
        card4.add(lblOccupancyRate);

        recentPanel = new JPanel();
        recentPanel.setBounds(20, 130, 860, 330);
        recentPanel.setBackground(Color.WHITE);
        recentPanel.setLayout(null);
        recentPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#DDDDDD")));
        dashboardPanel.add(recentPanel);

        lblRecent = new JLabel("Active Reservations (Recent)");
        lblRecent.setBounds(10, 5, 400, 30);
        lblRecent.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblRecent.setForeground(Color.decode("#333333"));
        recentPanel.add(lblRecent);

        String[] columns = {"Res ID", "Guest Name", "Room #", "Room Type", "Status", "Check-In"};
        reservationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReservations = new JTable(reservationTableModel);
        tblReservations.setRowHeight(26);
        tblReservations.setFont(new Font("Arial", Font.PLAIN, 13));
        tblReservations.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 13));
        tblReservations.getTableHeader().setBackground(Color.decode("#222222"));
        tblReservations.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblReservations);
        scrollPane.setBounds(10, 40, 840, 240);
        recentPanel.add(scrollPane);

        JLabel lblTotalLabel = new JLabel("Total Active:");
        lblTotalLabel.setBounds(10, 290, 150, 25);
        lblTotalLabel.setFont(new Font("Arial", Font.BOLD, 13));
        recentPanel.add(lblTotalLabel);

        lblTotalReservations = new JLabel(String.valueOf(reservationData.length));
        lblTotalReservations.setBounds(160, 290, 100, 25);
        lblTotalReservations.setFont(new Font("Arial Black", Font.BOLD, 13));
        lblTotalReservations.setForeground(Color.decode("#5A3FB8"));
        recentPanel.add(lblTotalReservations);

        btnRefresh = new JButton("Refresh Data");
        btnRefresh.setBounds(740, 287, 110, 30);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(Color.decode("#222222"));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(this);
        recentPanel.add(btnRefresh);
    }

    public void setStatistics(int checkIns, int checkOuts, int availableRooms, double occupancyRate) {
        this.todaysCheckIns = checkIns;
        this.todaysCheckOuts = checkOuts;
        this.availableRooms = availableRooms;
        this.occupancyRate = occupancyRate;
        refreshDisplay();
    }

    public void setReservationData(Object[][] data) {
        this.reservationData = data;
        refreshDisplay();
    }

    private void refreshDisplay() {
        lblCheckInCount.setText(String.valueOf(todaysCheckIns));
        lblCheckOutCount.setText(String.valueOf(todaysCheckOuts));
        lblAvailableRooms.setText(String.valueOf(availableRooms));
        lblOccupancyRate.setText(String.format("%.1f%%", occupancyRate));

        reservationTableModel.setRowCount(0);
        for (Object[] row : reservationData) {
            reservationTableModel.addRow(row);
        }
        lblTotalReservations.setText(String.valueOf(reservationData.length));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHomePage) {
            dispose();
            new HomePage().setVisible(true);
        } else if (e.getSource() == btnSideRegister) {
            dispose();
            new RegisterGuestPanel().setVisible(true);
        } else if (e.getSource() == btnSideRoomBooking) {
            dispose();
            new RoomBookingPanel().setVisible(true);
        } else if (e.getSource() == btnSidePay) {
            dispose();
            new RecordPaymentPanel().setVisible(true);
        } else if (e.getSource() == btnSideReserve) {
            dispose();
            new ViewReservationPanel().setVisible(true);
        } else if (e.getSource() == btnSideCancel) {
            dispose();
            new CancelBookingPanel().setVisible(true);
        } else if (e.getSource() == btnRefresh) {
            loadDashboardStats();  // reload from DB
            refreshDisplay();
        }
    }
}