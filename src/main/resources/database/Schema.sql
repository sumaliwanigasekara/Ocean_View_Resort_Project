CREATE DATABASE IF NOT EXISTS oceanview_resort;
USE oceanview_resort;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS ReservationDetails;
DROP TABLE IF EXISTS Bills;
DROP TABLE IF EXISTS Reservations;
DROP TABLE IF EXISTS Rooms;
DROP TABLE IF EXISTS Guests;
DROP TABLE IF EXISTS Users;
SET FOREIGN_KEY_CHECKS = 1;

-- ================= USERS =================
CREATE TABLE Users (
                       userId BIGINT AUTO_INCREMENT PRIMARY KEY,
                       userName VARCHAR(100) NOT NULL,
                       userRole ENUM('ADMIN','MANAGER','RECEPTIONIST') NOT NULL,
                       userEmail VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       userStatus ENUM('ACTIVE','INACTIVE','SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ================= GUESTS =================
CREATE TABLE Guests (
                        guestId BIGINT AUTO_INCREMENT PRIMARY KEY,
                        guestPassport VARCHAR(50) UNIQUE,
                        guestName VARCHAR(100) NOT NULL,
                        guestAddress VARCHAR(255),
                        guestContact VARCHAR(20) NOT NULL,
                        userId BIGINT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NULL,
                        FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE SET NULL
);

-- ================= ROOMS =================
CREATE TABLE Rooms (
                       roomId BIGINT AUTO_INCREMENT PRIMARY KEY,
                       roomType ENUM('SINGLE','DOUBLE','TWIN','SUITE','DELUXE','PRESIDENTIAL') NOT NULL,
                       ratePerNight DECIMAL(10,2) NOT NULL,
                       roomStatus ENUM('AVAILABLE','OCCUPIED','MAINTENANCE','RESERVED') NOT NULL DEFAULT 'AVAILABLE',
                       imageUrl VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL
);

-- ================= RESERVATIONS =================
CREATE TABLE Reservations (
                              reservationId BIGINT AUTO_INCREMENT PRIMARY KEY,
                              reservationNumber VARCHAR(20) NOT NULL UNIQUE,
                              reservationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              userId BIGINT NOT NULL,
                              guestId BIGINT NOT NULL,
                              totalAmount DECIMAL(10,2) DEFAULT 0.00,
                              status ENUM('PENDING','CONFIRMED','CHECKED_IN','CHECKED_OUT','CANCELLED','NO_SHOW')
                                                            NOT NULL DEFAULT 'PENDING',
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NULL,
                              FOREIGN KEY (userId) REFERENCES Users(userId),
                              FOREIGN KEY (guestId) REFERENCES Guests(guestId)
);

-- ================= BILLS =================
CREATE TABLE Bills (
                       billId BIGINT AUTO_INCREMENT PRIMARY KEY,
                       billNumber VARCHAR(20) NOT NULL UNIQUE,
                       billValue DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                       billStatus ENUM('PENDING','PAID','PARTIALLY_PAID','CANCELLED','REFUNDED')
                           NOT NULL DEFAULT 'PENDING',
                       billDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       reservationId BIGINT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL,
                       FOREIGN KEY (reservationId) REFERENCES Reservations(reservationId)
);

-- ================= RESERVATION DETAILS =================
CREATE TABLE ReservationDetails (
                                    reservationDetailId BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    costPerNight DECIMAL(10,2) NOT NULL,
                                    checkInDate DATE NOT NULL,
                                    checkOutDate DATE NOT NULL,
                                    numberOfNights INT
                                        AS (DATEDIFF(checkOutDate, checkInDate)) STORED,
                                    totalRoomCost DECIMAL(10,2)
                                        AS (costPerNight * DATEDIFF(checkOutDate, checkInDate)) STORED,
                                    reservationId BIGINT NOT NULL,
                                    roomId BIGINT NOT NULL,
                                    billId BIGINT,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP NULL,
                                    CONSTRAINT chk_dates CHECK (checkOutDate > checkInDate),
                                    FOREIGN KEY (reservationId) REFERENCES Reservations(reservationId) ON DELETE CASCADE,
                                    FOREIGN KEY (roomId) REFERENCES Rooms(roomId),
                                    FOREIGN KEY (billId) REFERENCES Bills(billId) ON DELETE SET NULL
);
