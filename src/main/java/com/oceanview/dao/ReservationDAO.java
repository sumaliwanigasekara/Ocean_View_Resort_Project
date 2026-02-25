package com.oceanview.dao;

import com.oceanview.model.Reservation;

public interface ReservationDAO {
    Reservation save(Reservation reservation);

    Reservation findById(Long reservationId);

    default boolean hasOverlappingReservation(Long roomId, java.time.LocalDate checkIn, java.time.LocalDate checkOut) {
        return false;
    }

    default java.util.List<Reservation> listByDateRange(java.time.LocalDate from, java.time.LocalDate to) {
        return java.util.List.of();
    }

    default long count() { return 0; }

    default long countByStatus(Reservation.ReservationStatus status) { return 0; }

    default java.util.List<Reservation> findBetweenDates(java.time.LocalDate start, java.time.LocalDate end) {
        return java.util.List.of();
    }

    default java.util.List<Reservation> findTodayCheckIns() {
        return java.util.List.of();
    }

    default java.util.List<Reservation> findTodayCheckOuts() {
        return java.util.List.of();
    }
}