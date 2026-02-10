package com.oceanview.dao;

import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.util.DBConnection;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class BillDAOImplTest {


    @Test
    public void findById_existingBill_returnsBill() throws Exception {
        long reservationId = insertReservation();
        long billId = insertBill(reservationId);
        assertTrue(billId > 0);

        BillDAO dao = new BillDAOImpl();
        Bill bill = dao.findById(billId);

        assertNotNull(bill);
        assertEquals(Long.valueOf(billId), bill.getBillId());
    }

    private long insertReservation() throws Exception {
        long guestId = insertGuest();
        long roomId = insertRoom();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO reservations (guestId, roomId, check_in_date, check_out_date, number_of_guests, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, guestId);
            ps.setLong(2, roomId);
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.of(2026, 2, 10)));
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.of(2026, 2, 12)));
            ps.setInt(5, 2);
            ps.setString(6, "PENDING");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    private long insertGuest() throws Exception {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO guests (guestPassport, guestName, guestEmail, guestAddress, guestContact, guestNIC) " +
                             "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "P" + uniqueId);
            ps.setString(2, "Test Guest");
            ps.setString(3, "guest" + uniqueId + "@test.com");
            ps.setString(4, "Galle");
            ps.setString(5, "0770000000");
            ps.setString(6, "NIC" + uniqueId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    private long insertRoom() throws Exception {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO rooms (roomType, ratePerNight, max_occupancy, roomStatus) " +
                             "VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "SINGLE");
            ps.setBigDecimal(2, new java.math.BigDecimal("100.00"));
            ps.setInt(3, 2);
            ps.setString(4, "AVAILABLE");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    private long insertBill(long reservationId) throws Exception {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO bills (reservationId, room_charges, service_charges, tax_amount, discount_amount, total_amount, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, reservationId);
            ps.setBigDecimal(2, new java.math.BigDecimal("200.00"));
            ps.setBigDecimal(3, new java.math.BigDecimal("20.00"));
            ps.setBigDecimal(4, new java.math.BigDecimal("10.00"));
            ps.setBigDecimal(5, new java.math.BigDecimal("5.00"));
            ps.setBigDecimal(6, new java.math.BigDecimal("225.00"));
            ps.setString(7, "PENDING");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            throw new IllegalStateException("No billId generated");
            }
        }
    }
