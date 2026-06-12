package GUIReceptionist;

import DAO.ReservationDAO;
import DAO.RoomDAO;
import Models.Reservation;
import Models.Room;
import Utilities.Constants;
import Utilities.MessageBox;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * CheckInPanel - Guest check-in processing interface
 * 
 * Allows receptionists to process guest check-ins, verify reservations,
 * and update room status to occupied.
 */
public class CheckInPanel extends JPanel {
    
    private ReservationDAO reservationDAO;
    private RoomDAO roomDAO;
    
    private JTextField txtReservationId, txtGuestName;
    private JLabel lblRoomInfo, lblCheckInDate, lblCheckOutDate, lblStatus;
    private JButton btnSearch, btnConfirmCheckIn, btnReset;
    private Reservation currentReservation;
    
    public CheckInPanel() {
        this.reservationDAO = new ReservationDAO();
        this.roomDAO = new RoomDAO();
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel detailsPanel = createDetailsPanel();
        add(detailsPanel, BorderLayout.CENTER);
        
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Search Reservation"));
        
        panel.add(new JLabel("Reservation ID:"));
        txtReservationId = new JTextField();
        panel.add(txtReservationId);
        
        btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(25, 118, 210));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> searchReservation());
        panel.add(btnSearch);
        
        panel.add(new JLabel("Or Guest Name:"));
        txtGuestName = new JTextField();
        panel.add(txtGuestName);
        
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        
        return panel;
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Reservation Details"));
        
        panel.add(new JLabel("Room Information:"));
        lblRoomInfo = new JLabel("Not loaded");
        panel.add(lblRoomInfo);
        
        panel.add(new JLabel("Check-in Date:"));
        lblCheckInDate = new JLabel("Not loaded");
        panel.add(lblCheckInDate);
        
        panel.add(new JLabel("Check-out Date:"));
        lblCheckOutDate = new JLabel("Not loaded");
        panel.add(lblCheckOutDate);
        
        panel.add(new JLabel("Status:"));
        lblStatus = new JLabel("Not loaded");
        panel.add(lblStatus);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnConfirmCheckIn = new JButton("Confirm Check-In");
        btnConfirmCheckIn.setBackground(new Color(76, 175, 80));
        btnConfirmCheckIn.setForeground(Color.WHITE);
        btnConfirmCheckIn.setEnabled(false);
        btnConfirmCheckIn.addActionListener(e -> confirmCheckIn());
        panel.add(btnConfirmCheckIn);
        
        btnReset = new JButton("Reset");
        btnReset.setBackground(new Color(158, 158, 158));
        btnReset.setForeground(Color.WHITE);
        btnReset.addActionListener(e -> resetForm());
        panel.add(btnReset);
        
        return panel;
    }
    
    private void searchReservation() {
        try {
            String resIdStr = txtReservationId.getText().trim();
            
            if (resIdStr.isEmpty()) {
                MessageBox.showError("Validation Error", "Please enter reservation ID");
                return;
            }
            
            int resId = Integer.parseInt(resIdStr);
            currentReservation = reservationDAO.getReservationById(resId);
            
            if (currentReservation == null) {
                MessageBox.showError("Not Found", "Reservation not found");
                return;
            }
            
            if (!currentReservation.getReservationStatus().equals(Constants.RESERVATION_STATUS_CONFIRMED)) {
                MessageBox.showError("Invalid Status", "Reservation must be Confirmed for check-in");
                return;
            }
            
            Room room = roomDAO.getRoomById(currentReservation.getRoomId());
            
            lblRoomInfo.setText("Room " + room.getRoomNumber() + " (" + room.getRoomType() + ")");
            lblCheckInDate.setText(currentReservation.getCheckInDate().toString());
            lblCheckOutDate.setText(currentReservation.getCheckOutDate().toString());
            lblStatus.setText(currentReservation.getReservationStatus());
            
            btnConfirmCheckIn.setEnabled(true);
            
        } catch (NumberFormatException e) {
            MessageBox.showError("Error", "Invalid reservation ID format");
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to search reservation: " + e.getMessage());
        }
    }
    
    private void confirmCheckIn() {
        try {
            if (currentReservation == null) {
                MessageBox.showError("Error", "No reservation selected");
                return;
            }
            
            currentReservation.setReservationStatus(Constants.RESERVATION_STATUS_CHECKED_IN);
            currentReservation.setCheckInTime(LocalTime.now());
            
            reservationDAO.updateReservation(currentReservation);
            
            Room room = roomDAO.getRoomById(currentReservation.getRoomId());
            room.setStatus(Constants.ROOM_STATUS_OCCUPIED);
            roomDAO.updateRoom(room);
            
            MessageBox.showInfo("Success", "Guest checked in successfully!");
            resetForm();
            
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to check in guest: " + e.getMessage());
        }
    }
    
    private void resetForm() {
        txtReservationId.setText("");
        txtGuestName.setText("");
        lblRoomInfo.setText("Not loaded");
        lblCheckInDate.setText("Not loaded");
        lblCheckOutDate.setText("Not loaded");
        lblStatus.setText("Not loaded");
        btnConfirmCheckIn.setEnabled(false);
        currentReservation = null;
    }
}