package MainAndGUIAdminService;

import MainAndGUIAdminService.AdminDashboard;
import javax.swing.SwingUtilities;

public class AdminMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}