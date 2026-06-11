package com.hotelreservationsystem.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Guest - Guest Model Class
 * 
 * Represents a hotel guest with personal information and identification.
 * Linked to a User account for authentication.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class Guest {
    
    private int guestId;
    private int userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String nationality;
    private String idDocumentType;
    private String idDocumentNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ============ CONSTRUCTORS ============
    public Guest() {}
    
    public Guest(int userId, String firstName, String lastName, String email, 
                 String phoneNumber, String address, LocalDate dateOfBirth) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
    
    // ============ GETTERS & SETTERS ============
    public int getGuestId() {
        return guestId;
    }
    
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public String getIdDocumentType() {
        return idDocumentType;
    }
    
    public void setIdDocumentType(String idDocumentType) {
        this.idDocumentType = idDocumentType;
    }
    
    public String getIdDocumentNumber() {
        return idDocumentNumber;
    }
    
    public void setIdDocumentNumber(String idDocumentNumber) {
        this.idDocumentNumber = idDocumentNumber;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ============ UTILITY METHODS ============
    public String getFullName() {
        StringBuilder name = new StringBuilder(firstName);
        if (middleName != null && !middleName.isEmpty()) {
            name.append(" ").append(middleName);
        }
        name.append(" ").append(lastName);
        return name.toString();
    }
    
    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
