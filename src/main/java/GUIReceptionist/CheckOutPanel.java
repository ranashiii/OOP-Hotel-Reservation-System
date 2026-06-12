package GUIReceptionist;

import DAO.ReservationDAO;
import DAO.RoomDAO;
import Models.Reservation;
import Models.Room;
import Services.DashboardService;
import Utilities.Constants;
import Utilities.CurrencyUtil;
import Utilities.DateUtil;
import Utilities.MessageBox;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * CheckOutPanel - Guest check-out processing interface
 * 
 * Allows receptionists to process guest check-outs, calculate final charges
 * including late checkout fees, and collect payment.
 */
public class CheckOutPanel extends JPanel {
    
    private ReservationDAO reservationDAO;
    private RoomDAO roomDAO;
    
    private JTextField txtReservationId;
    private JLabel lblRoomInfo, lblCheckOutDate, lblOriginalTotal;
    private JLabel lblLateCheckoutFee, lblAdditionalCharges, lblFinalAmount;
    private JButton btnSearch, btnConfirmCheckOut, btnReset;
    private Reservation currentReservation;
    
    public CheckOutPanel() {
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
        
        JPanel chargesPanel = createChargesPanel();
        add(chargesPanel, BorderLayout.CENTER);
        
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Search Reservation"));
        
        panel.add(new JLabel("Reservation ID:"));
        txtReservationId = new JTextField();
        panel.add(txtReservationId);
        
        btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(33, 150, 243));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> searchReservation());
        panel.add(btnSearch);
        
        return panel;
    }
    
    private JPanel createChargesPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Checkout Charges"));
        
        panel.add(new JLabel("Room Information:"));
        lblRoomInfo = new JLabel("Not loaded");
        lblRoomInfo.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblRoomInfo);
        
        panel.add(new JLabel("Check-out Date:"));
        lblCheckOutDate = new JLabel("Not loaded");
        panel.add(lblCheckOutDate);
        
        panel.add(new JLabel("Original Total:"));
        lblOriginalTotal = new JLabel("PHP 0.00");
        panel.add(lblOriginalTotal);
        
        panel.add(new JLabel("Late Checkout Fee:"));
        lblLateCheckoutFee = new JLabel("PHP 0.00");
        lblLateCheckoutFee.setForeground(new Color(244, 67, 54));
        panel.add(lblLateCheckoutFee);
        
        panel.add(new JLabel("Additional Charges:"));
        lblAdditionalCharges = new JLabel("PHP 0.00");
        panel.add(lblAdditionalCharges);
        
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        
        panel.add(new JLabel("FINAL AMOUNT DUE:"));
        lblFinalAmount = new JLabel("PHP 0.00");
        lblFinalAmount.setFont(new Font("Arial", Font.BOLD, 16));
        lblFinalAmount.setForeground(new Color(0, 128, 0));
        panel.add(lblFinalAmount);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnConfirmCheckOut = new JButton("Confirm Check-Out");
        btnConfirmCheckOut.setBackground(new Color(76, 175, 80));
        btnConfirmCheckOut.setForeground(Color.WHITE);
        btnConfirmCheckOut.setEnabled(false);
        btnConfirmCheckOut.addActionListener(e -> confirmCheckOut());
        panel.add(btnConfirmCheckOut);
        
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
            
            if (!currentReservation.getReservationStatus().equals(Constants.RESERVATION_STATUS_CHECKED_IN)) {
                MessageBox.showError("Invalid Status", "Guest must be checked-in to process checkout");
                return;
            }
            
            Room room = roomDAO.getRoomById(currentReservation.getRoomId());
            
            lblRoomInfo.setText("Room " + room.getRoomNumber() + " (" + room.getRoomType() + ")");
            lblCheckOutDate.setText(currentReservation.getCheckOutDate().toString());
            lblOriginalTotal.setText(CurrencyUtil.formatCurrency(currentReservation.getFinalTotal()));
            
            double lateCheckoutFee = 0;
            LocalTime actualCheckoutTime = LocalTime.now();
            if (actualCheckoutTime.getHour() > 11) {
                lateCheckoutFee = DateUtil.calculateLateCheckoutFee(actualCheckoutTime);
            }
            
            lblLateCheckoutFee.setText(CurrencyUtil.formatCurrency(new BigDecimal(lateCheckoutFee)));
            lblAdditionalCharges.setText("PHP 0.00");
            
            BigDecimal finalAmount = currentReservation.getFinalTotal().add(new BigDecimal(lateCheckoutFee));
            lblFinalAmount.setText(CurrencyUtil.formatCurrency(finalAmount));
            
            btnConfirmCheckOut.setEnabled(true);
            
        } catch (NumberFormatException e) {
            MessageBox.showError("Error", "Invalid reservation ID format");
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to search reservation: " + e.getMessage());
        }
    }
    
    private void confirmCheckOut() {
        try {
            if (currentReservation == null) {
                MessageBox.showError("Error", "No reservation selected");
                return;
            }
            
            currentReservation.setReservationStatus(Constants.RESERVATION_STATUS_CHECKED_OUT);
            currentReservation.setCheckOutTime(LocalTime.now());
            
            reservationDAO.updateReservation(currentReservation);
            
            Room room = roomDAO.getRoomById(currentReservation.getRoomId());
            room.setStatus(Constants.ROOM_STATUS_AVAILABLE);
            roomDAO.updateRoom(room);
            
            MessageBox.showInfo("Success", "Guest checked out successfully!\nFinal amount: " + lblFinalAmount.getText());
            resetForm();
            
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to check out guest: " + e.getMessage());
        }
    }
    
    private void resetForm() {
        txtReservationId.setText("");
        lblRoomInfo.setText("Not loaded");
        lblCheckOutDate.setText("Not loaded");
        lblOriginalTotal.setText("PHP 0.00");
        lblLateCheckoutFee.setText("PHP 0.00");
        lblAdditionalCharges.setText("PHP 0.00");
        lblFinalAmount.setText("PHP 0.00");
        btnConfirmCheckOut.setEnabled(false);
        currentReservation = null;
    }
}
