package com.oceanview.dao;

import com.oceanview.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {
    Reservation save(Reservation reservation);

    Reservation findById(Long reservationId);

    boolean hasOverlappingReservation(Long roomId, LocalDate checkIn, LocalDate checkOut);

    List<Reservation> listByDateRange(LocalDate from, LocalDate to);
}