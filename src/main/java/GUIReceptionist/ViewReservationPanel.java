package GUIReceptionist;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;

public class ViewReservationPanel extends JFrame implements ActionListener{

    private JButton btnSideDash, btnSideRegister, btnSideRoomBooking, btnSidePay, btnSideReserve,
            btnSideCancel, btnHomePage, btnRefresh, btnCancelSelected;
    private JLabel lblReception, lblHotel, lblManagement, lblDate, lblRecent, lblTotalReservations;
    private JPanel sidePan, topPan, dashboardPanel, recentPanel;
    private JTable tblReservations;
    private DefaultTableModel reservationTableModel;
    
    private Object[][] reservationData;

    ViewReservationPanel(){
        this.reservationData = new Object[0][];

        sidePanel();
        functionMenu();
        topPanel();
        reservationPan();
        refreshDisplay();

        setSize(1200,700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Hotel Reservation System - Receptionist View Reservation");
    }

    private void functionMenu(){

        lblReception = new JLabel ("RESERVATION");
        lblReception.setBounds(330, 60, 800, 60);
        lblReception.setFont(new Font ("Arial Black", Font.BOLD, 50));
        add(lblReception);

    }

    private void sidePanel() {
        sidePan = new JPanel ();
        sidePan.setBounds (0, 0, 300, 800);
        sidePan.setLayout(null);
        sidePan.setBackground(Color.decode("#222222"));
        add(sidePan);
        
        lblHotel = new JLabel ("HOTEL");
        lblHotel.setBounds(10, 10, 800, 50);
        lblHotel.setFont(new Font ("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidePan.add(lblHotel);
        
        lblManagement = new JLabel ("MANAGEMENT");
        lblManagement.setBounds(10, 50, 800, 50);
        lblManagement.setFont(new Font ("Arial Black", Font.BOLD, 30));
        lblManagement.setForeground(Color.WHITE);
        sidePan.add(lblManagement);
        
        btnHomePage = new JButton ("Home Page");
        btnHomePage.setBounds(0, 160, 300, 50);
        btnHomePage.setBackground(Color.decode("#222222"));
        btnHomePage.setForeground(Color.WHITE);
        btnHomePage.setFont(new Font ("Arial Black", Font.BOLD, 18));
        
        btnHomePage.setBorderPainted(false);
        btnHomePage.setFocusPainted(false);
        btnHomePage.setBorder(null);
        btnHomePage.addActionListener(this);
        sidePan.add(btnHomePage);
        
        btnSideDash = new JButton ("Dashboard");
        btnSideDash.setBounds(0, 230, 300, 50);
        btnSideDash.setBackground(Color.decode("#222222"));
        btnSideDash.setForeground(Color.WHITE);
        btnSideDash.setFont(new Font ("Arial Black", Font.BOLD, 18));
        
        btnSideDash.setBorderPainted(false);
        btnSideDash.setFocusPainted(false);
        btnSideDash.setBorder(null);
        btnSideDash.addActionListener(this);
        sidePan.add(btnSideDash);
        
        btnSideRegister = new JButton ("Guest Register");
        btnSideRegister.setBounds(0, 300, 300, 50);
        btnSideRegister.setBackground(Color.decode("#222222"));
        btnSideRegister.setForeground(Color.WHITE);
        btnSideRegister.setFont(new Font ("Arial Black", Font.BOLD, 18));
        
        btnSideRegister.setBorderPainted(false);
        btnSideRegister.setFocusPainted(false);
        btnSideRegister.setBorder(null);
        btnSideRegister.addActionListener(this);
        sidePan.add(btnSideRegister);
        
        btnSideRoomBooking = new JButton ("Room Booking");
        btnSideRoomBooking.setBounds(0, 370, 300, 50);
        btnSideRoomBooking.setBackground(Color.decode("#222222"));
        btnSideRoomBooking.setForeground(Color.WHITE);
        btnSideRoomBooking.setFont(new Font ("Arial Black", Font.BOLD, 18));
        
        btnSideRoomBooking.setBorderPainted(false);
        btnSideRoomBooking.setFocusPainted(false);
        btnSideRoomBooking.setBorder(null);
        btnSideRoomBooking.addActionListener(this);
        sidePan.add(btnSideRoomBooking);
        
        btnSidePay = new JButton ("Payment");
        btnSidePay.setBounds(0, 440, 300, 50);
        btnSidePay.setBackground(Color.decode("#222222"));
        btnSidePay.setForeground(Color.WHITE);
        btnSidePay.setFont(new Font ("Arial Black", Font.BOLD, 18));
        
        btnSidePay.setBorderPainted(false);
        btnSidePay.setFocusPainted(false);
        btnSidePay.setBorder(null);
        btnSidePay.addActionListener(this);
        sidePan.add(btnSidePay);
        
        btnSideReserve = new JButton ("Reservation");
        btnSideReserve.setBounds(0, 510, 300, 50);
        btnSideReserve.setBackground(Color.decode("#FFFFFF"));
        btnSideReserve.setForeground(Color.BLACK);
        btnSideReserve.setFont(new Font ("Arial Black", Font.BOLD, 18));
        
        btnSideReserve.setBorderPainted(false);
        btnSideReserve.setFocusPainted(false);
        btnSideReserve.setBorder(null);
        btnSideReserve.addActionListener(this);
        sidePan.add(btnSideReserve);

        btnSideCancel = new JButton ("Cancel Booking");
        btnSideCancel.setBounds(0, 580, 300, 50);
        btnSideCancel.setBackground(Color.decode("#222222"));
        btnSideCancel.setForeground(Color.WHITE);
        btnSideCancel.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideCancel.setBorderPainted(false);
        btnSideCancel.setFocusPainted(false);
        btnSideCancel.setBorder(null);
        btnSideCancel.addActionListener(this);
        sidePan.add(btnSideCancel);
    }

    private void topPanel() {
        topPan = new JPanel ();
        topPan.setBounds (300, 0, 900, 60);
        topPan.setLayout(null);
        topPan.setBackground(Color.decode("#FFFFFF"));
        add(topPan);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        lblDate = new JLabel(dateFormat.format(new Date()));
        lblDate.setBounds(20, 15, 300, 30);
        lblDate.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblDate.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblDate);
    }

    private void reservationPan(){

        dashboardPanel = new JPanel();
        dashboardPanel.setBounds(300, 130, 900, 520);
        dashboardPanel.setLayout(null);
        dashboardPanel.setBackground(Color.decode("#F5F5F5"));
        add(dashboardPanel);

        recentPanel = new JPanel();
        recentPanel.setBounds(20, 20, 860, 470);
        recentPanel.setBackground(Color.WHITE);
        recentPanel.setLayout(null);
        recentPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#DDDDDD")));
        dashboardPanel.add(recentPanel);

        lblRecent = new JLabel("All Reservations");
        lblRecent.setBounds(15, 10, 400, 30);
        lblRecent.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblRecent.setForeground(Color.decode("#333333"));
        recentPanel.add(lblRecent);

        String[] columns = {"Res ID", "Guest Name", "Room #", "Room Type", "Status", "Check-In"};
        reservationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReservations = new JTable(reservationTableModel);
        tblReservations.setRowHeight(26);
        tblReservations.setFont(new Font("Arial", Font.PLAIN, 13));
        tblReservations.getTableHeader().setFont(new Font("Arial Black", Font.BOLD, 13));
        tblReservations.getTableHeader().setBackground(Color.decode("#222222"));
        tblReservations.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tblReservations);
        scrollPane.setBounds(10, 50, 840, 350);
        recentPanel.add(scrollPane);

        JLabel lblTotalLabel = new JLabel("Total Reservations:");
        lblTotalLabel.setBounds(10, 420, 150, 25);
        lblTotalLabel.setFont(new Font("Arial", Font.BOLD, 13));
        recentPanel.add(lblTotalLabel);

        lblTotalReservations = new JLabel(String.valueOf(reservationData.length));
        lblTotalReservations.setBounds(160, 420, 100, 25);
        lblTotalReservations.setFont(new Font("Arial Black", Font.BOLD, 13));
        lblTotalReservations.setForeground(Color.decode("#5A3FB8"));
        recentPanel.add(lblTotalReservations);

        btnRefresh = new JButton("Refresh Data");
        btnRefresh.setBounds(720, 415, 130, 35);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(Color.decode("#222222"));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(this);
        recentPanel.add(btnRefresh);

        btnCancelSelected = new JButton("Cancel Selected");
        btnCancelSelected.setBounds(580, 415, 130, 35);
        btnCancelSelected.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelSelected.setBackground(Color.decode("#C0392B"));
        btnCancelSelected.setForeground(Color.WHITE);
        btnCancelSelected.addActionListener(this);
        recentPanel.add(btnCancelSelected);
    }

    public void setReservationData(Object[][] data){
        this.reservationData = data;
        refreshDisplay();
    }

    private void refreshDisplay(){
        reservationTableModel.setRowCount(0);
        for (Object[] row : reservationData) {
            reservationTableModel.addRow(row);
        }
        lblTotalReservations.setText(String.valueOf(reservationData.length));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == btnHomePage){
            dispose();
            new HomePage().setVisible(true);
        }else if(e.getSource() == btnSideRegister){
            dispose();
            new RegisterGuestPanel().setVisible(true);
        }else if(e.getSource() == btnSideRoomBooking){
            dispose();
            new RoomBookingPanel().setVisible(true);
        }else if(e.getSource() == btnSidePay){
            dispose();
            new RecordPaymentPanel().setVisible(true);
        }else if (e.getSource() == btnSideDash) {
            dispose();
            new ReceptionistDashboard().setVisible(true);
        }else if(e.getSource() == btnSideCancel){
            dispose();
            new CancelBookingPanel().setVisible(true);
        }else if(e.getSource() == btnCancelSelected){
            dispose();
            new CancelBookingPanel().setVisible(true);
        }else if(e.getSource() == btnRefresh){
            refreshDisplay();
        }

    }

}