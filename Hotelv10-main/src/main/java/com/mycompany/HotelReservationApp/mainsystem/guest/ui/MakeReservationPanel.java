package com.mycompany.HotelReservationApp.mainsystem.guest.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * MakeReservationPanel - Make a New Reservation
 * Handles step-by-step reservation process with payment method selection
 */
public class MakeReservationPanel extends JPanel implements ActionListener {
    
    private JTextField txtRoomNumber, txtCheckIn, txtCheckOut, txtGuests;
    private JComboBox<String> cmbPaymentMethod;
    private JButton btnMakeReservation, btnCancel;
    private JLabel lblTotalPrice, lblTax, lblSubtotal, lblErrorMessage;
    private JTextArea txtReservationDetails;
    private boolean isConfirmed = false;
    
    public MakeReservationPanel() {
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
        
        JLabel lblTitle = new JLabel("MAKE NEW RESERVATION");
        lblTitle.setBounds(15, 8, 600, 34);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);
        
        // Error Message Label
        lblErrorMessage = new JLabel();
        lblErrorMessage.setFont(new Font("Arial", Font.PLAIN, 11));
        lblErrorMessage.setForeground(new Color(244, 67, 54));
        lblErrorMessage.setBounds(20, 75, 920, 20);
        lblErrorMessage.setVisible(false);
        add(lblErrorMessage);
        
        // Reservation Details Panel
        JPanel detailsPanel = new JPanel(null);
        detailsPanel.setBounds(20, 100, 600, 380);
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(detailsPanel);
        
        // Room Number
        JLabel lblRoom = new JLabel("Room Number:");
        lblRoom.setBounds(15, 15, 150, 20);
        lblRoom.setFont(new Font("Arial", Font.BOLD, 11));
        detailsPanel.add(lblRoom);
        
        txtRoomNumber = new JTextField();
        txtRoomNumber.setBounds(15, 40, 200, 25);
        txtRoomNumber.setEditable(false);
        detailsPanel.add(txtRoomNumber);
        
        // Check-in Date
        JLabel lblCheckIn = new JLabel("Check-In Date (YYYY-MM-DD):");
        lblCheckIn.setBounds(15, 75, 200, 20);
        lblCheckIn.setFont(new Font("Arial", Font.BOLD, 11));
        detailsPanel.add(lblCheckIn);
        
        txtCheckIn = new JTextField();
        txtCheckIn.setBounds(15, 100, 200, 25);
        detailsPanel.add(txtCheckIn);
        
        // Check-out Date
        JLabel lblCheckOut = new JLabel("Check-Out Date (YYYY-MM-DD):");
        lblCheckOut.setBounds(15, 135, 200, 20);
        lblCheckOut.setFont(new Font("Arial", Font.BOLD, 11));
        detailsPanel.add(lblCheckOut);
        
        txtCheckOut = new JTextField();
        txtCheckOut.setBounds(15, 160, 200, 25);
        detailsPanel.add(txtCheckOut);
        
        // Number of Guests
        JLabel lblGuests = new JLabel("Number of Guests:");
        lblGuests.setBounds(15, 195, 150, 20);
        lblGuests.setFont(new Font("Arial", Font.BOLD, 11));
        detailsPanel.add(lblGuests);
        
        txtGuests = new JTextField("1");
        txtGuests.setBounds(15, 220, 200, 25);
        detailsPanel.add(txtGuests);
        
        // Payment Method
        JLabel lblPayment = new JLabel("Payment Method:");
        lblPayment.setBounds(15, 255, 150, 20);
        lblPayment.setFont(new Font("Arial", Font.BOLD, 11));
        detailsPanel.add(lblPayment);
        
        String[] paymentMethods = {"Cash", "Credit Card", "E-Wallet"};
        cmbPaymentMethod = new JComboBox<>(paymentMethods);
        cmbPaymentMethod.setBounds(15, 280, 200, 25);
        detailsPanel.add(cmbPaymentMethod);
        
        // Buttons
        btnMakeReservation = new JButton("CONFIRM RESERVATION");
        btnMakeReservation.setBounds(15, 330, 200, 35);
        btnMakeReservation.setBackground(new Color(76, 175, 80));
        btnMakeReservation.setForeground(Color.WHITE);
        btnMakeReservation.setFont(new Font("Arial", Font.BOLD, 11));
        btnMakeReservation.setBorderPainted(false);
        btnMakeReservation.setFocusPainted(false);
        btnMakeReservation.addActionListener(this);
        detailsPanel.add(btnMakeReservation);
        
        btnCancel = new JButton("CANCEL");
        btnCancel.setBounds(220, 330, 200, 35);
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 11));
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(this);
        detailsPanel.add(btnCancel);
        
        // Price Summary Panel
        JPanel pricePanel = new JPanel(null);
        pricePanel.setBounds(640, 100, 300, 380);
        pricePanel.setBackground(Color.WHITE);
        pricePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(pricePanel);
        
        JLabel lblPriceTitle = new JLabel("PRICE SUMMARY");
        lblPriceTitle.setBounds(15, 15, 270, 25);
        lblPriceTitle.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblPriceTitle.setForeground(new Color(33, 150, 243));
        pricePanel.add(lblPriceTitle);
        
        JLabel lblSubtotalLabel = new JLabel("Subtotal:");
        lblSubtotalLabel.setBounds(15, 50, 150, 20);
        lblSubtotalLabel.setFont(new Font("Arial", Font.BOLD, 11));
        pricePanel.add(lblSubtotalLabel);
        
        lblSubtotal = new JLabel("₱0.00");
        lblSubtotal.setBounds(170, 50, 110, 20);
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 11));
        lblSubtotal.setForeground(new Color(76, 175, 80));
        pricePanel.add(lblSubtotal);
        
        JLabel lblTaxLabel = new JLabel("Tax (12%):");
        lblTaxLabel.setBounds(15, 80, 150, 20);
        lblTaxLabel.setFont(new Font("Arial", Font.BOLD, 11));
        pricePanel.add(lblTaxLabel);
        
        lblTax = new JLabel("₱0.00");
        lblTax.setBounds(170, 80, 110, 20);
        lblTax.setFont(new Font("Arial", Font.BOLD, 11));
        lblTax.setForeground(new Color(76, 175, 80));
        pricePanel.add(lblTax);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(15, 110, 270, 2);
        pricePanel.add(separator);
        
        JLabel lblTotalLabel = new JLabel("TOTAL PRICE:");
        lblTotalLabel.setBounds(15, 125, 150, 20);
        lblTotalLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        pricePanel.add(lblTotalLabel);
        
        lblTotalPrice = new JLabel("₱0.00");
        lblTotalPrice.setBounds(170, 125, 110, 20);
        lblTotalPrice.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblTotalPrice.setForeground(new Color(244, 67, 54));
        pricePanel.add(lblTotalPrice);
        
        JLabel lblDetails = new JLabel("RESERVATION DETAILS:");
        lblDetails.setBounds(15, 160, 270, 20);
        lblDetails.setFont(new Font("Arial Black", Font.BOLD, 11));
        pricePanel.add(lblDetails);
        
        txtReservationDetails = new JTextArea();
        txtReservationDetails.setBounds(15, 185, 270, 150);
        txtReservationDetails.setEditable(false);
        txtReservationDetails.setFont(new Font("Arial", Font.PLAIN, 10));
        txtReservationDetails.setLineWrap(true);
        txtReservationDetails.setWrapStyleWord(true);
        txtReservationDetails.setBackground(Color.decode("#F5F5F5"));
        pricePanel.add(txtReservationDetails);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMakeReservation) {
            makeReservation();
        } else if (e.getSource() == btnCancel) {
            clearForm();
        }
    }
    
    private void makeReservation() {
        String checkIn = txtCheckIn.getText().trim();
        String checkOut = txtCheckOut.getText().trim();
        String guests = txtGuests.getText().trim();
        
        if (checkIn.isEmpty() || checkOut.isEmpty() || guests.isEmpty()) {
            showError("Please fill in all required fields");
            return;
        }
        
        try {
            LocalDate inDate = LocalDate.parse(checkIn);
            LocalDate outDate = LocalDate.parse(checkOut);
            int numGuests = Integer.parseInt(guests);
            
            if (outDate.isBefore(inDate) || outDate.equals(inDate)) {
                showError("Check-out date must be after check-in date");
                return;
            }
            
            if (inDate.isBefore(LocalDate.now())) {
                showError("Check-in date cannot be in the past");
                return;
            }
            
            if (numGuests < 1) {
                showError("Number of guests must be at least 1");
                return;
            }
            
            // Calculate nights and price
            long nights = ChronoUnit.DAYS.between(inDate, outDate);
            double roomRate = 5000.0; // Sample rate
            double subtotal = roomRate * nights;
            double tax = subtotal * 0.12;
            double total = subtotal + tax;
            
            // Update display
            lblSubtotal.setText("₱" + String.format("%,.2f", subtotal));
            lblTax.setText("₱" + String.format("%,.2f", tax));
            lblTotalPrice.setText("₱" + String.format("%,.2f", total));
            
            // Update details
            txtReservationDetails.setText(
                "Check-In: " + checkIn + "\n" +
                "Check-Out: " + checkOut + "\n" +
                "Nights: " + nights + "\n" +
                "Guests: " + numGuests + "\n" +
                "Payment: " + cmbPaymentMethod.getSelectedItem() + "\n" +
                "Status: Confirmed"
            );
            
            isConfirmed = true;
            lblErrorMessage.setVisible(false);
            JOptionPane.showMessageDialog(this,
                "Reservation confirmed!\n\nTotal: ₱" + String.format("%,.2f", total),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            showError("Invalid input format. Please check your entries.");
        }
    }
    
    private void clearForm() {
        txtCheckIn.setText("");
        txtCheckOut.setText("");
        txtGuests.setText("1");
        cmbPaymentMethod.setSelectedIndex(0);
        lblSubtotal.setText("₱0.00");
        lblTax.setText("₱0.00");
        lblTotalPrice.setText("₱0.00");
        txtReservationDetails.setText("");
        lblErrorMessage.setVisible(false);
        isConfirmed = false;
    }
    
    private void showError(String message) {
        lblErrorMessage.setText("⚠ " + message);
        lblErrorMessage.setVisible(true);
    }
}
