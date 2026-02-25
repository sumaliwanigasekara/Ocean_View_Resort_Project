package com.oceanview.strategy.reservation;

import com.oceanview.model.Reservation;

public class RoomAvailabilityValidationStrategy implements ReservationValidationStrategy {
    @Override
    public void validate(Reservation reservation, ReservationValidationContext context) {
        boolean overlap = context.getReservationDAO().hasOverlappingReservation(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
        if (overlap) {
            throw new IllegalArgumentException("Room is not available for the selected dates.");
        }
    }
}
