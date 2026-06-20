package Services;

import DAO.ReservationDAO;
import DAO.RoomDAO;
import DAO.GuestDAO;
import Models.Reservation;
import Models.Room;
import Models.Guest;
import Utilities.HotelException;
import Utilities.ValidationUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Reservation Service Layer
 * 
 * Handles all reservation business logic including booking, cancellation,
 * availability checking, and complex reservation calculations.
 */
public class ReservationService {
    private ReservationDAO reservationDAO = new ReservationDAO();
    private RoomDAO roomDAO = new RoomDAO();
    private GuestDAO guestDAO = new GuestDAO();

    private static final BigDecimal TAX_RATE = new BigDecimal("0.12");

    /**
     * Create new reservation with complete validation and calculation
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
     */
    public boolean isRoomAvailable(int roomId, LocalDate checkInDate, LocalDate checkOutDate) throws HotelException {
        validateReservationDates(checkInDate, checkOutDate);
        return reservationDAO.isRoomAvailable(roomId, checkInDate, checkOutDate);
    }

    /**
     * Get reservation by ID
     */
    public Reservation getReservationById(int reservationId) throws HotelException {
        return reservationDAO.getReservationById(reservationId);
    }

    /**
     * Get all reservations for a guest
     */
    public List<Reservation> getReservationsByGuestId(int guestId) throws HotelException {
        return reservationDAO.getReservationsByGuestId(guestId);
    }

    /**
     * Get all reservations
     */
    public List<Reservation> getAllReservations() throws HotelException {
        return reservationDAO.getAllReservations();
    }

    /**
     * Get reservations by date range
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
     * Get reservations by status
     */
    public List<Reservation> getReservationsByStatus(String status) throws HotelException {
        return reservationDAO.getReservationsByStatus(status);
    }

    /**
     * Cancel a reservation with refund calculation
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
     */
    public boolean updateReservationStatus(int reservationId, String newStatus) throws HotelException {
        if (!isValidReservationStatus(newStatus)) {
            throw new HotelException("Invalid status. Must be: Confirmed, Checked-In, Checked-Out, Cancelled");
        }
        return reservationDAO.updateReservationStatus(reservationId, newStatus);
    }

    private boolean isValidReservationStatus(String status) {
        return status != null &&
               (status.equals("Confirmed") ||
                status.equals("Checked-In") ||
                status.equals("Checked-Out") ||
                status.equals("Cancelled"));
    }

    // =============================================================
    //  ADMIN CONVENIENCE METHODS (added to fix compilation errors)
    // =============================================================

    public int countAllReservations() throws HotelException {
        return getAllReservations().size();
    }

    public int countActiveReservations() throws HotelException {
        int confirmed = getReservationsByStatus("Confirmed").size();
        int checkedIn = getReservationsByStatus("Checked-In").size();
        return confirmed + checkedIn;
    }

    public List<Reservation> findAllReservations() throws HotelException {
        return getAllReservations();
    }

    /**
     * Add reservation using four string parameters (admin UI style).
     * Default number of guests = 1.
     */
    public Reservation addReservation(String guestIdStr, String roomIdStr,
                                      String checkInStr, String checkOutStr) throws HotelException {
        Reservation r = new Reservation();
        r.setGuestId(Integer.parseInt(guestIdStr));
        r.setRoomId(Integer.parseInt(roomIdStr));
        r.setCheckInDate(LocalDate.parse(checkInStr));
        r.setCheckOutDate(LocalDate.parse(checkOutStr));
        r.setNumberOfGuests(1);
        r.setReservationStatus("Confirmed");
        r.setReservationDate(LocalDate.now());
        return createReservation(r);
    }

    /**
     * Cancel reservation without providing a reason (admin uses default).
     */
    public void cancelReservation(int reservationId) throws HotelException {
        cancelReservation(reservationId, "Cancelled by admin");
    }
}