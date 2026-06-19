package GUIGuest;

import javax.swing.*;
import javax.swing.JScrollBar;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.temporal.*;

public class CancelReservation extends JFrame implements ActionListener {

    private JLabel lblRefundInfo, lblOtherReason;
    private JComboBox<String> cmbReservations, cmbReason;
    private JTextArea areaPolicy, areaOtherReason;
    private JScrollPane otherReasonScroll;
    private JButton btnProcess;
    private JPanel contentArea, card;

    CancelReservation() {
        setTitle("Hotel Guest System - Cancel Reservation");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        // ===================== SIDEBAR =====================
        JPanel sidebar = new JPanel(null);
        sidebar.setBounds(0, 0, 250, 700);
        sidebar.setBackground(Color.decode("#222222"));
        add(sidebar);

        JLabel lblHotel = new JLabel("HOTEL");
        lblHotel.setBounds(10, 10, 230, 50);
        lblHotel.setFont(new Font("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidebar.add(lblHotel);

        JLabel lblPortal = new JLabel("GUEST PORTAL");
        lblPortal.setBounds(10, 50, 230, 40);
        lblPortal.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblPortal.setForeground(Color.WHITE);
        sidebar.add(lblPortal);

        sidebar.add(makeSideBtn("Search Rooms",       160, "Cancel Reservation", this, () -> openFrame(new SearchRooms())));
        sidebar.add(makeSideBtn("Make Reservation",   230, "Cancel Reservation", this, () -> openFrame(new MakeReservation())));
        sidebar.add(makeSideBtn("View Reservations",  300, "Cancel Reservation", this, () -> openFrame(new ViewReservations())));
        sidebar.add(makeSideBtn("Cancel Reservation", 370, "Cancel Reservation", this, () -> openFrame(new CancelReservation())));
        sidebar.add(makeSideBtn("Guest Profile",      440, "Cancel Reservation", this, () -> openFrame(new GuestProfile())));
        sidebar.add(makeSideBtn("Logout",             610, "Cancel Reservation", this, () -> {
            int c = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                // TODO: DB CONNECT [LOGOUT] - SessionManager.clearSession()
                // SessionManager.clearSession();
                // new LoginFrame().setVisible(true);
                dispose();
            }
        }));

        contentArea = new JPanel(null);
        contentArea.setBounds(250, 0, 950, 700);
        contentArea.setBackground(Color.decode("#F5F5F5"));
        add(contentArea);

        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 950, 80);
        titleBar.setBackground(Color.decode("#222222"));
        contentArea.add(titleBar);

        JLabel lblTitle = new JLabel("CANCEL BOOKING");
        lblTitle.setBounds(20, 15, 600, 50);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);

        JPanel formContent = new JPanel(null);
        formContent.setBackground(Color.decode("#F5F5F5"));
        formContent.setPreferredSize(new Dimension(880, 580));

        card = new JPanel(null);
        card.setBounds(30, 10, 560, 540);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        formContent.add(card);

        JScrollPane mainScroll = new JScrollPane(formContent);
        mainScroll.setBounds(0, 85, 950, 615);
        mainScroll.setBorder(null);
        mainScroll.setBackground(Color.decode("#F5F5F5"));
        mainScroll.getViewport().setBackground(Color.decode("#F5F5F5"));
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScroll.getVerticalScrollBar().setUnitIncrement(12);

        JScrollBar vBar = mainScroll.getVerticalScrollBar();
        vBar.setBackground(Color.decode("#222222"));
        vBar.setForeground(Color.decode("#555555"));
        vBar.setPreferredSize(new Dimension(8, 0));
        vBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.decode("#555555");
                this.trackColor = Color.decode("#222222");
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
        });
        contentArea.add(mainScroll);

        JLabel lblSelect = new JLabel("Select Reservation:");
        lblSelect.setBounds(25, 20, 200, 25);
        lblSelect.setFont(new Font("Arial Black", Font.BOLD, 14));
        card.add(lblSelect);

        // TODO: DB CONNECT [LOAD DROPDOWN] - ReservationDAO.getActiveReservationsByGuestId(guestId)
        // TABLE: reservations JOIN rooms ON reservations.room_id = rooms.room_id
        // Query: SELECT reservation_id, rooms.room_number, check_in_date, check_out_date
        //        FROM reservations JOIN rooms ON reservations.room_id = rooms.room_id
        //        WHERE guest_id = SessionManager.getCurrentGuestId()
        //        AND reservation_status = 'Confirmed'
        //        ORDER BY check_in_date ASC
        // Populate: cmbReservations with "RES#1 - Room 101 (2026-07-10 to 2026-07-15)"
        cmbReservations = new JComboBox<>(new String[]{"- Select Reservation -"});
        cmbReservations.setBounds(25, 50, 490, 35);
        cmbReservations.setFont(new Font("Arial", Font.PLAIN, 13));
        card.add(cmbReservations);

        JLabel lblPolicy = new JLabel("Cancellation Policy:");
        lblPolicy.setBounds(25, 100, 200, 25);
        lblPolicy.setFont(new Font("Arial Black", Font.BOLD, 14));
        card.add(lblPolicy);

        areaPolicy = new JTextArea(
            "  > More than 7 days before check-in: 100% Full Refund\n" +
            "  > Exactly 7 days before check-in:   90% Refund\n" +
            "  > Less than 7 days before check-in: No Refund\n" +
            "  > After check-in:                   No Refund"
        );
        areaPolicy.setBounds(25, 130, 490, 80);
        areaPolicy.setEditable(false);
        areaPolicy.setBackground(Color.decode("#F5F5F5"));
        areaPolicy.setFont(new Font("Courier New", Font.PLAIN, 12));
        areaPolicy.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        card.add(areaPolicy);

        lblRefundInfo = new JLabel("Estimated Refund: Select a reservation to calculate.");
        lblRefundInfo.setBounds(25, 222, 490, 22);
        lblRefundInfo.setFont(new Font("Arial Black", Font.BOLD, 12));
        card.add(lblRefundInfo);

        JLabel lblReason = new JLabel("Cancellation Reason:");
        lblReason.setBounds(25, 258, 220, 25);
        lblReason.setFont(new Font("Arial Black", Font.BOLD, 14));
        card.add(lblReason);

        cmbReason = new JComboBox<>(new String[]{
            "- Select Reason -", "Change of Plans", "Emergency", "Health", "Work", "Other"
        });
        cmbReason.setBounds(25, 288, 300, 35);
        cmbReason.setFont(new Font("Arial", Font.PLAIN, 13));
        card.add(cmbReason);

        lblOtherReason = new JLabel("Please specify your reason:");
        lblOtherReason.setBounds(25, 328, 300, 22);
        lblOtherReason.setFont(new Font("Arial Black", Font.BOLD, 12));
        lblOtherReason.setVisible(false);
        card.add(lblOtherReason);

        areaOtherReason = new JTextArea();
        areaOtherReason.setLineWrap(true);
        areaOtherReason.setWrapStyleWord(true);

        otherReasonScroll = new JScrollPane(areaOtherReason);
        otherReasonScroll.setBounds(25, 353, 490, 60);
        otherReasonScroll.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        otherReasonScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        otherReasonScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        otherReasonScroll.setVisible(false);
        card.add(otherReasonScroll);

        btnProcess = new JButton("CONFIRM CANCELLATION");
        btnProcess.setBounds(25, 428, 250, 45);
        btnProcess.setBackground(Color.decode("#222222"));
        btnProcess.setForeground(Color.WHITE);
        btnProcess.setFont(new Font("Arial Black", Font.BOLD, 14));
        btnProcess.setBorderPainted(false);
        btnProcess.setFocusPainted(false);
        card.add(btnProcess);

        cmbReservations.addActionListener(this);
        cmbReason.addActionListener(this);
        btnProcess.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cmbReservations) {
            if (cmbReservations.getSelectedIndex() == 0) {
                lblRefundInfo.setText("Estimated Refund: Select a reservation to calculate.");
                return;
            }
            // TODO: DB CONNECT [REFUND ESTIMATE] - get check_in_date and final_total from reservations
            // Replace hardcoded values in updateRefundEstimate() with real DB data
            updateRefundEstimate();
        }

        if (e.getSource() == cmbReason) {
            boolean isOther = cmbReason.getSelectedItem().toString().equals("Other");
            lblOtherReason.setVisible(isOther);
            otherReasonScroll.setVisible(isOther);
            card.revalidate();
            card.repaint();
        }

        if (e.getSource() == btnProcess) {
            if (cmbReservations.getSelectedIndex() == 0) { JOptionPane.showMessageDialog(this, "Error: Please select a reservation to cancel.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (cmbReason.getSelectedIndex() == 0) { JOptionPane.showMessageDialog(this, "Error: Please select a cancellation reason.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (cmbReason.getSelectedItem().toString().equals("Other") && areaOtherReason.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: Please specify your cancellation reason.", "Error", JOptionPane.ERROR_MESSAGE); return;
            }

            String refundMsg = lblRefundInfo.getText();
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this reservation?\n" + refundMsg,
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: DB CONNECT [CONFIRM CANCELLATION] - ReservationDAO.cancelReservation(reservationId, reason)
                // TABLE: reservations
                // UPDATE reservations SET reservation_status = 'Cancelled',
                //   cancellation_reason = ?, cancelled_date = NOW(), updated_at = NOW()
                // WHERE reservation_id = <selected reservation id>
                //
                // TABLE: payments (if refund applicable)
                // UPDATE payments SET refund_amount = <calculated refund>,
                //   refund_date = NOW(), payment_status = 'Refunded',
                //   refund_reason = <cancellation_reason>
                // WHERE reservation_id = <reservation_id>
                // NOTE: Use consistent status strings → 'Pending', 'Paid', 'Refunded', 'No Refund'
                //   Set 'Refunded' only if refund_amount > 0, otherwise set 'No Refund'
                //
                // After success: reload cmbReservations, show success message
                System.out.println("Cancellation confirmed. Proceeding with ReservationDAO update...");
            }
        }
    }

    private void updateRefundEstimate() {
        // TODO: DB CONNECT - Replace hardcoded values with real DB data
        // checkInDate     → reservations.check_in_date WHERE reservation_id = selected
        // reservationTotal → reservations.final_total WHERE reservation_id = selected
        LocalDate checkInDate = LocalDate.now().plusDays(10);
        long daysUntilCheckIn = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), checkInDate);
        double reservationTotal = 28000.00;
        double refund;
        String refundLabel;
        if (daysUntilCheckIn > 7)      { refund = reservationTotal;        refundLabel = "100% Full Refund"; }
        else if (daysUntilCheckIn == 7) { refund = reservationTotal * 0.90; refundLabel = "90% Refund"; }
        else                            { refund = 0;                        refundLabel = "No Refund"; }
        lblRefundInfo.setText(String.format("Estimated Refund: PHP %,.2f (%s)", refund, refundLabel));
    }

    private JButton makeSideBtn(String text, int y, String active, JFrame frame, Runnable action) {
        JButton btn = new JButton(text);
        btn.setBounds(0, y, 250, 50);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial Black", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        boolean isActive = text.equals(active);
        btn.setBackground(isActive ? Color.decode("#444444") : Color.decode("#222222"));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (!isActive) { btn.setBackground(Color.WHITE); btn.setForeground(Color.BLACK); }
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(isActive ? Color.decode("#444444") : Color.decode("#222222"));
                btn.setForeground(Color.WHITE);
            }
        });
        btn.addActionListener(ev -> { if (!isActive) action.run(); });
        return btn;
    }

    private void openFrame(JFrame next) {
        next.setVisible(true);
        dispose();
    }

}