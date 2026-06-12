package GUIReceptionist;

import DAO.ReservationDAO;
import DAO.RoomDAO;
import DAO.GuestDAO;
import Models.Reservation;
import Models.Room;
import Models.Guest;
import Services.DashboardService;
import Utilities.Constants;
import Utilities.HotelException;
import Utilities.MessageBox;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * ReceptionistMainPanel - Main Receptionist Dashboard
 * 
 * Displays key hotel metrics and today's reservations.
 * Shows today's check-ins, check-outs, available rooms, and recent reservations.
 * Provides quick overview of hotel status for receptionists.
 * 
 * Features:
 * - Real-time hotel status metrics
 * - Today's check-ins and check-outs
 * - Available rooms count
 * - Occupancy rate
 * - Table of today's reservations with details
 * - Refresh data functionality
 * 
 * @author Hotel Reservation System Team
 * @version 2.0.0
 */
public class ReceptionistMainPanel extends JPanel {
    
    private DashboardService dashboardService;
    private ReservationDAO reservationDAO;
    private RoomDAO roomDAO;
    private GuestDAO guestDAO;
    
    // Metric labels for dashboard cards
    private JLabel lblCheckInsCount;
    private JLabel lblCheckOutsCount;
    private JLabel lblAvailableRooms;
    private JLabel lblOccupancyRate;
    
    // Table for today's reservations
    private JTable todayReservationsTable;
    private DefaultTableModel tableModel;
    
    // Refresh button
    private JButton btnRefresh;
    
    /**
     * Constructor - Initializes the receptionist dashboard
     */
    public ReceptionistMainPanel() {
        this.dashboardService = new DashboardService();
        this.reservationDAO = new ReservationDAO();
        this.roomDAO = new RoomDAO();
        this.guestDAO = new GuestDAO();
        
        initUI();
        refreshData();
    }
    
    /**
     * Initialize UI components
     * Sets up the layout and creates all dashboard panels
     */
    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top section: Metrics/Cards
        JPanel metricsPanel = createMetricsPanel();
        add(metricsPanel, BorderLayout.NORTH);
        
        // Middle section: Reservations table
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Bottom section: Refresh button
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create metrics panel with hotel status cards
     * Displays 4 key metrics: check-ins, check-outs, available rooms, occupancy rate
     * 
     * @return JPanel containing metrics cards
     */
    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Hotel Status Overview",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        // Card 1: Today's Check-ins
        JPanel checkInPanel = createMetricCard(
            "Today's Check-ins",
            "0",
            new Color(76, 175, 80),  // Green
            "check-ins scheduled for today"
        );
        lblCheckInsCount = (JLabel) ((JPanel) checkInPanel.getComponent(1)).getComponent(0);
        panel.add(checkInPanel);
        
        // Card 2: Today's Check-outs
        JPanel checkOutPanel = createMetricCard(
            "Today's Check-outs",
            "0",
            new Color(255, 152, 0),  // Orange
            "check-outs scheduled for today"
        );
        lblCheckOutsCount = (JLabel) ((JPanel) checkOutPanel.getComponent(1)).getComponent(0);
        panel.add(checkOutPanel);
        
        // Card 3: Available Rooms
        JPanel availablePanel = createMetricCard(
            "Available Rooms",
            "0",
            new Color(33, 150, 243),  // Blue
            "rooms ready for guests"
        );
        lblAvailableRooms = (JLabel) ((JPanel) availablePanel.getComponent(1)).getComponent(0);
        panel.add(availablePanel);
        
        // Card 4: Occupancy Rate
        JPanel occupancyPanel = createMetricCard(
            "Occupancy Rate",
            "0%",
            new Color(156, 39, 176),  // Purple
            "current occupancy percentage"
        );
        lblOccupancyRate = (JLabel) ((JPanel) occupancyPanel.getComponent(1)).getComponent(0);
        panel.add(occupancyPanel);
        
        return panel;
    }
    
    /**
     * Create a single metric card for the dashboard
     * Each card shows a title, value, and description
     * 
     * @param title The title of the metric
     * @param value The current value to display
     * @param backgroundColor The background color of the card
     * @param description Short description of the metric
     * @return JPanel representing the metric card
     */
    private JPanel createMetricCard(String title, String value, Color backgroundColor, String description) {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 5));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 11));
        lblTitle.setForeground(Color.WHITE);
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Value container
        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setBackground(backgroundColor);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 32));
        lblValue.setForeground(Color.WHITE);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        valuePanel.add(lblValue, BorderLayout.CENTER);
        
        mainPanel.add(valuePanel, BorderLayout.CENTER);
        
        // Description
        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 9));
        lblDesc.setForeground(new Color(255, 255, 255, 200));
        mainPanel.add(lblDesc, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Create table panel displaying today's reservations
     * Shows all reservations with check-in or check-out today
     * 
     * @return JPanel containing the reservations table
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Today's Reservations",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        // Create table model with columns
        tableModel = new DefaultTableModel(
            new String[]{
                "Res ID",
                "Guest Name",
                "Room #",
                "Room Type",
                "Status",
                "Check-in",
                "Check-out"
            },
            0
        ) {
            /**
             * Make table non-editable
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table
        todayReservationsTable = new JTable(tableModel);
        todayReservationsTable.setRowHeight(25);
        todayReservationsTable.setFont(new Font("Arial", Font.PLAIN, 11));
        todayReservationsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        todayReservationsTable.getTableHeader().setBackground(new Color(33, 150, 243));
        todayReservationsTable.getTableHeader().setForeground(Color.WHITE);
        
        // Set column widths
        todayReservationsTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        todayReservationsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        todayReservationsTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        todayReservationsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        todayReservationsTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        todayReservationsTable.getColumnModel().getColumn(5).setPreferredWidth(90);
        todayReservationsTable.getColumnModel().getColumn(6).setPreferredWidth(90);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(todayReservationsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Status bar
        JLabel lblRecordCount = new JLabel("Total reservations: 0");
        lblRecordCount.setFont(new Font("Arial", Font.PLAIN, 10));
        lblRecordCount.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Update record count when data is refreshed
        panel.add(lblRecordCount, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Create action panel with refresh button
     * 
     * @return JPanel containing action buttons
     */
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(245, 245, 245));
        
        btnRefresh = new JButton("Refresh Data");
        btnRefresh.setBackground(new Color(25, 118, 210));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 11));
        btnRefresh.setPreferredSize(new Dimension(120, 35));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> refreshData());
        
        panel.add(btnRefresh);
        
        return panel;
    }
    
    /**
     * Refresh all dashboard data
     * Fetches current metrics and updates the display
     */
    private void refreshData() {
        try {
            // Get metrics
            List<Reservation> todayCheckIns = dashboardService.getTodayCheckIns();
            List<Reservation> todayCheckOuts = dashboardService.getTodayCheckOuts();
            int availableCount = dashboardService.getAvailableRoomsCount();
            int totalCount = dashboardService.getTotalRoomsCount();
            double occupancyRate = dashboardService.getOccupancyRate();
            
            // Update metric labels
            lblCheckInsCount.setText(String.valueOf(todayCheckIns.size()));
            lblCheckOutsCount.setText(String.valueOf(todayCheckOuts.size()));
            lblAvailableRooms.setText(String.valueOf(availableCount));
            lblOccupancyRate.setText(String.format("%.1f%%", occupancyRate));
            
            // Clear and repopulate table
            tableModel.setRowCount(0);
            
            // Add check-ins
            for (Reservation res : todayCheckIns) {
                addReservationToTable(res);
            }
            
            // Add check-outs
            for (Reservation res : todayCheckOuts) {
                addReservationToTable(res);
            }
            
        } catch (HotelException e) {
            MessageBox.showError(
                "Error",
                "Failed to refresh dashboard data: " + e.getMessage()
            );
        }
    }
    
    /**
     * Add a reservation row to the table
     * Fetches guest and room information and formats the display
     * 
     * @param reservation The reservation to add to the table
     */
    private void addReservationToTable(Reservation reservation) {
        try {
            // Get guest name
            Guest guest = guestDAO.getGuestById(reservation.getGuestId());
            String guestName = guest != null ? 
                guest.getFirstName() + " " + guest.getLastName() : 
                "Unknown";
            
            // Get room information
            Room room = roomDAO.getRoomById(reservation.getRoomId());
            String roomNumber = room != null ? room.getRoomNumber() : "Unknown";
            String roomType = room != null ? room.getRoomType() : "Unknown";
            
            // Format dates
            String checkInDate = reservation.getCheckInDate().toString();
            String checkOutDate = reservation.getCheckOutDate().toString();
            
            // Add row to table
            tableModel.addRow(new Object[]{
                reservation.getReservationId(),
                guestName,
                roomNumber,
                roomType,
                reservation.getReservationStatus(),
                checkInDate,
                checkOutDate
            });
            
        } catch (HotelException e) {
            // Log error but continue adding other rows
            System.err.println("Error adding reservation to table: " + e.getMessage());
        }
    }
}
