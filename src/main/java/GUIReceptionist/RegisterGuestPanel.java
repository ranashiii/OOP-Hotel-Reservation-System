package GUIReceptionist;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class RegisterGuestPanel extends JFrame implements ActionListener{

    private JButton btnSideDash, btnSideRegister, btnSideRoomBooking, btnSidePay, btnSideReserve, btnHomePage,
            btnSubmit, btnClear;
    private JLabel lblReception, lblHotel, lblManagement, lblDate;
    private JPanel sidePan, topPan, formPanel;
    private JTextField txtFirstName, txtMiddleName, txtLastName, txtDOB, txtNationality, txtAddress,
            txtPhone, txtEmail, txtIDNumber;
    private JComboBox<String> cmbIDType;


    RegisterGuestPanel(){
        sidePanel();
        functionMenu();
        topPanel();
        formPanel();

        setSize(1200,700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Hotel Reservation System - Receptionist Register Guest");
    }
    private void functionMenu(){

        lblReception = new JLabel ("GUEST REGISTRATION");
        lblReception.setBounds(350, 60, 800, 60);
        lblReception.setFont(new Font ("Arial Black", Font.BOLD, 40));
        add(lblReception);

    }

    private void sidePanel() {
        sidePan = new JPanel ();
        sidePan.setBounds (0, 0, 300, 800);
        sidePan.setLayout(null);
        sidePan.setBackground(Color.decode("#222222"));
        add(sidePan);

        lblHotel = new JLabel ("HOTEL");
        lblHotel.setBounds(10, 10, 800, 50);
        lblHotel.setFont(new Font ("Arial Black", Font.BOLD, 40));
        lblHotel.setForeground(Color.WHITE);
        sidePan.add(lblHotel);

        lblManagement = new JLabel ("MANAGEMENT");
        lblManagement.setBounds(10, 50, 800, 50);
        lblManagement.setFont(new Font ("Arial Black", Font.BOLD, 30));
        lblManagement.setForeground(Color.WHITE);
        sidePan.add(lblManagement);

        btnHomePage = new JButton ("Home Page");
        btnHomePage.setBounds(0, 160, 300, 50);
        btnHomePage.setBackground(Color.decode("#222222"));
        btnHomePage.setForeground(Color.WHITE);
        btnHomePage.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnHomePage.setBorderPainted(false);
        btnHomePage.setFocusPainted(false);
        btnHomePage.setBorder(null);
        btnHomePage.addActionListener(this);
        sidePan.add(btnHomePage);

        btnSideDash = new JButton ("Dashboard");
        btnSideDash.setBounds(0, 230, 300, 50);
        btnSideDash.setBackground(Color.decode("#222222"));
        btnSideDash.setForeground(Color.WHITE);
        btnSideDash.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideDash.setBorderPainted(false);
        btnSideDash.setFocusPainted(false);
        btnSideDash.setBorder(null);
        btnSideDash.addActionListener(this);
        sidePan.add(btnSideDash);

        btnSideRegister = new JButton ("Guest Register");
        btnSideRegister.setBounds(0, 300, 300, 50);
        btnSideRegister.setBackground(Color.decode("#FFFFFF"));
        btnSideRegister.setForeground(Color.BLACK);
        btnSideRegister.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideRegister.setBorderPainted(false);
        btnSideRegister.setFocusPainted(false);
        btnSideRegister.setBorder(null);
        btnSideRegister.addActionListener(this);
        sidePan.add(btnSideRegister);

        btnSideRoomBooking = new JButton ("Room Booking");
        btnSideRoomBooking.setBounds(0, 370, 300, 50);
        btnSideRoomBooking.setBackground(Color.decode("#222222"));
        btnSideRoomBooking.setForeground(Color.WHITE);
        btnSideRoomBooking.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideRoomBooking.setBorderPainted(false);
        btnSideRoomBooking.setFocusPainted(false);
        btnSideRoomBooking.setBorder(null);
        btnSideRoomBooking.addActionListener(this);
        sidePan.add(btnSideRoomBooking);

        btnSidePay = new JButton ("Payment");
        btnSidePay.setBounds(0, 440, 300, 50);
        btnSidePay.setBackground(Color.decode("#222222"));
        btnSidePay.setForeground(Color.WHITE);
        btnSidePay.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSidePay.setBorderPainted(false);
        btnSidePay.setFocusPainted(false);
        btnSidePay.setBorder(null);
        btnSidePay.addActionListener(this);
        sidePan.add(btnSidePay);

        btnSideReserve = new JButton ("Reservation");
        btnSideReserve.setBounds(0, 510, 300, 50);;
        btnSideReserve.setBackground(Color.decode("#222222"));
        btnSideReserve.setForeground(Color.WHITE);
        btnSideReserve.setFont(new Font ("Arial Black", Font.BOLD, 18));

        btnSideReserve.setBorderPainted(false);
        btnSideReserve.setFocusPainted(false);
        btnSideReserve.setBorder(null);
        btnSideReserve.addActionListener(this);
        sidePan.add(btnSideReserve);

    }
    private void topPanel() {
        topPan = new JPanel ();
        topPan.setBounds (300, 0, 900, 60);
        topPan.setLayout(null);
        topPan.setBackground(Color.decode("#FFFFFF"));
        add(topPan);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        lblDate = new JLabel(dateFormat.format(new Date()));
        lblDate.setBounds(20, 15, 300, 30);
        lblDate.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblDate.setForeground(Color.decode("#5A3FB8"));
        topPan.add(lblDate);
    }
    private void formPanel() {

        formPanel = new JPanel();
        formPanel.setBounds(320, 130, 850, 520);
        formPanel.setLayout(null);
        formPanel.setBackground(Color.WHITE);
        add(formPanel);


        JLabel lbl1 = new JLabel("First Name:");
        lbl1.setBounds(30, 15, 220, 25);
        lbl1.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl1);

        txtFirstName = new JTextField();
        txtFirstName.setBounds(280, 10, 530, 35);
        txtFirstName.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtFirstName);


        JLabel lbl2 = new JLabel("Middle Name:");
        lbl2.setBounds(30, 57, 220, 25);
        lbl2.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl2);

        txtMiddleName = new JTextField();
        txtMiddleName.setBounds(280, 52, 530, 35);
        txtMiddleName.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtMiddleName);


        JLabel lbl3 = new JLabel("Last Name:");
        lbl3.setBounds(30, 99, 220, 25);
        lbl3.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl3);

        txtLastName = new JTextField();
        txtLastName.setBounds(280, 94, 530, 35);
        txtLastName.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtLastName);


        JLabel lbl4 = new JLabel("Date of Birth (YYYY-MM-DD):");
        lbl4.setBounds(30, 141, 270, 25);
        lbl4.setFont(new Font("Arial Black", Font.BOLD, 15));
        formPanel.add(lbl4);

        txtDOB = new JTextField();
        txtDOB.setBounds(280, 136, 530, 35);
        txtDOB.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtDOB);


        JLabel lbl5 = new JLabel("Nationality:");
        lbl5.setBounds(30, 183, 220, 25);
        lbl5.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl5);

        txtNationality = new JTextField();
        txtNationality.setBounds(280, 178, 530, 35);
        txtNationality.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtNationality);


        JLabel lbl6 = new JLabel("Address:");
        lbl6.setBounds(30, 225, 220, 25);
        lbl6.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl6);

        txtAddress = new JTextField();
        txtAddress.setBounds(280, 220, 530, 35);
        txtAddress.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtAddress);


        JLabel lbl7 = new JLabel("Phone Number:");
        lbl7.setBounds(30, 267, 220, 25);
        lbl7.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl7);

        txtPhone = new JTextField();
        txtPhone.setBounds(280, 262, 530, 35);
        txtPhone.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtPhone);


        JLabel lbl8 = new JLabel("Email:");
        lbl8.setBounds(30, 309, 220, 25);
        lbl8.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl8);

        txtEmail = new JTextField();
        txtEmail.setBounds(280, 304, 530, 35);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtEmail);


        JLabel lbl9 = new JLabel("ID Document Type:");
        lbl9.setBounds(30, 351, 220, 25);
        lbl9.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl9);

        cmbIDType = new JComboBox<>(new String[]{"Select ID", "Passport", "Driver's License", "National ID", "Postal ID"});
        cmbIDType.setBounds(280, 346, 530, 35);
        cmbIDType.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(cmbIDType);


        JLabel lbl10 = new JLabel("ID Document Number:");
        lbl10.setBounds(30, 393, 220, 25);
        lbl10.setFont(new Font("Arial Black", Font.BOLD, 16));
        formPanel.add(lbl10);

        txtIDNumber = new JTextField();
        txtIDNumber.setBounds(280, 388, 530, 35);
        txtIDNumber.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(txtIDNumber);


        btnSubmit = new JButton("Create Guest Account");
        btnSubmit.setBounds(220, 450, 250, 40);
        btnSubmit.setBackground(Color.decode("#222222"));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Arial Black", Font.BOLD, 13));
        btnSubmit.setBorderPainted(false);
        btnSubmit.setFocusPainted(false);
        btnSubmit.addActionListener(this);
        formPanel.add(btnSubmit);

        btnClear = new JButton("CLEAR");
        btnClear.setBounds(550, 450, 120, 40);
        btnClear.setBackground(Color.decode("#D32F2F"));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFont(new Font("Arial Black", Font.BOLD, 13));
        btnClear.setBorderPainted(false);
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(this);
        formPanel.add(btnClear);
    }

    private void clearForm(){
        txtFirstName.setText("");
        txtMiddleName.setText("");
        txtLastName.setText("");
        txtDOB.setText("");
        txtNationality.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtIDNumber.setText("");
        cmbIDType.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnSubmit) {

            String first = txtFirstName.getText();
            String last = txtLastName.getText();
            String dob = txtDOB.getText();
            String nationality = txtNationality.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            String idNum = txtIDNumber.getText();
            String idType = cmbIDType.getSelectedItem().toString();

            if (first.isEmpty() || last.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter the guest's full name.");
                return;
            }

            if (dob.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter the guest's date of birth.");
                return;
            }

            if (nationality.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter the guest's nationality.");
                return;
            }

            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter the guest's address.");
                return;
            }

            if (!(phone.startsWith("09") || phone.startsWith("+63"))) {
                JOptionPane.showMessageDialog(this, "Phone should start with 09 or +63.");
                return;
            }

            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Check the email address.");
                return;
            }

            if (idType.equals("Select ID")) {
                JOptionPane.showMessageDialog(this, "Select an ID document type.");
                return;
            }

            if (idNum.length() < 5) {
                JOptionPane.showMessageDialog(this, "Enter a valid ID document number.");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Guest account created for " + first + " " + last,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            clearForm();
        }

        else if (e.getSource() == btnClear) {
            clearForm();
        }

        else if (e.getSource() == btnHomePage) {
            dispose();
            new HomePage().setVisible(true);
        } else if (e.getSource() == btnSideDash) {
            dispose();
            new ReceptionistDashboard().setVisible(true);
        } else if (e.getSource() == btnSideRoomBooking) {
            dispose();
            new RoomBookingPanel().setVisible(true);
        } else if (e.getSource() == btnSidePay) {
            dispose();
            new RecordPaymentPanel().setVisible(true);
        } else if (e.getSource() == btnSideReserve) {
            dispose();
            new ViewReservationPanel().setVisible(true);
        }
    }
}