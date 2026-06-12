package Utilities;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger - Singleton logging utility for the Hotel Reservation System
 * 
 * Provides centralized logging to both file and console output.
 * Logs are written to logs/hotel_system.log with timestamps and severity levels.
 * 
 * @author Hotel Reservation System Team
 * @version 1.0.0
 */
public class Logger {
    
    private static Logger instance;
    private FileWriter fileWriter;
    private DateTimeFormatter formatter;
    
    private Logger() {
        try {
            new File("logs").mkdirs();
            this.fileWriter = new FileWriter("logs/hotel_system.log", true);
            this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        } catch (IOException e) {
            // Log to console if file initialization fails
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }
    
    /**
     * Get singleton instance of Logger
     * 
     * @return Logger instance
     */
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    /**
     * Log info level message
     * 
     * @param message the message to log
     */
    public void info(String message) {
        log("INFO", message);
    }
    
    /**
     * Log warning level message
     * 
     * @param message the message to log
     */
    public void warn(String message) {
        log("WARN", message);
    }
    
    /**
     * Log error level message
     * 
     * @param message the message to log
     * @param e the exception
     */
    public void error(String message, Exception e) {
        log("ERROR", message + " - " + e.getMessage());
    }
    
    /**
     * Log debug level message
     * 
     * @param message the message to log
     */
    public void debug(String message) {
        log("DEBUG", message);
    }
    
    /**
     * Internal method to write log message
     * 
     * @param level the log level
     * @param message the log message
     */
    private void log(String level, String message) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String logMessage = "[" + timestamp + "] " + level + ": " + message + "\n";
            
            // Write to file if available
            if (fileWriter != null) {
                fileWriter.write(logMessage);
                fileWriter.flush();
            }
            
            // Always output to console
            System.out.println(logMessage);
        } catch (IOException e) {
            // If file writing fails, log to console
            System.err.println("Error writing to log file: " + e.getMessage());
            System.err.println("[" + LocalDateTime.now().format(formatter) + "] " + level + ": " + message);
        }
    }
    
    /**
     * Close the logger and release resources
     */
    public void close() {
        try {
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing logger: " + e.getMessage());
        }
    }
}