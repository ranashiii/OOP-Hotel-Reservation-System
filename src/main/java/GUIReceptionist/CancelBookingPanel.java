package GUIReceptionist;

import Models.Guest;
import Models.Reservation;
import Models.Room;
import Services.GuestService;
import Services.ReservationService;
import Services.RoomService;
import Utilities.CurrencyUtil;
import Utilities.HotelException;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * CancelBookingPanel - Receptionist screen for cancelling guest reservations.
 *
 * Lists all active (non-cancelled) reservations, lets the receptionist select one,
 * enter a cancellation reason, and confirm. Cancellation is delegated to
 * Services.ReservationService.cancelReservation(), which applies the hotel's
 * refund policy and persists the cancellation through the DAO layer.
 */
public class CancelBookingPanel extends JFrame implements ActionListener {

    private JButton btnSideDash, btnSideRegister, btnSideRoomBooking, btnSidePay, btnSideReserve,
            btnSideCancel, btnHomePage, btnRefresh, btnCancelSelected;

    private JLabel lblReception, lblHotel, lblManagement, lblDate, lblRecent, lblTotalActive,
            lblSelected, lblSelectedValue, lblRefundNote, lblReasonCaption;

    private JPanel sidePan, topPan, dashboardPanel, recentPanel, detailsPanel;

    private JTable tblReservations;
    private DefaultTableModel reservationTableModel;
    private JTextField txtReason;

    private final ReservationService reservationService = new ReservationService();
    private final GuestService guestService = new GuestService();
    private final RoomService roomService = new RoomService();

    // row index in the table -> actual reservation ID, since the table only shows active reservations
    private final Map<Integer, Integer> rowToReservationId = new HashMap<>();
    private int selectedReservationId = -1;

    CancelBookingPanel() {
        sidePanel();
        functionMenu();
        topPanel();
        cancelPan();
        loadReservations();

        setSize(1200, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Hotel Reservation System - Receptionist Cancel Booking");
    }

    private void functionMenu() {
        lblReception = new JLabel("CANCEL BOOKING");
        lblReception.setBounds(330, 60, 800, 60);
        lblReception.setFont(new Font("Arial Black", Font.BOLD, 45));
        add(lblReception);
    }

    private void sidePanel() {
        sidePan = new JPanel();
        sidePan.setBounds(0, 0, 300, 800);
        sidePan.setLayout(null);
        sidePan.setBackground(Color.decode("#222222"));
        add(sidePan);

        lblHotel = new JLabel("HOTEL");
        lblHotel.setBounds(10, 10, 800, 50);
        lblHotel.setFont(new Font("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidePan.add(lblHotel);

        lblManagement = new JLabel("MANAGEMENT");
        lblManagement.setBounds(10, 50, 800, 50);
        lblManagement.setFont(new Font("Arial Black", Font.BOLD, 30));
        lblManagement.setForeground(Color.WHITE);
        sidePan.add(lblManagement);

        btnHomePage = new JButton("Home Page");
        btnHomePage.setBounds(0, 160, 300, 50);
        btnHomePage.setBackground(Color.decode("#222222"));
        btnHomePage.setForeground(Color.WHITE);
        btnHomePage.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnHomePage.setBorderPainted(false);
        btnHomePage.setFocusPainted(false);
        btnHomePage.setBorder(null);
        btnHomePage.addActionListener(this);
        sidePan.add(btnHomePage);

        btnSideDash = new JButton("Dashboard");
        btnSideDash.setBounds(0, 230, 300, 50);
        btnSideDash.setBackground(Color.decode("#222222"));
        btnSideDash.setForeground(Color.WHITE);
        btnSideDash.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnSideDash.setBorderPainted(false);
        btnSideDash.setFocusPainted(false);
        btnSideDash.setBorder(null);
        btnSideDash.addActionListener(this);
        sidePan.add(btnSideDash);

        btnSideRegister = new JButton("Guest Register");
        btnSideRegister.setBounds(0, 300, 300, 50);
        btnSideRegister.setBackground(Color.decode("#222222"));
        btnSideRegister.setForeground(Color.WHITE);
        btnSideRegister.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnSideRegister.setBorderPainted(false);
        btnSideRegister.setFocusPainted(false);
        btnSideRegister.setBorder(null);
        btnSideRegister.addActionListener(this);
        sidePan.add(btnSideRegister);

        btnSideRoomBooking = new JButton("Room Booking");
        btnSideRoomBooking.setBounds(0, 370, 300, 50);
        btnSideRoomBooking.setBackground(Color.decode("#222222"));
        btnSideRoomBooking.setForeground(Color.WHITE);
        btnSideRoomBooking.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnSideRoomBooking.setBorderPainted(false);
        btnSideRoomBooking.setFocusPainted(false);
        btnSideRoomBooking.setBorder(null);
        btnSideRoomBooking.addActionListener(this);
        sidePan.add(btnSideRoomBooking);

        btnSidePay = new JButton("Payment");
        btnSidePay.setBounds(0, 440, 300, 50);
        btnSidePay.setBackground(Color.decode("#222222"));
        btnSidePay.setForeground(Color.WHITE);
        btnSidePay.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnSidePay.setBorderPainted(false);
        btnSidePay.setFocusPainted(false);
        btnSidePay.setBorder(null);
        btnSidePay.addActionListener(this);
        sidePan.add(btnSidePay);

        btnSideReserve = new JButton("Reservation");
        btnSideReserve.setBounds(0, 510, 300, 50);
        btnSideReserve.setBackground(Color.decode("#222222"));
        btnSideReserve.setForeground(Color.WHITE);
        btnSideReserve.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnSideReserve.setBorderPainted(false);
        btnSideReserve.setFocusPainted(false);
        btnSideReserve.setBorder(null);
        btnSideReserve.addActionListener(this);
        sidePan.add(btnSideReserve);

        btnSideCancel = new JButton("Cancel Booking");
        btnSideCancel.setBounds(0, 580, 300, 50);
        btnSideCancel.setBackground(Color.decode("#FFFFFF"));
        btnSideCancel.setForeground(Color.BLACK);
        btnSideCancel.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnSideCancel.setBorderPainted(false);
        btnSideCancel.setFocusPainted(false);
        btnSideCancel.setBorder(null);
        btnSideCancel.addActionListener(this);
        sidePan.add(btnSideCancel);
    }

    private void topPanel() {
        topPan = new JPanel();
        topPan.setBounds(300, 0, 900, 60);
        topPan.setLayout(null);
        topPan.setBackground(Color.decode("#FFFFFF"));
        add(topPan);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        lblDate = new JLabel(dateFormat.format(new Date()));
        lblDate.setBounds(20, 15, 300, 30);
        lblDate.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblDate.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblDate);
    }

    private void cancelPan() {
        dashboardPanel = new JPanel();
        dashboardPanel.setBounds(300, 130, 900, 520);
        dashboardPanel.setLayout(null);
        dashboardPanel.setBackground(Color.decode("#F5F5F5"));
        add(dashboardPanel);

        // ----- Reservation table (active bookings only) -----
        recentPanel = new JPanel();
        recentPanel.setBounds(20, 20, 860, 320);
        recentPanel.setBackground(Color.WHITE);
        recentPanel.setLayout(null);
        recentPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#DDDDDD")));
        dashboardPanel.add(recentPanel);

        lblRecent = new JLabel("Active Reservations");
        lblRecent.setBounds(15, 10, 400, 30);
        lblRecent.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblRecent.setForeground(Color.decode("#333333"));
        recentPanel.add(lblRecent);

        String[] columns = {"Res ID", "Guest Name", "Room #", "Room Type", "Status", "Check-In", "Final Total"};
        reservationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReservations = new JTable(reservationTableModel);
        tblReservations.setRowHeight(26);
        tblReservations.setFont(new Font("Arial", Font.PLAIN, 13));
        tblReservations.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 13));
        tblReservations.getTableHeader().setBackground(Color.decode("#222222"));
        tblReservations.getTableHeader().setForeground(Color.WHITE);
        tblReservations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblReservations.getSelectionModel().addListSelectionListener(e -> onRowSelected());

        JScrollPane scrollPane = new JScrollPane(tblReservations);
        scrollPane.setBounds(10, 50, 840, 210);
        recentPanel.add(scrollPane);

        lblTotalActive = new JLabel("Active reservations: 0");
        lblTotalActive.setBounds(10, 270, 300, 25);
        lblTotalActive.setFont(new Font("Arial Black", Font.BOLD, 13));
        lblTotalActive.setForeground(Color.decode("#5A3FB8"));
        recentPanel.add(lblTotalActive);

        btnRefresh = new JButton("Refresh Data");
        btnRefresh.setBounds(720, 267, 130, 30);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(Color.decode("#222222"));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(this);
        recentPanel.add(btnRefresh);

        // ----- Cancellation details panel -----
        detailsPanel = new JPanel();
        detailsPanel.setBounds(20, 355, 860, 145);
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setLayout(null);
        detailsPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#DDDDDD")));
        dashboardPanel.add(detailsPanel);

        JLabel lblDetailsTitle = new JLabel("Cancel Selected Reservation");
        lblDetailsTitle.setBounds(15, 10, 400, 25);
        lblDetailsTitle.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblDetailsTitle.setForeground(Color.decode("#333333"));
        detailsPanel.add(lblDetailsTitle);

        lblSelected = new JLabel("Selected Reservation:");
        lblSelected.setBounds(15, 45, 160, 25);
        lblSelected.setFont(new Font("Arial", Font.BOLD, 13));
        detailsPanel.add(lblSelected);

        lblSelectedValue = new JLabel("None selected");
        lblSelectedValue.setBounds(180, 45, 500, 25);
        lblSelectedValue.setFont(new Font("Arial", Font.PLAIN, 13));
        detailsPanel.add(lblSelectedValue);

        lblReasonCaption = new JLabel("Cancellation Reason:");
        lblReasonCaption.setBounds(15, 78, 160, 25);
        lblReasonCaption.setFont(new Font("Arial", Font.BOLD, 13));
        detailsPanel.add(lblReasonCaption);

        txtReason = new JTextField();
        txtReason.setBounds(180, 78, 500, 30);
        txtReason.setFont(new Font("Arial", Font.PLAIN, 13));
        detailsPanel.add(txtReason);

        lblRefundNote = new JLabel("Refund: more than 7 days before check-in = full refund, "
                + "exactly 7 days = 90%, less than 7 days = no refund.");
        lblRefundNote.setBounds(15, 113, 700, 22);
        lblRefundNote.setFont(new Font("Arial", Font.ITALIC, 11));
        lblRefundNote.setForeground(Color.GRAY);
        detailsPanel.add(lblRefundNote);

        btnCancelSelected = new JButton("CANCEL BOOKING");
        btnCancelSelected.setBounds(700, 75, 150, 40);
        btnCancelSelected.setFont(new Font("Arial Black", Font.BOLD, 13));
        btnCancelSelected.setBackground(Color.decode("#C0392B"));
        btnCancelSelected.setForeground(Color.WHITE);
        btnCancelSelected.setBorderPainted(false);
        btnCancelSelected.setFocusPainted(false);
        btnCancelSelected.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelSelected.addActionListener(this);
        detailsPanel.add(btnCancelSelected);
    }

    /**
     * Loads all non-cancelled reservations from the database via ReservationService
     * and populates the table. Guest names and room numbers are resolved through
     * GuestService / RoomService since the Reservation model only stores IDs.
     */
    private void loadReservations() {
        reservationTableModel.setRowCount(0);
        rowToReservationId.clear();
        selectedReservationId = -1;
        lblSelectedValue.setText("None selected");
        txtReason.setText("");

        List<Reservation> reservations;
        try {
            reservations = reservationService.getAllReservations();
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to load reservations: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            lblTotalActive.setText("Active reservations: 0");
            return;
        }

        List<Reservation> active = new ArrayList<>();
        for (Reservation r : reservations) {
            if (!r.isCancelled() && !r.isCheckedOut()) {
                active.add(r);
            }
        }

        int row = 0;
        for (Reservation r : active) {
            String guestName = resolveGuestName(r.getGuestId());
            String roomNumber = "Room " + r.getRoomId();
            String roomType = "";

            try {
                Room room = roomService.getRoomById(r.getRoomId());
                if (room != null) {
                    roomNumber = room.getRoomNumber();
                    roomType = room.getRoomType();
                }
            } catch (HotelException ignored) {
                // fall back to defaults above if the room lookup fails
            }

            String finalTotal = r.getFinalTotal() != null
                    ? CurrencyUtil.formatCurrency(r.getFinalTotal())
                    : "N/A";

            reservationTableModel.addRow(new Object[]{
                    r.getReservationId(),
                    guestName,
                    roomNumber,
                    roomType,
                    r.getReservationStatus(),
                    r.getCheckInDate(),
                    finalTotal
            });

            rowToReservationId.put(row, r.getReservationId());
            row++;
        }

        lblTotalActive.setText("Active reservations: " + active.size());
    }

    private String resolveGuestName(int guestId) {
        try {
            Guest guest = guestService.getGuestById(guestId);
            if (guest != null) {
                return guest.getFullName();
            }
        } catch (HotelException ignored) {
            // fall through to default below
        }
        return "Guest #" + guestId;
    }

    private void onRowSelected() {
        int viewRow = tblReservations.getSelectedRow();
        if (viewRow < 0) {
            selectedReservationId = -1;
            lblSelectedValue.setText("None selected");
            return;
        }
        int modelRow = tblReservations.convertRowIndexToModel(viewRow);
        Integer reservationId = rowToReservationId.get(modelRow);
        if (reservationId == null) {
            selectedReservationId = -1;
            lblSelectedValue.setText("None selected");
            return;
        }
        selectedReservationId = reservationId;
        String guestName = String.valueOf(reservationTableModel.getValueAt(modelRow, 1));
        String roomNumber = String.valueOf(reservationTableModel.getValueAt(modelRow, 2));
        lblSelectedValue.setText("Res #" + reservationId + " - " + guestName + " - " + roomNumber);
    }

    private void cancelSelectedReservation() {
        if (selectedReservationId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a reservation from the table first.",
                    "No Reservation Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String reason = txtReason.getText().trim();
        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter a reason for the cancellation.",
                    "Reason Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Cancel reservation #" + selectedReservationId + "?\nThis action cannot be undone.",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            BigDecimal refund = reservationService.cancelReservation(selectedReservationId, reason);
            JOptionPane.showMessageDialog(this,
                    "Reservation #" + selectedReservationId + " has been cancelled.\n"
                            + "Refund due to guest: " + CurrencyUtil.formatCurrency(refund),
                    "Cancellation Successful", JOptionPane.INFORMATION_MESSAGE);
            loadReservations();
        } catch (HotelException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not cancel reservation: " + e.getMessage(),
                    "Cancellation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnHomePage) {
            dispose();
            new HomePage().setVisible(true);
        } else if (e.getSource() == btnSideRegister) {
            dispose();
            new RegisterGuestPanel().setVisible(true);
        } else if (e.getSource() == btnSideRoomBooking) {
            dispose();
            new RoomBookingPanel().setVisible(true);
        } else if (e.getSource() == btnSidePay) {
            dispose();
            new RecordPaymentPanel().setVisible(true);
        } else if (e.getSource() == btnSideReserve) {
            dispose();
            new ViewReservationPanel().setVisible(true);
        } else if (e.getSource() == btnSideDash) {
            dispose();
            new ReceptionistDashboard().setVisible(true);
        } else if (e.getSource() == btnRefresh) {
            loadReservations();
        } else if (e.getSource() == btnCancelSelected) {
            cancelSelectedReservation();
        }
    }
}