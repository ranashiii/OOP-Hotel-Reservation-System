package GUIGuest;

import DAO.GuestDAO;
import DAO.ReservationDAO;
import Models.Guest;
import Models.Reservation;
import HotelReservationMainSystem.SessionManager;
import Utilities.Constants;
import Utilities.CurrencyUtil;
import Utilities.MessageBox;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewReservationsPanel - Display guest's reservation history
 * 
 * Shows all current and past reservations with details and status.
 * Allows filtering by status and viewing reservation details.
 */
public class ViewReservationsPanel extends JPanel {
    
    private ReservationDAO reservationDAO;
    private GuestDAO guestDAO;
    private JTable reservationsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbStatus;
    private JButton btnRefresh, btnViewDetails, btnCancel;
    
    public ViewReservationsPanel() {
        this.reservationDAO = new ReservationDAO();
        this.guestDAO = new GuestDAO();
        initUI();
        loadReservations();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        
        panel.add(new JLabel("Filter by Status:"));
        cmbStatus = new JComboBox<>(new String[]{"All", Constants.RESERVATION_STATUS_CONFIRMED, 
            Constants.RESERVATION_STATUS_CHECKED_IN, Constants.RESERVATION_STATUS_CHECKED_OUT, 
            Constants.RESERVATION_STATUS_CANCELLED});
        cmbStatus.addActionListener(e -> loadReservations());
        panel.add(cmbStatus);
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(25, 118, 210));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> loadReservations());
        panel.add(btnRefresh);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        tableModel = new DefaultTableModel(
            new String[]{"Reservation ID", "Room #", "Check-in", "Check-out", "Status", "Total Price"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reservationsTable = new JTable(tableModel);
        reservationsTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnViewDetails = new JButton("View Details");
        btnViewDetails.setBackground(new Color(25, 118, 210));
        btnViewDetails.setForeground(Color.WHITE);
        btnViewDetails.addActionListener(e -> viewDetails());
        panel.add(btnViewDetails);
        
        btnCancel = new JButton("Cancel Reservation");
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> cancelReservation());
        panel.add(btnCancel);
        
        return panel;
    }
    
    private void loadReservations() {
        try {
            int userId = SessionManager.getInstance().getCurrentUserId();
            Guest guest = guestDAO.getGuestByUserId(userId);
            
            if (guest == null) {
                MessageBox.showError("Error", "Guest profile not found");
                return;
            }
            
            List<Reservation> reservations = reservationDAO.getReservationsByGuestId(guest.getGuestId());
            
            tableModel.setRowCount(0);
            
            String selectedStatus = (String) cmbStatus.getSelectedItem();
            
            for (Reservation res : reservations) {
                if (!selectedStatus.equals("All") && !res.getReservationStatus().equals(selectedStatus)) {
                    continue;
                }
                
                tableModel.addRow(new Object[]{
                    res.getReservationId(),
                    res.getRoomId(),
                    res.getCheckInDate().toString(),
                    res.getCheckOutDate().toString(),
                    res.getReservationStatus(),
                    CurrencyUtil.formatCurrency(res.getFinalTotal() != null ? res.getFinalTotal() : res.getTotalPrice())
                });
            }
            
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to load reservations: " + e.getMessage());
        }
    }
    
    private void viewDetails() {
        if (reservationsTable.getSelectedRow() < 0) {
            MessageBox.showError("Error", "Please select a reservation to view");
            return;
        }
        
        int reservationId = (Integer) tableModel.getValueAt(reservationsTable.getSelectedRow(), 0);
        MessageBox.showInfo("Details", "Reservation ID: " + reservationId + "\n(View full details functionality to be implemented)");
    }
    
    private void cancelReservation() {
        if (reservationsTable.getSelectedRow() < 0) {
            MessageBox.showError("Error", "Please select a reservation to cancel");
            return;
        }
        
        int reservationId = (Integer) tableModel.getValueAt(reservationsTable.getSelectedRow(), 0);
        
        int confirm = MessageBox.showConfirm("Confirm Cancellation", 
            "Are you sure you want to cancel this reservation?\nYou may be eligible for a refund based on our policy.");
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                reservationDAO.cancelReservation(reservationId, "Guest initiated cancellation");
                MessageBox.showInfo("Success", "Reservation cancelled successfully");
                loadReservations();
            } catch (Exception e) {
                MessageBox.showError("Error", "Failed to cancel reservation: " + e.getMessage());
            }
        }
    }
}