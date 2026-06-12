package Config;

import java.awt.*;
import javax.swing.UIManager;

/**
 * AppConfig - Application Configuration and Styling
 * 
 * Centralized configuration for UI styling, colors, fonts, and window sizes.
 * Ensures consistent look and feel across all GUI components.
 */
public class AppConfig {
    
    // Window sizes
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;
    public static final int LOGIN_WINDOW_WIDTH = 600;
    public static final int LOGIN_WINDOW_HEIGHT = 500;
    public static final int DIALOG_WIDTH = 500;
    public static final int DIALOG_HEIGHT = 300;
    
    // Colors - Primary palette
    public static final Color COLOR_PRIMARY = new Color(33, 150, 243);
    public static final Color COLOR_PRIMARY_DARK = new Color(21, 101, 192);
    public static final Color COLOR_PRIMARY_LIGHT = new Color(227, 242, 253);
    
    // Colors - Secondary palette
    public static final Color COLOR_ACCENT = new Color(255, 152, 0);
    public static final Color COLOR_ACCENT_DARK = new Color(255, 111, 0);
    
    // Colors - Status
    public static final Color COLOR_SUCCESS = new Color(76, 175, 80);
    public static final Color COLOR_ERROR = new Color(244, 67, 54);
    public static final Color COLOR_WARNING = new Color(255, 193, 7);
    public static final Color COLOR_INFO = new Color(33, 150, 243);
    
    // Colors - UI Elements
    public static final Color COLOR_BORDER = new Color(189, 189, 189);
    public static final Color COLOR_BACKGROUND = new Color(245, 245, 245);
    public static final Color COLOR_TEXT = new Color(33, 33, 33);
    public static final Color COLOR_TEXT_LIGHT = new Color(117, 117, 117);
    
    // Colors - Role-based
    public static final Color COLOR_ADMIN = new Color(211, 47, 47);
    public static final Color COLOR_RECEPTIONIST = new Color(0, 121, 107);
    public static final Color COLOR_GUEST = new Color(25, 118, 210);
    
    // Fonts
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("Arial", Font.BOLD, 20);
    public static final Font FONT_LABEL = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_REGULAR = new Font("Arial", Font.PLAIN, 12);
    public static final Font FONT_SMALL = new Font("Arial", Font.PLAIN, 10);
    public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 12);
    
    // Button sizes
    public static final int BUTTON_WIDTH = 120;
    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_SMALL_WIDTH = 80;
    public static final int BUTTON_SMALL_HEIGHT = 30;
    
    // Component sizes
    public static final int TEXT_FIELD_HEIGHT = 35;
    public static final int COMBO_BOX_HEIGHT = 35;
    public static final int TABLE_ROW_HEIGHT = 25;
    
    // Padding
    public static final int PADDING_LARGE = 20;
    public static final int PADDING_MEDIUM = 15;
    public static final int PADDING_SMALL = 10;
    
    /**
     * Initialize application configuration
     */
    public static void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
        }
    }
    
    /**
     * Get color based on access level
     */
    public static Color getColorForAccessLevel(String accessLevel) {
        if ("Admin".equalsIgnoreCase(accessLevel)) {
            return COLOR_ADMIN;
        } else if ("Receptionist".equalsIgnoreCase(accessLevel)) {
            return COLOR_RECEPTIONIST;
        } else if ("Guest".equalsIgnoreCase(accessLevel)) {
            return COLOR_GUEST;
        }
        return COLOR_PRIMARY;
    }
}
