package com.hotelreservationsystem.gui.guest;

import com.hotelreservationsystem.dao.ReservationDAO;
import com.hotelreservationsystem.model.Reservation;
import com.hotelreservationsystem.SessionManager;
import com.hotelreservationsystem.dao.GuestDAO;
import com.hotelreservationsystem.model.Guest;
import com.hotelreservationsystem.util.HotelException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewReservationsPanel - View Guest's Reservations
 * 
 * Displays all reservations for the logged-in guest.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class ViewReservationsPanel extends JPanel {
    
    private JTable reservationsTable;
    private JButton viewDetailsButton;
    private JButton cancelButton;
    private JButton refreshButton;
    
    public ViewReservationsPanel() {
        setLayout(null);
        initializeComponents();
        loadReservations();
    }
    
    private void initializeComponents() {
        // Title
        JLabel titleLabel = new JLabel("My Reservations");
        titleLabel.setBounds(30, 20, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel);
        
        // Table
        reservationsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        scrollPane.setBounds(30, 60, 740, 300);
        add(scrollPane);
        
        // Buttons
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setBounds(30, 380, 120, 35);
        viewDetailsButton.addActionListener(e -> viewDetails());
        add(viewDetailsButton);
        
        cancelButton = new JButton("Cancel Reservation");
        cancelButton.setBounds(160, 380, 150, 35);
        cancelButton.addActionListener(e -> cancelReservation());
        add(cancelButton);
        
        refreshButton = new JButton("Refresh");
        refreshButton.setBounds(320, 380, 100, 35);
        refreshButton.addActionListener(e -> loadReservations());
        add(refreshButton);
    }
    
    private void loadReservations() {
        try {
            int guestId = getGuestIdForCurrentUser();
            List<Reservation> reservations = ReservationDAO.getReservationsByGuestId(guestId);
            
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Reservation ID");
            model.addColumn("Room #");
            model.addColumn("Check-in");
            model.addColumn("Check-out");
            model.addColumn("Nights");
            model.addColumn("Total");
            model.addColumn("Status");
            
            for (Reservation res : reservations) {
                model.addRow(new Object[]{
                    res.getReservationId(),
                    res.getRoomId(),
                    res.getCheckInDate(),
                    res.getCheckOutDate(),
                    res.getNumberOfNights(),
                    "PHP " + res.getFinalTotal(),
                    res.getReservationStatus()
                });
            }
            
            reservationsTable.setModel(model);
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewDetails() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            JOptionPane.showMessageDialog(this, "Details view coming soon!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cancelReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow >= 0) {
            JOptionPane.showMessageDialog(this, "Cancellation feature coming soon!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a reservation", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private int getGuestIdForCurrentUser() throws HotelException {
        int userId = SessionManager.getInstance().getCurrentUserId();
        Guest guest = GuestDAO.getGuestByUserId(userId);
        if (guest == null) {
            throw new HotelException("Guest profile not found");
        }
        return guest.getGuestId();
    }
}
