package GUIGuest;

import DAO.GuestDAO;
import DAO.ReservationDAO;
import Models.Guest;
import Models.Reservation;
import Models.Room;
import Services.ReservationService;
import Services.RoomService;
import HotelReservationMainSystem.SessionManager;
import Utilities.Constants;
import Utilities.CurrencyUtil;
import Utilities.MessageBox;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * MakeReservationPanel - Dialog for creating a new reservation
 * 
 * Allows guests to complete reservation process with date validation,
 * price calculation, and confirmation.
 */
public class MakeReservationPanel extends JDialog {
    
    private ReservationService reservationService;
    private RoomService roomService;
    private GuestDAO guestDAO;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private JLabel lblRoomInfo, lblTotalPrice, lblTax, lblFinalTotal;
    private JSpinner spinnerGuests;
    private JButton btnConfirm, btnCancel;
    
    public MakeReservationPanel() {
        this.reservationService = new ReservationService();
        this.roomService = new RoomService();
        this.guestDAO = new GuestDAO();
        
        setTitle("Make Reservation");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
    }
    
    public void setReservationDetails(int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel detailsPanel = createDetailsPanel();
        add(detailsPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        
        try {
            Room room = roomService.getRoomById(roomId);
            
            panel.add(new JLabel("Room Number:"));
            lblRoomInfo = new JLabel(room.getRoomNumber() + " (" + room.getRoomType() + ")");
            lblRoomInfo.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(lblRoomInfo);
            
            panel.add(new JLabel("Check-in Date:"));
            panel.add(new JLabel(checkInDate.toString()));
            
            panel.add(new JLabel("Check-out Date:"));
            panel.add(new JLabel(checkOutDate.toString()));
            
            long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            panel.add(new JLabel("Number of Nights:"));
            panel.add(new JLabel(nights + " nights"));
            
            panel.add(new JLabel("Number of Guests:"));
            spinnerGuests = new JSpinner(new SpinnerNumberModel(1, 1, room.getCapacity(), 1));
            panel.add(spinnerGuests);
            
            double roomRate = room.getPricePerNight();
            BigDecimal subtotal = CurrencyUtil.calculateSubtotal(new BigDecimal(roomRate), (int) nights);
            BigDecimal tax = CurrencyUtil.calculateTax(subtotal);
            BigDecimal finalTotal = CurrencyUtil.calculateFinalTotal(subtotal, BigDecimal.ZERO);
            
            panel.add(new JLabel("Room Rate/Night:"));
            panel.add(new JLabel(CurrencyUtil.formatCurrency(new BigDecimal(roomRate))));
            
            panel.add(new JLabel("Subtotal:"));
            panel.add(new JLabel(CurrencyUtil.formatCurrency(subtotal)));
            
            panel.add(new JLabel("Tax (12%):"));
            lblTax = new JLabel(CurrencyUtil.formatCurrency(tax));
            panel.add(lblTax);
            
            panel.add(new JLabel("Final Total:"));
            lblFinalTotal = new JLabel(CurrencyUtil.formatCurrency(finalTotal));
            lblFinalTotal.setFont(new Font("Arial", Font.BOLD, 14));
            lblFinalTotal.setForeground(new Color(0, 128, 0));
            panel.add(lblFinalTotal);
            
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to load room details: " + e.getMessage());
        }
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnConfirm = new JButton("Confirm Reservation");
        btnConfirm.setBackground(new Color(76, 175, 80));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addActionListener(e -> confirmReservation());
        panel.add(btnConfirm);
        
        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(244, 67, 54));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(e -> dispose());
        panel.add(btnCancel);
        
        return panel;
    }
    
    private void confirmReservation() {
        try {
            int numberOfGuests = (Integer) spinnerGuests.getValue();
            int userId = SessionManager.getInstance().getCurrentUserId();
            
            Guest guest = guestDAO.getGuestByUserId(userId);
            if (guest == null) {
                MessageBox.showError("Error", "Guest profile not found. Please create a guest profile first.");
                return;
            }
            
            Reservation reservation = new Reservation();
            reservation.setGuestId(guest.getGuestId());
            reservation.setRoomId(roomId);
            reservation.setCheckInDate(checkInDate);
            reservation.setCheckOutDate(checkOutDate);
            reservation.setNumberOfGuests(numberOfGuests);
            
            Reservation created = reservationService.createReservation(reservation);
            
            MessageBox.showInfo("Success", "Reservation created successfully!\nReservation ID: " + created.getReservationId());
            dispose();
            
        } catch (Exception e) {
            MessageBox.showError("Reservation Error", "Failed to create reservation: " + e.getMessage());
        }
    }
}
