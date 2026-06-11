package com.hotelreservationsystem.service;

import com.hotelreservationsystem.dao.ReservationDAO;
import com.hotelreservationsystem.dao.RoomDAO;
import com.hotelreservationsystem.dao.GuestDAO;
import com.hotelreservationsystem.model.Reservation;
import com.hotelreservationsystem.model.Room;
import com.hotelreservationsystem.model.Guest;
import com.hotelreservationsystem.util.HotelException;
import com.hotelreservationsystem.util.ValidationUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Reservation Service Layer
 * 
 * Handles all reservation business logic including booking, cancellation,
 * availability checking, and complex reservation calculations.
 * This is the MOST CRITICAL service layer.
 */
public class ReservationService {
    private ReservationDAO reservationDAO = new ReservationDAO();
    private RoomDAO roomDAO = new RoomDAO();
    private GuestDAO guestDAO = new GuestDAO();
    
    private static final BigDecimal TAX_RATE = new BigDecimal("0.12");
    
    /**
     * Create new reservation with complete validation and calculation
     * 
     * @param reservation the reservation object with details
     * @return Reservation object if creation successful
     * @throws HotelException if creation fails or validation fails
     */
    public Reservation createReservation(Reservation reservation) throws HotelException {
        if (reservation == null) {
            throw new HotelException("Reservation object cannot be null");
        }
        
        validateReservationDates(reservation.getCheckInDate(), reservation.getCheckOutDate());
        
        if (reservation.getNumberOfGuests() < 1) {
            throw new HotelException("Number of guests must be at least 1");
        }
        
        Room room = roomDAO.getRoomById(reservation.getRoomId());
        if (room == null) {
            throw new HotelException("Room not found");
        }
        
        if (reservation.getNumberOfGuests() > room.getCapacity()) {
            throw new HotelException("Number of guests exceeds room capacity of " + room.getCapacity());
        }
        
        Guest guest = guestDAO.getGuestById(reservation.getGuestId());
        if (guest == null) {
            throw new HotelException("Guest not found");
        }
        
        if (!reservationDAO.isRoomAvailable(reservation.getRoomId(), 
                                             reservation.getCheckInDate(), 
                                             reservation.getCheckOutDate())) {
            throw new HotelException("Room is not available for selected dates");
        }
        
        calculateReservationPrices(reservation, room);
        
        reservation.setReservationStatus("Confirmed");
        reservation.setReservationDate(LocalDate.now());
        
        int reservationId = reservationDAO.createReservation(reservation);
        reservation.setReservationId(reservationId);
        return reservation;
    }
    
    /**
     * Calculate reservation prices (nights, subtotal, tax, final total)
     * 
     * @param reservation the reservation to calculate prices for
     * @param room the room being reserved
     * @throws HotelException if calculation fails
     */
    private void calculateReservationPrices(Reservation reservation, Room room) throws HotelException {
        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        if (nights <= 0) {
            throw new HotelException("Check-out date must be after check-in date");
        }
        
        reservation.setNumberOfNights((int) nights);
        reservation.setRoomRate(room.getPricePerNight());
        
        BigDecimal subtotal = room.getPricePerNight().multiply(new BigDecimal(nights));
        BigDecimal tax = subtotal.multiply(TAX_RATE);
        BigDecimal discountApplied = reservation.getDiscountApplied() != null ? reservation.getDiscountApplied() : BigDecimal.ZERO;
        BigDecimal finalTotal = subtotal.add(tax).subtract(discountApplied);
        
        reservation.setTotalPrice(subtotal);
        reservation.setFinalTotal(finalTotal);
    }
    
    /**
     * Validate check-in and check-out dates
     * 
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @throws HotelException if dates are invalid
     */
    private void validateReservationDates(LocalDate checkInDate, LocalDate checkOutDate) throws HotelException {
        if (checkInDate == null || checkOutDate == null) {
            throw new HotelException("Check-in and check-out dates cannot be empty");
        }
        
        LocalDate today = LocalDate.now();
        if (checkInDate.isBefore(today)) {
            throw new HotelException("Check-in date cannot be in the past");
        }
        
        if (checkOutDate.isBefore(today)) {
            throw new HotelException("Check-out date cannot be in the past");
        }
        
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new HotelException("Check-out date must be after check-in date");
        }
        
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights < 1) {
            throw new HotelException("Minimum stay is 1 night");
        }
        
        if (nights > 365) {
            throw new HotelException("Maximum stay is 365 nights");
        }
    }
    
    /**
     * Check room availability for date range
     * 
     * @param roomId the room ID
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if room is available, false otherwise
     * @throws HotelException if check fails
     */
    public boolean isRoomAvailable(int roomId, LocalDate checkInDate, LocalDate checkOutDate) throws HotelException {
        validateReservationDates(checkInDate, checkOutDate);
        return reservationDAO.isRoomAvailable(roomId, checkInDate, checkOutDate);
    }
    
    /**
     * Get reservation by ID
     * 
     * @param reservationId the reservation ID
     * @return Reservation object if found, null otherwise
     * @throws HotelException if query fails
     */
    public Reservation getReservationById(int reservationId) throws HotelException {
        return reservationDAO.getReservationById(reservationId);
    }
    
    /**
     * Get all reservations for a guest
     * 
     * @param guestId the guest ID
     * @return List of Reservation objects for the guest
     * @throws HotelException if query fails
     */
    public List<Reservation> getReservationsByGuestId(int guestId) throws HotelException {
        return reservationDAO.getReservationsByGuestId(guestId);
    }
    
    /**
     * Get all reservations
     * 
     * @return List of all Reservation objects
     * @throws HotelException if query fails
     */
    public List<Reservation> getAllReservations() throws HotelException {
        return reservationDAO.getAllReservations();
    }
    
    /**
     * Get reservations by date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return List of Reservation objects within date range
     * @throws HotelException if query fails
     */
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        if (startDate == null || endDate == null) {
            throw new HotelException("Date range cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new HotelException("End date must be after start date");
        }
        return reservationDAO.getReservationsByDateRange(startDate, endDate);
    }
    
    /**
     * Cancel a reservation with refund calculation
     * 
     * @param reservationId the reservation ID to cancel
     * @param cancellationReason the reason for cancellation
     * @return refund amount based on cancellation policy
     * @throws HotelException if cancellation fails
     */
    public BigDecimal cancelReservation(int reservationId, String cancellationReason) throws HotelException {
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation == null) {
            throw new HotelException("Reservation not found");
        }
        
        if (reservation.getReservationStatus().equals("Cancelled")) {
            throw new HotelException("Reservation is already cancelled");
        }
        
        if (reservation.getReservationStatus().equals("Checked-Out")) {
            throw new HotelException("Cannot cancel a checked-out reservation");
        }
        
        BigDecimal refundAmount = calculateRefund(reservation);
        
        reservationDAO.cancelReservation(reservationId, cancellationReason);
        
        return refundAmount;
    }
    
    /**
     * Calculate refund amount based on cancellation policy
     * More than 7 days before: 100% refund
     * Exactly 7 days before: 90% refund
     * Less than 7 days before: No refund
     * After check-in: No refund
     * 
     * @param reservation the reservation to calculate refund for
     * @return refund amount
     */
    private BigDecimal calculateRefund(Reservation reservation) {
        LocalDate today = LocalDate.now();
        long daysBeforeCheckIn = ChronoUnit.DAYS.between(today, reservation.getCheckInDate());
        
        BigDecimal totalAmount = reservation.getFinalTotal();
        
        if (daysBeforeCheckIn > 7) {
            return totalAmount;
        } else if (daysBeforeCheckIn == 7) {
            return totalAmount.multiply(new BigDecimal("0.90"));
        } else {
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Update reservation status
     * 
     * @param reservationId the reservation ID
     * @param newStatus the new status
     * @return true if update successful
     * @throws HotelException if update fails or validation fails
     */
    public boolean updateReservationStatus(int reservationId, String newStatus) throws HotelException {
        if (!isValidReservationStatus(newStatus)) {
            throw new HotelException("Invalid status. Must be: Confirmed, Checked-In, Checked-Out, Cancelled");
        }
        return reservationDAO.updateReservationStatus(reservationId, newStatus);
    }
    
    /**
     * Validate reservation status
     * Valid statuses: Confirmed, Checked-In, Checked-Out, Cancelled
     * 
     * @param status the status to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidReservationStatus(String status) {
        return status != null && 
               (status.equals("Confirmed") || 
                status.equals("Checked-In") || 
                status.equals("Checked-Out") || 
                status.equals("Cancelled"));
    }
}
