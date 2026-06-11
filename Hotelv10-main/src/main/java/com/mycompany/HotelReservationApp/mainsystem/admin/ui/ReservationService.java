package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private static final List<String[]> reservations = new ArrayList<>();
    void addReservation(String guestName, String roomNumber, String checkIn, String checkOut) {
        reservations.add(new String[]{guestName, roomNumber, checkIn, checkOut, "ACTIVE"});
    }
    void cancelReservation(int index) {
        if (index >= 0 && index < reservations.size()) reservations.get(index)[4] = "CANCELLED";
    }
    List<String[]> findAllReservations() { return reservations; }
    int countAllReservations()           { return reservations.size(); }
    int countActiveReservations() {
        int c = 0;
        for (String[] r : reservations) if (r[4].equals("ACTIVE")) c++;
        return c;
    }
}
