package com.oceanview.strategy.reservation;

import com.oceanview.model.Reservation;

public interface ReservationValidationStrategy {
    void validate(Reservation reservation, ReservationValidationContext context);
}
