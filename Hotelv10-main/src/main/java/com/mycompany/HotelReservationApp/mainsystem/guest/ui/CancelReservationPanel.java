package com.mycompany.HotelReservationApp.mainsystem.guest.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * CancelReservationPanel - Cancel Existing Reservation
 * Handles reservation cancellation with refund calculation based on cancellation policy
 */
public class CancelReservationPanel extends JPanel implements ActionListener {
    
    private JTextField txtReservationId, txtGuestName;
    private JComboBox<String> cmbCancellationReason;
    private JButton btnSearch, btnCancelReservation, btnBack;
    private JLabel lblRefundAmount, lblCancellationPolicy, lblErrorMessage;
    private JTextArea txtReservationDetails;
    private boolean reservationFound = false;
    private LocalDate checkInDate = null;
    
    public CancelReservationPanel() {
        setLayout(null);
        setBackground(Color.decode("#F5F5F5"));
        createComponents();
    }
    
    private void createComponents() {
        // Title Bar
        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(20, 15, 920, 50);
        titleBar.setBackground(Color.decode("#222222"));
        add(titleBar);
        
        JLabel lblTitle = new JLabel("CANCEL RESERVATION");
        lblTitle.setBounds(15, 8, 600, 34);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);
        
        // Error Message
        lblErrorMessage = new JLabel();
        lblErrorMessage.setFont(new Font("Arial", Font.PLAIN, 11));
        lblErrorMessage.setForeground(new Color(244, 67, 54));
        lblErrorMessage.setBounds(20, 75, 920, 20);
        lblErrorMessage.setVisible(false);
        add(lblErrorMessage);
        
        // Search Panel
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(20, 100, 920, 80);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(searchPanel);
        
        JLabel lblResId = new JLabel("Reservation ID:");
        lblResId.setBounds(15, 15, 120, 20);
        lblResId.setFont(new Font("Arial", Font.BOLD, 11));
        searchPanel.add(lblResId);
        
        txtReservationId = new JTextField();
        txtReservationId.setBounds(140, 12, 150, 25);
        searchPanel.add(txtReservationId);
        
        JLabel lblGuest = new JLabel("Guest Name:");
        lblGuest.setBounds(320, 15, 100, 20);
        lblGuest.setFont(new Font("Arial", Font.BOLD, 11));
        searchPanel.add(lblGuest);
        
        txtGuestName = new JTextField();
        txtGuestName.setBounds(420, 12, 150, 25);
        searchPanel.add(txtGuestName);
        
        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(590, 12, 100, 25);
        btnSearch.setBackground(new Color(76, 175, 80));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 11));
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(this);
        searchPanel.add(btnSearch);
        
        JLabel lblReason = new JLabel("Cancellation Reason:");
        lblReason.setBounds(15, 45, 140, 20);
        lblReason.setFont(new Font("Arial", Font.BOLD, 11));
        searchPanel.add(lblReason);
        
        String[] reasons = {"- Select Reason -", "Change of Plans", "Emergency", "Health", "Work", "Other"};
        cmbCancellationReason = new JComboBox<>(reasons);
        cmbCancellationReason.setBounds(160, 42, 200, 25);
        searchPanel.add(cmbCancellationReason);
        
        // Details Panel
        JPanel detailsPanel = new JPanel(null);
        detailsPanel.setBounds(20, 190, 600, 280);
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(detailsPanel);
        
        JLabel lblDetailsTitle = new JLabel("RESERVATION DETAILS");
        lblDetailsTitle.setBounds(15, 15, 400, 25);
        lblDetailsTitle.setFont(new Font("Arial Black", Font.BOLD, 12));
        detailsPanel.add(lblDetailsTitle);
        
        txtReservationDetails = new JTextArea();
        txtReservationDetails.setBounds(15, 45, 570, 180);
        txtReservationDetails.setEditable(false);
        txtReservationDetails.setFont(new Font("Arial", Font.PLAIN, 11));
        txtReservationDetails.setLineWrap(true);
        txtReservationDetails.setWrapStyleWord(true);
        txtReservationDetails.setText("Search for a reservation to view details");
        detailsPanel.add(txtReservationDetails);
        
        btnCancelReservation = new JButton("CONFIRM CANCELLATION");
        btnCancelReservation.setBounds(15, 235, 200, 35);
        btnCancelReservation.setBackground(new Color(244, 67, 54));
        btnCancelReservation.setForeground(Color.WHITE);
        btnCancelReservation.setFont(new Font("Arial", Font.BOLD, 11));
        btnCancelReservation.setBorderPainted(false);
        btnCancelReservation.setFocusPainted(false);
        btnCancelReservation.setEnabled(false);
        btnCancelReservation.addActionListener(this);
        detailsPanel.add(btnCancelReservation);
        
        // Refund Policy Panel
        JPanel refundPanel = new JPanel(null);
        refundPanel.setBounds(640, 190, 300, 280);
        refundPanel.setBackground(Color.WHITE);
        refundPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(refundPanel);
        
        JLabel lblRefundTitle = new JLabel("REFUND POLICY");
        lblRefundTitle.setBounds(15, 15, 270, 25);
        lblRefundTitle.setFont(new Font("Arial Black", Font.BOLD, 12));
        lblRefundTitle.setForeground(new Color(76, 175, 80));
        refundPanel.add(lblRefundTitle);
        
        lblCancellationPolicy = new JLabel();
        lblCancellationPolicy.setBounds(15, 50, 270, 80);
        lblCancellationPolicy.setFont(new Font("Arial", Font.PLAIN, 10));
        lblCancellationPolicy.setText(
            "<html>" +
            "More than 7 days: 100% Refund<br>" +
            "Exactly 7 days: 90% Refund<br>" +
            "Less than 7 days: No Refund<br>" +
            "After check-in: No Refund" +
            "</html>"
        );
        refundPanel.add(lblCancellationPolicy);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(15, 140, 270, 2);
        refundPanel.add(separator);
        
        JLabel lblRefundLabel = new JLabel("REFUND AMOUNT:");
        lblRefundLabel.setBounds(15, 155, 150, 20);
        lblRefundLabel.setFont(new Font("Arial Black", Font.BOLD, 11));
        refundPanel.add(lblRefundLabel);
        
        lblRefundAmount = new JLabel("₱0.00");
        lblRefundAmount.setBounds(170, 155, 100, 20);
        lblRefundAmount.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblRefundAmount.setForeground(new Color(76, 175, 80));
        refundPanel.add(lblRefundAmount);
        
        btnBack = new JButton("BACK");
        btnBack.setBounds(15, 235, 270, 35);
        btnBack.setBackground(new Color(158, 158, 158));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 11));
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(this);
        refundPanel.add(btnBack);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            searchReservation();
        } else if (e.getSource() == btnCancelReservation) {
            confirmCancellation();
        } else if (e.getSource() == btnBack) {
            clearForm();
        }
    }
    
    private void searchReservation() {
        String resId = txtReservationId.getText().trim();
        String guestName = txtGuestName.getText().trim();
        
        if (resId.isEmpty() && guestName.isEmpty()) {
            showError("Please enter Reservation ID or Guest Name");
            return;
        }
        
        // Sample reservation data
        checkInDate = LocalDate.of(2026, 6, 20);
        LocalDate checkOutDate = LocalDate.of(2026, 6, 25);
        double totalPrice = 28000.00;
        
        // Display details
        long daysUntilCheckIn = ChronoUnit.DAYS.between(LocalDate.now(), checkInDate);
        double refund = calculateRefund(daysUntilCheckIn, totalPrice);
        
        txtReservationDetails.setText(
            "Reservation ID: RES001\n" +
            "Guest: John Doe\n" +
            "Room: 201 (Double Deluxe)\n" +
            "Check-In: " + checkInDate + "\n" +
            "Check-Out: " + checkOutDate + "\n" +
            "Total Price: ₱" + String.format("%,.2f", totalPrice) + "\n" +
            "Status: Confirmed\n" +
            "Days Until Check-in: " + daysUntilCheckIn
        );
        
        lblRefundAmount.setText("₱" + String.format("%,.2f", refund));
        reservationFound = true;
        btnCancelReservation.setEnabled(true);
        lblErrorMessage.setVisible(false);
    }
    
    private double calculateRefund(long daysUntilCheckIn, double totalPrice) {
        if (daysUntilCheckIn > 7) {
            return totalPrice; // 100% refund
        } else if (daysUntilCheckIn == 7) {
            return totalPrice * 0.90; // 90% refund
        } else if (daysUntilCheckIn > 0) {
            return 0; // No refund
        } else {
            return 0; // No refund after check-in
        }
    }
    
    private void confirmCancellation() {
        if (!reservationFound) {
            showError("No reservation found");
            return;
        }
        
        if (cmbCancellationReason.getSelectedIndex() == 0) {
            showError("Please select a cancellation reason");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this reservation?\n" +
            "Refund Amount: " + lblRefundAmount.getText(),
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Reservation cancelled successfully!\n" +
                "Refund: " + lblRefundAmount.getText() + "\n" +
                "Reason: " + cmbCancellationReason.getSelectedItem(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        }
    }
    
    private void clearForm() {
        txtReservationId.setText("");
        txtGuestName.setText("");
        cmbCancellationReason.setSelectedIndex(0);
        txtReservationDetails.setText("Search for a reservation to view details");
        lblRefundAmount.setText("₱0.00");
        lblErrorMessage.setVisible(false);
        reservationFound = false;
        btnCancelReservation.setEnabled(false);
    }
    
    private void showError(String message) {
        lblErrorMessage.setText("⚠ " + message);
        lblErrorMessage.setVisible(true);
    }
}
