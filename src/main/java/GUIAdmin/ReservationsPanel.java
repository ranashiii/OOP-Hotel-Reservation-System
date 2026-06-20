package GUIAdmin;

import Models.Reservation;
import Services.ReservationService;
import Services.RoomService;
import Utilities.HotelException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReservationsPanel extends JPanel implements ActionListener {

    private JTable reservationTable;
    private DefaultTableModel model;
    private JLabel lblTotal, lblActive;
    private JButton btnRefresh, btnCancel, btnAdd;
    private JTextField txtGuestId, txtRoomId, txtCheckIn, txtCheckOut;

    private ReservationService reservationService = new ReservationService();
    private RoomService roomService = new RoomService();

    public ReservationsPanel() {
        setLayout(new BorderLayout());

        // Top panel with stats and buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotal = new JLabel("Total: 0");
        lblActive = new JLabel("Active: 0");
        topPanel.add(lblTotal);
        topPanel.add(lblActive);

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(this);
        topPanel.add(btnRefresh);

        btnCancel = new JButton("Cancel Selected");
        btnCancel.addActionListener(this);
        topPanel.add(btnCancel);

        btnAdd = new JButton("Add Reservation");
        btnAdd.addActionListener(this);
        topPanel.add(btnAdd);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Guest ID", "Room ID", "Room #", "Check-in", "Check-out", "Status", "Total"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        reservationTable = new JTable(model);
        reservationTable.setRowHeight(25);
        reservationTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));
        reservationTable.getTableHeader().setBackground(Color.decode("#222222"));
        reservationTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(reservationTable);
        add(scroll, BorderLayout.CENTER);

        // Bottom panel for adding
        JPanel addPanel = new JPanel(new FlowLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Reservation"));
        addPanel.add(new JLabel("Guest ID:"));
        txtGuestId = new JTextField(5);
        addPanel.add(txtGuestId);
        addPanel.add(new JLabel("Room ID:"));
        txtRoomId = new JTextField(5);
        addPanel.add(txtRoomId);
        addPanel.add(new JLabel("Check-in (YYYY-MM-DD):"));
        txtCheckIn = new JTextField(10);
        addPanel.add(txtCheckIn);
        addPanel.add(new JLabel("Check-out (YYYY-MM-DD):"));
        txtCheckOut = new JTextField(10);
        addPanel.add(txtCheckOut);
        JButton btnAddSubmit = new JButton("Add");
        btnAddSubmit.addActionListener(e -> addReservation());
        addPanel.add(btnAddSubmit);
        add(addPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<Reservation> reservations = reservationService.findAllReservations();
            int total = reservations.size();
            int active = 0;
            for (Reservation r : reservations) {
                if (r.getReservationStatus().equals("Confirmed") || r.getReservationStatus().equals("Checked-In")) {
                    active++;
                }
                String roomNumber = "N/A";
                try {
                    roomNumber = roomService.getRoomById(r.getRoomId()).getRoomNumber();
                } catch (Exception ex) {}
                model.addRow(new Object[]{
                    r.getReservationId(),
                    r.getGuestId(),
                    r.getRoomId(),
                    roomNumber,
                    r.getCheckInDate(),
                    r.getCheckOutDate(),
                    r.getReservationStatus(),
                    String.format("%.2f", r.getFinalTotal())
                });
            }
            lblTotal.setText("Total: " + total);
            lblActive.setText("Active: " + active);
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + ex.getMessage());
        }
    }

    private void addReservation() {
        String guestId = txtGuestId.getText().trim();
        String roomId = txtRoomId.getText().trim();
        String checkIn = txtCheckIn.getText().trim();
        String checkOut = txtCheckOut.getText().trim();
        if (guestId.isEmpty() || roomId.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        try {
            reservationService.addReservation(guestId, roomId, checkIn, checkOut);
            JOptionPane.showMessageDialog(this, "Reservation added successfully.");
            loadData();
            txtGuestId.setText("");
            txtRoomId.setText("");
            txtCheckIn.setText("");
            txtCheckOut.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadData();
        } else if (e.getSource() == btnCancel) {
            int row = reservationTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Cancel reservation ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    reservationService.cancelReservation(id);
                    JOptionPane.showMessageDialog(this, "Reservation cancelled.");
                    loadData();
                } catch (HotelException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        } else if (e.getSource() == btnAdd) {
            // Scroll to bottom where add form is
            // Nothing needed, the form is always visible.
        }
    }
}