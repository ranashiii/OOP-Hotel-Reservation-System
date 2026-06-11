package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class ReservationsPanel extends JPanel {

    private static final Color DARK = Color.decode("#222222");

    private final JFrame parent;
    private final ReservationService reservationService;
    private JLabel lblTotal, lblActive;

    public ReservationsPanel(JFrame parent, ReservationService reservationService) {
    this.parent             = parent;
    this.reservationService = reservationService;
    setLayout(new BorderLayout());
    setBackground(Color.decode("#F5F5F5"));

    JPanel inner = new JPanel(null);
    inner.setBackground(Color.decode("#F5F5F5"));
    inner.setPreferredSize(new Dimension(850, 900));

    buildUI(inner);

    JScrollPane scroll = new JScrollPane(inner);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scroll.setBorder(null);
    add(scroll, BorderLayout.CENTER);
    }

    private void buildUI(JPanel formContainer) {
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(20, 10, 830, 470);
        formPanel.setBackground(Color.WHITE);
        formContainer.add(formPanel);

        JLabel lblTitle = new JLabel("RESERVATIONS");
        lblTitle.setBounds(30, 20, 500, 35);
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        formPanel.add(lblTitle);

        lblTotal = new JLabel("Total reservations: " + reservationService.countAllReservations());
        lblTotal.setBounds(30, 60, 350, 22);
        lblTotal.setFont(new Font("Arial Black", Font.PLAIN, 13));
        formPanel.add(lblTotal);

        lblActive = new JLabel("Active reservations: " + reservationService.countActiveReservations());
        lblActive.setBounds(30, 85, 350, 22);
        lblActive.setFont(new Font("Arial Black", Font.PLAIN, 13));
        formPanel.add(lblActive);

        addLabel(formPanel, "Search Reservation (Guest / ID):", 30, 120);
        JTextField txtSearch = new JTextField();
        txtSearch.setBounds(30, 148, 450, 40);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtSearch);

        JButton btnSearch = createDarkButton("SEARCH");
        btnSearch.setBounds(495, 148, 130, 40);
        btnSearch.addActionListener(e -> {
            String query = txtSearch.getText().trim().toLowerCase();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "Enter a guest name to search.", "Search",
                    JOptionPane.INFORMATION_MESSAGE); return;
            }
            StringBuilder sb = new StringBuilder();
            for (String[] r : reservationService.findAllReservations()) {
                if (r[0].toLowerCase().contains(query) || r[1].toLowerCase().contains(query)) {
                    sb.append("Guest: ").append(r[0])
                      .append(" | Room: ").append(r[1])
                      .append(" | ").append(r[2]).append(" - ").append(r[3])
                      .append(" | ").append(r[4]).append("\n");
                }
            }
            if (sb.length() == 0) sb.append("No matching reservations found.");
            JOptionPane.showMessageDialog(parent, sb.toString(),
                "Search Results", JOptionPane.INFORMATION_MESSAGE);
        });
        formPanel.add(btnSearch);

        addLabel(formPanel, "Guest Name:", 30, 205);
        JTextField txtGuestName = new JTextField();
        txtGuestName.setBounds(30, 233, 280, 40);
        txtGuestName.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtGuestName);

        addLabel(formPanel, "Room Number:", 330, 205);
        JTextField txtRoomNumber = new JTextField();
        txtRoomNumber.setBounds(330, 233, 200, 40);
        txtRoomNumber.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtRoomNumber);

        addLabel(formPanel, "Check In (YYYY-MM-DD):", 30, 290);
        JTextField txtCheckIn = new JTextField();
        txtCheckIn.setBounds(30, 318, 250, 40);
        txtCheckIn.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtCheckIn);

        addLabel(formPanel, "Check Out (YYYY-MM-DD):", 305, 290);
        JTextField txtCheckOut = new JTextField();
        txtCheckOut.setBounds(305, 318, 250, 40);
        txtCheckOut.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtCheckOut);

        JButton btnAdd = createDarkButton("ADD RESERVATION");
        btnAdd.setBounds(30, 390, 200, 45);
        btnAdd.addActionListener(e -> {
            String gn = txtGuestName.getText().trim();
            String rn = txtRoomNumber.getText().trim();
            String ci = txtCheckIn.getText().trim();
            String co = txtCheckOut.getText().trim();

            if (gn.isEmpty() || rn.isEmpty() || ci.isEmpty() || co.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "All fields are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE); return;
            }
            reservationService.addReservation(gn, rn, ci, co);
            JOptionPane.showMessageDialog(parent, "Reservation added successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            txtGuestName.setText(""); txtRoomNumber.setText("");
            txtCheckIn.setText(""); txtCheckOut.setText("");
            refreshLabels();
        });
        formPanel.add(btnAdd);

        JButton btnCancel = createDarkButton("CANCEL RESERVATION");
        btnCancel.setBounds(245, 390, 220, 45);
        btnCancel.addActionListener(e -> {
            if (reservationService.findAllReservations().isEmpty()) {
                JOptionPane.showMessageDialog(parent,
                    "No reservations available to cancel.", "Cancel Reservation",
                    JOptionPane.WARNING_MESSAGE); return;
            }
            showCancelDialog();
        });
        formPanel.add(btnCancel);

        JButton btnView = createDarkButton("VIEW ALL");
        btnView.setBounds(480, 390, 150, 45);
        btnView.addActionListener(e -> showAllReservationsDialog());
        formPanel.add(btnView);
        
        JPanel recentPanel = new JPanel(null);
        recentPanel.setBounds(20, 500, 830, 0);
        recentPanel.setBackground(Color.decode("#222222"));

        JLabel lblRecent = new JLabel("Recent Reservations");
        lblRecent.setBounds(15, 8, 400, 25);
        lblRecent.setFont(new Font("Arial Black", Font.BOLD, 15));
        lblRecent.setForeground(Color.WHITE);
        recentPanel.add(lblRecent);

        List<String[]> all = reservationService.findAllReservations();
        int start = Math.max(0, all.size() - 5);
        if (all.isEmpty()) {
        JLabel empty = new JLabel("No reservations yet.");
        empty.setBounds(15, 40, 400, 22);
        empty.setFont(new Font("Arial Black", Font.PLAIN, 11));
        empty.setForeground(Color.LIGHT_GRAY);
        recentPanel.add(empty);
        recentPanel.setBounds(20, 490, 830, 75);
        }
        else {
        for (int i = start; i < all.size(); i++) {
        String[] r = all.get(i);
        JLabel row = new JLabel((i - start + 1) + ".  " + r[0] + "  |  Room " + r[1] + "  |  " + r[2] + " → " + r[3] + "  |  " + r[4]);
        row.setBounds(15, 38 + (i - start) * 28, 800, 22);
        row.setFont(new Font("Arial Black", Font.PLAIN, 11));
        row.setForeground(Color.WHITE);
        recentPanel.add(row);
        }
        
        recentPanel.setBounds(20, 490, 830, 45 + (all.size() - start) * 28);
        }
        formContainer.add(recentPanel);
        }

        void showCancelDialog() {
        JDialog dlg = new JDialog(parent, "Cancel Reservation", true);
        dlg.setSize(620, 420);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setLocationRelativeTo(parent);

        JLabel lbl = new JLabel("Select a reservation to cancel:");
        lbl.setBounds(20, 15, 350, 25);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        dlg.add(lbl);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String[] r : reservationService.findAllReservations())
            model.addElement(r[0] + " | Rm " + r[1]
                + " | " + r[2] + " - " + r[3] + " | " + r[4]);

        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(20, 50, 565, 280);
        dlg.add(sp);

        JButton btnDo = createDarkButton("CANCEL SELECTED");
        btnDo.setBounds(20, 345, 190, 40);
        btnDo.addActionListener(e2 -> {
            int idx = list.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(dlg, "Please select a reservation.",
                    "Error", JOptionPane.ERROR_MESSAGE); return;
            }
            String[] r = reservationService.findAllReservations().get(idx);
            if (r[4].equals("CANCELLED")) {
                JOptionPane.showMessageDialog(dlg, "This reservation is already cancelled.",
                    "Info", JOptionPane.INFORMATION_MESSAGE); return;
            }
            int c = JOptionPane.showConfirmDialog(dlg,
                "Cancel this reservation?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                reservationService.cancelReservation(idx);
                JOptionPane.showMessageDialog(dlg, "Reservation cancelled.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
                refreshLabels();
            }
        });
        dlg.add(btnDo);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(225, 345, 100, 40);
        btnClose.setBackground(new Color(220, 220, 220));
        btnClose.setForeground(Color.DARK_GRAY);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e2 -> dlg.dispose());
        dlg.add(btnClose);

        dlg.setVisible(true);
    }

    void showAllReservationsDialog() {
        JDialog dlg = new JDialog(parent, "All Reservations", true);
        dlg.setSize(620, 420);
        dlg.setLayout(null);
        dlg.getContentPane().setBackground(Color.WHITE);
        dlg.setLocationRelativeTo(parent);

        JLabel lbl = new JLabel("All Reservations");
        lbl.setBounds(20, 15, 300, 25);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        dlg.add(lbl);

        DefaultListModel<String> model = new DefaultListModel<>();
        if (reservationService.findAllReservations().isEmpty()) {
            model.addElement("No reservations yet.");
        } else {
            for (String[] r : reservationService.findAllReservations())
                model.addElement(r[0] + " | Rm " + r[1]
                    + " | " + r[2] + " - " + r[3] + " | " + r[4]);
        }
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(list);
        sp.setBounds(20, 50, 565, 300);
        dlg.add(sp);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(240, 360, 120, 35);
        btnClose.setBackground(new Color(220, 220, 220));
        btnClose.setForeground(Color.DARK_GRAY);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dlg.dispose());
        dlg.add(btnClose);

        dlg.setVisible(true);
    }

    void refreshLabels() {
        lblTotal.setText("Total reservations: "  + reservationService.countAllReservations());
        lblActive.setText("Active reservations: " + reservationService.countActiveReservations());
    }

    void addLabel(JPanel p, String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 300, 25);
        lbl.setFont(new Font("Arial Black", Font.BOLD, 14));
        p.add(lbl);
    }

    JButton createDarkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial Black", Font.BOLD, 13));
        btn.setBackground(DARK);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
