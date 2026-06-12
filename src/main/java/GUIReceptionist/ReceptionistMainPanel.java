package GUIReceptionist;

import DAO.ReservationDAO;
import DAO.RoomDAO;
import DAO.PaymentDAO;
import Models.Reservation;
import Services.DashboardService;
import Utilities.CurrencyUtil;
import Utilities.MessageBox;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * ReceptionistMainPanel - Main receptionist dashboard with key metrics
 * 
 * Displays today's check-ins, check-outs, available rooms, and recent reservations.
 * Provides quick overview of hotel status.
 */
public class ReceptionistMainPanel extends JPanel {
    
    private DashboardService dashboardService;
    private ReservationDAO reservationDAO;
    private RoomDAO roomDAO;
    private JLabel lblCheckInsCount, lblCheckOutsCount, lblAvailableRooms, lblTotalRooms;
    private JTable todayReservationsTable;
    private DefaultTableModel tableModel;
    
    public ReceptionistMainPanel() {
        this.dashboardService = new DashboardService();
        this.reservationDAO = new ReservationDAO();
        this.roomDAO = new RoomDAO();
        initUI();
        refreshData();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel metricsPanel = createMetricsPanel();
        add(metricsPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel refreshPanel = createRefreshPanel();
        add(refreshPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Hotel Status"));
        
        // Check-ins today
        JPanel checkInPanel = createMetricCard("Today's Check-ins", "0", new Color(76, 175, 80));
        lblCheckInsCount = (JLabel) checkInPanel.getComponent(1);
        panel.add(checkInPanel);
        
        // Check-outs today
        JPanel checkOutPanel = createMetricCard("Today's Check-outs", "0", new Color(255, 152, 0));
        lblCheckOutsCount = (JLabel) checkOutPanel.getComponent(1);
        panel.add(checkOutPanel);
        
        // Available rooms
        JPanel availablePanel = createMetricCard("Available Rooms", "0", new Color(33, 150, 243));
        lblAvailableRooms = (JLabel) availablePanel.getComponent(1);
        panel.add(availablePanel);
        
        // Total rooms
        JPanel totalPanel = createMetricCard("Total Rooms", "0", new Color(156, 39, 176));
        lblTotalRooms = (JLabel) totalPanel.getComponent(1);
        panel.add(totalPanel);
        
        return panel;
    }
    
    private JPanel createMetricCard(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTitle.setForeground(Color.WHITE);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 28));
        lblValue.setForeground(Color.WHITE);
        panel.add(lblValue, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Today's Reservations"));
        
        tableModel = new DefaultTableModel(
            new String[]{"Reservation ID", "Guest", "Room #", "Type", "Status", "Check-in", "Check-out"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        todayReservationsTable = new JTable(tableModel);
        todayReservationsTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(todayReservationsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRefreshPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setBackground(new Color(25, 118, 210));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> refreshData());
        panel.add(btnRefresh);
        
        return panel;
    }
    
    private void refreshData() {
        try {
            int checkIns = dashboardService.getTodayCheckIns().size();
            int checkOuts = dashboardService.getTodayCheckOuts().size();
            int available = dashboardService.getAvailableRoomsCount();
            int total = dashboardService.getTotalRoomsCount();
            
            lblCheckInsCount.setText(String.valueOf(checkIns));
            lblCheckOutsCount.setText(String.valueOf(checkOuts));
            lblAvailableRooms.setText(String.valueOf(available));
            lblTotalRooms.setText(String.valueOf(total));
            
            // Load today's reservations
            List<Reservation> todayReservations = dashboardService.getTodayCheckIns();
            todayReservations.addAll(dashboardService.getTodayCheckOuts());
            
            tableModel.setRowCount(0);
            for (Reservation res : todayReservations) {
                tableModel.addRow(new Object[]{
                    res.getReservationId(),
                    res.getGuestId(),
                    res.getRoomId(),
                    "Room Type",
                    res.getReservationStatus(),
                    res.getCheckInDate().toString(),
                    res.getCheckOutDate().toString()
                });
            }
            
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to refresh data: " + e.getMessage());
        }
    }
}
