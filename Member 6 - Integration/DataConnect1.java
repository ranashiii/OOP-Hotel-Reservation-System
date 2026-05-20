package com.mycompany.guestinterface;

import java.util.ArrayList;
public class DataConnect {



   //Benedict
public static String reservationID;
public static String guestName;
public static String roomNumber;
public static String roomType;
public static int pax;
public static String checkInDate;
public static String checkOutDate;

   //Marla
public static String paymentMethod;
public static String accountNumber;
public static double totalAmount;

    //Lance
public static void clear() {
 reservationID = null;
 guestName = null;
 roomNumber = null;
 roomType = null;
 pax = 0;
 checkInDate = null;
 checkOutDate = null;
 paymentMethod = null;
 accountNumber = null;
 totalAmount = 0;
    }
}