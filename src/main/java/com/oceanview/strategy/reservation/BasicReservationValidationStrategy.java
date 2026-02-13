package com.oceanview.strategy.reservation;

import com.oceanview.model.Reservation;
import com.oceanview.util.ValidationUtil;

public class BasicReservationValidationStrategy implements ReservationValidationStrategy {
    @Override
    public void validate(Reservation reservation, ReservationValidationContext context) {
        ValidationUtil.requireNonNull(reservation, "Reservation");
        ValidationUtil.requirePositive(reservation.getGuestId(), "Guest id");
        ValidationUtil.requirePositive(reservation.getRoomId(), "Room id");
        ValidationUtil.requirePositive(reservation.getNumberOfGuests(), "Number of guests");
        ValidationUtil.requireNonNull(reservation.getCheckInDate(), "Check-in date");
        ValidationUtil.requireNonNull(reservation.getCheckOutDate(), "Check-out date");
        ValidationUtil.ensureDateRange(reservation.getCheckInDate(), reservation.getCheckOutDate());
    }
}
