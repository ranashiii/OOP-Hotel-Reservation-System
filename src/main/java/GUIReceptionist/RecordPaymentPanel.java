package GUIReceptionist;

import DAO.PaymentDAO;
import DAO.ReservationDAO;
import Models.Payment;
import Models.Reservation;
import Utilities.Constants;
import Utilities.CurrencyUtil;
import Utilities.MessageBox;
import Utilities.ValidationUtil;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * RecordPaymentPanel - Payment recording interface for receptionists
 * 
 * Allows receptionists to process payments with support for Cash, Credit Card,
 * and E-Wallet methods with proper validation and receipt generation.
 */
public class RecordPaymentPanel extends JPanel {
    
    private PaymentDAO paymentDAO;
    private ReservationDAO reservationDAO;
    
    private JTextField txtReservationId, txtPaymentAmount;
    private JLabel lblOutstandingBalance, lblPaymentMethod, lblReceiptArea;
    private JComboBox<String> cmbPaymentMethod;
    private JButton btnSearch, btnProcessPayment, btnReset;
    private Reservation currentReservation;
    private Payment currentPayment;
    
    // Payment method specific fields
    private JPanel cardDetailsPanel, ewalletPanel;
    private JTextField txtCardNumber, txtCardHolder, txtCardExpiry, txtCardCVV;
    private JTextField txtEwalletPhone, txtEwalletOTP;
    
    public RecordPaymentPanel() {
        this.paymentDAO = new PaymentDAO();
        this.reservationDAO = new ReservationDAO();
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel paymentPanel = createPaymentPanel();
        JScrollPane scrollPane = new JScrollPane(paymentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Search Reservation"));
        
        panel.add(new JLabel("Reservation ID:"));
        txtReservationId = new JTextField();
        panel.add(txtReservationId);
        
        btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(33, 150, 243));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> searchReservation());
        panel.add(btnSearch);
        
        return panel;
    }
    
    private JPanel createPaymentPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        
        // Balance info
        JPanel balancePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setBorder(BorderFactory.createTitledBorder("Outstanding Balance"));
        balancePanel.add(new JLabel("Amount Due:"));
        lblOutstandingBalance = new JLabel("PHP 0.00");
        lblOutstandingBalance.setFont(new Font("Arial", Font.BOLD, 14));
        lblOutstandingBalance.setForeground(new Color(244, 67, 54));
        balancePanel.add(lblOutstandingBalance);
        mainPanel.add(balancePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Payment details
        JPanel detailsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));
        
        detailsPanel.add(new JLabel("Payment Method:"));
        cmbPaymentMethod = new JComboBox<>(new String[]{"Cash", "Credit Card", "E-Wallet"});
        cmbPaymentMethod.addActionListener(e -> updatePaymentMethodFields());
        detailsPanel.add(cmbPaymentMethod);
        
        detailsPanel.add(new JLabel("Payment Amount (PHP):"));
        txtPaymentAmount = new JTextField();
        detailsPanel.add(txtPaymentAmount);
        
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Card details (initially hidden)
        cardDetailsPanel = createCardDetailsPanel();
        cardDetailsPanel.setVisible(false);
        mainPanel.add(cardDetailsPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // E-Wallet details (initially hidden)
        ewalletPanel = createEWalletPanel();
        ewalletPanel.setVisible(false);
        mainPanel.add(ewalletPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Receipt display
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBackground(Color.WHITE);
        receiptPanel.setBorder(BorderFactory.createTitledBorder("Receipt Preview"));
        
        lblReceiptArea = new JLabel("Receipt will appear here after payment");
        lblReceiptArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        lblReceiptArea.setVerticalAlignment(JLabel.TOP);
        receiptPanel.add(new JScrollPane(lblReceiptArea), BorderLayout.CENTER);
        
        mainPanel.add(receiptPanel);
        
        return mainPanel;
    }
    
    private JPanel createCardDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Credit Card Details"));
        
        panel.add(new JLabel("Card Number (13-19 digits):"));
        txtCardNumber = new JTextField();
        panel.add(txtCardNumber);
        
        panel.add(new JLabel("Card Holder Name:"));
        txtCardHolder = new JTextField();
        panel.add(txtCardHolder);
        
        panel.add(new JLabel("Expiry Date (MM/YY):"));
        txtCardExpiry = new JTextField();
        panel.add(txtCardExpiry);
        
        panel.add(new JLabel("CVV (3-4 digits):"));
        txtCardCVV = new JTextField();
        panel.add(txtCardCVV);
        
        return panel;
    }
    
    private JPanel createEWalletPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("E-Wallet Details"));
        
        panel.add(new JLabel("Phone Number (09XXXXXXXXX):"));
        txtEwalletPhone = new JTextField();
        panel.add(txtEwalletPhone);
        
        panel.add(new JLabel("OTP (6 digits):"));
        txtEwalletOTP = new JTextField();
        panel.add(txtEwalletOTP);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnProcessPayment = new JButton("Process Payment");
        btnProcessPayment.setBackground(new Color(76, 175, 80));
        btnProcessPayment.setForeground(Color.WHITE);
        btnProcessPayment.setEnabled(false);
        btnProcessPayment.addActionListener(e -> processPayment());
        panel.add(btnProcessPayment);
        
        btnReset = new JButton("Reset");
        btnReset.setBackground(new Color(158, 158, 158));
        btnReset.setForeground(Color.WHITE);
        btnReset.addActionListener(e -> resetForm());
        panel.add(btnReset);
        
        return panel;
    }
    
    private void updatePaymentMethodFields() {
        String method = (String) cmbPaymentMethod.getSelectedItem();
        cardDetailsPanel.setVisible("Credit Card".equals(method));
        ewalletPanel.setVisible("E-Wallet".equals(method));
        revalidate();
        repaint();
    }
    
    private void searchReservation() {
        try {
            String resIdStr = txtReservationId.getText().trim();
            
            if (resIdStr.isEmpty()) {
                MessageBox.showError("Validation Error", "Please enter reservation ID");
                return;
            }
            
            int resId = Integer.parseInt(resIdStr);
            currentReservation = reservationDAO.getReservationById(resId);
            
            if (currentReservation == null) {
                MessageBox.showError("Not Found", "Reservation not found");
                return;
            }
            
            BigDecimal balance = currentReservation.getFinalTotal();
            lblOutstandingBalance.setText(CurrencyUtil.formatCurrency(balance));
            btnProcessPayment.setEnabled(true);
            
        } catch (NumberFormatException e) {
            MessageBox.showError("Error", "Invalid reservation ID format");
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to search reservation: " + e.getMessage());
        }
    }
    
    private void processPayment() {
        try {
            if (currentReservation == null) {
                MessageBox.showError("Error", "No reservation selected");
                return;
            }
            
            String paymentAmountStr = txtPaymentAmount.getText().trim();
            if (paymentAmountStr.isEmpty()) {
                MessageBox.showError("Validation Error", "Please enter payment amount");
                return;
            }
            
            BigDecimal paymentAmount = new BigDecimal(paymentAmountStr);
            if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                MessageBox.showError("Validation Error", "Payment amount must be greater than 0");
                return;
            }
            
            String paymentMethod = (String) cmbPaymentMethod.getSelectedItem();
            String paymentDetails = "";
            
            if ("Credit Card".equals(paymentMethod)) {
                paymentDetails = validateAndGetCardDetails();
                if (paymentDetails == null) return;
            } else if ("E-Wallet".equals(paymentMethod)) {
                paymentDetails = validateAndGetEWalletDetails();
                if (paymentDetails == null) return;
            }
            
            Payment payment = new Payment();
            payment.setReservationId(currentReservation.getReservationId());
            payment.setPaymentAmount(paymentAmount.doubleValue());
            payment.setPaymentMethod(paymentMethod);
            payment.setPaymentTypeDetails(paymentDetails);
            payment.setPaymentStatus(Constants.PAYMENT_STATUS_COMPLETED);
            payment.setPaymentDate(LocalDate.now());
            payment.setTransactionId("TXN" + System.currentTimeMillis());
            
            paymentDAO.createPayment(payment);
            
            BigDecimal newBalance = currentReservation.getFinalTotal().subtract(paymentAmount);
            if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
                currentReservation.setReservationStatus(Constants.RESERVATION_STATUS_CHECKED_OUT);
            }
            reservationDAO.updateReservation(currentReservation);
            
            generateReceipt(payment, paymentAmount, newBalance);
            MessageBox.showInfo("Success", "Payment recorded successfully!");
            
            resetForm();
            
        } catch (NumberFormatException e) {
            MessageBox.showError("Error", "Invalid payment amount format");
        } catch (Exception e) {
            MessageBox.showError("Error", "Failed to process payment: " + e.getMessage());
        }
    }
    
    private String validateAndGetCardDetails() {
        String cardNumber = txtCardNumber.getText().trim();
        String cardHolder = txtCardHolder.getText().trim();
        String expiry = txtCardExpiry.getText().trim();
        String cvv = txtCardCVV.getText().trim();
        
        if (cardNumber.isEmpty() || cardHolder.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            MessageBox.showError("Validation Error", "All card details are required");
            return null;
        }
        
        if (!ValidationUtil.validateCreditCardNumber(cardNumber)) {
            MessageBox.showError("Validation Error", "Invalid card number (must be 13-19 digits)");
            return null;
        }
        
        if (!ValidationUtil.validateName(cardHolder)) {
            MessageBox.showError("Validation Error", "Invalid card holder name");
            return null;
        }
        
        if (!isValidCardExpiry(expiry)) {
            MessageBox.showError("Validation Error", "Invalid expiry date (use MM/YY format)");
            return null;
        }
        
        if (!ValidationUtil.validateCVV(cvv)) {
            MessageBox.showError("Validation Error", "Invalid CVV (must be 3-4 digits)");
            return null;
        }
        
        return "Card: " + cardNumber.substring(cardNumber.length() - 4);
    }
    
    private boolean isValidCardExpiry(String expiry) {
        if (expiry == null || !expiry.matches("^\\d{2}/\\d{2}$")) {
            return false;
        }
        
        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        
        if (month < 1 || month > 12) {
            return false;
        }
        
        int currentYear = LocalDate.now().getYear() % 100;
        int currentMonth = LocalDate.now().getMonthValue();
        
        if (year < currentYear) {
            return false;
        }
        
        return !(year == currentYear && month < currentMonth);
    }
    
    private String validateAndGetEWalletDetails() {
        String phone = txtEwalletPhone.getText().trim();
        String otp = txtEwalletOTP.getText().trim();
        
        if (phone.isEmpty() || otp.isEmpty()) {
            MessageBox.showError("Validation Error", "Phone number and OTP are required");
            return null;
        }
        
        if (!ValidationUtil.validatePhoneNumber(phone)) {
            MessageBox.showError("Validation Error", "Invalid phone number format");
            return null;
        }
        
        if (!ValidationUtil.validateOTP(otp)) {
            MessageBox.showError("Validation Error", "OTP must be 6 digits");
            return null;
        }
        
        return "E-Wallet: " + phone;
    }
    
    private void generateReceipt(Payment payment, BigDecimal amount, BigDecimal balance) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("==========================================<br>");
        receipt.append("            PAYMENT RECEIPT<br>");
        receipt.append("==========================================<br>");
        receipt.append("Receipt #: ").append(payment.getTransactionId()).append("<br>");
        receipt.append("Date: ").append(LocalDate.now()).append("<br>");
        receipt.append("==========================================<br>");
        receipt.append("Reservation ID: ").append(payment.getReservationId()).append("<br>");
        receipt.append("Payment Amount: ").append(CurrencyUtil.formatCurrency(amount)).append("<br>");
        receipt.append("Payment Method: ").append(payment.getPaymentMethod()).append("<br>");
        receipt.append("Details: ").append(payment.getPaymentTypeDetails()).append("<br>");
        receipt.append("==========================================<br>");
        receipt.append("Remaining Balance: ").append(CurrencyUtil.formatCurrency(balance)).append("<br>");
        receipt.append("Status: ").append(balance.compareTo(BigDecimal.ZERO) <= 0 ? "PAID" : "PARTIAL");
        receipt.append("<br>==========================================<br>");
        
        lblReceiptArea.setText("<html>" + receipt.toString() + "</html>");
    }
    
    private void resetForm() {
        txtReservationId.setText("");
        txtPaymentAmount.setText("");
        txtCardNumber.setText("");
        txtCardHolder.setText("");
        txtCardExpiry.setText("");
        txtCardCVV.setText("");
        txtEwalletPhone.setText("");
        txtEwalletOTP.setText("");
        lblOutstandingBalance.setText("PHP 0.00");
        lblReceiptArea.setText("Receipt will appear here after payment");
        btnProcessPayment.setEnabled(false);
        currentReservation = null;
        cmbPaymentMethod.setSelectedIndex(0);
        updatePaymentMethodFields();
    }
}
