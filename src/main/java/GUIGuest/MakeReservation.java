package GUIGuest;

import Models.Payment;
import Models.Reservation;
import Models.Room;
import Services.PaymentService;
import Services.ReservationService;
import Services.RoomService;
import Utilities.HotelException;
import HotelReservationMainSystem.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;   // <-- ADDED
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class MakeReservation extends JFrame implements ActionListener {

    // ========== UI COMPONENTS ==========
    private JLabel lblHeader;
    private JLabel secRoom;
    private JLabel lblType;
    private JLabel lblPax;
    private JLabel lblDates;
    private JLabel lblTo;
    private JLabel lblNights;
    private JTextArea lblTotalDisplay;
    private JLabel secPay;
    private JLabel lblPayMethod;
    private JLabel lblCardNumber;
    private JLabel lblCardHolder;
    private JLabel lblExpiry;
    private JLabel lblCvv;
    private JLabel lblWalletPhone;
    private JLabel lblOtp;
    private JLabel lblCashAmount;
    private JLabel lblReceiptTitle;

    private JTextField txtPax;
    private JTextField txtIn;
    private JTextField txtOut;
    private JTextField txtCardNumber;
    private JTextField txtCardHolder;
    private JTextField txtExpiry;
    private JTextField txtCvv;
    private JTextField txtWalletPhone;
    private JTextField txtOtp;
    private JTextField txtCashAmount;

    private JTextArea txtReceipt;

    private JComboBox<String> cbRoomType;
    private JComboBox<String> cbPayment;

    private JButton btnCalculate;
    private JButton btnBook;
    private JButton btnCloseReceipt;

    private JScrollPane scrollPane;
    private JScrollBar vBar;

    private JSeparator sep;

    private JPanel titleBar;
    private JPanel formContent;
    private JPanel formCard;
    private JPanel payDetailsPanel;
    private JPanel receiptOverlay;

    private double selectedRate = 0.0;
    private int maxPax = 0;
    private long numberOfNights = 0;
    private double finalTotal = 0.0;
    private double changeAmount = 0.0;
    private double cashReceived = 0.0;

    private int[] svcQty = new int[8];
    private JLabel[] svcQtyLabels = new JLabel[8];

    private static final double SVC_BREAKFAST = 250;
    private static final double SVC_LUNCH     = 350;
    private static final double SVC_DINNER    = 450;
    private static final double SVC_SNACKS    = 150;
    private static final double SVC_CLEANING  = 200;
    private static final double SVC_PILLOW    = 100;
    private static final double SVC_BLANKET   = 100;
    private static final double SVC_MATTRESS  = 300;

    // Hardcoded rates (used only if DB fails)
    private static final double[] RATES = {0, 3500, 5000, 7500, 12000};
    private static final int[]    PAXES = {0,    1,    2,    3,    4};

    public MakeReservation() {
        setTitle("Hotel Guest System - Make Reservation");
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

        sidebar.add(makeSideBtn("Search Rooms",       160, "Make Reservation", this, () -> openFrame(new SearchRooms())));
        sidebar.add(makeSideBtn("Make Reservation",   230, "Make Reservation", this, () -> openFrame(new MakeReservation())));
        sidebar.add(makeSideBtn("View Reservations",  300, "Make Reservation", this, () -> openFrame(new ViewReservations())));
        sidebar.add(makeSideBtn("Cancel Reservation", 370, "Make Reservation", this, () -> openFrame(new CancelReservation())));
        sidebar.add(makeSideBtn("Guest Profile",      440, "Make Reservation", this, () -> openFrame(new GuestProfile())));
        sidebar.add(makeSideBtn("Logout",             610, "Make Reservation", this, () -> {
            int c = JOptionPane.showConfirmDialog(MakeReservation.this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                SessionManager.getInstance().logout();
                dispose();
                // new GUILogin.LoginFrame().setVisible(true);
            }
        }));

        // Content area
        JPanel contentArea = new JPanel(null);
        contentArea.setBounds(250, 0, 950, 700);
        contentArea.setBackground(Color.decode("#F5F5F5"));
        add(contentArea);

        titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 950, 80);
        titleBar.setBackground(Color.decode("#222222"));
        contentArea.add(titleBar);

        lblHeader = new JLabel("MAKE A RESERVATION");
        lblHeader.setBounds(20, 15, 600, 50);
        lblHeader.setFont(new Font("Arial Black", Font.BOLD, 30));
        lblHeader.setForeground(Color.WHITE);
        titleBar.add(lblHeader);

        formContent = new JPanel(null);
        formContent.setBackground(Color.decode("#F5F5F5"));
        formContent.setPreferredSize(new Dimension(860, 1150));

        formCard = new JPanel(null);
        formCard.setBounds(10, 10, 500, 1000);
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        formContent.add(formCard);

        secRoom = new JLabel("ROOM DETAILS");
        secRoom.setBounds(20, 15, 300, 22);
        secRoom.setFont(new Font("Arial Black", Font.BOLD, 13));
        formCard.add(secRoom);

        lblType = new JLabel("Select Room Type:");
        lblType.setBounds(20, 45, 180, 18);
        lblType.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblType);

        cbRoomType = new JComboBox<>(new String[]{
            "- Select -",
            "Single Standard (PHP 3,500/night) - 1 Pax",
            "Double Standard (PHP 5,000/night) - 2 Pax",
            "Double Deluxe (PHP 7,500/night) - 3 Pax",
            "Suite Deluxe (PHP 12,000/night) - 4 Pax"
        });
        cbRoomType.setBounds(20, 65, 340, 30);
        formCard.add(cbRoomType);

        lblPax = new JLabel("Number of Guests:");
        lblPax.setBounds(20, 108, 160, 18);
        lblPax.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblPax);

        txtPax = new JTextField();
        txtPax.setBounds(20, 128, 90, 30);
        txtPax.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        formCard.add(txtPax);

        lblDates = new JLabel("Check-in / Check-out (YYYY-MM-DD):");
        lblDates.setBounds(20, 172, 320, 18);
        lblDates.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblDates);

        txtIn = new JTextField();
        txtIn.setBounds(20, 192, 120, 30);
        txtIn.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        formCard.add(txtIn);

        lblTo = new JLabel("to");
        lblTo.setBounds(150, 192, 25, 30);
        lblTo.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblTo);

        txtOut = new JTextField();
        txtOut.setBounds(178, 192, 120, 30);
        txtOut.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        formCard.add(txtOut);

        lblNights = new JLabel("Nights: --");
        lblNights.setBounds(315, 192, 150, 30);
        lblNights.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblNights);

        btnCalculate = new JButton("Calculate Price");
        btnCalculate.setBounds(20, 238, 145, 30);
        btnCalculate.setBackground(Color.decode("#222222"));
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.setFont(new Font("Arial Black", Font.BOLD, 11));
        btnCalculate.setBorderPainted(false);
        btnCalculate.setFocusPainted(false);
        formCard.add(btnCalculate);

        lblTotalDisplay = new JTextArea("Total: PHP 0.00");
        lblTotalDisplay.setFont(new Font("Arial Black", Font.BOLD, 12));
        lblTotalDisplay.setEditable(false);
        lblTotalDisplay.setLineWrap(true);
        lblTotalDisplay.setWrapStyleWord(true);
        lblTotalDisplay.setBackground(Color.WHITE);
        lblTotalDisplay.setBorder(null);

        JScrollPane totalScroll = new JScrollPane(lblTotalDisplay);
        totalScroll.setBounds(178, 238, 290, 45);
        totalScroll.setBorder(null);
        totalScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        totalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formCard.add(totalScroll);

        // ===================== ROOM SERVICE SECTION =====================
        JSeparator sepService = new JSeparator();
        sepService.setBounds(15, 282, 468, 2);
        sepService.setForeground(Color.LIGHT_GRAY);
        formCard.add(sepService);

        JLabel secService = new JLabel("ROOM SERVICE (Optional)");
        secService.setBounds(20, 292, 350, 22);
        secService.setFont(new Font("Arial Black", Font.BOLD, 13));
        formCard.add(secService);

        JLabel lblSvcNote = new JLabel("Set quantity to 0 to skip. Prices are per night.");
        lblSvcNote.setBounds(20, 314, 440, 16);
        lblSvcNote.setFont(new Font("Arial", Font.ITALIC, 11));
        lblSvcNote.setForeground(Color.GRAY);
        formCard.add(lblSvcNote);

        // Food & Beverages
        JLabel lblFoodBev = new JLabel("Food & Beverages:");
        lblFoodBev.setBounds(20, 338, 200, 18);
        lblFoodBev.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblFoodBev);

        addServiceRow(formCard, "Breakfast",       "+PHP 250/night", 20, 360, 0);
        addServiceRow(formCard, "Lunch",           "+PHP 350/night", 20, 390, 1);
        addServiceRow(formCard, "Dinner",          "+PHP 450/night", 20, 420, 2);
        addServiceRow(formCard, "Snacks / Mini-bar","+PHP 150/night", 20, 450, 3);

        // Cleaning
        JLabel lblCleaning = new JLabel("Cleaning Services:");
        lblCleaning.setBounds(20, 482, 200, 18);
        lblCleaning.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblCleaning);

        addServiceRow(formCard, "Room Cleaning", "+PHP 200/night", 20, 504, 4);

        // Extras
        JLabel lblExtras = new JLabel("Extra Amenities:");
        lblExtras.setBounds(20, 536, 200, 18);
        lblExtras.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblExtras);

        addServiceRow(formCard, "Extra Pillow",   "+PHP 100/night", 20, 558, 5);
        addServiceRow(formCard, "Extra Blanket",  "+PHP 100/night", 20, 588, 6);
        addServiceRow(formCard, "Extra Mattress", "+PHP 300/night", 20, 618, 7);

        sep = new JSeparator();
        sep.setBounds(15, 652, 468, 2);
        sep.setForeground(Color.LIGHT_GRAY);
        formCard.add(sep);

        secPay = new JLabel("PAYMENT DETAILS");
        secPay.setBounds(20, 662, 300, 22);
        secPay.setFont(new Font("Arial Black", Font.BOLD, 13));
        formCard.add(secPay);

        lblPayMethod = new JLabel("Payment Method:");
        lblPayMethod.setBounds(20, 692, 180, 18);
        lblPayMethod.setFont(new Font("Arial Black", Font.BOLD, 11));
        formCard.add(lblPayMethod);

        cbPayment = new JComboBox<>(new String[]{"- Select -", "Credit Card", "E-Wallet", "Cash"});
        cbPayment.setBounds(20, 712, 230, 30);
        formCard.add(cbPayment);

        payDetailsPanel = new JPanel(null);
        payDetailsPanel.setBounds(15, 755, 468, 220);
        payDetailsPanel.setBackground(Color.WHITE);
        formCard.add(payDetailsPanel);

        lblCardNumber = new JLabel("Card Number (13-19 digits):");
        lblCardNumber.setBounds(0, 0, 300, 18);
        lblCardNumber.setFont(new Font("Arial Black", Font.BOLD, 11));
        payDetailsPanel.add(lblCardNumber);

        txtCardNumber = new JTextField();
        txtCardNumber.setBounds(0, 20, 440, 30);
        txtCardNumber.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payDetailsPanel.add(txtCardNumber);

        lblCardHolder = new JLabel("Card Holder Name:");
        lblCardHolder.setBounds(0, 60, 300, 18);
        lblCardHolder.setFont(new Font("Arial Black", Font.BOLD, 11));
        payDetailsPanel.add(lblCardHolder);

        txtCardHolder = new JTextField();
        txtCardHolder.setBounds(0, 80, 440, 30);
        txtCardHolder.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payDetailsPanel.add(txtCardHolder);

        lblExpiry = new JLabel("Expiry Date (MM/YY):");
        lblExpiry.setBounds(0, 120, 220, 18);
        lblExpiry.setFont(new Font("Arial Black", Font.BOLD, 11));
        payDetailsPanel.add(lblExpiry);

        txtExpiry = new JTextField();
        txtExpiry.setBounds(0, 140, 140, 30);
        txtExpiry.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payDetailsPanel.add(txtExpiry);

        lblCvv = new JLabel("CVV:");
        lblCvv.setBounds(160, 120, 80, 18);
        lblCvv.setFont(new Font("Arial Black", Font.BOLD, 11));
        payDetailsPanel.add(lblCvv);

        txtCvv = new JTextField();
        txtCvv.setBounds(160, 140, 80, 30);
        txtCvv.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payDetailsPanel.add(txtCvv);

        lblWalletPhone = new JLabel("E-Wallet Phone (09XXXXXXXXX or +639XXXXXXXXX):");
        lblWalletPhone.setBounds(0, 0, 440, 18);
        lblWalletPhone.setFont(new Font("Arial Black", Font.BOLD, 11));
        payDetailsPanel.add(lblWalletPhone);

        txtWalletPhone = new JTextField();
        txtWalletPhone.setBounds(0, 20, 280, 30);
        txtWalletPhone.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payDetailsPanel.add(txtWalletPhone);

        lblOtp = new JLabel("OTP (6-digit code):");
        lblOtp.setBounds(0, 60, 220, 18);
        lblOtp.setFont(new Font("Arial Black", Font.BOLD, 11));
        payDetailsPanel.add(lblOtp);

        txtOtp = new JTextField();
        txtOtp.setBounds(0, 80, 140, 30);
        txtOtp.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payDetailsPanel.add(txtOtp);

        lblCashAmount = new JLabel("Cash Amount To Pay:");
        lblCashAmount.setBounds(5, 0, 300, 18);
        lblCashAmount.setFont(new Font("Arial Black", Font.BOLD, 11));
        payDetailsPanel.add(lblCashAmount);

        txtCashAmount = new JTextField();
        txtCashAmount.setBounds(5, 20, 140, 30);
        txtCashAmount.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        payDetailsPanel.add(txtCashAmount);

        payDetailsPanel.setVisible(false);

        btnBook = new JButton("CONFIRM & PAY");
        btnBook.setBounds(290, 712, 150, 30);
        btnBook.setBackground(Color.decode("#222222"));
        btnBook.setForeground(Color.WHITE);
        btnBook.setFont(new Font("Arial Black", Font.BOLD, 12));
        btnBook.setBorderPainted(false);
        btnBook.setFocusPainted(false);
        formCard.add(btnBook);

        receiptOverlay = new JPanel(null);
        receiptOverlay.setBounds(530, 10, 320, 700);
        receiptOverlay.setBackground(Color.decode("#222222"));
        receiptOverlay.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        receiptOverlay.setVisible(false);
        formContent.add(receiptOverlay);

        lblReceiptTitle = new JLabel("BOOKING RECEIPT");
        lblReceiptTitle.setBounds(15, 12, 290, 26);
        lblReceiptTitle.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblReceiptTitle.setForeground(Color.WHITE);
        receiptOverlay.add(lblReceiptTitle);

        txtReceipt = new JTextArea();
        txtReceipt.setEditable(false);
        txtReceipt.setBackground(Color.decode("#333333"));
        txtReceipt.setForeground(Color.WHITE);
        txtReceipt.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtReceipt.setLineWrap(true);
        txtReceipt.setWrapStyleWord(false);

        JScrollPane receiptScroll = new JScrollPane(txtReceipt);
        receiptScroll.setBounds(15, 48, 290, 600);
        receiptScroll.setBorder(BorderFactory.createLineBorder(Color.decode("#555555")));
        receiptScroll.setBackground(Color.decode("#333333"));
        receiptScroll.getViewport().setBackground(Color.decode("#333333"));
        receiptScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        receiptScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        receiptOverlay.add(receiptScroll);

        btnCloseReceipt = new JButton("CLOSE");
        btnCloseReceipt.setBounds(100, 655, 120, 30);
        btnCloseReceipt.setBackground(Color.WHITE);
        btnCloseReceipt.setForeground(Color.BLACK);
        btnCloseReceipt.setFont(new Font("Arial Black", Font.BOLD, 12));
        btnCloseReceipt.setBorderPainted(false);
        btnCloseReceipt.setFocusPainted(false);
        receiptOverlay.add(btnCloseReceipt);

        scrollPane = new JScrollPane(formContent);
        scrollPane.setBounds(0, 85, 950, 615);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.decode("#F5F5F5"));
        scrollPane.getViewport().setBackground(Color.decode("#F5F5F5"));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        vBar = scrollPane.getVerticalScrollBar();
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

        contentArea.add(scrollPane);

        cbPayment.addActionListener(this);
        btnCalculate.addActionListener(this);
        btnBook.addActionListener(this);
        btnCloseReceipt.addActionListener(this);
    }

    // ========== VALIDATION HELPERS ==========
    private int validateRoomAndPax() {
        int selection = cbRoomType.getSelectedIndex();
        if (selection == 0) {
            showError("Please select a room type.");
            return -1;
        }

        String paxText = txtPax.getText().trim();
        if (paxText.isEmpty()) {
            showError("Please enter number of guests.");
            return -1;
        }

        int inputPax;
        try {
            inputPax = Integer.parseInt(paxText);
        } catch (NumberFormatException ex) {
            showError("Please enter a valid number for guests.");
            return -1;
        }

        if (inputPax <= 0) {
            showError("Guests must be at least 1.");
            return -1;
        }

        int allowedPax = PAXES[selection];
        if (inputPax > allowedPax) {
            showError("This room type only allows up to " + allowedPax + " guest(s).");
            return -1;
        }

        return inputPax;
    }

    private LocalDate parseDate(String text, String label) {
        if (text.isEmpty()) {
            showError("Please enter a " + label + " date.");
            return null;
        }
        try {
            DateTimeFormatter dtf = new DateTimeFormatterBuilder().appendPattern("yyyy-M-d").toFormatter();
            return LocalDate.parse(text, dtf);
        } catch (DateTimeParseException ex) {
            showError("Invalid " + label + " date. Use YYYY-MM-DD.");
            return null;
        }
    }

    private boolean passesLuhn(String number) {
        int sum = 0;
        boolean alt = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alt) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alt = !alt;
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
        } catch (Exception ex) {
            return true;
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, "Error: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ========== ROOM SERVICE UI HELPERS ==========
    private void addServiceRow(JPanel panel, String name, String price, int x, int y, int index) {
        JLabel lbl = new JLabel(name);
        lbl.setBounds(x + 10, y + 2, 160, 20);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lbl);

        JLabel lblPrice = new JLabel(price);
        lblPrice.setBounds(x + 170, y + 2, 130, 20);
        lblPrice.setFont(new Font("Arial", Font.ITALIC, 11));
        lblPrice.setForeground(Color.GRAY);
        panel.add(lblPrice);

        JButton btnMinus = new JButton("-");
        btnMinus.setBounds(310, y, 30, 24);
        btnMinus.setFont(new Font("Arial", Font.BOLD, 14));
        btnMinus.setBackground(Color.decode("#222222"));
        btnMinus.setForeground(Color.WHITE);
        btnMinus.setBorderPainted(false);
        btnMinus.setFocusPainted(false);
        btnMinus.setMargin(new Insets(0, 0, 0, 0));
        panel.add(btnMinus);

        svcQtyLabels[index] = new JLabel("0", SwingConstants.CENTER);
        svcQtyLabels[index].setBounds(342, y, 34, 24);
        svcQtyLabels[index].setFont(new Font("Arial Black", Font.BOLD, 12));
        svcQtyLabels[index].setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        panel.add(svcQtyLabels[index]);

        JButton btnPlus = new JButton("+");
        btnPlus.setBounds(378, y, 30, 24);
        btnPlus.setFont(new Font("Arial", Font.BOLD, 14));
        btnPlus.setBackground(Color.decode("#222222"));
        btnPlus.setForeground(Color.WHITE);
        btnPlus.setBorderPainted(false);
        btnPlus.setFocusPainted(false);
        btnPlus.setMargin(new Insets(0, 0, 0, 0));
        panel.add(btnPlus);

        btnMinus.addActionListener(ev -> {
            if (svcQty[index] > 0) {
                svcQty[index]--;
                svcQtyLabels[index].setText(String.valueOf(svcQty[index]));
            }
        });

        btnPlus.addActionListener(ev -> {
            if (svcQty[index] < 10) {
                svcQty[index]++;
                svcQtyLabels[index].setText(String.valueOf(svcQty[index]));
            }
        });
    }

    // ========== SIDEBAR HELPERS ==========
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

    // ============================================================
    // ========== MAIN ACTION LISTENER (UPDATED) ==========
    // ============================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        // ---- Payment method visibility ----
        if (e.getSource() == cbPayment) {
            String method = cbPayment.getSelectedItem().toString();
            boolean isCard   = method.equals("Credit Card");
            boolean isWallet = method.equals("E-Wallet");
            boolean isCash   = method.equals("Cash");

            lblCardNumber.setVisible(isCard);    txtCardNumber.setVisible(isCard);
            lblCardHolder.setVisible(isCard);    txtCardHolder.setVisible(isCard);
            lblExpiry.setVisible(isCard);        txtExpiry.setVisible(isCard);
            lblCvv.setVisible(isCard);           txtCvv.setVisible(isCard);
            lblWalletPhone.setVisible(isWallet); txtWalletPhone.setVisible(isWallet);
            lblOtp.setVisible(isWallet);         txtOtp.setVisible(isWallet);
            lblCashAmount.setVisible(isCash);    txtCashAmount.setVisible(isCash);

            payDetailsPanel.setVisible(isCard || isWallet || isCash);
            formCard.revalidate();
            formCard.repaint();
            return;
        }

        // ---- Calculate ----
        if (e.getSource() == btnCalculate) {
            int selection = cbRoomType.getSelectedIndex();
            if (selection == 0) {
                showError("Please select a room type.");
                return;
            }

            int inputPax = validateRoomAndPax();
            if (inputPax == -1) return;

            selectedRate = RATES[selection];
            maxPax = PAXES[selection];

            LocalDate checkIn = parseDate(txtIn.getText().trim(), "check-in");
            if (checkIn == null) return;
            LocalDate checkOut = parseDate(txtOut.getText().trim(), "check-out");
            if (checkOut == null) return;

            if (checkIn.isBefore(LocalDate.now())) {
                showError("Check-in date cannot be in the past.");
                return;
            }
            if (!checkOut.isAfter(checkIn)) {
                showError("Check-out date must be after check-in date.");
                return;
            }

            numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (numberOfNights < 1 || numberOfNights > 365) {
                showError("Stay must be between 1 and 365 nights.");
                return;
            }

            double subtotal = selectedRate * numberOfNights;
            double tax = subtotal * 0.12;

            double serviceCost = 0;
            double[] svcPrices = {SVC_BREAKFAST, SVC_LUNCH, SVC_DINNER, SVC_SNACKS, SVC_CLEANING, SVC_PILLOW, SVC_BLANKET, SVC_MATTRESS};
            for (int i = 0; i < svcQty.length; i++) {
                serviceCost += svcQty[i] * svcPrices[i] * numberOfNights;
            }

            finalTotal = subtotal + tax + serviceCost;

            lblNights.setText("Nights: " + numberOfNights);
            String totalText = String.format("Subtotal: PHP %,.2f | Tax(12%%): PHP %,.2f", subtotal, tax);
            if (serviceCost > 0) totalText += String.format(" | Services: PHP %,.2f", serviceCost);
            totalText += String.format(" | Total: PHP %,.2f", finalTotal);
            lblTotalDisplay.setText(totalText);
            return;
        }

        // ---- Book / Confirm ----
        if (e.getSource() == btnBook) {
            // 1. Validate basic inputs
            int inputPax = validateRoomAndPax();
            if (inputPax == -1) return;

            if (txtIn.getText().trim().isEmpty() || txtOut.getText().trim().isEmpty()) {
                showError("Please enter check-in and check-out dates.");
                return;
            }
            LocalDate checkIn = parseDate(txtIn.getText().trim(), "check-in");
            if (checkIn == null) return;
            LocalDate checkOut = parseDate(txtOut.getText().trim(), "check-out");
            if (checkOut == null) return;
            if (!checkOut.isAfter(checkIn)) {
                showError("Check-out must be after check-in.");
                return;
            }
            if (checkIn.isBefore(LocalDate.now())) {
                showError("Check-in cannot be in the past.");
                return;
            }
            numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
            if (numberOfNights < 1 || numberOfNights > 365) {
                showError("Stay must be between 1 and 365 nights.");
                return;
            }
            if (finalTotal == 0) {
                showError("Please calculate the price first.");
                return;
            }
            if (finalTotal > 999999.99) {
                showError("Total exceeds PHP 999,999.99.");
                return;
            }
            if (cbPayment.getSelectedIndex() == 0) {
                showError("Please select a payment method.");
                return;
            }
            String payMethod = cbPayment.getSelectedItem().toString();

            // 2. Payment method validation (keep your existing checks)
            if (payMethod.equals("Credit Card")) {
                String cardNum = txtCardNumber.getText().trim();
                if (cardNum.isEmpty() || cardNum.length() < 13 || cardNum.length() > 19 || !cardNum.matches("\\d+")) {
                    showError("Card number must be 13-19 digits.");
                    return;
                }
                if (!passesLuhn(cardNum)) {
                    showError("Invalid card number (failed Luhn check).");
                    return;
                }
                String holder = txtCardHolder.getText().trim();
                if (holder.isEmpty() || !holder.matches("[a-zA-Z ]+")) {
                    showError("Card holder name must contain letters and spaces only.");
                    return;
                }
                String expiry = txtExpiry.getText().trim();
                if (expiry.isEmpty() || expiry.length() < 4 || !expiry.contains("/") || isCardExpired(expiry)) {
                    showError("Invalid or expired expiry date (MM/YY).");
                    return;
                }
                String cvv = txtCvv.getText().trim();
                if (cvv.isEmpty() || cvv.length() < 3 || cvv.length() > 4 || !cvv.matches("\\d+")) {
                    showError("CVV must be 3 or 4 digits.");
                    return;
                }
            }

            if (payMethod.equals("E-Wallet")) {
                String phone = txtWalletPhone.getText().trim();
                if (!phone.matches("09\\d{9}") && !phone.matches("\\+639\\d{9}")) {
                    showError("Phone must be 09XXXXXXXXX or +639XXXXXXXXX.");
                    return;
                }
                String otp = txtOtp.getText().trim();
                if (!otp.matches("\\d{6}")) {
                    showError("OTP must be exactly 6 digits.");
                    return;
                }
            }

            if (payMethod.equals("Cash")) {
                String cashText = txtCashAmount.getText().trim();
                if (cashText.isEmpty()) {
                    showError("Please enter cash amount.");
                    return;
                }
                try {
                    double cash = Double.parseDouble(cashText);
                    if (cash < finalTotal) {
                        showError("Insufficient cash. Need PHP " + String.format("%,.2f", finalTotal));
                        return;
                    }
                    cashReceived = cash;
                    changeAmount = cash - finalTotal;
                } catch (NumberFormatException ex) {
                    showError("Invalid cash amount.");
                    return;
                }
            }

            // 3. Get an available room for the selected type and dates
            int selection = cbRoomType.getSelectedIndex();
            String roomType = cbRoomType.getSelectedItem().toString();
            Room selectedRoom;
            try {
                selectedRoom = getAvailableRoom(roomType, inputPax, checkIn, checkOut);
                if (selectedRoom == null) {
                    showError("No available room for the selected type and dates.");
                    return;
                }
            } catch (HotelException ex) {
                showError("Database error: " + ex.getMessage());
                ex.printStackTrace();
                return;
            }

            // 4. Build Reservation object
            Reservation reservation = new Reservation();
            int guestId = SessionManager.getInstance().getGuestId();
            if (guestId == 0) {
                showError("Guest not logged in.");
                return;
            }
            reservation.setGuestId(guestId);
            reservation.setRoomId(selectedRoom.getRoomId());
            reservation.setCheckInDate(checkIn);
            reservation.setCheckOutDate(checkOut);
            reservation.setNumberOfGuests(inputPax);
            reservation.setNumberOfNights((int) numberOfNights);
            reservation.setRoomRate(BigDecimal.valueOf(selectedRate));
            reservation.setTotalPrice(BigDecimal.valueOf(selectedRate * numberOfNights));
            reservation.setDiscountApplied(BigDecimal.ZERO);
            reservation.setFinalTotal(BigDecimal.valueOf(finalTotal));
            reservation.setReservationDate(LocalDate.now());
            reservation.setReservationStatus("Confirmed");
            reservation.setNotes("");

            // 5. Create reservation
            ReservationService resService = new ReservationService();
            Reservation created;
            try {
                created = resService.createReservation(reservation);
            } catch (HotelException ex) {
                showError("Booking failed: " + ex.getMessage());
                ex.printStackTrace();
                return;
            }

            // 6. Process payment
            Payment payment = new Payment();
            payment.setReservationId(created.getReservationId());
            payment.setPaymentAmount(BigDecimal.valueOf(finalTotal));
            payment.setPaymentMethod(payMethod);
            if (payMethod.equals("Credit Card")) {
                String cardNum = txtCardNumber.getText().trim();
                String last4 = cardNum.length() >= 4 ? cardNum.substring(cardNum.length() - 4) : cardNum;
                payment.setPaymentTypeDetails("Card ending in " + last4);
            } else if (payMethod.equals("E-Wallet")) {
                payment.setPaymentTypeDetails(txtWalletPhone.getText().trim());
            } else {
                payment.setPaymentTypeDetails("Cash");
            }
            payment.setPaymentStatus("Completed");
            payment.setTransactionId(UUID.randomUUID().toString());

            PaymentService payService = new PaymentService();
            try {
                payService.processPayment(payment);
            } catch (HotelException ex) {
                showError("Payment failed: " + ex.getMessage());
                ex.printStackTrace();
                // Optionally cancel the reservation here
                return;
            }

            // 7. Show receipt
            double subtotal = selectedRate * numberOfNights;
            double tax = subtotal * 0.12;
            double serviceCostReceipt = 0;
            StringBuilder serviceLines = new StringBuilder();
            String[] svcNames  = {"Breakfast","Lunch","Dinner","Snacks","Cleaning","Pillow","Blanket","Mattress"};
            double[] svcPricesR = {SVC_BREAKFAST, SVC_LUNCH, SVC_DINNER, SVC_SNACKS, SVC_CLEANING, SVC_PILLOW, SVC_BLANKET, SVC_MATTRESS};
            for (int i = 0; i < svcQty.length; i++) {
                if (svcQty[i] > 0) {
                    double c = svcQty[i] * svcPricesR[i] * numberOfNights;
                    serviceCostReceipt += c;
                    serviceLines.append(String.format("  %-10s x%d: PHP %,.2f%n", svcNames[i], svcQty[i], c));
                }
            }

            receiptOverlay.setVisible(true);
            txtReceipt.setText(
                "    --- HOTEL RESERVATION RECEIPT ---\n\n" +
                "Room:      " + roomType + "\n" +
                "Guests:    " + inputPax + "\n" +
                "Check-in:  " + checkIn + "\n" +
                "Check-out: " + checkOut + "\n" +
                "Nights:    " + numberOfNights + "\n" +
                "---------------------------------\n" +
                String.format("Subtotal:  PHP %,.2f%n", subtotal) +
                String.format("Tax (12%%): PHP %,.2f%n", tax) +
                (serviceCostReceipt > 0 ? "----- Room Services -----\n" + serviceLines.toString() + String.format("Services:  PHP %,.2f%n", serviceCostReceipt) : "") +
                String.format("TOTAL:     PHP %,.2f%n", finalTotal) +
                "---------------------------------\n" +
                "Method:    " + payMethod + "\n" +
                (payMethod.equals("Cash") ? String.format("Cash:      PHP %,.2f%n", cashReceived) : "") +
                (payMethod.equals("Cash") ? String.format("Change:    PHP %,.2f%n", changeAmount) : "") +
                "---------------------------------\n" +
                "STATUS:    SUCCESSFUL"
            );
        }

        // ---- Close receipt ----
        if (e.getSource() == btnCloseReceipt) {
            receiptOverlay.setVisible(false);
        }
    }

    // ========== HELPER: Get an available room for the given type, date, and pax ==========
    private Room getAvailableRoom(String roomType, int minPax, LocalDate checkIn, LocalDate checkOut) throws HotelException {
        RoomService roomService = new RoomService();
        List<Room> available = roomService.getAvailableRoomsByTypeAndDate(roomType, minPax, checkIn, checkOut);
        if (available.isEmpty()) return null;
        return available.get(0); // pick the first available
    }
}