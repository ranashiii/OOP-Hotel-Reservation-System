# Hotel Reservation System
The Hotel Reservation System is a comprehensive Java application designed to manage all aspects of hotel operations. This system enables hotel staff and guests to efficiently handle room reservations, guest management, payment processing, and reception operations. The application provides a user-friendly interface for different user roles including guests, receptionists, and administrators.

## Project Description
- This project implements a complete hotel management solution using Object-Oriented Programming principles. The system manages guest information, room inventory, reservations, payments, and staff operations. 
- It provides different interfaces for guests to make reservations, receptionists to handle check-in and check-out procedures, and administrators to monitor the entire system.
- The application uses a modular architecture with separate components for each business function, making it easy to maintain and extend. All data is managed in memory using Java collections, with comprehensive logging capabilities to track system activities.

## Contributions
This is an academic project completed by 6 team members. Name of members are as follows:

Member 1: Arciaga, R. (Lead/Improvements)
Member 2: Sular, L. (Guest Interface)
Member 3: Rodriguez, B. (Data Models & Utilities)
Member 4: Bartolome, M. (Reception System)
Member 5: Graban, G. (Admin Dashboard)
Member 6: Peñaflor, N. (Integration)

[CURRENT STATUS OF THE PROJECT]   >>>   50%

------------------------------------------------

## How to Use the Application
Upon launching the application, you will see the login screen. Select your desired role from the dropdown menu and enter your credentials. The system supports three user roles: Guest, Receptionist, and Admin.

- For Guest users:
Enter username and password to access the guest interface where you can view reservations and manage your profile.

- For Receptionist users:
Access the reception management system to handle check-in and check-out procedures, view current reservations, and manage room status updates.

- For Admin users:
Access the administrative dashboard to view system statistics, financial reports, and system configurations.

## Default Login Credentials
The system includes default credentials for testing purposes:

- Guest role: Username is guest, Password is guest123
- Receptionist role: Username is receptionist, Password is rec123
- Admin role: Username is admin, Password is admin123

!!! All passwords must be at least 6 characters long. !!!

## Features by Module

A. Guest Management:
Add and maintain guest profiles with contact information and identity verification
Track VIP guests and manage guest classification
Search guests by email or phone number
View guest reservation history and total spending
Monitor and upgrade guest status based on loyalty

B. Room Management:
Maintain comprehensive room inventory with classification by type and capacity
Track room amenities including WiFi, television, air conditioning, and balconies
Monitor real-time room availability and occupancy status
Calculate occupancy rates for business analysis
Search available rooms by type or guest capacity requirements

C. Reservation System:
Create and manage hotel reservations with flexible date selection
Validate reservation details and dates
Track reservation status including confirmed, cancelled, or completed
Calculate room charges based on number of nights and room rate
Manage special requests and guest preferences

D. Payment Processing:
Process multiple payment methods including credit cards, debit cards, cash, and bank transfers
Securely mask card information for privacy protection
Track payment status for each reservation
Generate revenue reports and track pending payments
Record transaction references for audit purposes

E. Reception Operations:
Manage receptionist schedules and availability
Process guest check-in and check-out procedures
Update room status in real-time
Track receptionist performance metrics
Monitor check-in and check-out times

F. Administration and Monitoring:
View comprehensive system statistics including guest count and occupancy rates
Generate financial reports showing total revenue and pending payments
Monitor payment transactions by status
Access system logs for audit trails
Configure system settings and perform database backups
