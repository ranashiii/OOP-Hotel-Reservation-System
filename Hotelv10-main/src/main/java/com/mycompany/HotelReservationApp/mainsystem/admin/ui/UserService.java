package com.mycompany.HotelReservationApp.mainsystem.admin.ui;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final List<String[]> users = new ArrayList<>();
    void addUser(String name, String email, String password) {
        users.add(new String[]{name, email, password, "ACTIVE"});
    }
    void removeUser(String email){
        users.removeIf(u -> u[1].equals(email));
    }
    List<String[]> findAllUsers(){
        return users;
    }
    boolean userExists(String email)
    {
        for (String[] u : users) if (u[1].equals(email)) return true; return false;
    }
    boolean isValidEmail(String email){
        return email.contains("@") && email.contains(".");
    }
}
