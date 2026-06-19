package GUIAdmin;

import javax.swing.SwingUtilities;

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        HomePage home = new HomePage();
        home.setVisible(true);
    });
}