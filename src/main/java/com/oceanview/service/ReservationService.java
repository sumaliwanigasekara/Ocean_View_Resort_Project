package com.oceanview.service;

import com.oceanview.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    Reservation addReservation(Reservation reservation);

    Reservation getReservationDetails(Long reservationId);

    List<Reservation> listReservations(LocalDate from, LocalDate to);
}