package Config;

import Utilities.HotelException;
import java.awt.*;

/**
 * AppConfig - Application Configuration and UI Constants
 * 
 * Centralized configuration for application-wide settings including
 * window sizes, colors, fonts, and visual styling.
 */
public class AppConfig {
    
    // ============ WINDOW SIZES ============
    public static final int LOGIN_WINDOW_WIDTH = 600;
    public static final int LOGIN_WINDOW_HEIGHT = 500;
    public static final int MAIN_WINDOW_WIDTH = 1000;
    public static final int MAIN_WINDOW_HEIGHT = 700;
    public static final int PANEL_WIDTH = 960;
    public static final int PANEL_HEIGHT = 650;
    
    // ============ COLORS ============
    public static final Color COLOR_PRIMARY = new Color(25, 118, 210);
    public static final Color COLOR_PRIMARY_DARK = new Color(13, 71, 161);
    public static final Color COLOR_ACCENT = new Color(255, 87, 34);
    public static final Color COLOR_SUCCESS = new Color(76, 175, 80);
    public static final Color COLOR_ERROR = new Color(244, 67, 54);
    public static final Color COLOR_WARNING = new Color(255, 152, 0);
    public static final Color COLOR_BACKGROUND = new Color(245, 245, 245);
    public static final Color COLOR_BORDER = new Color(200, 200, 200);
    public static final Color COLOR_TEXT = new Color(33, 33, 33);
    public static final Color COLOR_TEXT_LIGHT = new Color(117, 117, 117);
    
    // ============ FONTS ============
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Arial", Font.BOLD, 16);
    public static final Font FONT_HEADER = new Font("Arial", Font.BOLD, 14);
    public static final Font FONT_LABEL = new Font("Arial", Font.BOLD, 12);
    public static final Font FONT_REGULAR = new Font("Arial", Font.PLAIN, 12);
    public static final Font FONT_SMALL = new Font("Arial", Font.PLAIN, 10);
    public static final Font FONT_BUTTON = new Font("Arial", Font.PLAIN, 12);
    
    // ============ BUTTON SIZES ============
    public static final int BUTTON_WIDTH = 100;
    public static final int BUTTON_HEIGHT = 30;
    public static final int BUTTON_SMALL_WIDTH = 80;
    public static final int BUTTON_SMALL_HEIGHT = 25;
    public static final int BUTTON_LARGE_WIDTH = 150;
    public static final int BUTTON_LARGE_HEIGHT = 40;
    
    // ============ PADDING & MARGINS ============
    public static final int PADDING = 15;
    public static final int MARGIN = 10;
    public static final int GAP = 5;
    
    /**
     * Initialize application configuration
     */
    public static void initialize() throws HotelException {
        DBConfig.initializeDriver();
        
        if (!DBConfig.testConnection()) {
            throw new HotelException("Failed to connect to database. Check database configuration.");
        }
    }
    
    /**
     * Shutdown application configuration
     */
    public static void shutdown() {
        DBConfig.closeConnection();
    }
}
