
package com.oceanview.service.impl;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationServiceImpl implements ReservationService {
    private final ReservationDAO reservationDAO;

    public ReservationServiceImpl(ReservationDAO reservationDAO) {
        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO");
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        validate(reservation);
        if (reservation.getStatus() == null) {
            reservation.setStatus(Reservation.ReservationStatus.PENDING);
        }
        return reservationDAO.save(reservation);
    }


    @Override
    public Reservation getReservationDetails(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException("Reservation ID is required.");
        }
        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }
        return reservation;
    }

    private void validate(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation is required.");
        }
        if (reservation.getGuestId() == null || reservation.getRoomId() == null) {
            throw new IllegalArgumentException("Guest and room are required.");
        }
        if (reservation.getNumberOfGuests() <= 0) {
            throw new IllegalArgumentException("Number of guests must be positive.");
        }
        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out must be after check-in.");
        }
    }
}