package com.mycompany.guestinterface;
public class HotelReservationApp {

    public static void main(String[] args) {     
    SessionManager.init();
      
    new LoginFrame().setVisible(true);
    }
}