package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private static final List<String[]> payments = new ArrayList<>();
    void addPayment(String guestName, String roomNumber, double amount) {
    String today = java.time.LocalDate.now().toString();
    payments.add(new String[]{guestName, roomNumber, String.valueOf(amount), today});
}
    List<String[]> findAllPayments() { return payments; }
    double totalRevenue() {
        double total = 0;
        for (String[] p : payments) total += Double.parseDouble(p[2]);
        return total;
    }
}
