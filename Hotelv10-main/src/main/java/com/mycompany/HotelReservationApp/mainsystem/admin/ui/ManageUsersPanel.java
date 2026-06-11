package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import javax.swing.*;

/**
 * ManageUsersPanel - Alias for UserManagementPanel
 * Provides user management functionality (add, remove, view, edit)
 * This class extends UserManagementPanel to maintain naming consistency
 */
public class ManageUsersPanel extends UserManagementPanel {
    
    public ManageUsersPanel() {
        super(null, null);
    }
    
    public ManageUsersPanel(JFrame parent, Object userService) {
        super(parent, (com.mycompany.HotelReservationApp.mainsystem.admin.service.UserService) userService);
    }
}
