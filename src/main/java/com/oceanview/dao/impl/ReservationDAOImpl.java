package com.oceanview.dao.impl;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {
    private static final String FIND_BY_ID_SQL =
            "SELECT reservationId, guestId, roomId, check_in_date, check_out_date, number_of_guests, status, total_amount " +
                    "FROM reservations WHERE reservationId = ?";

    @Override
    public Reservation findById(Long reservationId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setLong(1, reservationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapReservation(rs);
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservations (guestId, roomId, check_in_date, check_out_date, number_of_guests, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, reservation.getGuestId());
            ps.setLong(2, reservation.getRoomId());
            ps.setDate(3, java.sql.Date.valueOf(reservation.getCheckInDate()));
            ps.setDate(4, java.sql.Date.valueOf(reservation.getCheckOutDate()));
            ps.setInt(5, reservation.getNumberOfGuests());
            ps.setString(6, reservation.getStatus().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    reservation.setReservationId(rs.getLong(1));
                }
            }
            return reservation;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean hasOverlappingReservation(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE roomId = ? AND status IN ('PENDING','CONFIRMED','CHECKED_IN') " +
                "AND NOT (check_out_date <= ? OR check_in_date >= ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, roomId);
            ps.setDate(2, java.sql.Date.valueOf(checkIn));
            ps.setDate(3, java.sql.Date.valueOf(checkOut));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Reservation> listByDateRange(LocalDate from, LocalDate to) {
        String sql = "SELECT reservationId, guestId, roomId, check_in_date, check_out_date, number_of_guests, status, total_amount " +
                "FROM reservations WHERE check_in_date >= ? AND check_out_date <= ? ORDER BY check_in_date";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
            return reservations;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public long countByStatus(Reservation.ReservationStatus status) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE status = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Reservation> findBetweenDates(LocalDate start, LocalDate end) {
        String sql = "SELECT reservationId, guestId, roomId, check_in_date, check_out_date, number_of_guests, status, total_amount " +
                "FROM reservations WHERE check_in_date >= ? AND check_out_date <= ? ORDER BY check_in_date";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
            return reservations;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Reservation> findTodayCheckIns() {
        String sql = "SELECT reservationId, guestId, roomId, check_in_date, check_out_date, number_of_guests, status, total_amount " +
                "FROM reservations WHERE check_in_date = CURDATE()";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
            return reservations;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Reservation> findTodayCheckOuts() {
        String sql = "SELECT reservationId, guestId, roomId, check_in_date, check_out_date, number_of_guests, status, total_amount " +
                "FROM reservations WHERE check_out_date = CURDATE()";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(mapReservation(rs));
            }
            return reservations;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Reservation mapReservation(ResultSet rs) throws Exception {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getLong("reservationId"));
        reservation.setGuestId(rs.getLong("guestId"));
        reservation.setRoomId(rs.getLong("roomId"));
        reservation.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
        reservation.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
        reservation.setNumberOfGuests(rs.getInt("number_of_guests"));
        reservation.setStatus(Reservation.ReservationStatus.valueOf(rs.getString("status")));
        reservation.setTotalAmount(rs.getBigDecimal("total_amount"));
        return reservation;
    }
}
