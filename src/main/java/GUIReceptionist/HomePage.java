package GUIReceptionist;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class HomePage extends JFrame implements ActionListener{
        
    private JButton btnRoomBooking, btnRegistration, btnDashBoard, btnPayment, btnReservation, btnCancelBooking,
            btnSideDash, btnSideRegister, btnSideCheckIn, btnSideCheckOut, btnSidePay, btnSideReserve, btnHomePage;
    private JLabel lblReception, lblReceptionName;
    private String receptionistName;
    
    
    public HomePage(){
        this.receptionistName = "";
        functionMenu();
        
        setSize(1200,700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Hotel Reservation System - Receptionist Homepage");
    }
    private void functionMenu(){
        
        lblReception = new JLabel ("RECEPTION");
        lblReception.setBounds(380, 60, 800, 60);
        lblReception.setFont(new Font ("Arial Black", Font.BOLD, 70));
        add(lblReception);
        
        lblReceptionName = new JLabel (receptionistName);
        lblReceptionName.setBounds(500, 110, 800, 60);
        lblReceptionName.setFont(new Font ("Arial Black", Font.BOLD, 20));
        add(lblReceptionName);
        
        btnDashBoard = new JButton ("DASHBOARD");
        btnDashBoard.setBounds(160, 180, 245, 200);
        btnDashBoard.setBackground(Color.BLACK);
        btnDashBoard.setForeground(Color.WHITE);
        btnDashBoard.setFont(new Font ("Arial Black", Font.BOLD, 20));
        
        btnDashBoard.setBorderPainted(false);
        btnDashBoard.setFocusPainted(false);
        btnDashBoard.setBorder(null);
        btnDashBoard.addActionListener(this);
        add(btnDashBoard);
        
        btnRegistration = new JButton ("GUEST REGISTRATION");
        btnRegistration.setBounds(480, 180, 245, 200);
        btnRegistration.setBackground(Color.BLACK);
        btnRegistration.setForeground(Color.WHITE);
        btnRegistration.setFont(new Font ("Arial Black", Font.BOLD, 15));
        
        btnRegistration.setBorderPainted(false);
        btnRegistration.setFocusPainted(false);
        btnRegistration.setBorder(null);
        btnRegistration.addActionListener(this);
        add(btnRegistration);
        
        btnRoomBooking = new JButton ("ROOM BOOKING");
        btnRoomBooking.setBounds(800, 180, 245, 200);
        btnRoomBooking.setBackground(Color.BLACK);
        btnRoomBooking.setForeground(Color.WHITE);
        btnRoomBooking.setFont(new Font ("Arial Black", Font.BOLD, 20));
        
        btnRoomBooking.setBorderPainted(false);
        btnRoomBooking.setFocusPainted(false);
        btnRoomBooking.setBorder(null);
        btnRoomBooking.addActionListener(this);
        add(btnRoomBooking);
        
        btnPayment = new JButton ("PAYMENT ");
        btnPayment.setBounds(160, 400, 245, 200);
        btnPayment.setBackground(Color.BLACK);
        btnPayment.setForeground(Color.WHITE);
        btnPayment.setFont(new Font ("Arial Black", Font.BOLD, 20));
        
        btnPayment.setBorderPainted(false);
        btnPayment.setFocusPainted(false);
        btnPayment.setBorder(null);
        btnPayment.addActionListener(this);
        add(btnPayment);
        
        btnReservation = new JButton ("RESERVATION");
        btnReservation.setBounds(480, 400, 245, 200);
        btnReservation.setBackground(Color.BLACK);
        btnReservation.setForeground(Color.WHITE);
        btnReservation.setFont(new Font ("Arial Black", Font.BOLD, 20));
        
        btnReservation.setBorderPainted(false);
        btnReservation.setFocusPainted(false);
        btnReservation.setBorder(null);
        btnReservation.addActionListener(this);
        add(btnReservation);

        btnCancelBooking = new JButton ("CANCEL BOOKING");
        btnCancelBooking.setBounds(800, 400, 245, 200);
        btnCancelBooking.setBackground(Color.BLACK);
        btnCancelBooking.setForeground(Color.WHITE);
        btnCancelBooking.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnCancelBooking.setBorderPainted(false);
        btnCancelBooking.setFocusPainted(false);
        btnCancelBooking.setBorder(null);
        btnCancelBooking.addActionListener(this);
        add(btnCancelBooking);
    }

    public void setReceptionistName(String name){
        this.receptionistName = name;
        lblReceptionName.setText(name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == btnDashBoard){
            dispose();
            new ReceptionistDashboard().setVisible(true);
        }else if(e.getSource() == btnRegistration){
            dispose();
            new RegisterGuestPanel().setVisible(true);
        }else if(e.getSource() == btnRoomBooking){
            dispose();
            new RoomBookingPanel().setVisible(true);
        }else if(e.getSource() == btnPayment){
            dispose();
            new RecordPaymentPanel().setVisible(true);
        }else if(e.getSource() == btnReservation){
            dispose();
            new ViewReservationPanel().setVisible(true);
        }else if(e.getSource() == btnCancelBooking){
            dispose();
            new CancelBookingPanel().setVisible(true);
        }
        
    }
    
}