package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import java.util.ArrayList;
import java.util.List;

public class RoomService {
    private static final List<String[]> rooms = new ArrayList<>();
    void addRoom(String roomNumber, String roomType, String rate, String capacity, String status, String floor) {
    rooms.add(new String[]{roomNumber, roomType, rate, capacity, status, floor});
}
    void updateRoom(String roomNumber, String roomType, String rate, String capacity, String status) {
        for (String[] room : rooms) {
            if (room[0].equals(roomNumber)) {
                room[1] = roomType; room[2] = rate; room[3] = capacity; room[4] = status; return;
            }
        }
        throw new IllegalStateException("Room not found: " + roomNumber);
    }
    List<String[]> findAllRooms(){
        return rooms;
    }
    boolean roomExists(String roomNumber){
        for (String[] r : rooms) if (r[0].equals(roomNumber)) return true; return false;
    }
    String[] findRoomByNumber(String roomNumber){
        for (String[] r : rooms) if (r[0].equals(roomNumber)) return r; return null;
    }
}
