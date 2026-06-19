package GUIAdmin;

import UI.StyledButton;
import Utilities.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboard extends JFrame implements ActionListener {
    
    private JTabbedPane tabbedPane;
    private RoomService roomService;
    private ReservationService reservationService;
    private PaymentService paymentService;
    
    public AdminDashboard() {
        this.roomService = new RoomService();
        this.reservationService = new ReservationService();
        this.paymentService = new PaymentService();
        
        initWindow();
        createComponents();
        Logger.getInstance().info("AdminDashboardMain opened");
    }
    
    private int countRoomsByStatus(String status) {
        int count = 0;
        for (String[] room : roomService.findAllRooms()) {
            if (room[4].equals(status)) {
                count++;
            }
        }
        return count;
    }
    
    private void initWindow() {
        setTitle("Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
    
    private void createComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(45, 85, 130));
        
        JLabel lblTitle = new JLabel("ADMIN DASHBOARD");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.add(lblTitle, BorderLayout.WEST);
        
        add(topPanel, BorderLayout.NORTH);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("System Statistics", createStatisticsPanel());
        tabbedPane.addTab("Financial Reports", createFinancialPanel());
        tabbedPane.addTab("System Settings", createSettingsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(createStatCard("Total Rooms", String.valueOf(roomService.findAllRooms().size())));
        panel.add(createStatCard("Available Rooms", String.valueOf(countRoomsByStatus("AVAILABLE"))));
        panel.add(createStatCard("Active Reservations", String.valueOf(reservationService.countActiveReservations())));
        panel.add(createStatCard("Total Revenue", "PHP " + String.format("%.2f", paymentService.totalRevenue())));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(240, 240, 240));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblValue.setForeground(new Color(70, 130, 180));
        lblValue.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier", Font.PLAIN, 12));
        
        JButton btnRefresh = new StyledButton("Refresh Stats", "info");
        btnRefresh.addActionListener(e -> {
            int totalRooms = roomService.findAllRooms().size();
            int availableRooms = countRoomsByStatus("AVAILABLE");
            int occupiedRooms = countRoomsByStatus("OCCUPIED");
            double occupancyRate = totalRooms == 0 ? 0.0 : (occupiedRooms / (double) totalRooms) * 100;
            
            StringBuilder stats = new StringBuilder();
            stats.append("===== SYSTEM STATISTICS =====\n\n");
            
            stats.append("ROOMS:\n");
            stats.append("  Total Rooms: ").append(totalRooms).append("\n");
            stats.append("  Available: ").append(availableRooms).append("\n");
            stats.append("  Occupied: ").append(occupiedRooms).append("\n");
            stats.append("  Occupancy Rate: ").append(String.format("%.2f", occupancyRate)).append("%\n\n");
            
            stats.append("RESERVATIONS:\n");
            stats.append("  Total Reservations: ").append(reservationService.countAllReservations()).append("\n");
            stats.append("  Active: ").append(reservationService.countActiveReservations()).append("\n\n");
            
            stats.append("PAYMENTS:\n");
            stats.append("  Total Transactions: ").append(paymentService.findAllPayments().size()).append("\n");
            stats.append("  Total Revenue: PHP ").append(String.format("%.2f", paymentService.totalRevenue())).append("\n");
            
            textArea.setText(stats.toString());
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRefresh);
        
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createFinancialPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier", Font.PLAIN, 12));
        
        JButton btnRefresh = new StyledButton("Generate Report", "success");
        btnRefresh.addActionListener(e -> {
            List<String[]> allPayments = paymentService.findAllPayments();
            
            StringBuilder report = new StringBuilder();
            report.append("===== FINANCIAL REPORT =====\n\n");
            report.append("Total Revenue: PHP ").append(String.format("%.2f", paymentService.totalRevenue())).append("\n");
            report.append("Total Transactions: ").append(allPayments.size()).append("\n\n");
            report.append("Recent Payments:\n");
            
            int start = Math.max(0, allPayments.size() - 5);
            if (allPayments.isEmpty()) {
                report.append("  No payments recorded yet.\n");
            } else {
                for (int i = start; i < allPayments.size(); i++) {
                    String[] p = allPayments.get(i);
                    report.append("  ").append(p[0]).append(" | Room ").append(p[1])
                          .append(" | PHP ").append(p[2]).append(" | ").append(p[3]).append("\n");
                }
            }
            
            textArea.setText(report.toString());
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRefresh);
        
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        
        JLabel lblSettings = new JLabel("System Settings & Configuration");
        lblSettings.setFont(new Font("Arial", Font.BOLD, 14));
        lblSettings.setBounds(30, 30, 300, 25);
        panel.add(lblSettings);
        
        JLabel lblBackup = new JLabel("Database Backup:");
        lblBackup.setBounds(30, 80, 120, 25);
        JButton btnBackup = new StyledButton("Backup Now", "info");
        btnBackup.setBounds(160, 80, 150, 30);
        btnBackup.addActionListener(e -> Logger.getInstance().info("Database backup initiated"));
        panel.add(lblBackup);
        panel.add(btnBackup);
        
        JLabel lblLogs = new JLabel("View System Logs:");
        lblLogs.setBounds(30, 130, 120, 25);
        JButton btnLogs = new StyledButton("View Logs", "info");
        btnLogs.setBounds(160, 130, 150, 30);
        btnLogs.addActionListener(e -> Logger.getInstance().info("System logs requested"));
        panel.add(lblLogs);
        panel.add(btnLogs);
        
        return panel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}