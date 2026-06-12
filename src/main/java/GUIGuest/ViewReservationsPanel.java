package GUIGuest;

import DAO.GuestDAO;
import DAO.ReservationDAO;
import DAO.RoomDAO;
import Models.Guest;
import Models.Reservation;
import Models.Room;
import HotelReservationMainSystem.SessionManager;
import Utilities.Constants;
import Utilities.CurrencyUtil;
import Utilities.MessageBox;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ViewReservationsPanel - Guest reservation history and management
 *
 * Displays all reservations for the logged-in guest in a table.
 * Supports filtering by status, viewing full reservation details,
 * and cancelling a confirmed reservation with a reason.
 */
public class ViewReservationsPanel extends JPanel {

    private ReservationDAO reservationDAO;
    private GuestDAO guestDAO;
    private RoomDAO roomDAO;

    private JTable reservationsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbStatus;
    private JButton btnRefresh, btnViewDetails, btnCancel;

    // Store loaded reservations so we can look up the full object by selected row
    private List<Reservation> loadedReservations;

    public ViewReservationsPanel() {
        this.reservationDAO = new ReservationDAO();
        this.guestDAO = new GuestDAO();
        this.roomDAO = new RoomDAO();
        initUI();
        loadReservations();
    }

    // ============================================================
    // UI SETUP
    // ============================================================

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createFilterPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Filter by Status:"));

        cmbStatus = new JComboBox<>(new String[]{
            "All",
            Constants.RESERVATION_STATUS_CONFIRMED,
            Constants.RESERVATION_STATUS_CHECKED_IN,
            Constants.RESERVATION_STATUS_CHECKED_OUT,
            Constants.RESERVATION_STATUS_CANCELLED
        });
        cmbStatus.addActionListener(e -> loadReservations());
        panel.add(cmbStatus);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(25, 118, 210));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadReservations());
        panel.add(btnRefresh);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Non-editable table model
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
        reservationsTable.setRowHeight(28);
        reservationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationsTable.getTableHeader().setReorderingAllowed(false);
        reservationsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        reservationsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // Color-code rows by reservation status
        reservationsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = (String) tableModel.getValueAt(row, 4);
                    if (Constants.RESERVATION_STATUS_CONFIRMED.equals(status)) {
                        c.setBackground(new Color(232, 245, 233)); // light green
                    } else if (Constants.RESERVATION_STATUS_CHECKED_IN.equals(status)) {
                        c.setBackground(new Color(227, 242, 253)); // light blue
                    } else if (Constants.RESERVATION_STATUS_CHECKED_OUT.equals(status)) {
                        c.setBackground(new Color(250, 250, 250)); // light grey
                    } else if (Constants.RESERVATION_STATUS_CANCELLED.equals(status)) {
                        c.setBackground(new Color(255, 235, 238)); // light red
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        // Open details on double-click
        reservationsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewDetails();
                }
            }
        });

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
        btnViewDetails.setFocusPainted(false);
        btnViewDetails.addActionListener(e -> viewDetails());
        panel.add(btnViewDetails);

        btnCancel = new JButton("Cancel Reservation");
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> cancelReservation());
        panel.add(btnCancel);

        return panel;
    }

    // ============================================================
    // DATA LOADING
    // ============================================================

    /**
     * Loads reservations for the currently logged-in guest and
     * populates the table, applying the selected status filter.
     */
    private void loadReservations() {
        try {
            int userId = SessionManager.getInstance().getCurrentUserId();
            Guest guest = guestDAO.getGuestByUserId(userId);

            if (guest == null) {
                // Guest has not completed their profile yet — show empty table
                tableModel.setRowCount(0);
                loadedReservations = null;
                return;
            }

            loadedReservations = reservationDAO.getReservationsByGuestId(guest.getGuestId());
            tableModel.setRowCount(0);

            String selectedStatus = (String) cmbStatus.getSelectedItem();

            for (Reservation res : loadedReservations) {
                // Apply status filter
                if (!"All".equals(selectedStatus) &&
                        !res.getReservationStatus().equals(selectedStatus)) {
                    continue;
                }

                // Look up the room number to show instead of the internal room ID
                String roomNumber = String.valueOf(res.getRoomId());
                try {
                    Room room = roomDAO.getRoomById(res.getRoomId());
                    if (room != null) {
                        roomNumber = room.getRoomNumber();
                    }
                } catch (Exception ignored) {
                    // Fall back to showing the raw room ID if lookup fails
                }

                tableModel.addRow(new Object[]{
                    res.getReservationId(),
                    roomNumber,
                    res.getCheckInDate() != null ? res.getCheckInDate().toString() : "",
                    res.getCheckOutDate() != null ? res.getCheckOutDate().toString() : "",
                    res.getReservationStatus(),
                    CurrencyUtil.formatCurrency(
                        res.getFinalTotal() != null ? res.getFinalTotal() : res.getTotalPrice())
                });
            }

        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to load reservations: " + e.getMessage());
        }
    }

    // ============================================================
    // VIEW DETAILS
    // ============================================================

    /**
     * Shows a dialog with the full details of the selected reservation.
     */
    private void viewDetails() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow < 0) {
            MessageBox.showError("No Selection", "Please select a reservation to view.");
            return;
        }

        int reservationId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Reservation res = findReservationById(reservationId);
        if (res == null) {
            MessageBox.showError("Error", "Could not retrieve reservation details.");
            return;
        }

        // Fetch room info for the detail view
        Room room = null;
        try {
            room = roomDAO.getRoomById(res.getRoomId());
        } catch (Exception ignored) { }

        showDetailsDialog(res, room);
    }

    /**
     * Builds and displays the full-details dialog for a reservation.
     */
    private void showDetailsDialog(Reservation res, Room room) {
        JDialog dialog = new JDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Reservation Details — #" + res.getReservationId(),
            true
        );
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(480, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        // ---- Header bar ----
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(25, 118, 210));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel lblTitle = new JLabel("Reservation #" + res.getReservationId());
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle);
        dialog.add(header, BorderLayout.NORTH);

        // ---- Details panel ----
        JPanel details = new JPanel(new GridBagLayout());
        details.setBackground(Color.WHITE);
        details.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints labelGbc = new GridBagConstraints();
        labelGbc.anchor = GridBagConstraints.WEST;
        labelGbc.insets = new Insets(5, 0, 5, 15);
        labelGbc.gridx = 0;

        GridBagConstraints valueGbc = new GridBagConstraints();
        valueGbc.anchor = GridBagConstraints.WEST;
        valueGbc.insets = new Insets(5, 0, 5, 0);
        valueGbc.gridx = 1;
        valueGbc.fill = GridBagConstraints.HORIZONTAL;
        valueGbc.weightx = 1.0;

        int row = 0;

        // Room info
        String roomNum  = room != null ? room.getRoomNumber() : String.valueOf(res.getRoomId());
        String roomType = room != null ? room.getRoomType()   : "N/A";
        addDetailRow(details, labelGbc, valueGbc, row++, "Room Number:",  roomNum);
        addDetailRow(details, labelGbc, valueGbc, row++, "Room Type:",    roomType);

        // Dates and duration
        addDetailRow(details, labelGbc, valueGbc, row++, "Check-in Date:",
            res.getCheckInDate() != null ? res.getCheckInDate().toString() : "N/A");
        addDetailRow(details, labelGbc, valueGbc, row++, "Check-out Date:",
            res.getCheckOutDate() != null ? res.getCheckOutDate().toString() : "N/A");
        addDetailRow(details, labelGbc, valueGbc, row++, "Number of Nights:",
            String.valueOf(res.getNumberOfNights()));

        // Guests
        addDetailRow(details, labelGbc, valueGbc, row++, "Number of Guests:",
            String.valueOf(res.getNumberOfGuests()));

        // Pricing
        addDetailRow(details, labelGbc, valueGbc, row++, "Room Rate / Night:",
            res.getRoomRate() != null ? CurrencyUtil.formatCurrency(res.getRoomRate()) : "N/A");
        addDetailRow(details, labelGbc, valueGbc, row++, "Subtotal:",
            res.getTotalPrice() != null ? CurrencyUtil.formatCurrency(res.getTotalPrice()) : "N/A");
        addDetailRow(details, labelGbc, valueGbc, row++, "Discount Applied:",
            res.getDiscountApplied() != null ? CurrencyUtil.formatCurrency(res.getDiscountApplied()) : "PHP 0.00");

        // Final total — displayed in bold green
        GridBagConstraints finalLbl = (GridBagConstraints) labelGbc.clone();
        finalLbl.gridy = row;
        GridBagConstraints finalVal = (GridBagConstraints) valueGbc.clone();
        finalVal.gridy = row++;
        JLabel lblFinalKey = new JLabel("Total Price:");
        lblFinalKey.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel lblFinalVal = new JLabel(
            res.getFinalTotal() != null
                ? CurrencyUtil.formatCurrency(res.getFinalTotal())
                : (res.getTotalPrice() != null ? CurrencyUtil.formatCurrency(res.getTotalPrice()) : "N/A"));
        lblFinalVal.setFont(new Font("Arial", Font.BOLD, 13));
        lblFinalVal.setForeground(new Color(27, 94, 32));
        details.add(lblFinalKey, finalLbl);
        details.add(lblFinalVal, finalVal);

        // Status — colour-coded
        GridBagConstraints statusLbl = (GridBagConstraints) labelGbc.clone();
        statusLbl.gridy = row;
        GridBagConstraints statusVal = (GridBagConstraints) valueGbc.clone();
        statusVal.gridy = row++;
        JLabel lblStatusKey = new JLabel("Status:");
        lblStatusKey.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel lblStatusVal = new JLabel(res.getReservationStatus());
        lblStatusVal.setFont(new Font("Arial", Font.BOLD, 13));
        lblStatusVal.setForeground(getStatusColor(res.getReservationStatus()));
        details.add(lblStatusKey, statusLbl);
        details.add(lblStatusVal, statusVal);

        // Reservation date
        addDetailRow(details, labelGbc, valueGbc, row++, "Booked On:",
            res.getReservationDate() != null ? res.getReservationDate().toString() : "N/A");

        // Notes (if any)
        if (res.getNotes() != null && !res.getNotes().trim().isEmpty()) {
            addDetailRow(details, labelGbc, valueGbc, row++, "Notes:", res.getNotes());
        }

        // Cancellation details (if cancelled)
        if (res.isCancelled()) {
            addDetailRow(details, labelGbc, valueGbc, row++, "Cancelled On:",
                res.getCancelledDate() != null ? res.getCancelledDate().toString() : "N/A");
            addDetailRow(details, labelGbc, valueGbc, row++, "Cancel Reason:",
                res.getCancellationReason() != null ? res.getCancellationReason() : "N/A");
        }

        JScrollPane scrollPane = new JScrollPane(details);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(scrollPane, BorderLayout.CENTER);

        // ---- Close button ----
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JButton btnClose = new JButton("Close");
        btnClose.setPreferredSize(new Dimension(100, 30));
        btnClose.setBackground(new Color(25, 118, 210));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dialog.dispose());
        footer.add(btnClose);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Helper: adds one label-value row to the GridBagLayout details panel.
     */
    private void addDetailRow(JPanel panel, GridBagConstraints labelGbc,
            GridBagConstraints valueGbc, int row, String label, String value) {
        GridBagConstraints lgbc = (GridBagConstraints) labelGbc.clone();
        lgbc.gridy = row;
        GridBagConstraints vgbc = (GridBagConstraints) valueGbc.clone();
        vgbc.gridy = row;

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(new Color(80, 80, 80));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 13));

        panel.add(lbl, lgbc);
        panel.add(val, vgbc);
    }

    /**
     * Returns a display colour for each reservation status.
     */
    private Color getStatusColor(String status) {
        if (Constants.RESERVATION_STATUS_CONFIRMED.equals(status))  return new Color(27, 94, 32);
        if (Constants.RESERVATION_STATUS_CHECKED_IN.equals(status)) return new Color(13, 71, 161);
        if (Constants.RESERVATION_STATUS_CANCELLED.equals(status))  return new Color(183, 28, 28);
        return Color.DARK_GRAY; // Checked-Out or unknown
    }

    // ============================================================
    // CANCEL RESERVATION
    // ============================================================

    /**
     * Prompts the guest for a cancellation reason and cancels the
     * selected reservation, but only if it is still "Confirmed".
     */
    private void cancelReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        if (selectedRow < 0) {
            MessageBox.showError("No Selection", "Please select a reservation to cancel.");
            return;
        }

        int reservationId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);

        // Only allow cancellation of confirmed reservations
        if (!Constants.RESERVATION_STATUS_CONFIRMED.equals(currentStatus)) {
            MessageBox.showError("Cannot Cancel",
                "Only reservations with status \"Confirmed\" can be cancelled.\n" +
                "This reservation is currently: " + currentStatus);
            return;
        }

        // Confirm with the guest before proceeding
        int confirm = MessageBox.showConfirm(
            "Confirm Cancellation",
            "Are you sure you want to cancel Reservation #" + reservationId + "?\n" +
            "This action cannot be undone."
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Ask for a reason
        String reason = JOptionPane.showInputDialog(
            this,
            "Please enter a reason for cancellation (optional):",
            "Cancellation Reason",
            JOptionPane.QUESTION_MESSAGE
        );

        // User hit Cancel on the input dialog — abort
        if (reason == null) {
            return;
        }

        String finalReason = reason.trim().isEmpty()
            ? "Guest initiated cancellation"
            : reason.trim();

        try {
            reservationDAO.cancelReservation(reservationId, finalReason);
            MessageBox.showInfo("Cancelled",
                "Reservation #" + reservationId + " has been cancelled successfully.");
            loadReservations();
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to cancel reservation: " + e.getMessage());
        }
    }

    // ============================================================
    // HELPERS
    // ============================================================

    /**
     * Finds a reservation in the loaded list by its ID.
     *
     * @param reservationId the ID to search for
     * @return matching Reservation, or null if not found
     */
    private Reservation findReservationById(int reservationId) {
        if (loadedReservations == null) return null;
        for (Reservation r : loadedReservations) {
            if (r.getReservationId() == reservationId) return r;
        }
        return null;
    }
}