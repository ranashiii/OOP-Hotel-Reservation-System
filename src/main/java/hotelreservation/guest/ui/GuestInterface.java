package hotelreservation.guest.ui;

import hotelreservation.guest.model.Guest;
import hotelreservation.guest.manager.GuestManager;
import hotelreservation.ui.StyledButton;
import hotelreservation.util.MessageBox;
import hotelreservation.util.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuestInterface extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private GuestManager guestManager;
    private JTable guestTable;
    
    public GuestInterface() {
        this.guestManager = GuestManager.getInstance();
        initWindow();
        createComponents();
        Logger.getInstance().info("GuestInterface opened");
    }
    
    private void initWindow() {
        setTitle("Guest Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
    
    private void createComponents() {
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("View Guests", createViewGuestPanel());
        tabbedPane.addTab("Add Guest", createAddGuestPanel());
        tabbedPane.addTab("Guest Statistics", createStatisticsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createViewGuestPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Guest ID", "Name", "Email", "Phone", "Type"};
        guestTable = new JTable(new String[0][0], columns);
        
        JScrollPane scrollPane = new JScrollPane(guestTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnRefresh = new StyledButton("Refresh", "info");
        btnRefresh.addActionListener(e -> refreshGuestTable());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnRefresh);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAddGuestPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        
        JLabel lblFirstName = new JLabel("First Name:");
        lblFirstName.setBounds(30, 30, 100, 25);
        JTextField txtFirstName = new JTextField();
        txtFirstName.setBounds(140, 30, 250, 25);
        
        JLabel lblLastName = new JLabel("Last Name:");
        lblLastName.setBounds(30, 70, 100, 25);
        JTextField txtLastName = new JTextField();
        txtLastName.setBounds(140, 70, 250, 25);
        
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(30, 110, 100, 25);
        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(140, 110, 250, 25);
        
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(30, 150, 100, 25);
        JTextField txtPhone = new JTextField();
        txtPhone.setBounds(140, 150, 250, 25);
        
        JButton btnAdd = new StyledButton("Add Guest", "success");
        btnAdd.setBounds(140, 200, 120, 35);
        btnAdd.addActionListener(e -> {
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                MessageBox.showWarning("Input Error", "Please fill all fields");
                return;
            }
            
            Guest guest = new Guest(generateGuestID(), firstName, lastName, email, phone);
            if (guestManager.addGuest(guest)) {
                MessageBox.showInfo("Success", "Guest added successfully");
                txtFirstName.setText("");
                txtLastName.setText("");
                txtEmail.setText("");
                txtPhone.setText("");
            } else {
                MessageBox.showError("Error", "Failed to add guest");
            }
        });
        
        panel.add(lblFirstName);
        panel.add(txtFirstName);
        panel.add(lblLastName);
        panel.add(txtLastName);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblPhone);
        panel.add(txtPhone);
        panel.add(btnAdd);
        
        return panel;
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnUpdate = new StyledButton("Update Stats", "info");
        btnUpdate.addActionListener(e -> {
            StringBuilder stats = new StringBuilder();
            stats.append("=== GUEST STATISTICS ===\n\n");
            stats.append("Total Guests: ").append(guestManager.getTotalGuestCount()).append("\n");
            stats.append("VIP Guests: ").append(guestManager.getVIPCount()).append("\n");
            stats.append("Regular Guests: ").append(guestManager.getTotalGuestCount() - guestManager.getVIPCount()).append("\n\n");
            stats.append("=== VIP GUEST LIST ===\n");
            for (Guest guest : guestManager.getVIPGuests()) {
                stats.append(guest.getFullName()).append(" - ").append(guest.getEmail()).append("\n");
            }
            textArea.setText(stats.toString());
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnUpdate);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void refreshGuestTable() {
        Logger.getInstance().info("Guest table refreshed");
    }
    
    private String generateGuestID() {
        return "G" + System.currentTimeMillis();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
