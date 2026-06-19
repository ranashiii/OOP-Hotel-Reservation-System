package GUIGuest;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;

public class SearchRooms extends JFrame implements ActionListener {

    private JLabel lblTitle, lblDate, lblType, lblPax;
    private JTextField txtDate, txtPax;
    private JComboBox<String> cbType;
    private JButton btnSearch;
    private JTable roomTable;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JPanel titleBar, filterBar, contentArea;

    SearchRooms() {
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
        sidebar.add(makeSideBtn("Logout",             610, "Search Rooms", this, () -> {{
            int c = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {{
                // TODO: DB CONNECT [LOGOUT] - SessionManager.clearSession()
                // SessionManager.clearSession();
                // new LoginFrame().setVisible(true);
                dispose();
            }}
        }}));


        // Content area
        contentArea = new JPanel(null);
        contentArea.setBounds(250, 0, 950, 700);
        contentArea.setBackground(Color.decode("#F5F5F5"));
        add(contentArea);

        titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 950, 80);
        titleBar.setBackground(Color.decode("#222222"));
        contentArea.add(titleBar);

        lblTitle = new JLabel("SEARCH ROOMS");
        lblTitle.setBounds(20, 15, 600, 50);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        titleBar.add(lblTitle);

        filterBar = new JPanel(null);
        filterBar.setBounds(0, 80, 950, 70);
        filterBar.setBackground(Color.decode("#333333"));
        contentArea.add(filterBar);

        lblDate = new JLabel("Check-in Date:");
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
            String dateText = txtDate.getText().trim();
            if (dateText.isEmpty()) { JOptionPane.showMessageDialog(this, "Error: Please enter a check-in date.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (dateText.length() != 10 || dateText.charAt(4) != '-' || dateText.charAt(7) != '-') { JOptionPane.showMessageDialog(this, "Error: Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            LocalDate searchDate;
            try {
                searchDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Error: Invalid date. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE); return;
            }

            if (searchDate.isBefore(LocalDate.now())) { JOptionPane.showMessageDialog(this, "Error: Check-in date cannot be in the past.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (cbType.getSelectedIndex() == 0) { JOptionPane.showMessageDialog(this, "Error: Please select a room type.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            String paxText = txtPax.getText().trim();
            if (paxText.isEmpty()) { JOptionPane.showMessageDialog(this, "Error: Please specify the number of guests.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            int guests;
            try { guests = Integer.parseInt(paxText); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Error: Please enter a valid number for Pax/Guests.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            if (guests <= 0) { JOptionPane.showMessageDialog(this, "Error: Number of guests must be at least 1.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            if (guests > 10) { JOptionPane.showMessageDialog(this, "Error: Maximum room capacity is 10 guests.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            // TODO: DB CONNECT [SEARCH ROOMS] - RoomDAO.searchAvailableRooms(roomType, guests, checkInDate)
            // TABLE: rooms
            // Query: SELECT room_id, floor, room_type, capacity, price_per_night FROM rooms
            //        WHERE room_type = ? AND capacity >= ? AND status = 'Available'
            //        AND room_id NOT IN (
            //            SELECT room_id FROM reservations
            //            WHERE check_in_date <= ? AND check_out_date >= ?
            //            AND reservation_status != 'Cancelled'
            //        )
            // After query: model.setRowCount(0); then loop ResultSet → model.addRow(...)
            System.out.println("All fields valid. Proceeding with RoomDAO search...");
        }
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