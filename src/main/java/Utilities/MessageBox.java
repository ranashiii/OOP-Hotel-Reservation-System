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
     * Display an error message dialog (2 parameter overload for backwards compatibility)
     * 
     * @param title the dialog title
     * @param message the error message to display
     */
    public static void showError(String title, String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Display an error message dialog (3 parameter version with parent component)
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
     * Display a success message dialog
     * 
     * @param title the dialog title
     * @param message the success message to display
     */
    public static void showSuccess(String title, String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Display a success message dialog (3 parameter version with parent component)
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
     * Display a warning message dialog
     * 
     * @param title the dialog title
     * @param message the warning message to display
     */
    public static void showWarning(String title, String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    /**
     * Display a warning message dialog (3 parameter version with parent component)
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
     * Display an info message dialog
     * 
     * @param title the dialog title
     * @param message the info message to display
     */
    public static void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Display an info message dialog (3 parameter version with parent component)
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
     * Display a confirmation dialog with Yes/No buttons
     * 
     * @param title the dialog title
     * @param message the confirmation message
     * @return JOptionPane.YES_OPTION or JOptionPane.NO_OPTION
     */
    public static int showConfirm(String title, String message) {
        return JOptionPane.showConfirmDialog(
            null,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    /**
     * Display a confirmation dialog with Yes/No buttons (with parent component)
     * 
     * @param parent the parent component
     * @param message the confirmation message
     * @param title the dialog title
     * @return JOptionPane.YES_OPTION or JOptionPane.NO_OPTION
     */
    public static int showConfirm(java.awt.Component parent, String message, String title) {
        return JOptionPane.showConfirmDialog(
            parent,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    /**
     * Display a confirmation dialog with Yes/No/Cancel buttons
     * 
     * @param title the dialog title
     * @param message the confirmation message
     * @return JOptionPane.YES_OPTION, NO_OPTION, or CANCEL_OPTION
     */
    public static int showConfirmWithCancel(String title, String message) {
        return JOptionPane.showConfirmDialog(
            null,
            message,
            title,
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    /**
     * Display an input dialog for single line text input
     * 
     * @param title the dialog title
     * @param message the prompt message
     * @return the user input or null if cancelled
     */
    public static String showInput(String title, String message) {
        return JOptionPane.showInputDialog(
            null,
            message,
            title
        );
    }
}