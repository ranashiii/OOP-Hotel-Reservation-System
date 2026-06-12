DROP DATABASE IF EXISTS hotelreservationsystem;

CREATE DATABASE hotelreservationsystem
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE hotelreservationsystem;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    access_level VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_access_level (access_level),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE guests (
    guest_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    address VARCHAR(255),
    date_of_birth DATE,
    nationality VARCHAR(50),
    id_document_type VARCHAR(50),
    id_document_number VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_email (email),
    INDEX idx_phone (phone_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    floor INT NOT NULL,
    capacity INT NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    amenities TEXT,
    room_image VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Available',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_room_type (room_type),
    INDEX idx_status (status),
    INDEX idx_floor (floor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    number_of_guests INT NOT NULL,
    reservation_status VARCHAR(20) DEFAULT 'Confirmed',
    number_of_nights INT,
    room_rate DECIMAL(10,2),
    total_price DECIMAL(10,2) NOT NULL,
    discount_applied DECIMAL(10,2) DEFAULT 0,
    final_total DECIMAL(10,2),
    reservation_date DATE NOT NULL,
    notes TEXT,
    cancelled_date DATETIME,
    cancellation_reason TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (guest_id) REFERENCES guests(guest_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE CASCADE,
    INDEX idx_guest_id (guest_id),
    INDEX idx_room_id (room_id),
    INDEX idx_check_in (check_in_date),
    INDEX idx_status (reservation_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT NOT NULL,
    payment_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_type_details VARCHAR(100),
    payment_status VARCHAR(20) DEFAULT 'Pending',
    payment_date DATE,
    payment_time TIME,
    refund_amount DECIMAL(10,2) DEFAULT 0,
    refund_date DATE,
    refund_reason TEXT,
    transaction_id VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE CASCADE,
    INDEX idx_reservation_id (reservation_id),
    INDEX idx_payment_status (payment_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO users (username, password, email, access_level, is_active) VALUES
('admin', '$2a$10$SlQvG5g3OW7l0zqUMHVseuCHsNr8L5GRFaUDQS3VqJg9kYHiLF8H2', 'admin@hotelreservation.com', 'Admin', TRUE);

INSERT INTO users (username, password, email, access_level, is_active) VALUES
('receptionist', '$2a$10$SlQvG5g3OW7l0zqUMHVseuCHsNr8L5GRFaUDQS3VqJg9kYHiLF8H2', 'receptionist@hotelreservation.com', 'Receptionist', TRUE);

INSERT INTO users (username, password, email, access_level, is_active) VALUES
('guest', '$2a$10$SlQvG5g3OW7l0zqUMHVseuCHsNr8L5GRFaUDQS3VqJg9kYHiLF8H2', 'guest@hotelreservation.com', 'Guest', TRUE);

INSERT INTO guests (user_id, first_name, middle_name, last_name, email, phone_number, address, date_of_birth, nationality, id_document_type, id_document_number)
VALUES (3, 'Juan', 'Dela', 'Cruz', 'guest@hotelreservation.com', '09171234567', '123 Main Street, Manila, Philippines', '1990-05-15', 'Filipino', 'National ID', 'NI123456789');

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, status)
VALUES ('101', 'Single Standard', 1, 1, 3500.00, 'WiFi, AC, TV, Private Bathroom', 'Available');

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, status)
VALUES ('102', 'Single Standard', 1, 1, 3500.00, 'WiFi, AC, TV, Private Bathroom', 'Available');

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, status)
VALUES ('201', 'Double Standard', 2, 2, 5000.00, 'WiFi, AC, TV, King Bed, Private Bathroom', 'Available');

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, status)
VALUES ('202', 'Double Standard', 2, 2, 5000.00, 'WiFi, AC, TV, King Bed, Private Bathroom', 'Available');

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, status)
VALUES ('301', 'Double Deluxe', 3, 2, 7500.00, 'WiFi, AC, TV, King Bed, Mini Bar, Hot Tub, City View', 'Available');

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, status)
VALUES ('302', 'Double Deluxe', 3, 2, 7500.00, 'WiFi, AC, TV, King Bed, Mini Bar, Hot Tub, City View', 'Available');

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, status)
VALUES ('401', 'Suite Deluxe', 4, 4, 12000.00, 'WiFi, AC, TV, Multiple Rooms, Mini Bar, Jacuzzi, City View, Balcony', 'Available');

INSERT INTO rooms (room_number, room_type, floor, capacity, price_per_night, amenities, status)
VALUES ('402', 'Suite Deluxe', 4, 4, 12000.00, 'WiFi, AC, TV, Multiple Rooms, Mini Bar, Jacuzzi, City View, Balcony', 'Available');

SHOW TABLES;
SELECT * FROM users;
SELECT * FROM rooms;
SELECT * FROM guests;