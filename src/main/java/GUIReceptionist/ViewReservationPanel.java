package GUIReceptionist;

import Models.Reservation;
import Models.Room;
import Services.ReservationService;
import Services.RoomService;
import Utilities.HotelException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Receptionist view for all reservations.
 */
public class ViewReservationPanel extends JFrame {

    private JTable reservationTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;

    public ViewReservationPanel() {
        setTitle("Receptionist - View All Reservations");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("ALL RESERVATIONS", SwingConstants.CENTER);
        title.setFont(new Font("Arial Black", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Table columns
        String[] columns = {"ID", "Guest", "Room", "Check-in", "Check-out", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        // Load data
        try {
            ReservationService resService = new ReservationService();
            RoomService roomService = new RoomService();
            List<Reservation> reservations = resService.getAllReservations();

            for (Reservation r : reservations) {
                Room room = roomService.getRoomById(r.getRoomId());
                String roomNumber = (room != null) ? room.getRoomNumber() : "N/A";
                // Guest name would need a join; we can show guest ID for now
                model.addRow(new Object[]{
                    r.getReservationId(),
                    "Guest ID: " + r.getGuestId(),
                    roomNumber,
                    r.getCheckInDate(),
                    r.getCheckOutDate(),
                    r.getReservationStatus()
                });
            }
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        reservationTable = new JTable(model);
        reservationTable.setRowHeight(25);
        reservationTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));
        reservationTable.getTableHeader().setBackground(Color.decode("#222222"));
        reservationTable.getTableHeader().setForeground(Color.WHITE);

        scrollPane = new JScrollPane(reservationTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> refreshTable());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        model.setRowCount(0);
        try {
            ReservationService resService = new ReservationService();
            RoomService roomService = new RoomService();
            List<Reservation> reservations = resService.getAllReservations();
            for (Reservation r : reservations) {
                Room room = roomService.getRoomById(r.getRoomId());
                String roomNumber = (room != null) ? room.getRoomNumber() : "N/A";
                model.addRow(new Object[]{
                    r.getReservationId(),
                    "Guest ID: " + r.getGuestId(),
                    roomNumber,
                    r.getCheckInDate(),
                    r.getCheckOutDate(),
                    r.getReservationStatus()
                });
            }
        } catch (HotelException ex) {
            JOptionPane.showMessageDialog(this, "Refresh failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}