package GUIGuest;

import Models.Room;
import Services.RoomService;
import Utilities.HotelException;
import HotelReservationMainSystem.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class SearchRooms extends JFrame implements ActionListener {

    private JLabel lblTitle, lblDate, lblType, lblPax;
    private JTextField txtDate, txtPax;
    private JComboBox<String> cbType;
    private JButton btnSearch;
    private JTable roomTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JPanel contentArea;

    public SearchRooms() {
        setTitle("Hotel Guest System - Search Rooms");
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

        sidebar.add(makeSideBtn("Search Rooms",       160, "Search Rooms", this, () -> openFrame(new SearchRooms())));
        sidebar.add(makeSideBtn("Make Reservation",   230, "Search Rooms", this, () -> openFrame(new MakeReservation())));
        sidebar.add(makeSideBtn("View Reservations",  300, "Search Rooms", this, () -> openFrame(new ViewReservations())));
        sidebar.add(makeSideBtn("Cancel Reservation", 370, "Search Rooms", this, () -> openFrame(new CancelReservation())));
        sidebar.add(makeSideBtn("Guest Profile",      440, "Search Rooms", this, () -> openFrame(new GuestProfile())));
        sidebar.add(makeSideBtn("Logout",             610, "Search Rooms", this, () -> {
            int c = JOptionPane.showConfirmDialog(SearchRooms.this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                SessionManager.getInstance().logout();
                dispose();
                // new GUILogin.LoginFrame().setVisible(true);
            }
        }));

        // Content area
        contentArea = new JPanel(null);
        contentArea.setBounds(250, 0, 950, 700);
        contentArea.setBackground(Color.decode("#F5F5F5"));
        add(contentArea);

        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 950, 80);
        titleBar.setBackground(Color.decode("#222222"));
        contentArea.add(titleBar);

        lblTitle = new JLabel("SEARCH ROOMS");
        lblTitle.setBounds(20, 15, 600, 50);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);

        JPanel filterBar = new JPanel(null);
        filterBar.setBounds(0, 80, 950, 70);
        filterBar.setBackground(Color.decode("#333333"));
        contentArea.add(filterBar);

        lblDate = new JLabel("Check-in Date (YYYY-MM-DD):");
        lblDate.setBounds(15, 8, 200, 18);
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(new Font("Arial Black", Font.BOLD, 11));
        filterBar.add(lblDate);

        txtDate = new JTextField();
        txtDate.setBounds(15, 28, 140, 28);
        filterBar.add(txtDate);

        lblType = new JLabel("Room Type:");
        lblType.setBounds(175, 8, 100, 18);
        lblType.setForeground(Color.WHITE);
        lblType.setFont(new Font("Arial Black", Font.BOLD, 11));
        filterBar.add(lblType);

        cbType = new JComboBox<>(new String[]{"- Select -", "Single Standard", "Double Standard", "Double Deluxe", "Suite Deluxe"});
        cbType.setBounds(175, 28, 160, 28);
        filterBar.add(cbType);

        lblPax = new JLabel("Pax/Guests:");
        lblPax.setBounds(355, 8, 100, 18);
        lblPax.setForeground(Color.WHITE);
        lblPax.setFont(new Font("Arial Black", Font.BOLD, 11));
        filterBar.add(lblPax);

        txtPax = new JTextField();
        txtPax.setBounds(355, 28, 90, 28);
        filterBar.add(txtPax);

        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(465, 25, 110, 32);
        btnSearch.setBackground(Color.WHITE);
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFont(new Font("Arial Black", Font.BOLD, 12));
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);
        filterBar.add(btnSearch);

        String[] columns = {"Room ID", "Floor", "Type", "Beds", "Max Pax", "Price/Night"};
        model = new DefaultTableModel(columns, 0);
        roomTable = new JTable(model);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 13));
        roomTable.setRowHeight(28);
        roomTable.getTableHeader().setBackground(Color.decode("#222222"));
        roomTable.getTableHeader().setForeground(Color.WHITE);
        roomTable.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 12));

        scrollPane = new JScrollPane(roomTable);
        scrollPane.setBounds(0, 150, 950, 550);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.decode("#222222")));
        contentArea.add(scrollPane);

        btnSearch.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            // Validate date
            String dateText = txtDate.getText().trim();
            if (dateText.isEmpty()) {
                showError("Please enter a check-in date.");
                return;
            }
            LocalDate checkIn;
            try {
                checkIn = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException ex) {
                showError("Invalid date format. Use YYYY-MM-DD.");
                return;
            }
            if (checkIn.isBefore(LocalDate.now())) {
                showError("Check-in date cannot be in the past.");
                return;
            }

            // Room type
            if (cbType.getSelectedIndex() == 0) {
                showError("Please select a room type.");
                return;
            }
            String roomType = cbType.getSelectedItem().toString();

            // Pax
            String paxText = txtPax.getText().trim();
            if (paxText.isEmpty()) {
                showError("Please specify number of guests.");
                return;
            }
            int guests;
            try {
                guests = Integer.parseInt(paxText);
            } catch (NumberFormatException ex) {
                showError("Please enter a valid number for guests.");
                return;
            }
            if (guests < 1 || guests > 10) {
                showError("Guests must be between 1 and 10.");
                return;
            }

            // We search for a 1-night stay (check-in to check-in+1)
            LocalDate checkOut = checkIn.plusDays(1);

            try {
                RoomService roomService = new RoomService();
                List<Room> availableRooms = roomService.getAvailableRoomsByTypeAndDate(roomType, guests, checkIn, checkOut);

                model.setRowCount(0);
                if (availableRooms.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No rooms available for the selected criteria.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (Room r : availableRooms) {
                        model.addRow(new Object[]{
                            r.getRoomId(),
                            r.getFloor(),
                            r.getRoomType(),
                            r.getCapacity() + " Pax",
                            r.getCapacity(),
                            String.format("PHP %,.2f", r.getPricePerNight())
                        });
                    }
                }
            } catch (HotelException ex) {
                showError("Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, "Error: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ========== Sidebar helpers ==========
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