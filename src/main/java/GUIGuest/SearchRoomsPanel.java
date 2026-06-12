package GUIGuest;

import DAO.RoomDAO;
import Models.Room;
import Services.RoomService;
import Utilities.Constants;
import Utilities.HotelException;
import Utilities.MessageBox;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

/**
 * SearchRoomsPanel - Guest room search and filtering interface
 * 
 * Allows guests to search for available rooms by date, type, capacity, and price.
 * Displays results in a table with details and booking option.
 */
public class SearchRoomsPanel extends JPanel {
    
    private RoomService roomService;
    private JTable roomsTable;
    private DefaultTableModel tableModel;
    private JTextField txtCheckInDate, txtCheckOutDate, txtMinPrice, txtMaxPrice;
    private JComboBox<String> cmbRoomType, cmbCapacity;
    private JButton btnSearch, btnReset, btnBook;
    private LocalDate selectedCheckIn, selectedCheckOut;
    private int selectedRoomId;
    
    public SearchRoomsPanel() {
        this.roomService = new RoomService();
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Search Filters"));
        
        panel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        txtCheckInDate = new JTextField();
        panel.add(txtCheckInDate);
        
        panel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        txtCheckOutDate = new JTextField();
        panel.add(txtCheckOutDate);
        
        panel.add(new JLabel("Room Type:"));
        cmbRoomType = new JComboBox<>(new String[]{"All Types", Constants.ROOM_TYPE_SINGLE, 
            Constants.ROOM_TYPE_DOUBLE, Constants.ROOM_TYPE_DOUBLE_DELUXE, Constants.ROOM_TYPE_SUITE});
        panel.add(cmbRoomType);
        
        panel.add(new JLabel("Capacity:"));
        cmbCapacity = new JComboBox<>(new String[]{"All Capacities", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        panel.add(cmbCapacity);
        
        panel.add(new JLabel("Min Price (PHP):"));
        txtMinPrice = new JTextField("0");
        panel.add(txtMinPrice);
        
        panel.add(new JLabel("Max Price (PHP):"));
        txtMaxPrice = new JTextField("99999");
        panel.add(txtMaxPrice);
        
        btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(76, 175, 80));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> searchRooms());
        panel.add(btnSearch);
        
        btnReset = new JButton("Reset");
        btnReset.setBackground(new Color(158, 158, 158));
        btnReset.setForeground(Color.WHITE);
        btnReset.addActionListener(e -> resetFilters());
        panel.add(btnReset);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        tableModel = new DefaultTableModel(
            new String[]{"Room #", "Type", "Floor", "Capacity", "Price/Night", "Amenities"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        roomsTable = new JTable(tableModel);
        roomsTable.setRowHeight(25);
        roomsTable.getSelectionModel().addListSelectionListener(e -> {
            if (roomsTable.getSelectedRow() >= 0) {
                selectedRoomId = (Integer) tableModel.getValueAt(roomsTable.getSelectedRow(), 0);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnBook = new JButton("Book Selected Room");
        btnBook.setBackground(new Color(25, 118, 210));
        btnBook.setForeground(Color.WHITE);
        btnBook.addActionListener(e -> bookRoom());
        panel.add(btnBook);
        
        return panel;
    }
    
    private void searchRooms() {
        try {
            String checkInStr = txtCheckInDate.getText().trim();
            String checkOutStr = txtCheckOutDate.getText().trim();
            
            if (checkInStr.isEmpty() || checkOutStr.isEmpty()) {
                MessageBox.showError("Validation Error", "Check-in and check-out dates are required");
                return;
            }
            
            selectedCheckIn = LocalDate.parse(checkInStr);
            selectedCheckOut = LocalDate.parse(checkOutStr);
            
            if (!selectedCheckOut.isAfter(selectedCheckIn)) {
                MessageBox.showError("Validation Error", "Check-out date must be after check-in date");
                return;
            }
            
            String roomType = (String) cmbRoomType.getSelectedItem();
            String capacityStr = (String) cmbCapacity.getSelectedItem();
            double minPrice = Double.parseDouble(txtMinPrice.getText().trim());
            double maxPrice = Double.parseDouble(txtMaxPrice.getText().trim());
            
            List<Room> rooms = roomService.getAllRooms();
            
            tableModel.setRowCount(0);
            
            for (Room room : rooms) {
                if (!room.isAvailable()) continue;
                if (!roomType.equals("All Types") && !room.getRoomType().equals(roomType)) continue;
                if (!capacityStr.equals("All Capacities") && room.getCapacity() != Integer.parseInt(capacityStr)) continue;
                
                double price = room.getPricePerNight();
                if (price < minPrice || price > maxPrice) continue;
                
                tableModel.addRow(new Object[]{
                    room.getRoomId(),
                    room.getRoomType(),
                    room.getFloor(),
                    room.getCapacity(),
                    String.format("PHP %.2f", price),
                    room.getAmenities() != null ? room.getAmenities() : "None"
                });
            }
            
            MessageBox.showInfo("Search Complete", "Found " + tableModel.getRowCount() + " available rooms");
            
        } catch (Exception e) {
            MessageBox.showError("Search Error", "Error searching rooms: " + e.getMessage());
        }
    }
    
    private void resetFilters() {
        txtCheckInDate.setText("");
        txtCheckOutDate.setText("");
        txtMinPrice.setText("0");
        txtMaxPrice.setText("99999");
        cmbRoomType.setSelectedIndex(0);
        cmbCapacity.setSelectedIndex(0);
        tableModel.setRowCount(0);
    }
    
    private void bookRoom() {
        if (roomsTable.getSelectedRow() < 0) {
            MessageBox.showError("Selection Error", "Please select a room to book");
            return;
        }
        
        if (selectedCheckIn == null || selectedCheckOut == null) {
            MessageBox.showError("Date Error", "Please search for rooms with valid dates first");
            return;
        }
        
        MakeReservationPanel dialog = new MakeReservationPanel();
        dialog.setReservationDetails(selectedRoomId, selectedCheckIn, selectedCheckOut);
        dialog.setVisible(true);
    }
}
