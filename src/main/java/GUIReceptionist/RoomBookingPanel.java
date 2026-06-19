package GUIReceptionist;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class RoomBookingPanel extends JFrame implements ActionListener {

    private JButton btnSideDash, btnSideRegister, btnSideRoomBooking, btnSidePay, btnSideReserve,
            btnHomePage, btnVerifyGuest, btnClearVerify,
            btnSearch, btnClearFilter, btnCalculatePrice, btnBookRoom, btnBackToVerify,
            btnCalculateAddOns, btnConfirmAddOns, btnBackToReservation;

    private JLabel lblReception, lblHotel, lblManagement, lblDate,
            lblResTitle, lblGuestId, lblGuestIdValue,
            lblRoomType, lblGuests, lblCheckIn, lblCheckOut,
            lblNights, lblNightsValue, lblTotal, lblTotalValue,
            lblHint, lblVerifyTitle, lblPurpose,
            lblFirstName, lblMiddleName, lblLastName, lblEmailPhone,
            lblAddOnsTitle, lblAddOnsSubtitle,
            lblFoodBevHeader, lblCleaningHeader, lblAmenitiesHeader,
            lblBreakfast, lblLunch, lblDinner, lblSnacks, lblRoomCleaning,
            lblExtraPillow, lblExtraBlanket, lblExtraMattress,
            lblRoomSubtotalCaption, lblRoomSubtotalValue,
            lblAddOnsTotalCaption, lblAddOnsTotalValue,
            lblGrandTotalCaption, lblGrandTotalValue;

    private JPanel sidePan, topPan, verifyPan, reservationPan, addOnsPan;

    private JTextField txtFirstName, txtMiddleName, txtLastName, txtEmailPhone;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private JComboBox<String> cmbRoomType;
    private JComboBox<String> cmbGuests;
    private JComboBox<String> cmbCheckInYear, cmbCheckInMonth, cmbCheckInDay;
    private JComboBox<String> cmbCheckOutYear, cmbCheckOutMonth, cmbCheckOutDay;

    private JTable tblRooms;
    private DefaultTableModel roomTableModel;

    // add-on quantity spinners (max 10 each)
    private JSpinner spnBreakfast, spnLunch, spnDinner, spnSnacks,
            spnRoomCleaning, spnExtraPillow, spnExtraBlanket, spnExtraMattress;

    private static final double PRICE_BREAKFAST      = 250.00;
    private static final double PRICE_LUNCH          = 350.00;
    private static final double PRICE_DINNER         = 450.00;
    private static final double PRICE_SNACKS         = 150.00;
    private static final double PRICE_ROOM_CLEANING  = 200.00;
    private static final double PRICE_EXTRA_PILLOW   = 100.00;
    private static final double PRICE_EXTRA_BLANKET  = 100.00;
    private static final double PRICE_EXTRA_MATTRESS = 300.00;

    private String guestId;
    private String guestName;
    private static int reservationCounter = 1;

    // stored after Calculate Price so the Add-ons page / booking can use them
    private double calculatedTotal    = 0.0;
    private double calculatedPerNight = 0.0;
    private int    calculatedNights   = 0;

    // selected room snapshot (captured when entering the Add-ons page)
    private String selectedRoomNo, selectedRoomType;

    // stored after Calculate Add-ons so Confirm can use them
    private double addOnsTotal = 0.0;
    private double grandTotal  = 0.0;

    RoomBookingPanel() {
        sidePanel();
        functionMenu();
        topPanel();
        verifyForm();
        reservationForm();
        addOnsForm();

        verifyPan.setVisible(true);
        reservationPan.setVisible(false);
        addOnsPan.setVisible(false);

        setSize(1200, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Hotel Reservation System - Receptionist Room Booking");
    }

    private void functionMenu() {
        lblReception = new JLabel("ROOM BOOKING");
        lblReception.setBounds(350, 60, 800, 60);
        lblReception.setFont(new Font("Arial Black", Font.BOLD, 40));
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
        btnSideRoomBooking.setBackground(Color.decode("#FFFFFF"));
        btnSideRoomBooking.setForeground(Color.BLACK);
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

    private void verifyForm() {
        verifyPan = new JPanel();
        verifyPan.setBounds(320, 130, 850, 500);
        verifyPan.setLayout(null);
        verifyPan.setBackground(Color.decode("#FFFFFF"));
        add(verifyPan);

        lblVerifyTitle = new JLabel("ROOM BOOKING");
        lblVerifyTitle.setBounds(10, 10, 250, 40);
        lblVerifyTitle.setOpaque(true);
        lblVerifyTitle.setBackground(Color.decode("#FFFFFF"));
        lblVerifyTitle.setForeground(Color.decode("#222222"));
        lblVerifyTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        verifyPan.add(lblVerifyTitle);

        lblPurpose = new JLabel("Verify the guest's identity before proceeding to make a reservation.");
        lblPurpose.setBounds(10, 60, 700, 25);
        lblPurpose.setFont(new Font("Arial", Font.PLAIN, 14));
        verifyPan.add(lblPurpose);

        lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(10, 110, 150, 25);
        lblFirstName.setFont(new Font("Arial Black", Font.BOLD, 16));
        verifyPan.add(lblFirstName);

        txtFirstName = new JTextField();
        txtFirstName.setBounds(10, 135, 260, 35);
        txtFirstName.setFont(new Font("Arial", Font.PLAIN, 16));
        verifyPan.add(txtFirstName);

        lblMiddleName = new JLabel("Middle Name:");
        lblMiddleName.setBounds(290, 110, 150, 25);
        lblMiddleName.setFont(new Font("Arial Black", Font.BOLD, 16));
        verifyPan.add(lblMiddleName);

        txtMiddleName = new JTextField();
        txtMiddleName.setBounds(290, 135, 200, 35);
        txtMiddleName.setFont(new Font("Arial", Font.PLAIN, 16));
        verifyPan.add(txtMiddleName);

        lblLastName = new JLabel("Last Name:");
        lblLastName.setBounds(510, 110, 150, 25);
        lblLastName.setFont(new Font("Arial Black", Font.BOLD, 16));
        verifyPan.add(lblLastName);

        txtLastName = new JTextField();
        txtLastName.setBounds(510, 135, 260, 35);
        txtLastName.setFont(new Font("Arial", Font.PLAIN, 16));
        verifyPan.add(txtLastName);

        lblEmailPhone = new JLabel("Email or Phone:");
        lblEmailPhone.setBounds(10, 185, 200, 25);
        lblEmailPhone.setFont(new Font("Arial Black", Font.BOLD, 16));
        verifyPan.add(lblEmailPhone);

        txtEmailPhone = new JTextField();
        txtEmailPhone.setBounds(10, 210, 300, 35);
        txtEmailPhone.setFont(new Font("Arial", Font.PLAIN, 16));
        verifyPan.add(txtEmailPhone);

        lblHint = new JLabel("If using email, must be a valid format (e.g. name@example.com). If using phone, must start with 09 or +63.");
        lblHint.setBounds(10, 248, 740, 20);
        lblHint.setFont(new Font("Arial", Font.ITALIC, 11));
        lblHint.setForeground(Color.GRAY);
        verifyPan.add(lblHint);

        btnVerifyGuest = new JButton("VERIFY GUEST");
        btnVerifyGuest.setBounds(10, 290, 180, 40);
        btnVerifyGuest.setBackground(Color.decode("#222222"));
        btnVerifyGuest.setForeground(Color.WHITE);
        btnVerifyGuest.setFont(new Font("Arial Black", Font.BOLD, 12));
        btnVerifyGuest.setBorderPainted(false);
        btnVerifyGuest.setFocusPainted(false);
        btnVerifyGuest.addActionListener(this);
        verifyPan.add(btnVerifyGuest);

        btnClearVerify = new JButton("CLEAR");
        btnClearVerify.setBounds(210, 290, 120, 40);
        btnClearVerify.setBackground(Color.decode("#D32F2F"));
        btnClearVerify.setForeground(Color.WHITE);
        btnClearVerify.setFont(new Font("Arial Black", Font.BOLD, 12));
        btnClearVerify.setBorderPainted(false);
        btnClearVerify.setFocusPainted(false);
        btnClearVerify.addActionListener(this);
        verifyPan.add(btnClearVerify);
    }

    private void reservationForm() {
        reservationPan = new JPanel();
        reservationPan.setBounds(320, 130, 850, 530);
        reservationPan.setLayout(null);
        reservationPan.setBackground(Color.WHITE);
        add(reservationPan);

        lblResTitle = new JLabel("MAKE A RESERVATION");
        lblResTitle.setBounds(10, 10, 350, 30);
        lblResTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        reservationPan.add(lblResTitle);

        btnBackToVerify = new JButton("BACK");
        btnBackToVerify.setBounds(720, 10, 100, 30);
        btnBackToVerify.setBackground(Color.decode("#222222"));
        btnBackToVerify.setForeground(Color.WHITE);
        btnBackToVerify.setFont(new Font("Arial Black", Font.BOLD, 11));
        btnBackToVerify.setBorderPainted(false);
        btnBackToVerify.setFocusPainted(false);
        btnBackToVerify.addActionListener(this);
        reservationPan.add(btnBackToVerify);

        lblGuestId = new JLabel("Guest ID:");
        lblGuestId.setBounds(30, 55, 220, 25);
        lblGuestId.setFont(new Font("Arial Black", Font.BOLD, 14));
        reservationPan.add(lblGuestId);

        lblGuestIdValue = new JLabel("");
        lblGuestIdValue.setBounds(140, 55, 530, 25);
        lblGuestIdValue.setFont(new Font("Arial", Font.PLAIN, 14));
        reservationPan.add(lblGuestIdValue);

        lblRoomType = new JLabel("Room Type:");
        lblRoomType.setBounds(30, 95, 220, 25);
        lblRoomType.setFont(new Font("Arial Black", Font.BOLD, 16));
        reservationPan.add(lblRoomType);

        cmbRoomType = new JComboBox<>(new String[]{"All Types", "Single Standard", "Double Standard", "Double Deluxe", "Suite Deluxe"});
        cmbRoomType.setBounds(160, 90, 180, 35);
        cmbRoomType.setFont(new Font("Arial", Font.PLAIN, 16));
        reservationPan.add(cmbRoomType);

        lblGuests = new JLabel("No. of Guests:");
        lblGuests.setBounds(380, 95, 220, 25);
        lblGuests.setFont(new Font("Arial Black", Font.BOLD, 16));
        reservationPan.add(lblGuests);

        cmbGuests = new JComboBox<>(new String[]{"1", "2", "3", "4", "5+"});
        cmbGuests.setBounds(540, 90, 180, 35);
        cmbGuests.setFont(new Font("Arial", Font.PLAIN, 16));
        reservationPan.add(cmbGuests);

        lblCheckIn = new JLabel("Check-in Date:");
        lblCheckIn.setBounds(30, 135, 220, 25);
        lblCheckIn.setFont(new Font("Arial Black", Font.BOLD, 16));
        reservationPan.add(lblCheckIn);

        String[] years  = buildYears();
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String[] days   = buildDays();

        cmbCheckInYear  = new JComboBox<>(years);
        cmbCheckInMonth = new JComboBox<>(months);
        cmbCheckInDay   = new JComboBox<>(days);
        cmbCheckInYear.setBounds(230, 135, 160, 35);
        cmbCheckInMonth.setBounds(400, 135, 120, 35);
        cmbCheckInDay.setBounds(530, 135, 90, 35);
        for (JComboBox<?> c : new JComboBox[]{cmbCheckInYear, cmbCheckInMonth, cmbCheckInDay}) {
            c.setFont(new Font("Arial", Font.PLAIN, 15));
            reservationPan.add(c);
        }

        lblCheckOut = new JLabel("Check-out Date:");
        lblCheckOut.setBounds(30, 185, 220, 25);
        lblCheckOut.setFont(new Font("Arial Black", Font.BOLD, 16));
        reservationPan.add(lblCheckOut);

        cmbCheckOutYear  = new JComboBox<>(years);
        cmbCheckOutMonth = new JComboBox<>(months);
        cmbCheckOutDay   = new JComboBox<>(days);
        cmbCheckOutYear.setBounds(230, 185, 160, 35);
        cmbCheckOutMonth.setBounds(400, 185, 120, 35);
        cmbCheckOutDay.setBounds(530, 185, 90, 35);
        for (JComboBox<?> c : new JComboBox[]{cmbCheckOutYear, cmbCheckOutMonth, cmbCheckOutDay}) {
            c.setFont(new Font("Arial", Font.PLAIN, 15));
            reservationPan.add(c);
        }

        lblNights = new JLabel("No. of Nights:");
        lblNights.setBounds(30, 230, 220, 25);
        lblNights.setFont(new Font("Arial Black", Font.BOLD, 16));
        reservationPan.add(lblNights);

        lblNightsValue = new JLabel("--");
        lblNightsValue.setBounds(280, 230, 530, 25);
        lblNightsValue.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblNightsValue.setForeground(Color.decode("#5A3FB8"));
        reservationPan.add(lblNightsValue);

        btnSearch = new JButton("SEARCH ROOMS");
        btnSearch.setBounds(230, 270, 150, 30);
        btnSearch.setBackground(Color.decode("#222222"));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Arial Black", Font.BOLD, 11));
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(this);
        reservationPan.add(btnSearch);

        btnClearFilter = new JButton("CLEAR");
        btnClearFilter.setBounds(390, 270, 100, 30);
        btnClearFilter.setBackground(Color.decode("#D32F2F"));
        btnClearFilter.setForeground(Color.WHITE);
        btnClearFilter.setFont(new Font("Arial Black", Font.BOLD, 11));
        btnClearFilter.setBorderPainted(false);
        btnClearFilter.setFocusPainted(false);
        btnClearFilter.addActionListener(this);
        reservationPan.add(btnClearFilter);

        String[] columns = {"Room #", "Type", "Floor", "Capacity", "Price/Night", "Amenities"};
        roomTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tblRooms = new JTable(roomTableModel);
        tblRooms.setRowHeight(28);
        tblRooms.setFont(new Font("Arial", Font.PLAIN, 13));
        tblRooms.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 13));
        tblRooms.getTableHeader().setBackground(Color.decode("#222222"));
        tblRooms.getTableHeader().setForeground(Color.WHITE);
        tblRooms.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tblRooms);
        scrollPane.setBounds(30, 310, 790, 90);
        reservationPan.add(scrollPane);

        lblTotal = new JLabel("Total:");
        lblTotal.setBounds(30, 410, 220, 25);
        lblTotal.setFont(new Font("Arial Black", Font.BOLD, 16));
        reservationPan.add(lblTotal);

        lblTotalValue = new JLabel("PHP 0.00");
        lblTotalValue.setBounds(280, 410, 530, 25);
        lblTotalValue.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblTotalValue.setForeground(Color.decode("#5A3FB8"));
        reservationPan.add(lblTotalValue);

        btnCalculatePrice = new JButton("CALCULATE PRICE");
        btnCalculatePrice.setBounds(220, 450, 220, 40);
        btnCalculatePrice.setBackground(Color.decode("#222222"));
        btnCalculatePrice.setForeground(Color.WHITE);
        btnCalculatePrice.setFont(new Font("Arial Black", Font.BOLD, 13));
        btnCalculatePrice.setBorderPainted(false);
        btnCalculatePrice.setFocusPainted(false);
        btnCalculatePrice.addActionListener(this);
        reservationPan.add(btnCalculatePrice);

        btnBookRoom = new JButton("BOOK SELECTED ROOM");
        btnBookRoom.setBounds(460, 450, 260, 40);
        btnBookRoom.setBackground(Color.decode("#222222"));
        btnBookRoom.setForeground(Color.WHITE);
        btnBookRoom.setFont(new Font("Arial Black", Font.BOLD, 13));
        btnBookRoom.setBorderPainted(false);
        btnBookRoom.setFocusPainted(false);
        btnBookRoom.addActionListener(this);
        reservationPan.add(btnBookRoom);
    }

    /** Builds a small max-10 spinner styled to roughly mirror the mockup (boxed value with +/- arrows). */
    private JSpinner buildAddOnSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        spinner.setFont(new Font("Arial Black", Font.BOLD, 14));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setEditable(false);
        editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editor.getTextField().setBackground(Color.WHITE);
        spinner.addChangeListener(e -> recalcAddOnsLive());
        return spinner;
    }

    private void addOnsForm() {
        addOnsPan = new JPanel();
        addOnsPan.setBounds(320, 130, 850, 530);
        addOnsPan.setLayout(null);
        addOnsPan.setBackground(Color.WHITE);
        add(addOnsPan);

        lblAddOnsTitle = new JLabel("ADD-ON SERVICES  (Optional — per night per unit)");
        lblAddOnsTitle.setBounds(10, 5, 600, 30);
        lblAddOnsTitle.setFont(new Font("Arial Black", Font.BOLD, 18));
        addOnsPan.add(lblAddOnsTitle);

        btnBackToReservation = new JButton("BACK");
        btnBackToReservation.setBounds(720, 10, 100, 30);
        btnBackToReservation.setBackground(Color.decode("#222222"));
        btnBackToReservation.setForeground(Color.WHITE);
        btnBackToReservation.setFont(new Font("Arial Black", Font.BOLD, 11));
        btnBackToReservation.setBorderPainted(false);
        btnBackToReservation.setFocusPainted(false);
        btnBackToReservation.addActionListener(this);
        addOnsPan.add(btnBackToReservation);

        lblAddOnsSubtitle = new JLabel("Quantities apply per night. Max 10 units each.");
        lblAddOnsSubtitle.setBounds(10, 35, 500, 20);
        lblAddOnsSubtitle.setFont(new Font("Arial", Font.ITALIC, 12));
        lblAddOnsSubtitle.setForeground(Color.GRAY);
        addOnsPan.add(lblAddOnsSubtitle);


        lblFoodBevHeader = sectionHeader("Food & Beverages:", 60);
        addOnsPan.add(lblFoodBevHeader);

        lblBreakfast = addOnRowLabel("Breakfast", "PHP 250/night", 90);
        addOnsPan.add(lblBreakfast);
        spnBreakfast = buildAddOnSpinner();
        spnBreakfast.setBounds(400, 90, 50, 30);
        addOnsPan.add(spnBreakfast);

        lblLunch = addOnRowLabel("Lunch", "PHP 350/night", 130);
        addOnsPan.add(lblLunch);
        spnLunch = buildAddOnSpinner();
        spnLunch.setBounds(400, 130, 50, 30);
        addOnsPan.add(spnLunch);

        lblDinner = addOnRowLabel("Dinner", "PHP 450/night", 170);
        addOnsPan.add(lblDinner);
        spnDinner = buildAddOnSpinner();
        spnDinner.setBounds(400, 170, 50, 30);
        addOnsPan.add(spnDinner);

        lblSnacks = addOnRowLabel("Snacks / Mini-bar", "PHP 150/night", 210);
        addOnsPan.add(lblSnacks);
        spnSnacks = buildAddOnSpinner();
        spnSnacks.setBounds(400, 210, 50, 30);
        addOnsPan.add(spnSnacks);

        lblCleaningHeader = sectionHeader("Cleaning:", 250);
        addOnsPan.add(lblCleaningHeader);

        lblRoomCleaning = addOnRowLabel("Room Cleaning", "PHP 200/night", 280);
        addOnsPan.add(lblRoomCleaning);
        spnRoomCleaning = buildAddOnSpinner();
        spnRoomCleaning.setBounds(400, 280, 50, 30);
        addOnsPan.add(spnRoomCleaning);

        lblAmenitiesHeader = sectionHeader("Extra Amenities:", 320);
        addOnsPan.add(lblAmenitiesHeader);

        lblExtraPillow = addOnRowLabel("Extra Pillow", "PHP 100/night", 350);
        addOnsPan.add(lblExtraPillow);
        spnExtraPillow = buildAddOnSpinner();
        spnExtraPillow.setBounds(400, 350, 50, 30);
        addOnsPan.add(spnExtraPillow);

        lblExtraBlanket = addOnRowLabel("Extra Blanket", "PHP 100/night", 390);
        addOnsPan.add(lblExtraBlanket);
        spnExtraBlanket = buildAddOnSpinner();
        spnExtraBlanket.setBounds(400, 390, 50, 30);
        addOnsPan.add(spnExtraBlanket);

        lblExtraMattress = addOnRowLabel("Extra Mattress", "PHP 300/night", 430);
        addOnsPan.add(lblExtraMattress);
        spnExtraMattress = buildAddOnSpinner();
        spnExtraMattress.setBounds(400, 430, 50, 30);
        addOnsPan.add(spnExtraMattress);

        lblRoomSubtotalCaption = new JLabel("Room Subtotal:");
        lblRoomSubtotalCaption.setBounds(10, 480, 160, 22);
        lblRoomSubtotalCaption.setFont(new Font("Arial Black", Font.BOLD, 13));
        addOnsPan.add(lblRoomSubtotalCaption);

        lblRoomSubtotalValue = new JLabel("PHP 0.00");
        lblRoomSubtotalValue.setBounds(170, 480, 250, 22);
        lblRoomSubtotalValue.setFont(new Font("Arial", Font.PLAIN, 13));
        addOnsPan.add(lblRoomSubtotalValue);

        lblGrandTotalCaption = new JLabel("GRAND TOTAL:");
        lblGrandTotalCaption.setBounds(300, 490, 180, 25);
        lblGrandTotalCaption.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblGrandTotalCaption.setForeground(Color.decode("#5A3FB8"));
        addOnsPan.add(lblGrandTotalCaption);

        lblGrandTotalValue = new JLabel("PHP 0.00");
        lblGrandTotalValue.setBounds(450, 490, 180, 25);
        lblGrandTotalValue.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblGrandTotalValue.setForeground(Color.decode("#5A3FB8"));
        addOnsPan.add(lblGrandTotalValue);

        lblAddOnsTotalCaption = new JLabel("Add-ons Total:");
        lblAddOnsTotalCaption.setBounds(10, 505, 160, 22);
        lblAddOnsTotalCaption.setFont(new Font("Arial Black", Font.BOLD, 13));
        addOnsPan.add(lblAddOnsTotalCaption);

        lblAddOnsTotalValue = new JLabel("PHP 0.00");
        lblAddOnsTotalValue.setBounds(170, 505, 250, 22);
        lblAddOnsTotalValue.setFont(new Font("Arial", Font.PLAIN, 13));
        addOnsPan.add(lblAddOnsTotalValue);


        btnConfirmAddOns = new JButton("CONFIRM");
        btnConfirmAddOns.setBounds(650, 490, 130, 30);
        btnConfirmAddOns.setBackground(Color.decode("#222222"));
        btnConfirmAddOns.setForeground(Color.WHITE);
        btnConfirmAddOns.setFont(new Font("Arial Black", Font.BOLD, 10));
        btnConfirmAddOns.setBorderPainted(false);
        btnConfirmAddOns.setFocusPainted(false);
        btnConfirmAddOns.addActionListener(this);
        addOnsPan.add(btnConfirmAddOns);
    }

    private JLabel sectionHeader(String text, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(10, y, 300, 22);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        return lbl;
    }

    private JLabel addOnRowLabel(String name, String priceText, int y) {
        JLabel lbl = new JLabel("<html>" + name + " &nbsp;&nbsp;&nbsp; <font color='#888888'>" + priceText + "</font></html>");
        lbl.setBounds(25, y, 380, 25);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        return lbl;
    }

    /** Live-updates the on-screen totals as spinners change (does not lock in addOnsTotal/grandTotal; that happens on Calculate). */
    private void recalcAddOnsLive() {
        double liveAddOns = computeAddOnsTotal();
        lblAddOnsTotalValue.setText(String.format("PHP %,.2f", liveAddOns));
        lblGrandTotalValue.setText(String.format("PHP %,.2f", calculatedTotal + liveAddOns));
    }

    private double computeAddOnsTotal() {
        int nights = Math.max(calculatedNights, 0);
        double total = 0.0;
        total += (Integer) spnBreakfast.getValue()      * PRICE_BREAKFAST      * nights;
        total += (Integer) spnLunch.getValue()          * PRICE_LUNCH          * nights;
        total += (Integer) spnDinner.getValue()         * PRICE_DINNER         * nights;
        total += (Integer) spnSnacks.getValue()         * PRICE_SNACKS         * nights;
        total += (Integer) spnRoomCleaning.getValue()   * PRICE_ROOM_CLEANING  * nights;
        total += (Integer) spnExtraPillow.getValue()    * PRICE_EXTRA_PILLOW   * nights;
        total += (Integer) spnExtraBlanket.getValue()   * PRICE_EXTRA_BLANKET  * nights;
        total += (Integer) spnExtraMattress.getValue()  * PRICE_EXTRA_MATTRESS * nights;
        return total;
    }

    /** Builds a short human-readable summary of selected add-ons (only non-zero quantities), e.g. "Breakfast x2, Room Cleaning x1". */
    private String buildAddOnsSummary() {
        StringBuilder sb = new StringBuilder();
        addIfNonZero(sb, "Breakfast", (Integer) spnBreakfast.getValue());
        addIfNonZero(sb, "Lunch", (Integer) spnLunch.getValue());
        addIfNonZero(sb, "Dinner", (Integer) spnDinner.getValue());
        addIfNonZero(sb, "Snacks/Mini-bar", (Integer) spnSnacks.getValue());
        addIfNonZero(sb, "Room Cleaning", (Integer) spnRoomCleaning.getValue());
        addIfNonZero(sb, "Extra Pillow", (Integer) spnExtraPillow.getValue());
        addIfNonZero(sb, "Extra Blanket", (Integer) spnExtraBlanket.getValue());
        addIfNonZero(sb, "Extra Mattress", (Integer) spnExtraMattress.getValue());
        return sb.length() == 0 ? "None" : sb.toString();
    }

    private void addIfNonZero(StringBuilder sb, String name, int qty) {
        if (qty > 0) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(name).append(" x").append(qty);
        }
    }

    private void resetAddOnsForm() {
        spnBreakfast.setValue(0);
        spnLunch.setValue(0);
        spnDinner.setValue(0);
        spnSnacks.setValue(0);
        spnRoomCleaning.setValue(0);
        spnExtraPillow.setValue(0);
        spnExtraBlanket.setValue(0);
        spnExtraMattress.setValue(0);
        addOnsTotal = 0.0;
        grandTotal  = 0.0;
        lblRoomSubtotalValue.setText("PHP 0.00");
        lblAddOnsTotalValue.setText("PHP 0.00");
        lblGrandTotalValue.setText("PHP 0.00");
    }

    private String[] buildYears() {
        int y = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        String[] years = new String[5];
        for (int i = 0; i < 5; i++) years[i] = String.valueOf(y + i);
        return years;
    }

    private String[] buildDays() {
        String[] days = new String[31];
        for (int i = 0; i < 31; i++) days[i] = String.format("%02d", i + 1);
        return days;
    }

    private void clearVerifyForm() {
        txtFirstName.setText("");
        txtMiddleName.setText("");
        txtLastName.setText("");
        txtEmailPhone.setText("");
    }

    private void clearFilters() {
        cmbRoomType.setSelectedIndex(0);
        cmbGuests.setSelectedIndex(0);
        cmbCheckInYear.setSelectedIndex(0);
        cmbCheckInMonth.setSelectedIndex(0);
        cmbCheckInDay.setSelectedIndex(0);
        cmbCheckOutYear.setSelectedIndex(0);
        cmbCheckOutMonth.setSelectedIndex(0);
        cmbCheckOutDay.setSelectedIndex(0);
        lblNightsValue.setText("--");
        lblTotalValue.setText("PHP 0.00");
        roomTableModel.setRowCount(0);
        calculatedTotal    = 0.0;
        calculatedPerNight = 0.0;
        calculatedNights   = 0;
    }

    private int computeNightsFromDropdowns() {
        try {
            int inYear   = Integer.parseInt((String) cmbCheckInYear.getSelectedItem());
            int inMonth  = monthIndex((String) cmbCheckInMonth.getSelectedItem());
            int inDay    = Integer.parseInt((String) cmbCheckInDay.getSelectedItem());
            int outYear  = Integer.parseInt((String) cmbCheckOutYear.getSelectedItem());
            int outMonth = monthIndex((String) cmbCheckOutMonth.getSelectedItem());
            int outDay   = Integer.parseInt((String) cmbCheckOutDay.getSelectedItem());

            java.util.Calendar inCal  = java.util.Calendar.getInstance();
            java.util.Calendar outCal = java.util.Calendar.getInstance();
            inCal.set(inYear, inMonth, inDay, 0, 0, 0);
            outCal.set(outYear, outMonth, outDay, 0, 0, 0);

            long diff = outCal.getTimeInMillis() - inCal.getTimeInMillis();
            return (int)(diff / (1000L * 60 * 60 * 24));
        } catch (Exception ex) { return -1; }
    }

    private int monthIndex(String abbr) {
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (int i = 0; i < months.length; i++) if (months[i].equals(abbr)) return i;
        return 0;
    }

    private String getCheckInString() {
        int m = monthIndex((String) cmbCheckInMonth.getSelectedItem()) + 1;
        return cmbCheckInYear.getSelectedItem() + "-" + String.format("%02d", m) + "-" + cmbCheckInDay.getSelectedItem();
    }

    private String getCheckOutString() {
        int m = monthIndex((String) cmbCheckOutMonth.getSelectedItem()) + 1;
        return cmbCheckOutYear.getSelectedItem() + "-" + String.format("%02d", m) + "-" + cmbCheckOutDay.getSelectedItem();
    }

    private boolean isValidEmailOrPhone(String value) {
        if (EMAIL_PATTERN.matcher(value).matches()) return true;
        return value.startsWith("09") || value.startsWith("+63");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnHomePage) {
            dispose(); new HomePage().setVisible(true);

        } else if (e.getSource() == btnSideDash) {
            dispose(); new ReceptionistDashboard().setVisible(true);

        } else if (e.getSource() == btnSideRegister) {
            dispose(); new RegisterGuestPanel().setVisible(true);

        } else if (e.getSource() == btnSidePay) {
            dispose(); new RecordPaymentPanel().setVisible(true);

        } else if (e.getSource() == btnSideReserve) {
            dispose(); new ViewReservationPanel().setVisible(true);

        } else if (e.getSource() == btnVerifyGuest) {
            String firstName  = txtFirstName.getText().trim();
            String middleName = txtMiddleName.getText().trim();
            String lastName   = txtLastName.getText().trim();
            String emailPhone = txtEmailPhone.getText().trim();

            if (firstName.isEmpty())  { JOptionPane.showMessageDialog(this, "Enter the guest's first name.");            return; }
            if (lastName.isEmpty())   { JOptionPane.showMessageDialog(this, "Enter the guest's last name.");             return; }
            if (emailPhone.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter the guest's email or phone number."); return; }
            if (!isValidEmailOrPhone(emailPhone)) {
                JOptionPane.showMessageDialog(this,
                        "Enter a valid email (e.g. name@example.com)\nor phone number (starting with 09 or +63).",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String fullName = firstName + " " + (middleName.isEmpty() ? "" : middleName + " ") + lastName;
            this.guestId   = "GUEST-" + Math.abs(fullName.hashCode() % 1000);
            this.guestName = fullName;

            JOptionPane.showMessageDialog(this, "Guest Identity Verified.\n" + guestId);
            lblGuestIdValue.setText(guestId + "  (" + guestName + ")");
            verifyPan.setVisible(false);
            reservationPan.setVisible(true);

        } else if (e.getSource() == btnClearVerify) {
            clearVerifyForm();

        } else if (e.getSource() == btnBackToVerify) {
            clearVerifyForm();
            clearFilters();
            reservationPan.setVisible(false);
            verifyPan.setVisible(true);

        } else if (e.getSource() == btnSearch) {
            int nights = computeNightsFromDropdowns();
            if (nights <= 0) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date.");
                return;
            }
            lblNightsValue.setText(String.valueOf(nights));

            roomTableModel.setRowCount(0);
            Object[][] allRooms = {
                {"101", "Single Standard", "1", "1",  "3500.00", "Wi-Fi, AC"},
                {"102", "Double Standard", "1", "2",  "5000.00", "Wi-Fi, AC, TV"},
                {"201", "Double Deluxe",   "2", "3",  "7500.00", "Wi-Fi, AC, TV, Minibar"},
                {"301", "Suite Deluxe",    "3", "4", "12000.00", "Wi-Fi, AC, TV, Minibar, Balcony"}
            };

            String selectedType = (String) cmbRoomType.getSelectedItem();
            for (Object[] room : allRooms) {
                if (selectedType.equals("All Types") || selectedType.equals(room[1])) {
                    roomTableModel.addRow(room);
                }
            }

            if (roomTableModel.getRowCount() == 0)
                JOptionPane.showMessageDialog(this, "No available rooms match your filters.");

        } else if (e.getSource() == btnClearFilter) {
            clearFilters();

        } else if (e.getSource() == btnCalculatePrice) {
            int selectedRow = tblRooms.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Select a room from the table first."); return; }

            String nightsText = lblNightsValue.getText();
            if (nightsText.equals("--")) { JOptionPane.showMessageDialog(this, "Search for rooms first."); return; }

            calculatedNights   = Integer.parseInt(nightsText);
            calculatedPerNight = Double.parseDouble(
                    roomTableModel.getValueAt(selectedRow, 4).toString().replaceAll("[^0-9.]", ""));
            calculatedTotal    = calculatedPerNight * calculatedNights;
            lblTotalValue.setText(String.format("PHP %,.2f", calculatedTotal));

        } else if (e.getSource() == btnBookRoom) {
            int selectedRow = tblRooms.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Select a room from the table first."); return; }

            if (calculatedTotal == 0.0) {
                JOptionPane.showMessageDialog(this, "Please calculate the price first.");
                return;
            }

            // capture the selected room snapshot, then move to the Add-ons page
            selectedRoomNo   = roomTableModel.getValueAt(selectedRow, 0).toString();
            selectedRoomType = roomTableModel.getValueAt(selectedRow, 1).toString();

            resetAddOnsForm();
            lblRoomSubtotalValue.setText(String.format("PHP %,.2f", calculatedTotal));
            lblGrandTotalValue.setText(String.format("PHP %,.2f", calculatedTotal));

            reservationPan.setVisible(false);
            addOnsPan.setVisible(true);

        } else if (e.getSource() == btnBackToReservation) {
            addOnsPan.setVisible(false);
            reservationPan.setVisible(true);

        } else if (e.getSource() == btnCalculateAddOns) {
            addOnsTotal = computeAddOnsTotal();
            grandTotal  = calculatedTotal + addOnsTotal;
            lblRoomSubtotalValue.setText(String.format("PHP %,.2f", calculatedTotal));
            lblAddOnsTotalValue.setText(String.format("PHP %,.2f", addOnsTotal));
            lblGrandTotalValue.setText(String.format("PHP %,.2f", grandTotal));

        } else if (e.getSource() == btnConfirmAddOns) {
            // lock in totals based on current spinner values (in case Calculate wasn't clicked after a change)
            addOnsTotal = computeAddOnsTotal();
            grandTotal  = calculatedTotal + addOnsTotal;
            lblRoomSubtotalValue.setText(String.format("PHP %,.2f", calculatedTotal));
            lblAddOnsTotalValue.setText(String.format("PHP %,.2f", addOnsTotal));
            lblGrandTotalValue.setText(String.format("PHP %,.2f", grandTotal));

            String reservationId = "RES" + String.format("%03d", reservationCounter++);
            String addOnsSummary = buildAddOnsSummary();

            String checkIn  = getCheckInString();
            String checkOut = getCheckOutString();
            String guests = cmbGuests.getSelectedItem().toString();
            if (guests.equals("5+")) guests = "5";
            int numGuests = Integer.parseInt(guests);

            int choice = JOptionPane.showOptionDialog(this,
                    "Reservation created successfully!\nReservation ID: " + reservationId +
                    "\nRoom: " + selectedRoomNo + " - " + selectedRoomType +
                    "\nGuest: " + guestName +
                    "\nAdd-ons: " + addOnsSummary +
                    "\nRoom Subtotal: PHP " + String.format("%,.2f", calculatedTotal) +
                    "\nAdd-ons Total: PHP " + String.format("%,.2f", addOnsTotal) +
                    "\nGrand Total: PHP " + String.format("%,.2f", grandTotal),
                    "Booking Confirmed",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"PROCEED TO PAYMENT", "CLOSE"},
                    "PROCEED TO PAYMENT");

            if (choice == 0) {
                RecordPaymentPanel paymentPanel = new RecordPaymentPanel(
                        reservationId,
                        guestName,
                        selectedRoomNo,
                        selectedRoomType,
                        checkIn,
                        checkOut,
                        numGuests,
                        calculatedNights,
                        calculatedPerNight,
                        calculatedTotal,
                        addOnsSummary,
                        addOnsTotal,
                        grandTotal
                );
                paymentPanel.setVisible(true);
                dispose();
            }
        }
    }
}