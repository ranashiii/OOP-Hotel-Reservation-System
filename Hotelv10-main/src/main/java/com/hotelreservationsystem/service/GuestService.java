package com.hotelreservationsystem.service;

import com.hotelreservationsystem.dao.GuestDAO;
import com.hotelreservationsystem.dao.UserDAO;
import com.hotelreservationsystem.model.Guest;
import com.hotelreservationsystem.model.User;
import com.hotelreservationsystem.util.HotelException;
import com.hotelreservationsystem.util.ValidationUtil;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Guest Service Layer
 * 
 * Handles all guest-related business logic including registration,
 * profile management, and validation.
 */
public class GuestService {
    private GuestDAO guestDAO = new GuestDAO();
    private UserDAO userDAO = new UserDAO();
    
    /**
     * Register a new guest profile
     * 
     * @param guest the guest object with details
     * @return Guest object if registration successful
     * @throws HotelException if registration fails or validation fails
     */
    public Guest registerGuest(Guest guest) throws HotelException {
        if (guest == null) {
            throw new HotelException("Guest object cannot be null");
        }
        
        if (!ValidationUtil.validateName(guest.getFirstName())) {
            throw new HotelException("First name is invalid (letters only, 2-50 characters)");
        }
        
        if (!ValidationUtil.validateName(guest.getLastName())) {
            throw new HotelException("Last name is invalid (letters only, 2-50 characters)");
        }
        
        if (!ValidationUtil.validateEmail(guest.getEmail())) {
            throw new HotelException("Invalid email format");
        }
        
        if (!ValidationUtil.validatePhoneNumber(guest.getPhoneNumber())) {
            throw new HotelException("Invalid phone number format (must be Philippine format)");
        }
        
        if (!validateDateOfBirth(guest.getDateOfBirth())) {
            throw new HotelException("Guest must be at least 18 years old");
        }
        
        if (guest.getAddress() == null || guest.getAddress().trim().isEmpty()) {
            throw new HotelException("Address cannot be empty");
        }
        
        if (guest.getIdDocumentNumber() == null || guest.getIdDocumentNumber().length() < 5) {
            throw new HotelException("ID document number must be at least 5 characters");
        }
        
        User user = userDAO.getUserById(guest.getUserId());
        if (user == null) {
            throw new HotelException("User not found");
        }
        
        int guestId = guestDAO.createGuest(guest);
        guest.setGuestId(guestId);
        return guest;
    }
    
    /**
     * Get guest by guest ID
     * 
     * @param guestId the guest ID
     * @return Guest object if found, null otherwise
     * @throws HotelException if query fails
     */
    public Guest getGuestById(int guestId) throws HotelException {
        return guestDAO.getGuestById(guestId);
    }
    
    /**
     * Get guest by user ID
     * 
     * @param userId the user ID
     * @return Guest object if found, null otherwise
     * @throws HotelException if query fails
     */
    public Guest getGuestByUserId(int userId) throws HotelException {
        return guestDAO.getGuestByUserId(userId);
    }
    
    /**
     * Update guest profile
     * 
     * @param guest the guest object with updated information
     * @return true if update successful
     * @throws HotelException if update fails or validation fails
     */
    public boolean updateGuest(Guest guest) throws HotelException {
        if (guest == null || guest.getGuestId() == 0) {
            throw new HotelException("Invalid guest object");
        }
        
        if (!ValidationUtil.validateName(guest.getFirstName())) {
            throw new HotelException("First name is invalid");
        }
        
        if (!ValidationUtil.validateName(guest.getLastName())) {
            throw new HotelException("Last name is invalid");
        }
        
        if (!ValidationUtil.validateEmail(guest.getEmail())) {
            throw new HotelException("Invalid email format");
        }
        
        if (!ValidationUtil.validatePhoneNumber(guest.getPhoneNumber())) {
            throw new HotelException("Invalid phone number format");
        }
        
        return guestDAO.updateGuest(guest);
    }
    
    /**
     * Get all guests
     * 
     * @return List of all Guest objects
     * @throws HotelException if query fails
     */
    public List<Guest> getAllGuests() throws HotelException {
        return guestDAO.getAllGuests();
    }
    
    /**
     * Search guests by name
     * 
     * @param searchName part of guest name to search
     * @return List of Guest objects matching search
     * @throws HotelException if query fails
     */
    public List<Guest> searchGuestsByName(String searchName) throws HotelException {
        if (searchName == null || searchName.trim().isEmpty()) {
            throw new HotelException("Search name cannot be empty");
        }
        return guestDAO.searchGuestsByName(searchName);
    }
    
    /**
     * Validate date of birth (must be 18+ years old)
     * 
     * @param dateOfBirth the date of birth to validate
     * @return true if valid (18+ years old), false otherwise
     */
    private boolean validateDateOfBirth(java.util.Date dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        
        LocalDate birthDate = new java.sql.Date(dateOfBirth.getTime()).toLocalDate();
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthDate, today);
        
        return period.getYears() >= 18;
    }
}
