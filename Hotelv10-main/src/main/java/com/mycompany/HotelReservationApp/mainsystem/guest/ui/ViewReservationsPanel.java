package com.mycompany.HotelReservationApp.mainsystem.guest.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * ViewReservationsPanel - View Guest's Reservations
 * Displays all reservations for the logged-in guest
 */
public class ViewReservationsPanel extends JPanel implements ActionListener {
    
    private JTable reservationsTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnViewDetails, btnCancel;
    private JLabel lblErrorMessage;
    
    public ViewReservationsPanel() {
        setLayout(null);
        setBackground(Color.decode("#F5F5F5"));
        createComponents();
        loadSampleData();
    }
    
    private void createComponents() {
        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(30, 20, 880, 50);
        titleBar.setBackground(Color.decode("#222222"));
        add(titleBar);
        
        JLabel lblTitle = new JLabel("MY RESERVATIONS");
        lblTitle.setBounds(15, 8, 400, 34);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);
        
        lblErrorMessage = new JLabel();
        lblErrorMessage.setFont(new Font("Arial", Font.PLAIN, 11));
        lblErrorMessage.setForeground(new Color(244, 67, 54));
        lblErrorMessage.setBounds(30, 75, 880, 20);
        lblErrorMessage.setVisible(false);
        add(lblErrorMessage);
        
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBounds(30, 100, 880, 45);
        searchPanel.setBackground(Color.decode("#333333"));
        add(searchPanel);
        
        JLabel lblSearch = new JLabel("Search Reservation:");
        lblSearch.setBounds(15, 10, 150, 25);
        lblSearch.setForeground(Color.WHITE);
        lblSearch.setFont(new Font("Arial", Font.BOLD, 11));
        searchPanel.add(lblSearch);
        
        txtSearch = new JTextField();
        txtSearch.setBounds(170, 10, 400, 25);
        searchPanel.add(txtSearch);
        
        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(580, 10, 100, 25);
        btnSearch.setBackground(new Color(76, 175, 80));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial", Font.BOLD, 11));
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(this);
        searchPanel.add(btnSearch);
        
        String[] columns = {"Res ID", "Room #", "Check-In", "Check-Out", "Status", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reservationsTable = new JTable(tableModel);
        reservationsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        reservationsTable.setRowHeight(22);
        reservationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(reservationsTable);
        scrollPane.setBounds(30, 155, 880, 310);
        add(scrollPane);
        
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setBounds(30, 475, 880, 40);
        buttonPanel.setBackground(Color.decode("#F5F5F5"));
        add(buttonPanel);
        
        btnViewDetails = new JButton("VIEW DETAILS");
        btnViewDetails.setBounds(600, 5, 130, 30);
        btnViewDetails.setBackground(new Color(33, 150, 243));
        btnViewDetails.setForeground(Color.WHITE);
        btnViewDetails.setFont(new Font("Arial", Font.BOLD, 11));
        btnViewDetails.setBorderPainted(false);
        btnViewDetails.setFocusPainted(false);
        btnViewDetails.addActionListener(this);
        buttonPanel.add(btnViewDetails);
        
        btnCancel = new JButton("CANCEL RESERVATION");
        btnCancel.setBounds(750, 5, 130, 30);
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 11));
        btnCancel.setBorderPainted(false);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(this);
        buttonPanel.add(btnCancel);
    }
    
    private void loadSampleData() {
        tableModel.addRow(new Object[]{"RES001", "101", "2026-06-15", "2026-06-17", "Confirmed", "₱5,600"});
        tableModel.addRow(new Object[]{"RES002", "202", "2026-07-01", "2026-07-05", "Confirmed", "₱22,400"});
        tableModel.addRow(new Object[]{"RES003", "103", "2026-06-20", "2026-06-21", "Cancelled", "₱2,800"});
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            performSearch();
        } else if (e.getSource() == btnViewDetails) {
            viewDetails();
        } else if (e.getSource() == btnCancel) {
            cancelReservation();
        }
    }
    
    private void performSearch() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search keyword");
            return;
        }
        for (int i = 0; i < reservationsTable.getRowCount(); i++) {
            String resId = reservationsTable.getValueAt(i, 0).toString().toLowerCase();
            String room = reservationsTable.getValueAt(i, 1).toString().toLowerCase();
            if (resId.contains(keyword) || room.contains(keyword)) {
                reservationsTable.setRowSelectionInterval(i, i);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "No matching reservation found");
    }
    
    private void viewDetails() {
        int row = reservationsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation first");
            return;
        }
        String details = "RESERVATION DETAILS\n\n" +
            "Reservation ID: " + reservationsTable.getValueAt(row, 0) + "\n" +
            "Room Number: " + reservationsTable.getValueAt(row, 1) + "\n" +
            "Check-In: " + reservationsTable.getValueAt(row, 2) + "\n" +
            "Check-Out: " + reservationsTable.getValueAt(row, 3) + "\n" +
            "Status: " + reservationsTable.getValueAt(row, 4) + "\n" +
            "Total Amount: " + reservationsTable.getValueAt(row, 5);
        JOptionPane.showMessageDialog(this, details, "Reservation Details", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cancelReservation() {
        int row = reservationsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation first");
            return;
        }
        String status = reservationsTable.getValueAt(row, 4).toString();
        if (status.equals("Cancelled")) {
            JOptionPane.showMessageDialog(this, "This reservation is already cancelled");
            return;
        }
        if (status.equals("Checked-Out")) {
            JOptionPane.showMessageDialog(this, "Cannot cancel a completed reservation");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel this reservation?\n\nRefund will be processed according to our cancellation policy.",
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            reservationsTable.setValueAt("Cancelled", row, 4);
            JOptionPane.showMessageDialog(this,
                "Reservation cancelled successfully.\n\nRefund amount: ₱4,704 (84% of total)\n\nRefund will be processed within 5-7 business days.",
                "Cancellation Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}