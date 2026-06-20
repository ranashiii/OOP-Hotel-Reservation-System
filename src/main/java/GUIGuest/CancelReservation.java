package GUIGuest;

import Models.Reservation;
import Models.Room;
import Services.ReservationService;
import Services.RoomService;
import Utilities.HotelException;
import HotelReservationMainSystem.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

public class CancelReservation extends JFrame implements ActionListener {

    private JComboBox<String> cbReservations;
    private JTextArea txtDetails;
    private JTextField txtRefund;
    private JButton btnCancel, btnRefresh;

    private ReservationService reservationService = new ReservationService();
    private RoomService roomService = new RoomService();

    public CancelReservation() {
        setTitle("Cancel Reservation");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblSelect = new JLabel("Select Reservation:");
        lblSelect.setBounds(20, 20, 150, 25);
        add(lblSelect);

        cbReservations = new JComboBox<>();
        cbReservations.setBounds(170, 20, 380, 30);
        add(cbReservations);
        cbReservations.addActionListener(e -> loadReservationDetails());

        txtDetails = new JTextArea();
        txtDetails.setEditable(false);
        txtDetails.setBounds(20, 60, 540, 150);
        txtDetails.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtDetails);

        JLabel lblRefund = new JLabel("Refund Estimate:");
        lblRefund.setBounds(20, 220, 150, 25);
        add(lblRefund);

        txtRefund = new JTextField();
        txtRefund.setBounds(170, 220, 200, 25);
        txtRefund.setEditable(false);
        add(txtRefund);

        JLabel lblReason = new JLabel("Reason (optional):");
        lblReason.setBounds(20, 255, 150, 25);
        add(lblReason);

        JTextField txtReason = new JTextField();
        txtReason.setBounds(170, 255, 380, 25);
        add(txtReason);

        btnCancel = new JButton("Cancel Reservation");
        btnCancel.setBounds(20, 300, 180, 30);
        btnCancel.setBackground(Color.RED);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> {
            String reason = txtReason.getText().trim();
            if (reason.isEmpty()) reason = "Guest requested cancellation";
            performCancel(reason);
        });
        add(btnCancel);

        btnRefresh = new JButton("Refresh List");
        btnRefresh.setBounds(220, 300, 150, 30);
        btnRefresh.addActionListener(e -> loadReservations());
        add(btnRefresh);

        // Load initial data
        loadReservations();
    }

    private void loadReservations() {
        cbReservations.removeAllItems();
        try {
            int guestId = SessionManager.getInstance().getGuestId();
            if (guestId == 0) {
                JOptionPane.showMessageDialog(this, "Guest not logged in.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Reservation> reservations = reservationService.getReservationsByGuestId(guestId);
            boolean hasActive = false;
            for (Reservation r : reservations) {
                String status = r.getReservationStatus();
                if (!status.equals("Cancelled") && !status.equals("Checked-Out")) {
                    cbReservations.addItem(r.getReservationId() + " - " + r.getCheckInDate() + " to " + r.getCheckOutDate() + " (" + status + ")");
                    hasActive = true;
                }
            }
            if (!hasActive) {
                cbReservations.addItem("No active reservations");
                btnCancel.setEnabled(false);
            } else {
                btnCancel.setEnabled(true);
                cbReservations.setSelectedIndex(0);
                loadReservationDetails();
            }
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadReservationDetails() {
        String selected = (String) cbReservations.getSelectedItem();
        if (selected == null || selected.startsWith("No active")) {
            txtDetails.setText("");
            txtRefund.setText("");
            return;
        }
        try {
            int id = Integer.parseInt(selected.split(" - ")[0]);
            Reservation r = reservationService.getReservationById(id);
            if (r != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Reservation ID: ").append(r.getReservationId()).append("\n");
                Room room = roomService.getRoomById(r.getRoomId());
                sb.append("Room: ").append(room != null ? room.getRoomNumber() : "N/A").append("\n");
                sb.append("Check-in: ").append(r.getCheckInDate()).append("\n");
                sb.append("Check-out: ").append(r.getCheckOutDate()).append("\n");
                sb.append("Status: ").append(r.getReservationStatus()).append("\n");
                sb.append("Total: PHP ").append(String.format("%,.2f", r.getFinalTotal()));
                txtDetails.setText(sb.toString());

                // Estimate refund (we'll calculate manually to avoid double cancellation)
                BigDecimal refund = estimateRefund(r);
                txtRefund.setText("PHP " + String.format("%,.2f", refund));
            }
        } catch (Exception ex) {
            txtDetails.setText("Error loading details.");
            ex.printStackTrace();
        }
    }

    private BigDecimal estimateRefund(Reservation r) {
        // Replicate the refund logic from ReservationService for display
        java.time.LocalDate today = java.time.LocalDate.now();
        long daysBefore = java.time.temporal.ChronoUnit.DAYS.between(today, r.getCheckInDate());
        if (daysBefore > 7) return r.getFinalTotal();
        else if (daysBefore == 7) return r.getFinalTotal().multiply(new BigDecimal("0.90"));
        else return BigDecimal.ZERO;
    }

    private void performCancel(String reason) {
        String selected = (String) cbReservations.getSelectedItem();
        if (selected == null || selected.startsWith("No active")) {
            JOptionPane.showMessageDialog(this, "No active reservation selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(selected.split(" - ")[0]);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this reservation?\nReason: " + reason,
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            BigDecimal refund = reservationService.cancelReservation(id, reason);
            JOptionPane.showMessageDialog(this,
                    "Reservation cancelled successfully.\nRefund amount: PHP " + String.format("%,.2f", refund),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            loadReservations(); // refresh list
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Cancellation failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Not used – we used lambda for buttons, but keep for interface
    }
}