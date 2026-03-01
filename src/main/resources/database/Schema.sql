CREATE DATABASE IF NOT EXISTS oceanview_resort;
USE oceanview_resort;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS guests;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS users (
                       userId BIGINT AUTO_INCREMENT PRIMARY KEY,
                       userName VARCHAR(100) NOT NULL,
                       userRole ENUM('MANAGER','RECEPTIONIST') NOT NULL,
                       userEmail VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       userStatus ENUM('ACTIVE','INACTIVE','SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


INSERT INTO users (userName, userRole, userEmail, password, userStatus)
VALUES ('Manager', 'MANAGER', 'manager@oceanview.com', 'secret', 'ACTIVE');

INSERT INTO users (userName, userRole, userEmail, password, userStatus)
VALUES ('Receptionist', 'RECEPTIONIST', 'reception@oceanview.com', 'recep123', 'ACTIVE');


CREATE TABLE IF NOT EXISTS guests (
                        guestId BIGINT AUTO_INCREMENT PRIMARY KEY,
                        guestPassport VARCHAR(50) UNIQUE,
                        guestName VARCHAR(100) NOT NULL,
                        guestEmail VARCHAR(100) NOT NULL UNIQUE,
                        guestAddress VARCHAR(255),
                        guestContact VARCHAR(20) NOT NULL,
                        guestNIC VARCHAR(20) NOT NULL UNIQUE ,
                        userId BIGINT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NULL,
                        FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS rooms (
                       roomId BIGINT AUTO_INCREMENT PRIMARY KEY,
                       roomType ENUM('SINGLE','DOUBLE','TWIN','SUITE','DELUXE','PRESIDENTIAL') NOT NULL,
                       ratePerNight DECIMAL(10,2) NOT NULL,
                       max_occupancy INT NOT NULL DEFAULT 2,
                       roomStatus ENUM('AVAILABLE','OCCUPIED','MAINTENANCE','RESERVED') NOT NULL DEFAULT 'AVAILABLE',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL
);


INSERT INTO rooms (rooms.roomType, ratePerNight, max_occupancy, roomStatus) VALUES
  ('SINGLE', 120.00, 1, 'AVAILABLE'),
  ('DOUBLE', 180.00, 2, 'AVAILABLE'),
  ('TWIN', 170.00, 2, 'AVAILABLE'),
  ('SUITE', 320.00, 3, 'AVAILABLE'),
  ('DELUXE', 280.00, 3, 'AVAILABLE'),
  ('PRESIDENTIAL', 650.00, 4, 'AVAILABLE');

CREATE TABLE IF NOT EXISTS reservations (
                                            reservationId BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            guestId BIGINT NOT NULL,
                                            roomId BIGINT NOT NULL,
                                            check_in_date DATE NOT NULL,
                                            check_out_date DATE NOT NULL,
                                            number_of_guests INT NOT NULL DEFAULT 1,
                                            status ENUM('PENDING', 'CONFIRMED', 'CHECKED_IN', 'CHECKED_OUT', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
                                            total_amount DECIMAL(10, 2),
                                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                            updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
                                            FOREIGN KEY (guestId) REFERENCES guests(guestId) ON DELETE RESTRICT,
                                            FOREIGN KEY (roomId) REFERENCES rooms(roomId) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS bills (
                                     billId BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     reservationId BIGINT NOT NULL,
                                     room_charges DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
                                     service_charges DECIMAL(10, 2) DEFAULT 0.00,
                                     tax_amount DECIMAL(10, 2) DEFAULT 0.00,
                                     discount_amount DECIMAL(10, 2) DEFAULT 0.00,
                                     total_amount DECIMAL(10, 2) NOT NULL,
                                     status ENUM('PENDING', 'PAID', 'PARTIALLY_PAID', 'CANCELLED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
                                     payment_method VARCHAR(50),
                                     paid_at TIMESTAMP NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
                                     FOREIGN KEY (reservationId) REFERENCES reservations(reservationId) ON DELETE RESTRICT
);


