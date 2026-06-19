package GUIReceptionist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.*;

public class RecordPaymentPanel extends JFrame implements ActionListener {

    private JButton btnHomePage, btnSideDash, btnSideRegister, btnSideRoomBooking, btnSidePay, btnSideReserve;
    private JLabel lblReception, lblHotel, lblManagement, lblDate, lblGuestIdVal, lblReservationIdVal;
    private JPanel sidePan, topPan, mainScroll, leftPanel;
    private JScrollPane scrollPane;

    private JButton btnSearch;
    private JTextField txtSearch;

    private JTextField txtResID, txtGuest, txtRoom, txtRoomType,
            txtCheckIn, txtCheckOut, txtNights, txtPricePerNight;
    private JLabel lblRoomTotal;

    private JComboBox<String> cmbMethod;
    private JLabel lblCash, lblCard, lblExpiry, lblCVV, lblPhone, lblOTP;
    private JTextField txtCash, txtCard, txtExpiry, txtCVV, txtPhone, txtOTP;

    private JPanel receiptOverlay;
    private JTextArea txtReceipt;
    private JButton btnPrintReceipt;

    // Booking data (passed in from RoomBookingPanel, or filled in via search)
    private String reservationId = "";
    private String guestName = "";
    private String roomNo = "";
    private String roomType = "";
    private String checkIn = "";
    private String checkOut = "";
    private int numGuests = 0;
    private long numberOfNights = 0;
    private double pricePerNight = 0.0;
    private double roomTotal = 0.0;
    private String addOnsSummary = "None";
    private double addOnsTotal = 0.0;
    private double grandTotal = 0.0;


    // Default constructor - opened from sidebar without booking data
    public RecordPaymentPanel() {
        this("", "", "", "", "", "", 0, 0, 0.0, 0.0, "None", 0.0, 0.0);
    }

    // Full constructor - called from RoomBookingPanel with all data
    public RecordPaymentPanel(String reservationId, String guestName,
                              String roomNo, String roomType,
                              String checkIn, String checkOut,
                              int numGuests, long numberOfNights,
                              double pricePerNight, double roomTotal,
                              String addOnsSummary, double addOnsTotal,
                              double grandTotal) {

        this.reservationId  = reservationId;
        this.guestName      = guestName;
        this.roomNo         = roomNo;
        this.roomType       = roomType;
        this.checkIn        = checkIn;
        this.checkOut       = checkOut;
        this.numGuests      = numGuests;
        this.numberOfNights = numberOfNights;
        this.pricePerNight  = pricePerNight;
        this.roomTotal      = roomTotal;
        this.addOnsSummary  = addOnsSummary == null ? "None" : addOnsSummary;
        this.addOnsTotal    = addOnsTotal;
        this.grandTotal     = grandTotal;

        sidePanel();
        functionMenu();
        topPanel();
        mainContent();
        prefillBookingData();

        setSize(1200, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Hotel Reservation System - Receptionist Payment");
    }

    private void functionMenu(){

        lblReception = new JLabel ("PAYMENT");
        lblReception.setBounds(850, 0, 500, 60);
        lblReception.setFont(new Font("Arial Black", Font.BOLD, 50));
        add(lblReception);
    }

    private void sidePanel() {
        sidePan = new JPanel ();
        sidePan.setBounds (0, 0, 300, 800);
        sidePan.setLayout(null);
        sidePan.setBackground(Color.decode("#222222"));
        add(sidePan);

        lblHotel = new JLabel ("HOTEL");
        lblHotel.setBounds(10, 10, 800, 50);
        lblHotel.setFont(new Font ("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidePan.add(lblHotel);

        lblManagement = new JLabel ("MANAGEMENT");
        lblManagement.setBounds(10, 50, 800, 50);
        lblManagement.setFont(new Font ("Arial Black", Font.BOLD, 30));
        lblManagement.setForeground(Color.WHITE);
        sidePan.add(lblManagement);

        btnHomePage = new JButton ("Home Page");
        btnHomePage.setBounds(0, 160, 300, 50);
        btnHomePage.setBackground(Color.decode("#222222"));
        btnHomePage.setForeground(Color.WHITE);
        btnHomePage.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnHomePage.setBorderPainted(false);
        btnHomePage.setFocusPainted(false);
        btnHomePage.setBorder(null);
        btnHomePage.addActionListener(this);
        sidePan.add(btnHomePage);

        btnSideDash = new JButton ("Dashboard");
        btnSideDash.setBounds(0, 230, 300, 50);
        btnSideDash.setBackground(Color.decode("#222222"));
        btnSideDash.setForeground(Color.WHITE);
        btnSideDash.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideDash.setBorderPainted(false);
        btnSideDash.setFocusPainted(false);
        btnSideDash.setBorder(null);
        btnSideDash.addActionListener(this);
        sidePan.add(btnSideDash);

        btnSideRegister = new JButton ("Guest Register");
        btnSideRegister.setBounds(0, 300, 300, 50);
        btnSideRegister.setBackground(Color.decode("#222222"));
        btnSideRegister.setForeground(Color.WHITE);
        btnSideRegister.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideRegister.setBorderPainted(false);
        btnSideRegister.setFocusPainted(false);
        btnSideRegister.setBorder(null);
        btnSideRegister.addActionListener(this);
        sidePan.add(btnSideRegister);

        btnSideRoomBooking = new JButton ("Room Booking");
        btnSideRoomBooking.setBounds(0, 370, 300, 50);
        btnSideRoomBooking.setBackground(Color.decode("#222222"));
        btnSideRoomBooking.setForeground(Color.WHITE);
        btnSideRoomBooking.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideRoomBooking.setBorderPainted(false);
        btnSideRoomBooking.setFocusPainted(false);
        btnSideRoomBooking.setBorder(null);
        btnSideRoomBooking.addActionListener(this);
        sidePan.add(btnSideRoomBooking);

        btnSidePay = new JButton ("Payment");
        btnSidePay.setBounds(0, 440, 300, 50);
        btnSidePay.setBackground(Color.decode("#FFFFFF"));
        btnSidePay.setForeground(Color.BLACK);
        btnSidePay.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSidePay.setBorderPainted(false);
        btnSidePay.setFocusPainted(false);
        btnSidePay.setBorder(null);
        btnSidePay.addActionListener(this);
        sidePan.add(btnSidePay);

        btnSideReserve = new JButton ("Reservation");
        btnSideReserve.setBounds(0, 510, 300, 50);;
        btnSideReserve.setBackground(Color.decode("#222222"));
        btnSideReserve.setForeground(Color.WHITE);
        btnSideReserve.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideReserve.setBorderPainted(false);
        btnSideReserve.setFocusPainted(false);
        btnSideReserve.setBorder(null);
        btnSideReserve.addActionListener(this);
        sidePan.add(btnSideReserve);

    }

    private void topPanel() {
        topPan = new JPanel ();
        topPan.setBounds (300, 0, 900, 60);
        topPan.setLayout(null);
        topPan.setBackground(Color.decode("#FFFFFF"));
        add(topPan);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        lblDate = new JLabel(dateFormat.format(new Date()));
        lblDate.setBounds(20, 15, 300, 30);
        lblDate.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblDate.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblDate);

        JLabel lblGuestIdLabel = new JLabel("Guest ID:");
        lblGuestIdLabel.setBounds(320, 10, 100, 20);
        lblGuestIdLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        lblGuestIdLabel.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblGuestIdLabel);

        lblGuestIdVal = new JLabel("—");
        lblGuestIdVal.setBounds(400, 10, 120, 20);
        lblGuestIdVal.setFont(new Font("Arial", Font.PLAIN, 12));
        lblGuestIdVal.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblGuestIdVal);

        JLabel lblResIdLabel = new JLabel("Reservation ID:");
        lblResIdLabel.setBounds(320, 35, 130, 20);
        lblResIdLabel.setFont(new Font("Arial Black", Font.BOLD, 12));
        lblResIdLabel.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblResIdLabel);

        lblReservationIdVal = new JLabel("—");
        lblReservationIdVal.setBounds(440, 35, 60, 20);
        lblReservationIdVal.setFont(new Font("Arial", Font.PLAIN, 12));
        lblReservationIdVal.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblReservationIdVal);
    }

    private void mainContent() {

        // Scrollable container for everything below the top panel
        mainScroll = new JPanel();
        mainScroll.setLayout(null);
        mainScroll.setPreferredSize(new Dimension(900, 650));
        mainScroll.setBackground(Color.decode("#F5F5F5"));

        scrollPane = new JScrollPane(mainScroll);
        scrollPane.setBounds(300, 70, 900, 630);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.getViewport().setBackground(Color.decode("#F0F0F0"));
        add(scrollPane);

        leftPanel = new JPanel(null);
        leftPanel.setBounds(10, 10, 620, 580);
        leftPanel.setBackground(Color.decode("#F5F5F5"));
        mainScroll.add(leftPanel);

        searchCard();
        bookingInfoCard();
        paymentDetailsCard();
        grandTotalCard();
        receiptOverlayPanel();
    }

    private void searchCard() {
        JPanel searchCard = makeCard(10, 0, 880, 65);
        leftPanel.add(searchCard);

        JLabel lblSearch = new JLabel("Search Reservation ID:");
        lblSearch.setBounds(15, 10, 200, 18);
        lblSearch.setFont(new Font("Arial Black", Font.BOLD, 11));
        searchCard.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(15, 30, 320, 26);
        txtSearch.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        searchCard.add(txtSearch);

        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(345, 30, 100, 26);
        styleBtn(btnSearch);
        btnSearch.addActionListener(this);
        searchCard.add(btnSearch);
    }

    private void bookingInfoCard() {
        JPanel infoCard = makeCard(10, 70, 880, 145);
        leftPanel.add(infoCard);

        JLabel secInfo = new JLabel("BOOKING INFORMATION");
        secInfo.setBounds(15, 8, 350, 18);
        secInfo.setFont(new Font("Arial Black", Font.BOLD, 12));
        infoCard.add(secInfo);

        // Row 1: Res ID | Guest Name
        makeInfoLabel("Reservation ID:", 15, 32, infoCard);
        txtResID = makeInfoField(130, 30, 140, infoCard);

        makeInfoLabel("Guest Name:", 300, 32, infoCard);
        txtGuest = makeInfoField(400, 30, 160, infoCard);

        // Row 2: Room No | Room Type
        makeInfoLabel("Room No.:", 15, 62, infoCard);
        txtRoom = makeInfoField(100, 60, 90, infoCard);

        makeInfoLabel("Room Type:", 220, 62, infoCard);
        txtRoomType = makeInfoField(320, 60, 160, infoCard);

        // Row 3: Check-in | Check-out | Nights
        makeInfoLabel("Check-in:", 15, 92, infoCard);
        txtCheckIn = makeInfoField(95, 90, 120, infoCard);

        makeInfoLabel("Check-out:", 240, 92, infoCard);
        txtCheckOut = makeInfoField(325, 90, 120, infoCard);

        makeInfoLabel("Nights:", 470, 92, infoCard);
        txtNights = makeInfoField(530, 90, 55, infoCard);

        // Row 4: Rate/Night | Room Total
        makeInfoLabel("Rate/Night:", 15, 120, infoCard);
        txtPricePerNight = makeInfoField(105, 118, 130, infoCard);

        lblRoomTotal = new JLabel("Room Total: PHP 0.00");
        lblRoomTotal.setBounds(280, 118, 400, 22);
        lblRoomTotal.setFont(new Font("Arial Black", Font.BOLD, 13));
        lblRoomTotal.setForeground(Color.decode("#5A3FB8"));
        infoCard.add(lblRoomTotal);
    }

    private void paymentDetailsCard() {
        JPanel payCard = makeCard(10, 220, 880, 270);
        leftPanel.add(payCard);

        JLabel secPay = new JLabel("PAYMENT DETAILS");
        secPay.setBounds(15, 10, 300, 20);
        secPay.setFont(new Font("Arial Black", Font.BOLD, 13));
        payCard.add(secPay);

        JLabel lblPayMethod = new JLabel("Payment Method:");
        lblPayMethod.setBounds(15, 38, 150, 18);
        lblPayMethod.setFont(new Font("Arial Black", Font.BOLD, 11));
        payCard.add(lblPayMethod);

        cmbMethod = new JComboBox<>(new String[]{"- Select -", "Cash", "Credit Card", "E-Wallet"});
        cmbMethod.setBounds(15, 58, 200, 28);
        cmbMethod.addActionListener(this);
        payCard.add(cmbMethod);

        // Cash fields
        lblCash = makeLabel("Cash Amount:", 15, 103);
        payCard.add(lblCash);
        txtCash = new JTextField();
        txtCash.setBounds(15, 121, 180, 28);
        txtCash.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payCard.add(txtCash);

        // Credit Card fields
        lblCard = makeLabel("Card Number (13-19 digits):", 15, 103);
        payCard.add(lblCard);
        txtCard = new JTextField();
        txtCard.setBounds(15, 121, 340, 28);
        txtCard.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payCard.add(txtCard);

        lblExpiry = makeLabel("Expiry (MM/YY):", 15, 160);
        payCard.add(lblExpiry);
        txtExpiry = new JTextField();
        txtExpiry.setBounds(15, 178, 130, 28);
        txtExpiry.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payCard.add(txtExpiry);

        lblCVV = makeLabel("CVV:", 165, 160);
        payCard.add(lblCVV);
        txtCVV = new JTextField();
        txtCVV.setBounds(165, 178, 80, 28);
        txtCVV.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payCard.add(txtCVV);

        // E-Wallet fields
        lblPhone = makeLabel("Phone (09XXXXXXXXX or +639XXXXXXXXX):", 15, 103);
        payCard.add(lblPhone);
        txtPhone = new JTextField();
        txtPhone.setBounds(15, 121, 280, 28);
        txtPhone.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payCard.add(txtPhone);

        lblOTP = makeLabel("OTP (6-digit code):", 15, 160);
        payCard.add(lblOTP);
        txtOTP = new JTextField();
        txtOTP.setBounds(15, 178, 140, 28);
        txtOTP.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payCard.add(txtOTP);

        JButton btnProcess = new JButton("PROCESS PAYMENT");
        btnProcess.setBounds(330, 218, 220, 38);
        styleBtn(btnProcess);
        btnProcess.addActionListener(ev -> processPayment());
        payCard.add(btnProcess);

        hideAllPayFields();
    }

    private void grandTotalCard() {
        JPanel totalCard = makeCard(10, 495, 880, 60);
        leftPanel.add(totalCard);

        JLabel lblGTCaption = new JLabel("GRAND TOTAL:");
        lblGTCaption.setBounds(15, 18, 200, 24);
        lblGTCaption.setFont(new Font("Arial Black", Font.BOLD, 16));
        totalCard.add(lblGTCaption);

        JLabel lblGTValue = new JLabel(String.format("PHP %,.2f", grandTotal));
        lblGTValue.setName("grandTotalDisplay");
        lblGTValue.setBounds(200, 18, 300, 24);
        lblGTValue.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblGTValue.setForeground(Color.decode("#5A3FB8"));
        totalCard.add(lblGTValue);
    }

    private void receiptOverlayPanel() {
        receiptOverlay = new JPanel(null);
        receiptOverlay.setBounds(650, 10, 230, 500);
        receiptOverlay.setBackground(new Color(110, 110, 110));
        mainScroll.add(receiptOverlay);

        JLabel lblReceiptTitle = new JLabel("RECEIPT");
        lblReceiptTitle.setBounds(75, 10, 120, 25);
        lblReceiptTitle.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblReceiptTitle.setForeground(Color.WHITE);
        receiptOverlay.add(lblReceiptTitle);

        txtReceipt = new JTextArea();
        txtReceipt.setEditable(false);
        txtReceipt.setBackground(new Color(110, 110, 110));
        txtReceipt.setForeground(Color.WHITE);
        txtReceipt.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtReceipt.setLineWrap(true);
        txtReceipt.setWrapStyleWord(true);

        JScrollPane receiptScroll = new JScrollPane(txtReceipt);
        receiptScroll.setBounds(10, 50, 210, 360);
        receiptOverlay.add(receiptScroll);

        btnPrintReceipt = new JButton("PRINT RECEIPT");
        btnPrintReceipt.setBounds(25, 430, 180, 40);
        btnPrintReceipt.setBackground(Color.BLACK);
        btnPrintReceipt.setForeground(Color.RED);
        btnPrintReceipt.setFont(new Font("Arial Black", Font.BOLD, 11));
        btnPrintReceipt.setFocusPainted(false);
        btnPrintReceipt.addActionListener(this);
        receiptOverlay.add(btnPrintReceipt);
    }

    // Pre-fill from constructor data (when opened from RoomBookingPanel)
    private void prefillBookingData() {
        if (reservationId.isEmpty()) return;

        txtResID.setText(reservationId);
        txtGuest.setText(guestName);
        txtRoom.setText(roomNo);
        txtRoomType.setText(roomType);
        txtCheckIn.setText(checkIn);
        txtCheckOut.setText(checkOut);
        txtNights.setText(String.valueOf(numberOfNights));
        txtPricePerNight.setText(String.format("PHP %,.2f", pricePerNight));
        lblRoomTotal.setText("Room Total:  PHP " + String.format("%,.2f", roomTotal));

        lblGuestIdVal.setText(guestName);
        lblReservationIdVal.setText(reservationId);

        updateGrandTotalDisplay();
    }

    private void findAndFillGrandTotal(Component c) {
        if (c instanceof JScrollPane) {
            findAndFillGrandTotal(((JScrollPane) c).getViewport().getView());
        }
        if (c instanceof JPanel) {
            for (Component child : ((JPanel) c).getComponents()) findAndFillGrandTotal(child);
        }
        if (c instanceof JLabel) {
            JLabel lbl = (JLabel) c;
            if ("grandTotalDisplay".equals(lbl.getName()))
                lbl.setText(String.format("PHP %,.2f", grandTotal));
        }
    }

    private void updateGrandTotalDisplay() {
        for (Component c : getContentPane().getComponents()) findAndFillGrandTotal(c);
    }

    // Payment processing
    private void processPayment() {
        if (txtResID.getText().trim().isEmpty()) {
            showError("No reservation loaded. Search for a reservation ID first.");
            return;
        }
        if (grandTotal <= 0) {
            showError("Grand total is zero. Ensure booking data is loaded.");
            return;
        }

        String method = (String) cmbMethod.getSelectedItem();
        if (method.equals("- Select -")) {
            showError("Please select a payment method.");
            return;
        }

        double changeAmount  = 0.0;
        double cashReceived  = 0.0;
        String paymentDetail = "";

        if (method.equals("Cash")) {
            String cashText = txtCash.getText().trim();
            if (cashText.isEmpty()) { showError("Please enter the cash amount."); return; }
            try { cashReceived = Double.parseDouble(cashText); }
            catch (NumberFormatException ex) { showError("Enter a valid cash amount."); return; }
            if (cashReceived < grandTotal) {
                showError(String.format("Insufficient cash. Amount must be at least PHP %,.2f.", grandTotal));
                return;
            }
            changeAmount  = cashReceived - grandTotal;
            paymentDetail = "Cash";
        }

        if (method.equals("Credit Card")) {
            String cardNum = txtCard.getText().trim();
            String expiry  = txtExpiry.getText().trim();
            String cvv     = txtCVV.getText().trim();

            if (cardNum.isEmpty()) { showError("Card number cannot be empty."); return; }
            if (cardNum.length() < 13 || cardNum.length() > 19) { showError("Card number must be 13-19 digits."); return; }
            for (char ch : cardNum.toCharArray())
                if (!Character.isDigit(ch)) { showError("Card number must contain digits only."); return; }
            if (!passesLuhn(cardNum)) { showError("Invalid card number (failed Luhn check)."); return; }
            if (expiry.isEmpty() || !expiry.contains("/")) { showError("Expiry must be MM/YY format."); return; }
            if (isCardExpired(expiry)) { showError("This card has expired."); return; }
            if (cvv.isEmpty() || cvv.length() < 3 || cvv.length() > 4) { showError("CVV must be 3 or 4 digits."); return; }
            for (char ch : cvv.toCharArray())
                if (!Character.isDigit(ch)) { showError("CVV must contain digits only."); return; }
            paymentDetail = "Card ending " + cardNum.substring(cardNum.length() - 4);
        }

        if (method.equals("E-Wallet")) {
            String phone = txtPhone.getText().trim();
            String otp   = txtOTP.getText().trim();

            if (phone.isEmpty()) { showError("E-Wallet phone number cannot be empty."); return; }
            if (phone.length() != 11 && phone.length() != 13) {
                showError("Phone must be 09XXXXXXXXX (11 digits) or +639XXXXXXXXX (13 chars)."); return;
            }
            if (phone.length() == 11 && !phone.startsWith("09")) { showError("Phone must start with 09."); return; }
            if (phone.length() == 13 && !phone.startsWith("+639")) { showError("Phone must start with +639."); return; }
            if (otp.isEmpty() || otp.length() != 6) { showError("OTP must be exactly 6 digits."); return; }
            for (char ch : otp.toCharArray())
                if (!Character.isDigit(ch)) { showError("OTP must contain digits only."); return; }
            paymentDetail = "E-Wallet " + phone;
        }

        String receiptText =
            "  ---- HOTEL PAYMENT RECEIPT ----\n\n" +
            "Res ID:     " + txtResID.getText() + "\n" +
            "Guest:      " + txtGuest.getText() + "\n" +
            "Room:       " + roomNo + " (" + roomType + ")\n" +
            "Check-in:   " + txtCheckIn.getText() + "\n" +
            "Check-out:  " + txtCheckOut.getText() + "\n" +
            "Nights:     " + txtNights.getText() + "\n" +
            "Rate/Night: PHP " + String.format("%,.2f", pricePerNight) + "\n" +
            "---------------------------------\n" +
            String.format("Room Subtotal:  PHP %,.2f%n", roomTotal) +
            (addOnsTotal > 0
                ? "Add-ons: " + addOnsSummary + "\n" +
                  String.format("Add-ons Total:  PHP %,.2f%n", addOnsTotal)
                : "") +
            "---------------------------------\n" +
            String.format("GRAND TOTAL:    PHP %,.2f%n", grandTotal) +
            "---------------------------------\n" +
            "Method:     " + method + "\n" +
            (method.equals("Cash") ? String.format("Cash Paid:  PHP %,.2f%n", cashReceived) : "") +
            (method.equals("Cash") ? String.format("Change:     PHP %,.2f%n", changeAmount)  : "") +
            (!paymentDetail.isEmpty() ? "Detail:     " + paymentDetail + "\n" : "") +
            "Date:       " + LocalDate.now() + "\n" +
            "---------------------------------\n" +
            "  STATUS:  PAYMENT SUCCESSFUL\n";

        // TODO: DB CONNECT [SAVE PAYMENT]
        // INSERT INTO payments (reservation_id, payment_amount, payment_method,
        //     payment_type_details, payment_status, payment_date)
        // VALUES (reservationId, grandTotal, method, paymentDetail, 'Completed', LocalDate.now())

        txtReceipt.setText(receiptText);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cmbMethod) {
            hideAllPayFields();
            String m = (String) cmbMethod.getSelectedItem();
            if (m.equals("Cash")) {
                lblCash.setVisible(true);  txtCash.setVisible(true);
            } else if (m.equals("Credit Card")) {
                lblCard.setVisible(true);    txtCard.setVisible(true);
                lblExpiry.setVisible(true);  txtExpiry.setVisible(true);
                lblCVV.setVisible(true);     txtCVV.setVisible(true);
            } else if (m.equals("E-Wallet")) {
                lblPhone.setVisible(true);   txtPhone.setVisible(true);
                lblOTP.setVisible(true);     txtOTP.setVisible(true);
            }
        }

        if (e.getSource() == btnSearch) {
            String id = txtSearch.getText().trim();
            if (id.isEmpty()) { showError("Enter a Reservation ID to search."); return; }

            // TODO: DB CONNECT [LOAD RESERVATION]
            // SELECT r.*, g.first_name, g.last_name, ro.room_number, ro.room_type, ro.price_per_night
            // FROM reservations r
            // JOIN guests g ON r.guest_id = g.guest_id
            // JOIN rooms ro ON r.room_id = ro.room_id
            // WHERE r.reservation_id = ? AND r.reservation_status = 'Confirmed'
            //
            // Mock data:
            txtResID.setText(id);
            txtGuest.setText("Juan Dela Cruz");
            txtRoom.setText("101");
            txtRoomType.setText("Single Standard");
            txtCheckIn.setText("2026-07-01");
            txtCheckOut.setText("2026-07-03");
            txtNights.setText("2");
            txtPricePerNight.setText("PHP 3,500.00");
            numberOfNights = 2;
            pricePerNight  = 3500.0;
            roomTotal      = 7000.0;
            addOnsTotal    = 0.0;
            grandTotal     = roomTotal;
            lblRoomTotal.setText("Room Total:  PHP " + String.format("%,.2f", roomTotal));
            lblGuestIdVal.setText("Juan Dela Cruz");
            lblReservationIdVal.setText(id);
            updateGrandTotalDisplay();
        }

        if (e.getSource() == btnPrintReceipt) {
            try {
                txtReceipt.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Printing is not available on this system.", "Print", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if(e.getSource() == btnHomePage){
            dispose();
            new HomePage().setVisible(true);
        }else if(e.getSource() == btnSideDash){
            dispose();
            new ReceptionistDashboard().setVisible(true);
        }else if(e.getSource() == btnSideRegister){
            dispose();
            new RegisterGuestPanel().setVisible(true);
        }else if(e.getSource() == btnSideRoomBooking){
            dispose();
            new RoomBookingPanel().setVisible(true);
        }else if(e.getSource() == btnSideReserve){
            dispose();
            new ViewReservationPanel().setVisible(true);
        }
    }

    private JPanel makeCard(int x, int y, int w, int h) {
        JPanel p = new JPanel(null);
        p.setBounds(x, y, w, h);
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(Color.decode("#CCCCCC")));
        return p;
    }

    private void makeInfoLabel(String text, int x, int y, JPanel parent) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 120, 18);
        l.setFont(new Font("Arial Black", Font.BOLD, 10));
        parent.add(l);
    }

    private JLabel makeLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 260, 18);
        l.setFont(new Font("Arial Black", Font.BOLD, 11));
        return l;
    }

    private JTextField makeInfoField(int x, int y, int w, JPanel parent) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, 24);
        tf.setEditable(false);
        tf.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tf.setBackground(Color.decode("#F0F0F0"));
        tf.setFont(new Font("Arial", Font.PLAIN, 11));
        parent.add(tf);
        return tf;
    }

    private void styleBtn(JButton btn) {
        btn.setBackground(Color.decode("#222222"));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial Black", Font.BOLD, 11));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
    }

    private void hideAllPayFields() {
        for (Component c : new Component[]{
                lblCash, txtCash, lblCard, txtCard,
                lblExpiry, txtExpiry, lblCVV, txtCVV,
                lblPhone, txtPhone, lblOTP, txtOTP}) {
            if (c != null) c.setVisible(false);
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean passesLuhn(String number) {
        int sum = 0; boolean alt = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alt) { n *= 2; if (n > 9) n -= 9; }
            sum += n; alt = !alt;
        }
        return sum % 10 == 0;
    }

    private boolean isCardExpired(String expiry) {
        try {
            String[] parts = expiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year  = Integer.parseInt(parts[1]);
            if (year < 100) year += 2000;
            return LocalDate.of(year, month, 1).plusMonths(1).minusDays(1).isBefore(LocalDate.now());
        } catch (Exception ex) { return true; }
    }
}