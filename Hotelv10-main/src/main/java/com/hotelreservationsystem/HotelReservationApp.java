package com.hotelreservationsystem;

import com.hotelreservationsystem.config.AppConfig;
import com.hotelreservationsystem.util.HotelException;

import javax.swing.*;

/**
 * HotelReservationApp - Main Application Entry Point
 * 
 * This is the main class that launches the Hotel Reservation System application.
 * It initializes the database connection, creates the login frame, and manages
 * the application lifecycle.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class HotelReservationApp {
    
    /**
     * Main method - Application entry point
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set application look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }
        
        // Run GUI initialization on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize application configuration
                AppConfig.initialize();
                
                // Create and display login frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                
            } catch (HotelException e) {
                // Handle initialization errors
                JOptionPane.showMessageDialog(
                    null,
                    "Failed to initialize application: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            } catch (Exception e) {
                // Handle unexpected errors
                JOptionPane.showMessageDialog(
                    null,
                    "Unexpected error during application startup: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
