package com.hotelreservationsystem.service;

import com.hotelreservationsystem.dao.ReservationDAO;
import com.hotelreservationsystem.dao.PaymentDAO;
import com.hotelreservationsystem.dao.RoomDAO;
import com.hotelreservationsystem.model.Reservation;
import com.hotelreservationsystem.util.HotelException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard Service Layer
 * 
 * Handles all dashboard and reporting functionality including metrics,
 * analytics, revenue reports, and occupancy reports.
 */
public class DashboardService {
    private ReservationDAO reservationDAO = new ReservationDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();
    private RoomDAO roomDAO = new RoomDAO();
    
    /**
     * Get today's check-ins
     * 
     * @return List of Reservation objects with today's check-in date
     * @throws HotelException if query fails
     */
    public List<Reservation> getTodayCheckIns() throws HotelException {
        LocalDate today = LocalDate.now();
        List<Reservation> allReservations = reservationDAO.getAllReservations();
        
        return allReservations.stream()
            .filter(r -> r.getCheckInDate().equals(today) && 
                        (r.getReservationStatus().equals("Confirmed") || 
                         r.getReservationStatus().equals("Checked-In")))
            .toList();
    }
    
    /**
     * Get today's check-outs
     * 
     * @return List of Reservation objects with today's check-out date
     * @throws HotelException if query fails
     */
    public List<Reservation> getTodayCheckOuts() throws HotelException {
        LocalDate today = LocalDate.now();
        List<Reservation> allReservations = reservationDAO.getAllReservations();
        
        return allReservations.stream()
            .filter(r -> r.getCheckOutDate().equals(today) && 
                        r.getReservationStatus().equals("Checked-In"))
            .toList();
    }
    
    /**
     * Get available rooms count
     * 
     * @return number of available rooms
     * @throws HotelException if query fails
     */
    public int getAvailableRoomsCount() throws HotelException {
        return roomDAO.getAvailableRoomsCount();
    }
    
    /**
     * Get occupied rooms count
     * 
     * @return number of occupied rooms
     * @throws HotelException if query fails
     */
    public int getOccupiedRoomsCount() throws HotelException {
        return (int) roomDAO.getAllRooms().stream()
            .filter(r -> r.getStatus().equals("Occupied"))
            .count();
    }
    
    /**
     * Get total rooms count
     * 
     * @return total number of rooms
     * @throws HotelException if query fails
     */
    public int getTotalRoomsCount() throws HotelException {
        return roomDAO.getAllRooms().size();
    }
    
    /**
     * Calculate occupancy rate
     * 
     * @return occupancy percentage (0-100)
     * @throws HotelException if calculation fails
     */
    public double getOccupancyRate() throws HotelException {
        int occupied = getOccupiedRoomsCount();
        int total = getTotalRoomsCount();
        
        if (total == 0) {
            return 0.0;
        }
        
        return (double) occupied / total * 100;
    }
    
    /**
     * Get reservations for current month
     * 
     * @return count of reservations in current month
     * @throws HotelException if query fails
     */
    public int getCurrentMonthReservationsCount() throws HotelException {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());
        
        List<Reservation> reservations = reservationDAO.getReservationsByDateRange(monthStart, monthEnd);
        return reservations.size();
    }
    
    /**
     * Get revenue for date range
     * 
     * @param startDate start date
     * @param endDate end date
     * @return total revenue in the period
     * @throws HotelException if query fails
     */
    public BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate) throws HotelException {
        return paymentDAO.getTotalPaymentsByDateRange(startDate, endDate);
    }
    
    /**
     * Get revenue for current month
     * 
     * @return total revenue in current month
     * @throws HotelException if query fails
     */
    public BigDecimal getCurrentMonthRevenue() throws HotelException {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());
        
        return getRevenueByDateRange(monthStart, monthEnd);
    }
    
    /**
     * Get revenue breakdown by payment method
     * 
     * @param startDate start date
     * @param endDate end date
     * @return Map with payment methods and amounts
     * @throws HotelException if query fails
     */
    public Map<String, BigDecimal> getRevenueByPaymentMethod(LocalDate startDate, LocalDate endDate) throws HotelException {
        Map<String, BigDecimal> breakdown = new HashMap<>();
        breakdown.put("Cash", BigDecimal.ZERO);
        breakdown.put("Credit Card", BigDecimal.ZERO);
        breakdown.put("E-Wallet", BigDecimal.ZERO);
        
        List<com.hotelreservationsystem.model.Payment> payments = paymentDAO.getPaymentsByDateRange(startDate, endDate);
        
        for (com.hotelreservationsystem.model.Payment payment : payments) {
            if (payment.getPaymentStatus().equals("Completed")) {
                String method = payment.getPaymentMethod();
                BigDecimal current = breakdown.getOrDefault(method, BigDecimal.ZERO);
                breakdown.put(method, current.add(payment.getPaymentAmount()));
            }
        }
        
        return breakdown;
    }
    
    /**
     * Get revenue by room type
     * 
     * @param startDate start date
     * @param endDate end date
     * @return Map with room types and revenue
     * @throws HotelException if query fails
     */
    public Map<String, BigDecimal> getRevenueByRoomType(LocalDate startDate, LocalDate endDate) throws HotelException {
        Map<String, BigDecimal> breakdown = new HashMap<>();
        
        List<Reservation> reservations = reservationDAO.getReservationsByDateRange(startDate, endDate);
        
        for (Reservation reservation : reservations) {
            if (reservation.getReservationStatus().equals("Checked-Out")) {
                com.hotelreservationsystem.model.Room room = roomDAO.getRoomById(reservation.getRoomId());
                if (room != null) {
                    String roomType = room.getRoomType();
                    BigDecimal current = breakdown.getOrDefault(roomType, BigDecimal.ZERO);
                    breakdown.put(roomType, current.add(reservation.getFinalTotal()));
                }
            }
        }
        
        return breakdown;
    }
    
    /**
     * Get cancellation rate
     * 
     * @param startDate start date
     * @param endDate end date
     * @return cancellation percentage (0-100)
     * @throws HotelException if calculation fails
     */
    public double getCancellationRate(LocalDate startDate, LocalDate endDate) throws HotelException {
        List<Reservation> reservations = reservationDAO.getReservationsByDateRange(startDate, endDate);
        
        if (reservations.isEmpty()) {
            return 0.0;
        }
        
        long cancelled = reservations.stream()
            .filter(r -> r.getReservationStatus().equals("Cancelled"))
            .count();
        
        return (double) cancelled / reservations.size() * 100;
    }
    
    /**
     * Get most booked room type
     * 
     * @param startDate start date
     * @param endDate end date
     * @return room type with most bookings
     * @throws HotelException if query fails
     */
    public String getMostBookedRoomType(LocalDate startDate, LocalDate endDate) throws HotelException {
        Map<String, Integer> counts = new HashMap<>();
        
        List<Reservation> reservations = reservationDAO.getReservationsByDateRange(startDate, endDate);
        
        for (Reservation reservation : reservations) {
            com.hotelreservationsystem.model.Room room = roomDAO.getRoomById(reservation.getRoomId());
            if (room != null) {
                String roomType = room.getRoomType();
                counts.put(roomType, counts.getOrDefault(roomType, 0) + 1);
            }
        }
        
        return counts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
    }
    
    /**
     * Get average length of stay
     * 
     * @param startDate start date
     * @param endDate end date
     * @return average number of nights
     * @throws HotelException if calculation fails
     */
    public double getAverageLengthOfStay(LocalDate startDate, LocalDate endDate) throws HotelException {
        List<Reservation> reservations = reservationDAO.getReservationsByDateRange(startDate, endDate);
        
        if (reservations.isEmpty()) {
            return 0.0;
        }
        
        return reservations.stream()
            .mapToLong(r -> ChronoUnit.DAYS.between(r.getCheckInDate(), r.getCheckOutDate()))
            .average()
            .orElse(0.0);
    }
}
