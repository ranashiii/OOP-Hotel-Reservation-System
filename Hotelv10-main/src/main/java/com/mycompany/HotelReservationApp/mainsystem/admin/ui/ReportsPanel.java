package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class ReportsPanel extends JPanel {

    private static final Color DARK = Color.decode("#222222");

    static final List<String[]> payments = new ArrayList<>();

    private final AdminDashboard dashboard;
    private final RoomService roomService;
    private final UserService userService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    public ReportsPanel(AdminDashboard dashboard, RoomService rs, UserService us,ReservationService res, PaymentService ps) {
    this.dashboard = dashboard;
    this.roomService = rs;
    this.userService = us;
    this.reservationService = res;
    this.paymentService = ps;
    setLayout(null);
    setBackground(Color.decode("#F5F5F5"));
    buildUI();
    }

    private void buildUI() {
        String[] cardTitles = {
        "Total Rooms: " + roomService.findAllRooms().size(),
        "Available: " + countAvailable(),
        "Occupied: " + countOccupied(),
        "Active Reservations: " + reservationService.countActiveReservations(),
        "Total Reservations: " + reservationService.countAllReservations(),
        "Occupancy %: " + calcOccupancy()
        };
        
        int cardW = 270, cardH = 75, gap = 15, startX = 20;
        for (int i = 0; i < cardTitles.length; i++) {
        int col = i % 3;
        int row = i / 3;
        JPanel card = new JPanel(null);
        card.setBounds(startX + col * (cardW + gap), 10 + row * (cardH + 10), cardW, cardH);
        card.setBackground(DARK);
        JLabel lbl = new JLabel(cardTitles[i]);
        lbl.setBounds(10, 22, 250, 30);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 11));
        lbl.setForeground(Color.WHITE);
        card.add(lbl);
        add(card);
        }

        JPanel statsPanel = new JPanel(null);
        statsPanel.setBounds(20, 175, 400, 290);
        statsPanel.setBackground(DARK);
        add(statsPanel);

        JLabel lblStats = new JLabel("Statistics");
        lblStats.setBounds(15, 10, 350, 35);
        lblStats.setFont(new Font("Arial Black", Font.BOLD, 22));
        lblStats.setForeground(Color.WHITE);
        statsPanel.add(lblStats);

        String monthName = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String[] stats = {
        "Total rooms: " + roomService.findAllRooms().size(),
        "Available rooms: " + countAvailable(),
        "Occupied rooms: " + countOccupied(),
        "Total staff accounts: " + userService.findAllUsers().size(),
        "Total Reservations for the Month of " + monthName + ": " + countReservationsThisMonth(),
        "Total Revenue Collected for the Month of " + monthName + ": PHP "+ String.format("%.2f", revenueThisMonth())
        };
        
        for (int i = 0; i < stats.length; i++) {
        JLabel s = new JLabel(stats[i]);
        s.setBounds(15, 55 + i * 38, 370, 22);
        s.setFont(new Font("Arial Black", Font.PLAIN, 11));
        s.setForeground(Color.WHITE);
        statsPanel.add(s);
        }
        
        JPanel quickPanel = new JPanel(null);
        quickPanel.setBounds(440, 175, 410, 290);
        quickPanel.setBackground(DARK);
        add(quickPanel);

        JLabel lblQuick = new JLabel("Quick Actions");
        lblQuick.setBounds(10, 10, 380, 35);
        lblQuick.setFont(new Font("Arial Black", Font.BOLD, 22));
        lblQuick.setForeground(Color.WHITE);
        quickPanel.add(lblQuick);

        JButton btnQUser = new JButton("User Management");
        btnQUser.setBounds(0, 70, 410, 55);
        btnQUser.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnQUser.setBackground(DARK);
        btnQUser.setForeground(Color.WHITE);
        btnQUser.setBorderPainted(false);
        btnQUser.setFocusPainted(false);
        btnQUser.setBorder(null);
        btnQUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnQUser.addActionListener(e ->
        dashboard.switchTo(dashboard.btnUserManagement, "USER MANAGEMENT",
        new UserManagementPanel(dashboard, dashboard.userService)));
        quickPanel.add(btnQUser);

        JButton btnQRoom = new JButton("Room Management");
        btnQRoom.setBounds(0, 150, 410, 55);
        btnQRoom.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnQRoom.setBackground(DARK);
        btnQRoom.setForeground(Color.WHITE);
        btnQRoom.setBorderPainted(false);
        btnQRoom.setFocusPainted(false);
        btnQRoom.setBorder(null);
        btnQRoom.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnQRoom.addActionListener(e ->
        dashboard.switchTo(dashboard.btnRoomManagement, "ROOM MANAGEMENT",
        new RoomManagementPanel(dashboard, dashboard.roomService)));
        quickPanel.add(btnQRoom);

        JButton btnQRes = new JButton("Reservations");
        btnQRes.setBounds(0, 230, 410, 55);
        btnQRes.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnQRes.setBackground(DARK);
        btnQRes.setForeground(Color.WHITE);
        btnQRes.setBorderPainted(false);
        btnQRes.setFocusPainted(false);
        btnQRes.setBorder(null);
        btnQRes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnQRes.addActionListener(e ->
        dashboard.switchTo(dashboard.btnReservations, "RESERVATIONS",
        new ReservationsPanel(dashboard, dashboard.reservationService)));
        quickPanel.add(btnQRes);
        
        JPanel recentPayPanel = new JPanel(null);
        recentPayPanel.setBounds(20, 175, 410, 290);
        recentPayPanel.setBackground(DARK);
        add(recentPayPanel);

        JLabel lblRecentPay = new JLabel("Recent Payments");
        lblRecentPay.setBounds(10, 10, 390, 28);
        lblRecentPay.setFont(new Font("Arial Black", Font.BOLD, 15));
        lblRecentPay.setForeground(Color.WHITE);
        recentPayPanel.add(lblRecentPay);

        List<String[]> allPay = paymentService.findAllPayments();
        
        int payStart = Math.max(0, allPay.size() - 5);
        for (int i = payStart; i < allPay.size(); i++) {
        String[] p = allPay.get(i);
        JLabel entry = new JLabel((i - payStart + 1) + ". " + p[0] + " | Rm " + p[1] + " | PHP " + p[2]);
        entry.setBounds(10, 45 + (i - payStart) * 42, 390, 32);
        entry.setFont(new Font("Arial Black", Font.PLAIN, 11));
        entry.setForeground(Color.WHITE);
        recentPayPanel.add(entry);
        }
        
        if (allPay.isEmpty()) {
        JLabel empty = new JLabel("No payments recorded yet.");
        empty.setBounds(10, 45, 380, 25);
        empty.setFont(new Font("Arial Black", Font.PLAIN, 11));
        empty.setForeground(Color.LIGHT_GRAY);
        recentPayPanel.add(empty);
        }

        JButton btnRefresh = new JButton("UPDATE REPORTS");
        btnRefresh.setBounds(20, 478, 200, 25);
        btnRefresh.setFont(new Font("Arial Black", Font.BOLD, 13));
        btnRefresh.setBackground(DARK);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> {
            removeAll();
            buildUI();
            revalidate();
            repaint();
        JOptionPane.showMessageDialog(this,
        "Report data refreshed.", "Refresh",
        JOptionPane.INFORMATION_MESSAGE);
        });
        add(btnRefresh);
        }
    
        int countAvailable() {
        int c = 0;
        for (String[] r : roomService.findAllRooms())
            if (r[4].equals("AVAILABLE")) c++;
        return c;
        }

        int countOccupied() {
        int c = 0;
        for (String[] r : roomService.findAllRooms())
            if (r[4].equals("OCCUPIED")) c++;
        return c;
        }

        int countReservationsThisMonth() {
        int month = LocalDate.now().getMonthValue();
        int year  = LocalDate.now().getYear();
        int count = 0;
        for (String[] r : reservationService.findAllReservations()) {
        try {
            LocalDate checkIn = LocalDate.parse(r[2]);
            if (checkIn.getMonthValue() == month && checkIn.getYear() == year)
                count++;
        }
        catch (Exception ignored) {}
        }
            return count;
        }

        double revenueThisMonth() {
        int month = LocalDate.now().getMonthValue();
        int year  = LocalDate.now().getYear();
        double total = 0;
        for (String[] p : paymentService.findAllPayments()) {
            try {
                if (p.length >= 4) {
                    LocalDate date = LocalDate.parse(p[3]);
                if (date.getMonthValue() == month && date.getYear() == year)
                    total += Double.parseDouble(p[2]);
            } else {
                total += Double.parseDouble(p[2]);
            }
            } catch (Exception ignored) {}
        }
        return total;
        }

    int calcOccupancy() {
        int total = roomService.findAllRooms().size();
        if (total == 0) return 0;
        int occupied = 0;
        for (String[] r : roomService.findAllRooms())
            if (r[4].equals("OCCUPIED")) occupied++;
        return (int)((occupied / (double) total) * 100);
    }
}
