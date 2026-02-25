package com.oceanview.dao;

import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReservationDAOImplTest {

    @Test
    public void findById_existingReservation_returnsMappedReservation() throws Exception {
        long guestId = insertGuest();
        long roomId = insertRoom();
        long reservationId = insertReservation(guestId, roomId);

        ReservationDAO dao = new ReservationDAOImpl();
        Reservation reservation = dao.findById(reservationId);

        assertNotNull(reservation);
        assertEquals(Long.valueOf(reservationId), reservation.getReservationId());
        assertEquals(Long.valueOf(guestId), reservation.getGuestId());
        assertEquals(Long.valueOf(roomId), reservation.getRoomId());
    }

    private long insertGuest() throws Exception {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO guests (guestPassport, guestName, guestEmail, guestAddress, guestContact, guestNIC) " +
                             "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

            String uniqueId = String.valueOf(System.currentTimeMillis());
            ps.setString(1, "P" + uniqueId);
            ps.setString(2, "Test Guest");
            String uniqueEmail = "guest" + System.currentTimeMillis() + "@test.com";
            ps.setString(3, uniqueEmail);
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

    private long insertReservation(long guestId, long roomId) throws Exception {
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

    @Test
    public void save_validReservation_insertsAndReturnsId() throws Exception {
        long guestId = insertGuest();
        long roomId = insertRoom();

        ReservationDAO dao = new ReservationDAOImpl();
        Reservation reservation = new Reservation();
        reservation.setGuestId(guestId);
        reservation.setRoomId(roomId);
        reservation.setCheckInDate(LocalDate.of(2026, 2, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 2, 12));
        reservation.setNumberOfGuests(2);
        reservation.setStatus(Reservation.ReservationStatus.PENDING);

        Reservation saved = dao.save(reservation);

        assertNotNull(saved);
        assertNotNull(saved.getReservationId());
    }
}
