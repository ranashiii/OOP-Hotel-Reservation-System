package Utilities;

import javax.swing.*;

/**
 * MessageBox - Utility class for displaying message dialogs
 * 
 * Provides centralized methods for showing error, success, warning, and info messages
 * with a consistent look and feel across the application.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class MessageBox {
    
    /**
     * Display an error message dialog
     * 
     * @param parent the parent component
     * @param message the error message to display
     * @param title the dialog title
     */
    public static void showError(java.awt.Component parent, String message, String title) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Display an error message dialog with default title
     * 
     * @param parent the parent component
     * @param message the error message to display
     */
    public static void showError(java.awt.Component parent, String message) {
        showError(parent, message, "Error");
    }
    
    /**
     * Display a success message dialog
     * 
     * @param parent the parent component
     * @param message the success message to display
     * @param title the dialog title
     */
    public static void showSuccess(java.awt.Component parent, String message, String title) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Display a success message dialog with default title
     * 
     * @param parent the parent component
     * @param message the success message to display
     */
    public static void showSuccess(java.awt.Component parent, String message) {
        showSuccess(parent, message, "Success");
    }
    
    /**
     * Display a warning message dialog
     * 
     * @param parent the parent component
     * @param message the warning message to display
     * @param title the dialog title
     */
    public static void showWarning(java.awt.Component parent, String message, String title) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    /**
     * Display a warning message dialog with default title
     * 
     * @param parent the parent component
     * @param message the warning message to display
     */
    public static void showWarning(java.awt.Component parent, String message) {
        showWarning(parent, message, "Warning");
    }
    
    /**
     * Display an info message dialog
     * 
     * @param parent the parent component
     * @param message the info message to display
     * @param title the dialog title
     */
    public static void showInfo(java.awt.Component parent, String message, String title) {
        JOptionPane.showMessageDialog(
            parent,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Display an info message dialog with default title
     * 
     * @param parent the parent component
     * @param message the info message to display
     */
    public static void showInfo(java.awt.Component parent, String message) {
        showInfo(parent, message, "Information");
    }
    
    /**
     * Display a confirmation dialog with Yes/No buttons
     * 
     * @param parent the parent component
     * @param message the confirmation message
     * @param title the dialog title
     * @return true if user clicked Yes, false if No
     */
    public static boolean showConfirm(java.awt.Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Display a confirmation dialog with Yes/No buttons and default title
     * 
     * @param parent the parent component
     * @param message the confirmation message
     * @return true if user clicked Yes, false if No
     */
    public static boolean showConfirm(java.awt.Component parent, String message) {
        return showConfirm(parent, message, "Confirm");
    }
    
    /**
     * Display a confirmation dialog with Yes/No/Cancel buttons
     * 
     * @param parent the parent component
     * @param message the confirmation message
     * @param title the dialog title
     * @return JOptionPane.YES_OPTION, NO_OPTION, or CANCEL_OPTION
     */
    public static int showConfirmWithCancel(java.awt.Component parent, String message, String title) {
        return JOptionPane.showConfirmDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    /**
     * Display an input dialog for single line text input
     * 
     * @param parent the parent component
     * @param message the prompt message
     * @param title the dialog title
     * @return the user input or null if cancelled
     */
    public static String showInput(java.awt.Component parent, String message, String title) {
        return JOptionPane.showInputDialog(
            parent,
            message,
            title,
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    /**
     * Display an input dialog with default title
     * 
     * @param parent the parent component
     * @param message the prompt message
     * @return the user input or null if cancelled
     */
    public static String showInput(java.awt.Component parent, String message) {
        return showInput(parent, message, "Input");
    }
}