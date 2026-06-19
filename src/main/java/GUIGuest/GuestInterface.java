package GUIGuest;

import javax.swing.*;

public class GuestInterface {
    public static void main(String[] args) {
        GuestDashboard GD = new GuestDashboard();
        GD.setVisible(true);
        
            // TODO: DB CONNECT - After login is verified, open GuestDashBoard as the default landing page
            // Example: new LoginFrame().setVisible(true);
            // After successful login: new GuestDashboard().setVisible(true);
            
    }
}
