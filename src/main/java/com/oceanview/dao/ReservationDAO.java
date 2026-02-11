package com.oceanview.dao;

import com.oceanview.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {
    Reservation save(Reservation reservation);

    Reservation findById(Long reservationId);

    boolean hasOverlappingReservation(Long roomId, LocalDate checkIn, LocalDate checkOut);

    List<Reservation> listByDateRange(LocalDate from, LocalDate to);

    long count();

    long countByStatus(Reservation.ReservationStatus status);

    java.util.List<Reservation> findBetweenDates(java.time.LocalDate start, java.time.LocalDate end);

    java.util.List<Reservation> findTodayCheckIns();

    java.util.List<Reservation> findTodayCheckOuts();
}