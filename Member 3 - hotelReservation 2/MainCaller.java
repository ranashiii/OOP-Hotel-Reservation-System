package hotelreservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 *
 *
 * @author bened
 */
public class MainCaller {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("  HOTEL RESERVATION SYSTEM - TEST");
        System.out.println("========================================\n");

        System.out.println("Choose test mode:");
        System.out.println("[1] I will input the values manually");
        System.out.println("[2] Use fixed hardcoded values");
        System.out.print("\nEnter choice (1 or 2): ");
        String choice = scanner.nextLine().trim();

        System.out.println();

        if (choice.equals("1")) {
            runManualTest();
        } else if (choice.equals("2")) {
            runHardcodedTest();
        } else {
            System.out.println("Invalid choice. Running hardcoded test by default.\n");
            runHardcodedTest();
        }
    }

    
    // OPTION 1 MANUAL INPUT
    static void runManualTest() {
        System.out.println("========================================");
        System.out.println("  MANUAL INPUT MODE");
        System.out.println("========================================\n");

        // Room 
        System.out.println("--- Room Input ---");
        Room room = new Room();

        System.out.print("Room ID (number): ");
        room.setRoomId(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Room Number (e.g. 101): ");
        room.setRoomNumber(scanner.nextLine().trim());

        System.out.println("Room Type options: Single Standard, Double Standard, Double Deluxe, Suite Deluxe");
        System.out.print("Room Type: ");
        room.setRoomType(scanner.nextLine().trim());

        System.out.print("Floor (number): ");
        room.setFloor(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Capacity (1-10): ");
        room.setCapacity(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Price Per Night (PHP): ");
        room.setPricePerNight(Double.parseDouble(scanner.nextLine().trim()));

        System.out.print("Amenities (e.g. WiFi, TV): ");
        room.setAmenities(scanner.nextLine().trim());

        System.out.println("Status options: Available, Occupied, Maintenance, Cleaning");
        System.out.print("Status: ");
        room.setStatus(scanner.nextLine().trim());

        System.out.println("\nRoom created:");
        System.out.println(room.toString());
        System.out.println("Is Available:   " + room.isAvailable());
        System.out.println("Is Occupied:    " + room.isOccupied());
        System.out.println("Is Maintenance: " + room.isMaintenance());
        System.out.println("Is Cleaning:    " + room.isCleaning());

        // Reservation 
        System.out.println("\n--- Reservation Input ---");
        Reservation res = new Reservation();

        System.out.print("Reservation ID (number): ");
        res.setReservationId(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Guest ID (number): ");
        res.setGuestId(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Room ID (number): ");
        res.setRoomId(Integer.parseInt(scanner.nextLine().trim()));

        System.out.print("Check-in Date (YYYY-MM-DD): ");
        res.setCheckInDate(LocalDate.parse(scanner.nextLine().trim()));

        System.out.print("Check-out Date (YYYY-MM-DD): ");
        res.setCheckOutDate(LocalDate.parse(scanner.nextLine().trim()));

        System.out.print("Number of Guests: ");
        res.setNumberOfGuests(Integer.parseInt(scanner.nextLine().trim()));

        
        System.out.println("Status options: Confirmed, Checked-In, Checked-Out, Cancelled");
        System.out.print("Reservation Status: ");
        res.setReservationStatus(scanner.nextLine().trim());

        // Auto calculate nights
        long nights = DateUtil.calculateNights(res.getCheckInDate(), res.getCheckOutDate());
        res.setNumberOfNights((int) nights);

        // Auto calculate prices using room price
        double subtotal = CurrencyUtil.calculateSubtotal(room.getPricePerNight(), nights);
        double tax      = CurrencyUtil.calculateTax(subtotal);
        double total    = CurrencyUtil.calculateFinalTotal(subtotal, tax, 0);
        res.setRoomRate(room.getPricePerNight());
        res.setTotalPrice(subtotal);
        res.setDiscountApplied(0);
        res.setFinalTotal(total);
        res.setReservationDate(LocalDate.now());

        System.out.println("\nReservation created:");
        System.out.println(res.toString());
        System.out.println("Subtotal: " + CurrencyUtil.formatCurrency(subtotal));
        System.out.println("Tax:      " + CurrencyUtil.formatCurrency(tax));
        System.out.println("Total:    " + CurrencyUtil.formatCurrency(total));

        // Payment 
        System.out.println("\n--- Payment Input ---");
        Payment pay = new Payment();

        System.out.print("Payment ID (number): ");
        pay.setPaymentId(Integer.parseInt(scanner.nextLine().trim()));

        pay.setReservationId(res.getReservationId()); // auto-linked to reservation

        System.out.println("Payment Method options: Cash, Credit Card, E-Wallet");
        System.out.print("Payment Method: ");
        pay.setPaymentMethod(scanner.nextLine().trim());

        System.out.print("Amount to Pay (PHP): ");
        double amountPaid = Double.parseDouble(scanner.nextLine().trim());
        pay.setPaymentAmount(amountPaid);
        pay.setPaymentStatus("Completed");
        pay.setPaymentDate(LocalDate.now());
        pay.setPaymentTime(LocalTime.now());

        double change = CurrencyUtil.calculateChange(amountPaid, total);

        System.out.println("\nPayment created:");
        System.out.println(pay.toString());
        System.out.println("Change:   " + CurrencyUtil.formatCurrency(change));

        // DateUtil 
        System.out.println("\n--- DateUtil Results ---");
        System.out.println("Nights stayed:    " + nights);
        System.out.println("Refund % if cancelled now: " +
                (DateUtil.getRefundPercentage(res.getCheckInDate(), LocalDate.now()) * 100) + "%");
        System.out.println("Within refund window: " +
                DateUtil.isWithinRefundWindow(res.getCheckInDate(), LocalDate.now()));

        System.out.println("\n========================================");
        System.out.println("  MANUAL TEST DONE");
        System.out.println("========================================");
        
    }

    // OPTION 2 HARDCODED FIXED VALUES
    static void runHardcodedTest() {
        System.out.println("========================================");
        System.out.println("  HARDCODED TEST MODE");
        System.out.println("========================================\n");

        // Room 
        System.out.println("--- Room Model Test ---");
        Room room = new Room();
        room.setRoomId(1);
        room.setRoomNumber("101");
        room.setRoomType("Double Deluxe");
        room.setFloor(2);
        room.setCapacity(2);
        room.setPricePerNight(5000.00);
        room.setAmenities("WiFi, TV, Air Conditioning");
        room.setStatus("Available");
        System.out.println(room.toString());
        System.out.println("Is Available:   " + room.isAvailable());
        System.out.println("Is Occupied:    " + room.isOccupied());
        System.out.println("Is Maintenance: " + room.isMaintenance());
        System.out.println("Is Cleaning:    " + room.isCleaning());

        // Reservation 
        System.out.println("\n--- Reservation Model Test ---");
        Reservation res = new Reservation();
        res.setReservationId(1001);
        res.setGuestId(5);
        res.setRoomId(1);
        res.setCheckInDate(LocalDate.of(2025, 6, 1));
        res.setCheckOutDate(LocalDate.of(2025, 6, 6));
        res.setNumberOfGuests(2);
        res.setNumberOfNights(5);
        res.setRoomRate(5000.00);
        res.setTotalPrice(25000.00);
        res.setDiscountApplied(0);
        res.setFinalTotal(28000.00);
        res.setReservationStatus("Confirmed");
        res.setReservationDate(LocalDate.now());
        System.out.println(res.toString());

        // Payment 
        System.out.println("\n--- Payment Model Test ---");
        Payment pay = new Payment();
        pay.setPaymentId(1);
        pay.setReservationId(1001);
        pay.setPaymentAmount(28000.00);
        pay.setPaymentMethod("Cash");
        pay.setPaymentStatus("Completed");
        pay.setPaymentDate(LocalDate.now());
        pay.setPaymentTime(LocalTime.now());
        System.out.println(pay.toString());
        System.out.println("Change (paid 30000): " + CurrencyUtil.formatCurrency(
                CurrencyUtil.calculateChange(30000, 28000)));

        // DateUtil 
        System.out.println("\n--- DateUtil Test ---");
        LocalDate checkIn  = LocalDate.of(2025, 6, 1);
        LocalDate checkOut = LocalDate.of(2025, 6, 6);
        System.out.println("Nights (expect 5):                  " + DateUtil.calculateNights(checkIn, checkOut));
        System.out.println("Refund % 10 days before (expect 1.0): " + DateUtil.getRefundPercentage(LocalDate.now().plusDays(10), LocalDate.now()));
        System.out.println("Refund % 7 days before  (expect 0.9): " + DateUtil.getRefundPercentage(LocalDate.now().plusDays(7),  LocalDate.now()));
        System.out.println("Refund % 3 days before  (expect 0.0): " + DateUtil.getRefundPercentage(LocalDate.now().plusDays(3),  LocalDate.now()));
        System.out.println("Refund amount 10 days   (expect 28000): " + DateUtil.calculateRefundAmount(28000, LocalDate.now().plusDays(10), LocalDate.now()));
        System.out.println("Late fee 12PM (expect 500.0):  " + DateUtil.calculateCheckoutFee(LocalTime.of(12, 0)));
        
        System.out.println("Late fee 2PM  (expect 1000.0): " + DateUtil.calculateCheckoutFee(LocalTime.of(14, 0)));
        System.out.println("Late fee 4PM  (expect 2000.0): " + DateUtil.calculateCheckoutFee(LocalTime.of(16, 0)));
        System.out.println("Late fee 10AM (expect 0.0):    " + DateUtil.calculateCheckoutFee(LocalTime.of(10, 0)));

        // CurrencyUtil 
        System.out.println("\n--- CurrencyUtil Test ---");
        double subtotal = CurrencyUtil.calculateSubtotal(5000.00, 5);
        double tax      = CurrencyUtil.calculateTax(subtotal);
        double total    = CurrencyUtil.calculateFinalTotal(subtotal, tax, 0);
        System.out.println("Subtotal (expect PHP 25,000.00): " + CurrencyUtil.formatCurrency(subtotal));
        System.out.println("Tax      (expect PHP  3,000.00): " + CurrencyUtil.formatCurrency(tax));
        System.out.println("Total    (expect PHP 28,000.00): " + CurrencyUtil.formatCurrency(total));
        System.out.println("Valid amount 500 (expect true):  " + CurrencyUtil.isValidAmount(500));
        System.out.println("Valid amount 0   (expect false): " + CurrencyUtil.isValidAmount(0));

        System.out.println("\n========================================");
        System.out.println("  ALL TESTS DONE");
        System.out.println("========================================");
    }
}