package HotelReservationMainSystem;

import Config.AppConfig;
import Config.DBConfig;
import Utilities.HotelException;
import GUILogin.LoginFrame;
import javax.swing.*;

/**
 * Main entry point for Hotel Reservation System
 * Initializes database and launches login
 */
public class HotelReservationApp {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                AppConfig.initialize();
                new LoginFrame().setVisible(true);
            } catch (HotelException e) {
                showError("Initialization Error", e.getMessage());
                System.exit(1);
            } catch (Exception e) {
                showError("Error", "Unexpected error: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
    
    private static void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        DBConfig.closeConnection();
    }
}
