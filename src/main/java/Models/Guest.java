package Models;

import java.util.Date;

/**
 * Guest - Guest Model Class
 * 
 * Represents a hotel guest with personal information, contact details, and identity verification.
 * Each guest is linked to a user account for authentication.
 * 
 * Properties include personal info, contact details, ID document information, and timestamps.
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
    private Date dateOfBirth;  // Fixed: Using java.util.Date for compatibility
    private String nationality;
    private String idDocumentType;
    private String idDocumentNumber;
    private Date createdAt;
    private Date updatedAt;
    
    // ============ CONSTRUCTORS ============
    
    /**
     * Default constructor
     */
    public Guest() {
    }
    
    /**
     * Constructor with essential guest information
     * 
     * @param userId the associated user ID
     * @param firstName the guest's first name
     * @param lastName the guest's last name
     * @param email the guest's email
     * @param phoneNumber the guest's phone number
     */
    public Guest(int userId, String firstName, String lastName, String email, String phoneNumber) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Constructor with all guest details
     * 
     * @param userId the associated user ID
     * @param firstName the guest's first name
     * @param middleName the guest's middle name
     * @param lastName the guest's last name
     * @param email the guest's email
     * @param phoneNumber the guest's phone number
     * @param address the guest's address
     * @param dateOfBirth the guest's date of birth
     * @param nationality the guest's nationality
     * @param idDocumentType the guest's ID document type
     * @param idDocumentNumber the guest's ID document number
     */
    public Guest(int userId, String firstName, String middleName, String lastName, 
                 String email, String phoneNumber, String address, Date dateOfBirth,
                 String nationality, String idDocumentType, String idDocumentNumber) {
        this.userId = userId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.idDocumentType = idDocumentType;
        this.idDocumentNumber = idDocumentNumber;
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
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
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
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // ============ UTILITY METHODS ============
    
    /**
     * Get full guest name (first + middle + last)
     * 
     * @return full name
     */
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        
        if (firstName != null && !firstName.isEmpty()) {
            sb.append(firstName);
        }
        
        if (middleName != null && !middleName.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(middleName);
        }
        
        if (lastName != null && !lastName.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(lastName);
        }
        
        return sb.toString();
    }
    
    /**
     * Get guest name in format "FirstName LastName"
     * 
     * @return name in format "FirstName LastName"
     */
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        
        if (firstName != null && !firstName.isEmpty()) {
            sb.append(firstName);
        }
        
        if (lastName != null && !lastName.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(lastName);
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nationality='" + nationality + '\'' +
                ", idDocumentType='" + idDocumentType + '\'' +
                ", idDocumentNumber='" + idDocumentNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
