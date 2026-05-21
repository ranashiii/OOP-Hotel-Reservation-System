package com.mycompany.guestinterface;
public class SessionManager 
{


private static String currentUser;
private static String currentRole;

    // Benedict
public static void init() {
        currentUser = null;
        currentRole = null;
}
public static void login(String user, String role) {
        currentUser = user;
        currentRole  = role;
 }
public static void logout() {
        currentUser = null;
        currentRole = null;
}



    // Lance
public static boolean isLoggedIn() {
        return currentUser !=null;         
}
public static String getCurrentUser() {
        return currentUser;                         
}
 public static String getRole() {
        return currentRole;            
}
public static boolean hasRole(String role) {
        return currentRole !=null && currentRole.equalsIgnoreCase(role);
}


    // Marla
public static boolean canAccessAdmin(){
        return hasRole("Admin");
}
public static boolean canAccessGuest() {
        return hasRole("Guest");
}
public static boolean canAccessReceptionist() {
        return hasRole("Receptionist");
}


}