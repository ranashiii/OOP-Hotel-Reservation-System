package HotelReservationMainSystem;

import Config.AppConfig;
import Config.DBConfig;
import GUILogin.LoginFrame;
import javax.swing.*;

/**
 * Main entry point for Hotel Reservation System
 * Initializes database and launches login
 */
public class HotelReservationApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            try {
                AppConfig.initialize();
                new LoginFrame().setVisible(true);
            } catch (Exception e) {
                showError("Initialization Error", e.getMessage());
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