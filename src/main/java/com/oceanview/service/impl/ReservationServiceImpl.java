
package com.oceanview.service.impl;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.service.EmailService;
import com.oceanview.service.ReservationService;
import com.oceanview.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ReservationServiceImpl implements ReservationService {
    private final ReservationDAO reservationDAO;
    private final EmailService emailService;

    public ReservationServiceImpl(ReservationDAO reservationDAO, EmailService emailService) {
        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO");
        this.emailService = emailService;
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        validate(reservation);

        boolean overlap = reservationDAO.hasOverlappingReservation(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        if (overlap) {
            throw new IllegalArgumentException("Room is not available for the selected dates.");
        }

        if (reservation.getStatus() == null) {
            reservation.setStatus(Reservation.ReservationStatus.PENDING);
        }

        Reservation saved = reservationDAO.save(reservation);
        if (emailService != null) {
            emailService.sendReservationAlert(saved);
        }
        return saved;
    }

    @Override
    public Reservation getReservationDetails(Long reservationId) {
        ValidationUtil.requirePositive(reservationId, "Reservation ID");
        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }
        return reservation;
    }

    @Override
    public List<Reservation> listReservations(LocalDate from, LocalDate to) {
        ValidationUtil.requireNonNull(from, "From date");
        ValidationUtil.requireNonNull(to, "To date");
        ValidationUtil.ensureDateRange(from, to);
        return reservationDAO.listByDateRange(from, to);
    }

    private void validate(Reservation reservation) {
        ValidationUtil.requireNonNull(reservation, "Reservation");
        ValidationUtil.requirePositive(reservation.getGuestId(), "Guest id");
        ValidationUtil.requirePositive(reservation.getRoomId(), "Room id");
        ValidationUtil.requirePositive(reservation.getNumberOfGuests(), "Number of guests");
        ValidationUtil.requireNonNull(reservation.getCheckInDate(), "Check-in date");
        ValidationUtil.requireNonNull(reservation.getCheckOutDate(), "Check-out date");
        ValidationUtil.ensureDateRange(reservation.getCheckInDate(), reservation.getCheckOutDate());
    }
}