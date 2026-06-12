package HotelReservationMainSystem;

import Config.AppConfig;
import Utilities.HotelException;

import javax.swing.*;


public class HotelReservationApp {
    
   
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }
        
        
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
