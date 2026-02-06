
package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import java.util.Objects;

public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = Objects.requireNonNull(reservationService, "reservationService");
    }

    public Reservation addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation is required.");
        }
        return reservationService.addReservation(reservation);
    }
}