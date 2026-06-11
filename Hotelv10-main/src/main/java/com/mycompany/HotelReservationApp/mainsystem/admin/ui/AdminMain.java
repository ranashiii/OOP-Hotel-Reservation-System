package com.mycompany.HotelReservationApp.mainsystem;

import com.mycompany.HotelReservationApp.mainsystem.admin.ui.AdminDashboard;
import javax.swing.SwingUtilities;

public class AdminMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}